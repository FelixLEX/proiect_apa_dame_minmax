import java.awt.*;

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
        g.setColor(color);
        calculate_pos();
        g.fillOval(x, y, 80, 80);
    }


}
