/*
 * NetBattleField.java
 *
 * Created on December 19, 2006, 5:21 PM
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
import java.util.*;
import mechanics.statuses.StatusEffect;
import mechanics.statuses.abilities.IntrinsicAbility;
import netbattle.messages.*;
import shoddybattle.*;
import netbattle.*;
import mechanics.*;
import mechanics.clauses.Clause;
import java.io.*;
import netbattle.BattleServer.ChatQueueItem;

/**
 * This class represents an online battle field.
 * @author Colin
 */
public class NetBattleField extends BattleField {
    
    private BattleServer m_server;
    private List m_clients = Collections.synchronizedList(new ArrayList());
    private static List m_battles = Collections.synchronizedList(new ArrayList());
    private BattleTurn[] m_turn = { null, null };
    private volatile boolean[] m_replace = { false, false };
    private boolean m_initialised = false;
    private String m_description = null;
    private Thread m_dispatch = null;
    private PrintWriter m_log = null;
    private String[] m_participantName = new String[m_participants];
    
    /**
     * Dispose of this NetBattleField.
     */
    public void dispose() {
        super.dispose();
        m_server = null;
        m_clients = null;
        m_turn = null;
        m_replace = null;
        m_description = null;
        if (m_log != null) {
            m_log.close();
        }
        m_log = null;
    }
    
    /**
     * Get a description of this battle field.
     */
    public String getDescription() {
        return m_description;
    }
    
    /**
     * Get a list of battles currently taking place.
     */
    public static Collection getBattleList() {
        return Collections.unmodifiableCollection(m_battles);
    }
    
    /**
     * Return the number of battles currently ongoing.
     */
    public static int getBattleCount() {
        return m_battles.size();
    }
    
    /**
     * Get a NetBattleField by its description.
     */
    public static NetBattleField getFieldByDescription(String desc) {
        if (desc == null)
            return null;
        synchronized (m_battles) {
            Iterator i = m_battles.iterator();
            while (i.hasNext()) {
                NetBattleField field = (NetBattleField)i.next();
                if (desc.equals(field.m_description)) {
                    return field;
                }
            }
        }
        return null;
    }
    
    /**
     * Get a NetBattleField object by its absolute id, which is unique
     * within each instance of the server.
     */
    public static NetBattleField getFieldById(int id) {
        synchronized (m_battles) {
            Iterator i = m_battles.iterator();
            while (i.hasNext()) {
                NetBattleField field = (NetBattleField)i.next();
                if (field.getId() == id) {
                    return field;
                }
            }
        }
        return null;
    }
    
    /**
     * Get an array of the battles currently taking place. Each index in the
     * array contains the description of a battle.
     */
    public static String[] getBattleList(int[] fids) {
        synchronized (m_battles) {
            List list = new ArrayList();
            Iterator i = m_battles.iterator();
            int j = 0;
            while (i.hasNext()) {
                NetBattleField field = (NetBattleField)i.next();
                String desc = field.getDescription();
                if ((desc != null) && (desc.length() > 0)) {
                    list.add(desc);
                    fids[j] = field.getId();
                    ++j;
                } else {
                    if (field != null) {
                        field.dispose();
                    }
                    i.remove();
                }
            }
            return (String[])list.toArray(new String[list.size()]);
        }
    }
    
    /**
     * Return the absolute (unique within this instance of the server) id of
     * this battle field.
     */
    public int getId() {
        return hashCode();
    }
    
    /**
     * Allows for external construction.
     */
    public NetBattleField(BattleServer server, BattleMechanics mechanics) {
        super(mechanics);
        
        System.gc();
        m_server = server;
        
        m_battles.add(this);
        m_pokemon = new Pokemon[m_participants][];
    }
    
