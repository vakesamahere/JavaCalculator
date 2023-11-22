package MyCalculator.Entity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class Keyboard extends JDialog{
    private Key[] keys = new Key[30];
    private List<Character> hotKeys = new ArrayList<>();
    private JTextArea output;
    private ExpressionEditor parent;
    private int index=0;
    public Keyboard(ExpressionEditor eed){
        parent=eed;
        output=parent.textArea;
        setLayout(new GridLayout(0,5));
        iniKeys();
        this.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                output.setForeground(Color.BLACK);
            }
            @Override
            public void windowLostFocus(WindowEvent e) {
                output.setForeground(Color.WHITE);
            }
        
        });
    }
    public void iniKeys() {
        newKey("+", "+", '+', 1);
        newKey("-", "-", '-', 1);
        newKey("x", "*", '*', 1);
        newKey("÷", "/", '/', 1);
        newKey("^", "^", '^', 1);

        newKey("%", "%", '%', 1);
        newKey("()", "()", '(', 1);
        newKey("abs", "abs()", '|', 4);
        newKey("exp", "exp()", 'E', 4);
        newKey("ln", "ln()", 'L', 3);

        newKey("π", "π", 'P', 1);
        newKey("log", "log(,)", '\\', 4);
        newKey("∑", "∑(,,,)", '<', 2);
        newKey("∏", "∏(,,,)", '>', 2);
        newKey("∫", "∫(,,,)", 'I', 2);

        newKey("sin", "sin()", 'S', 4);
        newKey("cos", "cos()", 'C', 4);
        newKey("tan", "tan()", 'T', 4);

        newKey("arcsin", "arcsin()", '!', 7);
        newKey("arccos", "arccos()", '@', 7);
        newKey("arctan", "arctan()", '#', 7);

        
    }
    public void newKey(String name,String va,char key,int cOffset){
        keys[index]=new Key(name,va,key,cOffset,this);
        add(keys[index++]);
        hotKeys.add(key);
    }
    public void addValue(String value,int offset) {
        int pos = output.getCaretPosition();
        output.insert(value, pos);
        output.setCaretPosition(pos+offset);
    }
    public boolean keyTyped(char keyChar) {
        if(keyChar==' ')return false;
        int index=hotKeys.indexOf(keyChar);
        if(index==-1)return false;
        keys[index].click();
        return true;
    }
}
