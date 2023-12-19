package mycalculator.tools.operators;
import mycalculator.Lobby;
import mycalculator.entity.Expression;
import mycalculator.tools.Calculator;
import mycalculator.tools.Operator;
public class Percent extends Operator {
    public final static String pattern = "%";
    public final static boolean bracketlike=false;
    public final static boolean left = true;
    public final static boolean right = false;
    public Percent(){
    }
    public String solve(){
        Double num = Double.valueOf(Calculator.cal(parameters[0]));
        Double result = num/100;
        String output = nf.format(result);
        Lobby.getLogDisplayer().addLog(String.format("[Output]%s%%=%s", num,output));
        
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        expression.o = new Percent();
        Calculator.loadSelfSingle(expString, expression,pattern,left,right,index);
    }
}
