package MyCalculator.Entity;

import java.awt.event.*;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class ExpressionEditor extends ExpressionCollector implements KeyListener {
    private Keyboard keyboard;
    private CaretListener caretListener;
    private int dot=0;
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
        
    }
    @Override
    public void keyReleased(KeyEvent e) {
        
    }

    
}
