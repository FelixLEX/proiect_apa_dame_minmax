import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.*;

public class GamePanel extends JPanel implements Runnable {

    public static int WIDTH = 800;
    public static int HEIGHT = 800;

    private Thread thread;
    private boolean running;

    private BufferedImage image;
    private Graphics2D g;

    private Board board;

    private Color GREY = new Color(125, 83, 36);

    // Constructor
    public GamePanel()
    {
        super();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();
    }

    // Functions
    public void addNotify()
    {
        super.addNotify();

        if(thread == null)
        {
            thread = new Thread(this);
            thread.start();
        }

    }


    public void run()
    {
        running = true;

        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();

        board = new Board();

        while (running)
        {
            gameUpdate();
            gameRender();
            gameDraw();
        }
    }


    private void gameUpdate()
    {

    }


    private void gameRender()
    {
        g.setColor(GREY);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(GREY);
        board.draw(g, WIDTH, HEIGHT);
        g.drawString("Pls start working", 100, 100);
    }


    private void gameDraw()
    {
        Graphics g2 = this.getGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
    }



}