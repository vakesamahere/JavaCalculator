package MyCalculator.Entity;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class Key extends JButton{
    public final String value;
    public final char hotkey;
    private Keyboard keyboard;
    private int offset;
    public Key(String name,String va,char key,int cOffset,Keyboard parent){
        super((key==' ')?name:String.format("%s [%s]", name,key));
        value = va;
        hotkey = key;
        keyboard = parent;
        offset=cOffset;
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyboard.addValue(value,offset);
            }
        });
    }
    public void click(){
        keyboard.addValue(value,offset);
    }
    public String[] getMatcher(){
        String[] output = new String[2];
        output[0]=value.substring(0, offset);
        output[1]=value.substring(offset);
        return output;
    }
}
