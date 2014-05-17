package GUI;

import equationevaluator.*;
import Graph.GraphPanel;
import Graph.LineGraph;
import Graph.MouseMovementListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The GUI the user interacts with to create graphs.
 * @author michael
 */
public class MainField implements ChangeListener {
    /*TODO:
     */
    
    //Hold all the graphs that have been graphed
    ArrayList<LineGraph> lineGraphs = new ArrayList<>();
    int EQNum = 0; //the number of graphs
    int RootGraph = 0; //the current root graph
    
    JPanel Canvas = new JPanel(); //the canvas graphs are ddrawn to
    
    JTabbedPane Controls = new JTabbedPane();
    JScrollPane CanvasWrapper = new JScrollPane(Canvas);
    
    JFrame Frame = new JFrame(); //the main frame, everything is placed on this
    
    //Card layout used for graph controls
    CardLayout Cards = new CardLayout();
    JPanel EquationCardLayout = new JPanel(Cards);
    String CurrentEQ;
    int CurrentEQNum = 0;
    
    //Options for setting domain and range:
    JSpinner MinXSpin = new JSpinner(new SpinnerNumberModel(-10, -1000000, 1000000, 0.1));
    double MinX = -10;
    JSpinner MaxXSpin = new JSpinner(new SpinnerNumberModel(10, -1000000, 1000000, 0.1));
    double MaxX = 10;
    JSpinner MaxYSpin = new JSpinner(new SpinnerNumberModel(-10, -1000000, 1000000, 0.1));
    double MaxY = 10;
    JSpinner MinYSpin = new JSpinner(new SpinnerNumberModel(10, -1000000, 1000000, 0.1));
    double MinY = -10;
    
    //the equation generates an array of points, this determines how close together they are
    JSpinner StepSpin = new JSpinner(
            new SpinnerNumberModel(0.1, 0.001, 10000, 0.001));
    double Step = 0.1;
    
    //line thickness
    JSpinner CurveThicknessSpin = new JSpinner(
            new SpinnerNumberModel(1, 1, 10, 0.1));
    float CurveThickness = 1;
    
    
    JSpinner EquationNum = new JSpinner();
    Color BackColor = new JPanel().getBackground();
    Color LineColor = Color.red;
    
    //The background and line colours
    JColorChooser LineCC = new JColorChooser(LineColor);
    JColorChooser BackGCC = new JColorChooser(BackColor);
    
    //Whether or not to force the range or let the program figure it out
    JCheckBox ForceRangeBox = new JCheckBox("Force Range");
    boolean ForceRange = false;
    
    //Determines the number of labels (and increment lines) to place on each axis
    JSpinner IncSpin = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
    int Increments = 10;
    
    //Determiens whether to colour the area between the line and the axis
    JCheckBox DrawAreaBox = new JCheckBox("Draw area under curve");
    boolean DrawArea = false;
    
    //whether or not to draw a scale at all
    JCheckBox DrawScaleBox = new JCheckBox("Label Scale");
    boolean DrawScale = true;
    
    //handles zoom
    JSlider ZoomSlider = new JSlider(JSlider.HORIZONTAL,
            1, 80, 7);
    float Zoom = 0.7f;

    /**
     * Updates the variables with the ones on the GUI spinners, checkboxers etc
     */
    public void getGUIVALS() {
        MinX = (double) MinXSpin.getValue();
        MaxX = (double) MaxXSpin.getValue();
        MinY = (double) MinYSpin.getValue();
        MaxY = (double) MaxYSpin.getValue();
        Step = (double) StepSpin.getValue();
        ForceRange = ForceRangeBox.isSelected();
        LineColor = LineCC.getColor();
        BackColor = BackGCC.getColor();
        Increments = (int) IncSpin.getValue();
        DrawArea = DrawAreaBox.isSelected();
        DrawScale = DrawScaleBox.isSelected();
        CurveThickness = (float) (double) CurveThicknessSpin.getValue();
    }

