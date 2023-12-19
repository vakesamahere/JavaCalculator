package mycalculator.entity;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import javax.swing.*;

import mycalculator.Lobby;
import mycalculator.tools.DocHistoryRecorder;

public class Variable {
    public static final Color buttonColor = new Color(255, 255, 255);
    protected JPanel selfPanel;
    protected JTextArea valueText;
    protected JButton modifyButton;
    protected JScrollPane scrollPane;
    protected String name;
    protected String value="";
    protected DocHistoryRecorder dhr;
    protected ActionListener modifyAl = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
            if(e.getSource()==modifyButton){
                select();
            }
        }
    };
    public Variable(){
        initialize();
    }
    public Variable(String str,Boolean ini){
        name=str;
        if(ini){
            initialize();
        }
    }
    /**抽象变量，不设置panel*/
    public Variable(String str,String val){
        name=str;
        value =val;
    }
    public void initialize(){
        selfPanel=new JPanel(null);
        modifyButton=new JButton("...");
        valueText=new JTextArea();
        scrollPane=new JScrollPane(valueText);

        modifyButton.setBackground(buttonColor);
        modifyButton.setFocusable(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.lightGray,1));

        for(JComponent c:Arrays.asList(scrollPane,modifyButton)){
            selfPanel.add(c);
            c.setFont(Lobby.signFont);
        }
        valueText.setFont(Lobby.smallFormatFont);
        valueText.setLineWrap(true);

        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        modifyButton.addActionListener(modifyAl);

        dhr = new DocHistoryRecorder(valueText);
    }
    public void select(){
        Lobby.getExpressionEditor().selectDhr(dhr);
        Lobby.getExpressionEditor().setTarget(valueText);
        Lobby.getExpressionEditor().setDoc(valueText.getDocument());
        Lobby.getExpressionEditor().getTextArea().requestFocus();
        Lobby.getExpressionEditor().getTextArea().setCaretPosition(valueText.getCaretPosition());
        Lobby.getExpressionEditor().setSelectedButton(modifyButton);
    }
    public DocHistoryRecorder getRecorder(){
        return dhr;
    }
    public String getName(){
        return name;
    }
    public JTextArea getValueArea(){
        return valueText;
    }
    public String getValue(){
        return value;
    }
    public void setValue(String str){
        value=str;
    }
}