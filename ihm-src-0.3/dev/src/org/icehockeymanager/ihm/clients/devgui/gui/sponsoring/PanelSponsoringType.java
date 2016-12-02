/************************************************************* 
 * 
 * Ice Hockey Manager 
 * ================== 
 * 
 * Copyright (C) by the IHM Team (see doc/credits.txt) 
 * 
 * This program is released under the GPL (see doc/gpl.txt) 
 * 
 * Further informations: http://www.icehockeymanager.org  
 * 
 *************************************************************/ 
  
package org.icehockeymanager.ihm.clients.devgui.gui.sponsoring;

import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.sponsoring.*;
import org.icehockeymanager.ihm.game.sponsoring.*;
import org.icehockeymanager.ihm.game.team.*;

public class PanelSponsoringType extends JIhmPanel {

  private static final long serialVersionUID = 6486075952156934262L;

  private JIhmList listOfferedContracts;

  private JIhmLabel lblName;

  private JIhmLabel lblHappiness;

  private JIhmButton btnSign;

  private JIhmLabel lblSignedContract;

  private JIhmButton btnNegotiate;

  private JIhmLabel lblAmount;

  private Vector<SponsoringContract> offeredContracts;

  private SponsoringContract signedContract;

  private String sponsoringType;

  private Team team;

  public PanelSponsoringType() {
    super();
    initGUI();
    this.setBorder(BorderFactory.createTitledBorder("blahblah"));
  }

  private void initGUI() {
    try {
      this.setPreferredSize(new java.awt.Dimension(711, 130));
      this.setLayout(null);
      {
        lblAmount = new JIhmLabel();
        this.add(lblAmount);
        lblAmount.setText("lblName");
        lblAmount.setBounds(215, 38, 198, 30);
      }
      {
        ListModel listOfferedContractsModel = new DefaultComboBoxModel(new String[] { "Item One", "Item Two" });
        listOfferedContracts = new JIhmList();
        this.add(listOfferedContracts);
        listOfferedContracts.setModel(listOfferedContractsModel);
        listOfferedContracts.setBounds(10, 20, 190, 96);
        listOfferedContracts.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
        listOfferedContracts.setFont(new java.awt.Font("Monospaced", 1, 12));
        listOfferedContracts.addMouseListener(new MouseAdapter() {
          public void mouseClicked(MouseEvent evt) {
            listOfferedContractsMouseClicked(evt);
          }
        });
      }
      {
        lblName = new JIhmLabel();
        this.add(lblName);
        lblName.setText("lblName");
        lblName.setBounds(215, 18, 198, 30);
      }
      {
        lblHappiness = new JIhmLabel();
        this.add(lblHappiness);
        lblHappiness.setText("lblHappiness");
        lblHappiness.setBounds(215, 58, 198, 30);
      }
      {
        btnNegotiate = new JIhmButton();
        this.add(btnNegotiate);
        btnNegotiate.setText("sponsoring.negotiate");
        btnNegotiate.setMsgKey("sponsoring.negotiate");
        btnNegotiate.setBounds(427, 19, 135, 30);
        btnNegotiate.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            btnNegotiateActionPerformed(evt);
          }
        });
      }
      {
        btnSign = new JIhmButton();
        this.add(btnSign);
        btnSign.setText("sponsoring.sign");
        btnSign.setMsgKey("sponsoring.sign");
        btnSign.setBounds(427, 54, 135, 30);
        btnSign.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            btnSignActionPerformed(evt);
          }
        });
      }
      {
        lblSignedContract = new JIhmLabel();
        this.add(lblSignedContract);
        lblSignedContract.setText("lblSignedContract");
        lblSignedContract.setBounds(217, 88, 483, 30);
        lblSignedContract.setFont(new java.awt.Font("Dialog", 1, 14));
        lblSignedContract.setForeground(new java.awt.Color(30, 144, 255));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setContracts(String sponsoringType, Team team) {
    this.setBorder(BorderFactory.createTitledBorder(SponsoringTools.getTypeDescription(sponsoringType)));

    this.sponsoringType = sponsoringType;
    this.team = team;

    refresAllContracts();
  }

  public void refresAllContracts() {
    try {

      this.offeredContracts = this.team.getSponsoring().getOfferdContractsByType(sponsoringType);
      this.signedContract = this.team.getSponsoring().getSignedContractByType(sponsoringType);

      this.listOfferedContracts.setModel(new LMOfferedContracts(offeredContracts));
      if (offeredContracts.size() > 0) {
        this.listOfferedContracts.setSelectedIndex(0);
      }

      if (signedContract != null) {
        this.lblName.setText("");
        this.lblHappiness.setText("");
        this.lblAmount.setText("");
        this.btnNegotiate.setEnabled(false);
        this.btnSign.setEnabled(false);
        this.listOfferedContracts.setEnabled(false);
        this.lblSignedContract.setText(SponsoringTools.getSponsoringDescription(signedContract));
      } else {
        SponsoringContract contract = getCurrentContract();
        if (contract != null) {
          this.refreshCurrentContract();
        } else {
          this.lblName.setText("");
          this.lblHappiness.setText("");
          this.lblAmount.setText("");
          this.lblSignedContract.setText("");
          this.btnNegotiate.setEnabled(false);
          this.btnSign.setEnabled(false);
        }
      }
    } catch (Exception err) {

    }
  }

  private void refreshCurrentContract() {
    try {
      SponsoringContract contract = getCurrentContract();
      if (contract != null) {
        this.lblName.setText(SponsoringTools.getSponsorLabelAndName(contract));
        this.lblHappiness.setText(SponsoringTools.getSponsorLabelAndHappiness(contract));
        this.lblAmount.setText(SponsoringTools.getSponsorLabelAndAmount(contract));
        this.btnNegotiate.setEnabled(true);
        this.btnSign.setEnabled(true);
        this.lblSignedContract.setText("");
      } else {
        this.lblName.setText("");
        this.lblHappiness.setText("");
        this.lblAmount.setText("");
        this.lblSignedContract.setText("");
        this.btnNegotiate.setEnabled(false);
        this.btnSign.setEnabled(false);
      }
    } catch (Exception err) {

    }
    this.listOfferedContracts.invalidate();
    this.listOfferedContracts.validate();
    this.listOfferedContracts.updateUI();
  }

  private void btnNegotiateActionPerformed(ActionEvent evt) {
    SponsoringContract tmp = getCurrentContract();
    try {
      this.team.getSponsoring().negotiateContract(tmp);
      this.refreshCurrentContract();
    } catch (SponsoringNegotiationException err) {
      JOptionPane.showMessageDialog(ClientController.getInstance().getDesktop(), ClientController.getInstance().getTranslation("sponsoring.negotiation.failed"));
      this.refresAllContracts();
    }

  }

  private SponsoringContract getCurrentContract() {
    LMOfferedContracts lm = (LMOfferedContracts) listOfferedContracts.getModel();
    int row = listOfferedContracts.getSelectedIndex();
    if (row >= 0) {
      return lm.getContract(row);
    } else {
      return null;
    }
  }

  private void listOfferedContractsMouseClicked(MouseEvent evt) {
    if (this.signedContract == null) {
      this.refreshCurrentContract();
    }

  }

  private void btnSignActionPerformed(ActionEvent evt) {
    this.team.getSponsoring().signContract(getCurrentContract());
    this.refresAllContracts();
  }

}
