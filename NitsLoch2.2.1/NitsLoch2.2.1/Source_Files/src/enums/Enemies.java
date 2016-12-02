/*	
	This file is part of NitsLoch.

	Copyright (C) 2007 Darren Watts

    NitsLoch is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    NitsLoch is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with NitsLoch.  If not, see <http://www.gnu.org/licenses/>.
 */

package src.enums;

import src.scenario.Images;

/**
 * Enums for monsters.  Number of monsters is hardcoded.  The rest of the stats
 * can be read in from a file.
 * @author Darren Watts
 * date 11/11/07
 *
 */
public enum Enemies {
	// Type numbers must go from 0 to n-1
	E_000(0),	E_001(1),	E_002(2),	E_003(3),
	E_004(4),	E_005(5),	E_006(6),	E_007(7),
	E_008(8),	E_009(9),	E_010(10),	E_011(11),
	E_012(12),	E_013(13),	E_014(14),	E_015(15),
	E_016(16),	E_017(17),	E_018(18),	E_019(19),
	E_020(20),	E_021(21),	E_022(22),	E_023(23),
	E_024(24),	E_025(25),	E_026(26),	E_027(27),
	E_028(28),	E_029(29),	E_030(30),	E_031(31),
	E_032(32),	E_033(33),	E_034(34),	E_035(35),
	E_036(36),	E_037(37),	E_038(38),	E_039(39),
	E_040(40),	E_041(41),	E_042(42),	E_043(43),
	E_044(44),	E_045(45),	E_046(46),	E_047(47),
	E_048(48),	E_049(49),	E_050(50),	E_051(51),
	E_052(52),	E_053(53),	E_054(54),	E_055(55),
	E_056(56),	E_057(57),	E_058(58),	E_059(59),
	E_060(60),	E_061(61),	E_062(62),	E_063(63),
	E_064(64),	E_065(65),	E_066(66),	E_067(67),
	E_068(68),	E_069(69),	E_070(70),	E_071(71),
	E_072(72),	E_073(73),	E_074(74),	E_075(75),
	E_076(76),	E_077(77),	E_078(78),	E_079(79),
	E_080(80),	E_081(81),	E_082(82),	E_083(83),	
	E_084(84),	E_085(85),	E_086(86),	E_087(87),
	E_088(88),	E_089(89),	E_090(90),	E_091(91),
	E_092(92),	E_093(93),	E_094(94),	E_095(95),	
	E_096(96),	E_097(97),	E_098(98),	E_099(99),
	E_100(100),	E_101(101),	E_102(102),	E_103(103),
	E_104(104),	E_105(105),	E_106(106),	E_107(107),	
	E_108(108),	E_109(109),	E_110(110),	E_111(111),
	E_112(112),	E_113(113),	E_114(114),	E_115(115),
	E_116(116),	E_117(117),	E_118(118),	E_119(119),	
	E_120(120),	E_121(121),	E_122(122),	E_123(123),
	E_124(124),	E_125(125),	E_126(126),	E_127(127),
	E_128(128),	E_129(129),	E_130(130),	E_131(131),	
	E_132(132),	E_133(133),	E_134(134),	E_135(135),
	E_136(136),	E_137(137),	E_138(138),	E_139(139),
	E_140(140),	E_141(141),	E_142(142),	E_143(143),	
	E_144(144),	E_145(145),	E_146(146),	E_147(147),
	E_148(148),	E_149(149),	E_150(150),	E_151(151),
	E_152(152),	E_153(153),	E_154(154),	E_155(155),	
	E_156(156),	E_157(157),	E_158(158),	E_159(159),
	E_160(160),	E_161(161),	E_162(162),	E_163(163),	
	E_164(164),	E_165(165),	E_166(166),	E_167(167),
	E_168(168),	E_169(169),	E_170(170),	E_171(171),
	E_172(172),	E_173(173),	E_174(174),	E_175(175),	
	E_176(176),	E_177(177),	E_178(178),	E_179(179),
	E_180(180),	E_181(181),	E_182(182),	E_183(183),
	E_184(184),	E_185(185),	E_186(186),	E_187(187),	
	E_188(188),	E_189(189),	E_190(190),	E_191(191),
	E_192(192),	E_193(193),	E_194(194),	E_195(195),
	E_196(196),	E_197(197),	E_198(198),	E_199(199);
	
	private int type;
	private boolean isLeader;
	private boolean isShopkeeper;
	private boolean isThief;
	private boolean isProperNoun;
	private EnemyBehavior behavior;
	private int maxHitPoints;
	private int ability;
	private Weapon weapon;
	private Armor armor;
	private String name;
	private String leftImage;
	private String rightImage;
	private String dungeonImage;
	private int minMoney;
	private int maxMoney;
	private boolean used;
	
	private Enemies(int type){
		this.type = type;
		leftImage = rightImage = dungeonImage = "Monster image not assigned";
		isShopkeeper = false;
		isThief = false;
	}
	
	/**
	 * Image location on disk when enemy is facing left.
	 * @return String : path to image on disk.
	 */
	public String getLeftImage(){
		return leftImage;
	}
	
	/**
	 * Image location on disk when enemy is facing right.
	 * @return String : path to image on disk.
	 */
	public String getRightImage(){
		return rightImage;
	}
	
	/**
	 * Image location on disk when enemy is in a dungeon.
	 * @return String : path to image on disk.
	 */
	public String getDungeonImage(){
		return dungeonImage;
	}
	
	/**
	 * Type number to use for the map format.
	 * @return int : type
	 */
	public int getType(){
		return type;
	}
	
