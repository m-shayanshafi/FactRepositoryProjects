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
 * Different types for the obstructions in the world.
 * @author Darren Watts
 * date 11/14/07
 *
 */
public enum ObstructionLandType {
	// Type numbers must go from 0 to n-1
	OBS_000(0),
	OBS_001(1),
	OBS_002(2),
	OBS_003(3),
	OBS_004(4),
	OBS_005(5),
	OBS_006(6),
	OBS_007(7),
	OBS_008(8),
	OBS_009(9),
	OBS_010(10),
	OBS_011(11),
	OBS_012(12),
	OBS_013(13),
	OBS_014(14),
	OBS_015(15),
	OBS_016(16),
	OBS_017(17),
	OBS_018(18),
	OBS_019(19),
	OBS_020(20),
	OBS_021(21),
	OBS_022(22),
	OBS_023(23),
	OBS_024(24),
	OBS_025(25),
	OBS_026(26),
	OBS_027(27),
	OBS_028(28),
	OBS_029(29),
	OBS_030(30),
	OBS_031(31),
	OBS_032(32),
	OBS_033(33),
	OBS_034(34),
	OBS_035(35),
	OBS_036(36),
	OBS_037(37),
	OBS_038(38),
	OBS_039(39),
	OBS_040(40),
	OBS_041(41),
	OBS_042(42),
	OBS_043(43),
	OBS_044(44),
	OBS_045(45),
	OBS_046(46),
	OBS_047(47),
	OBS_048(48),
	OBS_049(49),
	OBS_050(50),
	OBS_051(51),
	OBS_052(52),
	OBS_053(53),
	OBS_054(54),
	OBS_055(55),
	OBS_056(56),
	OBS_057(57),
	OBS_058(58),
	OBS_059(59),
	OBS_060(60),
	OBS_061(61),
	OBS_062(62),
	OBS_063(63),
	OBS_064(64),
	OBS_065(65),
	OBS_066(66),
	OBS_067(67),
	OBS_068(68),
	OBS_069(69),
	OBS_070(70),
	OBS_071(71),
	OBS_072(72),
	OBS_073(73),
	OBS_074(74),
	OBS_075(75),
	OBS_076(76),
	OBS_077(77),
	OBS_078(78),
	OBS_079(79),
	OBS_080(80),
	OBS_081(81),
	OBS_082(82),
	OBS_083(83),
	OBS_084(84),
	OBS_085(85),
	OBS_086(86),
	OBS_087(87),
	OBS_088(88),
	OBS_089(89),
	OBS_090(90),
	OBS_091(91),
	OBS_092(92),
	OBS_093(93),
	OBS_094(94),
	OBS_095(95),
	OBS_096(96),
	OBS_097(97),
	OBS_098(98),
	OBS_099(99),
	OBS_100(100),
	OBS_101(101),
	OBS_102(102),
	OBS_103(103),
	OBS_104(104),
	OBS_105(105),
	OBS_106(106),
	OBS_107(107),
	OBS_108(108),
	OBS_109(109),
	OBS_110(110),
	OBS_111(111),
	OBS_112(112),
	OBS_113(113),
	OBS_114(114),
	OBS_115(115),
	OBS_116(116),
	OBS_117(117),
	OBS_118(118),
	OBS_119(119),
	OBS_120(120),
	OBS_121(121),
	OBS_122(122),
	OBS_123(123),
	OBS_124(124),
	OBS_125(125),
	OBS_126(126),
	OBS_127(127),
	OBS_128(128),
	OBS_129(129),
	OBS_130(130),
	OBS_131(131),
	OBS_132(132),
	OBS_133(133),
	OBS_134(134),
	OBS_135(135),
	OBS_136(136),
	OBS_137(137),
	OBS_138(138),
	OBS_139(139),
	OBS_140(140),
	OBS_141(141),
	OBS_142(142),
	OBS_143(143),
	OBS_144(144),
	OBS_145(145),
	OBS_146(146),
	OBS_147(147),
	OBS_148(148),
	OBS_149(149),
	OBS_150(150),
	OBS_151(151),
	OBS_152(152),
	OBS_153(153),
	OBS_154(154),
	OBS_155(155),
	OBS_156(156),
	OBS_157(157),
	OBS_158(158),
	OBS_159(159),
	OBS_160(160),
	OBS_161(161),
	OBS_162(162),
	OBS_163(163),
	OBS_164(164),
	OBS_165(165),
	OBS_166(166),
	OBS_167(167),
	OBS_168(168),
	OBS_169(169),
	OBS_170(170),
	OBS_171(171),
	OBS_172(172),
	OBS_173(173),
	OBS_174(174),
	OBS_175(175),
	OBS_176(176),
	OBS_177(177),
	OBS_178(178),
	OBS_179(179),
	OBS_180(180),
	OBS_181(181),
	OBS_182(182),
	OBS_183(183),
	OBS_184(184),
	OBS_185(185),
	OBS_186(186),
	OBS_187(187),
	OBS_188(188),
	OBS_189(189),
	OBS_190(190),
	OBS_191(191),
	OBS_192(192),
	OBS_193(193),
	OBS_194(194),
	OBS_195(195),
	OBS_196(196),
	OBS_197(197),
	OBS_198(198),
	OBS_199(199),
	OBS_INVALID(200);
	
	private int type;
	private String name;
	private String imageLocation;
	private boolean canBeDestroyed;
	private boolean used = false;
	
	/**
	 * Sets up the type number for the ObstructionLandType.
	 * @param type int : type number
	 */
	private ObstructionLandType(int type){
		this.type = type;
	}
	
	/**
	 * Accessor for the type number.
	 * @return int : type number
	 */
	public int getType(){
		return type;
	}
	
	/**
	 * Gets the image location for this obstruction type.
	 * @return String : path to image location
	 */
	public String getImage(){
		return imageLocation;
	}
	
	/**
	 * Sets the stats of this obstruction.
	 * @param img String : image location
	 * @param destroyable boolean : type is destroyable
	 */
	public void setStats(String img, boolean destroyable, String name){
		imageLocation = img;
		canBeDestroyed = destroyable;
		this.name = name;
		used = true;
		
		Images.getInstance().add(img);
	}
	
	/**
	 * Checks to see whether or not this obstruction type can
	 * be destroyed.  This is only for the type of obstruction,
	 * for example if type 0 can be destroyed, that doesn't
	 * necessarily mean the land object of that type can be
	 * destroyed, but any type that can't be destroyed will
	 * overwrite the land object's destroyed flag.
	 * @return boolean : is boolean
	 */
	public boolean getIsPossiblyDestroyed(){
		return canBeDestroyed;
	}
	
	/**
	 * Gets whether or not this type is used in this scenario.
	 * @return boolean : used
	 */
	public boolean getUsed(){
		return used;
	}
	
	/**
	 * Gets the name that will be displayed on the editor.
	 * @return String : name
	 */
	public String getName(){
		return name;
	}
	
	public static void clearAll() {
		for(ObstructionLandType o : ObstructionLandType.values()) {
			o.setName("");
			o.used = false;
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
	
	public void setDestroyable(boolean bool) {
		canBeDestroyed = bool;
	}
	
	public void remove() {
		name = "";
		used = false;
	}
}
