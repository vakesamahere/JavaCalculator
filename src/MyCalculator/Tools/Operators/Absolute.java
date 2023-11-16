package MyCalculator.Tools.Operators;
import MyCalculator.Entity.Expression;
import MyCalculator.Tools.Calculator;
import MyCalculator.Tools.Operator;
public class Absolute extends Operator {
    public final static String pattern = "abs";
    public final static boolean bracketlike=true;
    public final static boolean left = true;
    public final static boolean right = true;
    public Absolute(){
    }
    public String solve(){
        //System.err.println("Solve:"+parameters[0]);
        Double result = Math.abs(Double.valueOf(Calculator.cal(parameters[0])));
        String output = nf.format(result);
        //System.err.println(output);
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        
        expression.o = new Absolute();
        Operator.loadSelfBracketLike(expString, expression,pattern,index+pattern.length());
    }
}
