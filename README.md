# JavaCalculator
******************使用说明书******************
==========================================================================================
0.目录
    1.概念与功能区简介
    2.计算面板
    3.变量面板
        3.1.预设变量&注册变量
        3.2.使用方式
    4.表达式输入面板
        4.1.输入框
        4.2.软键盘
        4.3.热键
    5.实现的运算符
        5.1.预设运算符
    6.日志面板
    7.进度条
    8.帮助
    9.*自定义函数
==========================================================================================
1.概念与功能区简介
    1.1.使用计算器进行计算时，有可能涉及：
        A.运算符，如四则运算符1+6，括号运算符(a)，取对数运算符log(a,b)等
        B.变量，如x,y。常常是需要赋值的占位符，或是待解的未知数。
    1.2.计算器有下列功能区：
        A.表达式输入区域：几乎所有的文本框。输入的表达式会被解析
        B.变量注册表，位于主面板的左下方。用于创建可以被计算器识别的变量名称并编辑其代表的表达式
        C.计算结果显示区域，位于主面板上半部分的左侧。显示计算结果和统计结果
        D.图形结果显示区域，在计算完有关作图的任务后自动显示。显示函数图像。
        E.表达式编辑器&软键盘，点击变量旁边的"..."显示。用于更加高效地输入表达式。
        F.计算日志显示区域，位于主面板的右下方。显示一些计算的中间步骤。
        G.进度条，位于主面板中央。显示大型计算任务的进度，也是人为终止计算的入口。
==========================================================================================
2.计算面板
    计算面板是主面板进度条上方的部分，提供了主要表达式输入、结果显示、数值计算、函数作图与统计等功能。操作方式及注意事项如下：
        A.在Expression输入框中输入合法的表达式，点击solve按钮，计算完成后Result文本框中将显示计算结果。
        B.在Expression输入框中输入关于未知数的表达式，在From/To中输入未知数遍历的下界和上界，在Accu中输入遍历途中走的步数，在Vari中输入未知数的名称，点击table按钮，计算完成后Result中将显示表达式在区域内的零点、极值、最值。并显示一个绘制了散点图的坐标系。
        C.使用table功能时，若在四个输入框中有留空，该框将采用默认值，分别为[下界=-1;上界=1;步数=1000;未知数=x]
        D.使用solve功能时，可以使用任何非法的字符分隔多个表达式，使程序同时计算多个结果；使用table功能时，若有 同时做多个函数图像的需求，应使用分号;分隔多个表达式，而变量名称应统一。
        E.选中Expression中的一段，再点击相应按钮，可实现表达式中的局部计算。
==========================================================================================
3.变量面板
    变量面板是进度条下方左侧的区域，可以注册若干变量用于计算。
    3.1.注册变量&预设变量
        >>>>计算器中允许用户创建新的变量，给变量取名(如t)，赋值(如3.14+e+sin(y))。这些变量将会在开始计算时自动代入到被计算的表达式中。预设变量是系统自动注册的不可删除的变量，有e，π，RES，前两个是常用的无理常数，不可修改名称，RES自动引用当前的计算结果。
        考虑一些运算符中定义的临时变量，如sum(x,1,10,x*2)，临时变量不需要注册，且必须和注册变量的名称不重复。
    3.2.操作方法
        >>>>点击New Var按钮，将会在变量注册表中添加一行。在左端文本框输入变量名，在右端文本框输入变量表达式。点击"..."将显示对话框，也可用于编辑变量的值。点击del按钮可删除单个变量。点击上下按钮可以按喜好移动变量的上下次序。其中变量的上下次序一般情况下不会影响计算结果。
        点击constVars按钮，将会清除预设变量除外的所有变量。
    3.3.注意
        >>>>谨慎使用变量嵌套，避免出现引用的死循环，如x=2*x；x=y,y=z,z=x等。切勿错把多行变量用来解多元方程。
