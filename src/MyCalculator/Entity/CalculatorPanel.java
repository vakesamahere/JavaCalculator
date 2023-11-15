package MyCalculator.Entity;

import javax.swing.*;

import MyCalculator.Lobby;
import MyCalculator.Tools.Calculator;
import MyCalculator.Tools.ComponentEditor;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class CalculatorPanel extends JPanel implements ActionListener,ComponentListener{
    public JTextField diaRangeField;
    public JTextField diaNameField;
    public JTextField varNeedToSolve;
    public JCheckBox solveEquation;
    public JButton solve;
    public JButton diagram;
    public JLabel expressionLabel;
    public JLabel resultLabel;
    public JLabel equationLabel;
    public JLabel diaRangeNameLabel;
    public JLabel varDiaNameLabel;

    private DiagramDisplayer diagramDisplayer;

    public IndependentVar varExp = new IndependentVar("Expression",true);
    public IndependentVar varRes = new IndependentVar("Result",true);
    public Thread calThread;
    public Thread diaThread;
    private boolean running = false;

    public CalculatorPanel(){//
        diaRangeField = new JTextField();
        diaNameField = new JTextField();
        varNeedToSolve = new JTextField();
        solveEquation = new JCheckBox();
        solve = new JButton("Solve");
        diagram = new JButton("Diagram");
        expressionLabel = new JLabel("Expression");
        resultLabel = new JLabel("Result");
        equationLabel = new JLabel("Equation");
        diaRangeNameLabel = new JLabel("Range");
        varDiaNameLabel = new JLabel("VarDiaName");
        varExp = new IndependentVar("Expression",true);
        varRes = new IndependentVar("Result",true);
        diagramDisplayer=new DiagramDisplayer();
        
        solve.addActionListener(this);
        diagram.addActionListener(this);

        setLayout(null);
        setBorder(BorderFactory.createLineBorder(Color.gray,1));
        setBackground(Color.lightGray);

        this.addComponentListener(this);
        refreshComponent();
    }
    private void refreshComponent() {
        for(JComponent component:Arrays.asList(solve,diagram,expressionLabel,resultLabel,equationLabel,diaRangeNameLabel,varDiaNameLabel)){
            component.setFont(Lobby.signFont);
        }
        for(JComponent component:Arrays.asList(varNeedToSolve,diaRangeField,diaNameField)){
            component.setBorder(BorderFactory.createLineBorder(Color.gray,1));
            component.setFont(Lobby.smallFormatFont);
        }
        ComponentEditor.initializeComponentBody(varExp.selfPanel,this,0.05,0.17,0.5,0.43);
        ComponentEditor.initializeComponentBody(varRes.selfPanel,this,0.05,0.72,0.5,0.22);

        ComponentEditor.initializeComponentBody(solve,this,0.6,0.17,0.15,0.43);
        ComponentEditor.initializeComponentBody(diagram,this,0.8,0.17,0.15,0.43);

        ComponentEditor.initializeComponentBody(expressionLabel,this,0.05,0.08,0.3,0.1);
        ComponentEditor.initializeComponentBody(resultLabel,this,0.05,0.63,0.3,0.1);

        ComponentEditor.initializeComponentBody(solveEquation,this,0.6,0.69,0.02,0.1);
        solveEquation.setContentAreaFilled(false);
        ComponentEditor.initializeComponentBody(equationLabel,this,0.64,0.69,0.1,0.12);
        ComponentEditor.initializeComponentBody(varNeedToSolve,this,0.6,0.82,0.15,0.12);

        ComponentEditor.initializeComponentBody(diaRangeNameLabel,this,0.8,0.69,0.05,0.12);
        ComponentEditor.initializeComponentBody(diaRangeField,this,0.85,0.69,0.1,0.12);
        ComponentEditor.initializeComponentBody(varDiaNameLabel,this,0.8,0.82,0.05,0.12);
        ComponentEditor.initializeComponentBody(diaNameField,this,0.85,0.82,0.1,0.12);
    }
    public void startCal(){
        calThread=new Thread(new Runnable() {
            @Override
            public void run() {
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
          });
        calThread.start();
    }
    public void startDia(){
        diaThread=new Thread(new Runnable() {
            @Override
            public void run() {
                running=true;
                long stratTime = System.currentTimeMillis();
                String[] range=diaRangeField.getText().split(",");
                String[] exps=varExp.getValueArea().getText().split(";");
                Double start=0.0;
                Double end=1.0;
                int n =10;
                String varName=diaNameField.getText();
                try{
                    start = Double.parseDouble(range[0]);
                    end = Double.parseDouble(range[1]);
                    n = Integer.parseInt(range[2]);
                }catch(Exception e){}
                //System.err.println(String.format("(%s) %s %s %s %s", expString,start,end,n,varName));
                diagramDisplayer.inputss = new ArrayList<>();
                for(String expString:exps){
                    //System.err.println(expString);
                    expString = Calculator.calString(expString);
                    List<Double>[] outputs = Calculator.diaGen(expString,varName,start,end,n);
                    diagramDisplayer.inputss.add(outputs);
                }
                //diagramDisplayer.refreshSize();
                long endTime = System.currentTimeMillis();
                long cost = endTime-stratTime;
                Lobby.getLogDisplayer().addLog(String.format("Diagram Generated! Cost %s.%ss", cost/1000,cost%1000));
                Lobby.getProgressBar().setProgress(100.0);
                diagramDisplayer.setVisible(true);
                running=false;
            }
          });
        diaThread.start();
    }
    public void stopAll() {
        try{
            calThread.stop();
        }catch(Exception e){}
        try{
            diaThread.stop();
        }catch(Exception e){}
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
        if(e.getSource()==diagram){//scatter plot
            if(!running)startDia();
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
