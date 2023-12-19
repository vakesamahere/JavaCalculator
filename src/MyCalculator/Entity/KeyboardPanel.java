package mycalculator.entity;

import javax.swing.*;

import mycalculator.Lobby;
import mycalculator.tools.DocHistoryRecorder;

import java.awt.*;
import java.awt.event.*;

import java.util.List;
import java.util.ArrayList;
public class KeyboardPanel extends JPanel{
    private static final Color buttonColor = new Color(240, 240, 240);
    private static final int cols = 8;
    private static final double ratio = 0.23;
    private Font font;
    private KeyButton[] keys = new KeyButton[100];
    private List<Character> hotKeys = new ArrayList<>();
    private JTextArea output;
    private ExpressionEditor parent;
    private int index=0;

    private JPanel opPanel;
    public KeyboardPanel(){

        setLayout(new GridLayout(1,1));
        parent = Lobby.getExpressionEditor();
        setParent(parent);
        //
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int bound = Math.max((int)(ratio*screenSize.getWidth()),(int)(ratio*screenSize.getHeight()))/5;
    
        font =new Font("Microsoft Yahei", Font.BOLD, bound/7);
        
        opPanel=new JPanel();
        add(opPanel);
        opPanel.setLayout(new GridLayout(0,cols));
        opPanel.setFont(font);
        iniKeys();
        setSize(bound*(cols*2),bound*((index-1)/cols-3));
    }
    public void setParent(ExpressionEditor eed){
        parent=eed;
        output=parent.getTextArea();
        parent.setKeyboard(this);
    }
    public void iniKeys() {
        newKey("plus +", "+", ' ', 1,opPanel);
        newKey("minus -", "-", ' ', 1,opPanel);
        newKey("time x", "*", ' ', 1,opPanel);
        newKey("divide ÷", "/", ' ', 1,opPanel);
        newKey("power ^", "^", ' ', 1,opPanel);

        newKey("1", "1", ' ', 1,opPanel);
        newKey("2", "2", ' ', 1,opPanel);
        newKey("3", "3", ' ', 1,opPanel);
        //
        newKey("array []", "[]", '[', 1,opPanel);
        newKey("bracket ()", "()", '(', 1,opPanel);
        newKey("abs", "abs()", '|', 4,opPanel);
        newKey("exp", "exp()", 'e', 4,opPanel);
        newKey("ln", "ln()", 'l', 3,opPanel);

        newKey("4", "4", ' ', 1,opPanel);
        newKey("5", "5", ' ', 1,opPanel);
        newKey("6", "6", ' ', 1,opPanel);
        //
        newKey("∑ sum", "∑(,,,)", ';', 2,opPanel);
        newKey("∏ mul", "∏(,,,)", '\'', 2,opPanel);
        newKey("∫ inte", "∫(,,,)", 'I', 2,opPanel);
        newKey("arr∑ asum", "arr∑(,,,)", ':', 5,opPanel);
        newKey("arr∏ amul", "arr∏(,,,)", '"', 5,opPanel);

        newKey("7", "7", ' ', 1,opPanel);
        newKey("8", "8", ' ', 1,opPanel);
        newKey("9", "9", ' ', 1,opPanel);
        //
        newKey("sin", "sin()", 's', 4,opPanel);
        newKey("cos", "cos()", 'c', 4,opPanel);
        newKey("tan", "tan()", 't', 4,opPanel);
        newKey("percent %", "%", ' ', 1,opPanel);
        newKey("comma ,", ",", ' ', 1,opPanel);
        
        newKey(".", ".", ' ', 1,opPanel);
        newKey("0", "0", ' ', 1,opPanel);
        newKey("-", "-", ' ', 1,opPanel);
        //
        newKey("arcsin", "arcsin()", 'S', 7,opPanel);
        newKey("arccos", "arccos()", 'C', 7,opPanel);
        newKey("arctan", "arctan()", 'T', 7,opPanel);
        newKey("π", "π", 'P', 1,opPanel);
        newKey("log", "log(,)", 'L', 4,opPanel);
        //
        newKey("Mean", "E()", 'E', 2,opPanel);
        newKey("Var", "D()", 'D', 2,opPanel);
        newKey("CoVar", "cov()", 'V', 4,opPanel);
        newKey("JointRaw", "jr()", 'j', 3,opPanel);
        newKey("JointCol", "jc()", 'J', 3,opPanel);
        //
        newKey("Det", "det()", 'd', 4,opPanel);
        newKey("Trans", "tr()", 't', 3,opPanel);
        newKey("Inv", "inv()", 'i', 4,opPanel);
        keys[index++]=new FunctionalKey("left <<",this,opPanel,buttonColor,new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int pos = output.getCaretPosition();
                
                if(pos>0){
                    output.setCaretPosition(pos-1);
                }
                output.requestFocus();
            }
        });
        keys[index++]=new FunctionalKey("right >>",this,opPanel,buttonColor,new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int pos = output.getCaretPosition();
                if(pos<output.getText().length()){
                    output.setCaretPosition(pos+1);
                }
                output.requestFocus();
            }
        });
        
        //
        keys[index++]=new FunctionalKey("Backspace",this,opPanel,buttonColor,new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int pos = output.getCaretPosition();
                String text=output.getText();
                if(pos>0){
                    parent.getDocRecorder().softInput();
                    output.setText(text.substring(0,pos-1)+text.substring(pos));
                    parent.getDocRecorder().softInputEnd();
                    output.setCaretPosition(pos-1);
                    parent.getDocRecorder().addHistory(DocHistoryRecorder.EDO);
                }
                output.requestFocus();
            }
        });
        keys[index++]=new FunctionalKey("Undo",this,opPanel,buttonColor,new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.getDocRecorder().undo();
                output.requestFocus();
            }
        });
        keys[index++]=new FunctionalKey("Redo",this,opPanel,buttonColor,new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.getDocRecorder().redo();
                output.requestFocus();
            }
        });
        keys[index++]=new FunctionalKey("CE Clear",this,opPanel,buttonColor,new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                output.setText("");
                output.requestFocus();
            }
        });
        //*************************************************************

        
    }
    public void newKey(String name,String va,char key,int cOffset,JPanel p){
        keys[index]=new KeyButton(name,va,key,cOffset,this,p,buttonColor);
        hotKeys.add(key);
        index++;
    }
    public void newAir(){
        keys[index++]=new AirButton();
    }
    public void addValue(String value,int offset) {
        int pos = output.getCaretPosition();
        //添加值
        parent.getDocRecorder().softInput();
        output.insert(value, pos);
        parent.getDocRecorder().softInputEnd();
        //移动光标
        output.setCaretPosition(pos+offset);
        Lobby.getExpressionEditor().setTargetCaretPos(pos+offset);
        output.requestFocus();
        parent.getDocRecorder().addHistory(DocHistoryRecorder.EDO);
    }
    public boolean keyTyped(char keyChar) {
        if(keyChar==' '){
            return false;
        }
        int ind=hotKeys.indexOf(keyChar);
        if(ind==-1){
            return false;
        }
        keys[ind].click();
        return true;
    }
}
