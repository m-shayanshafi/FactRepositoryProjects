/**
 *   Stigma - Multiplayer online RPG - http://stigma.sourceforge.net
 *   Copyright (C) 2005-2009 Minions Studio
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *   
 */
package pl.org.minions.stigma.network.server;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import pl.org.minions.stigma.databases.server.PlayerLoadDB;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.globals.LoginState;
import pl.org.minions.stigma.network.Connector;
import pl.org.minions.stigma.network.messaging.NetworkMessage;
import pl.org.minions.stigma.network.messaging.NetworkMessageReceiver;
import pl.org.minions.stigma.network.messaging.NetworkMessageType;
import pl.org.minions.stigma.network.messaging.auth.LoginActorChosen;
import pl.org.minions.stigma.network.messaging.auth.LoginActorsList;
import pl.org.minions.stigma.network.messaging.auth.LoginBadVersion;
import pl.org.minions.stigma.network.messaging.auth.LoginCorrect;
import pl.org.minions.stigma.network.messaging.auth.LoginData;
import pl.org.minions.stigma.network.messaging.auth.LoginDisabledActorList;
import pl.org.minions.stigma.network.messaging.auth.LoginError;
import pl.org.minions.stigma.network.messaging.auth.LoginPasswordAccepted;
import pl.org.minions.stigma.network.messaging.auth.LoginRequest;
import pl.org.minions.stigma.network.messaging.system.CloseConnection;
import pl.org.minions.stigma.server.ServerConfig;
import pl.org.minions.stigma.server.managers.ActorManager;
import pl.org.minions.stigma.server.managers.ChatManager;
import pl.org.minions.stigma.server.managers.PlayerManager;
import pl.org.minions.stigma.server.managers.WorldManager;
import pl.org.minions.utils.Version;
import pl.org.minions.utils.logger.Log;

/**
 * Class responsible for authentication of incoming
 * connections. Starts it own thread. Also helps saving and
 * logging out players.
 */
public class Authenticator
{
    private static class BadLoginTask extends Task
    {
        public BadLoginTask(Request request)
        {
            super(request);
        }

        /** {@inheritDoc} */
        @Override
        public void execute()
        {
            getRequest().processBadLogin();
        }
    }

    private static class LoginSuccededTask extends Task
    {
        private List<Actor> availableActors;

        public LoginSuccededTask(Request request, List<Actor> availableActors)
        {
            super(request);
            this.availableActors = availableActors;
        }

        /** {@inheritDoc} */
        @Override
        public void execute()
        {
            getRequest().processLoginSucceded(availableActors);
        }

    }

    private static class NetworkMessageTask extends Task
    {
        private NetworkMessage msg;

        public NetworkMessageTask(Request request, NetworkMessage msg)
        {
            super(request);
            this.msg = msg;
        }

        /** {@inheritDoc} */
        @Override
        public void execute()
        {
            getRequest().processMessage(msg);
        }
    }

