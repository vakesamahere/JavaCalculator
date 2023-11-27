package MyCalculator.Entity;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

import MyCalculator.Lobby;

public class LogDisplayer extends IndependentVar {
    private final static int maxLength = 10000;
    private final static int secondsExecuteClean=30;
    private TimerTask run;
    private ScheduledExecutorService scheduledExecutorService;
    public int highFreq;

    public LogDisplayer(String str){
        super(str);
        highFreq=0;
        scheduledExecutorService = Executors.newScheduledThreadPool(3);
        run = new TimerTask() {
            @Override
            public void run(){
                controlLength();
            }
        };
        scheduledExecutorService.scheduleAtFixedRate(run, 0, secondsExecuteClean,TimeUnit.SECONDS);
    }
    public JPanel getPanel(){
        return selfPanel;
    }
    public void addLog(String str){
        if(highFreq>0)return;
        valueText.setText(valueText.getText()+str+"\n");
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
    }
    public void highFreqCalStart(){
        highFreq++;
    }
    public void highFreqCalEnd(){
        highFreq--;
    }
    public void highFreqCalStatementReset(){
        highFreq=0;
    }
    public void controlLength(){
        if(Lobby.getCalculatorPanel().isRunning()==false)highFreq=0;
        System.err.println("Checking Length..");
        int length = valueText.getText().length();
        if(length>maxLength){
            valueText.setText(valueText.getText().substring(length/2));
        }
        System.err.println(String.format("Length:%s>>>>%s", length,valueText.getText().length()));
    }
}
