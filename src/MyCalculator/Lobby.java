package MyCalculator;
//主窗口
import javax.swing.*;

import MyCalculator.Entity.CalculatorPanel;
import MyCalculator.Entity.LogDisplayer;
import MyCalculator.Entity.ProgressBar;
import MyCalculator.Entity.VariableRigisterLabel;
import MyCalculator.Tools.ComponentEditor;
import MyCalculator.Tools.Operator;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class Lobby extends JFrame implements ComponentListener{
    //settings
    private static final double formSizeRatio = 0.5;
    private static int formatFontSize;
    private static int smallFormatFontSize;
    private static int signFontSize;
    //
    public static Font formatFont;
    public static Font signFont;
    public static Font boldSignFont;
    public static Font smallFormatFont;
    //component
    private static JPanel formPanel = new JPanel();
    private static CalculatorPanel calculatorPanel = new CalculatorPanel();
    private static ProgressBar progressBar = new ProgressBar();
    
    private static LogDisplayer logDisplayer = new LogDisplayer("log");
    private static VariableRigisterLabel constVariablesManager;
    //
    public Lobby(String name){
        super(name);
        initialize();
        initializeComponent();
        //this.setVisible(true);
        this.addComponentListener(this);
    }
    public void initialize(){
        this.calSrceenSize(formSizeRatio);
        loadFont();
        //this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }
    public void initializeComponent(){
        //formPanel
        formPanel.setLayout(null);
        formPanel.setVisible(true);
        this.add(formPanel);
        formPanel.setBounds(0,0,getSize().width,getSize().height);
        //panels
        ComponentEditor.initializeComponentBody(calculatorPanel,formPanel,0,0,1,0.35);
        //constVariables,tempVariables
        constVariablesManager= new VariableRigisterLabel("constVars", formPanel, 0,0.4,1,0.65);
        //
        refreshComponent();
    }
    public void refreshComponent(){
        loadFont();
        ComponentEditor.initializeComponentBody(progressBar, formPanel, 0,0.34,1,0.05);
        ComponentEditor.initializeComponentBody(calculatorPanel,formPanel,0,0,1,0.35);
        formPanel.setBounds(0,0,getSize().width,getSize().height);
        ComponentEditor.initializeComponentBody(constVariablesManager, formPanel, 0,0.37,0.6,0.65);
        ComponentEditor.initializeComponentBody(logDisplayer.getPanel(), formPanel, 0.6,0.37,0.4,0.65);
        //stick
        progressBar.setLocation(new Point(0,calculatorPanel.getLocation().y+calculatorPanel.getSize().height));
        compStickUp(constVariablesManager,progressBar,0);
        compStickUp(logDisplayer.getPanel(), progressBar,30);
    }
    public void compStickUp(JComponent self,JComponent target,int delta){
        self.setBounds(new Rectangle(
            self.getLocation().x,
            target.getLocation().y+target.getSize().height-1,
            self.getWidth(),
            this.getSize().height-(target.getLocation().y+target.getSize().height-1+delta)
        ));
    }
    public void loadFont(){
        formatFontSize = calFontSize(20);
        smallFormatFontSize = calFontSize(10);
        signFontSize = calFontSize(15);

        formatFont = new Font("Microsoft Yahei", Font.PLAIN, formatFontSize);
        boldSignFont = new Font("Microsoft Yahei", Font.BOLD, signFontSize);
        signFont = new Font("Microsoft Yahei", Font.PLAIN, signFontSize);
        smallFormatFont = new Font("Microsoft Yahei", Font.PLAIN, smallFormatFontSize);
    }
    public void calSrceenSize(double ratio){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)(ratio*screenSize.getWidth());
        int height = (int)(ratio*screenSize.getHeight());
        this.setSize(width,height);
    }
    public static VariableRigisterLabel getConstVarMgr(){
        return constVariablesManager;
    }
    public static CalculatorPanel getCalculatorPanel(){
        return calculatorPanel;
    }
    public int calFontSize(int size){
        double realSize=size*Math.min(this.getHeight(),this.getWidth())/540;
        return (int)realSize;
    }
    public static ProgressBar getProgressBar(){
        return progressBar;
    }
    public static LogDisplayer getLogDisplayer(){
        return logDisplayer;
    }
    @Override
    public void componentResized(ComponentEvent e) {
        if(e.getComponent()==this){
            //setVisible(false);
            refreshComponent();
            //setVisible(true);
        }
    }
    @Override
    public void componentMoved(ComponentEvent e) {
    }
    @Override
    public void componentShown(ComponentEvent e) {

    }
    @Override
    public void componentHidden(ComponentEvent e) {
    }
    public static void main(String[] args){
        Operator.generateArrays();
        Lobby lobby = new Lobby("❤CAL❤CU❤LA❤TOR❤");
        lobby.setVisible(true);
    }
}