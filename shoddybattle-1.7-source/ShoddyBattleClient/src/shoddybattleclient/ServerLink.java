/*
 * ServerLink.java
 *
 * Created on December 20, 2006, 6:57 PM
 *
 * This file is a part of Shoddy Battle.
 * Copyright (C) 2006  Colin Fitzpatrick
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

package shoddybattleclient;
import java.net.*;
import java.io.*;
import java.util.*;
import netbattle.*;
import netbattle.messages.*;
import shoddybattle.*;
import java.security.*;
import java.util.zip.GZIPInputStream;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * This class represents a link between the client and the server.
 * This class has no abstract methods, but it is abstract because most of its
 * methods are empty.
 * 
 * @author Colin
 */
public abstract class ServerLink extends MessageHandler {
    
    private Socket m_server;
    /** Name of the server. */
    private String m_name; 
    private String m_userName;
    private Thread m_activity;
    
    public ServerLink() {
        Runtime.getRuntime().addShutdownHook(new Thread(
                    new Runnable() {
                            public void run() {
                                ServerLink.this.close();
                            }
                        }
                ));
    }
    
    /** Creates a new instance of ServerLink */
    public ServerLink(String host, int port) throws IOException, UnknownHostException {
        this();
        connect(host, port);
    }
    
    /**
     * Connect to a Shoddy Battle server.
     */
    public void connect(String host, int port) throws IOException, UnknownHostException {
        m_socket = m_server = new Socket(InetAddress.getByName(host), port);
        m_output = new ObjectOutputStream(m_server.getOutputStream());
        m_input = new ObjectInputStream(m_server.getInputStream());
    }
    
    /**
     * Run this thread.
     */
     public void run() {
        m_activity = new Thread(new Runnable() {
           public void run() {
               try {
                   ActivityMessage msg = new ActivityMessage();
                   while (true) {
                       synchronized (this) {
                           // Wait thirty seconds.
                           wait(30000);
                       }
                       // Send a message indicating that we are still alive.
                       sendMessage(msg);
                   }
               } catch (InterruptedException e) {
                   
               }
           } 
        });
        m_activity.start();
        super.run();
     }
    
     /**
      * Read an image from the server.
      */
     public BufferedImage readImage() throws IOException, ClassNotFoundException {
         byte[] bytes = (byte[])m_input.readObject();
         InputStream stream = new ByteArrayInputStream(bytes);
         BufferedImage image = ImageIO.read(stream);
         stream.close();
         return image;
     }
     
    /**
     * Cut references to other objects.
     */
    public void dispose() {
        m_server = null;
        m_activity = null;
    }
    
    /**
     * Get the user's name.
     */
    public String getUserName() {
        return m_userName;
    }
    
    /**
     * Get this server's name.
     */
    public String getServerName() {
        return m_name;
    }
    
    /**
     * Set this server's name.
     */
    public void setServerName(String server) {
        m_name = server;
    }
    
    /**
     * Close this link to the server.
     */
    public void close() {
        sendMessage(new StatusChangeMessage(false, -1));
        try {
            stopRunning();
            if (m_activity != null)
                m_activity.interrupt();
            m_server.close();
            dispose();
        } catch (Exception e) {
            
        }
    }
    
    /**
     * Receive a gzipped file from the server.
     */
    public void receiveFile(File f) throws IOException {
        FileOutputStream output = new FileOutputStream(f);
        long size = m_input.readLong();
        //ModDownload status = new ModDownload(size);
        //status.setVisible(true);
        byte[] bytes = new byte[50];
        GZIPInputStream input = new GZIPInputStream(m_input);
        long total = 0;
        while (true) {
            Thread.yield();
            int read = input.read(bytes);
            if (read == -1)
                break;
            output.write(bytes, 0, read);
            total += read;
            //status.setProgress(total);
        }
        output.close();
        //status.dispose();
    }
    
    /**
     * Join this server with a particular user name.
     *
     * @param name the user name to join this server with
     * @param password a plaintext version of the user's password
     */
    public void joinServer(String name, String password, String captcha) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hash = digest.digest(password.getBytes("ISO-8859-1"));
            String strHash = new String(hash, "ISO-8859-1");
            