    /**
     * Get a status update message.
     */
    private void updateStatus(Pokemon poke, StatusEffect eff) {
        int lock = eff.getLock();
        if ((lock == 0) || (lock == StatusEffect.SPECIAL_EFFECT_LOCK)) {
            if (eff.isSingleton() && !(eff instanceof IntrinsicAbility)) {
                String name = eff.getName();
                if (name == null) {
                    return;
                }
                sendMessage(new UpdatePokemonStatusMessage(getId(),
                    poke.getParty(),
                    poke.getId(),
                    poke.hasEffect(StatusEffect.SPECIAL_EFFECT_LOCK)
                        ? UpdatePokemonStatusMessage.STATUS_AFFLICTED
                        : UpdatePokemonStatusMessage.STATUS_ACTIVE,
                    name));
            }
        }
    }
    
    /**
     * Inform that a status effect was applied to a pokemon.
     */
    public void informStatusApplied(Pokemon poke, StatusEffect eff) {
        String desc = eff.getDescription();
        if (desc != null) {
            showMessage(poke.getName() + desc);
        }
        updateStatus(poke, eff);
    }
    
    /**
     * Inform that a status effect was removed from a pokemon.
     */
    public void informStatusRemoved(Pokemon poke, StatusEffect eff) {
        updateStatus(poke, eff);
    }
    
    /**
     * Get the name of a trainer.
     * @param idx the trainer whose name to get.
     */
    public String getTrainerName(int idx) {
        return ((NetClient)m_clients.get(idx)).getUserName();
    }
    
    /**
     * Format a pokemon's name for introduction purposes.
     */
    private String getFormattedName(Pokemon p) {
        StringBuffer buffer = new StringBuffer(p.getName());
        buffer.append(" (lvl ");
        buffer.append(p.getLevel());
        buffer.append(" ");
        buffer.append(p.getSpeciesName());
        int g = p.getGender();
        if (g != PokemonSpecies.GENDER_NONE) {
            buffer.append(' ');
            buffer.append((g == PokemonSpecies.GENDER_MALE) ? '♂' : '♀');
        }
        buffer.append(')');
        return new String(buffer);
    }
    
    /**
     * Initialise this battle by attaching this object to each of the pokemon
     * in the player's party as well as communicating the initial state of the
     * battle to each of the players.
     */
    private void initialiseBattle() {
        
        if (m_initialised) {
            return;
        }
        m_initialised = true;

        String[] users = new String[m_clients.size()];
        for (int i = 0; i < m_clients.size(); ++i) {
            users[i] = ((NetClient)m_clients.get(i)).getUserName();
        }

        m_description = users[0] + " v. " + users[1];
        if (getFieldByDescription(m_description) != this) {
            String base = m_description + " ";
            int number = 1;
            do {
                ++number;
                m_description = base + String.valueOf(number);
            } while (getFieldByDescription(m_description) != this);
        }
        
        Date now = new Date();
        File directory = new File("logs/battles-"
                + ChatQueueItem.formatDate(now) + "/");
        directory.mkdirs();
        File file = null;
        synchronized (NetBattleField.class) {
            do {
                String fileName = ChatQueueItem.formatTime(new Date());
                if (System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1) {
                    fileName = fileName.replace(':', '-');
                }
                file = new File(directory, fileName);
            } while (file.exists());
        }
        
        try {
            m_log = new PrintWriter(new FileOutputStream(file), true);
            m_log.println(m_description);
            m_log.println();
        } catch (IOException e) {
            
        }
        
        m_server.broadcast(new AddBattleMessage(getId(), m_description, true));

        for (int i = 0; i < m_clients.size(); ++i) {
            NetClient client = (NetClient)m_clients.get(i);
            
            final int length = m_pokemon[i].length;
            String[][] moves = new String[length][];
            String[] party = new String[length];
            for (int j = 0; j < length; ++j) {
                party[j] = m_pokemon[i][j].getSpeciesName();
                moves[j] = new String[4];
                for (int k = 0; k < 4; ++k) {
                    moves[j][k] = m_pokemon[i][j].getMoveName(k);
                }
            }

            NetMessage msg = new BattleReadyMessage(
                    getId(),
                    i,
                    users,
                    moves,
                    party);
            client.sendMessage(msg);
        }

        StringBuffer buffer = new StringBuffer("Rules: ");
        Iterator itr = m_effects.iterator();
        int clauses = 0;
        while (itr.hasNext()) {
            Object o = itr.next();
            if (o instanceof Clause) {
                ++clauses;
                Clause c = (Clause)o;
                buffer.append(c.getClauseName());
                buffer.append(", ");
            }
        }
        if (clauses != 0) {
            showMessage(buffer.substring(0, buffer.length() - 2));
        }
        
        for (int i = 0; i < m_participants; ++i) {
            refreshStats(i);
            Pokemon p0 = m_pokemon[i][m_active[i]];
            int opp = (i == 0) ? 1 : 0;
            Pokemon p1 = m_pokemon[opp][m_active[opp]];
            showMessage(users[i]
                    + " sent out "
                    + getFormattedName(p0)
                    + ".");
            ((NetClient)m_clients.get(i)).sendMessage(getPartyMessage(p0, p1));
        }

        setPokemon(m_pokemon);
        requestMoves();
    }
    
