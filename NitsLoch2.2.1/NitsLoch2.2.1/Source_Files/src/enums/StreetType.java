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

import java.util.ArrayList;

import src.scenario.Images;

/**
 * This enum keeps track of different slot types for the kinds of enemy
 * triggers that can exist in the world.  Each kind of enemy trigger
 * can have a different set of enemies associated with it.
 * @author Darren Watts
 * date 11/23/07
 */
public enum StreetType {
	// Type numbers must go from 0 to n-1
	ST_000(0),
	ST_001(1),
	ST_002(2),
	ST_003(3),
	ST_004(4),
	ST_005(5),
	ST_006(6),
	ST_007(7),
	ST_008(8),
	ST_009(9),
	ST_010(10),
	ST_011(11),
	ST_012(12),
	ST_013(13),
	ST_014(14),
	ST_015(15),
	ST_016(16),
	ST_017(17),
	ST_018(18),
	ST_019(19),
	ST_020(20),
	ST_021(21),
	ST_022(22),
	ST_023(23),
	ST_024(24),
	ST_025(25),
	ST_026(26),
	ST_027(27),
	ST_028(28),
	ST_029(29),
	ST_030(30),
	ST_031(31),
	ST_032(32),
	ST_033(33),
	ST_034(34),
	ST_035(35),
	ST_036(36),
	ST_037(37),
	ST_038(38),
	ST_039(39),
	ST_040(40),
	ST_041(41),
	ST_042(42),
	ST_043(43),
	ST_044(44),
	ST_045(45),
	ST_046(46),
	ST_047(47),
	ST_048(48),
	ST_049(49),
	ST_050(50),
	ST_051(51),
	ST_052(52),
	ST_053(53),
	ST_054(54),
	ST_055(55),
	ST_056(56),
	ST_057(57),
	ST_058(58),
	ST_059(59),
	ST_060(60),
	ST_061(61),
	ST_062(62),
	ST_063(63),
	ST_064(64),
	ST_065(65),
	ST_066(66),
	ST_067(67),
	ST_068(68),
	ST_069(69),
	ST_070(70),
	ST_071(71),
	ST_072(72),
	ST_073(73),
	ST_074(74),
	ST_075(75),
	ST_076(76),
	ST_077(77),
	ST_078(78),
	ST_079(79),
	ST_080(80),
	ST_081(81),
	ST_082(82),
	ST_083(83),
	ST_084(84),
	ST_085(85),
	ST_086(86),
	ST_087(87),
	ST_088(88),
	ST_089(89),
	ST_090(90),
	ST_091(91),
	ST_092(92),
	ST_093(93),
	ST_094(94),
	ST_095(95),
	ST_096(96),
	ST_097(97),
	ST_098(98),
	ST_099(99),
	ST_100(100),
	ST_101(101),
	ST_102(102),
	ST_103(103),
	ST_104(104),
	ST_105(105),
	ST_106(106),
	ST_107(107),
	ST_108(108),
	ST_109(109),
	ST_110(110),
	ST_111(111),
	ST_112(112),
	ST_113(113),
	ST_114(114),
	ST_115(115),
	ST_116(116),
	ST_117(117),
	ST_118(118),
	ST_119(119),
	ST_120(120),
	ST_121(121),
	ST_122(122),
	ST_123(123),
	ST_124(124),
	ST_125(125),
	ST_126(126),
	ST_127(127),
	ST_128(128),
	ST_129(129),
	ST_130(130),
	ST_131(131),
	ST_132(132),
	ST_133(133),
	ST_134(134),
	ST_135(135),
	ST_136(136),
	ST_137(137),
	ST_138(138),
	ST_139(139),
	ST_140(140),
	ST_141(141),
	ST_142(142),
	ST_143(143),
	ST_144(144),
	ST_145(145),
	ST_146(146),
	ST_147(147),
	ST_148(148),
	ST_149(149),
	ST_150(150),
	ST_151(151),
	ST_152(152),
	ST_153(153),
	ST_154(154),
	ST_155(155),
	ST_156(156),
	ST_157(157),
	ST_158(158),
	ST_159(159),
	ST_160(160),
	ST_161(161),
	ST_162(162),
	ST_163(163),
	ST_164(164),
	ST_165(165),
	ST_166(166),
	ST_167(167),
	ST_168(168),
	ST_169(169),
	ST_170(170),
	ST_171(171),
	ST_172(172),
	ST_173(173),
	ST_174(174),
	ST_175(175),
	ST_176(176),
	ST_177(177),
	ST_178(178),
	ST_179(179),
	ST_180(180),
	ST_181(181),
	ST_182(182),
	ST_183(183),
	ST_184(184),
	ST_185(185),
	ST_186(186),
	ST_187(187),
	ST_188(188),
	ST_189(189),
	ST_190(190),
	ST_191(191),
	ST_192(192),
	ST_193(193),
	ST_194(194),
	ST_195(195),
	ST_196(196),
	ST_197(197),
	ST_198(198),
	ST_199(199),
	
