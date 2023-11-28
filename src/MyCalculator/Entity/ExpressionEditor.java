package MyCalculator.Entity;

import MyCalculator.Lobby;
import MyCalculator.Tools.HistoryRecorder;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.*;

public class ExpressionEditor extends JDialog implements DocumentListener,FocusListener,KeyListener,ComponentListener  {
    private static final double formSizeRatio = 0.4;
    private Keyboard keyboard;
    private CaretListener caretListener;
    private int dot=0;
    private boolean shifting=false;
    private boolean hotKeyLock=false;
    private boolean ctrlPressed=false;
    private HistoryRecorder hr;
    private boolean softKeyboardInput =false;//HistoryRecorder调用 用于判断判断软键盘输入

    private JTextArea textArea;
    private JScrollPane scrollPane;
    private Variable target;
    private Font font;
    public ExpressionEditor(Variable va){
        target = va;
        calSrceenSize(formSizeRatio);
        this.setLocationRelativeTo(null);
        textArea= new JTextArea();
        refreshFont();
        textArea.setLineWrap(true);
        scrollPane = new JScrollPane(textArea);
        this.add(scrollPane);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        Document doc = textArea.getDocument();
        doc.addDocumentListener(this);
        textArea.addFocusListener(this);
        Lobby.useKeyBoard(this);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e){
                refreshFont();
            }

        });
        setAlwaysOnTop(true);
        this.addComponentListener(this);
        caretListener = new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                dot = e.getDot();
            }
        };
        textArea.addCaretListener(caretListener);
        textArea.addKeyListener(this);
        
        hr=new HistoryRecorder(textArea,va.getValueArea(),this);
    }
    public void refreshFont() {
        double size=50*Math.min(getHeight(),getWidth())/540;
        font = new Font("Microsoft Yahei", Font.PLAIN, (int)size);
        textArea.setFont(font);
    }
    public void calSrceenSize(double ratio){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)(ratio*screenSize.getWidth());
        int height = (int)(ratio*screenSize.getHeight());
        this.setSize(width,height);
    }
    public void setKeyboard(Keyboard k){
        keyboard = k;
    }
    public JTextArea getTextArea(){
        return textArea;
    }
    public void setDot(){
        textArea.setCaretPosition(dot);
    }
    public int getDot(){
        return dot;
    }
    public HistoryRecorder getHr(){
        return hr;
    }
    public void setSoftKeyInput(boolean value){
        softKeyboardInput=value;
    }
    public boolean getSoftKeyInput(){
        return softKeyboardInput;
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
    public void insertUpdate(DocumentEvent e) {output();}
    @Override
    public void removeUpdate(DocumentEvent e) {output();}
    @Override
    public void changedUpdate(DocumentEvent e) {output();}
    public void output(){
        target.getValueArea().setText(textArea.getText());
    }
    @Override
    public void focusGained(FocusEvent e) {
        Lobby.useKeyBoard(this);
        textArea.setText(target.getValueArea().getText());
        textArea.setForeground(Color.BLACK);
        this.setTitle("Input the value of "+target.getName());
        textArea.setCaretPosition(dot);
    }
    @Override
    public void focusLost(FocusEvent e) {
        textArea.getCaret().setVisible(true);
        if(keyboard.isFocused())return;
        textArea.setForeground(Color.WHITE);
    }
    @Override
    public void keyTyped(KeyEvent e) {
        if(hotKeyLock||ctrlPressed||e.getKeyChar()=='('||e.getKeyChar()=='['){
            boolean match = keyboard.keyTyped(e.getKeyChar());
            if(match){
                e.consume();
                softKeyboardInput=true;
            }
        }
        ctrlPressed=false;
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_SHIFT){
            shifting=true;
        }
        if(e.getKeyCode()==KeyEvent.VK_CONTROL){
            ctrlPressed=!ctrlPressed;
        }
        if(e.isControlDown()&&e.getKeyCode()==192){//`~
            hotKeyLock=!hotKeyLock;
            ctrlPressed=false;
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
    @Override
    public void componentShown(ComponentEvent e) {
        Lobby.useKeyBoard(this);
        keyboard.setVisible(true);
    }
    public void componentHidden(ComponentEvent e) {
        //keyboard.setVisible(false);
    }
    @Override
    public void componentResized(ComponentEvent e) {
    }
    @Override
    public void componentMoved(ComponentEvent e) {
    }
}
