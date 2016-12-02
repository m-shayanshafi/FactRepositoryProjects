/*
 * NetClient.java
 *
 * Created on December 19, 2006, 5:35 PM
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

package netbattle;
import java.io.*;
import java.net.*;
import java.util.*;
import netbattle.database.registry.AccountRegistry;
import netbattle.messages.*;
import netbattle.database.*;
//import netbattle.ProxyCheck;
import mechanics.BattleMechanics;
import shoddybattle.*;
import java.lang.reflect.Constructor;
import shoddybattle.util.TimeInterval;
import com.octo.captcha.service.image.*;
import java.awt.image.*;
import javax.imageio.*;
import mechanics.clauses.Clause;
import mechanics.clauses.Clause.ClauseChoice;

/**
 * This class represents an online client.
 * @author Colin
 */
public class NetClient extends MessageHandler {
    
    private static ImageCaptchaService m_captchaService =
            new DefaultManageableImageCaptchaService();
    
    private static class FailedAttempt {
        /** Map of IP Address -> FailedAttempt object. */
        private static Map m_map = new HashMap();
        
        /** Window of time to check for failed attempts. */
        public static long TIME_INTERVAL = 1000 * 60 * 5L;
        /** Number of failed attempts allowed in said interval. */
        public static int ALLOWED_FAILURES = 4;
        /** Time of first failed attempt. */
        private long m_time;
        /** Number of failures. */
        private int m_failures;
        
        /** Initialise an instance of FailedAttempt. */
        public FailedAttempt() {
            m_time = new Date().getTime();
        }
        
        /** Get a FailedAttempt object by index. */
        public static synchronized FailedAttempt getFailedAttempt(String ip) {
            FailedAttempt ret = (FailedAttempt)m_map.get(ip);
            if (ret == null) {
                ret = new FailedAttempt();
                m_map.put(ip, ret);
            }
            return ret;
        }
        
        /** Check if this information requires that a captcha be sent. */
        public boolean isCaptchaRequired() {
            long now = new Date().getTime();
            if ((now - m_time) > TIME_INTERVAL)
                return false;
                
            return (m_failures > ALLOWED_FAILURES);
        }
        
        /** Increment the number of failures. */
        public void incrementFailures() {
            long now = new Date().getTime();
            if ((now - m_time) > TIME_INTERVAL) {
                m_time = now;
                m_failures = 0;
            }
            ++m_failures;
        }
    }
    
    private BattleServer m_server;
    private int m_level;
    private int m_status;
    private boolean m_open = true;
    private boolean m_captcha = false;
    
    /** The time when this client last indicated that it was alive, or zero
     *  if it has yet to indicate. */
    private long m_alive;
    private Thread m_activity;
    
    /** The user name of the client.
     */
    private String m_name = "";
    
    /** Battles that this client is involved in.
     */
    private Map m_battles = new HashMap();
    
    /** Clauses that each opponent has been challenged with.
     *  String : boolean[]
     */
    private Map m_clauses = new HashMap();
    
    /** Creates a new instance of NetClient */
    public NetClient(BattleServer server, Socket socket) throws IOException {
        m_server = server;
        m_socket = socket;
        m_socket.setSoLinger(true, 2);
        m_input = new ObjectInputStream(m_socket.getInputStream());
        m_output = new ObjectOutputStream(m_socket.getOutputStream());
    }
    
    public void close() {
        if (!m_open)
            return;
        
        try {
            if (m_name.length() != 0) {
                // Remove the client from all of his battles.
                Iterator i = m_battles.values().iterator();
                while (i.hasNext()) {
                    NetBattleField field = (NetBattleField)i.next();
                    field.removeClient(this);
                }
                m_battles.clear();

                m_server.broadcast(new StatusChangeMessage(
                        false, m_name, m_level, 0, -1));
                
                m_server.sendChatMessage(
                        m_name + " has left " + m_server.getServerName() + "."
                    );
            }
        } catch (Exception e) {
            
        }

        System.out.println("Client disconnected: "
                + m_socket.getInetAddress().getHostName()
                + ".");
        
        try {
            if (m_activity != null)
                m_activity.interrupt();
            stopRunning();
            m_socket.close();
        } catch (IOException e) {
            
        }
        m_open = false;
    }
    
