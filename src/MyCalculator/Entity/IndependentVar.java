package mycalculator.entity;

import java.awt.event.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import mycalculator.Help;
import mycalculator.Lobby;
import mycalculator.tools.ComponentEditor;


public class IndependentVar extends Variable {
    private boolean useFormatFont = false;
    private String selected="";
    private ComponentAdapter ca = new ComponentAdapter(){
        @Override
        public void componentResized(ComponentEvent e) {
            refreshComponents();
        }
    };
    public IndependentVar(String str,Boolean isUseFormatFont) {
        super(str,true);
        selfPanel.addComponentListener(ca);
        useFormatFont=isUseFormatFont;
        if(useFormatFont){
            valueText.setFont(Lobby.formatFont);
        }
        valueText.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                if(e.getMark()==e.getDot()){
                    selected="";
                    return;
                }
                String temp = valueText.getSelectedText();
                if(temp.length()>0){
                    selected=temp;
                }
            }
        });
        Help.varModify.add(modifyButton);
    }
    public IndependentVar(String str){
        super(str,true);
        selfPanel.addComponentListener(ca);
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
}
