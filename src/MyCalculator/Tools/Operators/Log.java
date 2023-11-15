package MyCalculator.Tools.Operators;
import MyCalculator.Lobby;
import MyCalculator.Entity.Expression;
import MyCalculator.Tools.Calculator;
import MyCalculator.Tools.Operator;
public class Log extends Operator {
    public final static String pattern = "log";
    public final static boolean bracketlike=true;
    public final static boolean left = true;
    public final static boolean right = true;
    public final static int commaCount = 1;
    public Log(){
    }
    public String solve(){
        //System.err.println("Solve:"+parameters[0]);
        Double num1 = Double.valueOf(Calculator.cal(parameters[0]));
        Double num2 = Double.valueOf(Calculator.cal(parameters[1]));
        String output = nf.format(Math.log(num2)/Math.log(num1));
        Lobby.getLogDisplayer().addLog(String.format("[Output]log(%s,%s)=%s", num1,num2,output));
        
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        
        expression.o = new Log();
        Operator.loadSelfCommaIncluded(expString, expression,pattern,commaCount,index+pattern.length());
    }
}
