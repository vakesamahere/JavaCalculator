package mycalculator.entity;

import java.awt.Color;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class FunctionalKey extends KeyButton {
    public FunctionalKey(String name,KeyboardPanel parent, JPanel container,Color backColor,ActionListener al) {
        super(name, "", ' ', 0, parent, container, backColor);
        addActionListener(al);
    }
    @Override
    public void click(){
        //do nothing but the "al"
    }
    
}