package mycalculator.tools;
import javax.swing.JTextArea;
import javax.swing.event.*;

import mycalculator.entity.ExpressionEditor;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class HistoryRecorder implements CaretListener,FocusListener,KeyListener {
    private List<String> history;
    private List<String> future;
    private String textTe;
    private String textPreTe;
    private String textSufTe;
    private int modeTe;

    private int caretPosCu;
    private String textCu;
    private String textPreCu;
    private String textSufCu;
    private int modeCu;
    private int addPre;
    private int addSuf;
    private int modeOp;
    private int modeText;

    private boolean sleep;
    private boolean lost;
    private boolean addHistory;
    private JTextArea textArea;
    private JTextArea vaTextArea;
    private ExpressionEditor eed;
    public HistoryRecorder(JTextArea textArea,JTextArea vaTextArea,ExpressionEditor eed){
        this.textArea=textArea;
        this.textArea.addCaretListener(this);
        this.textArea.addFocusListener(this);
        this.textArea.addKeyListener(this);
        this.vaTextArea=vaTextArea;
        this.vaTextArea.addCaretListener(this);
        this.eed=eed;
        modeCu=0;
        history= Arrays.asList(textArea.getText());
        caretPosCu=textArea.getCaretPosition();
        textCu=textArea.getText();
        textPreCu=textCu.substring(0,caretPosCu);
        textSufCu=textCu.substring(caretPosCu);

        history=new ArrayList<>();
        future=new ArrayList<>();
        sleep=false;
        lost=false;
        addHistory=false;
    }
    public void sleep(){
        sleep=true;
    }
    public void wake(){
        sleep=false;
    }
    public void updateState(JTextArea t){
        //原始前后文本
        textTe=textCu;
        textPreTe=textPreCu;
        textSufTe=textSufCu;
        //当前光标，前后文本
        caretPosCu=t.getCaretPosition();
        textCu=t.getText();
        textPreCu=textCu.substring(0,caretPosCu);
        textSufCu=textCu.substring(caretPosCu);
    }
    public void undo(){
        sleep=true;
        if(history.size()>0){
            String backup = history.get(history.size()-1);
            future.add(textArea.getText());
            textArea.setText(backup);
            history.remove(history.size()-1);
            updateState(textArea);
            modeCu=0;
        }
        sleep=false;
    }
    public void redo(){
        sleep=true;
        if(future.size()>0){
            String backup = future.get(future.size()-1);
            history.add(textArea.getText());
            textArea.setText(backup);
            future.remove(future.size()-1);
            updateState(textArea);
            modeCu=0;
        }
        sleep=false;
    }
    public void compare(JTextArea t){
        updateState(t);
        //比较pre和suf
        String upPre = getPreUpdate(textPreCu, textPreTe);
        String upSuf = getSufUpdate(textSufCu, textSufTe);
        String up=(upPre+upSuf);
        System.err.println(String.format("update:%s", up));
        if(addHistory){
            modeCu=0;
        }else{
            modeTe=modeCu;
            //判断输入类型，与上次相异则加入history
            //仅仅移动光标
            if((addPre+addSuf==0)&&(upPre.equals(upSuf))){
                System.err.println("only the caret moved.");
                return;
            }
            //必有一个是0
            //delete
            if(addPre<0||addSuf<0){
                modeOp=0;
                System.err.println("delete");
            }else{//insert
                modeOp=1;
                System.err.println("insert");
            }
            try {//数字类型
                Double.parseDouble(up);
                modeText=0;
                System.err.println("num");
            } catch (Exception e) {
                modeText=2;
                System.err.println("aplha");
            }
            modeCu = modeOp+modeText;
        }

        future.clear();
        //history generation
        if(addHistory||modeCu!=modeTe){
            addHistory=false;
            System.err.println("success");
            history.add(textTe);
        }

        System.err.println(String.format("prefixUpdate:%s", upPre));
        System.err.println(String.format("suffixUpdate:%s", upSuf));
        System.err.println("\n\n\n");

    }
    public String getPreUpdate(String a,String b){
        int d = a.length()-b.length();
        if(d<=0){
            addPre=-1;
            if(d==0){
                addPre=0;
            }
            return b.substring(a.length());
        }else{ 
            addPre=1;
            return a.substring(b.length());
        }
    }
    public String getSufUpdate(String a,String b){
        int d=a.length()-b.length();
        if(d<=0){
            addSuf=-1;
            if(d==0){
                addSuf=0;
            }
            return b.substring(0,-d);
        }else{
            addSuf=1;
            return a.substring(0,d);
        }
    }
    @Override
    public void caretUpdate(CaretEvent e) {
        if((!vaTextArea.isFocusOwner())&&(!textArea.isFocusOwner())){
            lost=true;
        }
        if(e.getSource()==vaTextArea&&vaTextArea.isFocusOwner()){
            compare(vaTextArea);
        }else if(!sleep&&e.getSource()==textArea){
            System.err.println("update occurred");
            if (eed.getSoftKeyInput()||!textArea.isFocusOwner()) {
                System.err.println("soft keyboard input");
                addHistory=true;
                eed.setSoftKeyInput(false);
            }
            compare(textArea);
            System.err.println(history);
        }

    }
    @Override
    public void focusGained(FocusEvent e) {
        System.err.println("focus gained");
        System.err.println(textArea.getText());
        if(!lost){
            return;
        }
        if(history.size()>1&&history.get(history.size()-1).length()==0){
            history.remove(history.size()-1);
        }
        if(history.size()>0&&history.get(history.size()-1).equals(textArea.getText())){
            history.remove(history.size()-1);
        }
        lost=false;
    }
    @Override
    public void focusLost(FocusEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.isControlDown()&&e.getKeyCode()==KeyEvent.VK_Z){
            if(!e.isShiftDown()){
                undo();
            }else{
                redo();
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {}
    
}
