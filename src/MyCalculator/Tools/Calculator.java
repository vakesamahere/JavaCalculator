package mycalculator.tools;

import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.regex.*;

import mycalculator.Lobby;
import mycalculator.entity.Expression;
import mycalculator.entity.ProgressBar;
import mycalculator.entity.Variable;
import mycalculator.tools.Calculator;
import mycalculator.tools.Operators.*;

import java.util.List;
import java.util.ArrayList;

public class Calculator {
    private static boolean remainVar;
    private static boolean execute;
    private static NumberFormat nf = NumberFormat.getInstance();
    public static String calString(String expString){//预处理用户输入的算式字符串
        expString = "("+expString+")";
        //variables
        remainVar=true;
        execute=false;
        //留存变量
        while(remainVar){
            execute=false;
            remainVar=false;
            expString=dealOperator(expString);
            expString=Lobby.getConstVarMgr().replaceVars(expString);
            expString=dealOperator(expString);
            if(!execute){
                break;
            }
        }
        //)->)' '
        expString = expString.substring(1,expString.length()-2);
        Lobby.getLogDisplayer().addLog("\n********\n"+expString+"\n********\n");

        return expString;
    }
    public static String dealOperator(String expString){
        //\t\n
        expString=expString.replace("\t"," ");
        expString=expString.replace("\n"," ");
        //Minus,Power,',','()'
        expString=expString.replaceAll("([\\(\\[,][\\s]?)-", "$1"+"0-");
        expString=expString.replace("-", Minus.pattern);
        expString=expString.replace("^", Power.pattern);
        expString=expString.replace("arr∏", ArrayMultiplicative.pattern);
        expString=expString.replace("arr∑", ArraySum.pattern);
        expString=expString.replace("∫", DefiniteIntegral.pattern);
        expString=expString.replace("∑", Sum.pattern);
        expString=expString.replace("∏", Multiplicative.pattern);
        expString=expString.replace(",", " , ");
        expString=expString.replace("(", "( ");
        expString=expString.replace("[", "[ ");
        expString=expString.replace("]", " ] ");
        expString=expString.replace(")", " ) ");
        //abs
        //e,pi
        expString = expString.replaceAll("([^A-za-z0-9])[e]([^A-za-z0-9])", String.format("%s%s%s","$1",String.valueOf(Math.E),"$2"));
        expString = expString.replaceAll("([^A-za-z0-9])[p][i]([^A-za-z0-9])", String.format("%s%s%s","$1",String.valueOf(Math.PI),"$2"));
        //Normal Bracket
        expString = expString.replaceAll("([^A-Za-z]{1})[(]", "$1B(");
        //multi space
        expString = expString.replaceAll("[\\s]+", " ");
        return expString;

    }
    public static List<Double>[] listGen(String expString,String varName,Double start,Double end,int n){//生成x，y列表
        
        List<Double>[] outputs = new List[2];
        Variable va = new Variable(varName,"&");
        Double unit = (end-start)/n;
        Double x=start;
        expString=replaceVar(String.format(" %s ", expString),va);

        Double unitIcre = 100.0/n;
        Double progress=0.0;
        ProgressBar bar = Lobby.getProgressBar();
        bar.setProgress(0.0);
        List<Double> xs = new ArrayList<>();
        List<Double> ys = new ArrayList<>();
        //******************************************************
        Lobby.getLogDisplayer().highFreqCalStart();
        for(int i=0;i<n;i++){
            progress+=unitIcre;
            bar.setProgress(progress);
            xs.add(x);
            String tempString=expString.replaceAll("&",nf.format(x));
            ys.add(Double.parseDouble(cal(tempString)));
            x+=unit;
        }
        outputs[0]=xs;
        outputs[1]=ys;
        Lobby.getLogDisplayer().highFreqCalEnd();
        //******************************************************
        return outputs;
    }
    /**预处理后进行单次运算*/
    public static String cal(String expString){
        Expression expression = new Expression();
        boolean flag = getOperator(expString,expression);
        if (flag) {
            Lobby.getLogDisplayer().addLog(String.format("\nCAL:%s {%s} %s", expression.prefix,expression.pending,expression.suffix).replace(" ", ""));
            Lobby.getLogDisplayer().addLog(String.format("With operator:%s",expression.opPat));
            return cal(String.format("%s%s%s", expression.prefix,expression.o.solve(),expression.suffix));
        }
        else{//纯数字
            expString=expString.replaceAll(" ", "");
            return expString;
        }
    }
    public static boolean getOperator(String expString,Expression expression){//字符串中获得最优先的运算符
        //for each operator whose level higher than the times of examination:examine every char
        for(int i=0;i<Operator.operators.size();i++){
            Matcher matcher = Operator.patterns.get(i).matcher(expString);
            if(matcher.find()){
            }else{
                continue;
            }
            //found an operator
            String opPattern=matcher.group();
            opPattern=opPattern.substring(1,opPattern.length()-1);
            expression.opPat=opPattern;
            int posInList = Operator.patternStrings.get(i).indexOf(opPattern);
            String className=Operator.operators.get(i).get(posInList);
            int pos = matcher.start()+1;
            try{
                Class<?> op=Class.forName(Operator.operatorPath+className);
                Method method = op.getDeclaredMethod("loadSelf", String.class,Expression.class,int.class);
                method.invoke(null,expString,expression,pos);
            }catch(Exception e){
                e.printStackTrace();
                continue;
            }
            return true;
        }
        return false;
    }
    public static String replaceVar(String expString,Variable va){
        execute=true;
        String regex="";
        for(char c:va.getName().toCharArray()){
            regex+=String.format("[%s]", c);
        }
        regex=String.format("([^A-za-z0-9])%s([^A-za-z0-9])", regex);
        Pattern pat = Pattern.compile(regex);
        Matcher mat = pat.matcher(expString);
        if(mat.matches()){
            remainVar=true;
        }
        String replaced = va.getValue();
        //do it twice to avoid (given x=1 and x+x+x+x->1+x+1+x)
        expString=mat.replaceAll(String.format("%s%s%s","$1",replaced,"$2"));
        mat = pat.matcher(expString);
        expString=mat.replaceAll(String.format("%s%s%s","$1",replaced,"$2"));
        return expString;

    }
    /**获得数据*/
    public static void analysis(
        List<Double> xs,
        List<Double> ys,

        List<Double> rootsX,
        List<Double> maxsX,
        List<Double> minsX,
        List<Double> extsX,
        List<Double> mostsX,
        List<Double> rootsY,
        List<Double> maxsY,
        List<Double> minsY,
        List<Double> extsY,
        List<Double> mostsY
        ) {//找到解，极值，最值
        int len=ys.size();
        Double max;
        Double min;
        int posMax;
        int posMin;
        if(ys.get(0)<ys.get(len-1)){
            max=ys.get(len-1);
            posMax=len-1;
            min=ys.get(0);
            posMin=0;
        }else{
            max=ys.get(0);
            posMax=0;
            min=ys.get(len-1);
            posMin=len-1;
        }

        rootsY.clear();
        maxsY.clear();
        minsY.clear();
        extsY.clear();

        rootsX.clear();
        maxsX.clear();
        minsX.clear();
        extsX.clear();

        int pos;
        int times = len-2;
        boolean rootFounded=false;
        for(pos=1;pos<times;pos++){
            Double left = ys.get(pos-1);
            Double self = ys.get(pos);
            Double right = ys.get(pos+1);
            boolean lUp =(left-self<0);
            boolean rUp =(right-self>0);
            Double absSelf= Math.abs(self);
            if(!rootFounded&&((Math.abs(left)-absSelf>=0)&&(Math.abs(right)-absSelf>=0))&&((left>0)^(right>0))){
                rootsY.add(self);
                rootsX.add(xs.get(pos));
                rootFounded=true;
            }
            //extreme value
            if(lUp^rUp){
                //极大值
                if(lUp){
                    maxsY.add(self);
                    maxsX.add(xs.get(pos));
                    //最大值
                    if(self>max){
                        max=self;
                        posMax=pos;
                    }
                    //极小值
                }else{
                    minsY.add(self);
                    minsX.add(xs.get(pos));
                    //最小值
                    if(self<min){
                        min=self;
                        posMin=pos;
                    }
                }
                extsY.add(self);
                extsX.add(xs.get(pos));
                int extSize = extsY.size();
                if(extSize<=1){
                    rootFounded=false;
                }else if (((self>=0)^(extsY.get(extSize-2)>=0))){
                    rootFounded=false;
                }
            }
        }
        mostsY.add(max);
        mostsY.add(min);
        mostsX.add(xs.get(posMax));
        mostsX.add(xs.get(posMin));
    }
}
