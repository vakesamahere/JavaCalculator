package mycalculator.entity;

import javax.swing.*;

import mycalculator.Help;
import mycalculator.Lobby;
import mycalculator.tools.Calculator;
import mycalculator.tools.ComponentEditor;

import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VariableRigisterLabel extends JPanel {
    private static final Color buttonColor = new Color(253, 253, 253);
    private JLabel nameSign = new JLabel("Var");
    private JLabel valueSign = new JLabel("Value");
    private JButton nameDisplayer = new JButton();
    private JButton register = new JButton("New Var");
    private JPanel variablesPanel = new JPanel(null);
    private JScrollPane varScrollPane = new JScrollPane();
    private List<RegistedVar> varList = new ArrayList<>();
    private List<RegistedVar> perList = new ArrayList<>();

    public VariableRigisterLabel(String name,JComponent father,double posX,double posY,double sizeX,double sizeY){
        setLayout(null);
        addComponentListener(new ComponentAdapter(){
            @Override
            public void componentResized(ComponentEvent e) {
                refreshComponent();
            }
        });
        nameDisplayer.setText(name);
        ComponentEditor.initializeComponentBody(this,father,posX,posY,sizeX,sizeY);
        setBorder(BorderFactory.createLineBorder(Color.gray));
        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                createNewVariable();
            }
        });
        nameDisplayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                clearList();
            }
        });
        register.setBackground(buttonColor);
        nameDisplayer.setBackground(buttonColor);

        varScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        varScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        nameDisplayer.setFocusable(false);
        register.setFocusable(false);
        refreshComponent();
        getHelp();
    }
    private void getHelp(){
        Help.clearVars=nameDisplayer;
        Help.newVars=register;
    }
    public JPanel getVarPanel(){
        return variablesPanel;
    }
    public JScrollPane getVarScrollPane(){
        return varScrollPane;
    }
    public List<RegistedVar> getVarList(){
        return varList;
    }
    public List<RegistedVar> getPerList(){
        return perList;
    }
    public int getVarCount(){
        return variablesPanel.getComponentCount();
    }
    public int varPos(RegistedVar va){
        return varList.indexOf(va);
    }
    public void delVar(RegistedVar va){
        variablesPanel.remove(va.getSelfPanel());
        varList.remove(va);
        refreshVarDisplay();
    }
    public void refreshVarDisplay(){
        for(RegistedVar va:varList){
            va.refreshDisplay();
        }
    }
    public void refreshComponent(){
        ComponentEditor.initializeComponentBody(varScrollPane, this,0.1, 0.25,0.8,0.6);
        ComponentEditor.initializeComponentBody(variablesPanel,varScrollPane,0,0,1,1);
        
        varScrollPane.setViewportView(variablesPanel);
        variablesPanel.setPreferredSize(new Dimension(variablesPanel.getSize()));
        varScrollPane.getVerticalScrollBar().setUnitIncrement((int)(RegistedVar.getHeightRatio()*getBounds().height));
        
        ComponentEditor.initializeComponentBody(nameSign, this, 0.11,0.15,0.3,0.1);
        ComponentEditor.initializeComponentBody(valueSign, this, 0.25,0.15,0.3,0.1);
        ComponentEditor.initializeComponentBody(nameDisplayer, this, 0.1,0.02,0.3,0.1);
        ComponentEditor.initializeComponentBody(register, this, 0.45,0.02,0.45,0.1);
        
        for(JComponent component:Arrays.asList(nameSign,valueSign,nameDisplayer,register)){
            component.setFont(Lobby.signFont);
        }
        for(RegistedVar rv:varList){
            rv.refreshComponent();
        }
    }
    public void createNewVariable(){
        varList.add(new RegistedVar(this));
        varList.get(varList.size()-1).refreshComponent();
        //this.repaint();
    }
    public void clearList(){
        for(RegistedVar va:varList){
            variablesPanel.remove(va.getSelfPanel());
        }
        variablesPanel.setPreferredSize(new Dimension(varScrollPane.getSize()));
        ComponentEditor.refreshBar(varScrollPane.getVerticalScrollBar());

        varList = new ArrayList<>();
        for(RegistedVar va:perList){
            variablesPanel.add(va.getSelfPanel());
            varList.add(va);
        }
        revalidate();
        repaint();
    }
    public String replaceVars(String expString) {
        for(RegistedVar va:varList){
            expString=Calculator.replaceVar(expString, va);
        }
        return expString;
    }
    public void varMoveUp(RegistedVar va) {
        int pos = varList.indexOf(va);
        if(pos==0){
            return;
        }
        varList.add(pos-1, va);
        varList.remove(pos+1);
        refreshVarDisplay();
    }
    public void varMoveDown(RegistedVar va) {
        int pos = varList.indexOf(va);
        if(pos==varList.size()-1){
            return;
        }
        varList.add(pos+2, va);
        varList.remove(pos);
        refreshVarDisplay();
    }
}
