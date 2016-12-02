/*
 * Shoddy Metaserver (main.cc)
 *
 * Created on February 5, 2007.
 *
 * This file is a part of Shoddy Battle.
 * Copyright (C) 2007  Colin Fitzpatrick
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, visit the Free Software Foundation, Inc.
 * online at http://gnu.org.
 */

#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <stdlib.h>
#include <iostream>
#include <vector>
#include <set>
#include <string>
#include <sstream>
#include <errno.h>
#include <assert.h>
#include <fcntl.h>
#include <pthread.h>

using std::string;
using std::vector;
using std::set;
using std::cout;
using std::stringstream;

#define METASERVER_REQUEST 1    // Request the list of servers.
#define METASERVER_POST 2       // Add a new server to the list.
#define METASERVER_UPDATE 3     // Update a server's entry.
#define METASERVER_REMOVE 4     // Remove a server's entry.

// Port to run the metaserver on.
#define METASERVER_PORT 9757

struct tagServer;

// List of servers.
static string g_list;
static vector<tagServer> g_servers;

// Mutex used for accessing the list of servers.
static pthread_mutex_t g_mutex;

// Mutex for the socket.
static pthread_mutex_t g_socketMutex;

// A server.
typedef struct tagServer {
    string name;            // The name of this server.
    string description;     // A brief description of the serer.
    string host;            // The host name of the server.
    int port;               // The port the server is running on.
    int clients;            // Clients this server claims to be serving.
    int maximum;            // Maximum number of clients the server can hold.
                            // This is not enforced by the metaserver.
    int code;               // Secret code for this server.
    
    // Write an int, as an int, to a stringstream.
    static void writeInt(stringstream &ss, const int n) {
        char *p = (char *)&n;
        for (int i = 0; i < sizeof(int); ++i) {
            ss << p[i];
        }
    }
    
    tagServer() {
        code = 0;
    }
    
    // Convert this server to a string.
    string getString() const {
        const char separator = (char)1;
        stringstream ss;
        ss  << name << separator
            << description << separator
            << host << separator;
        writeInt(ss, port);
        writeInt(ss, clients);
        writeInt(ss, maximum);
        return ss.str();
    }
} SERVER, *LPSERVER;

// For communication for the server posting thread.
typedef struct tagThreadParam {
    SERVER server;
    sockaddr_in from;
    socklen_t fromLen;
    int sock;
} THREAD_PARAM, *LPTHREAD_PARAM;

// Convert a list of servers to a string.
string getServerList(vector<SERVER> &servers) {
    string str;
    vector<SERVER>::const_iterator i = servers.begin();
    for (; i != servers.end(); ++i) {
        str += i->getString();
        str += '\002';
    }
    return str;
}

// Get a SERVER from a datagram.
bool getServerFromDatagram(char *pData, SERVER &server) {
    const char delimiters[] = { '\001', '\0' };
    string strings[3];
    char *start = pData;
    pData = strtok(pData, delimiters);
    int len = 0;
    for (int i = 0; pData && (i < 2); pData = strtok(NULL, delimiters), ++i) {
        strings[i] = pData;
        len += strings[i].length() + 1;
    }
    server.name = strings[0];
    server.description = strings[1];
    //server.host = strings[2];
    
    const int *const ints = (int *)(start + len);
    server.port = ints[0];
    server.clients = ints[1];
    server.maximum = ints[2];
    
    cout << "Name: " << server.name
        << "\n"
        << "Description: " << server.description
        << "\n"
        << "Host: " << server.host
        << "\n"
        << "Port: " << server.port
        << "\n"
        << "Clients: " << server.clients
        << "\n"
        << "Maximum: " << server.maximum
        << "\n";
    
    return true;
}

// Get a SERVER by name.
LPSERVER getServerByName(vector<SERVER> &servers, string name) {
    LPSERVER ret = NULL;
    pthread_mutex_lock(&g_mutex);
    vector<SERVER>::iterator i = servers.begin();
    for (; i != servers.end(); ++i) {
        if (i->name == name) {
            ret = &*i;
            break;
        }
    }
    pthread_mutex_unlock(&g_mutex);
    return ret;
}

// Trim a string.
string trim(string &s, const string &drop = " ")
{
    string r = s.erase(s.find_last_not_of(drop) + 1);
    return r.erase(0, r.find_first_not_of(drop));
}

