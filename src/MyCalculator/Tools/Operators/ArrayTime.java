package MyCalculator.Tools.Operators;
import MyCalculator.Entity.Expression;
import MyCalculator.Tools.Calculator;
import MyCalculator.Tools.Operator;

public class ArrayTime extends Operator {
    public final static String pattern = "M*M";
    public final static boolean bracketlike=false;
    public final static boolean left = true;
    public final static boolean right = true;
    public ArrayTime(){
    }
    public String solve(){
        //判断类型，前提：至少一个是矩阵
        if(parameters[2]=="num")return solveNA(parameters[0],parameters[1]);
        if(parameters[3]=="num")return solveNA(parameters[1],parameters[0]);
        //else>>内积

        String[] array1 = Operator.stringToArray(parameters[0]);
        String[] array2 = Operator.stringToArray(parameters[1]);
        String[][] matrix1= Operator.arrayToMatrix(array1);
        if(matrix1.length==0||matrix1[0].length==0){
            matrix1 = new String[array1.length][1];
            for(int i=0;i<array1.length;i++)matrix1[i][0]=array1[i];
        }
        String[][] matrix2= Operator.arrayToMatrix(array2);    
        if(matrix2.length==0||matrix2[0].length==0){
            matrix2 = new String[array2.length][1];
            for(int i=0;i<array2.length;i++)matrix2[i][0]=array2[i];
        }    
        //
        int m1=matrix1.length;
        int len=Math.min(matrix1[0].length,matrix2.length);
        int n2=matrix2[0].length;
        String[][] matrixRes = new String[m1][n2];
        for(int i=0;i<m1;i++)for(int j=0;j<n2;j++){
            String sum="0";
            for(int k=0;k<len;k++)sum=Calculator.cal(String.format("%s %s %s %s %s",sum,Add.pattern,Calculator.cal(matrix1[i][k]),Time.pattern,Calculator.cal(matrix2[k][j])));
            matrixRes[i][j]=sum;
        }
        String output = Operator.matrixToString(matrixRes);
        //Lobby.getLogDisplayer().TimeLog(String.format("[Output]%s+%s=%s", num1,num2,output));
        return output;
    }
    public String solveNA(String num,String array){//数乘
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
