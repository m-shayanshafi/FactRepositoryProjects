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
  
package org.icehockeymanager.ihm.game.scenario;

import java.util.*;

import org.icehockeymanager.ihm.game.league.*;
import org.icehockeymanager.ihm.game.league.std.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.game.randomimpacts.*;
import org.icehockeymanager.ihm.game.team.*;
import org.icehockeymanager.ihm.game.user.*;
import org.icehockeymanager.ihm.game.injuries.*;
import org.icehockeymanager.ihm.game.sponsoring.*;

/**
 * Creates a test scenario from scratch
 * 
 * @author Bernhard von Gunten
 * @created January, 2002
 */
public class ScenarioDummy {

  /** All Teams in this scenario */
  private Team[] teams;

  /** All Players in this scenario */
  private Player[] players;

  /** All LeagueOwners (i.E. contries) in this scenario */
  private LeagueOwner[] leagueOwners;

  /** Users */
  private User[] users;

  /** Countries */
  private Country[] countries = null;

  /** First names (temp) */
  private Vector<String> firstNames = null;

  /** Last names (temp) */
  private Vector<String> lastNames = null;

  /**
   * Scenario settings
   */
  private ScenarioSettings scenarioSettings = null;

  /**
   * Injuries
   */
  private InjuryData[] injuries = null;

  /**
   * Random impacts
   */
  private RandomImpact[] randomImpacts = null;

  /**
   * Sponsors
   */
  private SponsorData[] sponsors = null;

  /** Constructor for the ScenarioDummy object 
   * @param scenarioSettings 
   * @param injuries 
   * @param randomImpacts 
   * @param sponsors */
  public ScenarioDummy(ScenarioSettings scenarioSettings, InjuryData[] injuries, RandomImpact[] randomImpacts, SponsorData[] sponsors) {
    this.scenarioSettings = scenarioSettings;
    this.injuries = injuries;
    this.randomImpacts = randomImpacts;
    this.sponsors = sponsors;
    createPlayersTeamsLeaguesUsers();
  }

  /**
   * Returns a dummy scenario
   * 
   * @return scenario
   */
  public Scenario getScenario() {
    String[] myNames = lastNames.toArray(new String[lastNames.size()]);
    String[] myFirstNames = firstNames.toArray(new String[firstNames.size()]);

    return new Scenario(leagueOwners, teams, players, users, countries, injuries, randomImpacts, myNames, myFirstNames, scenarioSettings, sponsors);
  }

  /**
   * Gets the teams attribute of the ScenarioDummy object
   * 
   * @return The teams value
   */
  public Team[] getTeams() {
    return teams;
  }

  /**
   * Gets the leagueOwners attribute of the ScenarioDummy object
   * 
   * @return The leagueOwners value
   */
  public LeagueOwner[] getLeagueOwners() {
    return leagueOwners;
  }

  /**
   * Gets the players attribute of the ScenarioDummy object
   * 
   * @return The players value
   */
  public Player[] getPlayers() {
    return players;
  }

  /**
   * Returns all users
   * 
   * @return The users
   */
  public User[] getUsers() {
    return users;
  }

