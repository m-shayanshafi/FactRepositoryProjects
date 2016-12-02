package kw.chinesechess.view;
import java.awt.*;

public final class renewDialog extends Dialog
{
    BlueButton continueBtn=new BlueButton("Continue");
    BlueButton exitBtn=new BlueButton("Exit");
    String theChoice;
    Frame theParent;
    
    public renewDialog(Frame ancestor)
    {
        super(ancestor, true);
        setLayout(new GridLayout(1, 2, 30, 30));
     //  setBounds(200, 100, 100, 36);
        add(continueBtn);
        add(exitBtn);
//        theChoice=choice;
        theParent=ancestor;
    }
    
    public Insets insets()
    {
        return new Insets(50, 50, 50, 50);
    }
    
    public boolean action(Event evt, Object arg)
    {
        if(evt.target instanceof Button)
        {
            String label=(String)arg;
            if(label=="Continue")
            {
                //theParent.init();
            }
            else
            {
                //theParent.stop();
                System.exit(0);
            }
            hide();
            return true;
        }
        else return false;
    }
}
        
class BlueButton extends Button
{
    BlueButton(String label)
    {
        super(label);
        setBackground(Color.blue);
        setForeground(Color.white);
    }
}