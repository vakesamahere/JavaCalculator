package MyCalculator.Tools.Operators;
import MyCalculator.Entity.Expression;
import MyCalculator.Tools.Calculator;
import MyCalculator.Tools.Operator;

public class ArrayMinus extends Operator {
    public final static String pattern = pattrnFix+Minus.pattern+pattrnFix;
    public final static boolean bracketlike=false;
    public final static boolean left = true;
    public final static boolean right = true;
    public ArrayMinus(){
    }
    public String solve(){
        //不接受数字，只接受向量
        String[] array1 = Operator.stringToArray(parameters[0]);
        String[] array2 = Operator.stringToArray(parameters[1]);
        int n=array1.length;//n列
        String[] arrayRes = new String[n];
        for(int i=0;i<n;i++){
            arrayRes[i]=Calculator.cal(String.format(" %s(%s) %s %s(%s) ",Bracket.pattern,array1[i],Minus.pattern,Bracket.pattern,array2[i]));
        }
        String output = Operator.arrayToString(arrayRes);
        //Lobby.getLogDisplayer().MinusLog(String.format("[Output]%s+%s=%s", num1,num2,output));
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        expression.o = new ArrayMinus();
        Operator.loadSelfArrayIncluded(expString, expression,pattern,left,right,index);
    }
}
