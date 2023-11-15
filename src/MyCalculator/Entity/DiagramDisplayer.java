package MyCalculator.Entity;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JDialog;
import javax.swing.JPanel;

import MyCalculator.Lobby;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DiagramDisplayer extends JDialog implements ComponentListener {
    private static final double formSizeRatio = 0.3;
    private static final double panelSizeRatioX = 0.9;
    private static final double panelSizeRatioY = 0.7;
    private DrawPanel drawPanel;
    private Dimension selfSize=null;

    public List<List<Double>[]> inputss = new ArrayList<>();
    //List:[ResultA:[Xs:[Double,...],Ys:[...]],ResultB:[Xs:[...],Ys:[...]],...]

    public DiagramDisplayer(){
        drawPanel=new DrawPanel();
        this.setTitle("Diagram Displayer");
        this.setLayout(new GridLayout(1,1));
        this.add(drawPanel);
        calSrceenSize(formSizeRatio);
        this.addComponentListener(this);
    }
    public void calSrceenSize(double ratio){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)(ratio*screenSize.getWidth());
        int height = (int)(ratio*screenSize.getHeight());
        this.setSize(width,height);
    }
    public void refreshSize(){
        selfSize = this.getSize();
        drawPanel.center = new Point(selfSize.width/2,(int)(selfSize.height*0.45));
        selfSize = new Dimension((int)(selfSize.width*panelSizeRatioX),(int)(selfSize.height*panelSizeRatioY));
        drawPanel.size=new Point(selfSize.width,selfSize.height);
        drawPanel.calLimit(inputss);
        drawPanel.calRatio();
        drawPanel.calSize(inputss);
        drawPanel.calR();
    }

    @Override
    public void componentResized(ComponentEvent e) {
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
}
class DrawPanel extends JPanel{
    Point size=null;
    Point center = null;
    Double xlimit=1.0;
    Double ylimit=1.0;
    Double xRatio;
    Double yRatio;
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
        //System.out.println(String.format("(%s %s %s %s %s %s)", xRatio,yRatio,xlimit,ylimit,center.x,center.y));
        g.setColor(Color.BLACK);
        g.drawLine(center.x-size.x/2, center.y,center.x+size.x/2,center.y);//axeX
        g.drawLine(center.x,center.y-size.y/2, center.x, size.y+size.y/2);//axeY
        for(List<int[]> points : pointss){
            g.setColor(colors.get(pointss.indexOf(points)));
            drawPoints(g,points.get(0),points.get(1));
        }
        
    }
    public void calSize(List<List<Double>[]> inputss) {
        pointss = new ArrayList<>();
        for(List<Double>[] inputs:inputss){
            List<Double> xs=inputs[0];
            List<Double> ys=inputs[1];
            int len=xs.size();
            int[] outxs = new int[len];
            int[] outys = new int[len];
            for(int i=0;i<len;i++){
                outxs[i]=transferToPosX(xs.get(i));
                outys[i]=transferToPosY(ys.get(i));
                //System.err.println(String.format("[x,y][%s]%s %s>>>%s %s",i,xs[i],ys[i], outxs[i],outys[i]));
            }
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
            if((tempX)>maxX)maxX=tempX;
            tempX=Math.abs(lis[0].get(xsLen-1));
            if((tempX)>maxX)maxX=tempX;

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
        //System.err.println(String.format("[x]%s*%s+%s=%s",xRatio,x,center.x,res));
        return res;
    }
    public int transferToPosY(Double y){
        int res = (int)(yRatio*y+center.y);
        //System.err.println(String.format("[y]%s*%s+%s+%s",yRatio,y,center.y,res));
        return res;
    }
    public void line(Graphics g,Double xa,Double ya,Double xb,Double yb){
        g.drawLine(transferToPosX(xa),transferToPosY(ya),transferToPosX(xb),transferToPosY(yb));
    }
    public void drawPoints(Graphics g,int[]xs,int[]ys){
        int len=xs.length;
        for(int i=0;i<len;i++){
            //System.out.println(String.format("%s %s %s", xs[i],ys[i],r));
            g.fillOval(xs[i]-r/2,ys[i]-r/2,r,r);
        }
    }
}