    private class Request implements
                         NetworkMessageReceiver,
                         PlayerLoadDB.InfoReceiver
    {
        private Connector conn;
        private LoginState currentState = LoginState.CONNECTED;
        private boolean requestSend;
        private LoginState nextFailureState = LoginState.LOGIN_FAILED;
        private List<Actor> availableActors;

        private String lastUserName; // for debug purposes only
        private Set<Integer> disabledActors;

        private final long creationTime = System.currentTimeMillis();

        public Request(Connector conn)
        {
            this.conn = conn;
            this.conn.setNetworkMessageReceiver(this);
            conn.networkMessage(new LoginRequest(ServerConfig.globalInstance()));
            currentState = LoginState.KEYS_EXCHANGED;
        }

        /** {@inheritDoc} */
        @Override
        public void badLogin()
        {
            enqueueTask(new BadLoginTask(this));
        }

        public boolean isTimedOut()
        {
            return System.currentTimeMillis() - creationTime > requestTimeout;
        }

        public boolean isWorking()
        {
            return conn != null;
        }

        /** {@inheritDoc} */
        @Override
        public void loginSucceded(List<Actor> availableActors)
        {
            enqueueTask(new LoginSuccededTask(this, availableActors));
        }

        public void networkMessage(NetworkMessage msg)
        {
            enqueueTask(new NetworkMessageTask(this, msg));
        }

        private void processBadLogin()
        {
            if (!requestSend)
            {
                Log.logger.error("Response received when not waiting for any (1)");
                if (conn != null)
                    conn.networkMessage(new CloseConnection());
                currentState = LoginState.LOGIN_FAILED;
                return;
            }

            if (Log.logger.isDebugEnabled())
                Log.logger.debug("Bad login/password for: " + lastUserName);

            requestSend = false;
            currentState = nextFailureState;
            if (conn != null)
            {
                conn.networkMessage(new LoginError());
                if (currentState == LoginState.LOGIN_FAILED)
                    conn.closeConnection();
            }
        }

        private void processLoginSucceded(List<Actor> availableActors)
        {
            if (!requestSend)
            {
                Log.logger.error("Response received when not waiting for any (2)");
                if (conn != null)
                    conn.networkMessage(new CloseConnection());
                currentState = LoginState.LOGIN_FAILED;
                return;
            }

            if (Log.logger.isDebugEnabled())
                Log.logger.debug("Password accepted: " + lastUserName);
            requestSend = false;
            currentState = LoginState.PASSWORD_ACCEPTED;
            this.disabledActors = new HashSet<Integer>();
            this.availableActors = new LinkedList<Actor>();
            for (Actor a : availableActors)
            {
                Actor actor;
                ActorManager manager = logonDB.getActorManager(a.getId());
                if (manager != null)
                {
                    actor = manager.getActor();
                    manager.disconnect();
                    if (Log.logger.isDebugEnabled())
                    {
                        Log.logger.debug(MessageFormat.format("User {0} logged from different connection - disconnecting other logged actors: {1}",
                                                              lastUserName,
                                                              manager.getActor()
                                                                     .getName()));
                    }
                }
                else
                    actor = a;

                if (logonDB.hasRecentlyLoggedOut(a.getId()))
                    disabledActors.add(a.getId());
                this.availableActors.add(actor);
            }

            if (conn != null)
                conn.networkMessage(new LoginPasswordAccepted());
        }

        private void processMessage(NetworkMessage msg)
        {
            if (msg == null)
                return;

            if (msg.getType() == NetworkMessageType.DISCONNECTED)
            {
                if (conn != null)
                {
                    conn.setNetworkMessageReceiver(null);
                    conn = null;
                }
                currentState = LoginState.LOGIN_FAILED;
                return;
            }

            switch (currentState)
            {
                case LOGIN_FAILED:
                case BAD_CLIENT_VERSION:
                    if (conn != null)
                    {
                        conn.setNetworkMessageReceiver(null);
                        conn = null;
                    }
                    break;

                case KEYS_EXCHANGED:
                    keysExchangedState(msg);
                    break;
                case FIRST_RETRY:
                    firstRetryState(msg);
                    break;
                case SECOND_RETRY:
                    secondRetryState(msg);
                    break;
                case PASSWORD_ACCEPTED:
                    passwordAcceptedState(msg);
                    break;
                case CHOOSING_ACTOR:
                    choosingActorState(msg);
                    break;

                case LOGGED_IN:
                case CONNECTED:
                default:
                    Log.logger.fatal("Unsupported states in Authenticator");
            }

            // check if processing of messages generated error or authorization failed
            if (currentState == LoginState.LOGIN_FAILED
                || currentState == LoginState.BAD_CLIENT_VERSION)
            {
                Log.logger.debug("Authorization failed");
                if (conn != null)
                {
                    conn.setNetworkMessageReceiver(null);
                    conn.networkMessage(new CloseConnection());
                    conn = null;
                }
            }
        }

        private void processLogin(LoginData msg, LoginState failureState)
        {
            if (!msg.getVersion().equals(Version.SIMPLIFIED_VERSION)
                && !Version.SIMPLIFIED_VERSION.equals(Version.DEVELOPMENT)
                && !msg.getVersion().equals(Version.DEVELOPMENT))
            {
                if (conn != null)
                {
                    conn.networkMessage(new LoginBadVersion());
                    conn.networkMessage(new CloseConnection());
                }
                currentState = LoginState.BAD_CLIENT_VERSION;
                return;
            }

            if (requestSend)
            {
                Log.logger.warn("Request for authorization already send to UserDB");
                currentState = LoginState.LOGIN_FAILED;
                requestSend = false;
                return;
            }

            requestSend = true;
            nextFailureState = failureState;
            lastUserName = msg.getUser();

            if (Log.logger.isDebugEnabled())
            {
                Log.logger.debug("Request for authentication send to UserDB for username: "
                    + msg.getUser());
            }

            userDB.requestActorList(msg.getUser(), msg.getPasswd(), this);
        }

        private void keysExchangedState(NetworkMessage msg)
        {
            if (msg.getType() != NetworkMessageType.LOGIN_DATA)
            {
                Log.logger.warn(UNEXPECTED_MSG + msg.getType());
                currentState = LoginState.LOGIN_FAILED;
                return;
            }

            processLogin((LoginData) msg, LoginState.FIRST_RETRY);
        }

        private void firstRetryState(NetworkMessage msg)
        {
            if (msg.getType() != NetworkMessageType.LOGIN_DATA)
            {
                Log.logger.warn(UNEXPECTED_MSG + msg.getType());
                currentState = LoginState.LOGIN_FAILED;
                return;
            }

            processLogin((LoginData) msg, LoginState.SECOND_RETRY);
        }

        private void secondRetryState(NetworkMessage msg)
        {
            if (msg.getType() != NetworkMessageType.LOGIN_DATA)
            {
                Log.logger.warn(UNEXPECTED_MSG + msg.getType());
                currentState = LoginState.LOGIN_FAILED;
                return;
            }

            processLogin((LoginData) msg, LoginState.LOGIN_FAILED);

        }

        private void passwordAcceptedState(NetworkMessage msg)
        {
            if (msg.getType() != NetworkMessageType.LOGIN_PROCEED)
            {
                Log.logger.warn(UNEXPECTED_MSG + msg.getType());
                currentState = LoginState.LOGIN_FAILED;
                return;
            }

            currentState = LoginState.CHOOSING_ACTOR;

            if (conn != null)
                conn.networkMessage(new LoginActorsList(this.availableActors,
                                                        this.disabledActors));
        }

        private void choosingActorState(NetworkMessage msg)
        {
            if (msg.getType() == NetworkMessageType.LOGIN_REFRESH_DISABLED_ACTOR_LIST)
            {
                this.disabledActors = new HashSet<Integer>();
                for (Actor a : availableActors)
                    if (logonDB.hasRecentlyLoggedOut(a.getId()))
                        disabledActors.add(a.getId());
                if (conn != null)
                {
                    Log.logger.debug("Player requested refreshing disabled actors list: "
                        + this.lastUserName);
                    conn.networkMessage(new LoginDisabledActorList(disabledActors));
                }
                return;
            }

            if (msg.getType() != NetworkMessageType.LOGIN_ACTOR_CHOSEN)
            {
                Log.logger.warn(UNEXPECTED_MSG + msg.getType());
                currentState = LoginState.LOGIN_FAILED;
                return;
            }
            int id = ((LoginActorChosen) msg).getId();

            if (disabledActors.contains(id))
            {
                Log.logger.warn("Player tried to log in with disabled actor: "
                    + id);
                currentState = LoginState.LOGIN_FAILED;
                return;
            }

            Actor actor = null;
            for (Actor a : availableActors)
            {
                if (a.getId() == id)
                {
                    actor = a;
                    break;
                }
            }
            if (actor == null)
            {
                Log.logger.warn(MessageFormat.format("Bad id chosen by user (username: {0})",
                                                     lastUserName));
                currentState = LoginState.LOGIN_FAILED;
                return;
            }

            if (Log.logger.isDebugEnabled())
            {
                Log.logger.debug("User " + lastUserName
                    + " logged in and chosen actor: " + actor.getName()
                    + " (id: " + id + ")");
            }

            currentState = LoginState.LOGGED_IN;
            ActorManager manager =
                    new PlayerManager(worldManager,
                                      chatManager,
                                      actor,
                                      conn,
                                      Authenticator.this);
            conn.networkMessage(new LoginCorrect(actor));
            logonDB.addActorManager(manager);

            conn = null;
        }
    }