// This function copied from the GNU C Library manual.
void
 init_sockaddr (struct sockaddr_in *name,
                const char *hostname,
                uint16_t port)
 {
   struct hostent *hostinfo;

   name->sin_family = AF_INET;
   name->sin_port = htons (port);
   hostinfo = gethostbyname (hostname);
   if (hostinfo == NULL)
     {
       fprintf (stderr, "Unknown host %s.\n", hostname);
       exit (EXIT_FAILURE);
     }
   name->sin_addr = *(struct in_addr *) hostinfo->h_addr;
 }

// Determine whether it will be possible to connect to a server.
bool isValidServer(const SERVER &server) {
    int sock = socket(AF_INET, SOCK_STREAM, 0);
    if (sock < 0)
        return false;
    fcntl(sock, F_SETFL, O_NONBLOCK | O_ASYNC);
    sockaddr_in addr;
    init_sockaddr(&addr, server.host.c_str(), server.port);
    // This call returns immediately because of the O_ASYNC flag.
    if (connect(sock, (sockaddr *)&addr, sizeof(addr)) == -1) {
        // EINPROGRESS means we've started connecting but haven't finished;
        // it is not a real error.
        if (errno != EINPROGRESS) {
            close(sock);
            return false;
        }
    }
    // Now we attempt the connection.
    fd_set set;
    FD_ZERO(&set);
    FD_SET(sock, &set);
    timeval time = { 3, 0 };
    select(FD_SETSIZE, NULL, &set, NULL, &time);
    if (select(FD_SETSIZE, NULL, &set, NULL, &time) <= 0) {
        close(sock);
        return false;
    }
    // connect() may have succeeded but this does not mean we are actually
    // connected; we'll try reading a byte, since the server should send us
    // a welcome message.
    char byte;
    read(sock, &byte, 1);
    bool valid = (select(FD_SETSIZE, NULL, &set, NULL, &time) > 0);
    close(sock);
    return valid;
}

// Start of a thread that handles a request to post a new server.
// p is an instance of THREAD_PARAM allocated on the heap; it will be freed
// by this function.
void *postServer(void *p) {
    LPTHREAD_PARAM param = (LPTHREAD_PARAM)p;
    SERVER server = param->server;
    sockaddr_in from = param->from;
    socklen_t fromLen = param->fromLen;
    int sock = param->sock;
    delete param;

    server.name = trim(server.name);
    if (server.name.length()
            && !getServerByName(g_servers, server.name)
            && isValidServer(server)) {
        while (server.code == 0) {
            FILE *file = fopen("/dev/urandom", "r");
            fread(&server.code, sizeof(int), 1, file);
            fclose(file);
        }
        pthread_mutex_lock(&g_mutex);
        g_servers.push_back(server);
        g_list = getServerList(g_servers);
        pthread_mutex_unlock(&g_mutex);
    }

    pthread_mutex_lock(&g_socketMutex);
    sendto(sock, &server.code, sizeof(int), 0, (sockaddr *)&from, fromLen);
    pthread_mutex_unlock(&g_socketMutex);
    return NULL;
}

// Prune the list of servers that can no longer be connected to.
void pruneServerList() {
    // To avoid holding onto the mutex, make a copy of the server list.
    pthread_mutex_lock(&g_mutex);
    vector<SERVER> servers = g_servers;
    pthread_mutex_unlock(&g_mutex);

    // List of servers to be removed. It is possible, but unlikely, that
    // two servers have the same code, and this thus has the potential
    // to wrongly remove a server (it is by code). However, if this happens
    // the server will relist itself with five minutes, so the harm is
    // minimal.
    set<int> removable;

    // This is done in two steps because each isValidServer() call could
    // potentially take a second, which is a long time for the metaserver
    // to be down for.
    vector<SERVER>::iterator i = servers.begin();
    for (; i != servers.end(); ++i) {
        if (!isValidServer(*i)) {
            removable.insert(i->code);
        }
    }

    // We don't need to do anything if there are no removable servers.
    if (removable.empty())
        return;

    // Now we actually remove the offending servers.
    pthread_mutex_lock(&g_mutex);
    i = g_servers.begin();
    for (; i != g_servers.end(); ++i) {
        if (removable.find(i->code) != removable.end()) {
            i = g_servers.erase(i) - 1;
        }
    }
    g_list = getServerList(g_servers);
    pthread_mutex_unlock(&g_mutex);
}

