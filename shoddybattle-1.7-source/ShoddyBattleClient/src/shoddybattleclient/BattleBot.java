/*
 * BattleBot.java
 * 
 * Created on Aug 25, 2007, 10:52:02 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shoddybattleclient;
import netbattle.messages.*;
import shoddybattle.*;
import mechanics.*;
import mechanics.moves.*;
import java.util.*;
import java.io.File;

/**
 *
 * @author ben
 */;
public class BattleBot extends BotClient {
    private ModData m_data;
    private Random random = new Random();
    private int m_wins;
    private int m_losses;
    private static final String[] m_files = new String[] { "Fred", "Fred copy", 
        "Fred copy 1"};
    private ArrayList m_battles = new ArrayList();
    private HashMap m_opponentMap = new HashMap();
    
    public void initialise(ModData data, UserListMessage msg) {
        m_data = data;
        m_wins = 0;
        m_losses = 0;
        sendChatMessage(-1, "Hi guys, anyone want to battle?");
        new Thread(new Runnable() {
            public void run() {
                synchronized (this) {
                    try {
                        wait(1000 * 60 * 8);
                    } catch (InterruptedException e) {
                        System.out.println("interrupted");
                    }
                    sendChatMessage(-1, "Anyone want to battle?");
                }
            }
        });
    }
    
    protected void handleFieldTextMessage(FieldTextMessage text) {
        //System.out.println(text.getMessage());
    }
    
    protected void handlePartyMessage(PartyMessage msg) {
        BotBattle battle = getBattle(msg.getFieldId());
        if (battle == null) return;
        battle.handlePartyMessage(msg);
    }
    
    protected void handleIssueChallengeMessage(IssueChallengeMessage msg) {
        /*String[] ou = new String[]{ "Slowbro",
"Gengar",
"Weezing",
"Starmie",
"Gyarados",
"Aerodactyl",
"Dragonite",
"Heracross",
"Skarmory",
"Donphan",
"Blissey",
"Celebi",
"Swampert",
"Breloom",
"Salamence",
"Metagross",
"Jirachi",
"Infernape",
"Bronzong",
"Garchomp",
"Lucario",
"Hippowdon",
"Weavile",
"Magnezone",
"Rhyperior",
"Tangrowth",
"Electivire",
"Togekiss",
"Gliscor",
"Porygonz",
"Dusknoir",
"Azelf",
"Heatran",
"Cresselia"};

        for (int i = 0; i < 6; i++) {
            m_team[i] = Pokemon.getRandomPokemon(m_data, new AdvanceMechanics(4), 
                    ou[random.nextInt(ou.length)]);
        }*/
        Pokemon[] team = new Pokemon[6];
        File fred = new File(m_files[random.nextInt(m_files.length)]);
        Pokemon.loadTeam(fred, team);
        m_opponentMap.put(msg.getOpponent(), team);
        acceptChallenge(msg.getOpponent(), team);
    }
    
    protected void handleStatRefreshMessage(StatRefreshMessage msg) {
        BotBattle battle = getBattle(msg.getFieldId());
        if (battle == null) return;
        battle.handleStatRefreshMessage(msg);
    }
    
    protected void handleSelectionEndMessage(SelectionEndMessage msg) {
        BotBattle battle = getBattle(msg.getFieldId());
        if (battle == null) return;
        battle.handleSelectionEndMessage();
    }
    
    protected void handleBattleReadyMessage(BattleReadyMessage msg) {
        String opponent = msg.getUsers()[1];
        Pokemon[] team = (Pokemon[])m_opponentMap.get(opponent);
        m_opponentMap.remove(opponent);
        m_battles.add(new BotBattle(m_data, this, team, opponent, msg.getFieldId()));
    }
    
    protected void handleStatusChangeMessage(StatusChangeMessage msg) {
        BotBattle battle = getBattle(msg.getFieldId());
        if (battle == null) return;
        battle.handleStatusChange(msg);
    }
    
    protected void handleRequestMoveMessage(RequestMoveMessage msg) {
        BotBattle battle = getBattle(msg.getFieldId());
        if (battle == null) return;
        battle.handleMoveRequested();
    }
    
    protected void handleReplacePokemonMessage(ReplacePokemonMessage msg) {
        BotBattle battle = getBattle(msg.getFieldId());
        if (battle == null) return;
        battle.handleReplacementRequested();
    }
    
    protected void handleUpdatePokemonStatusMessage(UpdatePokemonStatusMessage msg) {
        BotBattle battle = getBattle(msg.getFid());
        if (battle == null) return;
        battle.handleStatusUpdate(msg);
    }
    
    protected void handleBattleEndMessage(BattleEndMessage msg) {
        BotBattle battle = getBattle(msg.getFieldId());
        if (battle == null) return;
        m_battles.remove(battle);
        battle.handleBattleEnd();
        sendChatMessage(-1, "Anyone else want to battle?");
        if (msg.getVictor() == 0) {
            m_wins++;
        } else {
            m_losses++;
        }
        System.out.println(m_wins + "-" + m_losses);
    }
    
    private BotBattle getBattle(int fid) {
        Iterator i = m_battles.iterator();
        while (i.hasNext()) {
            BotBattle battle = (BotBattle)i.next();
            if (battle.getFieldId() == fid) return battle;
        }
        return null;
    }
    
    public void addWin() {
        m_wins++;
    }

}

class MoveScore {
    private int m_idx;
    private double m_score;
    
    public MoveScore(int idx, double score) {
        m_idx = idx;
        m_score = score;
    }
    public double getScore() {
        return m_score;
    }
    public int getIndex() {
        return m_idx;
    }
}
