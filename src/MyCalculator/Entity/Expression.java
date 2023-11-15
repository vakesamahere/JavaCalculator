package MyCalculator.Entity;

import MyCalculator.Tools.Operator;

public class Expression {
    public String prefix;
    public String suffix;
    public String pending;
    public String opPat;
    public Operator o;
    public int level;
    public Expression(){
    }
}