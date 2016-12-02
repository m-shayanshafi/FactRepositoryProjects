/*
 * HumanClient.java
 *
 * Created on August 15th, 2007, 4:04 PM
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
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * The Free Software Foundation may be visited online at http://www.fsf.org.
 */

package shoddybattleclient;
import java.net.*;
import java.io.*;
import java.util.*;
import netbattle.*;
import netbattle.messages.*;
import shoddybattle.*;
import javax.swing.JOptionPane;
import java.security.*;
import java.util.zip.GZIPInputStream;
import java.awt.EventQueue;

/**
 *
 * @author HumanClient
 */
public class HumanClient extends ServerLink {

    private LobbyWindow m_lobby;
    private Map m_battles = Collections.synchronizedMap(new HashMap());
    /** Map of description -> fid. */
    private Map m_battleMap = Collections.synchronizedMap(new HashMap());
    private Map m_offers = Collections.synchronizedMap(new HashMap());
    private UserTableModel m_userTable;
    private int m_level = -1;
    
    /** Creates a new instance of HumanClient */
    public HumanClient(String host, int port) throws IOException, UnknownHostException {
        super(host, port);
    }

    /**
     * Return the user level of this user.
     * This is obviously *not* used for authentication purposes - the client
     * might lie! It is only used to avoid displaying options to users who
     * do not need them, providing for a cleaner experience.
     */
    public int getUserLevel() {
        return m_level;
    }
    
    public void dispose() {
        super.dispose();
        m_lobby = null;
        m_battles = null;
        m_offers = null;
    }
    
