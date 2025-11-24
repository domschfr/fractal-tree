import javax.swing.*;
import java.awt.*;
import java.util.concurrent.*;

public class FractalTree extends Canvas {
    /* Variables with class-wide visibility */
    private static boolean slowMode;

    private final BlockingQueue<Line> bq = new LinkedBlockingQueue<>(1000);
    private final ExecutorService executorService = Executors.newFixedThreadPool(128);

    public static class Line {
        int x1;
        int y1;
        int x2;
        int y2;
        Color color;

        public Line(int x1, int y1, int x2, int y2, Color color) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.color = color;
        }
    }

    /* Recursive function for calculating all drawcalls for the fractal tree */
    public void makeFractalTree(int x, int y, int angle, int height) {
        if (slowMode) {
            try {Thread.sleep(100);}
            catch (InterruptedException ie) {ie.printStackTrace();}
        }

        if (height == 0) return;

        int x2 = x + (int)(Math.cos(Math.toRadians(angle)) * height * 8);
        int y2 = y + (int)(Math.sin(Math.toRadians(angle)) * height * 8);
        Color color = height < 5 ? Color.GREEN : Color.BLACK;

        try {
            bq.put(new Line(x, y, x2, y2, color));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executorService.submit(() -> makeFractalTree(x2, y2, angle-20, height-1));
        makeFractalTree(x2, y2, angle+20, height-1);
    }

    /* Code for EDT */
    /* Must only contain swing code (draw things on the screen) */
    /* Must not contain calculations (do not use math and compute libraries here) */
    /* No need to understand swing, a simple endless loop that draws lines is enough */
    @Override
    public void paint(Graphics g) {
        while(true) {
            try {
                Line line = bq.take();
                g.setColor(line.color);
                g.drawLine(line.x1, line.y1, line.x2, line.y2);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    /* Code for main thread */
    public static void main(String args[]) throws InterruptedException {

        /* Parse args */
        slowMode = args.length != 0 && Boolean.parseBoolean(args[0]);

        /* Initialize graphical elements and EDT */
        FractalTree tree = new FractalTree();
        JFrame frame = new JFrame();
        frame.setSize(800,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(tree);
        frame.setVisible(true);

        tree.executorService.submit(() -> tree.makeFractalTree(390, 480, -90, 10));

//        tree.executorService.shutdown();
//        if (!tree.executorService.awaitTermination(3, TimeUnit.SECONDS)) {
//            tree.executorService.shutdownNow();
//        }

        /* Log success as last step */
        System.out.println("Main has finished");
    }
}