    /**
     * Get the id in this battle field of a client.
     */
    public int getTrainerId(NetClient client) {
        synchronized (m_clients) {
            for (int i = 0; i < m_clients.size(); ++i) {
                if (m_clients.get(i) == client) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    /**
     * Add a client to this battle field, along with his team of pokemon.
     * A team does does not need to be passed (and should not be passed) if
     * there are already two clients in the field. When the total number of
     * clients reaches two, this method has the side effect of initialising the
     * battle. (This is arguably bad.)
     */
    public synchronized void addClient(NetClient client, Pokemon[] team) {
        if (m_clients.contains(client)) {
            return;
        }
        m_clients.add(client);
        int size = m_clients.size();
        int idx = size - 1;
        if ((size <= m_participants) && !m_initialised) {
            m_pokemon[idx] = team;
            attachField(idx);
            try {
                validateTeam(team, idx);
            } catch (ValidationException e) {
                m_pokemon[idx] = null;
                m_clients.remove(client);
                String message = "The battle cannot begin because "
                        + client.getUserName() + "'s team has a problem:\n\n"
                        + e.getMessage();
                NetMessage msg = new ErrorMessage(message);
                synchronized (m_clients) {
                    Iterator i = m_clients.iterator();
                    while (i.hasNext()) {
                        NetClient c = (NetClient)i.next();
                        c.sendMessage(msg);
                    }
                }
                return;
            }
            
            if (size == m_participants) {
                initialiseBattle();
            }
        } else {
            updateStatus(true, client.getUserName());
            prepareClient(idx);
        }
    }
    
    /**
     * Remove a client from this battle field. This method will destroy the
     * field if no clients remain after the removal.
     */
    public void removeClient(NetClient remove) {
        if (m_clients == null)
            return;
        synchronized (m_clients) {
            Iterator i = m_clients.iterator();
            while (i.hasNext()) {
                NetClient client = (NetClient)i.next();
                if (client == remove) {
                    updateStatus(false, remove.getUserName());
                    i.remove();
                    break;
                }
            }
        }
        if (m_clients.size() == 0) {
            if (m_log != null) {
                m_log.close();
                m_log = null;
            }
            m_server.broadcast(new AddBattleMessage(getId(), m_description, false));
            //m_ids.set(m_id, new Boolean(false));
            m_battles.remove(this);
            if (m_dispatch != null) {
                m_dispatch.stop();
            }
            dispose();
            System.gc();
        }
    }
    
    /**
     * Inform that a pokemon used a move.
     *
     * @param poke the pokemon who used the move
     * @param name the name of the move that was used
     */
    public void informUseMove(Pokemon poke, String name) {
        showMessage(poke.getName() + " used " + name + ".");
    }
    
    /**
     * Obtain a replacement pokemon for the team identified by the parameter.
     */
    protected void requestPokemonReplacement(int i) {
        NetClient client = (NetClient)m_clients.get(i);
        String trainer = client.getUserName();
        
        m_replace[i] = true;
        client.sendMessage(new ReplacePokemonMessage(getId()));
    }
    
    /**
     * Send a message to all users conveying a status update.
     */
    private void updateStatus(boolean online, String user) {
        sendMessage(new StatusChangeMessage(online, user, -1, -1, getId()));
    }
    
    /**
     * Send a message to all clients on this battle field.
     */
    private void sendMessage(NetMessage msg) {
        synchronized (m_clients) {
            Iterator i = m_clients.iterator();
            while (i.hasNext()) {
                NetClient client = (NetClient)i.next();
                client.sendMessage(msg);
            }
        }
    }
 
    /**
     * Narrate the battle.
     */
    public void showMessage(String message) {
        showMessage(message, false);
    }
    
    public void showMessage(String message, boolean important) {
        if ((Thread.currentThread() == m_dispatch) && !isNarrationEnabled())
            return;
        FieldTextMessage msg = new FieldTextMessage(getId(), message);
        msg.setImportant(important);
        sendMessage(msg);
        if (m_log != null) {
            m_log.println(ChatQueueItem.getPrefix() + message);
        }
    }
    
    /**
     * Get a spectator message.
     */
    private SpectatorMessage getSpectatorMessage() {
        int[][] states = new int[m_pokemon.length][];
        String[][][] statuses = new String[m_pokemon.length][][];
        
        for (int i = 0; i < m_pokemon.length; ++i) {
            Pokemon[] team = m_pokemon[i];
            states[i] = new int[team.length];
            statuses[i] = new String[team.length][];
            for (int j = 0; j < team.length; ++j) {
                Pokemon p = team[j];
                int state;
                if (p.isFainted()) {
                    state = UpdatePokemonStatusMessage.STATUS_FAINTED;
                } else if (p.hasEffect(StatusEffect.SPECIAL_EFFECT_LOCK)) {
                    state = UpdatePokemonStatusMessage.STATUS_AFFLICTED;
                } else {
                    state = UpdatePokemonStatusMessage.STATUS_ACTIVE;
                }
                states[i][j] = state;
                
                List list = p.getNormalStatuses(StatusEffect.SPECIAL_EFFECT_LOCK);
                Iterator itr = list.iterator();
                while (itr.hasNext()) {
                    StatusEffect effect = (StatusEffect)itr.next();
                    if (!effect.isSingleton()
                            || (effect.getName() == null)) {
                        itr.remove();
                    }
                }
                statuses[i][j] = new String[list.size()];
                int k = 0;
                itr = list.iterator();
                while (itr.hasNext()) {
                    StatusEffect effect = (StatusEffect)itr.next();
                    statuses[i][j][k++] = effect.getName();
                }
            }
        }
        
        return new SpectatorMessage(getId(), states, statuses);
    }
    
    /**
     * Prepare a client for watching this battle.
     *
     * @param id the position of the client in m_clients
     */
    private void prepareClient(int id) {
        String[] users;
        synchronized (m_clients) {
            int size = m_clients.size();
            users = new String[size];
            for (int i = 0; i < size; ++i) {
                users[i] = ((NetClient)m_clients.get(i)).getUserName();
            }
        }
        NetClient client = (NetClient)m_clients.get(id);
        client.sendMessage(new UserListMessage(
                users,
                null,
                null,
                null,
                null,
                getId(),
                null));
        refreshActivePokemon(id, id + 1);
        client.sendMessage(getRatioMessage());
        client.sendMessage(getSpectatorMessage());
    }
    
    /**
     * Inform that a pokemon fainted.
     */
    public void informPokemonFainted(int party, int idx) {
        Pokemon p = m_pokemon[party][idx];
        showMessage(p.getTrainerName() + "'s " + p.getName() + " fainted.");
        UpdatePokemonStatusMessage msg = new UpdatePokemonStatusMessage(
                getId(), party, idx,
                UpdatePokemonStatusMessage.STATUS_FAINTED,
                null);
        sendMessage(msg);
    }
    
    /**
     * Get a PartyMessage.
     */
    private PartyMessage getPartyMessage(Pokemon p0, Pokemon p1) {
        boolean sub0 = p0.hasSubstitute();
        boolean sub1 = p1.hasSubstitute();
        int[] ids = {
                p0.getId(),
                p1.getId()
            };
        String[] active = {
                sub0 ? "Substitute" : p0.getSpeciesName(),
                sub1 ? "Substitute" : p1.getSpeciesName()
            };
        int[] gender = {
                sub0 ? PokemonSpecies.GENDER_NONE : p0.getGender(),
                sub1 ? PokemonSpecies.GENDER_NONE : p1.getGender()
            };
        boolean[] shiny = {
                sub0 ? false : p0.isShiny(),
                sub1 ? false : p1.isShiny()
            };
        return new PartyMessage(ids, active, gender, shiny, getId());
    }
    
    /**
     * Refresh all active pokemon. This function is used in conjunction with
     * MoveList.SubstituteEffect to display the proper sprite.
     */
    public void refreshActivePokemon() {
        informSwitchInPokemon(0, m_pokemon[0][m_active[0]], true);
    }
    
    /**
     * Refresh the active pokemon (for watchers only).
     */
    private void refreshActivePokemon(int low, int high) {
        if (m_clients.size() <= low) {
            return;
        }
        Pokemon p0 = m_pokemon[0][m_active[0]];
        Pokemon p1 = m_pokemon[1][m_active[1]];
        PartyMessage msg = getPartyMessage(p0, p1);
        synchronized (m_clients) {
            for (int i = low; i < high; ++i) {
                NetClient client = (NetClient)m_clients.get(i);
                client.sendMessage(msg);
            }
        }
    }
    
    /**
     * A pokemon was switched in.
     */
    public void informSwitchInPokemon(int trainer, Pokemon poke, boolean silent) {
        if (!silent) {
            String name = ((NetClient)m_clients.get(trainer)).getUserName();
            showMessage(name + " switched in " + getFormattedName(poke) + ".");
        }
  
        int opp = (trainer == 0) ? 1 : 0;
        NetClient client = (NetClient)m_clients.get(opp);
        Pokemon popp = m_pokemon[opp][m_active[opp]];
        client.sendMessage(getPartyMessage(popp, poke));
        ((NetClient)m_clients.get(trainer)).sendMessage(getPartyMessage(poke, popp));

        refreshActivePokemon(m_participants, m_clients.size());
    }
    
    /**
     * A pokemon was switched in.
     */
    public void informSwitchInPokemon(int trainer, Pokemon poke) {
        informSwitchInPokemon(trainer, poke, false);
    }
    
    /**
     * A pokemon's health was changed.
     */
    public void informPokemonHealthChanged(Pokemon poke, int hp) {
        double ratio = (double)hp / (double)poke.getStat(Pokemon.S_HP);
        String name = poke.getName();
        int party = poke.getParty();
        int id = poke.getId();
        sendMessage(new InformDamageMessage(getId(), party, id, ratio, name));
        if (ratio != 0.0) {
            long percent = Math.abs(Math.round(ratio * 100.0));
            String verb = (ratio <= 0) ? " lost " : " restored ";
            String message = name + verb + percent + "% of its health.";
            if (m_log != null) {
                m_log.println(ChatQueueItem.getPrefix() + message);
            }
        }
    }
    
    /**
     * A player has won.
     */
    public void informVictory(int trainer) {
        refreshStats();
        if (trainer != -1) {
            NetClient client = (NetClient)m_clients.get(trainer);
            showMessage(client.getUserName() + " wins!");
        } else {
            showMessage("It's a draw!");
        }
        sendMessage(new BattleEndMessage(trainer, getId()));
        if (m_dispatch != null) {
            // SINFUL! Do not ever do this!
            Thread t = m_dispatch;
            m_dispatch = null;
            t.stop();
        }
    }
    
    /**
     * Get a RatioRefreshMessage of the current ratios.
     */
    private RatioRefreshMessage getRatioMessage() {
        double[] ratio = new double[m_turn.length];
        for (int i = 0; i < m_turn.length; ++i) {
            Pokemon p = m_pokemon[i][m_active[i]];
            ratio[i] = (double)(p.getHealth()) / p.getStat(Pokemon.S_HP);
        }
        return new RatioRefreshMessage(getId(), ratio);
    }
    
    /**
     * Refresh all players' statistics.
     */
    public void refreshStats() {
        for (int i = 0; i < m_participants; ++i) {
            refreshStats(i);
        }
        synchronized (m_clients) {
            int clients = m_clients.size();
            if (m_participants == clients) {
                return;
            }
            RatioRefreshMessage msg = getRatioMessage();
            for (int i = m_participants; i < clients; ++i) {
                ((NetClient)m_clients.get(i)).sendMessage(msg);
            }
        }
    }
    
    /**
     * Refresh a player's statistics.
     */
    public void refreshStats(int trainer) {
        Pokemon p = m_pokemon[trainer][m_active[trainer]];
        int hp = p.getHealth();
        int maxHp = p.getStat(Pokemon.S_HP);
        int[] pp = new int[4], maxpp = new int[4];
        for (int i = 0; i < 4; ++i) {
            pp[i] = p.getPp(i);
            maxpp[i] = p.getMaxPp(i);
        }

        int opponent = (trainer == 0) ? 1 : 0;
        Pokemon pokeOpponent = m_pokemon[opponent][m_active[opponent]];

        double ratio = ((double)pokeOpponent.getHealth()) / pokeOpponent.getStat(Pokemon.S_HP);
        
        boolean[] usable = new boolean[4];
        for (int i = 0; i < usable.length; ++i) {
            try {
                usable[i] = (p.getVetoingEffect(i) == null);
            } catch (MoveQueueException e) {
                usable[i] = false;
            }
        }
        
        NetClient client = (NetClient)m_clients.get(trainer);
        NetMessage msg = new StatRefreshMessage(getId(),
                hp, maxHp, pp, maxpp, p.getItemName(), ratio, usable);
        client.sendMessage(msg);
    }
    
    /**
     * Wait for a player to switch pokemon.
     * This method should be called *only* from the dispatch thread. It will
     * throw an InternalError if it is called from another thread.
     */
    public void requestAndWaitForSwitch(int party) {
        if (Thread.currentThread() != m_dispatch)
            throw new InternalError();
        refreshStats();
        requestPokemonReplacement(party);
        if (!m_replace[party]) {
            return;
        }
        do {
            synchronized (m_dispatch) {
                try {
                    m_dispatch.wait(1000);
                } catch (InterruptedException e) {

                }
            }
        } while ((m_replace != null) && m_replace[party]);
    }
    
    /**
     * Process a move sent in by a client. If this is the second (m_participants
     * is not referenced, although it should be) move to be added this turn
     * then this method has the side effect of executing the round.
     */
    public synchronized void queueMove(int trainer, BattleTurn move) throws MoveQueueException {
        if (trainer > (m_participants - 1)) {
            return;
        }
        
        if (move == null) {
            if (m_dispatch == null) {
                m_turn[trainer] = null;
            }
            return;
        }
        
        Pokemon[] active = getActivePokemon();
        
        if (active[trainer].isActive() || m_replace[trainer]) {
            if (!move.isMoveTurn()) {
                Pokemon poke = m_pokemon[trainer][move.getId()];
                if (poke.getHealth() == 0) {
                    requestPokemonReplacement(trainer);
                    throw new MoveQueueException(poke.getName()
                        + " has already fainted!");
                }
                int id = move.getId();
                if (m_active[trainer] == id) {
                    if (m_replace[trainer]) {
                        requestPokemonReplacement(trainer);
                    } else {
                        //requestMove(trainer);
                    }
                    throw new MoveQueueException(
                            active[trainer].getName() + " is already out.");
                }
                
                if (m_replace[trainer]) {
                    int opponent = (trainer == 0) ? 1 : 0;
                    if (!m_replace[opponent]) {
                        m_replace[trainer] = false;
                        boolean search = (m_dispatch == null);
                        replaceFaintedPokemon(trainer, id, search);
                        refreshStats();
                        if (!search) {
                            synchronized (m_dispatch) {
                                m_dispatch.notify();
                            }
                        }
                        return;
                    }
                }
                else if (!active[trainer].canSwitch()) {
                    //requestMove(trainer);
                    throw new MoveQueueException(active[trainer].getName()
                        + " is trapped!");
                }
            } else {
                Pokemon p = active[trainer];
                
                if (m_replace[trainer]) {
                    requestPokemonReplacement(trainer);
                    throw new MoveQueueException("Please switch to another pokemon.");
                }
                
                if (p.mustStruggle()) {
                    move = BattleTurn.getMoveTurn(-1);
                } else {                
                    if (p.getPp(move.getId()) <= 0) {
                        //requestMove(trainer);
                        throw new MoveQueueException(
                            p.getMoveName(move.getId())
                            + " has no PP left!");
                    }
                    StatusEffect veto = p.getVetoingEffect(move.getId());
                    if (veto != null) {
                        //requestMove(trainer);
                        throw new MoveQueueException(veto.getName()
                                + " prohibits the use of this move.");
                    }
                    
                }
            }
            if (m_dispatch != null)
                return;
            m_turn[trainer] = move;
        }
        
        boolean finished = true;
        for (int i = 0; i < m_turn.length; ++i) {
            if ((m_turn[i] == null) && (active[i].isActive() || m_replace[i])) {
                finished = false;
                break;
            }
        }
        
        if (!finished) {
            return;
        }
        
        // If both pokemon had fainted.
        if (m_replace[0]) {
            for (int i = 0; i < m_turn.length; ++i) {
                m_replace[i] = false;
                replaceFaintedPokemon(i, m_turn[i].getId(), true);
                m_turn[i] = null;
            }
            refreshStats();
            return;
        }
        
        synchronized (m_clients) {
            for (int i = 0; i < m_participants; ++i) {
                if (m_turn[i] == null) {
                    m_turn[i] = BattleTurn.getMoveTurn(0);
                }
                NetClient client = (NetClient)m_clients.get(i);
                client.sendMessage(new SelectionEndMessage(getId()));
            }
        }
        
        m_dispatch = new Thread(new Runnable() {
            public void run() {
                executeTurn(m_turn);
                refreshStats();
                for (int i = 0; i < m_participants; ++i) {
                    m_turn[i] = null;
                }
                m_dispatch = null;
            }
        });
        m_dispatch.start();
    }
    
    /**
     * Request a move from a particular trainer.
     */
    private void requestMove(int trainer) {
        if (trainer >= m_clients.size())
            return;
        NetClient client = (NetClient)m_clients.get(trainer);
        client.sendMessage(new RequestMoveMessage(getId()));
    }
    
    /**
     * Request moves for the next turn.
     */
    protected void requestMoves() {       
        synchronized (m_clients) {
            for (int i = 0; i < m_participants; ++i) {  
                requestMove(i);
            }
        }
    }
    
}
