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

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

import src.game.Messages;
import src.game.SoundThread;
import src.scenario.Images;

/**
 * Class that keeps track of different NPC types.  Each NPC has a type
 * for the map format, and a string that they will say when the player
 * talks to them.
 * @author Darren Watts
 * date 11/15/07
 *
 */
public enum NPCs {
	// Type numbers must go from 0 to n-1
	NPC_000(0),
	NPC_001(1),
	NPC_002(2),
	NPC_003(3),
	NPC_004(4),
	NPC_005(5),
	NPC_006(6),
	NPC_007(7),
	NPC_008(8),
	NPC_009(9),
	NPC_010(10),
	NPC_011(11),
	NPC_012(12),
	NPC_013(13),
	NPC_014(14),
	NPC_015(15),
	NPC_016(16),
	NPC_017(17),
	NPC_018(18),
	NPC_019(19),
	NPC_020(20),
	NPC_021(21),
	NPC_022(22),
	NPC_023(23),
	NPC_024(24),
	NPC_025(25),
	NPC_026(26),
	NPC_027(27),
	NPC_028(28),
	NPC_029(29),
	NPC_030(30),
	NPC_031(31),
	NPC_032(32),
	NPC_033(33),
	NPC_034(34),
	NPC_035(35),
	NPC_036(36),
	NPC_037(37),
	NPC_038(38),
	NPC_039(39),
	NPC_040(40),
	NPC_041(41),
	NPC_042(42),
	NPC_043(43),
	NPC_044(44),
	NPC_045(45),
	NPC_046(46),
	NPC_047(47),
	NPC_048(48),
	NPC_049(49),
	NPC_050(50),
	NPC_051(51),
	NPC_052(52),
	NPC_053(53),
	NPC_054(54),
	NPC_055(55),
	NPC_056(56),
	NPC_057(57),
	NPC_058(58),
	NPC_059(59),
	NPC_060(60),
	NPC_061(61),
	NPC_062(62),
	NPC_063(63),
	NPC_064(64),
	NPC_065(65),
	NPC_066(66),
	NPC_067(67),
	NPC_068(68),
	NPC_069(69),
	NPC_070(70),
	NPC_071(71),
	NPC_072(72),
	NPC_073(73),
	NPC_074(74),
	NPC_075(75),
	NPC_076(76),
	NPC_077(77),
	NPC_078(78),
	NPC_079(79),
	NPC_080(80),
	NPC_081(81),
	NPC_082(82),
	NPC_083(83),
	NPC_084(84),
	NPC_085(85),
	NPC_086(86),
	NPC_087(87),
	NPC_088(88),
	NPC_089(89),
	NPC_090(90),
	NPC_091(91),
	NPC_092(92),
	NPC_093(93),
	NPC_094(94),
	NPC_095(95),
	NPC_096(96),
	NPC_097(97),
	NPC_098(98),
	NPC_099(99),
	NPC_100(100),
	NPC_101(101),
	NPC_102(102),
	NPC_103(103),
	NPC_104(104),
	NPC_105(105),
	NPC_106(106),
	NPC_107(107),
	NPC_108(108),
	NPC_109(109),
	NPC_110(110),
	NPC_111(111),
	NPC_112(112),
	NPC_113(113),
	NPC_114(114),
	NPC_115(115),
	NPC_116(116),
	NPC_117(117),
	NPC_118(118),
	NPC_119(119),
	NPC_120(120),
	NPC_121(121),
	NPC_122(122),
	NPC_123(123),
	NPC_124(124),
	NPC_125(125),
	NPC_126(126),
	NPC_127(127),
	NPC_128(128),
	NPC_129(129),
	NPC_130(130),
	NPC_131(131),
	NPC_132(132),
	NPC_133(133),
	NPC_134(134),
	NPC_135(135),
	NPC_136(136),
	NPC_137(137),
	NPC_138(138),
	NPC_139(139),
	NPC_140(140),
	NPC_141(141),
	NPC_142(142),
	NPC_143(143),
	NPC_144(144),
	NPC_145(145),
	NPC_146(146),
	NPC_147(147),
	NPC_148(148),
	NPC_149(149),
	NPC_150(150),
	NPC_151(151),
	NPC_152(152),
	NPC_153(153),
	NPC_154(154),
	NPC_155(155),
	NPC_156(156),
	NPC_157(157),
	NPC_158(158),
	NPC_159(159),
	NPC_160(160),
	NPC_161(161),
	NPC_162(162),
	NPC_163(163),
	NPC_164(164),
	NPC_165(165),
	NPC_166(166),
	NPC_167(167),
	NPC_168(168),
	NPC_169(169),
	NPC_170(170),
	NPC_171(171),
	NPC_172(172),
	NPC_173(173),
	NPC_174(174),
	NPC_175(175),
	NPC_176(176),
	NPC_177(177),
	NPC_178(178),
	NPC_179(179),
	NPC_180(180),
	NPC_181(181),
	NPC_182(182),
	NPC_183(183),
	NPC_184(184),
	NPC_185(185),
	NPC_186(186),
	NPC_187(187),
	NPC_188(188),
	NPC_189(189),
	NPC_190(190),
	NPC_191(191),
	NPC_192(192),
	NPC_193(193),
	NPC_194(194),
	NPC_195(195),
	NPC_196(196),
	NPC_197(197),
	NPC_198(198),
	NPC_199(199);

