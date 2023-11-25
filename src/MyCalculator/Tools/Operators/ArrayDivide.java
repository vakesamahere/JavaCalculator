package MyCalculator.Tools.Operators;
import MyCalculator.Entity.Expression;
import MyCalculator.Tools.Calculator;
import MyCalculator.Tools.Operator;

public class ArrayDivide extends Operator {
    public final static String pattern = "M/M";
    public final static boolean bracketlike=false;
    public final static boolean left = true;
    public final static boolean right = true;
    public ArrayDivide(){
    }
    public String solve(){
        String[] arr = Operator.stringToArray(parameters[0]);
        int n=arr.length;
        String[] arrayRes = new String[n];
        for(int i=0;i<n;i++){
            arrayRes[i]=Calculator.cal(String.format(" %s %s %s ",Calculator.cal(arr[i]),Divide.pattern,Calculator.cal(parameters[1])));
        }
        String output = Operator.arrayToString(arrayRes);
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        expression.o = new ArrayDivide();
        Operator.loadSelfArrayIncluded(expString, expression,pattern,left,right,index);
    }
}
