package cases;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class CaseBord  extends Case {
        
        private static String img1 = "./img/pixelart.png";
        
        public CaseBord()
        {

        }
        
        public int type(){
        	return BORD;
        }
}