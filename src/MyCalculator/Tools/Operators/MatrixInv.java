package mycalculator.tools.operators;
import mycalculator.Lobby;
import mycalculator.entity.Expression;
import mycalculator.tools.Calculator;
import mycalculator.tools.Operator;
public class MatrixInv extends Operator {
    public final static String pattern = "inv";
    public final static boolean bracketlike=true;
    public final static boolean left = true;
    public final static boolean right = true;
    public MatrixInv(){
    }
    public String solve(){
        String[] array = Calculator.stringToArray(Calculator.cal(parameters[0]));
        String[][] matrixStr = Calculator.arrayToMatrix(array);
        //方阵
        int n=matrixStr.length;
        Double[][] matrixHalf = Calculator.matrixToDoubles(matrixStr);
        Double[][] matrix = new Double[n][2*n];
        for(int i=0;i<n;i++){
            System.arraycopy(matrixHalf[i], 0, matrix[i], 0, n);
            for(int j=0;j<n;j++){
                matrix[i][n+j]=(i==j)?1.0:0.0;
            }
        }
        //高斯
        for(int i=0;i<n;i++){
            for(int k=i;k<n;k++){
                if(Math.abs(matrix[i][i])<Math.abs(matrix[k][i])){
                    Calculator.matrixRowSwap(matrix, i, k);
                }
            }
            if(matrix[i][i]==0){
                return "{ERROR:DET=0}";
            }
            Calculator.matrixRowMul(matrix, i, 1/matrix[i][i]);
            for(int k=0;k<n;k++){
                if(k==i){
                    continue;
                }
                Calculator.matrixRowAdd(matrix, i, k,-matrix[k][i]/matrix[i][i]);
            }
        }
        Double[][] resultMatrix = new Double[n][n];
        for(int i=0;i<n;i++){
            System.arraycopy(matrix[i], n, resultMatrix[i], 0, n);
        }
        String output = Calculator.matrixToString(resultMatrix);
        Lobby.getLogDisplayer().addLog(String.format("[Output]%s^-1", parameters[0],output));
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        
        expression.o = new MatrixInv();
        Calculator.loadSelfBracketLike(expString, expression,pattern,index+pattern.length());
    }
}
