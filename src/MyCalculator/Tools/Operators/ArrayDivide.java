package mycalculator.tools.operators;
import mycalculator.Lobby;
import mycalculator.entity.Expression;
import mycalculator.tools.Calculator;
import mycalculator.tools.Operator;

public class ArrayDivide extends Operator {
    public final static String pattern = pattrnFix+Divide.pattern+pattrnFix;
    public final static boolean bracketlike=false;
    public final static boolean left = true;
    public final static boolean right = true;
    public ArrayDivide(){
    }
    public String solve(){
        String[] arr = Calculator.stringToArray(parameters[0]);
        int n=arr.length;
        String[] arrayRes = new String[n];
        for(int i=0;i<n;i++){
            arrayRes[i]=Calculator.cal(String.format(" %s %s %s ",Calculator.cal(arr[i]),Divide.pattern,Calculator.cal(parameters[1])));
        }
        String output = Calculator.arrayToString(arrayRes);
        Lobby.getLogDisplayer().addLog(String.format("[Output]%s/%s=%s", arr,parameters[1],output));
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        expression.o = new ArrayDivide();
        Calculator.loadSelfArrayIncluded(expString, expression,pattern,left,right,index);
    }
}