    /**
     * Adds components to <Frame> and displays it
     */
    public void Build() {
        //Equation input
        JPanel EquationPanel = new JPanel();
        EquationPanel.setLayout(new BoxLayout(EquationPanel, BoxLayout.Y_AXIS));
        JPanel EqButtons = new JPanel();
        JButton Add = new JButton("Add");
        Add.addActionListener(new GraphActionListener(this, null));
        JButton Clear = new JButton("Clear");
        Clear.addActionListener(new GraphActionListener(this, null));
        EqButtons.add(Add);
        EqButtons.add(Clear);
        JPanel CurrentEq = new JPanel();
        CurrentEq.add(new JLabel("Display Equation:"));
        CurrentEq.add(EquationNum);
        EquationNum.addChangeListener(this);
        AddNewGraph();
        EquationPanel.add(EqButtons);
        EquationPanel.add(CurrentEq);
        JPanel p = new JPanel();
        p.add(EquationCardLayout);
        EquationPanel.add(p);
        Controls.addTab("Equation", EquationPanel);

        //Range Input
        JPanel RangePanel = new JPanel();
        MinYSpin.setPreferredSize(new Dimension(100, 20));
        MaxYSpin.setPreferredSize(new Dimension(100, 20));
        MinYSpin.setEnabled(false);
        MaxYSpin.setEnabled(false);
        RangePanel.add(new JLabel("Range:"));
        RangePanel.add(ForceRangeBox);
        ForceRangeBox.addActionListener(new GraphActionListener(this, null));
        RangePanel.add(MaxYSpin);
        RangePanel.add(MinYSpin);
        //Domain input
        JPanel DRPanel = new JPanel();
        DRPanel.setLayout(new BoxLayout(DRPanel, BoxLayout.Y_AXIS));
        JPanel DomainPanel = new JPanel();
        MinXSpin.setPreferredSize(new Dimension(100, 20));
        MaxXSpin.setPreferredSize(new Dimension(100, 20));
        DomainPanel.add(new JLabel("Domain:"));
        DomainPanel.add(MinXSpin);
        DomainPanel.add(MaxXSpin);
        DRPanel.add(DomainPanel);
        DRPanel.add(RangePanel);
        Controls.addTab("Restrictions", DRPanel);



        //Zoom input
        JPanel ZoomPanel = new JPanel();
        JButton toDefault = new JButton();
        toDefault.addActionListener(new GraphActionListener(this, null));
        toDefault.setText("Defualt Zoom");
        ZoomPanel.add(ZoomSlider);
        ZoomSlider.addChangeListener(this);
        ZoomPanel.add(toDefault);
        Controls.addTab("Zoom", ZoomPanel);

        //colours input
        Controls.addTab("Line Colours", new JPanel());
        Controls.addTab("BackGround Colours", new JPanel());

        //Visuals input
        JPanel VisualsPanel = new JPanel();
        VisualsPanel.setLayout(new BoxLayout(VisualsPanel, BoxLayout.Y_AXIS));
        VisualsPanel.add(DrawAreaBox);
        VisualsPanel.add(DrawScaleBox);

        //Options input
        JPanel OptionsPanel = new JPanel();
        OptionsPanel.setLayout(new BoxLayout(OptionsPanel, BoxLayout.Y_AXIS));
        JPanel stepPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel la = new JLabel("Step:");
        stepPanel.add(la);
        stepPanel.add(StepSpin);
        stepPanel.add(DrawAreaBox);
        JPanel thicknessPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        thicknessPanel.add(new JLabel("Curve Thickness"));
        thicknessPanel.add(CurveThicknessSpin);
        stepPanel.add(thicknessPanel);
        JPanel inc = new JPanel(new FlowLayout(FlowLayout.LEFT));
        la = new JLabel("grid lines:");
        inc.add(la);
        inc.add(IncSpin);
        inc.add(DrawScaleBox);
        DrawScaleBox.setSelected(true);
        OptionsPanel.add(stepPanel);
        OptionsPanel.add(inc);
        Controls.addTab("Options", OptionsPanel);

        //instructions
        JPanel instrpanel = new JPanel();
        instrpanel.add(new JLabel("<html>Hover over the graph "
                + "to get the coordinates at that point"
                + "<br>and evaluation of the top most equation for the given x value."
                + "<br> Press Graph to graph the current equation and move it to the top with "
                + "<br>all settings. Press Bring To Top to move the graph to the top retaining"
                + "<br> it's previous settings"
                + "<br>All equations will follow standard BEDMAS rules."
                + "<br><br>Supported functions:"
                + "<br> Standard operations, +,-,/,*"
                + "<br> Exponents using ^ (2 <sup>x</sup> = 2^x)"
                + "<br>fac : takes the factorial of the value ahead (x! = facx)"
                + "<br>abs : takes the absolute value"
                + "<br>sin, cos, tan : trig functions"
                + "<br>log : log base ten of the value"));
        JScrollPane s = new JScrollPane(instrpanel);
        s.setPreferredSize(new Dimension(EquationPanel.getSize()));
        Controls.addTab("Help", s);
        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Frame.setSize(550, 650);

        BorderLayout layout = new BorderLayout();
        Frame.setLayout(layout);
        Controls.addChangeListener(this);

        Frame.setTitle("Graphing Calculator");
        String imagePath = "icon.png";
        InputStream imgStream = MainField.class.getResourceAsStream(imagePath);
        BufferedImage myImg;
        try {
            myImg = ImageIO.read(imgStream);
            Frame.setIconImage(myImg);
        } catch (IOException ex) {
        }
        Frame.add(Controls, BorderLayout.NORTH);
        Frame.add(CanvasWrapper, BorderLayout.CENTER);
        Frame.setVisible(true);
        Frame.validate();


    }

