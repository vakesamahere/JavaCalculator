package MyCalculator.Tools.Operators;
import MyCalculator.Entity.Expression;
import MyCalculator.Tools.Calculator;
import MyCalculator.Tools.Operator;
public class MatrixInv extends Operator {
    public final static String pattern = "inv";
    public final static boolean bracketlike=true;
    public final static boolean left = true;
    public final static boolean right = true;
    public MatrixInv(){
    }
    public String solve(){
        //System.err.println("Solve:"+parameters[0]);
        String[] array = stringToArray(Calculator.cal(parameters[0]));
        String[][] matrixStr = arrayToMatrix(array);
        //方阵
        int n=matrixStr.length;
        Double matrixHalf[][] = Operator.matrixToDoubles(matrixStr);
        Double[][] matrix = new Double[n][2*n];
        for(int i=0;i<n;i++){
            System.arraycopy(matrixHalf[i], 0, matrix[i], 0, n);
            for(int j=0;j<n;j++)matrix[i][n+j]=(i==j)?1.0:0.0;
        }
        //高斯
        for(int i=0;i<n;i++){
            for(int k=i;k<n;k++){
                if(Math.abs(matrix[i][i])<Math.abs(matrix[k][i]))Operator.matrixRowSwap(matrix, i, k);
            }
            if(matrix[i][i]==0)return "{ERROR:DET=0}";
            Operator.matrixRowMul(matrix, i, 1/matrix[i][i]);
            for(int k=0;k<n;k++){
                if(k==i)continue;
                Operator.matrixRowAdd(matrix, i, k,-matrix[k][i]/matrix[i][i]);
            }
        }
        Double[][] resultMatrix = new Double[n][n];
        for(int i=0;i<n;i++)System.arraycopy(matrix[i], n, resultMatrix[i], 0, n);
        //Lobby.getLogDisplayer().addLog(String.format("[Output]MatrixInv(%s)=%s", num,output));
        String output = Operator.matrixToString(resultMatrix);
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        
        expression.o = new MatrixInv();
        Operator.loadSelfBracketLike(expString, expression,pattern,index+pattern.length());
    }
}
