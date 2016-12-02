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
  
package org.icehockeymanager.ihm.game.sponsoring;

import java.util.*;

import org.icehockeymanager.ihm.game.GameController;
import org.icehockeymanager.ihm.game.finance.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.lib.ToolsXML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.*;

/**
 * Sponsoring - Sponsoring object of team
 * 
 * @author adasen, Bernhard von Gunten
 * @created Oct, 2004
 */
public class Sponsoring implements Serializable {

  /** Main sponsor */
  public static final String SPONSOR_TYPE_MAIN = "MAIN";

  /** Media Sponsor TV */
  public static final String SPONSOR_TYPE_MEDIA_TV = "MEDIA_TV";

  /** Media Sponsor RADIO */
  public static final String SPONSOR_TYPE_MEDIA_RADIO = "MEDIA_RADIO";

  /** Media Sponsor TV */
  public static final String SPONSOR_TYPE_MEDIA_WEB = "MEDIA_WEB";

  /** Arena Sponsor */
  public static final String SPONSOR_TYPE_ARENA_BOARD = "ARENA_BOARD";

  /** Arena Ice Sponsor */
  public static final String SPONSOR_TYPE_ARENA_ICE = "ARENA_ICE";

  /** Team Sponsor */
  public static final String SPONSOR_TYPE_TEAM_MAIN = "TEAM_MAIN";

  /** Team Sponsor */
  public static final String SPONSOR_TYPE_TEAM_STICK = "TEAM_STICK";

  /** Team Sponsor */
  public static final String SPONSOR_TYPE_TEAM_EQUIPMENT = "TEAM_EQUIPMENT";

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3690189932564263221L;

  /**
   * team (owner)k
   */
  private Team team = null;

  /**
   * Signed contracts
   */
  private Vector<SponsoringContract> signedContracts = null;

  /**
   * Offered contracts
   */
  private Vector<SponsoringContract> offeredContracts = null;

  /**
   * Sponsoring constructor
   * 
   * @param team
   */
  public Sponsoring(Team team) {
    this.team = team;
    signedContracts = new Vector<SponsoringContract>();
    offeredContracts = new Vector<SponsoringContract>();
  }

  /**
   * Resets all the offered Contracts and check old contracts 
   */
  public void newSeason() {
    this.checkValidContracts();
    this.generateOfferedContracts();
  }
  
  
  /**
   * Calculate and return revenues
   * 
   * @return Revenues
   */
  public double getTotalRevenues() {
    double tempRevenues = 0;
    for (int i = 0; i < signedContracts.size(); i++) {
      tempRevenues += signedContracts.get(i).getRevenues();
    }
    return tempRevenues;
  }
  
  /**
   * Calculate and return revenues
   * @param date 
   * @return Revenues
   */
  public Vector<BookingEntry> getRevenues(Calendar date) {
    Vector <BookingEntry> result = new Vector <BookingEntry>();
    for (int i = 0; i < signedContracts.size(); i++) {
      result.add(new BookingEntry(signedContracts.get(i).getRevenues(), (Calendar) date.clone(), "accounting.booking.sponsoring"));
    }
    return result;
  }

  /**
   * remove outdated contracts
   */
  public void checkValidContracts() {
    int i = 0;
    while (i < signedContracts.size()) {
      if (!signedContracts.elementAt(i).isValid()) {
        signedContracts.removeElementAt(i);
      } else {
        i++;
      }
    }
  }

