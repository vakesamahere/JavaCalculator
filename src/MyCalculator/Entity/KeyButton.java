package mycalculator.entity;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
public class KeyButton extends JButton{
    public final String value;
    public final char hotkey;
    private KeyboardPanel keyboard;
    private int offset;
    private JLabel nameLabel;
    private JLabel keyLabel;
    public KeyButton(){
        value = "";
        hotkey = ' ';
    }
    public KeyButton(String name,String va,char key,int cOffset,KeyboardPanel parent,JPanel container,Color backColor){
        container.add(this);
        setMargin(new Insets(0, 0, 0, 0));
        setBackground(backColor);
        setLayout(new GridLayout(2,1));
        nameLabel=new JLabel(name);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setFont(getParent().getFont());
        add(nameLabel);
        if(key==' '){
            keyLabel=new JLabel();
        }else{
            keyLabel=new JLabel(String.format("< %s >", key));
        }
        keyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        keyLabel.setFont(getParent().getFont());
        add(keyLabel);
        value = va;
        hotkey = key;
        keyboard = parent;
        offset=cOffset;
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                click();
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
class AirButton extends KeyButton{
    public AirButton(){
        super();
        setEnabled(false);
    }
}