    /**
     * Inform the user of a read error.
     */
    protected void informReadError(final Throwable e) {
        if (!isRunning()) {
            return;
        }
        
        try {
            EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    String err =
                            "An error occurred while reading a message from the server: "
                            + e.getMessage()
                            + "\n\nOne way you can help is by sending us "
                            + "(via the bug tracker) this file:\n\n    "
                            + ModData.getStorageLocation() + "errors"
                            + "\n\nDisconnect from the server? (Try again at least once.)";

                    int result = JOptionPane.showConfirmDialog(
                            null,
                            err,
                            "Error",
                            JOptionPane.YES_NO_OPTION);

                    if (result == JOptionPane.YES_OPTION) {
                        close();
                        m_lobby.dispose();
                        new WelcomeWindow().setVisible(true);
                    }
                }
            });
        } catch (Exception ex) {
            
        }
    }
    
    /**
     * The HumanClient uses Swing to respond to many of its messages, so we
     * might as well handle them all on the Swing event dispatch thread.
     */
    protected void executeMessage(final NetMessage raw) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                handleMessage(raw);
            }
        });
    }
    
    protected BattleWindow getBattleByFieldId(int fid) {
        return (BattleWindow)m_battles.get(new Integer(fid));
    }
    
    protected ChatWindow getChatById(int fid) {
        if (fid == -1) {
            return m_lobby;
        }
        return getBattleByFieldId(fid);
    }
    
    public void setUserTable(UserTableModel model) {
        m_userTable = model;
    }
    
    /**
     * Add an entry to the battle map.
     */
    public void addBattleMapEntry(String desc, int fid) {
        m_battleMap.put(desc, new Integer(fid));
    }
    
    /**
     * Start watching a battle.
     */
    public void watchBattle(String description) {
        Integer fid = (Integer)m_battleMap.get(description);
        BattleWindow bat = new BattleWindow(this, fid.intValue());
        m_battles.put(fid, bat);
        sendMessage(new StatusChangeMessage(true, description));
        bat.setVisible(true);
    }
    
    /**
     * Remove a battle from this user's list of battles.
     */
    public void removeBattle(BattleWindow wnd) {
        m_battles.remove(new Integer(wnd.getFieldId()));
    }
    
    /**
     * Attach the messages coming from this link to the server to an
     * instance of LobbyWindow.
     */
    public void setLobbyWindow(LobbyWindow lobby) {
        m_lobby = lobby;
    }

    /**
     * Get the lobby window attached to this ServerLink.
     */
    public LobbyWindow getLobbyWindow() {
        return m_lobby;
    }
    
    protected void handleFieldTextMessage(FieldTextMessage text) {
        int fid = text.getFieldId();
        ChatWindow wnd = getChatById(fid);
        if (wnd != null) {
            wnd.sendMessage(text.getTextMessage(), text.isImportant());
        }
    }
    
    protected void handleStatusChangeMessage(StatusChangeMessage sta) {
        ChatWindow wnd = getChatById(sta.getFieldId());
        if (wnd != null) {
            wnd.updateStatus(sta.isOnline(), sta.getUserName(),
                    sta.getLevel(), sta.getStatus());
        }
    }
    
    protected void handleIssueChallengeMessage(IssueChallengeMessage msg) {
        String opponent = msg.getOpponent();
        IncomingChallenge offer = new IncomingChallenge(
                this, opponent, msg.getClauses());
        m_offers.put(opponent, offer);
        offer.setVisible(true);
    }
    
    protected void handleErrorMessage(ErrorMessage msg) {
        JOptionPane.showMessageDialog(null, msg.getErrorMessage());
    }
    
    protected void handleAcceptedChallengeMessage(AcceptedChallengeMessage accept) {
        int fid = accept.getFieldId();
        String opponent = accept.getOpponent();
        ChallengeWindow wnd = m_lobby.getChallengeByOpponent(opponent);
        if (fid != -1) {
            Pokemon[] team = wnd.getPokemon();
            sendMessage(new FinaliseChallengeMessage(fid, team));
        } else {
            new MessageBox(wnd,
                    "Rejected",
                    opponent + " rejected your challenge.")
                        .setVisible(true);
        }
        m_offers.remove(opponent);
        wnd.dispose();
        m_lobby.removeChallenge(wnd);
    }
    
    protected void handleBattleReadyMessage(BattleReadyMessage ready) {
        int fid = ready.getFieldId();
        BattleWindow bat = new BattleWindow(this,
                fid,
                ready.getParticipant(),
                ready.getUsers(),
                ready.getMoves(),
                ready.getParty());
        m_battles.put(new Integer(fid), bat);
        bat.setVisible(true);
    }
    
    protected void handleRequestMoveMessage(RequestMoveMessage msg) {
        BattleWindow bat = getBattleByFieldId(msg.getFieldId());
        if (bat != null) {
            bat.requestMove();
        }
    }
    
    protected void handleSelectionEndMessage(SelectionEndMessage msg) {
        BattleWindow bat = getBattleByFieldId(msg.getFieldId());
        if (bat != null) {
            bat.endSelection();
        }
    }
    
    protected void handleReplacePokemonMessage(ReplacePokemonMessage msg) {
        BattleWindow bat = getBattleByFieldId(msg.getFieldId());
        if (bat != null) {
            bat.replaceFaintedPokemon();
        }
    }
    
    protected void handleStatRefreshMessage(StatRefreshMessage msg) {
        BattleWindow bat = getBattleByFieldId(msg.getFieldId());
        if (bat != null) {
            bat.refreshStats(msg);
        }
    }
    
    protected void handleRatioRefreshMessage(RatioRefreshMessage msg) {
        BattleWindow bat = getBattleByFieldId(msg.getFieldId());
        if (bat != null) {
            bat.refreshStats(msg.getRatios());
        }
    }
    
    protected void handlePartyMessage(PartyMessage msg) {
        BattleWindow bat = getBattleByFieldId(msg.getFieldId());
        boolean[] shiny = msg.getShininess();
        int[] gender = msg.getGender();
        int[] ids = msg.getIds();
        if (bat != null) {
            bat.setPokemon(
                    ids[0], msg.getActivePokemon(0), gender[0], shiny[0],
                    ids[1], msg.getActivePokemon(1), gender[1], shiny[1]);
        }
    }
    
    protected void handleAddBattleMessage(AddBattleMessage msg) {
        String desc = msg.getBattle();
        boolean online = msg.isOnline();
        Integer id = new Integer(msg.getId());
        m_lobby.updateBattle(online, desc);

        String message = null;
        if (online) {
            message = "A new battle has begun: " + desc + ".";
            m_battleMap.put(desc, id);
        } else {
            message = "A battle has completed: " + desc + ".";
            m_battleMap.remove(desc);
        }
        if (m_lobby.showBattleMessages()) {
            m_lobby.sendMessage(message, false);
        }
    }
    
    protected void handleUpdatePokemonStatusMessage(UpdatePokemonStatusMessage msg) {
        int fid = msg.getFid();
        BattleWindow bat = getBattleByFieldId(fid);
        if (bat != null) {
            bat.setPokemonStatus(msg.getParty(), msg.getIdx(),
                    msg.getState(), msg.getStatus());
        }
    }
    
    protected void handleUserListMessage(UserListMessage msg) {
        BattleWindow bat = getBattleByFieldId(msg.getFieldId());
        if (bat != null) {
            bat.setUserList(msg.getUserList());
        }
    }
    
    protected void handleInformDamageMessage(InformDamageMessage msg) {
        BattleWindow bat = getBattleByFieldId(msg.getFieldId());
        if (bat != null) {
            bat.informDamage(msg);
        }
    }
    
    protected void handleWelcomeMessage(WelcomeMessage msg) {
        m_level = msg.getLevel();
        m_lobby.setAdminVisibility(m_level);
    }
    
    protected void handleUserTableMessage(UserTableMessage msg) {
        if (m_userTable == null) {
            UserTableModel.showUserTable(
                    this,
                    m_lobby,
                    msg.getNames(),
                    msg.getIps(),
                    msg.getLevels(),
                    msg.getDates());
        } else {
            m_userTable.updateData(
                    msg.getNames(),
                    msg.getIps(),
                    msg.getLevels(),
                    msg.getDates());
        }
    }
    
    protected void handleBattleEndMessage(BattleEndMessage msg) {
        BattleWindow bat = getBattleByFieldId(msg.getFieldId());
        if (bat != null) {
            bat.declareBattleEnd(msg.getVictor());
        }
    }
    
    protected void handleSpectatorMessage(SpectatorMessage msg) {
        BattleWindow bat = getBattleByFieldId(msg.getFid());
        if (bat != null) {
            bat.updatePartyStates(msg.getState(), msg.getStatuses());
        }
    }
    
    protected void handleWithdrawChallengeMessage(WithdrawChallengeMessage msg) {
        String opp = msg.getChallenger();
        IncomingChallenge offer = (IncomingChallenge)m_offers.get(opp);
        if (offer != null) {
            m_offers.remove(opp);
            offer.dispose();
            JOptionPane.showMessageDialog(null, opp + " withdrew the challenge.");
        }
    }
    
    protected void handleWelcomeTextMessage(WelcomeTextMessage msg) {
        WelcomeTextEditor editor = m_lobby.getWelcomeTextEditor();
        if (editor != null) {
            editor.setText(msg.getText());
        }
    }
    
    protected void handleFindAliasesMessage(FindAliasesMessage msg) {
        StringBuffer buffer = new StringBuffer(
                "The following users have the same IP address:\n");
        List list = msg.getAliases();
        if (list == null)
            return;
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                String s1 = (String)o1;
                String s2 = (String)o2;
                return s1.compareToIgnoreCase(s2);
            }
        });
        Iterator i = list.iterator();
        while (i.hasNext()) {
            String s = (String)i.next();
            buffer.append(s);
            buffer.append("\n");
        }
        JOptionPane.showMessageDialog(m_lobby, new String(buffer));
    }
    
}
