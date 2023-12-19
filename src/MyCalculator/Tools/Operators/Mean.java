package mycalculator.tools.operators;
import mycalculator.Lobby;
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
        String[] array = Calculator.stringToArray(Calculator.cal(parameters[0]));
        Double sum=0.0;
        for(String num:array){
            sum+=Double.parseDouble(Calculator.cal(num));
        }
        sum/=array.length;
        String output = nf.format(sum);
        Lobby.getLogDisplayer().addLog(String.format("[Output]E(%s)=%s", parameters[0],output));
        
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        
        expression.o = new Mean();
        Calculator.loadSelfBracketLike(expString, expression,pattern,index+pattern.length());
    }
}
