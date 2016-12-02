/*
 * BotBattle.java
 * 
 * Created on Sep 2, 2007, 1:30:29 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shoddybattleclient;
import java.util.*;
import shoddybattle.*;
import mechanics.*;
import netbattle.messages.*;
import mechanics.moves.*;

/**
 *
 * @author ben
 */
public class BotBattle {
    public static final int SWITCH = 0;
    public static final int MOVE = 1;
    
    private BattleBot m_bot;
    private int m_fid;
    private ModData m_data;
    private String m_opponent;
    private Random random = new Random();
    private int m_idx = 0;
    private PokemonSpecies m_opp;
    private Pokemon[] m_team = new Pokemon[6];
    private boolean[] m_enabled = new boolean[4];
    
    public BotBattle(ModData data, BattleBot bot, Pokemon[] team, String opponent, int fid) {
        m_data = data;
        m_bot = bot;
        m_team = team;
        m_fid = fid;
        m_opponent = opponent;
    }
    
    public int getFieldId() {
        return m_fid;
    }
    
    public void handleStatRefreshMessage(StatRefreshMessage msg) {
        for (int i = 0; i < 4; ++i) {
            m_enabled[i] = msg.isMoveEnabled(i);
        }
        
    }
    
    public void handlePartyMessage(PartyMessage msg) {
        PokemonSpeciesData speciesdata = m_data.getSpeciesData();
        m_opp = speciesdata.getSpecies(speciesdata.getPokemonByName(msg.getActivePokemon(1)));
        
        String user = msg.getActivePokemon(0);
        for (int i = 0; i < 6; i++) {
            if (m_team[i].getSpeciesName().equals(user)) {
                m_idx = i;
                break;
            }
        }
    }
    
    public void handleStatusUpdate(UpdatePokemonStatusMessage msg) {
        if ((msg.getParty() == 0) && (msg.getStatus() == null)) {
            if (msg.getState() == UpdatePokemonStatusMessage.STATUS_FAINTED) {
                m_team[msg.getIdx()].faint();
            }
        }
    }
    
    public void handleBattleEnd() {
        m_bot.sendChatMessage(m_fid, "gg");
        m_bot.sendMessage(new StatusChangeMessage(false, m_fid));
    }
    
    public void handleStatusChange(StatusChangeMessage msg) {
        if (msg.getUserName().equals(m_opponent) && (msg.isOnline() == false)) {
            m_bot.sendMessage(new StatusChangeMessage(false, m_fid));
            m_bot.addWin();
        }
    }
    
    public void handleMoveRequested() {
        new Thread(new Runnable() {
            public void run() {
                synchronized (this) {
                    try {
                        wait(500);
                    } catch (InterruptedException e) {
                        
                    }
                }
                Pokemon user = m_team[m_idx];
                int action = getBestAction(user);
                switch(action) {
                case SWITCH:
                    int bestSwitch = getBestSwitch(user);
                    m_bot.sendMessage(
                        new UseMoveMessage(BattleTurn.getSwitchTurn(bestSwitch),
                            m_fid));
                    break;
                case MOVE:
                    int bestMove = getBestMove(user);
                    m_bot.sendMessage(
                        new UseMoveMessage(BattleTurn.getMoveTurn(bestMove),
                            m_fid));
                    break;
                }
            }
        }).start();
    }
    
    public void handleReplacementRequested() {
        int bestReplacement = getBestReplacement();
        m_bot.sendMessage(
            new UseMoveMessage(BattleTurn.getSwitchTurn(bestReplacement), m_fid));
    }
    
    private int getBestAction(Pokemon p) {
        int deadCount = 0;
        for (int i = 0; i < 6; i++) {
            if (m_team[i].isFainted()) deadCount++;
        }
        if (deadCount == 5) return MOVE;
        
        double danger = 1.0;
        PokemonType[] oppTypes = m_opp.getTypes();
        PokemonType[] userTypes = p.getTypes();
        
        for (int i = 0; i < oppTypes.length; i++) {
            double effectiveness = getEffectiveness(oppTypes[i], userTypes);
            if (effectiveness > danger) {
                danger = effectiveness;
            }
        }
        if (danger <= 1.0) {
            danger = 0.05;
        } else {
            danger /= 4.0;
        }
        
        int[] oppStats = m_opp.getBaseStats();
        if (oppStats[Pokemon.S_ATTACK] > oppStats[Pokemon.S_SPATTACK]) {
            danger *= (double)oppStats[Pokemon.S_ATTACK] / 
                (double)p.getStat(Pokemon.S_DEFENCE);
        } else {
            danger *= (double)oppStats[Pokemon.S_SPATTACK] / 
                (double)p.getStat(Pokemon.S_SPDEFENCE);
        }
        if (p.getStat(Pokemon.S_SPEED) > oppStats[Pokemon.S_SPEED]) {
            danger -= 0.15;
        }
        if (danger >= 1.0) {
            danger = 0.95;
        }
        return (random.nextDouble() <= danger) ? SWITCH : MOVE;
    }
    
