package MyCalculator.Tools;
import javax.swing.event.*;
import java.util.ArrayList;
import java.util.List;
public class HistoryRecorder implements DocumentListener {
    private List<String> history;
    private int mode;
    public HistoryRecorder(){
        mode=0;
        history= new ArrayList<>();
    }
    @Override
    public void insertUpdate(DocumentEvent e) {
        
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        System.err.println(e.getType());
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        System.err.println(3);
    }
    
}
