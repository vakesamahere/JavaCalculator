package mycalculator.tools.Operators;
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
        String[] array = stringToArray(Calculator.cal(parameters[0]));
        String[][] matrix = arrayToMatrix(array);
        int m = matrix.length,n=matrix[0].length;
        String[][] result = new String[n][m];
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                result[j][i]=matrix[i][j];
            }
        }
        String output = matrixToString(result);
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        
        expression.o = new MatrixTrans();
        Operator.loadSelfBracketLike(expString, expression,pattern,index+pattern.length());
    }
}