	/**
	 * Sets the stats of this enemy.  Used when reading info from a file.
	 * @param name Name of the enemy
	 * @param maxHitPoints Maximum hit points.
	 * @param ability Ability number
	 * @param weaponType Type of weapon
	 * @param armorType Type of armor
	 * @param behaviorType Type of behavior
	 * @param minMoney minimum amount of money this enemy can have
	 * @param maxMoney maximum amount of money this enemy can have
	 * @param isLeader boolean : leader
	 * @param isShopkeeper boolean : shopkeeper
	 * @param leftPath String : image location to left facing enemy
	 * @param rightPath String : image location to right facing enemy
	 * @param dunPath String : image location to dungeon enemy
	 */
	public void setStats(
			String name, 
			int maxHitPoints, 
			int ability, 
			int weaponType, 
			int armorType,
			int behaviorType,
			int minMoney,
			int maxMoney,
			boolean isLeader,
			boolean isShopkeeper,
			boolean isThief,
			boolean isProperNoun,
			String leftPath,
			String rightPath,
			String dunPath){
		this.name = name;
		this.maxHitPoints = maxHitPoints;
		this.ability = ability;
		this.isLeader = isLeader;
		this.isShopkeeper = isShopkeeper;
		this.isThief = isThief;
		this.isProperNoun = isProperNoun;
		this.minMoney = minMoney;
		this.maxMoney = maxMoney;
		
		weapon = Weapon.values()[weaponType];
		armor = Armor.values()[armorType];
		behavior = EnemyBehavior.values()[behaviorType];
		
		leftImage = leftPath;
		rightImage = rightPath;
		dungeonImage = dunPath;
		
		Images images = Images.getInstance();
		
		images.add(leftImage);
		images.add(rightImage);
		images.add(dungeonImage);
		
		used = true;
	}
	
	/**
	 * Gets whether or not this enemy type is a leader.  All leaders in the world
	 * must be killed before the game ends.
	 * @return boolean : leader
	 */
	public boolean getIsLeader(){
		return isLeader;
	}
	
	/**
	 * Accessor for whether or not this enemy can run a shop.  Any enemies
	 * marked as a shopkeeper has a chance to spawn when a new shopkeeper
	 * appears at a shop.
	 * @return boolean : is shopkeeper
	 */
	public boolean getIsShopkeeper(){
		return isShopkeeper;
	}
	
	/**
	 * Accessor for whether or not this enemy can steal items from
	 * players.
	 * @return boolean : can steal
	 */
	public boolean getIsThief(){
		return isThief;
	}
	
	/**
	 * Accessor for whether or not this enemy's name is a proper noun.
	 * players.
	 * @return boolean : has proper noun for name
	 */
	public boolean getIsProperNoun(){
		return isProperNoun;
	}

	/**
	 * Accessor for behavior of this enemy.
	 * @return EnemyBehavior
	 */
	public EnemyBehavior getBehavior() {
		return behavior;
	}

	/**
	 * Accessor for max hit points of this enemy.
	 * @return int
	 */
	public int getMaxHitPoints() {
		return maxHitPoints;
	}

	/**
	 * Accessor for weapon of this enemy.
	 * @return Weapon
	 */
	public Weapon getWeapon() {
		return weapon;
	}

	/**
	 * Accessor for the name of this enemy.
	 * @return String : name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Accessor for ability number of this enemy.
	 * @return int
	 */
	public int getAbility() {
		return ability;
	}

	/**
	 * Accessor for the armor of this enemy.
	 * @return Armor
	 */
	public Armor getArmor() {
		return armor;
	}
	
	/**
	 * Gets a random amount of money for this type of enemy based on
	 * the minimum and maximum amount of money this type of enemy
	 * can carry.
	 * @return int : random amount of money this enemy can have
	 */
	public int getRandomMoney(){
		double rand = Math.random() * ((maxMoney - minMoney) + 1);
		int randInt = (int)rand;
		return minMoney + randInt;
	}
	
	/**
	 * Gets whether or not this type is used in the scenario.
	 * @return boolean : used
	 */
	public boolean getUsed(){
		return used;
	}
	
	public static void clearAll() {
		for(Enemies e : Enemies.values()) {
			e.setName("");
			e.used = false;
		}
	}
	
	/* ******************************************************
	 * The following methods are used only for the scenario editor
	 * ******************************************************/
	
	public void setName(String str) {
		name = str;
	}
	
	public void setLeftImage(String str) {
		leftImage = str;
	}
	
	public void setRightImage(String str) {
		rightImage = str;
	}
	
	public void setDungeonImage(String str) {
		dungeonImage = str;
	}
	
	public void setHitPoints(int num) {
		maxHitPoints = num;
	}
	
	public void setAbility(int num) {
		ability = num;
	}
	
	public void setMinMoney(int num) {
		minMoney = num;
	}
	
	public void setMaxMoney(int num) {
		maxMoney = num;
	}
	
	public void setWeaponType(Weapon wep) {
		weapon = wep;
	}
	
	public void setArmorType(Armor armor) {
		this.armor = armor;
	}
	
	public void setEnemyBehavior(EnemyBehavior behavior) {
		this.behavior = behavior;
	}
	
	public void setLeader(boolean bool) {
		isLeader = bool;
	}
	
	public void setShopkeeper(boolean bool) {
		isShopkeeper = bool;
	}
	
	public void setThief(boolean bool) {
		isThief = bool;
	}
	
	public void setProperNoun(boolean bool) {
		isProperNoun = bool;
	}
	
	public int getMinMoney() {
		return minMoney;
	}
	
	public int getMaxMoney() {
		return maxMoney;
	}

	public void remove() {
		name = "";
		used = false;
	}
	
}
