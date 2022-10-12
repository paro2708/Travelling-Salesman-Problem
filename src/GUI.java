import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.Map;

/**
 * This class contains all the UI elements. It takes the X and Y coordinates from the
 * symmetric matrix and plots them on the UI.
 */
public class GUI extends JPanel {
    public Map<Integer, Point> points;

    public GUI(Map<Integer, Point> points) {
        this.points = points;
    }

    /**
     * This method uses a scaling factor to ensure that all the coordinates are plotted on the screen.
     * @param grf the <code>Graphics</code> object to protect
     */
    protected void paintComponent(Graphics grf) {
        super.paintComponent(grf);
        Graphics2D graph = (Graphics2D) grf;

        graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        int scalingFactorX = (getMaxX() / width) + 1;
        int scalingFactorY = (getMaxY() / height) + 1;
        graph.setPaint(Color.RED);
        for (Integer p1 : points.keySet()) {
            int x2 = (int) points.get(p1).x / scalingFactorX;
            int y2 = (int) points.get(p1).y / scalingFactorY;
            graph.fill(new Ellipse2D.Double(-x2+width,
                    -1 * y2 + height,
                    4, 4));
        }
    }

    /**
     * This is a utility function to get the maximum Y coordinate.
     * @return int for the maximum Y coordinate
     */
    private int getMaxY() {
        int max = -Integer.MAX_VALUE;
        for (Integer p1 : points.keySet()) {
            if (points.get(p1).getY() > max)
                max = (int) points.get(p1).getY();
        }
        return max;
    }

    /**
     * This is a utility function to get the maximum X coordinate.
     * @return int For the maximum X coordinate
     */
    private int getMaxX() {
        int max = -Integer.MAX_VALUE;
        for (Integer p1 : points.keySet()) {
            if (points.get(p1).getX() > max)
                max = (int) points.get(p1).getX();
        }
        return max;
    }

    /**
     * This function initializes the panel or canvas for plotting the coordinates.
     */
    public void utilDraw() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new GUI(points));
        frame.setSize(1000, 1000);
        frame.setLocation(200, 200);
        frame.setVisible(true);
    }
}
