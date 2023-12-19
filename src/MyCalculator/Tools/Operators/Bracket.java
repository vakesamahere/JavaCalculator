package mycalculator.tools.operators;
import mycalculator.entity.Expression;
import mycalculator.tools.Calculator;
import mycalculator.tools.Operator;
public class Bracket extends Operator {
    public final static String pattern = "B";
    public final static boolean bracketlike=true;
    public final static boolean left = true;
    public final static boolean right = true;
    public Bracket(){
    }
    public String solve(){
        String output = Calculator.cal(parameters[0]);
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        
        expression.o = new Bracket();
        Calculator.loadSelfBracketLike(expString, expression,pattern,index+pattern.length());
    }
}