	private int type;
	private String name;
	private String talkString;
	private String imageLocation;
	private String soundPath;
	private boolean used;
	private AudioClip sound = null;

	/**
	 * Constructor for the enum.  Sets the type number associated
	 * with this NPC.
	 * @param type int : type number.  Used for the map format.
	 */
	private NPCs(int type){
		this.type = type;
		soundPath = "";
	}

	/**
	 * Sets this NPCs name and text.  The text is what will be sent
	 * to the messages window when a player talks to this NPC. 
	 * This will be set when reading in from a scenario file.
	 * @param name String : name of the NPC.
	 * @param talk String : message of the NPC.
	 * @param image String : image location
	 * @param soundFile String : path to sound file
	 */
	public void setStats(String name, String talk, String image, String soundFile){
		this.name = name;
		talkString = talk;
		imageLocation = image;
		used = true;

		soundPath = soundFile;
		if(!soundFile.equals("")){
			try {
				URL u;
				u = new URL("file:///" + System.getProperty("user.dir") + "/" + soundFile);
				sound = Applet.newAudioClip(u);

			} catch(Exception ex) {}

		}

		Images.getInstance().add(image);
	}

	/**
	 * Adds this NPCs talk string to the messages. And plays NPC sound
	 * if one exists.
	 */
	public void talk(){
		Messages.getInstance().addMessage(name + ": " + talkString);
		
		try{
			if(sound != null)
				SoundThread.getInstance().setSound(sound);
		} catch(Exception ex) { }
	}

	/**
	 * Image location for this NPC.
	 * @return String : image location
	 */
	public String getImage(){
		return imageLocation;
	}

	/**
	 * Accessor for the type number of this NPC.
	 * @return int : type number.
	 */
	public int getType(){
		return type;
	}

	/**
	 * Accessor for the name of this NPC.
	 * @return String : name
	 */
	public String getName(){
		return name;
	}

	/**
	 * Gets whether or not this type is used in the scenario.
	 * @return boolean : used
	 */
	public boolean getUsed(){
		return used;
	}
	
	public static void clearAll() {
		for(NPCs n : NPCs.values()) {
			n.setName("");
			n.used = false;
		}
	}
	
	/* ******************************************************
	 * The following methods are used only for the scenario editor
	 * ******************************************************/
	
	public void setName(String str) {
		name = str;
	}
	
	public void setImage(String str) {
		imageLocation = str;
	}
	
	public void setSoundPath(String str) {
		soundPath = str;
	}
	
	public void setMessage(String str) {
		talkString = str;
	}
	
	public String getMessage() {
		return talkString;
	}
	
	public String getSoundPath() {
		return soundPath;
	}

	public void remove() {
		talkString = "";
		name = "";
		imageLocation = "";
		soundPath = "";
		used = false;
	}
}
