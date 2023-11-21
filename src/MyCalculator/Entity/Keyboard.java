package MyCalculator.Entity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class Keyboard extends JDialog{
    private Key[] keys = new Key[30];
    private List<Character> hotKeys = new ArrayList<>();
    private JTextArea output;
    private ExpressionEditor parent;
    public Keyboard(ExpressionEditor eed){
        parent=eed;
        output=parent.textArea;
        setLayout(new GridLayout(0,5));
        iniKeys();
        this.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                output.setForeground(Color.BLACK);
            }
            @Override
            public void windowLostFocus(WindowEvent e) {
                output.setForeground(Color.WHITE);
            }
        
        });
        // 获取JDialog的根面板
        JRootPane rootPane = getRootPane();
        // 创建一个Action对象，用于处理键盘事件
        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 在这里写上您想要执行的动作，例如关闭对话框
                dispose();
            }
        };
        // 为Action对象设置一个名称，用于绑定
        String actionName = "close";
        // 将Action对象添加到根面板的ActionMap中
        rootPane.getActionMap().put(actionName, action);
        // 创建一个KeyStroke对象，用于表示键盘事件，例如按下Esc键
        KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        // 将KeyStroke对象和Action对象的名称绑定到根面板的InputMap中
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, actionName);
    }
    public void iniKeys() {
        newKey(0, "+", "+", '+', 1);
        newKey(1, "-", "-", '-', 1);
        newKey(2, "x", "*", '*', 1);
        newKey(3, "÷", "/", '/', 1);

        newKey(4, "^", "^", '^', 1);
        newKey(5, "%", "%", '%', 1);
        newKey(6, "()", "()", '(', 1);
        newKey(7, "abs", "abs()", '\\', 4);

        newKey(8, "exp", "exp()", 'E', 4);
        newKey(9, "ln", "ln()", 'L', 3);
        newKey(10, "log", "log(,)", ' ', 4);
        newKey(11, "π", "π", 'P', 1);

        newKey(12, "sin", "sin()", 'S', 4);
        newKey(13, "cos", "cos()", 'C', 4);
        newKey(14, "tan", "tan()", 'T', 4);
        newKey(15, "∑", "∑(,,,)", '<', 2);

        newKey(16, "arcsin", "arcsin()", '!', 7);
        newKey(17, "arccos", "arccos()", '@', 7);
        newKey(18, "arctan", "arctan()", '#', 7);
        newKey(19, "∏", "∏(,,,)", '>', 2);

        newKey(20, "∫", "∫(,,,)", 'I', 2);
        
    }
    public void newKey(int index,String name,String va,char key,int cOffset){
        keys[index]=new Key(name,va,key,cOffset,this);
        add(keys[index]);
        hotKeys.add(key);
    }
    public void addValue(String value,int offset) {
        int pos = output.getCaretPosition();
        output.insert(value, pos);
        output.setCaretPosition(pos+offset);
    }
    public boolean keyTyped(char keyChar) {
        int index=hotKeys.indexOf(keyChar);
        if(index==-1)return false;
        keys[index].click();
        return true;
    }
}