	TRIG_000(200),
	TRIG_001(201),
	TRIG_002(202),
	TRIG_003(203),
	TRIG_004(204),
	TRIG_005(205),
	TRIG_006(206),
	TRIG_007(207),
	TRIG_008(208),
	TRIG_009(209);
	
	private int type;
	private String name;
	private double chance; // chance enemy spawns will be triggered (if trigger type)
	private String imageLocation;
	private ArrayList<Enemies> enemyTypes;
	private boolean used;
	
	/**
	 * Constructor
	 * @param type int : type number
	 */
	private StreetType(int type){
		this.type = type;
		enemyTypes = new ArrayList<Enemies>();
		chance = 0;
	}
	
	/**
	 * Clears the Street types
	 */
	public static void clear(){
		for (StreetType st : StreetType.values()){
			st.enemyTypes = new ArrayList<Enemies>();
		}
	}
	
	/**
	 * Accessor for the type number.
	 * @return int : type number
	 */
	public int getType(){
		return type;
	}
	
	/**
	 * Accessor for the chance of triggering the enemies.
	 * @return double : chance
	 */
	public double getChance(){
		return chance;
	}
	
	/**
	 * Sets the chance of triggering the enemies
	 * @param path String : path to image location
	 * @param ch double : chance
	 */
	public void setStats(String name, String path, double ch){
		chance = ch;
		this.name = name;
		imageLocation = path;
		used = true;
		
		Images.getInstance().add(imageLocation);
	}
	
	/**
	 * Gets image location for this street type.
	 * @return String : image location
	 */
	public String getImage(){
		return imageLocation;
	}
	
	/**
	 * Accessor for the name of this street type.
	 * @return String : name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Sets the list of enemies associated with this trigger type.  Only
	 * works for enemy trigger types.
	 * @param list ArrayList<Integer> : enemy list
	 */
	public void setEnemies(ArrayList<Enemies> list){
		if(!getIsTrigger()) return;
		enemyTypes = list;
	}
	
	/**
	 * Gets the enemy list associated with this trigger type.  Only
	 * works for enemy trigger types.  Returns null otherwise.
	 * @return ArrayList<Integer> : enemy list
	 */
	public ArrayList<Enemies> getEnemies(){
		if(!getIsTrigger()) return null;
		return enemyTypes;
	}
	
	/**
	 * Checks to see whether or not this street type is a enemy trigger.
	 * @return boolean : trigger
	 */
	public boolean getIsTrigger(){
		switch(this){
		case TRIG_000:
		case TRIG_001:
		case TRIG_002:
		case TRIG_003:
		case TRIG_004:
		case TRIG_005:
		case TRIG_006:
		case TRIG_007:
		case TRIG_008:
		case TRIG_009:
			return true;
		}
		return false;
	}
	
	/**
	 * Returns whether or not this type is used in the current
	 * scenario.
	 * @return boolean : used
	 */
	public boolean getUsed(){
		return used;
	}
	
	/**
	 * Gets the street type with the specified name
	 * @param str String : name of street type
	 * @return StreetType : street type
	 */
	public static StreetType getType(String str){
		for(StreetType s : StreetType.values()){
			try {
				if(s.name.equals(str))
					return s;
			} catch(Exception ex) {}
		}
		return null;
	}
	
	public static void clearAll() {
		for(StreetType s : StreetType.values()) {
			s.setName("");
			s.used = false;
		}
	}
	
	/* ******************************************************
	 * The following methods are used only for the scenario editor
	 * ******************************************************/
	
	public void setName(String str) {
		name = str;
	}
	
	public void setImage(String img) {
		imageLocation = img;
	}
	
	public void setChance(double ch) {
		chance = ch;
	}
	
	public void deleteEnemy(int enemyIndex) {
		for(Enemies e : enemyTypes) {
			if(e.getType() == enemyIndex) {
				enemyTypes.remove(e);
				return;
			}
		}
	}
	
	public void remove() {
		name = "";
		used = false;
	}
}
