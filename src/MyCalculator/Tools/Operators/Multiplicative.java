package mycalculator.tools.Operators;

import mycalculator.Lobby;
import mycalculator.entity.Expression;
import mycalculator.entity.ProgressBar;
import mycalculator.entity.Variable;
import mycalculator.tools.Calculator;
import mycalculator.tools.Operator;

public class Multiplicative extends Operator{
    public final static String pattern = "mul";
    public final static boolean bracketlike=true;
    public final static boolean left = true;
    public final static boolean right = true;
    public final static int commaCount = 3;
    private final static String varString = "#";
    //6parameters:min=4,max=4 [x,a,b,fx,d(=1)]
    public Multiplicative(){
    }

    public String solve() {
        String x = parameters[0];
        String a = Calculator.cal(parameters[1]);
        String b = Calculator.cal(parameters[2]);
        String fx = parameters[3];

        
        x=x.replace(" ", "");
        Variable va = new Variable(x,false);
        va.setValue(varString);
        fx=Calculator.replaceVar(fx, va);

        Double result=1.0;
        int startInt =Integer.parseInt(a);
        int endInt =Integer.parseInt(b);
        int time=Math.max(endInt-startInt+1, 0);
        if(time==0){
            return "0";
        }

        Double unitIcre = 100.0/time;
        Double progress=0.0;
        ProgressBar bar = Lobby.getProgressBar();
        bar.setProgress(0.0);
        for(int i=startInt;i<=endInt;i++) {
            progress+=unitIcre;
            //cost less than 2%
            bar.setProgress(progress);
            result*=Double.parseDouble(Calculator.cal(fx.replace(varString, nf.format(i))));
        }
        
        String output=nf.format(result);
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        
        expression.o = new Multiplicative();
        Operator.loadSelfCommaIncluded(expString, expression,pattern,commaCount,index+pattern.length());
    }
    
}