  /**
   * generate new sponsoring contracts to be offered
   */
  public void generateOfferedContracts() {
    offeredContracts = new Vector<SponsoringContract>();
    appendOfferedContracts(offeredContracts, SPONSOR_TYPE_MAIN, GameController.getInstance().getScenario().getScenarioSettings().SPONSORING_TYPE_MAIN_VALUE);
    appendOfferedContracts(offeredContracts, SPONSOR_TYPE_ARENA_BOARD, GameController.getInstance().getScenario().getScenarioSettings().SPONSORING_TYPE_ARENA_BOARD_VALUE);
    appendOfferedContracts(offeredContracts, SPONSOR_TYPE_ARENA_ICE, GameController.getInstance().getScenario().getScenarioSettings().SPONSORING_TYPE_ARENA_ICE_VALUE);
    appendOfferedContracts(offeredContracts, SPONSOR_TYPE_MEDIA_TV, GameController.getInstance().getScenario().getScenarioSettings().SPONSORING_TYPE_MEDIA_TV_VALUE);
    appendOfferedContracts(offeredContracts, SPONSOR_TYPE_MEDIA_RADIO, GameController.getInstance().getScenario().getScenarioSettings().SPONSORING_TYPE_MEDIA_RADIO_VALUE);
    appendOfferedContracts(offeredContracts, SPONSOR_TYPE_MEDIA_WEB, GameController.getInstance().getScenario().getScenarioSettings().SPONSORING_TYPE_MEDIA_WEB_VALUE);
    appendOfferedContracts(offeredContracts, SPONSOR_TYPE_TEAM_MAIN, GameController.getInstance().getScenario().getScenarioSettings().SPONSORING_TYPE_TEAM_MAIN_VALUE);
    appendOfferedContracts(offeredContracts, SPONSOR_TYPE_TEAM_EQUIPMENT, GameController.getInstance().getScenario().getScenarioSettings().SPONSORING_TYPE_TEAM_EQUIPMENT_VALUE);
    appendOfferedContracts(offeredContracts, SPONSOR_TYPE_TEAM_STICK, GameController.getInstance().getScenario().getScenarioSettings().SPONSORING_TYPE_TEAM_STICK_VALUE);
  }

  /**
   * Generates Sponsors for a sponsoring type including value, and adds it to a
   * vector
   * 
   * @param appendTo
   * @param type
   * @param sponsoringValue
   */
  private void appendOfferedContracts(Vector<SponsoringContract> appendTo, String type, int sponsoringValue) {
    SponsorData[] sponsors = getSponsorsByType(type);
    for (int i = 0; i < 3; i++) {
      Calendar today = GameController.getInstance().getScenario().getScheduler().getToday();
      Calendar endDate = ((Calendar) today.clone());
      endDate.add(Calendar.YEAR, 2);
      String name = sponsors[i].getName();
      SponsoringContract contract = new SponsoringContract(team, null, name, type);
      double rnd = GameController.getInstance().getScenario().getRandomInt(GameController.getInstance().getScenario().getScenarioSettings().SPONSORING_AMOUNT_RANGE);
      double amountRangeChange = 1 + (rnd / 100);
      contract.setAmount(team.getTeamAttributes().getOverall() * sponsoringValue * amountRangeChange);
      contract.setStartDate(today);
      contract.setEndDate(endDate);
      appendTo.addElement(contract);
    }
  }

  /**
   * Returns all offered sponsoring contracts by sponsoring type
   * 
   * @param type
   * @return Vector of sponsoring contracts
   */
  public Vector<SponsoringContract> getOfferdContractsByType(String type) {
    Vector<SponsoringContract> result = new Vector<SponsoringContract>();
    for (int i = 0; i < offeredContracts.size(); i++) {
      if (offeredContracts.get(i).getSponsoringType().equalsIgnoreCase(type)) {
        result.add(offeredContracts.get(i));
      }
    }
    return result;
  }

  /**
   * Returns the signed sponsoring contract by sponsoring type. Null if none
   * exists.
   * 
   * @param type
   * @return sponsoring contract, null if none is found
   */
  public SponsoringContract getSignedContractByType(String type) {
    SponsoringContract result = null;
    for (int i = 0; i < signedContracts.size(); i++) {
      if (signedContracts.get(i).getSponsoringType().equalsIgnoreCase(type)) {
        return signedContracts.get(i);
      }
    }
    return result;
  }

  /**
   * Signs contract
   * 
   * @param contract
   */
  public void signContract(SponsoringContract contract) {
    if (hasExistingContractByType(contract.getSponsoringType())) {
      throw new IllegalArgumentException();
    }
    this.signedContracts.add(contract);
    this.offeredContracts.remove(contract);
  }

