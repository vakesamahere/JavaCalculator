package mycalculator.tools.operators;
import mycalculator.Lobby;
import mycalculator.entity.Expression;
import mycalculator.tools.Calculator;
import mycalculator.tools.Operator;
public class Arccos extends Operator {
    public final static String pattern = "arccos";
    public final static boolean bracketlike=true;
    public final static boolean left = true;
    public final static boolean right = true;
    public Arccos(){
    }
    public String solve(){
        Double num = Double.valueOf(Calculator.cal(parameters[0]));
        Double result = Math.acos(num);
        String output = nf.format(result);
        Lobby.getLogDisplayer().addLog(String.format("[Output]arccos(%s)=%s", num,output));
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        
        expression.o = new Arccos();
        Operator.loadSelfBracketLike(expString, expression,pattern,index+pattern.length());
    }
}
