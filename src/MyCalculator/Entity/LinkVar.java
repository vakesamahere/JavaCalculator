package MyCalculator.Entity;

import javax.swing.JTextArea;

public class LinkVar extends RegistedVar {

    public LinkVar(VariableRigisterLabel father,JTextArea target,String sign) {
        super(father);
        father.getVarList().add(this);
        father.getPerList().add(this);
        valueText.setText(sign);
        valueText.setEnabled(false);
        deleteButton.setEnabled(false);
        valueText=target;
    }
    public void setName(String input){
        name=input;
        nameText.setText(input);
    }
    
}
