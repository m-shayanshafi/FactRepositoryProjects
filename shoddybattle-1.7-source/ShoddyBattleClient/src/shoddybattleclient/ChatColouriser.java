/*
 * ChatColouriser.java
 *
 * Created on May 17, 2007, 8:27 PM
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
import javax.swing.*;
import javax.swing.text.html.*;
import javax.swing.event.*;
import java.net.URL;

/**
 * A class that supplements a JTextPane by colouring its text.
 * @author Colin
 */
public abstract class ChatColouriser {
    
    /**
     * Maximum number of lines before the log is truncated.
     */
    private static final int MAXIMUM_LINES = 150;
    
    private JTextPane m_pane;
    private String m_user, m_opponent;
    private static String m_styles[] = {
            "color: #009900;",
            "color: #990000;",
            "color: #000099;"
        };
    private int m_lines = 0;
    private int m_actualLines = 0;
    
    /**
     * Encode HTML entities.
     * Copied from www.owasp.org.
     */
    public static String htmlEntityEncode(String s) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ((c >= 'a' && c <= 'z') ||
                    (c >= 'A' && c <= 'Z') ||
                    (c >= '0' && c <= '9')) {
                buf.append(c);
            } else {
                buf.append("&#" + (int)c + ";");
            }
        }
        return buf.toString();
    }
    
    /**
     * Post a message to the chat.
     */
    public synchronized void addMessage(String msg, boolean important) {
        if (msg == null)
            return;
        int idx = msg.indexOf(':');
        if (idx != -1) {
            String name = msg.substring(0, idx).trim();
            String cls = "<b style=\"";
            if (name.equals(m_user)) {
                cls += m_styles[0];
            } else if (name.equals(m_opponent)) {
                cls += m_styles[1];
            } else {
                cls += m_styles[2];
            }
            cls += "\">";
            String importantBegin = "", importantEnd = "";
            if (important) {
                importantBegin = "<font style='font-size: 15pt; font-weight: bold; color: #990000'>";
                importantEnd = "</font>";
            }
            msg = cls + htmlEntityEncode(name) + "</b>" + importantBegin
                    + htmlEntityEncode(msg.substring(idx))
                        .replaceAll("&#32;", " ")
                        .replaceAll("\\b([^ ]*&#58;&#47;&#47;[^ ]+)",
                            "<a href=\"$1\">$1</a>")
                    + importantEnd;
        } else if (msg.equals("---")) {
            msg = "<hr>";
        } else {
            msg = htmlEntityEncode(msg);
        }
        msg = "<div>" + msg + "<i>&nbsp;</i></div>";
        HTMLDocument doc = (HTMLDocument)m_pane.getDocument();
        HTMLEditorKit kit = (HTMLEditorKit)m_pane.getEditorKit();
        try {
            kit.insertHTML(doc, doc.getLength(), msg, 1, 0, HTML.Tag.DIV);
            if (++m_lines > MAXIMUM_LINES) {
                m_lines = MAXIMUM_LINES;
                int newline = 0;
                int size = 50;
                int offset = -size + 1;
                int length = doc.getLength();
                //StringBuffer buffer = new StringBuffer();
                while (true) {
                    offset += size;
                    if ((offset + size) > length) {
                        size = length - offset;
                    }
                    String text = doc.getText(offset, size);
                    newline = text.indexOf("\n");
                    if (newline != -1) {
                        //buffer.append(text.substring(0, newline));
                        break;
                    }
                    //buffer.append(text);
                }
                //String line = new String(buffer).trim();
                /** TODO: Perhaps uncomment the commented lines and write
                 *  'line' - the line that was removed - to some log file
                 *  for later viewing. */
                int position = offset + newline;
                doc.remove(0, position);
            }
        } catch (Exception e) {
            
        }
        m_pane.setCaretPosition(doc.getLength());
        try {
            if (++m_actualLines > MAXIMUM_LINES) {
                m_actualLines = 0;
                String text = doc.getText(0, doc.getLength());
                String[] lines = text.split("\n");

                clear();
                for (int i = 1; i < lines.length; ++i) {
                    addMessage(lines[i], false);
                }
                
                // The following line is *not* redundant.
                m_actualLines = 0;
                
                // Nor is this one.
                m_pane.setCaretPosition(doc.getLength());
            }
        } catch (Exception e) {
            
        }
    }
    
    /**
     * Clear the chat.
     */
    public void clear() {
        m_pane.setText("");
        m_lines = 0;
    }
    
    /**
     * Creates a new instance of ChatColouriser
     */
    public ChatColouriser(JTextPane pane, String user, String opponent) {
        m_pane = pane;
        m_user = user;
        m_opponent = opponent;
        
        m_pane.setEditable(false);
        m_pane.setContentType("text/html");
        m_pane.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    openPage(e.getURL());
                }
            }
        });

        HTMLEditorKit kit = new HTMLEditorKit();
        StyleSheet css = new StyleSheet();
        css.addRule("body { background-color: #FFFFFF }");
        css.addRule("a { text-decoration: underline; color: blue; }");
        /**css.addRule("b { font-weight: bold; }");
        css.addRule("b.user { color: #009900; }");
        css.addRule("b.opponent { color: #990000; }");
        css.addRule("b.others { color: #000099; }");**/
        
        kit.setStyleSheet(css);
        m_pane.setEditorKit(kit);
    }
    
    /**
     * Display the specified page to the user.
     */
    public abstract void openPage(URL url);
    
    public static void main(String[] args) {
        JTextPane pane = new JTextPane();
        pane.setEditable(false);
        final ChatColouriser colouriser = new ChatColouriser(pane, "Colin", "Ben") {
            public void openPage(URL url) {
                System.out.println(url.toString());
            }
        };
        /**colouriser.addMessage("Colin: Hello there!", false);
        colouriser.addMessage("Ben: Hoho, I am not much of a polymath.", true);
        colouriser.addMessage("---", false);
        colouriser.addMessage("Random Person: I agree with Ben.", false);
        colouriser.addMessage("Random Person: In fact the lot of you should "
                + "check out http://benisnotapolymath.com", false);**/
        JFrame frame = new JFrame("Testing ChatPane");
        frame.setSize(300, 300);
        frame.getContentPane().add(pane);
        pane.setLocation(0, 0);
        pane.setSize(frame.getSize());
        pane.setVisible(true);
        frame.setVisible(true);
        new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < 1000; ++i) {
                    colouriser.addMessage(String.valueOf(i), false);
                    synchronized (this) {
                        try {
                            wait(100);
                        } catch (InterruptedException e) {
                            
                        }
                    }
                }
            }
        }).start();
    }
    
}
