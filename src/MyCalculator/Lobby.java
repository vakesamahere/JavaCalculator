package mycalculator;
import javax.swing.*;

import mycalculator.entity.*;
import mycalculator.tools.Calculator;
import mycalculator.tools.ComponentEditor;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Lobby extends JFrame{
    /**settings*/
    public static final double formSizeRatio = 0.75;
    private static int formatFontSize;
    private static int smallFormatFontSize;
    private static int signFontSize;
    
    public static Font formatFont;
    public static Font signFont;
    public static Font boldSignFont;
    public static Font smallFormatFont;
    /**component*/
    private static ExpressionEditor expressionEditor = new ExpressionEditor();
    private static KeyboardPanel keyboardPanel=new KeyboardPanel();
    private static JPanel formPanel = new JPanel();
    private static JPanel mainPanel = new JPanel();
    private static CalculatorPanel calculatorPanel = new CalculatorPanel();
    private static ProgressBar progressBar = new ProgressBar();
    
    private static LogDisplayer logDisplayer = new LogDisplayer("log");
    private static JLabel help = new JLabel("HELP");
    private static VariableRigisterLabel constVariablesManager;
    
    public Lobby(String name){
        super(name);
        initialize();
        initializeComponent();
        calculatorPanel.getVarExp().select();
        addComponentListener(new ComponentAdapter(){
            @Override
            public void componentResized(ComponentEvent e) {
                refreshComponent();
            }
        });
    }
    private void initialize(){
        this.calSrceenSize(formSizeRatio);
        loadFont();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }
    private void initializeComponent(){
        //mainPanel
        add(formPanel);
        formPanel.setLayout(null);

        formPanel.setBounds(0,0,getSize().width,getSize().height);

        mainPanel.setLayout(null);
        mainPanel.setMinimumSize(new Dimension(1000,1000));

        constVariablesManager= new VariableRigisterLabel("constVars", mainPanel, 0,0.4,1,0.65);
        //
        LinkVar resRegVar = new LinkVar(constVariablesManager, calculatorPanel.getVarRes().getValueArea(), "Result");
        resRegVar.setName("RES");
        //
        refreshComponent();
        Help.helpLabel=help;
    }
    private void refreshComponent(){
        loadFont();
        formPanel.setBounds(0,0,getSize().width,getSize().height);

        ComponentEditor.initializeComponentBody(keyboardPanel, formPanel, 0, 0.35, 0.35, 0.575);
        ComponentEditor.initializeComponentBody(expressionEditor, formPanel, 0, 0, 0.35, 0.35);
        ComponentEditor.initializeComponentBody(mainPanel, formPanel, 0.35, 0, 0.65, 1);

        ComponentEditor.initializeComponentBody(progressBar, mainPanel, 0,0.34,1,0.05);
        ComponentEditor.initializeComponentBody(calculatorPanel,mainPanel,0,0,1,0.35);
        
        ComponentEditor.initializeComponentBody(constVariablesManager, mainPanel, 0,0.39,0.6,0.52);
        ComponentEditor.initializeComponentBody(logDisplayer.getPanel(), mainPanel, 0.6,0.39,0.4,0.52);
        ComponentEditor.initializeComponentBody(help, mainPanel, 0,0.88,1,0.1);
        //stick
        progressBar.setLocation(new Point(0,calculatorPanel.getLocation().y+calculatorPanel.getSize().height));
        compStickUp(constVariablesManager,progressBar,0);
        compStickUp(logDisplayer.getPanel(), progressBar,30);
    }
    private void compStickUp(JComponent self,JComponent target,int delta){
        self.setBounds(new Rectangle(
            self.getLocation().x,
            target.getLocation().y+target.getSize().height-1,
            self.getWidth(),
            self.getHeight()
        ));
    }
    private void loadFont(){
        formatFontSize = calFontSize(20);
        smallFormatFontSize = calFontSize(10);
        signFontSize = calFontSize(15);

        formatFont = new Font("Microsoft Yahei", Font.PLAIN, formatFontSize);
        boldSignFont = new Font("Microsoft Yahei", Font.BOLD, signFontSize);
        signFont = new Font("Microsoft Yahei", Font.PLAIN, signFontSize);
        smallFormatFont = new Font("Microsoft Yahei", Font.PLAIN, smallFormatFontSize);
    }
    private void calSrceenSize(double ratio){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)(ratio*screenSize.getWidth());
        int height = (int)(ratio*screenSize.getHeight());
        this.setSize(width,height);
    }
    private int calFontSize(int size){
        double realSize=size*Math.min(this.getHeight(),this.getWidth())/540;
        return (int)realSize;
    }
    public static VariableRigisterLabel getConstVarMgr(){
        return constVariablesManager;
    }
    public static CalculatorPanel getCalculatorPanel(){
        return calculatorPanel;
    }
    public static ProgressBar getProgressBar(){
        return progressBar;
    }
    public static LogDisplayer getLogDisplayer(){
        return logDisplayer;
    }
    public static ExpressionEditor getExpressionEditor(){
        return expressionEditor;
    }
    public static void useKeyBoard(ExpressionEditor eed){
        //把keyboard的链接对象改为传入的表达式
        
    }
    public static void main(String[] args){
        Calculator.generateArrays();
        Lobby lobby = new Lobby("❤CAL❤CU❤LA❤TOR❤");
        Help.genReflact();
        Calculator.initializeNf();
        lobby.setVisible(true);
    }
}