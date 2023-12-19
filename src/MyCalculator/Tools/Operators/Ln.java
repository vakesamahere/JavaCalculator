package mycalculator.tools.operators;
import mycalculator.Lobby;
import mycalculator.entity.Expression;
import mycalculator.tools.Calculator;
import mycalculator.tools.Operator;
public class Ln extends Operator {
    public final static String pattern = "ln";
    public final static boolean bracketlike=true;
    public final static boolean left = true;
    public final static boolean right = true;
    public Ln(){
    }
    public String solve(){
        Double num = Double.valueOf(Calculator.cal(parameters[0]));
        Double result = Math.log(num);
        String output = nf.format(result);
        Lobby.getLogDisplayer().addLog(String.format("[Output]ln(%s)=%s", num,output));
        
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        
        expression.o = new Ln();
        Calculator.loadSelfBracketLike(expString, expression,pattern,index+pattern.length());
    }
}
