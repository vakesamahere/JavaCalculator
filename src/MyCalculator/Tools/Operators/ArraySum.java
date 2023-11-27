package MyCalculator.Tools.Operators;

import MyCalculator.Lobby;
import MyCalculator.Entity.Expression;
import MyCalculator.Entity.ProgressBar;
import MyCalculator.Entity.Variable;
import MyCalculator.Tools.Calculator;
import MyCalculator.Tools.Operator;

public class ArraySum extends Operator{
    public final static String pattern = "asum";
    public final static boolean bracketlike=true;
    public final static boolean left = true;
    public final static boolean right = true;
    public final static int commaCount = 2;
    private final static String varString = "@asum@";
    //6parameters:min=4,max=4 [x,[array],fx,d(=1)]
    public ArraySum(){
    }

    public String solve() {
        String x = parameters[0];
        String[] arr = Operator.stringToArray(Calculator.cal(parameters[1]));//array
        String fx = parameters[2];

        
        x=x.replace(" ", "");
        Variable va = new Variable(x,false);
        va.setValue(varString);
        fx=Calculator.replaceVar(fx, va);
        //System.err.println(String.format("Fx:%s;", fx));

        Double result=0.0;
        if(arr.length==0)return "0";

        //System.err.println("start..");
        Double unitIcre = 100.0/arr.length;
        Double progress=0.0;
        ProgressBar bar = Lobby.getProgressBar();
        bar.setProgress(0.0);
        //long stratTime = System.nanoTime();
        for(String num:arr) {
            progress+=unitIcre;
            bar.setProgress(progress);//cost less than 2%
            result+=Double.parseDouble(Calculator.cal(fx.replace(varString, Calculator.cal(num))));
            //System.err.println(String.format("n=%d,i=%d,dx=%.4f,result=%.4f",n,i,dx,result)); cost 17.5% more
        }
        //long endTime = System.nanoTime();  
        //System.err.println(String.format("TimeCosted:%d",endTime-stratTime));
        //System.err.println("Done!");
        
        String output=nf.format(result);
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        
        expression.o = new ArraySum();
        Operator.loadSelfCommaIncluded(expString, expression,pattern,commaCount,index+pattern.length());
    }
    
}