    private abstract static class Task
    {
        private Request request;

        public Task(Request request)
        {
            this.request = request;
        }

        public abstract void execute();

        public Request getRequest()
        {
            return request;
        }
    }

    private class TaskThread implements Runnable
    {
        /** {@inheritDoc} */
        @Override
        public void run()
        {
            Log.logger.debug("Authentication thread stated");
            while (!Thread.interrupted())
            {
                Task task;
                try
                {
                    task = taskQueue.take();
                }
                catch (InterruptedException e)
                {
                    break;
                }
                if (task.getRequest().isWorking())
                    task.execute();
            }
            Log.logger.debug("Authentication thread interrupted");
        }

    }

    private static final String UNEXPECTED_MSG =
            "Unexpected message sent by client: ";

    private final int requestTimeout =
            ServerConfig.globalInstance().getAuthRequestTimeout() * 1000;

    private final int requestQueueMaxSize =
            ServerConfig.globalInstance().getAuthRequestPool();

    private LoggedPlayersCache logonDB;

    private WorldManager worldManager;
    private ChatManager chatManager;

    private PlayerLoadDB userDB;

    private Queue<Request> requestQueue = new LinkedList<Request>();

    private BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<Task>();
    private Thread workingThread;

    /**
     * Creates new authorization object. Starts its thread.
     * @param worldManager
     *            receiver of logged actor
     * @param chatManager
     *            receiver of logged actor
     * @param logonDB
     *            currently logged - if actor is in it and
     *            logins properly its old connection is
     *            replaced by this from authenticator
     * @param userDB
     *            connection to database containing
     *            authorization info (login / password)
     */
    public Authenticator(WorldManager worldManager,
                         ChatManager chatManager,
                         LoggedPlayersCache logonDB,
                         PlayerLoadDB userDB)
    {
        this.worldManager = worldManager;
        this.chatManager = chatManager;
        this.logonDB = logonDB;
        this.userDB = userDB;
        this.workingThread =
                new Thread(new TaskThread(), "authenticationThread");
        workingThread.start();
    }

