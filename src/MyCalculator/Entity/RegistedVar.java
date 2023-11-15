package MyCalculator.Entity;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

import javax.swing.*;

import MyCalculator.Lobby;
import MyCalculator.Tools.ComponentEditor;

public class RegistedVar extends Variable{
    private static final double heightRatio = 0.08;
    private JButton deleteButton;
    private JButton up;
    private JButton down;
    private JTextField nameText;
    private VariableRigisterLabel owner;

    public RegistedVar(VariableRigisterLabel father){
        super();
        owner=father;
        load();
    }
    public void load(){
        deleteButton = new JButton("del");
        nameText = new JTextField();
        up = new JButton("∧");
        down = new JButton("v");
        nameText.setFont(Lobby.smallFormatFont);

        nameText.addActionListener(this);
        deleteButton.addActionListener(this);
        up.addActionListener(this);
        down.addActionListener(this);
        owner.getVarPanel().add(selfPanel);
        refreshComponent();
    }
    public void refreshComponent(){
        for(JComponent c:Arrays.asList(nameText,deleteButton,up,down)){
            selfPanel.add(c);
            c.setFont(Lobby.signFont);
            c.setBorder(BorderFactory.createLineBorder(Color.lightGray,1));
        }
        valueText.setFont(Lobby.formatFont);

        JPanel panel = owner.getVarPanel();
        int posCount = owner.getVarList().indexOf(this);
        Rectangle rec = panel.getBounds();
        Rectangle recf = owner.getBounds();
        int sizeY = (int)(recf.height*heightRatio);
        int posY = sizeY*posCount;
        int width = (int)(rec.width/3);
        selfPanel.setBounds(0,posY,rec.width-2,sizeY);

        //need to capture max(widthSet,widthString)
        nameText.setBounds(3,0,width/2,sizeY);
        scrollPane.setBounds(nameText.getX()+nameText.getWidth()+1,0,width*5/4,sizeY);
        deleteButton.setBounds(scrollPane.getX()+scrollPane.getWidth()+1,0,width/3,sizeY);
        modifyButton.setBounds(deleteButton.getX()+deleteButton.getWidth()+1,0,width/4,sizeY);
        up.setBounds(modifyButton.getX()+modifyButton.getWidth()+1,0,width/6,sizeY);
        down.setBounds(up.getX()+up.getWidth()+1,0,width/6,sizeY);

        int height = panel.getPreferredSize().height;
        panel.setPreferredSize(new Dimension(rec.width,height+sizeY));
        ComponentEditor.refreshBar(owner.getVarScrollPane().getVerticalScrollBar());
        
    }
    public static double getHeightRatio(){
        return heightRatio;
    }
    public JPanel getSelfPanel(){
        return selfPanel;
    }
    public String getName(){
        return nameText.getText();
    }
    public String getValue() {
        return valueText.getText();
    }
    public JTextArea getValueArea(){
        return valueText;
    }
    public void refreshDisplay(){
        int pos = owner.varPos(this);
        selfPanel.setLocation(0,pos*(int)(owner.getBounds().height*heightRatio));
    }
    
    public void actionPerformed(ActionEvent e){
        super.actionPerformed(e);
        if(e.getSource()==deleteButton){
            owner.delVar(this);
            owner.repaint();
        }
        if(e.getSource()==up){
            owner.varMoveUp(this);
        }
        if(e.getSource()==down){
            owner.varMoveDown(this);
        }
        
    }
}