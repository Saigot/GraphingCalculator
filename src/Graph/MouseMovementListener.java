package Graph;

import equationevaluator.Equation;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.text.DecimalFormat;
import javax.swing.JPanel;

/**
 * Handles mouse movement over a GraphPanel providing tooltips and precision lines
 * @author Michael Scovell
 */
public class MouseMovementListener implements MouseMotionListener {

    LineGraph Graph;
    JPanel Canvas;
    Equation p = null;

    /**
     *
     * @param canvas the canvas that will be drawn on and activated
     * @param g the line graph that will be handled by this listener
     */
    public MouseMovementListener(JPanel canvas, LineGraph g) {
        Graph = g;
        Canvas = canvas;
    }

    /**
     *
     * @param canvas the canvas that will be drawn on and activated
     * @param g the line graph that will be handled by this listener
     * @param eq the equation of the graph that is handled
     */
    public MouseMovementListener(JPanel canvas, LineGraph g, Equation eq) {
        Graph = g;
        Canvas = canvas;
        p = eq;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Canvas.repaint();
        double x = Graph.GetRelativeX(e.getX(), Canvas.getWidth());
        double y = Graph.GetRelativeY(e.getY(), Canvas.getHeight());
        DecimalFormat d = new DecimalFormat("### ### ##0.###;-### ### ##0.###");
        String xs = d.format(x);
        if (p == null) {
            Canvas.setToolTipText("(" + xs + "," + d.format(y) + ")");
        } else {
            Canvas.setToolTipText("<html>(" + xs + "," + d.format(y) + ") <br>"
                    + "f(" + xs + ") = " + d.format(p.peekAt("x", x)) + "</br></html>");
        }
    }
}
