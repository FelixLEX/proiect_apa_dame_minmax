import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Piece {
    public int row;
    public int col;
    public Color color;
    private int x = 0;
    private int y = 0;
    public boolean is_king = false;
    public boolean is_fake = false;
    private BufferedImage crown;


    public Piece(int _row, int _col, Color _color)
    {
        row = _row;
        col = _col;
        color = _color;
    }

    // Constructor for fake pieces
    public Piece(boolean fake)
    {
        is_fake = fake;
    }

    public void make_king()
    {
        is_king = true;
    }

    private void calculate_pos()
    {
        x = col * 100 + 10;
        y = row * 100 + 10;
    }

    public void move(int _row, int _col)
    {
        row = _row;
        col = _col;
    }

    private void load_crown()
    {
        //TODO: Load image and display in draw function when the piece is king
    }

    public void draw(Graphics2D g)
    {
        if (is_king)
        {
            g.setColor(color);
            calculate_pos();
            g.fillOval(x, y, 80, 80);

        }
        else {
            load_crown();
            g.setColor(color);
            calculate_pos();
            g.fillOval(x, y, 80, 80);
        }
    }


}