  /** Creates a debug/test scneario */
  private void createPlayersTeamsLeaguesUsers() {

    fillFirstNames();
    fillLastNames();
    fillCountries();

    // Generate all leagues
    leagueOwners = new LeagueOwner[1];

    Random r = new Random();

    // Generate all Teams
    teams = new Team[48];
    teams[0] = new Team(0, new TeamInfo("Bern"));
    teams[1] = new Team(1, new TeamInfo("Lugano"));
    teams[2] = new Team(2, new TeamInfo("Zürich"));
    teams[3] = new Team(3, new TeamInfo("Kloten"));
    teams[4] = new Team(4, new TeamInfo("Davos"));
    teams[5] = new Team(5, new TeamInfo("Zug"));
    teams[6] = new Team(6, new TeamInfo("Rapperswil"));
    teams[7] = new Team(7, new TeamInfo("Servette"));
    teams[8] = new Team(8, new TeamInfo("Lausanne"));
    teams[9] = new Team(9, new TeamInfo("Ambri"));
    teams[10] = new Team(10, new TeamInfo("Freiburg"));
    teams[11] = new Team(11, new TeamInfo("Langnau"));

    teams[12] = new Team(12, new TeamInfo("Chur"));
    teams[13] = new Team(13, new TeamInfo("Olten"));
    teams[14] = new Team(14, new TeamInfo("Ajoje"));
    teams[15] = new Team(15, new TeamInfo("La Chaux de Fonds"));
    teams[16] = new Team(16, new TeamInfo("GCK"));
    teams[17] = new Team(17, new TeamInfo("Visp"));
    teams[18] = new Team(18, new TeamInfo("Sierre"));
    teams[19] = new Team(19, new TeamInfo("Zuchwil"));
    teams[20] = new Team(20, new TeamInfo("Basel"));
    teams[21] = new Team(21, new TeamInfo("Morges"));
    teams[22] = new Team(22, new TeamInfo("Langenthal"));
    teams[23] = new Team(23, new TeamInfo("Biel"));

    teams[24] = new Team(24, new TeamInfo("Winterthur"));
    teams[25] = new Team(25, new TeamInfo("Wetzikon"));
    teams[26] = new Team(26, new TeamInfo("Uzwil"));
    teams[27] = new Team(27, new TeamInfo("Bellinzona"));
    teams[28] = new Team(28, new TeamInfo("Dübendorf"));
    teams[29] = new Team(29, new TeamInfo("Weinfelden"));
    teams[30] = new Team(30, new TeamInfo("Frauenfeld"));
    teams[31] = new Team(31, new TeamInfo("Bülach"));
    teams[32] = new Team(32, new TeamInfo("St. Moritz"));
    teams[33] = new Team(33, new TeamInfo("Lenzerheide"));
    teams[34] = new Team(34, new TeamInfo("Herisau"));
    teams[35] = new Team(35, new TeamInfo("Wil"));

    teams[36] = new Team(36, new TeamInfo("Unterseen"));
    teams[37] = new Team(37, new TeamInfo("RB Bern"));
    teams[38] = new Team(38, new TeamInfo("Zuchwil"));
    teams[39] = new Team(39, new TeamInfo("Aarau"));
    teams[40] = new Team(40, new TeamInfo("Lyss"));
    teams[41] = new Team(41, new TeamInfo("Zunzgen"));
    teams[42] = new Team(42, new TeamInfo("Napf"));
    teams[43] = new Team(43, new TeamInfo("Wiki"));
    teams[44] = new Team(44, new TeamInfo("Napf"));
    teams[45] = new Team(45, new TeamInfo("Burgdorf"));
    teams[46] = new Team(46, new TeamInfo("Wettingen"));
    teams[47] = new Team(47, new TeamInfo("Thun"));

    // Generate all Players
    players = new Player[1500];

    Calendar today = Calendar.getInstance();

    // Set normal field players (27 players exactly for one team (including 3
    // goalies, 9 defense, 9 wings, 9 centers)
    int begOverall = 80;
    for (int i = 0; i < teams.length; i++) {
      for (int n = 0 + (i * 27); n < 3 + (i * 27); n++) {
        players[n] = Player.createRandomPlayer(r, today, n, PlayerAttributes.POSITION_GOALTENDING, this.getLastNames(), this.getFirstNames(), 18, 40, begOverall, begOverall + 1, countries);
      }
      for (int n = 3 + (i * 27); n < 13 + (i * 27); n++) {
        players[n] = Player.createRandomPlayer(r, today, n, PlayerAttributes.POSITION_DEFENSE, this.getLastNames(), this.getFirstNames(), 18, 40, begOverall, begOverall + 1, countries);
      }
      for (int n = 13 + (i * 27); n < 18 + (i * 27); n++) {
        players[n] = Player.createRandomPlayer(r, today, n, PlayerAttributes.POSITION_CENTER, this.getLastNames(), this.getFirstNames(), 18, 40, begOverall, begOverall + 1, countries);
      }
      for (int n = 18 + (i * 27); n < 27 + (i * 27); n++) {
        players[n] = Player.createRandomPlayer(r, today, n, PlayerAttributes.POSITION_WING, this.getLastNames(), this.getFirstNames(), 18, 40, begOverall, begOverall + 1, countries);
      }
      begOverall -= 1;
    }

    // Add players to teams again
    int count = 0;
    for (int i = 0; i < teams.length; i++) {
      for (int n = 0; n < 27; n++) {
        teams[i].addPlayer(players[count]);
        count++;
      }
    }

    // Set attributes of free players
    for (int i = teams.length * 27; i < players.length; i++) {
      int overall = r.nextInt(80);

      players[i] = Player.createRandomPlayer(r, today, i, this.getLastNames(), this.getFirstNames(), 18, 40, overall, overall + 1, countries);
      players[i].setOnTransferList(true);
    }

    Team[] lna = new Team[12];
    for (int i = 0; i < 12; i++) {
      lna[i] = teams[i];
    }

    Team[] lnb = new Team[12];
    for (int i = 12; i < 24; i++) {
      lnb[i - 12] = teams[i];
    }

    Team[] lig11 = new Team[12];
    for (int i = 24; i < 36; i++) {
      lig11[i - 24] = teams[i];
    }

    Team[] lig12 = new Team[12];
    for (int i = 36; i < 48; i++) {
      lig12[i - 36] = teams[i];
    }

    // Generate league for one LeagueOwner
    League[] leagues = new League[4];
    leagues[0] = new StdLeague("LNA", 0, lna, 2, 3, 7, 2, 7);
    leagues[1] = new StdLeague("LNB", 1, lnb, 2, 3, 7, 0, 0);
    leagues[2] = new StdLeague("1. Liga Ost", 2, lig11, 2, 3, 7, 0, 0);
    leagues[3] = new StdLeague("1. Liga West", 2, lig12, 2, 3, 7, 0, 0);

    // Link teams to leagueOwner
    leagueOwners[0] = new StdLeagueOwner("Schweiz", leagues, true, 7);

    // User 1
    users = new User[1];
    User usr = new User("Alan Haworth");
    usr.setTeam(teams[0]);
    users[0] = usr;

    /**
     * User usr2 = new User("Globi (the) Blue"); usr2.setTeam(teams[15]);
     * users[1] = usr2;
     */
  }



  /**
   * Returns last names
   * 
   * @return lastNames
   */
  private String[] getLastNames() {
    return lastNames.toArray(new String[lastNames.size()]);
  }

  /**
   * Returns first names
   * 
   * @return firstNames
   */
  private String[] getFirstNames() {
    return firstNames.toArray(new String[firstNames.size()]);
  }

  /**
   * Fill up a simple country list
   */
  private void fillCountries() {
    Vector<Country> countriesVec = new Vector<Country>();
    countriesVec.add(new Country("1", "country.switzerland", "Switzerland"));
    countriesVec.add(new Country("2", "country.austria", "Austria"));
    countriesVec.add(new Country("3", "country.germany", "Germany"));
    countriesVec.add(new Country("4", "country.italy", "Italy"));
    countriesVec.add(new Country("5", "country.france", "France"));
    countriesVec.add(new Country("6", "country.finnland", "Finnland"));
    countriesVec.add(new Country("7", "country.sweden", "Sweden"));
    countriesVec.add(new Country("8", "country.norway", "Norway"));
    countriesVec.add(new Country("9", "country.russja", "Russja"));
    countriesVec.add(new Country("10", "country.canada", "Canada"));
    countriesVec.add(new Country("11", "country.usa", "USA"));
    this.countries = countriesVec.toArray(new Country[countriesVec.size()]);
  }

