package MyCalculator.Tools.Operators;
import MyCalculator.Entity.Expression;
import MyCalculator.Tools.Operator;

public class ArrayPower extends Operator {
    public final static String pattern = pattrnFix+Power.pattern+pattrnFix;
    public final static boolean bracketlike=false;
    public final static boolean left = true;
    public final static boolean right = true;
    public ArrayPower(){
    }
    public String solve(){
        String[] arr = Operator.stringToArray(parameters[0]);
        int n=arr.length;//方阵
        int times = (int)Double.parseDouble(parameters[1]);
        String[][] I = new String[n][n];
        for(int i=0;i<n;i++)for(int j=0;j<n;j++)I[i][j]=(i==j)?"1":"0";
        String output = Operator.matrixToString(I);
        ArrayTime arraytime = new ArrayTime();
        arraytime.setPa(2, "array");
        arraytime.setPa(3, "array");
        for(int i=0;i<times;i++){
            arraytime.setPa(0, output);
            arraytime.setPa(1, parameters[0]);
            output=arraytime.solve();            
        }
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        expression.o = new ArrayPower();
        Operator.loadSelfArrayIncluded(expString, expression,pattern,left,right,index);
    }
}