// Continually poll the listed servers to make sure they are still active.
void *pollServers(void *p) {
    while (true) {
        // Sleep for three minutes.
        sleep(60 * 3);
        
        // Prune the list.
        pruneServerList();
    }
}

// Main entry point. The port to run on should be passed on the command line.
int main(int argc, char **argv) {
    
    //freopen("metaserver.log", "w", stdout);
    
    if (fork() != 0) {
        return 0;
    }
    
    // Initialise the mutexes.
    pthread_mutex_init(&g_mutex, NULL);
    pthread_mutex_init(&g_socketMutex, NULL);
    
    pthread_mutex_lock(&g_mutex);
    g_list = getServerList(g_servers);
    pthread_mutex_unlock(&g_mutex);
    
    const int sock = socket(AF_INET, SOCK_DGRAM, 0);
    if (sock < 0) {
        cout << "Could not create a socket.\n";
        return EXIT_FAILURE;
    }
    
    sockaddr_in server;
    const int length = sizeof(server);
    bzero(&server, length);
    server.sin_family = AF_INET;
    server.sin_port = htons(METASERVER_PORT);
    if (bind(sock, (sockaddr *)&server, length) < 0) {
        cout << "Could not bind to port.\n";
        return EXIT_FAILURE;
    }
    
    // Spawn the server polling thread.
    pthread_t tid;
    pthread_create(&tid, NULL, pollServers, NULL);
    
    // Loop indefinitely.
    while (true) {
        char data[1024];
        char *pData = data;
        sockaddr_in from;
        socklen_t fromLen = sizeof(from);
        
        pthread_mutex_lock(&g_socketMutex);
        const int len = recvfrom(sock, data, 1024, 0, (sockaddr *)&from, &fromLen);
        pthread_mutex_unlock(&g_socketMutex);
        
        char host[255];
        inet_ntop(AF_INET, &from.sin_addr, host, 255);
        
        data[len] = '\0';
        cout << "Received datagram from " << host << ".\n";
        const int message = *(int *)data;
        pData += sizeof(int);
        
        switch (message) {
            case METASERVER_REQUEST: {
                pthread_mutex_lock(&g_mutex);
                string list = g_list;
                pthread_mutex_unlock(&g_mutex);
                const int len = list.length() * sizeof(char);
                pthread_mutex_lock(&g_socketMutex);
                sendto(sock, &len, sizeof(len), 0, (sockaddr *)&from, fromLen);
                
                if (len) {
                    sendto(sock, list.c_str(), len, 0, (sockaddr *)&from, fromLen);
                }
                pthread_mutex_unlock(&g_socketMutex);
            } break;
            case METASERVER_POST: {
                SERVER server;
                try {
                    server.host = host;

                    // Ugly hack.
                    if (server.host == "192.168.1.1") {
                        server.host = "official.shoddybattle.com";
                    }
                    
                    char *text = (char *)pData;
                    if (!getServerFromDatagram(text, server))
                        continue;
                    
                } catch (...) {
                    continue;
                }
                // We don't really care about the thread id (tid).
                pthread_t tid;
                LPTHREAD_PARAM param = new THREAD_PARAM;
                param->server = server;
                param->from = from;
                param->fromLen = fromLen;
                param->sock = sock;
                pthread_create(&tid, NULL, postServer, param);
            } break;
            case METASERVER_UPDATE: {

            } break;
            case METASERVER_REMOVE: {
                const int code = *(int *)pData;
                pData += sizeof(int);
                string name = pData;
                LPSERVER pServer = getServerByName(g_servers, name);
                if (pServer && ((pServer->code == code)) {
                    pthread_mutex_lock(&g_mutex);
                    vector<SERVER>::iterator i = g_servers.begin();
                    for (; i != g_servers.end(); ++i) {
                        if (&*i == pServer) {
                            g_servers.erase(i);
                            cout << "Server removed: " << name << ".\n";
                            break;
                        }
                    }
                    g_list = getServerList(g_servers);
                    pthread_mutex_unlock(&g_mutex);
                }
            } break;
        }
    }
    
    return EXIT_SUCCESS;
}

