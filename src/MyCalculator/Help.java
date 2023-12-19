package mycalculator;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JLabel;

import java.util.ArrayList;
import java.util.Arrays;
public class Help {
    public static Component expArea;
    public static Component resArea;
    public static Component solveBut;
    public static Component tableBut;
    public static Component fromField;
    public static Component toField;
    public static Component accuField;
    public static Component variField;
    public static Component progressBar;
    public static Component clearVars;
    public static Component newVars;
    public static Component logArea;
    public static List<Component> varName=new ArrayList<>();
    public static List<Component> varValue=new ArrayList<>();
    public static List<Component> varDelete=new ArrayList<>();
    public static List<Component> varModify=new ArrayList<>();
    public static List<Component> varUp=new ArrayList<>();
    public static List<Component> varDown=new ArrayList<>();

    public static List<Component> comps;
    public static List<List<Component>> compss;

    public static HelpSingleListener singleListener=new HelpSingleListener();
    public static HelpGroupListener groupListener = new HelpGroupListener();
    public static JLabel helpLabel;
    public static void genReflact(){
        compss=Arrays.asList(varName,varValue,varDelete,varModify,varUp,varDown);
        comps=Arrays.asList(
            expArea
            ,resArea
            ,solveBut
            ,tableBut
            ,fromField
            ,toField
            ,accuField
            ,variField
            ,progressBar
            ,clearVars
            ,newVars
            ,logArea
        );
        for(Component c:comps){
            c.addMouseListener(singleListener);
        }
        for(List<Component> cs:compss){
            for(Component c:cs){
                c.addMouseListener(groupListener);
            }
        }
    }
    public static List<String> singsSingle = Arrays.asList(
            "[被计算的表达式]在Expression输入框中输入合法的表达式，点击solve按钮，计算完成后Result文本框中将显示计算结果。"
            ,"[计算结果]显示计算结果和统计结果"
            ,"[开始计算]使用solve功能时，可以使用任何非法的字符分隔多个表达式，使程序同时计算多个结果"
            ,"[统计&画图&解方程]请确认在四个输入框中输入合法数值。若有同时做多个函数图像的需求，应使用分号;分隔多个表达式，而变量名称应统一。"
            ,"[左边界a]默认为-1。遍历操作的开始值，需小于右边界"
            ,"[右边界b]默认为1.遍历操作的结束值，需大于左边界"
            ,"[步长n]默认为1000。在左右边界之间等距离取n个点，进行遍历计算"
            ,"[被遍历变量x]默认为'x'。在表达式中，找到被遍历变量，将其赋值计算"
            ,"[进度条]显示当前的计算进度"
            ,"[清空变量]清除预设变量除外的所有变量"
            ,"[新建变量]在变量注册表中添加一行"
            ,"[日志]显示部分计算的中间结果"
    );
    public static List<String> singsGroup = Arrays.asList(
        "[变量识别名]表达式解析过程中会把识别名替换为变量的值"
        ,"[变量的值]可被解析的表达式。注意避免输入连环调用的变量组"
        ,"[删除本行]删除该变量"
        ,"[编辑]进入值编辑窗口，可以通过软键盘或快捷键更加方便地操作"
        ,"[上移]上移变量。注意，计算器的变量解析顺序为从上到下依次赋值，循环至表达式没有变量为止。"
        ,"[下移]下移变量。注意，计算器的变量解析顺序为从上到下依次赋值，循环至表达式没有变量为止。"
    );
    public static String helpDefault = "[Help]";
}
class HelpSingleListener implements MouseListener{

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        int pos = Help.comps.indexOf(e.getComponent());
        if(pos<0)return;
        Help.helpLabel.setText(Help.singsSingle.get(pos));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Help.helpLabel.setText(Help.helpDefault);
    }
    
}
class HelpGroupListener implements MouseListener{

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        for(List<Component> cs:Help.compss){
            if(cs.contains(e.getComponent())){
                int pos = Help.compss.indexOf(cs);
                Help.helpLabel.setText(Help.singsGroup.get(pos));
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Help.helpLabel.setText(Help.helpDefault);
    }
    
}