package MyCalculator.Entity;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

import javax.swing.*;

import MyCalculator.Lobby;

public class Variable implements ActionListener{
    public JPanel selfPanel;//
    public JTextArea valueText;//
    public JButton modifyButton;//
    public JScrollPane scrollPane;//
    public ExpressionCollector dialog;//
    public String name;
    public String value="";
    public Variable(){
        initialize();
    }
    public Variable(String str,Boolean ini){
        name=str;
        if(ini)initialize();
    }
    public Variable(String str,String val){
        name=str;
        value =val;
    }
    public void initialize(){
        selfPanel=new JPanel(null);
        modifyButton=new JButton("...");//
        valueText=new JTextArea();//
        scrollPane=new JScrollPane(valueText);//
        dialog=new ExpressionCollector(this);//

        for(JComponent c:Arrays.asList(scrollPane,modifyButton)){
            selfPanel.add(c);
            c.setFont(Lobby.signFont);
            c.setBorder(BorderFactory.createLineBorder(Color.lightGray,1));
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
