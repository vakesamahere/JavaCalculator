package mycalculator.entity;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JDialog;
import javax.swing.JPanel;

import mycalculator.Lobby;

import java.util.ArrayList;
import java.util.List;
import java.text.NumberFormat;

public class DiagramDisplayer extends JDialog {
    private static final double formSizeRatio = 0.3;
    private static final double panelSizeRatioX = 0.9;
    private static final double panelSizeRatioY = 0.7;
    private DrawPanel drawPanel;
    private Dimension selfSize=null;
    private static final String title = "Diagram Displayer";
    private static final NumberFormat nf = NumberFormat.getInstance();

    private List<List<Double>[]> inputss;
    //List:[ResultA:[Xs:[Double,...],Ys:[...]],ResultB:[Xs:[...],Ys:[...]],...]

    public DiagramDisplayer(){
        drawPanel=new DrawPanel();
        setTitle(title);
        setLayout(new GridLayout(1,1));
        add(drawPanel);
        calSrceenSize(formSizeRatio);
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseMove();
            }
        });
    }
    public void calSrceenSize(double ratio){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)(ratio*screenSize.getWidth());
        int height = (int)(ratio*screenSize.getHeight());
        setSize(width,height);
    }
    public void refreshSize(){
        selfSize = getSize();
        drawPanel.center = new Point(selfSize.width/2,(int)(selfSize.height*0.45));
        selfSize = new Dimension((int)(selfSize.width*panelSizeRatioX),(int)(selfSize.height*panelSizeRatioY));
        drawPanel.size=new Point(selfSize.width,selfSize.height);
        drawPanel.calLimit(inputss);
        drawPanel.calRatio();
        drawPanel.calSize(inputss);
        drawPanel.calR();
    }
    public void setInputss(List<List<Double>[]> li){
        inputss=li;
    }
    private void mouseMove(){        
        int pos=MouseInfo.getPointerInfo().getLocation().x - getLocationOnScreen().x;
        int index = drawPanel.calPosMouse(pos);
        if(index<0||index>=drawPanel.arrayLen){
            return;
        }
        List<String> ys = new ArrayList<>();
        for(List<Double>[] inputs:inputss){
            ys.add(nf.format(inputs[1].get(index)));
        }
        String output="";
        output+=String.format("||x=%.4f||", inputss.get(0)[0].get(index));
        output+=String.format("y=%s", String.join("; ",ys));
        setTitle(title+output);
    }
}
class DrawPanel extends JPanel{
    final int tickCount = 5;
    final NumberFormat nf = NumberFormat.getInstance();
    Point size=null;
    Point center = null;
    Double xlimit=1.0;
    Double ylimit=1.0;
    Double xRatio;
    Double yRatio;
    int posxLeft;
    int posxRight;
    int posDelta;
    int arrayLen;
    int n=10;
    int r=7;
    List<Color> colors = new ArrayList<>();
    final int oR=7;
    List<List<int[]>> pointss = new ArrayList<>();
    public DrawPanel(){
        colors.add(Color.red);
        colors.add(Color.blue);
        colors.add(Color.green);
        colors.add(Color.yellow);
        colors.add(Color.pink);
        colors.add(Color.gray);
    }
    @Override
    public void paint(Graphics g){
        super.paint(g);
        Lobby.getCalculatorPanel().getDiagramDisplayer().refreshSize();
        g.setColor(Color.BLACK);
        //axeX
        g.drawLine(center.x-size.x/2, center.y,center.x+size.x/2,center.y);
        //axeY
        g.drawLine(center.x,center.y-size.y/2, center.x, center.y+size.y/2);
        for(int i=0-tickCount;i<=tickCount;i++){
            g.drawString(nf.format(xlimit*i/tickCount), center.x+size.x*i/tickCount/2, center.y);
            g.drawString(nf.format(-ylimit*i/tickCount), center.x, center.y+size.y*i/tickCount/2);
        }
        for(List<int[]> points : pointss){
            g.setColor(colors.get(pointss.indexOf(points)%colors.size()));
            drawPoints(g,points.get(0),points.get(1));
        }
        
    }
    public int calPosMouse(int pos){
        return (pos-posxLeft)*arrayLen/posDelta;
    }
    public void calSize(List<List<Double>[]> inputss) {
        pointss = new ArrayList<>();
        for(List<Double>[] inputs:inputss){
            List<Double> xs=inputs[0];
            List<Double> ys=inputs[1];
            int len=xs.size();
            arrayLen=len;
            int[] outxs = new int[len];
            int[] outys = new int[len];
            int i;
            posxLeft=transferToPosX(xs.get(0));
            for(i=0;i<len;i++){
                outxs[i]=transferToPosX(xs.get(i));
                outys[i]=transferToPosY(ys.get(i));
            }
            posxRight=transferToPosX(xs.get(i-1));
            posDelta=posxRight-posxLeft;
            //drawPoints(getGraphics(), outxs, outys);
            List<int[]> temp = new ArrayList<>();
            temp.add(outxs);
            temp.add(outys);
            pointss.add(temp);
        }
    }
    public void calLimit(List<List<Double>[]> inputss){
        Double maxY = 0.0;
        Double maxX = 0.0;
        for(List<Double>[] lis:inputss){
            int xsLen = lis[0].size();
            Double tempX=Math.abs(lis[0].get(0));
            if((tempX)>maxX){
                maxX=tempX;
            }
            tempX=Math.abs(lis[0].get(xsLen-1));
            if((tempX)>maxX){
                maxX=tempX;
            }

            for(Double x:lis[1]){
                Double temp=Math.abs(x);
                if(temp>maxY){
                    maxY=temp;
                }
            }
        }
        xlimit=maxX;
        ylimit=maxY;
    }
    public void calR() {
        r=Math.min((int)(transferToPosX(xlimit)/2/pointss.get(0).get(0).length),oR);
        r=Math.max(r,2);
        //System.out.println(String.format("r=%s", r));
    }
    public void calRatio(){
        xRatio=size.x/xlimit/2;
        yRatio=-size.y/ylimit/2;
    }
    public Point transferToPos(Double x,Double y){
        int posx = (int)(xRatio*x+center.x);
        int posy = (int)(yRatio*y+center.y);
        return new Point(posx,posy);
    }
    public int transferToPosX(Double x){
        int res=(int)(xRatio*x+center.x);
        return res;
    }
    public int transferToPosY(Double y){
        int res = (int)(yRatio*y+center.y);
        return res;
    }
    public void line(Graphics g,Double xa,Double ya,Double xb,Double yb){
        g.drawLine(transferToPosX(xa),transferToPosY(ya),transferToPosX(xb),transferToPosY(yb));
    }
    public void drawPoints(Graphics g,int[]xs,int[]ys){
        int len=xs.length;
        for(int i=0;i<len;i++){
            g.fillOval(xs[i]-r/2,ys[i]-r/2,r,r);
        }
    }
}