    /**
     * Creates a new set of GUI components to display a new graph, called often
     *  by the add button
     */
    public void AddNewGraph() {
        EQNum++;
        LineGraph line = new LineGraph();
        JButton GraphButton = new JButton("Graph");
        JButton MakeRoot = new JButton("Bring To Top");
        JTextField Eqinput = new JTextField();
        Eqinput.setPreferredSize(new Dimension(300, 20));
        JPanel EqPanel = new JPanel();
        EqPanel.add(new JLabel("f(x) ="));
        EqPanel.add(Eqinput);
        GraphActionListener g = new GraphActionListener(this, Eqinput);
        Eqinput.addActionListener(g);
        Eqinput.setActionCommand("Graph");
        EqPanel.add(GraphButton);
        EqPanel.add(MakeRoot);
        GraphButton.addActionListener(g);
        MakeRoot.addActionListener(g);
        EquationCardLayout.add(EqPanel, Integer.toString(EQNum));
        CardLayout c = (CardLayout) EquationCardLayout.getLayout();
        c.show(EquationCardLayout, Integer.toString(EQNum));
        EquationNum.setModel(new SpinnerNumberModel(EQNum, 1, EQNum, 1));
        lineGraphs.add(line);
    }

    /**
     * Removes all the graphs and there GUI components 
     */
    public void RemoveAllGraphs() {
        EquationCardLayout.removeAll();
        lineGraphs.clear();
        CurrentEQ = "";
        EQNum = 0;
        AddNewGraph();
        Canvas.removeAll();
        Canvas.repaint();
    }

    /**
     * Draws the current Graph onto the root graphs canvas, nothing but the exact line is 
     * overwritten
     */
    public void DrawGraphPassive() {
        if (CurrentEQ == null || CurrentEQ.isEmpty()) {
            return;
        }
        StringParser parser = new StringParser();
        Equation eq = parser.ParseString(CurrentEQ);

        LineGraph root = lineGraphs.get(RootGraph);
        Canvas.removeAll();
        Canvas.repaint();
        int h = Frame.getHeight();
        int w = Frame.getWidth();
        GraphPanel p;
        if (ForceRange) {
            p = (GraphPanel) root.drawToJPanel((int) (w * Zoom), (int) (h * Zoom),
                    Math.min(MinX, MaxX),
                    Math.min(MinY, MaxY),
                    Math.max(MinX, MaxX),
                    Math.max(MinY, MaxY));
            p.addMouseMotionListener(new MouseMovementListener(p, root, eq));
            Canvas.add(p);
        } else {
            p = (GraphPanel) root.drawToJPanel((int) (w * Zoom), (int) (h * Zoom));
            p.addMouseMotionListener(new MouseMovementListener(p, root, eq));
            Canvas.add(p);
            // Canvas.setBackground(Color.red);
        }

        Frame.validate();
        CenterScrollPane();
        lineGraphs.set(RootGraph, root);
        p.LineGraphs = lineGraphs;
    }

