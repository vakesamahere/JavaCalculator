package MyCalculator.Tools;
import MyCalculator.Lobby;
import MyCalculator.Entity.Expression;
import MyCalculator.Entity.ProgressBar;
import MyCalculator.Entity.Variable;
import MyCalculator.Tools.Operators.*;

import java.lang.reflect.Method;
import java.util.regex.*;
import java.util.List;
import java.util.ArrayList;

public class Calculator {
    public static String calString(String expString){
        expString = "("+expString+")";
        //\t\n
        expString=expString.replace("\t"," ");
        expString=expString.replace("\n"," ");
        //Minus,Power,',','()'
        expString=expString.replace("{", "(");
        expString=expString.replace("[", "(");
        expString=expString.replace("（", "(");
        expString=expString.replace("}", ")");
        expString=expString.replace("]", ")");
        expString=expString.replace("）", ")");
        expString=expString.replace("-", " "+Minus.pattern+" ");
        expString=expString.replace("^", " "+Power.pattern+" ");
        expString=expString.replace(",", " , ");
        expString=expString.replace("(", "( ");
        expString=expString.replace(")", " ) ");//
        //variables
        expString=Lobby.getConstVarMgr().replaceVars(expString);
        //e,pi
        expString = expString.replaceAll("([^A-za-z0-9])[e]([^A-za-z0-9])", String.format("%s%s%s","$1",String.valueOf(Math.E),"$2"));
        expString = expString.replaceAll("([^A-za-z0-9])[p][i]([^A-za-z0-9])", String.format("%s%s%s","$1",String.valueOf(Math.PI),"$2"));
        //Normal Bracket
        expString = expString.replaceAll("([^A-Za-z]{1})[(]", "$1B(");
        //if (expString.charAt(0)=='m')expString="0"+expString;

        expString = expString.substring(1,expString.length()-2);//)->)' '
        Lobby.getLogDisplayer().addLog("\n********\n"+expString+"\n********\n");

        return expString;
    }
    public static List<Double>[] diaGen(String expString,String varName,Double start,Double end,int n){
        List<Double>[] outputs = new List[2];
        Variable va = new Variable(varName,"&");
        Double unit = (end-start)/n;
        Double x=start;
        expString=replaceVar(String.format(" %s ", expString),va);
        System.err.println(expString);

        Double unitIcre = 100.0/n;
        Double progress=0.0;
        ProgressBar bar = Lobby.getProgressBar();
        bar.setProgress(0.0);
        List<Double> xs = new ArrayList<>();
        List<Double> ys = new ArrayList<>();
        //******************************************************
        Lobby.getLogDisplayer().highFreq++;
        for(int i=0;i<n;i++){
            progress+=unitIcre;
            bar.setProgress(progress);
            xs.add(x);
            String tempString=expString.replaceAll("&",x.toString());
            ys.add(Double.parseDouble(cal(tempString)));
            //System.err.println(String.format("%s %s", temp[0],temp[1]));
            x+=unit;
        }
        outputs[0]=xs;
        outputs[1]=ys;
        Lobby.getLogDisplayer().highFreq--;
        //******************************************************
        return outputs;
    }
    public static String cal(String expString){//预处理后
        //System.err.println("\nCAL:"+expString);
        Expression expression = new Expression();
        boolean flag = getOperator(expString,expression);
        if (flag) {
            Lobby.getLogDisplayer().addLog(String.format("\nCAL:%s {%s} %s", expression.prefix,expression.pending,expression.suffix).replace(" ", ""));
            Lobby.getLogDisplayer().addLog(String.format("With operator:%s",expression.opPat));
            return cal(String.format("%s%s%s", expression.prefix,expression.o.solve(),expression.suffix));//solve:operator(cal(string1),cal(string2))
        }
        else{//纯数字
            expString=expString.replaceAll(" ", "");
            return expString;
        }
    }
    public static boolean getOperator(String expString,Expression expression){
        //for each operator whose level higher than the times of examination:examine every char
        for(int i=0;i<Operator.operators.size();i++){
            Matcher matcher = Operator.patterns.get(i).matcher(expString);
            if(matcher.find()){
                //System.out.println("Found AN operator:" + matcher.group());
            }else{
                //System.out.println("Found no operator..");
                continue;
            }
            //found an operator
            String opPattern=matcher.group();
            opPattern=opPattern.substring(1,opPattern.length()-1);
            expression.opPat=opPattern;
            //System.err.println(opPattern);
            int posInList = Operator.patternStrings.get(i).indexOf(opPattern);
            String className=Operator.operators.get(i).get(posInList);
            int pos = matcher.start()+1;
            try{
                Class<?> op=Class.forName(Operator.operatorPath+className);//getClass
                //System.out.println("loading..");
                Method method = op.getDeclaredMethod("loadSelf", String.class,Expression.class,int.class);//getMethod
                method.invoke(null,expString,expression,pos);//run
            }catch(Exception e){
                System.out.println(e);
                continue;
            }
            return true;
        }
        return false;
    }
    public static String replaceVar(String expString,Variable va){
        String regex="";
        for(char c:va.getName().toCharArray()){
            regex+=String.format("[%s]", c);
        }
        regex=String.format("([^A-za-z0-9])%s([^A-za-z0-9])", regex);
        Pattern pat = Pattern.compile(regex);
        Matcher mat = pat.matcher(expString);
        //do it twice to avoid (given x=1 and x+x+x+x->1+x+1+x)
        expString=mat.replaceAll(String.format("%s%s%s","$1",va.getValue(),"$2"));
        //Lobby.getLogDisplayer().addLog(String.format("Replacement Completed:",expString));
        mat = pat.matcher(expString);
        expString=mat.replaceAll(String.format("%s%s%s","$1",va.getValue(),"$2"));
        //Lobby.getLogDisplayer().addLog(String.format("Replacement Completed:",expString));
        return expString;

    }
}
