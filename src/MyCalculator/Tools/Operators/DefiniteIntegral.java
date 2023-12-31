package mycalculator.tools.operators;

import mycalculator.Lobby;
import mycalculator.entity.Expression;
import mycalculator.entity.ProgressBar;
import mycalculator.entity.Variable;
import mycalculator.tools.Calculator;
import mycalculator.tools.Operator;

public class DefiniteIntegral extends Operator{
    public final static String pattern = "inte";
    public final static boolean bracketlike=true;
    public final static boolean left = true;
    public final static boolean right = true;
    public final static int commaCount = 4;
    private final static String varString = "`";
    //6parameters:min=4,max=5 [a,b,fx,gx,n(=1000),realX(=x)]
    public DefiniteIntegral(){
    }

    public String solve() {
        String a = Calculator.cal(parameters[0]);
        String b = Calculator.cal(parameters[1]);
        String fx = parameters[2];
        String x = parameters[3];
        int n;
        Double unit;
        try{
            n=Integer.valueOf(Calculator.cal(parameters[4]));
        }catch(Exception e){
            n=1000;
        }

        unit=Math.abs(Double.parseDouble(b)-Double.parseDouble(a))/n;
        x=x.replace(" ", "");
        Variable va = new Variable(x,false);
        va.setValue(varString);
        //****************************************
        
        //****************************************
        fx=Calculator.replaceVar(fx, va);

        Double result=0.0;
        Double dx=Double.parseDouble(a);
        if(Math.abs(dx-Double.parseDouble(b))<1E-10){
            return "0";
        }

        Double unitIcre = 100.0/n;
        Double progress=0.0;
        ProgressBar bar = Lobby.getProgressBar();
        bar.setProgress(0.0);

        Lobby.getLogDisplayer().highFreqCalStart();
        //*****************************************************
        for(int i=0;i<n;i++) {
            progress+=unitIcre;
            //cost less than 2%
            bar.setProgress(progress);
            result+=Double.parseDouble(Calculator.cal(fx.replace(varString, nf.format(dx))));
            dx+=unit;
        }
        //*****************************************************
        Lobby.getLogDisplayer().highFreqCalEnd();
        
        result/=n/(Double.parseDouble(b)-Double.parseDouble(a));
        String output=nf.format(result);
        Lobby.getLogDisplayer().addLog(String.format("[Output]inte(%s,%s,%s,%s)=%s", a,b,fx,x,output));
        
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        
        expression.o = new DefiniteIntegral();
        Calculator.loadSelfCommaIncluded(expString, expression,pattern,commaCount,index+pattern.length());
    }
    
}
