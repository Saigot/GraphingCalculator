package Graph;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 * A Panel that a line graph must draw to
 * @author Michael1 Scovell
 */
public class GraphPanel extends JPanel {
    //the root line graph to be drawn to
    LineGraph g;
    
    
    // the graphs that will be drawn on top of eachh other
    public ArrayList<LineGraph> LineGraphs = new ArrayList<>();
    int w; //width of draw area
    int h; //height of draw area
    double minX; //the smallest value x can be for the root graph
    double minY; //the smallest value y can be for the root graph
    double maxX; //the largest value x can be for the root graph
    double maxY; //the largest value y can be for the root graph
    boolean useBounds; //Whether or not to abey the constarits above or let the line graph decide

    GraphPanel(LineGraph G, int W, int H, double MinX, double MinY,
            double MaxX, double MaxY) {
        g = G;
        w = W;
        h = H;
        minX = MinX;
        maxX = MaxX;
        minY = MinY;
        maxY = MaxY;
        useBounds = true;
        this.setBackground(G.getBackColor());
    }

    GraphPanel(LineGraph G, int W, int H) {
        g = G;
        w = W;
        h = H;
        minX = 0;
        maxX = 0;
        minY = 0;
        maxY = 0;
        useBounds = false;
        this.setBackground(G.getBackColor());
    }

    @Override
    public void paintComponent(Graphics gr) {
        Graphics2D graphics = (Graphics2D) gr;
        super.paintComponent(gr);
        if (g.IsErrors()) {
            return;
        }
        PointerInfo a = MouseInfo.getPointerInfo();
        if (g.getDrawMouseLines()) {
            int x = (int) (a.getLocation().getX() - this.getLocationOnScreen().getX());
            int y = (int) (a.getLocation().getY() - this.getLocationOnScreen().getY());
            if (x <= this.getWidth() - g.getOffSetX() && x >= g.getOffSetX()
                    && y <= this.getHeight() - g.getOffSetY() && y >= g.getOffSetY()) {
                graphics.setColor(Color.black);
                graphics.drawLine(g.getOffSetX(), y, this.getWidth() - g.getOffSetX(), y);
                graphics.drawLine(x, g.getOffSetY(), x, this.getHeight() - g.getOffSetY());
            }

        }

        if (useBounds) {
            if (g.GetDrawArea()) {
                g.drawGrid(graphics, w, h, minX, minY, maxX, maxY);
                g.drawArea(graphics, w, h, minX, minY, maxX, maxY);
            } else {
                g.drawGrid(graphics, w, h, minX, minY, maxX, maxY);
                g.drawLine(graphics, w, h, minX, minY, maxX, maxY);
            }
        } else {
            if (g.GetDrawArea()) {
                g.drawGrid(graphics, w, h);
                g.drawArea(graphics, w, h);
            } else {
                g.drawGrid(graphics, w, h);
                g.drawLine(graphics, w, h);
            }
        }

        for (LineGraph line : LineGraphs) {
            if (line == null) {
                continue;
            }
            if (line.GetDrawArea()) {
                line.drawArea(graphics, w, h, g.getMinX(), g.getMinY(), g.getMaxX(), g.getMaxY());
            } else {
                line.drawLine(graphics, w, h, g.getMinX(), g.getMinY(), g.getMaxX(), g.getMaxY());
            }
        }
    }
}