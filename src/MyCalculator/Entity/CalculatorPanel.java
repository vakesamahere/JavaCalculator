package mycalculator.entity;
import javax.swing.*;

import mycalculator.Help;
import mycalculator.Lobby;
import mycalculator.tools.Calculator;
import mycalculator.tools.ComponentEditor;

import java.awt.*;
import java.awt.event.*;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CalculatorPanel extends JPanel{
    private static final Color buttonColor = new Color(233, 233, 233);

    private JTextField accuracyField;
    private JTextField diaNameField;
    private JTextField rightLimitField;
    private JTextField leftLimitField;
    
    private JButton solve;
    private JButton table;
    private JLabel expressionLabel;
    private JLabel resultLabel;
    private JLabel accuracyLabel;
    private JLabel varDiaNameLabel;
    private JLabel fromLabel;
    private JLabel toLabel;

    private DiagramDisplayer diagramDisplayer;

    private IndependentVar varExp = new IndependentVar("Expression",true);
    private IndependentVar varRes = new IndependentVar("Result",true);
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
    /*构造函数 */
    public CalculatorPanel(){//
        outputss= new ArrayList<>();
        setLayout(null);
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
        /*默认值 */
        varRes.getValueArea().setText("0");

        solve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                if(!running){
                    startCal();
                }
            }
        });
        table.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                if(!running){
                    startTab();
                }
            }
        });
        solve.setFocusable(false);
        table.setFocusable(false);

        solve.setBackground(buttonColor);
        table.setBackground(buttonColor);
        setBorder(BorderFactory.createLineBorder(Color.gray,1));
        setBackground(Color.lightGray);

        addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent e) {
                refreshComponent();
            }
        });
        refreshComponent();
        getHelp();
    }
    private void getHelp(){
        Help.expArea=varExp.getValueArea();
        Help.resArea=varRes.getValueArea();
        Help.solveBut=solve;
        Help.tableBut=table;
        Help.fromField=leftLimitField;
        Help.toField=rightLimitField;
        Help.accuField=accuracyField;
        Help.variField=diaNameField;
    }
    private void refreshComponent() {
        for(JComponent component:Arrays.asList(expressionLabel,resultLabel)){
            component.setFont(Lobby.signFont);
        }
        for(JComponent component:Arrays.asList(solve,table)){
            component.setFont(Lobby.boldSignFont);
        }
        for(JComponent component:Arrays.asList(leftLimitField,rightLimitField,accuracyField,diaNameField)){
            component.setBorder(BorderFactory.createLineBorder(Color.gray,1));
            component.setFont(Lobby.smallFormatFont);
        }
        for(JComponent component:Arrays.asList(accuracyLabel,varDiaNameLabel,fromLabel,toLabel)){
            component.setFont(Lobby.smallFormatFont);
        }
        ComponentEditor.initializeComponentBody(varExp.selfPanel,this,0.05,0.17,0.5,0.43);
        ComponentEditor.initializeComponentBody(varRes.selfPanel,this,0.05,0.72,0.5,0.22);

        ComponentEditor.initializeComponentBody(solve,this,0.6,0.17,0.15,0.43);
        ComponentEditor.initializeComponentBody(table,this,0.8,0.17,0.15,0.43);

        ComponentEditor.initializeComponentBody(expressionLabel,this,0.05,0.08,0.3,0.1);
        ComponentEditor.initializeComponentBody(resultLabel,this,0.05,0.63,0.3,0.1);
        
        ComponentEditor.initializeComponentBody(fromLabel,this,0.6,0.69,0.05,0.12);
        ComponentEditor.initializeComponentBody(leftLimitField,this,0.65,0.69,0.1,0.12);
        ComponentEditor.initializeComponentBody(toLabel,this,0.6,0.82,0.05,0.12);
        ComponentEditor.initializeComponentBody(rightLimitField,this,0.65,0.82,0.1,0.12);

        ComponentEditor.initializeComponentBody(accuracyLabel,this,0.8,0.69,0.05,0.12);
        ComponentEditor.initializeComponentBody(accuracyField,this,0.85,0.69,0.1,0.12);
        ComponentEditor.initializeComponentBody(varDiaNameLabel,this,0.8,0.82,0.05,0.12);
        ComponentEditor.initializeComponentBody(diaNameField,this,0.85,0.82,0.1,0.12);
         //*/
    }
    /*前台运算调度方法 */
    public void calWork(){
        running=true;
        long stratTime = System.currentTimeMillis();
        String result = varExp.getSelected();
        if(result.length()==0){
            result = varExp.getValueArea().getText();
        }
        result = Calculator.calString(result);
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
        String input = varExp.getSelected();
        if(input.length()==0){
            input = varExp.getValueArea().getText();
        }
        String[] exps=input.split(";");
        if(exps.length==1&&exps[0].length()==0){
            running=false;
            return;
        }
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
        if(varName.length()==0){
            varName="x";
        }
        expStrings = new ArrayList<>();
        outputss = new ArrayList<>();
        //*****************************************************************************************
        for(String expString:exps){
            expStrings.add(expString);
            expString = Calculator.calString(expString);
            List<Double>[] outputs = Calculator.listGen(expString,varName,start,end,n);
            outputss.add(outputs);
        }
        diagramDisplayer.setInputss(outputss);
        //*****************************************************************************************
        //*****************************************************************************************
        String output="";
        //each expString and result
        for(List<Double>[] outputs:outputss){
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
        }
        running=false;
        Lobby.getProgressBar().setProgress(0.0);
    }
    public boolean isRunning(){
        return running;
    }
    /*get & set */
    public DiagramDisplayer getDiagramDisplayer(){
        return diagramDisplayer;
    }
    public IndependentVar getVarRes(){
        return varRes;
    }
    public IndependentVar getVarExp(){
        return varExp;
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