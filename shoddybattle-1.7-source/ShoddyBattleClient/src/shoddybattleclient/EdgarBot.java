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
public class EdgarBot extends BotClient {
    private ModData m_data;
    private PokemonSpecies opp;
    private boolean m_battling = false;
    Pokemon[] m_team = new Pokemon[6];
    private int m_wins;
    private int m_turn;
    private Random random = new Random();
    
    private String[] godMessages = new String[] {"GOD DOESNT EXIST",
        "science is the absolute law of the universe",
        "science is the onnly real thing",
        "god just isnt real science is the only explanation",
        "god doesnt make any sense",
        "science explains everything its the rule of the universe",
        "religion is lies look at science"};
        
    
    public void initialise(ModData data, UserListMessage msg) {
        m_data = data;
        m_wins = 0;
        sendChatMessage(-1, "battle edgar clause");
    }
    
    protected void handleFieldTextMessage(FieldTextMessage text) {
        String[] parts = text.getTextMessage().split(":", 2);
        if (parts[0].equals("gared")) return;
        String message = parts[1].toLowerCase();
        if (message.indexOf("god") != -1) {
            sendChatMessage(text.getFieldId(), godMessages[random.nextInt(godMessages.length)]);
            return;
        }
        if ((message.indexOf("gared") != -1) && (message.indexOf("bot") != -1)) {
            sendChatMessage(text.getFieldId(), "no im not!!");
            return;
        }
        
    }
    
    protected void handlePartyMessage(PartyMessage msg) {
        PokemonSpeciesData speciesdata = m_data.getSpeciesData();
        opp = speciesdata.getSpecies(speciesdata.getPokemonByName(msg.getActivePokemon(1)));
    }
    
    protected void handleIssueChallengeMessage(IssueChallengeMessage msg) {
        
        if (!m_battling) {
            m_team = null;
            m_team = new Pokemon[6];
            /*for (int i = 0; i < 6; i++) {
                m_team[i] = Pokemon.getRandomPokemon(m_data, new AdvanceMechanics(4), 
                        ou[random.nextInt(ou.length)]);
            }*/
            File edgar = new File("edgar");
            Pokemon.loadTeam(edgar, m_team);
            acceptChallenge(msg.getOpponent(), m_team);
            m_battling = true;
            m_turn = 1;
        } else {
            acceptChallenge(msg.getOpponent(), null);
        }
    }
    
    protected void handleRequestMoveMessage(RequestMoveMessage msg) {
        Pokemon user = m_team[0];
        
        PokemonType[] oppTypes = opp.getTypes();

        if (m_turn == 1) {
            sendMessage(
                new UseMoveMessage(BattleTurn.getMoveTurn(3), msg.getFieldId()));
        } else if (m_turn == 2) {
            sendMessage(
                new UseMoveMessage(BattleTurn.getMoveTurn(0), msg.getFieldId()));
        } else {
            double[] effectiveness = new double[] { 1.0, 1.0 };
            for (int i = 1; i < 3; i++) {
                MoveListEntry entry = user.getMove(i);
                PokemonMove move = entry.getMove();
                for (int j = 0; j < oppTypes.length; j++) {
                    effectiveness[i - 1] *= move.getType().getMultiplier(oppTypes[j]);
                }
                if (move.getType().equals(PokemonType.T_GROUND) &&
                        opp.canUseAbility(m_data.getSpeciesData(), "Levitate")) {
                    effectiveness[i - 1] = 0;
                }
            }
            int bigger = effectiveness[0] > effectiveness[1] ? 1 : 2;
            sendMessage(
                new UseMoveMessage(BattleTurn.getMoveTurn(bigger), msg.getFieldId()));
        }
        m_turn++;
    }
    
    protected void handleReplacePokemonMessage(ReplacePokemonMessage msg) {
        sendMessage(
            new StatusChangeMessage(false, msg.getFieldId()));
        m_battling = false;
        System.out.println("quit");
        sendChatMessage(-1, "battle edgar clause");
    }
    
    protected void handleBattleEndMessage(BattleEndMessage msg) {
        sendChatMessage(msg.getFieldId(), "Good game!");
        sendMessage(
            new StatusChangeMessage(false, msg.getFieldId()));
        m_battling = false;
        sendChatMessage(-1, "battle edgar clause");
        if (msg.getVictor() == 0) {
            m_wins++;
        }
        System.out.println(m_wins + " wins");
    }

}
