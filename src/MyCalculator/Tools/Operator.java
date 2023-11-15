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
            "DefiniteIntegral","Sum","Multiplicative"
        ),
        Arrays.asList(
            "Bracket","Exp","Ln","Sin","Cos","Tan","Log","Arcsin","Arccos","Arctan"
        ),
        Arrays.asList(
            "Percent"
        ),
        Arrays.asList(
            "Power"
        ),
        Arrays.asList(
            "Time",
            "Divide"
        ),
        Arrays.asList(
            "Add",
            "Minus"
        )
    );
    public static List<List<String>> patternStrings=new ArrayList<>();
    public static List<Pattern> patterns=new ArrayList<>();

    public java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
    public String parameters[] = new String[5];//record parameters need to be operated
    public static String pattern;
    public static String[] punish;
    public static boolean bracketlike;
    public static boolean left;
    public static boolean right;
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
    public abstract String solve();//return a string(join the original expression) of the result
    public static void loadSelfSingle(String expString,Expression expression,String pattern,boolean left,boolean right,int pos){// (...)?(...)
        //single operation
        if(left){//capture the left number
            int tempPos=pos;
            boolean end = false;
            //if(pos==0)find=false;
            while(true){//skip other unary
                char tempChar=expString.charAt(tempPos);
                if(!isNum(tempChar)){
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
            while(true){
                char tempChar=expString.charAt(tempPos);
                if(isNum(tempChar)){
                    tempPos++;
                    if (tempPos==end){
                        expression.o.parameters[1]=expString.substring(pos+pattern.length());
                        expression.suffix="";
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
                if(tempChar==')')delta--;
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
            //System.out.println(regex);
            Pattern pat = Pattern.compile(regex);
            patterns.add(pat);
        }
    }
}