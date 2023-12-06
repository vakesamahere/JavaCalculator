package mycalculator.tools.operators;

import mycalculator.Lobby;
import mycalculator.entity.Expression;
import mycalculator.entity.ProgressBar;
import mycalculator.entity.Variable;
import mycalculator.tools.Calculator;
import mycalculator.tools.Operator;

public class ArrayMultiplicative extends Operator{
    public final static String pattern = "amul";
    public final static boolean bracketlike=true;
    public final static boolean left = true;
    public final static boolean right = true;
    public final static int commaCount = 2;
    private final static String varString = "@amul@";
    public ArrayMultiplicative(){
    }

    public String solve() {
        String x = parameters[0];
        String[] arr = Operator.stringToArray(Calculator.cal(parameters[1]));
        String fx = parameters[2];

        
        x=x.replace(" ", "");
        Variable va = new Variable(x,false);
        va.setValue(varString);
        fx=Calculator.replaceVar(fx, va);

        Double result=1.0;
        if(arr.length==0){
            return "0";
        }

        Double unitIcre = 100.0/arr.length;
        Double progress=0.0;
        ProgressBar bar = Lobby.getProgressBar();
        bar.setProgress(0.0);
        for(String num:arr) {
            progress+=unitIcre;
            //cost less than 2%
            bar.setProgress(progress);
            result*=Double.parseDouble(Calculator.cal(fx.replace(varString, Calculator.cal(num))));
        }
        
        String output=nf.format(result);
        Lobby.getLogDisplayer().addLog(String.format("[Output]Mul(%s)of %s =%s", fx,arr,output));
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        
        expression.o = new ArrayMultiplicative();
        Operator.loadSelfCommaIncluded(expString, expression,pattern,commaCount,index+pattern.length());
    }
    
}
