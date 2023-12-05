package mycalculator.tools.Operators;
import mycalculator.entity.Expression;
import mycalculator.tools.Calculator;
import mycalculator.tools.Operator;
public class Mean extends Operator {
    public final static String pattern = "E";
    public final static boolean bracketlike=true;
    public final static boolean left = true;
    public final static boolean right = true;
    public Mean(){
    }
    public String solve(){
        String[] array = Operator.stringToArray(Calculator.cal(parameters[0]));
        Double sum=0.0;
        for(String num:array){
            sum+=Double.parseDouble(Calculator.cal(num));
        }
        sum/=array.length;
        String output = nf.format(sum);
        
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        
        expression.o = new Mean();
        Operator.loadSelfBracketLike(expString, expression,pattern,index+pattern.length());
    }
}
