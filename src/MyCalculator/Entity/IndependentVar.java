package MyCalculator.Entity;

import java.awt.event.*;

import MyCalculator.Lobby;
import MyCalculator.Tools.ComponentEditor;

public class IndependentVar extends Variable implements ComponentListener {
    private boolean useFormatFont = false;
    public IndependentVar(String str,Boolean isUseFormatFont) {
        super(str,true);
        selfPanel.addComponentListener(this);
        useFormatFont=isUseFormatFont;
        if(useFormatFont)valueText.setFont(Lobby.formatFont);
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
}
