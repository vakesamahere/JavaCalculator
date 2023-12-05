package mycalculator.tools.Operators;
import mycalculator.entity.Expression;
import mycalculator.tools.Calculator;
import mycalculator.tools.Operator;

public class ArrayTime extends Operator {
    public final static String pattern = pattrnFix+Time.pattern+pattrnFix;
    public final static boolean bracketlike=false;
    public final static boolean left = true;
    public final static boolean right = true;
    public ArrayTime(){
    }
    public String solve(){
        //判断类型，前提：至少一个是矩阵
        if(parameters[2]=="num"){
            return solveNA(parameters[0],parameters[1]);
        }
        if(parameters[3]=="num"){
            return solveNA(parameters[1],parameters[0]);
        }
        //else>>内积

        String[] array1 = Operator.stringToArray(parameters[0]);
        String[] array2 = Operator.stringToArray(parameters[1]);
        String[][] matrix1= Operator.arrayToMatrix(array1);
        String[][] matrix2= Operator.arrayToMatrix(array2);
        //
        int m1=matrix1.length;
        int len=Math.min(matrix1[0].length,matrix2.length);
        int n2=matrix2[0].length;
        String[][] matrixRes = new String[m1][n2];
        for(int i=0;i<m1;i++){
            for(int j=0;j<n2;j++){
                String sum="0";
                for(int k=0;k<len;k++){
                    sum=Calculator.cal(String.format("%s %s %s %s %s",sum,Add.pattern,Calculator.cal(matrix1[i][k]),Time.pattern,Calculator.cal(matrix2[k][j])));
                }
                matrixRes[i][j]=sum;
            }
        }
        String output = Operator.matrixToString(matrixRes);
        return output;
    }
    /**数乘*/
    public String solveNA(String num,String array){
        String[] arr = Operator.stringToArray(array);
        int n=arr.length;
        String[] arrayRes = new String[n];
        for(int i=0;i<n;i++){
            arrayRes[i]=Calculator.cal(String.format(" %s %s %s ",Calculator.cal(num),Time.pattern,Calculator.cal(arr[i])));
        }
        String output = Operator.arrayToString(arrayRes);

        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        expression.o = new ArrayTime();
        Operator.loadSelfArrayIncluded(expString, expression,pattern,left,right,index);
    }
}
