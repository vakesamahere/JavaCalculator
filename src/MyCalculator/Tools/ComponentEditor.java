package mycalculator.tools;

import javax.swing.*;
import java.awt.*;

public class ComponentEditor {
    public static void initializeComponentBody(JComponent self,JComponent father,double posX,double posY,double sizeX,double sizeY){//set the relative position and the relative size to the container
        
        father.add(self);
        Rectangle rectangle = father.getBounds();
        double width = rectangle.width;
        double height = rectangle.height;
        self.setBounds((int)(posX*width),(int)(posY*height),(int)(sizeX*width),(int)(sizeY*height));
    }
    public static void refreshBar(JScrollBar bar){
        bar.revalidate();
    }
    public static void gbcAdd(Component c,int x,int y,int w,int h,int wx,int wy,GridBagConstraints gbc,Component co){
        gbc.gridx=x;
        gbc.gridy=y;
        gbc.gridwidth=w;
        gbc.gridheight=h;
        gbc.weightx=wx;
        gbc.weighty=wy;
        Container con =(Container)co;
        con.add(c,gbc);
    }

}