==========================================================================================
4.表达式输入面板
    表达式输入面板为除了e和π每一个变量设置，点击变量右侧的"..."显示的对话框和软键盘正属于表达式输入面板。
    4.1.输入框
        >>>>输入框是与主面板中的小文本框互通文本的对话框，此区域较大，可以更加方便地输入、更直观地浏览表达式。
    4.2.软键盘
        >>>>键盘窗口对于所有实现的运算符，都实现了按钮点击输入。
    4.3.热键
        >>>>在输入对话框的输入中，允许用户使用热键一键输入较长的内容，如ctrl+z实现撤销、按一次ctrl后输入A可以输入arcsin()等较高级输入设置。通过按一次ctrl后输入相应的内容(软键盘界面按钮下方[]内的字符)，或是ctrl+`(键盘上1左边的按钮)来切换"热键优先"模式，该模式下无需按ctrl，直接按相应字符，便可输入相应的表达式内容。
        >>>>Tab和Shift+Tab可以快速前往右/左的下一个数字。
        >>>>Ctrl+Z/Ctrl+Shift+Z可以进行撤销和重做
==========================================================================================
5.实现的运算符
    计算器实现了35种运算符：字母越小优先级越低
        A ...其他字符
        A 00.Array 向量 [a,b,c,d,...] 列向量
        B 01.Plus 加法 a+b;
        B 02.Minus 减法 a-b;
        B 03.ArrayPlus 向量加法 [a,b]+[c,d]=[a+c,b+d] 要求左右长度相等
        B 04.ArrayMinus 向量减法 [a,b]-[c,d]=[a-c,b-d] 要求左右长度相等
        C 05.Multiplication 乘法 a*b;
        C 06.Division 除法 a/b
        C 07.MatrixMultiplication/ArrayMultiplication 矩阵乘法/向量数乘 [[a11,a12,a13],[a21,a22,a23]]*[[b11,b12],[b21,b22],[b31,b32]] 要求左端列数=右端行数; [a,b,c]*d= [a*d,b*d,c*d] 
        C 08.ArrayDivision 向量除以数字 [a,b]/c=[a/c,b/c] 要求c!=0
        D 09.Power 幂 a^b 要求a<=0时b>=0，ab不同时为0
        D 10.MatrixPower 矩阵的乘方 [Matrix]^n要求n是整数且Matrix是方阵
        E 11.Percent 百分 % a%=a/100
        F 12.Bracket 括号运算 (Expression)让Expression的优先级等于当前括号的优先级
        F 13.NaturalExpFunction 自然底数的幂 exp(a)=e^a
        F 14.NaturalLogFunction 自然底数对数 ln(a)要求a>0
        F 15.Sine 正弦 sin(a)
        F 16.Cisine 余弦 cos(a)
        F 17.Tangent 正切 tan(a)要求a!=(2*n+1)*π/2,n是任意整数
        F 18.AntiSine 反正弦 arcsin(a)要求-π/2<=a<=π/2
        F 19.AntiCosine 反余弦 arccos(a)要求-π/2<=a<=π/2
        F 20.AntiTangent 反正切 arctan(a)要求-π/2<a<π/2
        F 21.Logarithm 对数 log(a,b)=ln(b)/ln(a)要求ab均大于0
        F 22.AbsoluteValue 绝对值 abs(a)
        F 23.RowJoint 向量对应行合并 jr([[a,b],[c,d]],[[e,f],[g,h]],(...))=[[a,b,e,f],[c,d,g,h]] 要求各向量列数相同
        F 24.ColJoint 向量对应列合并 jc([[a,b],[c,d]],[[e,f],[g,h]],(...))=[[a,b],[c,d],[e,f],[g,h]] 要求各向量行数相同
        F 25.Determinant 行列式 det([Matrix]) 要求Matrix是方阵
        F 26.Inverse 逆 inv([Matrix]) 要求det([Matrix])!=0
        F 27.Transposition 转置 trans([[a,b]])=[[a],[b]];trans([a,b,c])=trans([[a],[b],[c]])=[[a,b,c]]
        F 28.Mean 数学期望 E([array])
        F 29.Variance 方差 D([array])
        F 30.Covariance 协方差 cov([arrayX],[arrayY])
        G 31.DefiniteIntegral 定积分 inte(a,b,fx,x[,n])下界为a，上界为b，被积函数fx，微分形式dx。x为临时变量；步数n可选，默认为1000，必须为整数
        G 32.Accumulation 解析数列求和 sum(x,a,b,fx)从a到b步长为1将x代入fx求和，x为临时变量。要求a不大于b且ab为整数
        G 33.Multiplicative 解析数列求积 mul(x,a,b,fx)从a到b步长为1将x代入fx求积，x为临时变量。要求a不大于b且Gab为整数
        G 34.ArrayAccumulation 随机数列求和 asum(x,[array],fx) x为临时变量
        G 35.ArrayMultiplicative 随机数列求积 amul(x,[array],fx) x为临时变量
==========================================================================================
6.日志面板
    >>>>位于主面板的右下方。显示一些计算的中间步骤。
==========================================================================================
7.进度条
    >>>>进度条，位于主面板中央。显示大型计算任务的进度，也是人为终止计算的入口。
    在进行定积分、解方程等需要遍历较长序列的过程中，进度条会启动，此时点击进度条将停止计算线程。在较为简短的运算任务中，进度条不会被调用，而只会在显示结果的时候将进度显示为100% 
==========================================================================================
8.帮助
    >>>>将鼠标放在部分控件上，在主面板的底部将显示相关说明信息。
==========================================================================================
9.*自定义函数
    >>>>仅是变量注册器的应用之一
    示例：定义协方差函数。注册变量p,赋值cov(x,y)/(D(x)^0.5*D(y)^0.5);再注册x=[array],y=[array],注意x y和p内变量名称的对应，将x y放在p的下方，在expression中输入p便可以计算x和y的相关系数。