    private int getBestSwitch(Pokemon p) {
        PokemonType[] oppTypes = m_opp.getTypes();
        PokemonType[] userTypes = p.getTypes();
        PokemonType dangerType = null;
        for (int i = 0; i < oppTypes.length; i++) {
            double effectiveness = getEffectiveness(oppTypes[i], userTypes);
            if (effectiveness > 1.0) {
                dangerType = oppTypes[i];
                break;
            } 
        }
        if (dangerType == null) {
            dangerType = oppTypes[random.nextInt(oppTypes.length)];
        }
        int bestIdx = -1;
        double bestResist = 1.0;
        for (int i = 0; i < 6; i++) {
            if (m_team[i].isFainted()) continue;
            double effectiveness = getEffectiveness(dangerType, m_team[i].getTypes());
            if (effectiveness < bestResist) {
                bestIdx = i;
                bestResist = effectiveness;
            }
        }
        if (bestIdx == -1) {
            do {
                bestIdx = random.nextInt(6);
            } while (m_team[bestIdx].equals(p) || m_team[bestIdx].isFainted());
        }
        return bestIdx;
    }
    
    private int getBestMove(Pokemon p) {
        ArrayList goodness = new ArrayList();
        for (int i = 0; i < 4; i++) {
            if (!m_enabled[i])
                continue;
            
            double effectiveness = 1.0;
            MoveListEntry entry = p.getMove(i);
            if (entry == null) {
                effectiveness = -1.0;
                goodness.add(new MoveScore(i, -1.0));
                continue;
            }
            PokemonMove move = entry.getMove();
            boolean attack = move.isAttack();
            if (!attack) {
                effectiveness = 1.0;
                goodness.add(new MoveScore(i, effectiveness));
                continue;
            }
            PokemonType[] types = m_opp.getTypes();
            
            for (int j = 0; j < types.length; j++) {
                effectiveness *= move.getType().getMultiplier(types[j]);
            }
            if (move.getType().equals(PokemonType.T_GROUND) 
                    && m_opp.canUseAbility(m_data.getSpeciesData(), "Levitate")) {
                effectiveness = 0;
            }
            if (effectiveness != 0) {
                effectiveness += 0.5;
                effectiveness += (double)move.getPower() / 1000;
                if (p.isType(move.getType())) effectiveness *= 1.2;
            }
            goodness.add(new MoveScore(i, effectiveness));
        }
        
        Collections.sort(goodness, new Comparator() {
            public int compare(Object o1, Object o2) {
                MoveScore move1 = (MoveScore)o1;
                MoveScore move2 = (MoveScore)o2;
                if (move1.getScore() > move2.getScore()) {
                    return -1;
                } else if (move1.getScore() == move2.getScore()) {
                    return 0;
                } else {
                    return 1;
                }
                
            }
        });
        
        
        int choices = goodness.size();
        
        if (choices == 0) {
            /** We have to struggle if we get here. It is probably a better
             *  idea to switch instead, but that can't easily be done with this
             *  design.
             */
            return 0;
        }
        
        MoveScore score = (MoveScore)goodness.get(0);
        do {
            double rand = random.nextDouble();
            if (score.getScore() > 2.0) {
                if ((rand <= 0.85) || (choices == 1)) {
                    score = (MoveScore)goodness.get(0);
                } else if ((rand <= 0.93) || (choices == 2)) {
                    score = (MoveScore)goodness.get(1);
                } else if ((rand <= 0.98) || (choices == 3)) {
                    score = (MoveScore)goodness.get(2);
                } else {
                    score = (MoveScore)goodness.get(3);
                }
            } else {
                if ((rand <= 0.65) || (choices == 1)) {
                    score = (MoveScore)goodness.get(0);
                } else if ((rand <= 0.85) || (choices == 2)) {
                    score = (MoveScore)goodness.get(1);
                } else if ((rand <= 0.95) || (choices == 3)) {
                    score = (MoveScore)goodness.get(2);
                } else {
                    score = (MoveScore)goodness.get(3);
                }
            }
        } while (score.getScore() == 0);
        
        int choice = score.getIndex();
        
        return choice;
    }
    
    public int getBestReplacement() {
        PokemonType[] oppTypes = m_opp.getTypes();
        for (int i = 0; i < 6; i++) {
            Pokemon p = m_team[i];
            if (m_team[i].isFainted()) continue;
            for (int j = 0; j < 4; j++) {
                MoveListEntry entry = p.getMove(j);
                if (entry == null) continue;
                PokemonMove move = entry.getMove();
                if (!move.isAttack()) continue;
                PokemonType type = move.getType();
                double effectiveness = 1.0;
                for (int k = 0; k < oppTypes.length; k++) {
                    effectiveness *= type.getMultiplier(oppTypes[k]);
                }
                if (type.equals(PokemonType.T_GROUND) && m_opp.canUseAbility(
                        m_data.getSpeciesData(), "Levitate")) {
                    effectiveness = 0;
                }
                if (effectiveness > 1.0) return i;
            }
        }
        return random.nextInt(6);
    }
    
    private double getEffectiveness(PokemonType type, PokemonType[] defender) {
        double effectiveness = 1.0;
        for (int i = 0; i < defender.length; i++) {
            effectiveness *= type.getMultiplier(defender[i]);
        }
        return effectiveness;
    }

}