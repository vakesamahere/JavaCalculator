package mycalculator.tools.Operators;
import mycalculator.entity.Expression;
import mycalculator.tools.Calculator;
import mycalculator.tools.Operator;
public class Absolute extends Operator {
    public final static String pattern = "abs";
    public final static boolean bracketlike=true;
    public final static boolean left = true;
    public final static boolean right = true;
    public Absolute(){
    }
    public String solve(){
        Double result = Math.abs(Double.valueOf(Calculator.cal(parameters[0])));
        String output = nf.format(result);
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        
        expression.o = new Absolute();
        Operator.loadSelfBracketLike(expString, expression,pattern,index+pattern.length());
    }
}
