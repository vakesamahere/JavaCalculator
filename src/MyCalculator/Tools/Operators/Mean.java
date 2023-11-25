package MyCalculator.Tools.Operators;
import MyCalculator.Entity.Expression;
import MyCalculator.Tools.Calculator;
import MyCalculator.Tools.Operator;
public class Mean extends Operator {
    public final static String pattern = "E";
    public final static boolean bracketlike=true;
    public final static boolean left = true;
    public final static boolean right = true;
    public Mean(){
    }
    public String solve(){
        //System.err.println("Solve:"+parameters[0]);
        String[] array = Operator.stringToArray(Calculator.cal(parameters[0]));
        Double sum=0.0;
        for(String num:array){
            sum+=Double.parseDouble(Calculator.cal(num));
        }
        sum/=array.length;
        String output = nf.format(sum);
        //Lobby.getLogDisplayer().addLog(String.format("[Output]Mean(%s)=%s", num,output));
        
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        
        expression.o = new Mean();
        Operator.loadSelfBracketLike(expString, expression,pattern,index+pattern.length());
    }
}
