package MyCalculator.Entity;

import MyCalculator.Lobby;
import MyCalculator.Tools.ComponentEditor;

import java.awt.event.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;


public class IndependentVar extends Variable implements ComponentListener,CaretListener {
    private boolean useFormatFont = false;
    private String selected="";
    public IndependentVar(String str,Boolean isUseFormatFont) {
        super(str,true);
        selfPanel.addComponentListener(this);
        useFormatFont=isUseFormatFont;
        if(useFormatFont)valueText.setFont(Lobby.formatFont);
        valueText.addCaretListener(this);
    }
    public IndependentVar(String str){
        super(str,true);
        selfPanel.addComponentListener(this);
    }
    public void refreshComponents() {
        valueText.setFont(useFormatFont?Lobby.formatFont:Lobby.smallFormatFont);
        ComponentEditor.initializeComponentBody(scrollPane,selfPanel,0,0,0.95,1);
        ComponentEditor.initializeComponentBody(modifyButton,selfPanel,0.95,0,0.05,1);
    }
    public String getSelected(){
        String temp = selected;
        selected="";
        return temp;
    }
    @Override
    public void componentResized(ComponentEvent e) {
        if(e.getSource()==selfPanel)refreshComponents();
    }
    @Override
    public void componentMoved(ComponentEvent e) {
    }
    @Override
    public void componentShown(ComponentEvent e) {
    }
    @Override
    public void componentHidden(ComponentEvent e) {
    }
    @Override
    public void caretUpdate(CaretEvent e) {
        if(e.getMark()==e.getDot()){
            selected="";
            return;
        }
        String temp = valueText.getSelectedText();
        if(temp.length()>0)selected=temp;
    }
}