            sendMessage(new JoinServerMessage(m_userName = name.trim(), strHash, captcha));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Register on this server with a particular user name.
     *
     * @param name the user name to join this server with
     * @param password a plaintext version of the user's password
     */
    public void registerAccount(String name, String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hash = digest.digest(password.getBytes("ISO-8859-1"));
            String strHash = new String(hash, "ISO-8859-1");
            
            sendMessage(new RegisterAccountMessage(name.trim(), strHash));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    
    protected void informReadError(final Throwable e) {
        if (isRunning()) {
            close();
        }
    }
    
    public void sendChatMessage(int field, String message) {
        FieldTextMessage msg = new FieldTextMessage(field, message);
        sendMessage(msg);
    }
    
    public void issueChallenge(String opponent, boolean[] clauses) {
        sendMessage(new IssueChallengeMessage(opponent, clauses));
    }
    
    public void acceptChallenge(String opponent, Pokemon[] team) {
        sendMessage(new AcceptChallengeMessage(opponent, team));
    }
    
    protected void executeMessage(final NetMessage raw) {
        handleMessage(raw);
    }
    
    protected void handleErrorMessage(ErrorMessage msg) {
    }
    
    protected void handleFieldTextMessage(FieldTextMessage text) {
    }
    
    protected void handleStatusChangeMessage(StatusChangeMessage sta) {
    }
    
    protected void handleIssueChallengeMessage(IssueChallengeMessage msg) {
    }
    
    protected void handleAcceptedChallengeMessage(AcceptedChallengeMessage accept) {
    }
    
    protected void handleBattleReadyMessage(BattleReadyMessage ready) {
    }
    
    protected void handleRequestMoveMessage(RequestMoveMessage msg) {
    }
    
    protected void handleSelectionEndMessage(SelectionEndMessage msg) {
    }
    
    protected void handleReplacePokemonMessage(ReplacePokemonMessage msg) {
    }
    
    protected void handleStatRefreshMessage(StatRefreshMessage msg) {
    }
    
    protected void handleRatioRefreshMessage(RatioRefreshMessage msg) {
    }
    
    protected void handlePartyMessage(PartyMessage msg) {
    }
    
    protected void handleAddBattleMessage(AddBattleMessage msg) {
    }
    
    protected void handleUpdatePokemonStatusMessage(UpdatePokemonStatusMessage msg) {
    }
    
    protected void handleUserListMessage(UserListMessage msg) {
    }
    
    protected void handleInformDamageMessage(InformDamageMessage msg) {
    }
    
    protected void handleWelcomeMessage(WelcomeMessage msg) {
    }
    
    protected void handleUserTableMessage(UserTableMessage msg) {
    }
    
    protected void handleBattleEndMessage(BattleEndMessage msg) {
    }
    
    protected void handleSpectatorMessage(SpectatorMessage msg) {
    }
    
    protected void handleWithdrawChallengeMessage(WithdrawChallengeMessage msg) {
    }
    
    protected void handleWelcomeTextMessage(WelcomeTextMessage msg) {
    }
    
    protected void handleFindAliasesMessage(FindAliasesMessage msg) {
    }
    
    protected void handleMessage(NetMessage raw) {
        switch (raw.getMessage()) {
            case NetMessage.FIELD_TEXT: {
                handleFieldTextMessage((FieldTextMessage)raw);
            } break;

            case NetMessage.STATUS_CHANGE: {
                handleStatusChangeMessage((StatusChangeMessage)raw);
            } break;

            case NetMessage.ISSUE_CHALLENGE: {
                handleIssueChallengeMessage((IssueChallengeMessage)raw);
            } break;

            case NetMessage.ACCEPTED_CHALLENGE: {
                handleAcceptedChallengeMessage((AcceptedChallengeMessage)raw);
            } break;
                
            case NetMessage.BATTLE_READY: {
                handleBattleReadyMessage((BattleReadyMessage)raw);
            } break;
            
            case NetMessage.REQUEST_MOVE: {
                handleRequestMoveMessage((RequestMoveMessage)raw);
            } break;
            
            case NetMessage.SELECTION_END: {
                handleSelectionEndMessage((SelectionEndMessage)raw);
            } break;
            
            case NetMessage.REPLACE_POKEMON: {
                handleReplacePokemonMessage((ReplacePokemonMessage)raw);
            } break;
            
            case NetMessage.STAT_REFRESH: {
                handleStatRefreshMessage((StatRefreshMessage)raw);
            } break;

            case NetMessage.RATIO_REFRESH: {
                handleRatioRefreshMessage((RatioRefreshMessage)raw);
            } break;
            
            case NetMessage.PARTY_MESSAGE: {
                handlePartyMessage((PartyMessage)raw);
            } break;
            
            case NetMessage.ADD_BATTLE: {
                handleAddBattleMessage((AddBattleMessage)raw);
            } break;
            
            case NetMessage.UPDATE_POKEMON_STATUS: {
                handleUpdatePokemonStatusMessage((UpdatePokemonStatusMessage)raw);
            } break;
            
            case NetMessage.SPECTATOR_MESSAGE: {
                handleSpectatorMessage((SpectatorMessage)raw);
            } break;
            
            case NetMessage.USER_LIST: {
                handleUserListMessage((UserListMessage)raw);
            } break;
            
            case NetMessage.INFORM_DAMAGE: {
                handleInformDamageMessage((InformDamageMessage)raw);
            } break;
            
            case NetMessage.WELCOME_MESSAGE: {
                handleWelcomeMessage((WelcomeMessage)raw);
            } break;
            
            case NetMessage.USER_TABLE: {
                handleUserTableMessage((UserTableMessage)raw);
            } break;
            
            case NetMessage.BATTLE_END: {
                handleBattleEndMessage((BattleEndMessage)raw);
            } break;
            
            case NetMessage.WITHDRAW_CHALLENGE: {
                handleWithdrawChallengeMessage((WithdrawChallengeMessage)raw);
            } break;
            
            case NetMessage.WELCOME_TEXT_MESSAGE: {
                handleWelcomeTextMessage((WelcomeTextMessage)raw);
            } break;
            
            case NetMessage.FIND_ALIASES_MESSAGE: {
                handleFindAliasesMessage((FindAliasesMessage)raw);
            } break;
            
            case NetMessage.ERROR_MESSAGE: {
                handleErrorMessage((ErrorMessage)raw);
            } break;
        }
    }
    
}
