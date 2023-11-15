package MyCalculator.Entity;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import MyCalculator.Lobby;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class ExpressionCollector extends JDialog implements DocumentListener,FocusListener {
    private static final double formSizeRatio = 0.2;
    JTextArea textArea;
    JScrollPane scrollPane;
    Variable target;
    Boolean passive;
    public ExpressionCollector(Variable va){
        target = va;
        calSrceenSize(formSizeRatio);
        this.setLocationRelativeTo(null);
        textArea= new JTextArea();
        textArea.setFont(Lobby.formatFont);
        textArea.setLineWrap(true);
        scrollPane = new JScrollPane(textArea);
        this.add(scrollPane);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        Document doc = textArea.getDocument();
        doc.addDocumentListener(this);
        textArea.addFocusListener(this);
    }
    public void calSrceenSize(double ratio){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)(ratio*screenSize.getWidth());
        int height = (int)(ratio*screenSize.getHeight());
        this.setSize(width,height);
    }
    public JTextArea getTextArea(){
        return textArea;
    }
    @Override
    public void insertUpdate(DocumentEvent e) {output();}
    @Override
    public void removeUpdate(DocumentEvent e) {output();}
    @Override
    public void changedUpdate(DocumentEvent e) {output();}
    public void output(){
        target.getValueArea().setText(textArea.getText());
    }
    @Override
    public void focusGained(FocusEvent e) {
        textArea.setText(target.getValueArea().getText());
        textArea.setForeground(Color.BLACK);
        this.setTitle("Input the value of "+target.getName());
    }
    @Override
    public void focusLost(FocusEvent e) {
        textArea.setForeground(Color.WHITE);
    }
}