    public String getUserName() {
        return m_name;
    }
    
    protected void informWriteError(IOException e) {
        stopRunning();
        if (m_server != null) {
            m_server.removeClient(this);
        }
    }
    
    protected void informReadError(Throwable e) {
        stopRunning();
        if (m_server != null) {
            m_server.removeClient(this);
        }
    }
    
    private String getBanMessage(Date future) {
        return "You are banned until " + future
            + " which is in "
            + TimeInterval.getDeltaInterval(future)
                .getApproximation()
            + ".\n\nIf you think this is a mistake, you may "
            + "contact the server administration.";
    }
    
    private boolean authenticateUser() throws IOException, ClassNotFoundException {
        NetMessage raw = (NetMessage)getNextMessage();
        
        /**if (ProxyCheck.isOpenProxy(m_client.getInetAddress(), m_server.getPort())) {
            return false;
        }**/
        
        AccountRegistry registry = AccountRegistry.getInstance();
        
        String ip = getIpAddress();
        long date = registry.getIpUnbanDate(ip);
        Date future = new Date(date);
        if (Calendar.getInstance().getTime().before(future)) {
            sendMessage(new SuccessMessage(false, getBanMessage(future), m_captcha));
            return false;
        }

        final int msgid = raw.getMessage();
        if (msgid == NetMessage.JOIN_SERVER) {
            JoinServerMessage msg = (JoinServerMessage)raw;

            String name = msg.getUserName().trim();
            String message = null;
            boolean success = !m_server.isLoggedOn(name) && (name.length() != 0);
            if (!success) {
                message = "You are already logged on with this account. "
                        + "If you experienced a\nconnection error, your "
                        + "account will likely time out within three minutes.";
            }
            
            // Check captcha.
            if (success && m_captcha) {
                if (!(success = m_captchaService.validateResponseForID(ip,
                        msg.getCaptcha()).booleanValue())) {
                    message = "The entered human verification image response did "
                        + "not match the text in the image.";
                }
            }
            
            // Check whether the user has the correct password.
            if (success) {
                String expected = registry.getPassword(name);
                if (expected == null) {
                    success = false;
                    message = "The user account does not exist.";
                } else if (!expected.equals(msg.getPasswordHash())) {
                    success = false;
                    message = "The password is incorrect.";
                    
                    /** Increment the number of failures from this IP. */
                    FailedAttempt attempt = FailedAttempt.getFailedAttempt(ip);
                    attempt.incrementFailures();
                    m_captcha = attempt.isCaptchaRequired();
                    
                } else {
                    // Check if the user is banned.
                    date = registry.getUserUnbanDate(name);
                    future = new Date(date);
                    if (Calendar.getInstance().getTime().before(future)) {
                        // User is banned.
                        success = false;
                        message = getBanMessage(future);
                    }
                }
            }
            
            sendMessage(new SuccessMessage(success, message, m_captcha));
            if (success) {
                m_name = name;
                return true;
            }
        } else if (msgid == NetMessage.REGISTER_ACCOUNT) {
            RegisterAccountMessage msg = (RegisterAccountMessage)raw;
            boolean success = true;
            
            final String name = msg.getUserName().trim();
            if (name.contains("*") || name.contains("+")) {
                success = false;
            }
            
            final String password = msg.getPasswordHash();

            if (success) {
                success = registry.addNewUser(name, password);
            }

            sendMessage(new SuccessMessage(success, null, m_captcha));
            if (success) {
                // Update IP address.
                setBanExpiry(0);
            }
        }
        return false;
    }
    
    public boolean setBanExpiry(long date) {
        return AccountRegistry.getInstance().banUser(m_name, getIpAddress(), date);
    }
    
    public int getUserLevel() {
        return m_level;
    }
    