  /**
   * Returns true, if team has signed contract with given sponsoring type
   * 
   * @param type
   * @return boolean
   */
  public boolean hasExistingContractByType(String type) {
    for (int i = 0; i < signedContracts.size(); i++) {
      if (signedContracts.get(i).getSponsoringType().equalsIgnoreCase(type)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Negotiates contract. Updates contract, if negotiations fail, contract is
   * also deleted from offered contracts and a SponsoringNegotiationsException
   * is thrown.
   * 
   * @param contract
   * @throws SponsoringNegotiationException
   */
  public void negotiateContract(SponsoringContract contract) throws SponsoringNegotiationException {
    double bonus = GameController.getInstance().getScenario().getRandomInt(GameController.getInstance().getScenario().getScenarioSettings().SPONSORING_NEGOTIATION_INCREASE_BONUS);
    double amountRangeChange = 1 + (bonus / 100);
    double amount = amountRangeChange * contract.getAmount();

    int decrease = -1 * (GameController.getInstance().getScenario().getRandomInt(GameController.getInstance().getScenario().getScenarioSettings().SPONSORING_NEGOTIATION_HAPPINESS_DECREASE));

    contract.setAmount(amount);
    contract.increaseSponsorHappines((int) decrease);

    if (contract.getSponsorHappiness() <= 0) {
      this.offeredContracts.remove(contract);
      throw new SponsoringNegotiationException();
    }
  }

  /* Some static stuff */

  /**
   * AI for signing sponsoring contracts
   */
  public static void aiChooseSponsoringContracts() {

    Team[] teams = GameController.getInstance().getScenario().getTeams();
    for (int i = 0; i < teams.length; i++) {
      if (GameController.getInstance().getScenario().isAutoTeam(teams[i]) || GameController.getInstance().getScenario().getUserByTeam(teams[i]).isAutoSponsoring()) {

        aiChooseSponsoringContractByType(teams[i], SPONSOR_TYPE_MAIN);
        aiChooseSponsoringContractByType(teams[i], SPONSOR_TYPE_ARENA_BOARD);
        aiChooseSponsoringContractByType(teams[i], SPONSOR_TYPE_ARENA_ICE);
        aiChooseSponsoringContractByType(teams[i], SPONSOR_TYPE_MEDIA_TV);
        aiChooseSponsoringContractByType(teams[i], SPONSOR_TYPE_MEDIA_RADIO);
        aiChooseSponsoringContractByType(teams[i], SPONSOR_TYPE_MEDIA_WEB);
        aiChooseSponsoringContractByType(teams[i], SPONSOR_TYPE_TEAM_MAIN);
        aiChooseSponsoringContractByType(teams[i], SPONSOR_TYPE_TEAM_EQUIPMENT);
        aiChooseSponsoringContractByType(teams[i], SPONSOR_TYPE_TEAM_STICK);

      }
    }

  }

  private static void aiChooseSponsoringContractByType(Team team, String type) {
    // Is there on existing contract for type ?
    if (!team.getSponsoring().hasExistingContractByType(type)) {
      // Get contract offers for type
      Vector<SponsoringContract> offers = team.getSponsoring().getOfferdContractsByType(type);
      // Sign contract
      if (offers.size() > 0) {
        team.getSponsoring().signContract(offers.get(0));
      }
    }

  }

  /**
   * Read sponsors from XML Database
   * 
   * @param file File
   * @throws Exception
   * @return SponsorData[]
   */
  public static SponsorData[] readSponsorsFromXMLDatabase(File file) throws Exception {

    Vector<SponsorData> sponsors = new Vector<SponsorData>();
    Document doc = ToolsXML.loadXmlFile(file);

    Element root = doc.getDocumentElement();

    NodeList sponsorList = root.getElementsByTagName("SPONSOR");
    int count = sponsorList.getLength();
    for (int i = 0; i < count; i++) {
      Element sponsor = (Element) sponsorList.item(i);
      sponsors.add(new SponsorData(sponsor));
    }

    return sponsors.toArray(new SponsorData[sponsors.size()]);
  }

  /**
   * Returns sponsor data by sponsoring type.
   * 
   * @param type
   * @return array of SponsorData
   */
  private static SponsorData[] getSponsorsByType(String type) {
    Vector<SponsorData> result = new Vector<SponsorData>();
    SponsorData[] sponsors = GameController.getInstance().getScenario().getSponsors();
    for (int i = 0; i < sponsors.length; i++) {
      if (sponsors[i].getType().equals(type)) {
        result.addElement(sponsors[i]);
      }
    }
    return result.toArray(new SponsorData[result.size()]);
  }
}
