package MyCalculator.Tools.Operators;
import MyCalculator.Lobby;
import MyCalculator.Entity.Expression;
import MyCalculator.Tools.Calculator;
import MyCalculator.Tools.Operator;
public class Add extends Operator {
    public final static String pattern = "+";
    public final static boolean bracketlike=false;
    public final static boolean left = true;
    public final static boolean right = true;
    public Add(){
    }
    public String solve(){
        Double num1 = Double.valueOf(Calculator.cal(parameters[0]));
        Double num2 = Double.valueOf(Calculator.cal(parameters[1]));
        Double result = num1+num2;
        String output = nf.format(result);
        Lobby.getLogDisplayer().addLog(String.format("[Output]%s+%s=%s", num1,num2,output));
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        expression.o = new Add();
        Operator.loadSelfSingle(expString, expression,pattern,left,right,index);
    }
}
