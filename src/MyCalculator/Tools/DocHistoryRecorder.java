package mycalculator.tools;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.event.*;
import javax.swing.text.Document;

import mycalculator.Lobby;


public class DocHistoryRecorder implements DocumentListener,CaretListener{
    public static final byte INSERT = 0;
    public static final byte DELETE = 1;
    public static final byte ALPHA = 2;
    public static final byte NUMBER = 4;
    public static final byte EDO = 6;
    public static final byte ORI = -1;
    public static final byte INA = INSERT+ALPHA;
    public static final byte INN = INSERT+NUMBER;
    public static final byte DEA = DELETE+ALPHA;
    public static final byte DEN = DELETE+NUMBER;

    private byte modeO = ORI;
    private byte modeN;

    private Document doc;

    private String oriText="";
    private int oriDot=0;
    private JTextArea textArea;

    private int caretDot = 0;

    
    private List<String> history = new ArrayList<>();
    private List<String> future = new ArrayList<>();

    private List<Integer> historyC = new ArrayList<>();
    private List<Integer> futureC = new ArrayList<>();

    private boolean sleep = false;
    private boolean softKey = false;
    private boolean outerInput = false;

    public DocHistoryRecorder(JTextArea textArea){
        this.doc=textArea.getDocument();
        this.textArea=textArea;
        doc.addDocumentListener(this);
        textArea.addCaretListener(this);
    }
    public void insert(String text){
        System.err.println("insert");
        if (sleep) {
            System.err.println("sleep");
            return;
        }
        future.clear();
        futureC.clear();
        modeN=INN;
        for (char c : text.toCharArray()) {
            if(!Calculator.isNum(c)){
                modeN=INA;
                break;
            }
        }
        if(modeN==modeO){
            modeO=modeN;
            return;
        }
        modeO=modeN;
        addHistory();
    }
    public void remove(String text){
        System.err.println("remove");
        if (sleep) {
            System.err.println("sleep");
            return;
        }
        future.clear();
        futureC.clear();
        modeN=DEN;
        for (char c : text.toCharArray()) {
            if(!Calculator.isNum(c)){
                modeN=DEA;
                break;
            }
        }
        if(modeN==modeO){
            modeO=modeN;
            return;
        }
        modeO=modeN;
        addHistory();

    }
    public void addHistory(byte mode){
        System.err.println("addOpHistory");
        modeN = mode;
        addHistory();
        oriText=textArea.getText();
        oriDot = caretDot;
    }
    public void addHistory(){
        System.err.println("addHistory");
        history.add(oriText);
        historyC.add(oriDot);
        System.err.println(history);
        System.err.println(historyC);
    }
    public void undo(){
        sleep();
        int last = history.size()-1;
        if(last<0){
            wake();
            return;
        }
        //
        modeO=ORI;
        System.err.println(last);
        future.add(textArea.getText());
        futureC.add(caretDot);
        textArea.setText(history.get(last));
        textArea.setCaretPosition(historyC.get(last));
        history.remove(last);
        historyC.remove(last);
        oriText=textArea.getText();
        oriDot = caretDot;
        wake();
    }
    public void redo(){
        sleep();
        int last = future.size()-1;
        if(last<0){
            wake();
            return;
        }
        modeO=ORI;
        //future传入历史记录
        history.add(textArea.getText());
        historyC.add(caretDot);
        textArea.setText(future.get(last));
        textArea.setCaretPosition(futureC.get(last));
        future.remove(last);
        futureC.remove(last);
        oriText=textArea.getText();
        oriDot = caretDot;
        
        wake();
    }
    public void sleep(){
        sleep=true;
    }
    public void wake(){
        sleep=false;
    }
    public void softInput(){
        softKey=true;
    }
    public void softInputEnd(){
        softKey=false;
    }
    public void outerInput(){
        outerInput=true;
    }
    @Override
    public void insertUpdate(DocumentEvent e) {
        try {
            if(softKey){
                return;
            }
            insert(e.getDocument().getText(e.getOffset(), e.getLength()));
            oriText=textArea.getText();
            oriDot = caretDot;
        } catch (Exception ee) {
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        try {
            if(softKey){
                return;
            }
            remove(oriText.substring(e.getOffset(), e.getOffset()+e.getLength()));
            oriText=textArea.getText();
            oriDot = caretDot;
        } catch (Exception ee) {
            System.err.println(ee);
        }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        System.err.println("change");
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        if(Lobby.getExpressionEditor().getTarget()==textArea&&textArea.getCaretPosition()!=Lobby.getExpressionEditor().getTextArea().getCaretPosition()){
            if(!Lobby.getExpressionEditor().getTarget().isFocusOwner()){
                textArea.setCaretPosition(Lobby.getExpressionEditor().getTextArea().getCaretPosition());
                textArea.getCaret().setVisible(true);
            }else{
                Lobby.getExpressionEditor().getTextArea().setCaretPosition(textArea.getCaretPosition());
            }
        }
        caretDot=e.getDot();
    }
}
