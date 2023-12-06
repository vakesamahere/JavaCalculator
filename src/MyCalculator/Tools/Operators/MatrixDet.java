package mycalculator.tools.operators;
import mycalculator.Lobby;
import mycalculator.entity.Expression;
import mycalculator.tools.Calculator;
import mycalculator.tools.Operator;
public class MatrixDet extends Operator {
    public final static String pattern = "det";
    public final static boolean bracketlike=true;
    public final static boolean left = true;
    public final static boolean right = true;
    public MatrixDet(){
    }
    public String solve(){
        String[] array = stringToArray(Calculator.cal(parameters[0]));
        String[][] matrixStr = arrayToMatrix(array);
        //方阵
        int n=matrixStr.length;
        Double[][] matrix = Operator.matrixToDoubles(matrixStr);
        //高斯
        for(int i=0;i<n;i++){
            for(int k=i;k<n;k++){
                if(Math.abs(matrix[i][i])<Math.abs(matrix[k][i])){
                    Operator.matrixRowSwap(matrix, i, k);
                }
            }
            if(matrix[i][i]==0){
                return "0";
            }
            for(int k=i+1;k<n;k++){
                Operator.matrixRowAdd(matrix, i, k,-matrix[k][i]/matrix[i][i]);
            }
            System.err.println(String.format("%s", matrix[i][i]));
        }
        double result=1.0;
        for(int i=0;i<n;i++){
            result*=matrix[i][i];
        }
        String output = nf.format(result);
        Lobby.getLogDisplayer().addLog(String.format("[Output]|%s|=%s", parameters[0],output));
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        
        expression.o = new MatrixDet();
        Operator.loadSelfBracketLike(expString, expression,pattern,index+pattern.length());
    }
}
