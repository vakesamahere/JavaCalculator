package MyCalculator.Tools.Operators;

import MyCalculator.Lobby;
import MyCalculator.Entity.Expression;
import MyCalculator.Entity.ProgressBar;
import MyCalculator.Entity.Variable;
import MyCalculator.Tools.Calculator;
import MyCalculator.Tools.Operator;

public class Sum extends Operator{
    public final static String pattern = "sum";
    public final static boolean bracketlike=true;
    public final static boolean left = true;
    public final static boolean right = true;
    public final static int commaCount = 3;
    private final static String varString = "@";
    //6parameters:min=4,max=4 [x,a,b,fx,d(=1)]
    public Sum(){
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
        //System.err.println(String.format("Fx:%s;", fx));

        Double result=0.0;
        int startInt =Integer.parseInt(a);
        int endInt =Integer.parseInt(b);
        int time=Math.max(endInt-startInt+1, 0);
        if(time==0)return "0";

        //System.err.println("start..");
        Double unitIcre = 100.0/time;
        Double progress=0.0;
        ProgressBar bar = Lobby.getProgressBar();
        bar.setProgress(0.0);
        //long stratTime = System.nanoTime();
        for(int i=startInt;i<=endInt;i++) {
            progress+=unitIcre;
            bar.setProgress(progress);//cost less than 2%
            result+=Double.parseDouble(Calculator.cal(fx.replace(varString, nf.format(i))));
            //System.err.println(String.format("n=%d,i=%d,dx=%.4f,result=%.4f",n,i,dx,result)); cost 17.5% more
        }
        //long endTime = System.nanoTime();  
        //System.err.println(String.format("TimeCosted:%d",endTime-stratTime));
        //System.err.println("Done!");
        
        String output=nf.format(result);
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        
        expression.o = new Sum();
        Operator.loadSelfCommaIncluded(expString, expression,pattern,commaCount,index+pattern.length());
    }
    
}
