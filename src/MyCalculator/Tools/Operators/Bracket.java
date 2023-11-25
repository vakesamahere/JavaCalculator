package MyCalculator.Tools.Operators;
import MyCalculator.Entity.Expression;
import MyCalculator.Tools.Calculator;
import MyCalculator.Tools.Operator;
public class Bracket extends Operator {
    public final static String pattern = "B";
    public final static boolean bracketlike=true;
    public final static boolean left = true;
    public final static boolean right = true;
    public Bracket(){
    }
    public String solve(){
        //System.err.println("Solve:"+parameters[0]);
        String output = Calculator.cal(parameters[0]);
        //System.err.println(output);
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        
        expression.o = new Bracket();
        Operator.loadSelfBracketLike(expString, expression,pattern,index+pattern.length());
    }
}
