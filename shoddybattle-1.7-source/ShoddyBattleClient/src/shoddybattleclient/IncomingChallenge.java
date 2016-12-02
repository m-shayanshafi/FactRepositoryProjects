/*
 * IncomingChallenge.java
 *
 * Created on December 21, 2006, 9:39 PM
 */

package shoddybattleclient;
import javax.swing.*;
import shoddybattle.*;
import netbattle.*;
import java.util.*;
import java.io.*;
import java.awt.event.*;

/**
 *
 * @author  Colin
 */
public class IncomingChallenge extends ChallengeWindow {
    
    /** Creates new form IncomingChallenge */
    public IncomingChallenge(HumanClient server, String source, boolean[] clauses) {
        super(server, source);
        setButtonNames("Accept", "Reject");
        MouseListener[] listener = m_clauseList.getMouseListeners();
        for (int i = 0; i < listener.length; ++i) {
            m_clauseList.removeMouseListener(listener[i]);
        }
        if (clauses != null) {
            for (int i = 0; i < m_clauses.length; ++i) {
                if (clauses.length == m_clauses.length) {
                    m_clauses[i].setSelected(clauses[i]);
                }
                m_clauses[i].setEnabled(false);
            }
        }
        m_clauseList.setEnabled(false);
    }
    
    protected void setDescription(String source) {
        setStatusText("You have been challenged by " + source + ".");
    }

    protected void executeChallenge() {
        if (m_server != null) {
            Pokemon[] team = m_team;
            if (m_team == null) {
                team = new Pokemon[6];
            }
            m_server.acceptChallenge(m_target, team);
        }
        dispose();
    }

    public void cancelChallenge() {
        if (m_server != null) {
            m_server.acceptChallenge(m_target, null);
        }
        dispose();
    }
    
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new IncomingChallenge(null, "bearzly", new boolean[] {
                    true, true, false, false, true, false, false, true
                }).setVisible(true);
            }
        });
    }
    
}

