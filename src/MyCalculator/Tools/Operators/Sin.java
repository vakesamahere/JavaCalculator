package mycalculator.tools.Operators;
import mycalculator.Lobby;
import mycalculator.entity.Expression;
import mycalculator.tools.Calculator;
import mycalculator.tools.Operator;
public class Sin extends Operator {
    public final static String pattern = "sin";
    public final static boolean bracketlike=true;
    public final static boolean left = true;
    public final static boolean right = true;
    public Sin(){
    }
    public String solve(){
        Double num = Double.valueOf(Calculator.cal(parameters[0]));
        Double result = Math.sin(num);
        String output = nf.format(result);
        Lobby.getLogDisplayer().addLog(String.format("[Output]sin(%s)=%s", num,output));
        
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        
        expression.o = new Sin();
        Operator.loadSelfBracketLike(expString, expression,pattern,index+pattern.length());
    }
}
