package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;

/**
 * A Listener that handles button presses
 * @author Michael Scovell
 */
public class GraphActionListener implements ActionListener {

    MainField m;
    JTextField Source;

    /**
     *
     * @param M the Mainfield being acted on
     * @param s The textfield with focus
     */
    public GraphActionListener(MainField M, JTextField s) {
        m = M;
        Source = s;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String c = e.getActionCommand();
        switch (c) {
            case "Graph":
                m.RootGraph = (int) m.EquationNum.getValue() - 1;
                m.CurrentEQ = Source.getText();
                m.getGUIVALS();
                m.DrawGraphActive();
                Source.setForeground(m.LineColor);
                Source.setBackground(m.BackColor);
                break;
            case "Add":
                m.AddNewGraph();
                break;
            case "Clear":
                m.RemoveAllGraphs();
                break;
            case "Bring To Top":
                m.RootGraph = (int) m.EquationNum.getValue() - 1;
                m.CurrentEQ = m.lineGraphs.get(m.RootGraph).raw;
                m.DrawGraphPassive();
                break;
            case "Force Range":
                m.MaxYSpin.setEnabled(m.ForceRangeBox.isSelected());
                m.MinYSpin.setEnabled(m.ForceRangeBox.isSelected());
                break;
            case "Defualt Zoom":
                m.ZoomSlider.setValue(7);
        }
    }
}
