package mycalculator.tools.Operators;
import java.util.ArrayList;
import java.util.List;

import mycalculator.entity.Expression;
import mycalculator.tools.Calculator;
import mycalculator.tools.Operator;
public class MatrixJointRow extends Operator {
    public final static String pattern = "jr";
    public final static boolean bracketlike=true;
    public final static boolean left = true;
    public final static boolean right = true;
    public final static int commaCount = 16;
    public final static int maxSize = 16;
    public MatrixJointRow(){
        parameters = new String[maxSize];
    }
    public String solve(){
        int len=0;
        String[] array0 = stringToArray(Calculator.cal(parameters[0]));
        String[][] matrix0 = arrayToMatrix(array0);
        len+=matrix0[0].length;
        List<String>[] temp = new List[matrix0.length];
        for(int i=0;i<matrix0.length;i++){
            temp[i]=new ArrayList<>();
            for(String item:matrix0[i]){
                temp[i].add(item);
            }
        }
        for(int i=1;i<maxSize;i++){
            if(parameters[i]==null){
                break;
            }
            String[] array = stringToArray(Calculator.cal(parameters[i]));
            String[][] matrix = arrayToMatrix(array);
            len+=matrix[0].length;
            for(int k=0;k<matrix.length;k++){
                for(String item:matrix[k]){
                    temp[k].add(item);
                }
            }
        }
        String[][] result = new String[matrix0.length][len];
        for(int i=0;i<matrix0.length;i++){
            for(int j=0;j<len;j++){
                result[i][j]=temp[i].get(j);
            }
        }
        String output = Operator.matrixToString(result);
        
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        
        expression.o = new MatrixJointRow();
        Operator.loadSelfCommaIncluded(expString, expression,pattern,commaCount,index+pattern.length());
    }
}
