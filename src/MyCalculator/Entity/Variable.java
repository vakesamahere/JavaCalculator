package mycalculator.entity;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import javax.swing.*;

import mycalculator.Lobby;

public class Variable implements ActionListener{
    protected static final Color buttonColor = new Color(255, 255, 255);
    protected JPanel selfPanel;
    protected JTextArea valueText;
    protected JButton modifyButton;
    protected JScrollPane scrollPane;
    protected ExpressionEditor dialog;
    protected String name;
    protected String value="";
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
        dialog=new ExpressionEditor(this);

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

        modifyButton.addActionListener(this);
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
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==modifyButton){
            dialog.setVisible(true);
            dialog.focusGained(null);
        }
    }
}