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


    // Constructor to make deep copies
    public Piece(Piece piece)
    {
        row = piece.row;
        col = piece.col;
        color = piece.color;
        x = piece.x;
        y = piece.y;
        is_fake = piece.is_fake;
        is_king = piece.is_king;
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


    public void draw(Graphics2D g)
    {
        if (!is_king)
        {
            g.setColor(color);
            calculate_pos();
            g.fillOval(x, y, 80, 80);

        }
        else {
            g.setColor(color);
            calculate_pos();
            g.fillOval(x, y, 80, 80);
            g.setColor(Color.ORANGE);
            g.setFont(new Font("Arial Unicode MS", Font.PLAIN, 36));
            g.drawString("\uD83D\uDC51", x+22, y+50);
        }
    }
}
