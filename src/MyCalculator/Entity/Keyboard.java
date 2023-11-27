package MyCalculator.Entity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
public class Keyboard extends JDialog{
    private static final Color buttonColor = new Color(240, 240, 240);
    private static final int cols = 5;
    private static final double ratio = 0.25;
    private Font font1;
    private Font font2;
    private Key[] keys = new Key[100];
    private List<Character> hotKeys = new ArrayList<>();
    private JTextArea output;
    private ExpressionEditor parent;
    private int index=0;

    private JPanel opPanel;
    private JPanel numPanel;
    public Keyboard(){

        setTitle("KEYBOARD");
        setLayout(new GridLayout(1,2));

        //
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int bound = Math.max((int)(ratio*screenSize.getWidth()),(int)(ratio*screenSize.getHeight()))/5;
        
        font1 =new Font("Microsoft Yahei", Font.PLAIN, bound/2);
        font2 =new Font("Microsoft Yahei", Font.BOLD, bound/7);
        
        //parent=eed;
        opPanel=new JPanel();
        numPanel =new JPanel();
        add(opPanel);
        add(numPanel);
        opPanel.setLayout(new GridLayout(0,cols));
        numPanel.setLayout(new GridLayout(0,3));
        opPanel.setFont(font2);
        numPanel.setFont(font1);
        iniKeys();
        setSize(bound*cols*2,bound*((index-1)/cols));
        setResizable(false);
        setAutoRequestFocus(false);

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
    public void setParent(ExpressionEditor eed){
        parent=eed;
        output=parent.textArea;
    }
    public void iniKeys() {
        newKey("plus +", "+", ' ', 1,opPanel);
        newKey("minus -", "-", ' ', 1,opPanel);
        newKey("time x", "*", ' ', 1,opPanel);
        newKey("divide ÷", "/", ' ', 1,opPanel);
        newKey("power ^", "^", ' ', 1,opPanel);

        newKey("array []", "[]", '[', 1,opPanel);
        newKey("bracket ()", "()", '(', 1,opPanel);
        newKey("abs", "abs()", '|', 4,opPanel);
        newKey("exp", "exp()", 'e', 4,opPanel);
        newKey("ln", "ln()", 'l', 3,opPanel);

        newKey("∑ sum", "∑(,,,)", ';', 2,opPanel);
        newKey("∏ mul", "∏(,,,)", '\'', 2,opPanel);
        newKey("∫ inte", "∫(,,,)", 'I', 2,opPanel);
        newKey("arr∑ asum", "arr∑(,,,)", ':', 5,opPanel);
        newKey("arr∏ amul", "arr∏(,,,)", '"', 5,opPanel);

        newKey("sin", "sin()", 's', 4,opPanel);
        newKey("cos", "cos()", 'c', 4,opPanel);
        newKey("tan", "tan()", 't', 4,opPanel);
        newKey("percent %", "%", ' ', 1,opPanel);
        newKey("comma ,", ",", ' ', 1,opPanel);

        newKey("arcsin", "arcsin()", 'S', 7,opPanel);
        newKey("arccos", "arccos()", 'C', 7,opPanel);
        newKey("arctan", "arctan()", 'T', 7,opPanel);
        newKey("π", "π", 'P', 1,opPanel);
        newKey("log", "log(,)", 'L', 4,opPanel);

        newKey("Mean", "E()", 'E', 2,opPanel);
        newKey("Var", "D()", 'D', 2,opPanel);
        newKey("CoVar", "cov()", 'V', 4,opPanel);
        newKey("JointRaw", "jr()", 'j', 3,opPanel);
        newKey("JointCol", "jc()", 'J', 3,opPanel);
        
        newKey("Det", "det()", 'd', 4,opPanel);
        newKey("Trans", "tr()", 't', 3,opPanel);
        newKey("Inv", "inv()", 'i', 4,opPanel);
        newKey("Backspace", "", ' ', 4,opPanel);

        //*************************************************************
        newKey("1", "1", ' ', 1,numPanel);
        newKey("2", "2", ' ', 1,numPanel);
        newKey("3", "3", ' ', 1,numPanel);
        newKey("4", "4", ' ', 1,numPanel);
        newKey("5", "5", ' ', 1,numPanel);
        newKey("6", "6", ' ', 1,numPanel);
        newKey("7", "7", ' ', 1,numPanel);
        newKey("8", "8", ' ', 1,numPanel);
        newKey("9", "9", ' ', 1,numPanel);
        newKey(".", ".", ' ', 1,numPanel);
        newKey("0", "0", ' ', 1,numPanel);
        newKey("-", "-", ' ', 1,numPanel);

        
    }
    public void newKey(String name,String va,char key,int cOffset,JPanel p){
        keys[index]=new Key(name,va,key,cOffset,this,p,buttonColor);
        hotKeys.add(key);
        index++;
    }
    public void addValue(String value,int offset) {
        int pos = output.getCaretPosition();
        output.insert(value, pos);
        output.setCaretPosition(pos+offset);
    }
    public boolean keyTyped(char keyChar) {
        if(keyChar==' ')return false;
        int ind=hotKeys.indexOf(keyChar);
        if(ind==-1)return false;
        keys[ind].click();
        return true;
    }
}
