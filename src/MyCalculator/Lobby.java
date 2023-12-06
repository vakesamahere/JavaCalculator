package mycalculator;
import javax.swing.*;

import mycalculator.entity.*;
import mycalculator.tools.ComponentEditor;
import mycalculator.tools.Operator;

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
    public static Keyboard keyboard=new Keyboard();

    private static JPanel formPanel = new JPanel();
    private static CalculatorPanel calculatorPanel = new CalculatorPanel();
    private static ProgressBar progressBar = new ProgressBar();
    
    private static LogDisplayer logDisplayer = new LogDisplayer("log");
    private static JLabel help = new JLabel("HELP");
    private static VariableRigisterLabel constVariablesManager;
    
    public Lobby(String name){
        super(name);
        initialize();
        initializeComponent();
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
        LinkVar resRegVar = new LinkVar(constVariablesManager, calculatorPanel.getVarRes().getValueArea(), "Result");
        resRegVar.setName("RES");
        //
        refreshComponent();
        Help.helpLabel=help;
    }
    private void refreshComponent(){
        loadFont();
        ComponentEditor.initializeComponentBody(progressBar, formPanel, 0,0.34,1,0.05);
        ComponentEditor.initializeComponentBody(calculatorPanel,formPanel,0,0,1,0.35);
        formPanel.setBounds(0,0,getSize().width,getSize().height);
        ComponentEditor.initializeComponentBody(constVariablesManager, formPanel, 0,0.39,0.6,0.52);
        ComponentEditor.initializeComponentBody(logDisplayer.getPanel(), formPanel, 0.6,0.39,0.4,0.52);
        ComponentEditor.initializeComponentBody(help, formPanel, 0,0.87,1,0.1);
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
            //this.getSize().height-(target.getLocation().y+target.getSize().height-1+delta)
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
    public static void useKeyBoard(ExpressionEditor eed){
        eed.setKeyboard(keyboard);
        keyboard.setParent(eed);
    }
    public static void main(String[] args){
        Operator.generateArrays();
        Lobby lobby = new Lobby("❤CAL❤CU❤LA❤TOR❤");
        Help.genReflact();
        lobby.setVisible(true);
    }
}