    /**
     * Schedules given connection for authentication. After
     * authentication connection will be passed to world
     * manager.
     * @param conn
     *            connection to authenticate
     */
    public void authenticate(Connector conn)
    {
        while (requestQueue.size() > 0 && !requestQueue.peek().isWorking())
            requestQueue.poll();
        if (requestQueue.size() >= requestQueueMaxSize)
        {
            if (requestQueue.peek().isTimedOut())
            {
                Request r = requestQueue.poll();
                if (Log.isDebugEnabled())
                    Log.logger.debug("Closing timed out authentication process: "
                        + r.conn);
                r.conn.closeConnection();

                requestQueue.add(new Request(conn));
            }
            else
            {
                if (Log.isDebugEnabled())
                    Log.logger.debug("Closing authentication process - out of room: "
                        + conn);
                conn.closeConnection();
            }
        }
        else
            requestQueue.add(new Request(conn));
    }

    private void enqueueTask(Task task)
    {
        try
        {
            taskQueue.put(task);
        }
        catch (InterruptedException e)
        {
            Log.logger.debug("Put interrupted");
        }
    }

    /**
     * Logs out player. Uses
     * {@link LoggedPlayersCache#logoutActor(ActorManager)}.
     * Removes proper actor from world.
     * @param manager
     *            player's actor's manager to logout
     */
    public void logout(PlayerManager manager)
    {
        worldManager.removeActor(manager);
        logonDB.logoutActor(manager);
    }

    /**
     * Stops authentication thread.
     */
    public void stop()
    {
        if (workingThread != null)
        {
            workingThread.interrupt();
            workingThread = null;
        }
    }
}
