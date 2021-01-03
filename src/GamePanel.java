import com.sun.xml.internal.ws.api.ha.StickyFeature;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.image.*;
import java.util.HashMap;
import java.util.Map;

public class GamePanel extends JPanel implements Runnable {

    public static int WIDTH = 800;
    public static int HEIGHT = 800;

    private Thread thread;
    private boolean running;

    private BufferedImage image;
    private Graphics2D g;

    private Board board;

    private int  mouse_x;
    private int  mouse_y;

    private Color GREY = new Color(125, 83, 36);

    // TODO: Bad design, find another way to store moves including multiple jumps.
    private Map<String, Vector2i> moves = new HashMap<String, Vector2i>();

    public GamePanel()
    {
        super();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();


        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouse_x = e.getX();
                mouse_y = e.getY();

                if (board.selected[0] == 99) {
                    if (!board.get_piece(mouse_x / 100, mouse_y / 100).is_fake)
                    {
                        board.select(g, mouse_x, mouse_y);
                    }
                }
                else
                {
                    board.move_piece(board.get_piece(board.selected[0], board.selected[1]), mouse_y / 100, mouse_x / 100);
                    board.selected[0] = 99;
                }
            }
        });
    }

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
        board.update();

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


    public Map<String, Vector2i> get_valid_move(Piece piece)
    {
        Map<String, Vector2i> moves = new HashMap<String, Vector2i>();

        int left = piece.col - 1;
        int right = piece.row + 1;
        int row = piece.row;

        return moves;
    }


}