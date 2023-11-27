package MyCalculator.Entity;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import MyCalculator.Lobby;
import MyCalculator.Tools.Calculator;
import MyCalculator.Tools.ComponentEditor;
import MyCalculator.Tools.HistoryRecorder;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CalculatorPanel extends JPanel implements ActionListener,ComponentListener{
    private static final Color buttonColor = new Color(233, 233, 233);

    public JTextField accuracyField;
    public JTextField diaNameField;
    public JTextField rightLimitField;
    public JTextField leftLimitField;
    
    public JButton solve;
    public JButton table;
    public JLabel expressionLabel;
    public JLabel resultLabel;
    public JLabel accuracyLabel;
    public JLabel varDiaNameLabel;
    public JLabel fromLabel;
    public JLabel toLabel;

    private DiagramDisplayer diagramDisplayer;

    public IndependentVar varExp = new IndependentVar("Expression",true);
    public IndependentVar varRes = new IndependentVar("Result",true);
    private boolean running = false;

    private List<Double> rootsX = new ArrayList<>();
    private List<Double> maxsX = new ArrayList<>();
    private List<Double> minsX = new ArrayList<>();
    private List<Double> extsX = new ArrayList<>();
    private List<Double> mostsX = new ArrayList<>();
    private List<Double> rootsY = new ArrayList<>();
    private List<Double> maxsY = new ArrayList<>();
    private List<Double> minsY = new ArrayList<>();
    private List<Double> extsY = new ArrayList<>();
    private List<Double> mostsY = new ArrayList<>();

    private List<List<Double>[]> outputss;
    private List<String> expStrings;

    private ExecutorService executorCal;
    private ExecutorService executorDia;
    private Future<String> futureCal;
    private Future<String> futureDia;

    public CalculatorPanel(){//
        outputss= new ArrayList<>();

        accuracyField = new JTextField();
        diaNameField = new JTextField();
        rightLimitField = new JTextField();
        leftLimitField = new JTextField();
        solve = new JButton("Solve");
        table = new JButton("Table");
        expressionLabel = new JLabel("Expression");
        resultLabel = new JLabel("Result");
        accuracyLabel = new JLabel("Accu");
        varDiaNameLabel = new JLabel("Vari");
        fromLabel = new JLabel("From");
        toLabel = new JLabel("To");
        varExp = new IndependentVar("Expression",true);
        varRes = new IndependentVar("Result",true);
        diagramDisplayer=new DiagramDisplayer();
        varExp.getValueArea().getDocument().addDocumentListener(new HistoryRecorder());

        solve.addActionListener(this);
        table.addActionListener(this);

        solve.setBackground(buttonColor);
        table.setBackground(buttonColor);

        setLayout(null);
        setBorder(BorderFactory.createLineBorder(Color.gray,1));
        setBackground(Color.lightGray);

        this.addComponentListener(this);
        refreshComponent();
    }
    private void refreshComponent() {
        for(JComponent component:Arrays.asList(expressionLabel,resultLabel,leftLimitField,accuracyLabel,varDiaNameLabel,fromLabel,toLabel)){
            component.setFont(Lobby.signFont);
        }
        for(JComponent component:Arrays.asList(solve,table)){
            component.setFont(Lobby.boldSignFont);
        }
        for(JComponent component:Arrays.asList(leftLimitField,rightLimitField,accuracyField,diaNameField)){
            component.setBorder(BorderFactory.createLineBorder(Color.gray,1));
            component.setFont(Lobby.smallFormatFont);
        }
        ComponentEditor.initializeComponentBody(varExp.selfPanel,this,0.05,0.17,0.5,0.43);
        ComponentEditor.initializeComponentBody(varRes.selfPanel,this,0.05,0.72,0.5,0.22);

        ComponentEditor.initializeComponentBody(solve,this,0.6,0.17,0.15,0.43);
        ComponentEditor.initializeComponentBody(table,this,0.8,0.17,0.15,0.43);

        ComponentEditor.initializeComponentBody(expressionLabel,this,0.05,0.08,0.3,0.1);
        ComponentEditor.initializeComponentBody(resultLabel,this,0.05,0.63,0.3,0.1);
        
        ComponentEditor.initializeComponentBody(fromLabel,this,0.605,0.69,0.05,0.12);
        ComponentEditor.initializeComponentBody(leftLimitField,this,0.65,0.69,0.1,0.12);
        ComponentEditor.initializeComponentBody(toLabel,this,0.605,0.82,0.05,0.12);
        ComponentEditor.initializeComponentBody(rightLimitField,this,0.65,0.82,0.1,0.12);

        ComponentEditor.initializeComponentBody(accuracyLabel,this,0.805,0.69,0.05,0.12);
        ComponentEditor.initializeComponentBody(accuracyField,this,0.85,0.69,0.1,0.12);
        ComponentEditor.initializeComponentBody(varDiaNameLabel,this,0.805,0.82,0.05,0.12);
        ComponentEditor.initializeComponentBody(diaNameField,this,0.85,0.82,0.1,0.12);
    }
    public void calWork(){
        running=true;
        long stratTime = System.currentTimeMillis();
        String result = Calculator.calString(varExp.getValueArea().getText());
        result = Calculator.cal(result);
        varRes.getValueArea().setText(result);
        long endTime = System.currentTimeMillis();
        long cost = endTime-stratTime;
        Lobby.getLogDisplayer().addLog(String.format("Calculation Completed! Cost %s.%ss", cost/1000,cost%1000));
        Lobby.getProgressBar().setProgress(100.0);
        running=false;
    }
    public void diaWork(){
        running=true;
        long stratTime = System.currentTimeMillis();
        String[] exps=varExp.getValueArea().getText().split(";");
        Double start,end;
        int n;
        String 
            varName = diaNameField.getText(),
            left = leftLimitField.getText(),
            right = rightLimitField.getText(),
            accu = accuracyField.getText();
        start = (left.length()==0)?-1.0:Double.parseDouble(left);
        end = (right.length()==0)?1.0:Double.parseDouble(right);
        n = (accu.length()==0)?1000:Integer.parseInt(accu);
        //System.err.println(String.format("(%s) %s %s %s %s", expString,start,end,n,varName));
        expStrings = new ArrayList<>();
        outputss = new ArrayList<>();
        //*****************************************************************************************
        for(String expString:exps){
            expStrings.add(expString);
            //System.err.println(expString);
            expString = Calculator.calString(expString);
            List<Double>[] outputs = Calculator.listGen(expString,varName,start,end,n);
            outputss.add(outputs);
        }
        diagramDisplayer.inputss = outputss;
        //*****************************************************************************************
        //*****************************************************************************************
        String output="";
        for(List<Double>[] outputs:outputss){//each expString and result
            //System.err.println(outputs[1].toString());
            Calculator.analysis(outputs[0], outputs[1], rootsX, maxsX, minsX, extsX, mostsX, rootsY, maxsY, minsY, extsY, mostsY);
            output+="\n>>>>>********************************<<<<<\nNew Data Analysis\n>>>>>********************************<<<<<\n";
            output+=String.format("*********Equation:\n%s=0\n", expStrings.get(outputss.indexOf(outputs)));
            output+=String.format("*********Roots:\n%s\n", rootsX.toString().replace(",", "\n"));
            output+="*********Extreme Points:\n";
            int len = extsX.size();
            for(int i=0;i<len;i++){
                output+=String.format("(%s,%s)\n", extsX.get(i),extsY.get(i));
            }
            output+=String.format("\n*********Max Point:\n(%s,%s)\n", mostsX.get(0),mostsY.get(0));
            output+=String.format("*********Min Point:\n(%s,%s)\n", mostsX.get(1),mostsY.get(1));
            output+="\n\n\n\n\n";
        }
        varRes.getValueArea().setText(output);
        //*****************************************************************************************
        long endTime = System.currentTimeMillis();
        long cost = endTime-stratTime;
        Lobby.getLogDisplayer().addLog(String.format("Diagram Generated! Cost %s.%ss", cost/1000,cost%1000));
        Lobby.getProgressBar().setProgress(100.0);
        diagramDisplayer.setVisible(true);
        running=false;
    }
    public void startCal(){
        executorCal = Executors.newSingleThreadExecutor();
        futureCal = null;
        try{
            CalCallable calTask = new CalCallable(this);
            futureCal=executorCal.submit(calTask);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            executorCal.shutdown();
        }
    }
    public void startTab(){
        executorDia = Executors.newSingleThreadExecutor();
        futureDia = null;
        try{
            DiaCallable diaTask = new DiaCallable(this);
            futureCal=executorDia.submit(diaTask);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            executorDia.shutdown();
        }
    }
    public void stopAll() {
        try{
            futureCal.cancel(true);
            futureDia.cancel(true);
        }catch(Exception e){
            //Lobby.getLogDisplayer().addLog("Interrupt");
        }
        running=false;
        Lobby.getProgressBar().setProgress(0.0);
    }
    public boolean isRunning(){
        return running;
    }
    public DiagramDisplayer getDiagramDisplayer(){
        return diagramDisplayer;
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==solve){//calculate
            if(!running)startCal();
        }
        if(e.getSource()==table){//table and diaplay scatter plots
            if(!running)startTab();
        }
    }
    @Override
    public void componentResized(ComponentEvent e) {
        refreshComponent();
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
class CalCallable implements Callable<String> {
    private CalculatorPanel cp;
    public CalCallable(CalculatorPanel cp){
        this.cp=cp;
    }
    @Override
    public String call() throws Exception {
        if(Thread.interrupted()){
            return "计算取消";
        }else{
            cp.calWork();
            return "计算成功";
        }    
    }
}
class DiaCallable implements Callable<String> {
    private CalculatorPanel cp;
    public DiaCallable(CalculatorPanel cp){
        this.cp=cp;
    }
    @Override
    public String call() throws Exception {
        if(Thread.interrupted()){
            return "计算取消";
        }else{
            cp.diaWork();
            return "计算成功";
        }    
    }
}