  /** Fill up last names */
  private void fillLastNames() {
    lastNames = new Vector<String>();

    lastNames.add("Zubler");
    lastNames.add("Aebersold");
    lastNames.add("Aerni");
    lastNames.add("Steinhauer");
    lastNames.add("Aeschbacher");
    lastNames.add("Aeschlimann");
    lastNames.add("Allemand");
    lastNames.add("Allenbach");
    lastNames.add("Al Mansouri");
    lastNames.add("Amstutz");
    lastNames.add("Angerer");
    lastNames.add("Apollonio");
    lastNames.add("Aragon");
    lastNames.add("Ardovino");
    lastNames.add("Aregger");
    lastNames.add("Atteslander");
    lastNames.add("Augsburger");
    lastNames.add("Baehler");
    lastNames.add("Baglivo");
    lastNames.add("Balaster");
    lastNames.add("Balmer");
    lastNames.add("Balota");
    lastNames.add("Balsiger");
    lastNames.add("Balsiger");
    lastNames.add("Bangerter");
    lastNames.add("Barbier");
    lastNames.add("Barone");
    lastNames.add("Barraud");
    lastNames.add("Baertschi");
    lastNames.add("Basler");
    lastNames.add("Baumgartner");
    lastNames.add("Baur");
    lastNames.add("Bayer");
    lastNames.add("Beeler");
    lastNames.add("Beetschen");
    lastNames.add("Belfiore");
    lastNames.add("Bella");
    lastNames.add("Benmohamed");
    lastNames.add("Berger");
    lastNames.add("Berisha");
    lastNames.add("Bertossa");
    lastNames.add("Bertschy");
    lastNames.add("Beutler");
    lastNames.add("Bichsel");
    lastNames.add("Bichsel");
    lastNames.add("Biel");
    lastNames.add("Bieri");
    lastNames.add("Bieri");
    lastNames.add("Bieri");
    lastNames.add("Bigler");
    lastNames.add("Bigler");
    lastNames.add("Bischof");
    lastNames.add("Bischoff");
    lastNames.add("Bitschnau");
    lastNames.add("Bitzi");
    lastNames.add("Blank");
    lastNames.add("Blaser");
    lastNames.add("Blattner");
    lastNames.add("Bleuler");
    lastNames.add("Boday");
    lastNames.add("Boell");
    lastNames.add("Bonaria");
    lastNames.add("Borle");
    lastNames.add("Borle");
    lastNames.add("Boesiger");
    lastNames.add("Bosshard");
    lastNames.add("Brambilla");
    lastNames.add("Bratschi");
    lastNames.add("Breitenstein");
    lastNames.add("Brenni");
    lastNames.add("Broennimann");
    lastNames.add("Bruegger");
    lastNames.add("Brunner");
    lastNames.add("Bucher");
    lastNames.add("Buechler");
    lastNames.add("Buchser");
    lastNames.add("Buehler");
    lastNames.add("Buehlmann");
    lastNames.add("Buri");
    lastNames.add("Burkhalter");
    lastNames.add("Burkhardt");
    lastNames.add("Burkhart");
    lastNames.add("Buerki");
    lastNames.add("Buetikofer");
    lastNames.add("Calle");
    lastNames.add("Cancellara");
    lastNames.add("Cancelliere");
    lastNames.add("Canclini");
    lastNames.add("Cardinale");
    lastNames.add("Castellan");
    lastNames.add("Cavallotti");
    lastNames.add("Cavegn");
    lastNames.add("Cescutti");
    lastNames.add("Chevalley");
    lastNames.add("Chisholm");
    lastNames.add("Christen");
    lastNames.add("Cinter Fischlin");
    lastNames.add("Clamann");
    lastNames.add("Coban");
    lastNames.add("Coello");
    lastNames.add("Cordic");
    lastNames.add("Curti");
    lastNames.add("Dahinden");
    lastNames.add("Daetwiler");
    lastNames.add("Debrit");
    lastNames.add("De Giorgi");
    lastNames.add("Demirag");
    lastNames.add("Dernesch");
    lastNames.add("Deuber");
    lastNames.add("Dobiaschofsky");
    lastNames.add("Dos Santos");
    lastNames.add("Dueby");
    lastNames.add("Du Pasquier");
    lastNames.add("Duppenthaler");
    lastNames.add("Eberhard");
    lastNames.add("Eggen");
    lastNames.add("Eggimann");
    lastNames.add("Egli");
    lastNames.add("Egli");
    lastNames.add("Ehrenreich");
    lastNames.add("Eichholzer");
    lastNames.add("Emmisberger");
    lastNames.add("Ertavi");
    lastNames.add("Etter");
    lastNames.add("Facchi-Negri");
    lastNames.add("Faeh");
    lastNames.add("Faller");
    lastNames.add("Fankhauser");
    lastNames.add("Farese");
    lastNames.add("Favre");
    lastNames.add("Fehlmann");
    lastNames.add("Ficker");
    lastNames.add("Fischbach");
    lastNames.add("Fischer");
    lastNames.add("Fivaz");
    lastNames.add("Flury");
    lastNames.add("Forsch");
    lastNames.add("Forsch");
    lastNames.add("Frei");
    lastNames.add("Frei");
    lastNames.add("Freiburghaus");
    lastNames.add("Freiburghaus");
    lastNames.add("Freitag");
    lastNames.add("Frey");
    lastNames.add("Frutig");
    lastNames.add("Frutiger");
    lastNames.add("Furrer");
    lastNames.add("Gagnebin");
    lastNames.add("Gagnebin");
    lastNames.add("Gattner");
    lastNames.add("Geiger");
    lastNames.add("Gerber");
    lastNames.add("Gerber");
    lastNames.add("Gerber");
    lastNames.add("Gerber");
    lastNames.add("Gerber");
    lastNames.add("Gerteis");
    lastNames.add("Gfeller");
    lastNames.add("Gfeller");
    lastNames.add("Gharbi el");
    lastNames.add("Giurca");
    lastNames.add("Glanzmann");
    lastNames.add("Glauser");
    lastNames.add("Gnaegi");
    lastNames.add("Gorber");
    lastNames.add("Greco");
    lastNames.add("Greutert");
    lastNames.add("Grossen");
    lastNames.add("Gruetter");
    lastNames.add("Gruetter");
    lastNames.add("Guedel");
    lastNames.add("Gusset");
    lastNames.add("Habegger");
    lastNames.add("Haeberli");
    lastNames.add("Hadorn");
    lastNames.add("Hadorn");
    lastNames.add("Hadorn");
    lastNames.add("Haefliger");
    lastNames.add("Haenni");
    lastNames.add("Hafner");
    lastNames.add("Hamm");
    lastNames.add("Haesler");
    lastNames.add("Hason");
    lastNames.add("Hausammann");
    lastNames.add("Heim");
    lastNames.add("Heimo");
    lastNames.add("Heise");
    lastNames.add("Heldner");
    lastNames.add("Helfer");
    lastNames.add("Hemmeler");
    lastNames.add("Hessing");
    lastNames.add("Hindermann");
    lastNames.add("Hinni");
    lastNames.add("Hirsbrunner");
    lastNames.add("Hoegger");
    lastNames.add("Hoerler");
    lastNames.add("Horst");
    lastNames.add("Hostettler");
    lastNames.add("Hostettler");
    lastNames.add("Hostettler");
    lastNames.add("Hotz");
    lastNames.add("Hug");
    lastNames.add("Hunziker");
    lastNames.add("Huerlimann");
    lastNames.add("Hurni");
    lastNames.add("Huerzeler");
    lastNames.add("Imobersteg");
    lastNames.add("Jauch");
    lastNames.add("Jaussi");
    lastNames.add("Jeanneret");
    lastNames.add("Jegerlehner");
    lastNames.add("Jenzer");
    lastNames.add("Jeschko");
    lastNames.add("Jorns");
    lastNames.add("Josi");
    lastNames.add("Jost");
    lastNames.add("Jutzi");
    lastNames.add("Kaderli");
    lastNames.add("Kaeser");
    lastNames.add("Kalt");
    lastNames.add("Kaempf");
    lastNames.add("Kaspar");
    lastNames.add("Kauer");
    lastNames.add("Keller");
    lastNames.add("Keller");
    lastNames.add("Keller");
    lastNames.add("Keller");
    lastNames.add("Kernen");
    lastNames.add("Kiener");
    lastNames.add("Kieschke");
    lastNames.add("Klaus");
    lastNames.add("Kleeb");
    lastNames.add("Kleiner");
    lastNames.add("Klinger");
    lastNames.add("Knoecklein");
    lastNames.add("Knoll");
    lastNames.add("Kobel");
    lastNames.add("Kobel");
    lastNames.add("Koller");
    lastNames.add("Koenig");
    lastNames.add("Koenig");
    lastNames.add("Koenig");
    lastNames.add("Konrad");
    lastNames.add("Kopf");
    lastNames.add("Kopp");
    lastNames.add("Schlapfer");
    lastNames.add("Kraehenbuehl");
    lastNames.add("Krebs");
    lastNames.add("Krebs");
    lastNames.add("Krezdorn");
    lastNames.add("Kubisch");
    lastNames.add("Kummer");
    lastNames.add("Kundert");
    lastNames.add("Kuendig");
    lastNames.add("Kueng");
    lastNames.add("Kueng");
    lastNames.add("Kunz");
    lastNames.add("Kunz");
    lastNames.add("Kunz");
    lastNames.add("Kuenzi");
    lastNames.add("Kuonen");
    lastNames.add("Kyburz");
    lastNames.add("Kyburz");
    lastNames.add("Laederach");
    lastNames.add("Lakache");
    lastNames.add("Landolf");
    lastNames.add("Lanz");
    lastNames.add("Lehmann");
    lastNames.add("Lelli");
    lastNames.add("Leuenberg");
    lastNames.add("Leuenberger");
    lastNames.add("Leuenberger");
    lastNames.add("Leuenberger");
    lastNames.add("Leuenberger");
    lastNames.add("Leutwyler");
    lastNames.add("Leuzinger Heise");
    lastNames.add("Lewis");
    lastNames.add("Liechti");
    lastNames.add("Liechti");
    lastNames.add("Liechti");
    lastNames.add("Liniger");
    lastNames.add("Ljutic");
    lastNames.add("Locher");
    lastNames.add("Lohri");
    lastNames.add("Loreggia");
    lastNames.add("Loria");
    lastNames.add("Luder");
    lastNames.add("Luft");
    lastNames.add("Luginbuehl");
    lastNames.add("Luethi");
    lastNames.add("Luethi");
    lastNames.add("Maeder");
    lastNames.add("Maeder");
    lastNames.add("Manicavasakar");
    lastNames.add("Marbacher");
    lastNames.add("Marbec");
    lastNames.add("Marchand");
    lastNames.add("Mark");
    lastNames.add("Maerki");
    lastNames.add("Marti");
    lastNames.add("Maulaz");
    lastNames.add("Maurer");
    lastNames.add("Maeusli");
    lastNames.add("Meer");
    lastNames.add("Meier");
    lastNames.add("Meister");
    lastNames.add("Menet");
    lastNames.add("Merki");
    lastNames.add("Merz");
    lastNames.add("Merz");
    lastNames.add("Mignot");
    lastNames.add("Minder");
    lastNames.add("Minotto");
    lastNames.add("Mlinaric");
    lastNames.add("Moghaddami");
    lastNames.add("Monopoli");
    lastNames.add("Monopoli");
    lastNames.add("Morciano");
    lastNames.add("Morel");
    lastNames.add("Mory");
    lastNames.add("Moser");
    lastNames.add("Moser");
    lastNames.add("Moser");
    lastNames.add("Moser");
    lastNames.add("Moser");
    lastNames.add("Mosimann");
    lastNames.add("Mosimann");
    lastNames.add("Moudry");
    lastNames.add("Mueller");
    lastNames.add("Mueller");
    lastNames.add("Mueller");
    lastNames.add("Mueller");
    lastNames.add("Mueller");
    lastNames.add("Mueller");
    lastNames.add("Mueller");
    lastNames.add("Muralt");
    lastNames.add("Nabholz");
    lastNames.add("Nanzer");
    lastNames.add("Nebert");
    lastNames.add("Neuenschwander");
    lastNames.add("Neuhaus");
    lastNames.add("von Niederhaeusern");
    lastNames.add("von Niederhaeusern");
    lastNames.add("Zubler");
    lastNames.add("Nunez");
    lastNames.add("Nuenlist");
    lastNames.add("Nussbaumer");
    lastNames.add("Nydegger");
    lastNames.add("Nyffeler");
    lastNames.add("Nyffenegger");
    lastNames.add("Obando");
    lastNames.add("Oberli");
    lastNames.add("Ochsenbein");
    lastNames.add("Ochsenbein");
    lastNames.add("Oertle");
    lastNames.add("Oezdemir");
    lastNames.add("Ofosu");
    lastNames.add("Ordinelli");
    lastNames.add("Orendi");
    lastNames.add("Ozimek");
    lastNames.add("Paduczky");
    lastNames.add("Pavlin");
    lastNames.add("Pereira");
    lastNames.add("Perez Khalil");
    lastNames.add("Perren");
    lastNames.add("Peterhans");
    lastNames.add("Petersen");
    lastNames.add("Pfulg");
    lastNames.add("Phurtag");
    lastNames.add("Pizarro");
    lastNames.add("Poschung");
    lastNames.add("Ramseier");
    lastNames.add("Reber");
    lastNames.add("Reber");
    lastNames.add("Redzepi");
    lastNames.add("Reichen");
    lastNames.add("Schenk");
    lastNames.add("Renggli");
    lastNames.add("Riaz");
    lastNames.add("Rieser");
    lastNames.add("Rigault");
    lastNames.add("Riley");
    lastNames.add("Rivoir");
    lastNames.add("Robbi");
    lastNames.add("Robert");
    lastNames.add("Roggli");
    lastNames.add("Rolli");
    lastNames.add("Rolli");
    lastNames.add("Romagosa");
    lastNames.add("Romano");
    lastNames.add("Rostetter");
    lastNames.add("Rueegsegger");
    lastNames.add("Ruf");
    lastNames.add("Ruf");
    lastNames.add("Rufener");
    lastNames.add("Rufer");
    lastNames.add("Rutishauser");
    lastNames.add("Ryf");
    lastNames.add("Ryser");
    lastNames.add("Saegesser");
    lastNames.add("Salamone");
    lastNames.add("Salis Gross");
    lastNames.add("Salz");
    lastNames.add("Salzmann");
    lastNames.add("Salzmann");
    lastNames.add("Salzmann");
    lastNames.add("Sandmeier");
    lastNames.add("Santamaria");
    lastNames.add("Santoli");
    lastNames.add("Santosuosso");
    lastNames.add("Schaeren");
    lastNames.add("Schaerer");
    lastNames.add("Schauwecker");
    lastNames.add("Scheidegger");
    lastNames.add("Schenk");
    lastNames.add("Scherrer");
    lastNames.add("Schlaefli");
    lastNames.add("Schlapbach");
    lastNames.add("Schlapfer");
    lastNames.add("Schlegel");
    lastNames.add("Schlup");
    lastNames.add("Schmid");
    lastNames.add("Schmid");
    lastNames.add("Schmidt");
    lastNames.add("Schmied");
    lastNames.add("Schmutz");
    lastNames.add("Schnell");
    lastNames.add("Schoeberlein");
    lastNames.add("Schroeder");
    lastNames.add("Schubkegel");
    lastNames.add("Schudel");
    lastNames.add("Schuetz");
    lastNames.add("Schuetz");
    lastNames.add("Schwab");
    lastNames.add("Schwab");
    lastNames.add("Schwarz");
    lastNames.add("Schwarz");
    lastNames.add("Schwarzmann");
    lastNames.add("Schwegler");
    lastNames.add("Schweizer");
    lastNames.add("Schweizer");
    lastNames.add("Seng");
    lastNames.add("Sevinc");
    lastNames.add("Shala");
    lastNames.add("Sidler");
    lastNames.add("Siebenmann");
    lastNames.add("Sieber");
    lastNames.add("Siegenthaler");
    lastNames.add("Siegrist");
    lastNames.add("Simoes");
    lastNames.add("Spengler");
    lastNames.add("Spiardi");
    lastNames.add("Spiroski");
    lastNames.add("Spoerri");
    lastNames.add("Spoerri");
    lastNames.add("Spreng");
    lastNames.add("Squaratti");
    lastNames.add("Stamatiadis");
    lastNames.add("Stamm");
    lastNames.add("Stauber");
    lastNames.add("Steck");
    lastNames.add("Steinbach");
    lastNames.add("Steiner");
    lastNames.add("Steiner");
    lastNames.add("Steiner");
    lastNames.add("Steiner");
    lastNames.add("Stettler");
    lastNames.add("Stettler");
    lastNames.add("Stierli");
    lastNames.add("Stifani");
    lastNames.add("Stoeckli");
    lastNames.add("Storz");
    lastNames.add("Streit");
    lastNames.add("Stuber");
    lastNames.add("Stuebi");
    lastNames.add("Stucki");
    lastNames.add("Studer");
    lastNames.add("Stutz");
    lastNames.add("Suter");
    lastNames.add("Suter");
    lastNames.add("Sutter");
    lastNames.add("Szakacs");
    lastNames.add("Tamarazio");
    lastNames.add("Tanaka");
    lastNames.add("Thudianplackal");
    lastNames.add("Toha");
    lastNames.add("Traber");
    lastNames.add("Tromp");
    lastNames.add("Tschanz");
    lastNames.add("Tschanz");
    lastNames.add("Ucha");
    lastNames.add("Udry");
    lastNames.add("Urban");
    lastNames.add("Ursprung");
    lastNames.add("Van");
    lastNames.add("Verdun");
    lastNames.add("Vernier");
    lastNames.add("Vernier");
    lastNames.add("Vetter");
    lastNames.add("Vetter");
    lastNames.add("Vetter");
    lastNames.add("Vieites Alvite");
    lastNames.add("Villanueva");
    lastNames.add("Voegele");
    lastNames.add("Voegeli");
    lastNames.add("Voney");
    lastNames.add("Vu");
    lastNames.add("Vuille");
    lastNames.add("Waeber");
    lastNames.add("Walther");
    lastNames.add("Waelti");
    lastNames.add("Waelti");
    lastNames.add("Wartmann");
    lastNames.add("Wasserfallen");
    lastNames.add("Weber");
    lastNames.add("Weder");
    lastNames.add("Weibel");
    lastNames.add("Weibel");
    lastNames.add("Welti");
    lastNames.add("Wendt");
    lastNames.add("Wenger");
    lastNames.add("Wernli");
    lastNames.add("Wichtermann");
    lastNames.add("Wildberger");
    lastNames.add("Willi");
    lastNames.add("Windisch");
    lastNames.add("Windlinger");
    lastNames.add("Winkler");
    lastNames.add("Witschi");
    lastNames.add("Wuethrich");
    lastNames.add("Wyder");
    lastNames.add("Wyler");
    lastNames.add("Wyler");
    lastNames.add("Wyrsch");
    lastNames.add("Wyss");
    lastNames.add("Wyss");
    lastNames.add("Yavuz");
    lastNames.add("Yaygir");
    lastNames.add("Zambetti");
    lastNames.add("Zbinden");
    lastNames.add("Zbinden");
    lastNames.add("Zeindler");
    lastNames.add("Zeiter");
    lastNames.add("Zeller");
    lastNames.add("Zimmermann");
    lastNames.add("Zingaro");
    lastNames.add("Zubler");
    lastNames.add("Zupanek");
    lastNames.add("Zurbuchen");
    lastNames.add("Zuercher");
    lastNames.add("Zwahlen");
    lastNames.add("Allemann");
    lastNames.add("Althaus");
    lastNames.add("Althaus");
    lastNames.add("Ammann");
    lastNames.add("Auchli");
    lastNames.add("Badini");
    lastNames.add("Baumgartner");
    lastNames.add("Bevington");
    lastNames.add("Beyeler");
    lastNames.add("Beyeler");
    lastNames.add("Bischoff");
    lastNames.add("Brand");
    lastNames.add("Burri");
    lastNames.add("Daeppen");
    lastNames.add("De Nadai");
    lastNames.add("Duong");
    lastNames.add("Eggimann");
    lastNames.add("Egli");
    lastNames.add("Facchinetti");
    lastNames.add("Friederich");
    lastNames.add("Friedli");
    lastNames.add("Fuellemann");
    lastNames.add("Galante");
    lastNames.add("Geiser");
    lastNames.add("Guenther");
    lastNames.add("Gurtner");
    lastNames.add("Haldemann");
    lastNames.add("Haesler");
    lastNames.add("Heinze");
    lastNames.add("Hiltbrunner");
    lastNames.add("Hirsiger");
    lastNames.add("Hofmann");
    lastNames.add("Hostettler");
    lastNames.add("Jaun");
    lastNames.add("Jobin");
    lastNames.add("Jung");
    lastNames.add("Lopez");
    lastNames.add("Magnaguagno Badini");
    lastNames.add("Melone");
    lastNames.add("Nussbaumer");
    lastNames.add("Oberli");
    lastNames.add("Oester");
    lastNames.add("Perez Otero");
    lastNames.add("Pluess");
    lastNames.add("Ritz");
    lastNames.add("Rufener");
    lastNames.add("Saladin");
    lastNames.add("Schaad");
    lastNames.add("Schaer");
    lastNames.add("Schenk");
    lastNames.add("Schmidhauser");
    lastNames.add("Schmidlin");
    lastNames.add("Brand");
    lastNames.add("Schuerch");
    lastNames.add("Schweizer");
    lastNames.add("Sigg");
    lastNames.add("Stocker");
    lastNames.add("Stoffel");
    lastNames.add("Strahm");
    lastNames.add("Svitek");
    lastNames.add("Trachsel");
    lastNames.add("Tschannen");
    lastNames.add("Vigneswaran");
    lastNames.add("Zoss");
    lastNames.add("Jecklin");
    lastNames.add("Marthaler");
    lastNames.add("Schori");

  }

