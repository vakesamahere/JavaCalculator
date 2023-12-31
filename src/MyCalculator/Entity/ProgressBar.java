package mycalculator.entity;

import javax.swing.*;

import mycalculator.Help;
import mycalculator.Lobby;
import mycalculator.tools.ComponentEditor;

import java.awt.*;
import java.awt.event.*;

public class ProgressBar extends JPanel {
    private JPanel progressBarPanel;
    private JLabel progressBarLabel;
    private double progress;
    private static int gridCount=100;
    private static Color borderColor = Color.lightGray;
    private static Color remainColor = Color.white;
    private static Color completedColor = new Color(152,251,152);
    private static Color runningColor = new Color(210,210,210);
    private static Color sleepingColor = remainColor;
    private static final double ZERO = 1E-9;
    public ProgressBar() {
        setLayout(null);
        setBackground(remainColor);
        progressBarPanel = new JPanel();
        progressBarPanel.setBackground(remainColor);
        loadBarPanel(progressBarPanel);

        progressBarLabel = new JLabel("0.00%  (Click to Stop)");
        progressBarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        progressBarLabel.setBorder(BorderFactory.createLineBorder(borderColor,2));
        progressBarLabel.setOpaque(false);
        refreshComponent();
        addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent e) {
                refreshComponent();
            }
        });
        progressBarLabel.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e) {
                Lobby.getLogDisplayer().highFreqCalStatementReset();
                Lobby.getCalculatorPanel().stopAll();
                Lobby.getLogDisplayer().addLog("Calculation Interrupted.");
            }
        });
        Help.progressBar=progressBarLabel;
    }
    private void loadBarPanel(JPanel panel) {
        panel.setLayout(new GridLayout(1,0));
        for(int i=0;i<gridCount;i++){
            panel.add(new JPanel(), i);
        }
        for(Component comp:panel.getComponents()){
            comp.setBackground(remainColor);
            JComponent jComp = (JComponent)comp;
            jComp.setBorder(BorderFactory.createLineBorder(remainColor,1));
        }
    }
    public void updateProgress(double pro) {
        progress=pro;
        updateProgress();
    }
    public void addProgress(Double unitIcre) {
        progress+=unitIcre;
        updateProgress();
    }
    public void setProgress(Double pro) {
        progress=pro;
        updateProgress();
    }
    private void updateProgress() {
        progressBarLabel.setText(String.format("%.2f%%  (Click to Stop)", progress));
        double i=1E-9;
        for(Component comp:progressBarPanel.getComponents()){
            Color tempColor =progress<i?remainColor:completedColor;
            comp.setBackground(tempColor);
            JComponent jComp = (JComponent)comp;
            jComp.setBorder(BorderFactory.createLineBorder(tempColor,1));
            i++;
        }
        progressBarPanel.setBackground(progress>ZERO?runningColor:sleepingColor);
        this.repaint();
    }

    private void refreshComponent() {
        ComponentEditor.initializeComponentBody(progressBarLabel, this, 0,0,1,1);
        ComponentEditor.initializeComponentBody(progressBarPanel, this, 0,0,1,1);
        progressBarLabel.setFont(Lobby.boldSignFont);
    }
}
