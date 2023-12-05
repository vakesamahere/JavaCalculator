package mycalculator.tools.Operators;
import mycalculator.Lobby;
import mycalculator.entity.Expression;
import mycalculator.tools.Calculator;
import mycalculator.tools.Operator;
public class Power extends Operator {
    public final static String pattern = "@Power@";
    public final static boolean bracketlike=false;
    public final static boolean left = true;
    public final static boolean right = true;
    public Power(){
    }
    public String solve(){
        //矩阵^数字
        if(parameters[0].indexOf(']')!=-1){
            return parameters[0]+String.format("%s%s%s",pattrnFix, pattern, pattrnFix)+parameters[1];
        }
        Double num1 = Double.valueOf(Calculator.cal(parameters[0]));
        Double num2 = Double.valueOf(Calculator.cal(parameters[1]));
        Double result = Math.pow(num1, num2);
        String output = nf.format(result);
        Lobby.getLogDisplayer().addLog(String.format("[Output]%s^%s=%s", num1,num2,output));
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        expression.o = new Power();
        Operator.loadSelfSingle(expString, expression,pattern,left,right,index);
    }
}
