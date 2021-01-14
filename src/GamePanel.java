import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.image.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class GamePanel extends JPanel implements Runnable {

    public static int WIDTH = 800;
    public static int HEIGHT = 800;

    private Thread thread;
    private boolean running;

    private BufferedImage image;
    private Graphics2D g;

    private Board board;

    private int mouse_x;
    private int mouse_y;

    private Minimax minimax;

    private Color GREY = new Color(125, 83, 36);

    public GamePanel() {

        super();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouse_x = e.getX();
                mouse_y = e.getY();

                if (board.selected[0] == 99) {
                    if (!board.get_piece(mouse_x / 100, mouse_y / 100).is_fake) {
                        board.select(g, mouse_x, mouse_y);
                    }
                } else {
                    if (!board.is_ocupied(mouse_x, mouse_y)) {
                        board.move_piece(board.get_piece(board.selected[0], board.selected[1]), mouse_y / 100, mouse_x / 100);
                        System.out.println("Turn made by you.");
                    } else {
                        board.select(g, mouse_x, mouse_y);
                    }
                }
            }
        });
    }

    public void addNotify() {
        super.addNotify();

        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }

    }


    public void run() {
        running = true;

        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();

        board = new Board();
        minimax = new Minimax();
        while (running) {
            if(board.turn == Color.WHITE)
            {
                Map<Float, Board> best_move = minimax.algorithm(board,6,-999999, 999999, true);
                float key = 0;
                for(float x : best_move.keySet())
                {
                    key = x;
                }
                minimax_move(best_move.get(key));
                System.out.println("Turn made by ai with value" + key);
                System.out.println("Black left " + board.black_left);
                System.out.println("White kings " + board.white_kings);
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            gameUpdate();
            gameRender();
            gameDraw();
        }
    }


    private void minimax_move(Board new_board)
    {
        board = new Board(new_board);
    }


    private void gameUpdate() {
        board.update(g);
    }

    private void show_winner()
    {
        if (board.check_winner() != Color.GREEN) {
            g.setColor(Color.GREEN);
            if (board.check_winner() == Color.BLACK) {
                g.drawString("You lost.", 350, 400);
            } else if (board.check_winner() == Color.WHITE) {
                g.drawString("You won.", 350, 400);
            }
            running = false;
        }

    }


    private void gameRender()
    {
        g.setColor(GREY);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(GREY);
        board.draw(g, WIDTH, HEIGHT);
        show_winner();
    }


    private void gameDraw()
    {
        Graphics g2 = this.getGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
    }

}