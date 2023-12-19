package mycalculator.tools.operators;
import mycalculator.Lobby;
import mycalculator.entity.Expression;
import mycalculator.tools.Calculator;
import mycalculator.tools.Operator;
public class MatrixTrans extends Operator {
    public final static String pattern = "tr";
    public final static boolean bracketlike=true;
    public final static boolean left = true;
    public final static boolean right = true;
    public MatrixTrans(){
    }
    public String solve(){
        String[] array = Calculator.stringToArray(Calculator.cal(parameters[0]));
        String[][] matrix = Calculator.arrayToMatrix(array);
        int m = matrix.length,n=matrix[0].length;
        String[][] result = new String[n][m];
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                result[j][i]=matrix[i][j];
            }
        }
        String output = Calculator.matrixToString(result);
        Lobby.getLogDisplayer().addLog(String.format("[Output]%s^T", parameters[0],output));
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        
        expression.o = new MatrixTrans();
        Calculator.loadSelfBracketLike(expString, expression,pattern,index+pattern.length());
    }
}