  /** Fill up first names */
  private void fillFirstNames() {
    firstNames = new Vector<String>();

    firstNames.add("Adrian");
    firstNames.add("Ahmad");
    firstNames.add("Albert");
    firstNames.add("Albin");
    firstNames.add("Alexandra-E.");
    firstNames.add("Alfred");
    firstNames.add("Alfred");
    firstNames.add("Alfred");
    firstNames.add("Amedeo");
    firstNames.add("Andre");
    firstNames.add("Andre");
    firstNames.add("Andrea");
    firstNames.add("Andreas");
    firstNames.add("Andreas");
    firstNames.add("Andreas");
    firstNames.add("Andreas");
    firstNames.add("Andreas");
    firstNames.add("Andree");
    firstNames.add("Andree");
    firstNames.add("Andri");
    firstNames.add("Antonino");
    firstNames.add("Antonio");
    firstNames.add("Antonio");
    firstNames.add("Antonio");
    firstNames.add("Arjang");
    firstNames.add("Arthur");
    firstNames.add("Barbara");
    firstNames.add("Bastian");
    firstNames.add("Beat");
    firstNames.add("Belen");
    firstNames.add("Benjamin");
    firstNames.add("Bennina");
    firstNames.add("Bernhard");
    firstNames.add("Bertrand");
    firstNames.add("Bertrand H.");
    firstNames.add("Bertrand H.");
    firstNames.add("Bertrand H.");
    firstNames.add("Besim");
    firstNames.add("Biagio");
    firstNames.add("Bruno");
    firstNames.add("Bruno");
    firstNames.add("Carla");
    firstNames.add("Carlo");
    firstNames.add("Carlos");
    firstNames.add("Carmen");
    firstNames.add("Cecilia");
    firstNames.add("Cesare");
    firstNames.add("Charles");
    firstNames.add("Christian");
    firstNames.add("Christoph");
    firstNames.add("Christoph");
    firstNames.add("Christoph");
    firstNames.add("Clemens");
    firstNames.add("Cyrill");
    firstNames.add("Daniel");
    firstNames.add("Daniel");
    firstNames.add("Daniel");
    firstNames.add("Daniel");
    firstNames.add("Daniel");
    firstNames.add("Daniel");
    firstNames.add("Daniel");
    firstNames.add("Daniel");
    firstNames.add("Daniele");
    firstNames.add("Diamantino");
    firstNames.add("Dominik");
    firstNames.add("Dominik");
    firstNames.add("Durs");
    firstNames.add("Eduard");
    firstNames.add("Edwin");
    firstNames.add("Elmar");
    firstNames.add("Emil");
    firstNames.add("Enrico");
    firstNames.add("Erhard");
    firstNames.add("Erich");
    firstNames.add("Erich");
    firstNames.add("Erika");
    firstNames.add("Erkan");
    firstNames.add("Ernst");
    firstNames.add("Ernst");
    firstNames.add("Ernst");
    firstNames.add("Ernst");
    firstNames.add("Ernst");
    firstNames.add("Ernst");
    firstNames.add("Ernst");
    firstNames.add("Ernst");
    firstNames.add("Ernst");
    firstNames.add("Ernst");
    firstNames.add("Ernst");
    firstNames.add("Ernst");
    firstNames.add("Ernst");
    firstNames.add("Ernst W.");
    firstNames.add("Fanny");
    firstNames.add("Felix");
    firstNames.add("Felix");
    firstNames.add("Ferdinand");
    firstNames.add("Franz");
    firstNames.add("Franz");
    firstNames.add("Fred");
    firstNames.add("Fred");
    firstNames.add("Friedrich");
    firstNames.add("Fritz");
    firstNames.add("Fritz");
    firstNames.add("Geza");
    firstNames.add("Giancarlo");
    firstNames.add("Giorgio");
    firstNames.add("Giuseppe");
    firstNames.add("Hannes");
    firstNames.add("Hans");
    firstNames.add("Hans");
    firstNames.add("Hans");
    firstNames.add("Hans");
    firstNames.add("Hans");
    firstNames.add("Hans");
    firstNames.add("Hans");
    firstNames.add("Hans");
    firstNames.add("Hans");
    firstNames.add("Hans");
    firstNames.add("Hans");
    firstNames.add("Hans Peter");
    firstNames.add("Hans W.");
    firstNames.add("Hans-Chr.");
    firstNames.add("Hansjuerg");
    firstNames.add("Hansjuerg");
    firstNames.add("Hanspeter");
    firstNames.add("Hanspeter");
    firstNames.add("Hans-Peter");
    firstNames.add("Hans-Rudolf");
    firstNames.add("Hans-Rudolf");
    firstNames.add("Hans-Rudolf");
    firstNames.add("Hansueli");
    firstNames.add("Hans-Ulrich");
    firstNames.add("Hasim");
    firstNames.add("Hedwig");
    firstNames.add("Heinrich");
    firstNames.add("Heinrich");
    firstNames.add("Heinrich");
    firstNames.add("Heinz");
    firstNames.add("Heinz");
    firstNames.add("Heinz");
    firstNames.add("Heinz");
    firstNames.add("Helmuth");
    firstNames.add("Herbert");
    firstNames.add("Herbert");
    firstNames.add("Hermann");
    firstNames.add("Hermann");
    firstNames.add("Hermine");
    firstNames.add("Hilde");
    firstNames.add("Hilde");
    firstNames.add("Italo");
    firstNames.add("Jaime");
    firstNames.add("Janka");
    firstNames.add("Jean");
    firstNames.add("Jean-Claude");
    firstNames.add("Jean-Jacques");
    firstNames.add("Jean-Louis");
    firstNames.add("Jean-Pierre");
    firstNames.add("Jens");
    firstNames.add("Jiri");
    firstNames.add("Joanna");
    firstNames.add("Johann");
    firstNames.add("Johanna");
    firstNames.add("Joerg");
    firstNames.add("Jose");
    firstNames.add("Jose");
    firstNames.add("Jose");
    firstNames.add("Josef");
    firstNames.add("Josef");
    firstNames.add("Josefa");
    firstNames.add("Joseph");
    firstNames.add("Juerg");
    firstNames.add("Juerg");
    firstNames.add("Juerg");
    firstNames.add("Juerg");
    firstNames.add("Juerg");
    firstNames.add("Kanagasabai");
    firstNames.add("Karl");
    firstNames.add("Kim Bich");
    firstNames.add("Kim Lang");
    firstNames.add("Klara");
    firstNames.add("Klara");
    firstNames.add("Klaus");
    firstNames.add("Klaus");
    firstNames.add("Kurt");
    firstNames.add("Kurt");
    firstNames.add("Kurt");
    firstNames.add("Leandro");
    firstNames.add("Leon Andre");
    firstNames.add("Lilli");
    firstNames.add("Lilly");
    firstNames.add("Lorenz");
    firstNames.add("Lotty");
    firstNames.add("Louis");
    firstNames.add("Louise");
    firstNames.add("Lucia");
    firstNames.add("Luis");
    firstNames.add("Lukas");
    firstNames.add("Lukas");
    firstNames.add("Marc");
    firstNames.add("Marcel");
    firstNames.add("Marcel");
    firstNames.add("Marcel");
    firstNames.add("Marcel");
    firstNames.add("Marcelle");
    firstNames.add("Mario");
    firstNames.add("Mario");
    firstNames.add("Mark");
    firstNames.add("Markus");
    firstNames.add("Markus");
    firstNames.add("Markus");
    firstNames.add("Martin");
    firstNames.add("Martin");
    firstNames.add("Martin B.");
    firstNames.add("Matthias");
    firstNames.add("Max");
    firstNames.add("Max");
    firstNames.add("Max");
    firstNames.add("Metodi");
    firstNames.add("Mette");
    firstNames.add("Micha");
    firstNames.add("Michael");
    firstNames.add("Michael");
    firstNames.add("Michael");
    firstNames.add("Mostafa");
    firstNames.add("Neil");
    firstNames.add("Nelly");
    firstNames.add("Nino Paolo");
    firstNames.add("Nizamettin");
    firstNames.add("Olivier");
    firstNames.add("Orlanda");
    firstNames.add("Oscar");
    firstNames.add("Otto");
    firstNames.add("Otto");
    firstNames.add("Otto");
    firstNames.add("Pascale");
    firstNames.add("Pasquale");
    firstNames.add("Pasquale");
    firstNames.add("Paul");
    firstNames.add("Paul");
    firstNames.add("Paul");
    firstNames.add("Paul");
    firstNames.add("Paul");
    firstNames.add("Paul");
    firstNames.add("Paul Rudolf");
    firstNames.add("Per");
    firstNames.add("Peter");
    firstNames.add("Peter");
    firstNames.add("Peter");
    firstNames.add("Peter");
    firstNames.add("Peter");
    firstNames.add("Peter");
    firstNames.add("Peter");
    firstNames.add("Peter");
    firstNames.add("Peter");
    firstNames.add("Peter");
    firstNames.add("Peter");
    firstNames.add("Peter");
    firstNames.add("Peter");
    firstNames.add("Peter");
    firstNames.add("Peter");
    firstNames.add("Raphael");
    firstNames.add("Raymond");
    firstNames.add("Renato");
    firstNames.add("Reto");
    firstNames.add("Reto");
    firstNames.add("Reto");
    firstNames.add("Robert");
    firstNames.add("Robert");
    firstNames.add("Robert");
    firstNames.add("Robert");
    firstNames.add("Rochus");
    firstNames.add("Roger");
    firstNames.add("Roger");
    firstNames.add("Roland");
    firstNames.add("Roland");
    firstNames.add("Rolf");
    firstNames.add("Rolf");
    firstNames.add("Rolf");
    firstNames.add("Rolf-Dieter");
    firstNames.add("Roman");
    firstNames.add("Romualdo");
    firstNames.add("Rudolf");
    firstNames.add("Rudolf");
    firstNames.add("Rudolf");
    firstNames.add("Rudolf");
    firstNames.add("Rudolf");
    firstNames.add("Rudolf");
    firstNames.add("Rudolf");
    firstNames.add("Rudolf");
    firstNames.add("Rudolf");
    firstNames.add("Rustem");
    firstNames.add("Ruth");
    firstNames.add("Ruth");
    firstNames.add("Ruth");
    firstNames.add("Sandro");
    firstNames.add("Sandro");
    firstNames.add("Sandro");
    firstNames.add("Santina");
    firstNames.add("Saverio");
    firstNames.add("Serpil");
    firstNames.add("Sevalj");
    firstNames.add("Silvio");
    firstNames.add("Simon");
    firstNames.add("Stefan");
    firstNames.add("Stefan");
    firstNames.add("Stephan");
    firstNames.add("Stephan");
    firstNames.add("Stephane");
    firstNames.add("Sven");
    firstNames.add("Sylvia");
    firstNames.add("Tanja");
    firstNames.add("Theo");
    firstNames.add("Theo");
    firstNames.add("Theodor");
    firstNames.add("Theres");
    firstNames.add("Thi Thu Thao");
    firstNames.add("Thomas");
    firstNames.add("Tom");
    firstNames.add("Toni");
    firstNames.add("Tuende");
    firstNames.add("Ulrich");
    firstNames.add("Urs");
    firstNames.add("Urs");
    firstNames.add("Viktor");
    firstNames.add("Vincenzo");
    firstNames.add("Walo");
    firstNames.add("Walter");
    firstNames.add("Walter");
    firstNames.add("Walter");
    firstNames.add("Walter");
    firstNames.add("Walter");
    firstNames.add("Walter");
    firstNames.add("Walter");
    firstNames.add("Werner");
    firstNames.add("Werner");
    firstNames.add("Werner");
    firstNames.add("Werner");
    firstNames.add("Werner");
    firstNames.add("Werner");
    firstNames.add("Willi");
    firstNames.add("William");
    firstNames.add("Willy");
    firstNames.add("Willy");
    firstNames.add("Willy");
    firstNames.add("Willy");
    firstNames.add("Xaje");
    firstNames.add("Yalcin");
    firstNames.add("Yeboaa");
    firstNames.add("Yoshikuni");
    firstNames.add("Zeljko");
  }

}