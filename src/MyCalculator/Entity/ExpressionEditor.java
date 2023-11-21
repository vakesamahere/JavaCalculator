package MyCalculator.Entity;

import java.awt.event.*;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class ExpressionEditor extends ExpressionCollector implements KeyListener {
    private Keyboard keyboard;
    private CaretListener caretListener;
    private int dot=0;
    private boolean shifting=false;
    public ExpressionEditor(Variable va) {
        super(va);
        setAlwaysOnTop(true);
        keyboard = new Keyboard(this);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                keyboard.setSize(getSize());
                keyboard.setVisible(true);
            }
            public void componentHidden(ComponentEvent e) {
                keyboard.setVisible(false);
            }
            
        });
        caretListener = new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                dot = e.getDot();
            }
        };
        textArea.addCaretListener(caretListener);
        textArea.addKeyListener(this);
    }
    public void setDot(){
        textArea.setCaretPosition(dot);
    }
    public int getDot(){
        return dot;
    }
    public void rightTab() {
        String str=classfication(textArea.getText());
        int pos = str.indexOf("1",dot);
        if(pos==-1){
            if(dot==str.length())return;
            textArea.setCaretPosition(++dot);
            return;
        }
        pos = str.indexOf("0",pos);
        if(pos==-1){
            if(dot==str.length())return;
            textArea.setCaretPosition(++dot);
            return;
        }
        int end = str.indexOf("1",pos);
        if(end==-1)end=str.length();
        //textArea.setCaretPosition(pos);
        textArea.setSelectionStart(pos);
        textArea.setSelectionEnd(end);
        
    }
    public void leftTab() {
        String str=classfication(textArea.getText());
        int pos = str.lastIndexOf("1",dot-1);
        if(pos==-1){
            if(dot==0)return;
            textArea.setCaretPosition(--dot);
            return;
        }
        pos = str.lastIndexOf("0",pos);
        if(pos==-1){
            if(dot==0)return;
            textArea.setCaretPosition(--dot);
            return;
        }
        int end = str.lastIndexOf("1",pos);
        textArea.setSelectionStart(end+1);
        textArea.setSelectionEnd(pos+1);
    }
    public String classfication(String str){
        return str
            .replaceAll("[0123456789.\n\t ]", "0")
            .replaceAll("([^A-za-z0-9])[e]([^A-za-z0-9])", String.format("%s%s%s","$1","0","$2"))
            .replaceAll("π", "0")
            .replaceAll("([^0)%])[-]", String.format("%s%s", "$1","0"))
            .replaceAll("[^0]", "1");
        

        }
    @Override
    public void focusGained(FocusEvent e) {
        super.focusGained(e);
        textArea.setCaretPosition(dot);
    }
    @Override
    public void focusLost(FocusEvent e) {
        textArea.getCaret().setVisible(true);
        if(!keyboard.isFocused())super.focusLost(e);
    }
    @Override
    public void keyTyped(KeyEvent e) {
        boolean match =keyboard.keyTyped(e.getKeyChar());
        if(match)e.consume();
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_SHIFT){
            shifting=true;
        }
        switch (e.getKeyChar()) {
            case KeyEvent.VK_TAB:{
                if(shifting){
                    leftTab();
                }else{
                    rightTab();
                }
                break;
            }
        
            default:{
                return;
            }
        }
        e.consume();
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_SHIFT){
            shifting=false;
        }
        
    }

    
}
