package mycalculator.tools.operators;
import mycalculator.Lobby;
import mycalculator.entity.Expression;
import mycalculator.tools.Operator;
import mycalculator.tools.Calculator;

public class ArrayPower extends Operator {
    public final static String pattern = pattrnFix+Power.pattern+pattrnFix;
    public final static boolean bracketlike=false;
    public final static boolean left = true;
    public final static boolean right = true;
    public ArrayPower(){
    }
    public String solve(){
        String[] arr = Calculator.stringToArray(parameters[0]);
        //方阵
        int n=arr.length;
        int times = (int)Double.parseDouble(parameters[1]);
        String[][] I = new String[n][n];
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                I[i][j]=(i==j)?"1":"0";
            }
        }
        String output = Calculator.matrixToString(I);
        ArrayTime arraytime = new ArrayTime();
        arraytime.setPa(2, "array");
        arraytime.setPa(3, "array");
        for(int i=0;i<times;i++){
            arraytime.setPa(0, output);
            arraytime.setPa(1, parameters[0]);
            output=arraytime.solve();            
        }
        Lobby.getLogDisplayer().addLog(String.format("[Output]%s^%s=%s", arr,times,output));
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        expression.o = new ArrayPower();
        Calculator.loadSelfArrayIncluded(expString, expression,pattern,left,right,index);
    }
}
