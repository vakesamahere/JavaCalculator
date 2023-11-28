package MyCalculator.Tools;
import javax.swing.JTextArea;
import javax.swing.event.*;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class HistoryRecorder implements CaretListener,FocusListener,KeyListener {
    private List<String> history;
    private List<String> future;
    private String textTe;
    private String textPreTe;//
    private String textSufTe;//
    private int modeTe;

    private int caretPosCu;
    private String textCu;
    private String textPreCu;//
    private String textSufCu;//
    private int modeCu;
    private int addPre;
    private int addSuf;
    private int modeOp;
    private int modeText;

    private boolean newFocus;
    private boolean sleep;
    private JTextArea textArea;
    private JTextArea vaTextArea;
    public HistoryRecorder(JTextArea textArea,JTextArea vaTextArea){
        this.textArea=textArea;
        this.textArea.addCaretListener(this);
        this.textArea.addFocusListener(this);
        this.textArea.addKeyListener(this);
        this.vaTextArea=vaTextArea;
        this.vaTextArea.addCaretListener(this);
        modeCu=0;
        history= Arrays.asList(textArea.getText());
        caretPosCu=textArea.getCaretPosition();
        textCu=textArea.getText();
        textPreCu=textCu.substring(0,caretPosCu);
        textSufCu=textCu.substring(caretPosCu);

        history=new ArrayList<>();
        future=new ArrayList<>();
        newFocus=false;
        sleep=false;
    }
    public void compare(JTextArea t){
        //原始前后文本
        textTe=textCu;
        textPreTe=textPreCu;
        textSufTe=textSufCu;
        //当前光标，前后文本
        caretPosCu=t.getCaretPosition();
        textCu=t.getText();
        textPreCu=textCu.substring(0,caretPosCu);
        textSufCu=textCu.substring(caretPosCu);
        //比较pre和suf
        String upPre = getPreUpdate(textPreCu, textPreTe);
        String upSuf = getSufUpdate(textSufCu, textSufTe);
        String up=(upPre+upSuf);
        System.err.println(String.format("update:%s", up));

        modeTe=modeCu;
        //判断输入类型，与上次相异则加入history
        if((addPre+addSuf==0)&&(upPre.equals(upSuf))){//仅仅移动光标
            System.err.println("only the caret moved.");
            return;
        }
        //必有一个是0
        if(addPre<0||addSuf<0){//delete
            modeOp=0;
            System.err.println("delete");
        }else{//insert
            modeOp=1;
            System.err.println("insert");
        }
        try {//数字类型
            double temp = Double.parseDouble(up);
            modeText=0;
            System.err.println("num");
        } catch (Exception e) {
            modeText=2;
            System.err.println("aplha");
        }
        modeCu = modeOp+modeText;
        if(modeCu!=modeTe){//history generation
            System.err.println("success");
            history.add(textTe);
            future.clear();
        }

        System.err.println(String.format("prefixUpdate:%s", upPre));
        System.err.println(String.format("suffixUpdate:%s", upSuf));
        System.err.println("\n\n\n");

    }
    public String getPreUpdate(String a,String b){
        int d = a.length()-b.length();
        if(d<=0){
            addPre=-1;
            if(d==0)addPre=0;
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
            if(d==0)addSuf=0;
            return b.substring(0,-d);
        }else{
            addSuf=1;
            return a.substring(0,d);
        }
    }
    @Override
    public void caretUpdate(CaretEvent e) {
        if(e.getSource()==textArea&&textArea.isFocusOwner()){
            if(newFocus||sleep){
                newFocus=false;
                return;
            }
            System.err.println("update occurred");
            compare(textArea);
            System.err.println(history);
        }else if(e.getSource()==vaTextArea&&vaTextArea.isFocusOwner()){
            compare(vaTextArea);
        }

    }
    @Override
    public void focusGained(FocusEvent e) {
        newFocus=true;
        sleep=false;
        System.err.println("focus gained");
        System.err.println(textArea.getText());
        if(history.size()>0&&history.get(history.size()-1).length()==0)history.remove(history.size()-1);
        if(history.size()>0&&history.get(history.size()-1).equals(textArea.getText()))history.remove(history.size()-1);
    }
    @Override
    public void focusLost(FocusEvent e) {
        sleep=true;
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.isControlDown()&&e.getKeyCode()==KeyEvent.VK_Z){
            sleep=true;
            if(!e.isShiftDown()){
                if(history.size()>0){
                    String backup = history.get(history.size()-1);
                    future.add(textArea.getText());
                    textArea.setText(backup);
                    history.remove(history.size()-1);
                }
            }else{
                if(future.size()>0){
                    String backup = future.get(future.size()-1);
                    history.add(textArea.getText());
                    textArea.setText(backup);
                    future.remove(future.size()-1);
                }
            }
            sleep=false;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }
    
}