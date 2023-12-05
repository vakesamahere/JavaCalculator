package mycalculator.tools.Operators;
import mycalculator.Lobby;
import mycalculator.entity.Expression;
import mycalculator.tools.Calculator;
import mycalculator.tools.Operator;
public class Minus extends Operator {
    public final static String pattern = "@Minus@";
    public final static boolean bracketlike=false;
    public final static boolean left = true;
    public final static boolean right = true;
    public Minus(){
    }
    public String solve(){
        if(parameters[0].indexOf(']')!=-1||parameters[1].indexOf('[')!=-1){
            return parameters[0]+String.format("%s%s%s",pattrnFix, pattern, pattrnFix)+parameters[1];
        }
        Double num1;
        boolean includePre=false;
        try{
            num1 = Double.valueOf(Calculator.cal(parameters[0]));
        }catch(Exception e){
            num1=0.0;
            includePre=true;
        }
        Double num2 = Double.valueOf(Calculator.cal(parameters[1]));
        Double result = num1-num2;
        String output = nf.format(result);
        if(includePre){
            output=parameters[0]+output;
        }
        Lobby.getLogDisplayer().addLog(String.format("[Output]%s-%s=%s", num1,num2,output));
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        expression.o = new Minus();
        Operator.loadSelfSingle(expString, expression,pattern,left,right,index);
    }
}