    public void sendFile(final File f, final long length) {
        sendMessage(new Runnable() {
            public void run() {
                try {
                    m_output.writeLong(length);
                    FileInputStream input = new FileInputStream(f);
                    try {
                        byte[] bytes = new byte[32];
                        long total = 0;
                        while (true) {
                            int read = input.read(bytes);
                            if (read == -1)
                                break;
                            total += read;
                            m_output.write(bytes, 0, read);
                        }
                    } finally {
                        input.close();
                    }
                } catch (Exception e) {
                    
                }
            }
        });
    }
    
    /**
     * Set the internal level of the user. Does not actually change the user's
     * level! To do the latter, use BattleServer.setUserLevel().
     */
    /*package*/ void setUserLevel(int level) {
        m_level = level;
        sendMessage(new WelcomeMessage(m_level));
    }
    
    /**
     * Return the status of this client.
     */
    public int getStatus() {
        return m_status;
    }
    
    public String getIpAddress() {
        return m_socket.getInetAddress().getHostAddress();
    }
    
    public void run() {
        final AccountRegistry registry = AccountRegistry.getInstance();
        
        String ip = getIpAddress();
        FailedAttempt attempt = FailedAttempt.getFailedAttempt(ip);
        m_captcha = attempt.isCaptchaRequired();
        try {
            /** Before we do anything else, send a message to indicate whether
             *  a captcha is going to be required. */
            sendMessage(new SuccessMessage(false, null, m_captcha));
            while (isRunning()) {
                if (m_captcha) {
                    // Send a captcha challenge.
                    final BufferedImage image =
                            m_captchaService.getImageChallengeForID(ip);
                    sendMessage(new Runnable() {
                        public void run() {
                            try {
                                ByteArrayOutputStream output = new ByteArrayOutputStream();
                                ImageIO.write(image, "png", output);
                                output.flush();
                                m_output.writeObject(output.toByteArray());
                                output.close();
                            } catch (Exception e) {
                                
                            }
                        }
                    });
                }
                if (authenticateUser()) {
                    break;
                }
            }
        } catch (Throwable e) {
            m_server.removeClient(this);
        }
        
        if ((m_name != null) && (m_name.length() == 0)) {
            return;
        }
        
        sendFile(ModData.MOD_DATA_FILE,
                ModData.getDefaultData().getModDataLength());
        
        m_level = registry.getUserLevel(m_name);
        int[] fids = new int[NetBattleField.getBattleCount()];
        int[] level = new int[m_server.getClientCount()];
        int[] status = new int[level.length];
        sendMessage(new UserListMessage(
                m_server.getClientNames(level, status),
                level,
                status,
                NetBattleField.getBattleList(fids),
                fids,
                -1,
                Clause.getClauses()));
        
        sendMessage(new WelcomeMessage(m_level));
        
        m_server.sendChatMessage(
                m_name + " has joined " + m_server.getServerName() + "."
            );
        
        m_server.broadcast(new StatusChangeMessage(true, m_name, m_level,
                StatusChangeMessage.STATUS_HERE, -1));
        
        // If we get this far, the user is not banned, so we can use this
        // call to update his IP address.
        setBanExpiry(0);
        
        m_activity = new Thread(new Runnable() {
           private static final int DELAY = 200000;
           public void run() {
               try {
                   while (true) {
                        synchronized (this) {
                            wait(DELAY);
                        }
                        if ((System.currentTimeMillis() - m_alive) > DELAY) {
                            if (m_server != null) {
                                m_server.removeClient(NetClient.this);
                            }
                        }
                    }
                } catch (InterruptedException e) {
                   
                }
            } 
        });
        m_activity.start();
        
        super.run();
    }
    
    public void sendMessage(NetMessage msg) {
        if ((m_name.length() != 0) || (!msg.logInRequired())) {
            super.sendMessage(msg);
        }
    }
    
