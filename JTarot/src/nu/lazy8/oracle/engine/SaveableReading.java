/*
 *  :tabSize=4:indentSize=2:noTabs=true:
 *  :folding=explicit:collapseFolds=1:
 *
 *  Copyright (C) 2007 Thomas Dilts.  This program is free
 *  software; you can redistribute it and/or modify it under the terms of the
 *  GNU General Public License as published by the Free Software Foundation;
 *  either version 2 of the License, or (at your option) any later version. This
 *  program is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details. You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software Foundation,
 *  Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA For more
 *  information, surf to www.tarot.lazy8.nu or email tarot@lazy8.nu
 */

package nu.lazy8.oracle.engine;

//import nu.lazy8.oracle.util.*;
import java.io.*;
import java.util.*;

public class SaveableReading {
	public String groupname;
    public String groupnametrans;
	public String layoutname;
	public String layoutlanguage;
	public String graphicalobjects;
	public String graphicalobjectsDef;
	public int[] chosenObjects;
	public boolean[] choosenObjectsUpsideDown;
    public Date timestamp;
	public String question;
	public String comments;

    public SaveableReading(byte[] buffer)throws Exception{
      readRecordBytes(buffer);
    }
    public SaveableReading(InputStream inpStr)throws Exception{
      readReadingXml(inpStr);
    }
    public SaveableReading(String groupname,String groupnametrans,
        String layoutname,String layoutlanguage,String graphicalobjects,
        String graphicalobjectsDef, int[] chosenObjects,
        boolean[] choosenObjectsUpsideDown,
        String question,String comments,Date timestamp){
      this.groupname=groupname;
      this.groupnametrans=groupnametrans;
      this.layoutname=layoutname;
      this.layoutlanguage=layoutlanguage;
      this.graphicalobjects=graphicalobjects;
      this.graphicalobjectsDef=graphicalobjectsDef;
      this.chosenObjects=chosenObjects;
      this.choosenObjectsUpsideDown=choosenObjectsUpsideDown;
      this.question=question;
      this.comments=comments;
      this.timestamp=timestamp;
    }
    public void copyToOracleReading(OracleReading or)throws Exception{
      or.SetReading(chosenObjects,choosenObjectsUpsideDown,timestamp);
      or.Reload(groupname,groupnametrans,graphicalobjects,graphicalobjectsDef,layoutname,layoutlanguage);
      or.SetReading(chosenObjects,choosenObjectsUpsideDown,timestamp);
    }
    public void writeReadingXml(OutputStream outStr)throws Exception{
      Hashtable keyValuePairs=new Hashtable();
      keyValuePairs.put("layoutname",layoutname);
      keyValuePairs.put("graphicalobjects",graphicalobjects);
      keyValuePairs.put("graphicalobjectsDef",graphicalobjectsDef);
      String outText="";
      for(int i=0;i<chosenObjects.length;i++)outText=outText + chosenObjects[i] + " ";
      keyValuePairs.put("chosenObjects",outText);
      outText="";
      for(int i=0;i<choosenObjectsUpsideDown.length;i++)
        outText=outText + (choosenObjectsUpsideDown[i]?"true":"false") + " ";
      keyValuePairs.put("choosenObjectsUpsideDown",outText);
      keyValuePairs.put("question",question);
      keyValuePairs.put("comments",comments);
      keyValuePairs.put("timestamp",timestamp.getTime());
      OracleStandardFileParser parser=new OracleStandardFileParser(keyValuePairs,groupname,"","Reading","","1","");
      parser.writeXmlFile(outStr);
    }
    public void readReadingXml(InputStream inpStr)throws Exception{
      OracleStandardFileParser parser=new OracleStandardFileParser(inpStr);
      if(!parser.getFileAtt("classification").equals("Reading")){
        throw new Exception("Bad file or not an oracle reading file. " + parser.getFileAtt("classification") );
      }
      this.groupname=parser.getFileAtt("groupname");
      this.groupnametrans="";
      this.layoutname=parser.translate("layoutname");
      this.layoutlanguage="";
      this.graphicalobjects=parser.translate("graphicalobjects");
      this.graphicalobjectsDef=parser.translate("graphicalobjectsDef");
	  StringTokenizer tokenizer = new StringTokenizer( parser.translate( "chosenObjects" ) );
      int numObjects=0;
      while ( tokenizer.hasMoreTokens() ) {
        tokenizer.nextToken();
        numObjects++;
      }
      tokenizer = new StringTokenizer( parser.translate( "chosenObjects" ) );
      chosenObjects=new int[numObjects];
      for ( int i = 0; i < chosenObjects.length; i++ )
        chosenObjects[i] = Integer.parseInt( tokenizer.nextToken() );
      tokenizer = new StringTokenizer( parser.translate( "choosenObjectsUpsideDown" ) );
      choosenObjectsUpsideDown=new boolean[numObjects];
      for ( int i = 0; i < choosenObjectsUpsideDown.length; i++ )
        choosenObjectsUpsideDown[i] = tokenizer.nextToken().equals("true")?true:false;
      this.question=parser.translate("question");
      this.comments=parser.translate("comments");
      try{
      this.timestamp=new Date(Long.parseLong(parser.translate("timestamp")));
      }catch(Exception e){}
    }
    public byte[] getReadingBytes()throws Exception{
        ByteArrayOutputStream baos;
        DataOutputStream das;
        byte[] data;
        try {
            baos = new ByteArrayOutputStream();
            das = new DataOutputStream(baos);

            das.writeUTF(groupname);
            das.writeUTF(groupnametrans);
            das.writeUTF(layoutname);
            das.writeUTF(layoutlanguage);
            das.writeUTF(graphicalobjects);
            das.writeUTF(graphicalobjectsDef);
            das.writeInt(chosenObjects.length);
           String tmpString=new String();
            for (int i=0;i<chosenObjects.length;i++){
              tmpString=tmpString + " " + new Integer(chosenObjects[i]).toString();
            }
            das.writeUTF(tmpString);
            tmpString="";
            for (int i=0;i<choosenObjectsUpsideDown.length;i++){
              tmpString=tmpString + " " +(choosenObjectsUpsideDown[i]?"1":"0") ;
            }
            das.writeUTF(tmpString);
            das.writeLong(timestamp.getTime());
            das.writeUTF(question);
            das.writeUTF(comments);
    
            data = baos.toByteArray();
            das.close();
            return data;
        } catch (Exception ioe) {
            throw ioe;
        }
    }
    private void readRecordBytes(byte[] buffer)throws Exception{
        ByteArrayInputStream bais;
        DataInputStream dis;
        try {
          bais = new ByteArrayInputStream(buffer);
          dis = new DataInputStream(bais);
          groupname=dis.readUTF();
          groupnametrans=dis.readUTF();
          layoutname=dis.readUTF();
          layoutlanguage=dis.readUTF();
          graphicalobjects=dis.readUTF();
          graphicalobjectsDef=dis.readUTF();
          chosenObjects=new int[dis.readInt()];
          StringTokenizer tokenizer = new StringTokenizer( dis.readUTF() );
          for ( int i = 0; i < chosenObjects.length; i++ )
            chosenObjects[i] = Integer.parseInt( tokenizer.nextToken() );
          choosenObjectsUpsideDown=new boolean[chosenObjects.length];
          tokenizer = new StringTokenizer( dis.readUTF() );
          for ( int i = 0; i < choosenObjectsUpsideDown.length; i++ )
            choosenObjectsUpsideDown[i] = tokenizer.nextToken().equals("0") ?false:true;
          timestamp=new Date(dis.readLong());
          question=dis.readUTF();
          comments=dis.readUTF();

          dis.close();
        } catch (Exception ioe) {
            throw ioe;
        }
    }
}

