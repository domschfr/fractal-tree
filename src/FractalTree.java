import javax.swing.*;
import java.awt.*;

public class FractalTree extends Canvas {
    /* Variables with class-wide visibility */
    private static boolean slowMode;

    /* Recursive function for calculating all drawcalls for the fractal tree */
    public void makeFractalTree(Graphics g, int x, int y, int angle, int height) {

        if (slowMode) {
            try {Thread.sleep(100);}
            catch (InterruptedException ie) {ie.printStackTrace();}
        }

        if (height == 0) return;

        int x2 = x + (int)(Math.cos(Math.toRadians(angle)) * height * 8);
        int y2 = y + (int)(Math.sin(Math.toRadians(angle)) * height * 8);
        g.setColor(Color.BLACK);
        if (height < 5 ) g.setColor(Color.GREEN);
        else g.setColor(Color.BLACK);
        g.drawLine(x, y, x2, y2);

        makeFractalTree(g, x2, y2, angle-20, height-1);
        makeFractalTree(g, x2, y2, angle+20, height-1);
    }

    /* Code for EDT */
    /* Must only contain swing code (draw things on the screen) */
    /* Must not contain calculations (do not use math and compute libraries here) */
    /* No need to understand swing, a simple endless loop that draws lines is enough */
    @Override
    public void paint(Graphics g) {
        makeFractalTree(g, 390, 480, -90, 10); // Should not be here!
        while(true) {
            // ...
        }
    }

    /* Code for main thread */
    public static void main(String args[]) {

        /* Parse args */
        slowMode = args.length != 0 && Boolean.parseBoolean(args[0]);

        /* Initialize graphical elements and EDT */
        FractalTree tree = new FractalTree();
        JFrame frame = new JFrame();
        frame.setSize(800,600);
        frame.setVisible(true);
        frame.add(tree);

        /* Log success as last step */
        System.out.println("Main has finished");
    }
}
