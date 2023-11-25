package MyCalculator.Tools.Operators;
import MyCalculator.Lobby;
import MyCalculator.Entity.Expression;
import MyCalculator.Tools.Calculator;
import MyCalculator.Tools.Operator;
public class Covariance extends Operator {
    public final static String pattern = "cov";
    public final static boolean bracketlike=true;
    public final static boolean left = true;
    public final static boolean right = true;
    public final static int commaCount = 1;
    public Covariance(){
    }
    public String solve(){
        //System.err.println("Solve:"+parameters[0]);
        String inputX = Calculator.cal(parameters[0]);
        String inputY = Calculator.cal(parameters[1]);
        String[] arrayX = Operator.stringToArray(inputX);
        String[] arrayY = Operator.stringToArray(inputY);
        Double meanX,meanY,sum=0.0;
        Mean m = new Mean();
        m.parameters[0]=inputX;
        meanX=Double.parseDouble(m.solve());
        m.parameters[0]=inputY;
        meanY=Double.parseDouble(m.solve());
        for(int i=0;i<arrayX.length;i++){
            sum+=(Double.parseDouble(Calculator.cal(arrayX[i]))-meanX)*(Double.parseDouble(Calculator.cal(arrayY[i]))-meanY);
        }
        sum/=arrayX.length;
        String output = nf.format(sum);
        //Lobby.getCovarianceDisplayer().addCovariance(String.format("[Output]Covariance(%s,%s)=%s", num1,num2,output));
        
        return output;
    }
    public static void loadSelf(String expString,Expression expression,int index){
        
        expression.o = new Covariance();
        Operator.loadSelfCommaIncluded(expString, expression,pattern,commaCount,index+pattern.length());
    }
}
