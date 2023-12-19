package mycalculator.tools.operators;
import mycalculator.Lobby;
import mycalculator.entity.Expression;
import mycalculator.tools.Calculator;
import mycalculator.tools.Operator;
public class Arctan extends Operator {
    public final static String pattern = "arctan";
    public final static boolean bracketlike=true;
    public final static boolean left = true;
    public final static boolean right = true;
    public Arctan(){
    }
    public String solve(){
        //System.err.println("Solve:"+parameters[0]);
        Double num = Double.valueOf(Calculator.cal(parameters[0]));
        Double result = Math.atan(num);
        String output = nf.format(result);
        Lobby.getLogDisplayer().addLog(String.format("[Output]arctan(%s)=%s", num,output));
        
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        
        expression.o = new Arctan();
        Calculator.loadSelfBracketLike(expString, expression,pattern,index+pattern.length());
    }
}
