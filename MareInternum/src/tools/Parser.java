/*	 
*    This file is part of Mare Internum.
*
*	 Copyright (C) 2008,2009  Johannes Hoechstaedter
*
*    Mare Internum is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    Mare Internum is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with Mare Internum.  If not, see <http://www.gnu.org/licenses/>.
*
*/

package tools;

import entities.KonstantenProtokoll;

/**
 * This class parses a string, and replaces key chars,
 * which are used in another context.
 * @author johannes
 *
 */
public class Parser {
	
	//replace parameters for separators
	public static final String PRSEM="a1l2s3k4d5j6f7h8g3";
	public static final String PRSKOMMA="q1p2w3o4e5i6r7u8t9z";
	public static final String PRSSLASH="12347890qwtzhgasflj";
	
	public Parser(){}
	
	/**
	 * add key chars to an encoded string
	 * @param s
	 * @return
	 */
	public String decode(String s){
		s=s.replaceAll(PRSKOMMA,KonstantenProtokoll.SEPARATORSUBJEKT);
		s=s.replaceAll(PRSEM,KonstantenProtokoll.SEPARATORHEADER);
		s=s.replaceAll(PRSSLASH,KonstantenProtokoll.SEPARATOR1);
		return s;
	}
	
	/**
	 * remove key chars from a string
	 * @param s
	 * @return
	 */
	public String encode(String s){
		s=s.replaceAll(KonstantenProtokoll.SEPARATORSUBJEKT,PRSKOMMA);
		s=s.replaceAll(KonstantenProtokoll.SEPARATORHEADER,PRSEM);
		s=s.replaceAll(KonstantenProtokoll.SEPARATOR1,PRSSLASH);
		return s;
	}
}
