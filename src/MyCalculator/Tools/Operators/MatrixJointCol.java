package mycalculator.tools.operators;
import java.util.ArrayList;
import java.util.List;

import mycalculator.entity.Expression;
import mycalculator.tools.Calculator;
import mycalculator.tools.Operator;
public class MatrixJointCol extends Operator {
    public final static String pattern = "jc";
    public final static boolean bracketlike=true;
    public final static boolean left = true;
    public final static boolean right = true;
    public final static int commaCount = 16;
    public final static int maxSize = 16;
    public MatrixJointCol(){
        parameters = new String[maxSize];
    }
    public String solve(){
        List<String[]> temp = new ArrayList<>();
        for(int i=0;i<maxSize;i++){
            if(parameters[i]==null){
                break;
            }
            String[] array = Calculator.stringToArray(Calculator.cal(parameters[i]));
            String[][] matrix = Calculator.arrayToMatrix(array);
            for(String[] arr:matrix){
                temp.add(arr);
            }
        }
        String[][] result = new String[temp.size()][];
        int index=0;
        for(String[] arr:temp){
            result[index++]=arr;
        }
        String output = Calculator.matrixToString(result);
        
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        
        expression.o = new MatrixJointCol();
        Calculator.loadSelfCommaIncluded(expString, expression,pattern,commaCount,index+pattern.length());
    }
}
