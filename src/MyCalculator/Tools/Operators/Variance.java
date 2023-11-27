package MyCalculator.Tools.Operators;
import MyCalculator.Entity.Expression;
import MyCalculator.Tools.Calculator;
import MyCalculator.Tools.Operator;
public class Variance extends Operator {
    public final static String pattern = "D";
    public final static boolean bracketlike=true;
    public final static boolean left = true;
    public final static boolean right = true;
    public Variance(){
    }
    public String solve(){
        //System.err.println("Solve:"+parameters[0]);
        String input = Calculator.cal(parameters[0]);
        String[] array = Operator.stringToArray(input);
        Double sum=0.0;
        Double mean;
        Mean m = new Mean();
        m.parameters[0]=input;
        mean=Double.parseDouble(m.solve());
        for(String num:array){
            sum+=Math.pow((Double.parseDouble(Calculator.cal(num))-mean),2);
        }
        sum/=array.length;
        String output = nf.format(sum);
        //Lobby.getLogDisplayer().addLog(String.format("[Output]Variance(%s)=%s", num,output));
        
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        
        expression.o = new Variance();
        Operator.loadSelfBracketLike(expString, expression,pattern,index+pattern.length());
    }
}