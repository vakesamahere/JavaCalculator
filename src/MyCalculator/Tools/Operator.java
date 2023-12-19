package mycalculator.tools;

import java.text.NumberFormat;

public abstract class Operator {
    protected final static String pattrnFix = "&";
    protected String[] parameters = new String[5];
    protected static NumberFormat nf = NumberFormat.getInstance();
    /**在子类final，一些需要在反射里获取*/
    public static String pattern;
    public static boolean bracketlike;
    public static boolean left;
    public static boolean right;
    /**return a string(join the original expression) of the result*/
    public abstract String solve();

    public Operator(){
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(20);
    }
    public void setPa(int i,String value){
        parameters[i]=value;
    }

}