    protected void executeMessage(NetMessage raw) {
        switch (raw.getMessage()) {
            
            /**
             * This user has sent a text message.
             */
            case NetMessage.FIELD_TEXT: {
                FieldTextMessage text = (FieldTextMessage)raw;
                String message = m_name + ": " + text.getTextMessage();
                int fid = text.getFieldId();
                if (fid == -1) {
                    m_server.sendChatMessage(message);
                } else if (fid == -2) {
                    if (m_level >= AccountRegistry.LEVEL_MOD) {
                        m_server.sendChatMessage(message, true);
                        Collection c = NetBattleField.getBattleList();
                        Iterator i = c.iterator();
                        while (i.hasNext()) {
                            NetBattleField field = (NetBattleField)i.next();
                            if (field == null)
                                continue;
                            if (field.getDescription() == null)
                                continue;
                            field.showMessage(message, true);
                        }
                    }
                } else {
                    NetBattleField field = (NetBattleField)m_battles.get(new Integer(fid));
                    field.showMessage(message);
                }
            } break;
            
            /**
             * This user has changed his status.
             */
            case NetMessage.STATUS_CHANGE: {
                StatusChangeMessage msg = (StatusChangeMessage)raw;
                int fid = msg.getFieldId();
                NetBattleField field = null;
                if (fid == -2) {
                    field = NetBattleField.getFieldByDescription(
                            msg.getDescription());
                } else if (fid != -1) {
                    field = (NetBattleField)m_battles.get(new Integer(fid));
                } else {
                    if (msg.isOnline()) {
                        m_status = msg.getStatus();
                        m_server.broadcast(new StatusChangeMessage(
                                true, m_name, m_level, m_status, -1));
                    } else {
                        // User has closed the application!
                        m_server.removeClient(this);
                    }
                    return;
                }
                if (field != null) {
                    if (msg.isOnline()) {
                        m_battles.put(new Integer(field.getId()), field);
                        field.addClient(this, null);
                    } else {
                        field.removeClient(this);
                        m_battles.remove(new Integer(field.getId()));
                    }
                }
            } break;
            
            /**
             * The user has issued a challenge.
             */
            case NetMessage.ISSUE_CHALLENGE: {
                IssueChallengeMessage msg = (IssueChallengeMessage)raw;
                String name = msg.getOpponent();
                NetClient opponent = m_server.getClientByName(name);
                if (opponent != null) {
                    if (opponent.getStatus() == StatusChangeMessage.STATUS_HERE) {
                        boolean[] clauses = msg.getClauses();
                        opponent.sendMessage(new IssueChallengeMessage(
                                m_name, clauses));
                        m_clauses.put(name, clauses);
                    }
                }
            } break;
                
            /**
             * This user has accepted a challenge and sent in his team.
             */
            case NetMessage.ACCEPT_CHALLENGE: {
                AcceptChallengeMessage msg = (AcceptChallengeMessage)raw;
                NetClient opponent = m_server.getClientByName(msg.getOpponent());
                Pokemon[] pokemon = msg.getPokemon();
                
                int id = -1;
                
                String name = getUserName();
                if (pokemon != null) {
                    Class cls = m_server.getMechanicsClass();
                    BattleMechanics mechanics = null;
                    try {
                        Constructor ctor = cls.getConstructor(
                                new Class[] { int.class });
                        mechanics = (BattleMechanics)ctor.newInstance(
                                new Object[] { new Integer(10) });
                    } catch (Exception e) {
                        System.out.println("Failed to initialise battle mechanics.");
                        e.printStackTrace();
                    }
                    
                    NetBattleField field = new NetBattleField(m_server, mechanics);
                    
                    // Before we add any clients, add the clauses to the field.
                    boolean[] clauses = (boolean[])opponent.m_clauses.get(name);
                    ClauseChoice[] choices = Clause.getClauses();
                    if ((clauses != null) && (clauses.length == choices.length)) {
                        for (int i = 0; i < choices.length; ++i) {
                            if (clauses[i]) {
                                Clause c = choices[i].getClause();
                                field.applyEffect(c);
                            }
                        }
                    }
                    
                    field.addClient(this, pokemon);
                    id = field.getId();
                    Integer i = new Integer(id);
                    m_battles.put(i, field);
                    opponent.m_battles.put(i, field);
                }
                opponent.m_clauses.remove(name);
                
                opponent.sendMessage(new AcceptedChallengeMessage(id, m_name));
            } break;
            
            /**
             * This user has finalised his challenge and sent in his team.
             */
            case NetMessage.FINALISE_CHALLENGE: {
                FinaliseChallengeMessage msg = (FinaliseChallengeMessage)raw;
                int fid = msg.getId();
                NetBattleField field = (NetBattleField)m_battles.get(new Integer(fid));
                field.addClient(this, msg.getTeam());
            } break;
            
            /**
             * This user has used a move.
             */
            case NetMessage.USE_MOVE: {
                UseMoveMessage msg = (UseMoveMessage)raw;
                int fid = msg.getFieldId();
                NetBattleField field = (NetBattleField)m_battles.get(new Integer(fid));
                try {
                    field.queueMove(field.getTrainerId(this), msg.getMove());
                } catch (MoveQueueException e) {
                    sendMessage(new FieldTextMessage(fid, e.getMessage()));
                    sendMessage(new RequestMoveMessage(fid));
                }
            } break;
            
            /**
             * This user is requesting that another another user be
             * banned or kicked from the server.
             */
            case NetMessage.BAN_USER: {
                BanMessage msg = (BanMessage)raw;
                String user = msg.getUser();
                boolean self = user.equalsIgnoreCase(getUserName());
                if ((m_level < AccountRegistry.LEVEL_MOD) && !self) {
                    break;
                }
                
                long date = msg.getDate();
                if (date != -1) {
                    if (!self) {
                        m_server.banClient(this, user, date);
                    } else {
                        m_server.banClient(this, user, date, " banned him- or herself for ");
                    }
                } else {
                    m_server.kickClient(this, user);
                }
            } break;
            
            /**
             * This user is requesting the user list.
             */
            case NetMessage.USER_TABLE: {
                if (m_level < AccountRegistry.LEVEL_MOD) {
                    break;
                }
                
                sendMessage(m_server.getUserInformation());
            } break;
            
            /**
             * This user is changing the level of another user.
             */
            case NetMessage.WELCOME_MESSAGE: {
                if (m_level < AccountRegistry.LEVEL_ADMIN) {
                    break;
                }
                
                WelcomeMessage msg = (WelcomeMessage)raw;
                
                // getServerName() actually returns a user name!
                m_server.setUserLevel(msg.getServerName(), msg.getLevel());
            } break;
            
            /**
             * This user would like to withdraw a previously offered challenge.
             */
            case NetMessage.WITHDRAW_CHALLENGE: {
                WithdrawChallengeMessage msg = (WithdrawChallengeMessage)raw;
                String opp = msg.getChallenger();
                NetClient client = m_server.getClientByName(opp);
                if (client != null) {
                    client.sendMessage(new WithdrawChallengeMessage(m_name));
                }
                m_clauses.remove(opp);
            } break;
            
            /**
             * The user is either requesting the welcome window text or
             * attempting to set it. Don't allow either unless the user
             * is an administrator.
             */
            case NetMessage.WELCOME_TEXT_MESSAGE: {
                if (m_level < AccountRegistry.LEVEL_ADMIN) {
                    break;
                }
                
                WelcomeTextMessage msg = (WelcomeTextMessage)raw;
                String text = msg.getText();
                try {
                    if (text == null) {
                        sendMessage(new WelcomeTextMessage(
                                m_server.getWelcomeText()));
                    } else {
                        m_server.setWelcomeText(text);
                    }
                } catch (BattleServer.BattleServerException e) {
                    e.printStackTrace();
                }
            } break;
            
            /**
             * The user is indicating that he is alive.
             */
            case NetMessage.ACTIVITY_MESSAGE: {
                m_alive = System.currentTimeMillis();
            } break;
            
            /**
             * This user has requested a list of aliases of a user.
             */
            case NetMessage.FIND_ALIASES_MESSAGE: {
                if (m_level < AccountRegistry.LEVEL_MOD) {
                    break;
                }
                
                FindAliasesMessage msg = (FindAliasesMessage)raw;
                String user = msg.getUser();
                AccountRegistry registry = AccountRegistry.getInstance();
                List list = registry.getUsersByIp(registry.getIpAddress(user));
                sendMessage(new FindAliasesMessage(user, list));
            } break;
        }
    }
    
}
