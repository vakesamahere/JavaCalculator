package MyCalculator.Tools;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

import MyCalculator.Entity.Expression;

public abstract class Operator {
    public final static String operatorPath = "MyCalculator.Tools.Operators.";
    public final static String nonOpRegex = "[^A-Za-z]";
    public static List<List<String>> operators = Arrays.asList(
        Arrays.asList(
            "DefiniteIntegral"
            ,"Sum"
            ,"Multiplicative"
            //,"ArraySum"//求和
            //,"ArrayMul"//求积
        ),
        Arrays.asList(
            "Bracket"
            ,"Exp"
            ,"Ln"
            ,"Sin"
            ,"Cos"
            ,"Tan"
            ,"Log"
            ,"Arcsin"
            ,"Arccos"
            ,"Arctan"
            ,"Absolute"
            ,"MatrixJointRow"//行拼接
            ,"MatrixJointCol"//列拼接
            //,"MatrixAdjoint"//生成伴随矩阵
            //,"MatrixDet"//求行列式
            //,"MatrixInv"//逆矩阵
            ,"MatrixTrans"//转置
            ,"Mean"//期望
            ,"Variance"//方差
            ,"Covariance"//协方差
        ),
        Arrays.asList(
            "Percent"
        ),
        Arrays.asList(
            "Power"
            ,"ArrayPower"//矩阵幂运算
        ),
        Arrays.asList(
            "Time"
            ,"Divide"
            ,"ArrayTime"//数乘&矩阵乘法
            ,"ArrayDivide"//矩阵和数的除法
        ),
        Arrays.asList(
            "Add"
            ,"Minus"
            ,"ArrayAdd"//矩阵加
            ,"ArrayMinus"//矩阵减
        )
    );
    public static List<List<String>> patternStrings=new ArrayList<>();
    public static List<Pattern> patterns=new ArrayList<>();
    public java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
    public String[] parameters = new String[5];//record parameters need to be operated
    public static String pattern;
    public static String[] punish;
    public static boolean bracketlike;
    public static boolean left;
    public static boolean right;

    public abstract String solve();//return a string(join the original expression) of the result

