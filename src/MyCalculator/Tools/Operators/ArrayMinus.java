package mycalculator.tools.operators;
import mycalculator.Lobby;
import mycalculator.entity.Expression;
import mycalculator.tools.Calculator;
import mycalculator.tools.Operator;

public class ArrayMinus extends Operator {
    public final static String pattern = pattrnFix+Minus.pattern+pattrnFix;
    public final static boolean bracketlike=false;
    public final static boolean left = true;
    public final static boolean right = true;
    public ArrayMinus(){
    }
    public String solve(){
        //不接受数字，只接受向量
        String[] array1 = Calculator.stringToArray(parameters[0]);
        String[] array2 = Calculator.stringToArray(parameters[1]);
        int n=array1.length;
        String[] arrayRes = new String[n];
        for(int i=0;i<n;i++){
            arrayRes[i]=Calculator.cal(String.format(" %s(%s) %s %s(%s) ",Bracket.pattern,array1[i],Minus.pattern,Bracket.pattern,array2[i]));
        }
        String output = Calculator.arrayToString(arrayRes);
        Lobby.getLogDisplayer().addLog(String.format("[Output]%s-%s=%s", array1,array2,output));
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        expression.o = new ArrayMinus();
        Calculator.loadSelfArrayIncluded(expString, expression,pattern,left,right,index);
    }
}
