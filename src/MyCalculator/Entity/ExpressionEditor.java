package mycalculator.entity;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.Document;

import mycalculator.Lobby;
import mycalculator.tools.DocHistoryRecorder;

import java.awt.*;
import java.awt.event.*;

public class ExpressionEditor extends JPanel {
    private KeyboardPanel keyboard;
    private int dot=0;
    private boolean hotKeyLock=false;
    private boolean ctrlPressed=false;
    private JTextArea target;
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private Font font;
    private DocHistoryRecorder dhr;
    private JButton selectedButton;
    public ExpressionEditor(){
        setLayout(new GridLayout(1,1));
        textArea= new JTextArea();
        refreshFont();
        textArea.setLineWrap(true);
        scrollPane = new JScrollPane(textArea);
        add(scrollPane);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        textArea.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                if(!textArea.isFocusOwner())return;
                dot = e.getDot();
                System.err.println(target.getText());
                System.err.println(textArea.getText());
                setTargetCaretPos(dot);
                target.getCaret().setVisible(true);
            }
        });
        textArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(hotKeyLock||ctrlPressed||e.getKeyChar()=='('||e.getKeyChar()=='['){
                    boolean match = keyboard.keyTyped(e.getKeyChar());
                    if(match){
                        e.consume();
                    }
                }
                ctrlPressed=false;
            }
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_CONTROL){
                    ctrlPressed=!ctrlPressed;
                }
                if(e.isControlDown()&&e.getKeyCode()==KeyEvent.VK_BACK_QUOTE){
                    hotKeyLock=!hotKeyLock;
                    ctrlPressed=false;
                }

                if(e.isControlDown()&&e.getKeyCode()==KeyEvent.VK_Z){
                    if(!e.isShiftDown()){
                        getDocRecorder().undo();
                    }else{
                        getDocRecorder().redo();
                    }
                }
                switch (e.getKeyChar()) {
                    case KeyEvent.VK_TAB:{
                        if(e.isShiftDown()){
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
            }
        });
    }
    public void refreshFont() {
        double size=15*Math.min(getToolkit().getScreenSize().height,getToolkit().getScreenSize().width)/540;
        font = new Font("Microsoft Yahei", Font.PLAIN, (int)size);
        textArea.setFont(font);
    }
    public JTextArea getTextArea(){
        return textArea;
    }
    public JTextArea getTarget(){
        return target;
    }
    public void setTargetCaretPos(int pos){
        target.setCaretPosition(pos);
    }
    public void setDot(){
        textArea.setCaretPosition(dot);
    }
    public int getDot(){
        return dot;
    }
    public void setSelectedButton(JButton button){
        if(selectedButton!=null){
            selectedButton.setEnabled(true);
            selectedButton.setBackground(Variable.buttonColor);
        }
        selectedButton=button;
        button.setEnabled(false);
        button.setBackground(Color.lightGray);
    }
    public void setTarget(JTextArea target){
        this.target=target;
    }
    public void setKeyboard(KeyboardPanel kp){
        keyboard=kp;
    }
    public DocHistoryRecorder getDocRecorder(){
        return dhr;
    }
    public void selectDhr(DocHistoryRecorder d){
        dhr = d;
    }
    public void rightTab() {
        String str=classfication(textArea.getText());
        int pos = str.indexOf("1",dot);
        if(pos==-1){
            if(dot==str.length()){
                return;
            }
            textArea.setCaretPosition(++dot);
            return;
        }
        pos = str.indexOf("0",pos);
        if(pos==-1){
            if(dot==str.length()){
                return;
            }
            textArea.setCaretPosition(++dot);
            return;
        }
        int end = str.indexOf("1",pos);
        if(end==-1){
            end=str.length();
        }
        textArea.setSelectionStart(pos);
        textArea.setSelectionEnd(end);
    }
    public void leftTab() {
        String str=classfication(textArea.getText());
        int pos = str.lastIndexOf("1",dot-1);
        if(pos==-1){
            if(dot==0){
                return;
            }
            textArea.setCaretPosition(--dot);
            return;
        }
        pos = str.lastIndexOf("0",pos);
        if(pos==-1){
            if(dot==0){
                return;
            }
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
    public void setDoc(Document doc){
        textArea.setDocument(doc);
    }
}