    public Operator(){
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(15);
    }
    public static Boolean isNum(char c){
        if(('0'<=c)&&(c<='9')||(c=='.')||(c=='-')||(c==' '))return true;
        return false;
    }
    public String getPattern(){
        return pattern;
    }
    public static void loadSelfSingle(String expString,Expression expression,String pattern,boolean left,boolean right,int pos){// (...)?(...)
        //single operation
        if(left){//capture the left number
            int tempPos=pos;
            boolean end = false;
            //if(pos==0)find=false;
            while(true){//skip other unary
                char tempChar=expString.charAt(tempPos);
                if(!isNum(tempChar)||tempChar==' '){
                    tempPos--;
                    if (tempPos==-1){
                        end=true;
                        break;
                    }
                    continue;
                }
                break;//find a number
            }
            while(!end){//include the numbers
                char tempChar=expString.charAt(tempPos);
                if(isNum(tempChar)){
                    tempPos--;
                    if (tempPos==-1){
                        break;
                    }
                    continue;
                }
                break;//not a number
            }
            //System.err.println("find:"+expString.substring(tempPos+1, pos)+";");
            expression.o.parameters[0]=expString.substring(tempPos+1,pos);
            expression.prefix=expString.substring(0,tempPos+1);
        }else{
            expression.prefix=expString.substring(0,pos);
        }
        if(right){//capture the right number
            int tempPos=pos+pattern.length();
            int end = expString.length();
            boolean endB = false;
            while(true){//skip other unary
                char tempChar=expString.charAt(tempPos);
                if(!isNum(tempChar)||tempChar==' '){
                    tempPos++;
                    if (tempPos==end){
                        endB=true;
                        break;
                    }
                    continue;
                }
                break;//find a number
            }
            while(!endB){
                char tempChar=expString.charAt(tempPos);
                if(isNum(tempChar)){
                    tempPos++;
                    if (tempPos==end){
                        break;
                    }
                    continue;
                }
                break;//not a number
            }
            expression.o.parameters[1]=expString.substring(pos+pattern.length(),tempPos);
            expression.suffix=expString.substring(tempPos);
        }else{
            expression.suffix=expString.substring(pos+pattern.length());
        }
        expression.pending=expString.substring(expression.prefix.length(),expString.length()-expression.suffix.length());
    }
    public static void loadSelfBracketLike(String expString,Expression expression,String pattern,int pos1){// ?(...)
        int pos2 = pos1+1;
        int delta = 1;
        while (true) {
            char tempChar=expString.charAt(pos2);
            if(tempChar=='(')delta++;
            if(tempChar==')')delta--;
            if(delta==0)break;
            pos2++;
        }
        expression.o.parameters[0]=expString.substring(pos1+1, pos2);
        expression.prefix=expString.substring(0,pos1-pattern.length());//cut alpha
        if(pos2+1>=expString.length()){
            //System.err.println("end..");
            expression.suffix="";
        }else{
            expression.suffix=expString.substring(pos2+1);
        }
        expression.pending=expString.substring(expression.prefix.length(),expString.length()-expression.suffix.length());
    }
    public static void loadSelfCommaIncluded(String expString,Expression expression,String pattern,int commaCount,int pos1){//?(,...,)
        int posComma[] = new int[commaCount];
        int delta = 1;
        posComma[0]=pos1+1;
        boolean end = false;
        int pos2=0;
        for(int i=0;i<commaCount;i++){
            if(i>0)posComma[i]=posComma[i-1]+1;
            while (true) {
                char tempChar=expString.charAt(posComma[i]);
                if(tempChar=='(')delta++;
                if(tempChar=='[')delta++;
                if(tempChar==')')delta--;
                if(tempChar==']')delta--;
                if(delta==1&&tempChar==','){
                    break;
                }
                if(delta==0)break;
                posComma[i]++;
            }
            if(delta==0){
                end=true;
                pos2=posComma[i];
                posComma[i]=-1;
                break;
            }
        }
        if(!end)pos2 = posComma[commaCount-1]+1;
        while (!end) {
            char tempChar=expString.charAt(pos2);
            if(tempChar=='(')delta++;
            if(tempChar==')')delta--;
            if(delta==0)break;
            pos2++;
        }
        int posCount;
        expression.o.parameters[0]=expString.substring(pos1+1,posComma[0]);
        for(posCount=0;posCount<commaCount-1;posCount++){
            try{
                expression.o.parameters[posCount+1]=expString.substring(posComma[posCount]+1,posComma[posCount+1]);
            }catch(Exception e){
                break;
            }
        }
        expression.o.parameters[posCount+1]=expString.substring(posComma[posCount]+1,pos2);
        expression.prefix=expString.substring(0,pos1-pattern.length());//cut alpha
        if(pos2+1>=expString.length()){
            //System.err.println("end..");
            expression.suffix="";
        }else{
            expression.suffix=expString.substring(pos2+1);
        }
        expression.pending=expString.substring(expression.prefix.length(),expString.length()-expression.suffix.length());
    }
    public static void loadSelfArrayIncluded(String expString,Expression expression,String pattern,boolean left,boolean right,int pos){
        if(left){
            int tempPos=pos-1;
            boolean end=false;
            //type
            while(true){
                if(tempPos<0){
                    end=true;
                    break;
                }
                char tempChar = expString.charAt(tempPos);
                if(tempChar==' '){
                    tempPos--;
                    continue;
                }//else:
                if(tempChar==']'){//先遇到矩阵
                    int rightPos=tempPos+1;
                    int delta=1;
                    while(delta!=0){
                        tempPos--;
                        if(expString.charAt(tempPos)==']')delta++;
                        if(expString.charAt(tempPos)=='[')delta--;
                    }//tempPos>>'['
                    expression.o.parameters[2]="array";
                    expression.o.parameters[0]=expString.substring(tempPos, rightPos);
                    expression.prefix=expString.substring(0, tempPos);
                    break;
                }else{//先遇到数字
                    int rightPos=tempPos+1;
                    while(tempPos>=0&&isNum(expString.charAt(tempPos))){
                        tempPos--;
                    }//tempPos+1>>num
                    tempPos++;//tempPos>>num
                    expression.o.parameters[2]="num";
                    expression.o.parameters[0]=expString.substring(tempPos, rightPos);
                    expression.prefix=expString.substring(0, tempPos);
                    break;
                }
            }
            if(end){
                expression.o.parameters[2]="num";
                expression.o.parameters[0]="0";
                expression.prefix=expString.substring(0, pos);
            }
        }else{
            expression.prefix=expString.substring(0, pos);
        }
        if(right){
            int tempPos=pos+pattern.length();
            boolean end = false;
            int len = expString.length();
            while (true) {
                if(tempPos==len){
                    end=true;
                    break;
                }
                char tempChar = expString.charAt(tempPos);
                if(tempChar==' '){
                    tempPos++;
                    continue;
                }
                if(tempChar=='['){//先遇到矩阵
                    int delta=1;
                    int leftPos=tempPos;
                    while(delta!=0){
                        tempPos++;
                        if(expString.charAt(tempPos)=='[')delta++;
                        if(expString.charAt(tempPos)==']')delta--;
                    }
                    tempPos++;//tempPos-1>>]
                    expression.o.parameters[3]="array";
                    expression.o.parameters[1]=expString.substring(leftPos, tempPos);
                    expression.suffix=expString.substring(tempPos);
                    break;
                }else{//先遇到数字
                    int leftPos=tempPos;
                    while (tempPos<len&&isNum(expString.charAt(tempPos))) {
                        tempPos++;
                    }//tempPos-1>>num
                    expression.o.parameters[3]="num";
                    expression.o.parameters[1]=expString.substring(leftPos,tempPos);
                    expression.suffix=expString.substring(tempPos);
                    break;
                }
            }
            if(end){
                expression.o.parameters[3]="num";
                expression.o.parameters[1]="0";
                expression.suffix=expString.substring(pos+1);
            }
        }else{
            expression.suffix=expString.substring(pos+1);
        }
    }
    public static void generateArrays(){
        for(int i=0;i<operators.size();i++){
            //patternStrings,regexs
            String regex="";
            patternStrings.add(new ArrayList<>());
            for (String className : Operator.operators.get(i)){
                try{
                    Class<?> Op = Class.forName(operatorPath+className);
                    String pattern = (String)Op.getDeclaredField("pattern").get(null);
                    patternStrings.get(i).add(pattern);
                    String piece="";
                    for(char c:pattern.toCharArray()){
                        piece+=String.format("[%s]", c);
                    }
                    //boolean tempBracketLike=(Boolean)Op.getDeclaredField("bracketLike").get(null);
                    //boolean tempLeft=(Boolean)Op.getDeclaredField("left").get(null);
                    //boolean tempRight=(Boolean)Op.getDeclaredField("right").get(null);
                    
                    regex+=String.format("|%s(%s)%s",nonOpRegex, piece,nonOpRegex);
                }catch(Exception e){
                    //System.out.println("sth wrong when generating regex arrays");
                }
            }
            regex=regex.substring(1);
            System.out.println(regex);
            Pattern pat = Pattern.compile(regex);
            patterns.add(pat);
        }
    }
    public static String[] stringToArray(String str){
        int n=0,delta=0,len=str.length();//m行n列
        int pos=0;
        char[] chars = str.toCharArray();
        List<String> tempStrings = new ArrayList<>();
        for(int i=0;i<len;i++){
            if(chars[i]=='[')delta++;
            if(chars[i]=='['&&delta==1){//开始
                pos=i;
            }
            if((chars[i]==','||chars[i]==']')&&delta==1){//向量的一个元素
                tempStrings.add(str.substring(pos+1, i));
                pos=i;
            }
            if(chars[i]==']')delta--;
        }
        n=tempStrings.size();
        String[] array = new String[n];
        for(int j=0;j<n;j++)array[j]=tempStrings.get(j);

        return array;
    }
    public static String arrayToString(String[] array){
        String output=String.format("[%s]", String.join(" , ", array));//[ , , , ]
        return output;
    }
    public static String[][] arrayToMatrix(String[] array){
        int m=array.length;
        List<String[]> tempStringss= new ArrayList<>();
        for(int i=0;i<m;i++){
            tempStringss.add(stringToArray(array[i]));
        }
        String[][] output = new String[m][tempStringss.size()];
        int index=0;
        for(String[] tempStrings:tempStringss)output[index++]=tempStrings;
        return output;
    }
    public static String matrixToString(String[][] matrix) {
        List<String> tempStrings = new ArrayList<>();
        for(String[] array:matrix){
            tempStrings.add(String.format("[%s]", String.join(" , ", array)));
        }
        String output = String.format("[%s]", String.join(" , ", tempStrings));
        return output;
    }
}