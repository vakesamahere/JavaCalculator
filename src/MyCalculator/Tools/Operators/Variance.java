package mycalculator.tools.operators;
import mycalculator.Lobby;
import mycalculator.entity.Expression;
import mycalculator.tools.Calculator;
import mycalculator.tools.Operator;
public class Variance extends Operator {
    public final static String pattern = "D";
    public final static boolean bracketlike=true;
    public final static boolean left = true;
    public final static boolean right = true;
    public Variance(){
    }
    public String solve(){
        String input = Calculator.cal(parameters[0]);
        String[] array = Calculator.stringToArray(input);
        Double sum=0.0;
        Double mean;
        Mean m = new Mean();
        m.setPa(0, input);
        mean=Double.parseDouble(m.solve());
        for(String num:array){
            sum+=Math.pow((Double.parseDouble(Calculator.cal(num))-mean),2);
        }
        sum/=array.length;
        String output = nf.format(sum);
        Lobby.getLogDisplayer().addLog(String.format("[Output]D(%s)=%s", parameters[0],output));
        
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        
        expression.o = new Variance();
        Calculator.loadSelfBracketLike(expString, expression,pattern,index+pattern.length());
    }
}
