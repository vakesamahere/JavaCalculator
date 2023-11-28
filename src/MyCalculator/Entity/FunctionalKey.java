package MyCalculator.Entity;

import java.awt.Color;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class FunctionalKey extends Key {
    public FunctionalKey(String name,Keyboard parent, JPanel container,Color backColor,ActionListener al) {
        super(name, "", ' ', 0, parent, container, backColor);
        addActionListener(al);
    }
    @Override
    public void click(){
        //do nothing
    }
    
}