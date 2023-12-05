package mycalculator.tools.Operators;
import mycalculator.Lobby;
import mycalculator.entity.Expression;
import mycalculator.tools.Calculator;
import mycalculator.tools.Operator;
public class Cos extends Operator {
    public final static String pattern = "cos";
    public final static boolean bracketlike=true;
    public final static boolean left = true;
    public final static boolean right = true;
    public Cos(){
    }
    public String solve(){
        Double num = Double.valueOf(Calculator.cal(parameters[0]));
        Double result = Math.cos(num);
        String output = nf.format(result);
        Lobby.getLogDisplayer().addLog(String.format("[Output]cos(%s)=%s", num,output));
        
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        
        expression.o = new Cos();
        Operator.loadSelfBracketLike(expString, expression,pattern,index+pattern.length());
    }
}
