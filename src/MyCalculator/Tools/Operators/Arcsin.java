package mycalculator.tools.operators;
import mycalculator.Lobby;
import mycalculator.entity.Expression;
import mycalculator.tools.Calculator;
import mycalculator.tools.Operator;
public class Arcsin extends Operator {
    public final static String pattern = "arcsin";
    public final static boolean bracketlike=true;
    public final static boolean left = true;
    public final static boolean right = true;
    public Arcsin(){
    }
    public String solve(){
        Double num = Double.valueOf(Calculator.cal(parameters[0]));
        Double result = Math.asin(num);
        String output = nf.format(result);
        Lobby.getLogDisplayer().addLog(String.format("[Output]arcsin(%s)=%s", num,output));
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        
        expression.o = new Arcsin();
        Calculator.loadSelfBracketLike(expString, expression,pattern,index+pattern.length());
    }
}