    /**
     *  Replaces whatever is in the Canvas with current graph
     */
    public void DrawGraphActive() {
        if (CurrentEQ == null || CurrentEQ.isEmpty()) {
            return;
        }
        StringParser parser = new StringParser();
        Equation eq = parser.ParseString(CurrentEQ);

        int size = (int) ((Math.max(MinX, MaxX) - Math.min(MinX, MaxX)) / Step);
        double[] points = new double[size * 2 + 2];
        int index = 0;
        for (double i = Math.min(MinX, MaxX); i <= Math.max(MinX, MaxX); i += Step) {
            points[index] = i;
            index++;
            points[index] = eq.peekAt("x", i);
            index++;
        }
        LineGraph root = new LineGraph(points);
        root.raw = CurrentEQ;
        root.setIncrements(Increments);
        root.setLineColour(LineColor);
        root.SetDrawArea(DrawArea);
        root.setShowNumbers(DrawScale);
        root.setBackColor(BackColor);
        root.setCurveThickness(CurveThickness);
        Canvas.removeAll();
        Canvas.repaint();
        int h = Frame.getHeight();
        int w = Frame.getWidth();
        GraphPanel p;
        if (ForceRange) {
            p = (GraphPanel) root.drawToJPanel((int) (w * Zoom), (int) (h * Zoom),
                    Math.min(MinX, MaxX),
                    Math.min(MinY, MaxY),
                    Math.max(MinX, MaxX),
                    Math.max(MinY, MaxY));
            p.addMouseMotionListener(new MouseMovementListener(p, root, eq));
            Canvas.add(p);
        } else {
            p = (GraphPanel) root.drawToJPanel((int) (w * Zoom), (int) (h * Zoom));
            p.addMouseMotionListener(new MouseMovementListener(p, root, eq));
            Canvas.add(p);
            // Canvas.setBackground(Color.red);
        }

        Frame.validate();
        CenterScrollPane();
        lineGraphs.set(RootGraph, root);
        p.LineGraphs = lineGraphs;
    }

    /**
     * Centers the view on the graph center
     */
    public void CenterScrollPane() {
        JScrollBar sc = CanvasWrapper.getHorizontalScrollBar();
        sc.setValue((sc.getMaximum() - CanvasWrapper.getWidth()) / 2);
        sc = CanvasWrapper.getVerticalScrollBar();
        sc.setValue((sc.getMaximum() - CanvasWrapper.getHeight()) / 2);
    }
    
    //Handles the zoom slider
    @Override
    public void stateChanged(ChangeEvent e) {
        if (Controls.getSelectedIndex() == 3) {
            Controls.setComponentAt(3, LineCC);
            Controls.setComponentAt(4, null);
        } else if (Controls.getSelectedIndex() == 4) {
            Controls.setComponentAt(3, null);
            Controls.setComponentAt(4, BackGCC);
        } else {
            Controls.setComponentAt(3, null);
            Controls.setComponentAt(4, null);
        }
        Zoom = ZoomSlider.getValue() / 10f;
        DrawGraphPassive();
        if ((int) EquationNum.getValue() != CurrentEQNum) {
            CurrentEQNum = (int) EquationNum.getValue();
            BackGCC.setColor(lineGraphs.get(CurrentEQNum - 1).getBackColor());
            LineCC.setColor(lineGraphs.get(CurrentEQNum - 1).getLineColour());
            Cards.show(EquationCardLayout, Integer.toString((int) EquationNum.getValue()));
        }
    }
}
