import java.awt.*;
import java.security.PrivateKey;

import static java.awt.Color.*;

public class Board {

    // Board colors: beige and the default black
    private Color BEIGE = new Color(199, 171, 127);
    private int white_left = 12;
    private int black_left = 12;
    private int white_kings = 0;
    private int black_kings = 0;
    private Piece[][] board = new Piece[8][8];
    public int selected[] = new int[2];
    public boolean selection = false;


    public Board() {
        selected[0] = 99;
        selected[1] = 99;
        fill_board();
    }

    public void update()
    {
    }

    public void move_piece(Piece piece, int row, int col)
    {
        Piece temp;
        temp = piece;
        board[piece.row][piece.col] = board[row][col];
        board[row][col] = temp;
        board[row][col].move(row, col);
    }

    public Piece get_piece(int x, int y)
    {
        return board[y][x];
    }


    public void draw(Graphics2D g, int window_width, int window_height)
    {
        draw_squares(g, window_width, window_height);

        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (!board[i][j].is_fake)
                {
                    board[i][j].draw(g);
                }
            }
        }

        if(selection)
        {
            g.setColor(RED);
            g.fillOval(selected[0]*100+40, selected[1]*100+40, 20,20);
        }


    }


    private void fill_board()
    {

        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (j % 2 == ((i + 1) % 2))
                {
                    if (i < 3)
                    {
                        board[i][j] = new Piece(i, j, WHITE);
                    }
                    else if (i > 4)
                    {
                        board[i][j] = new Piece(i, j, BLACK);
                    }
                    else
                    {
                        board[i][j] = new Piece(true);
                    }
                }
                else
                {
                    board[i][j] = new Piece(true);
                }
            }
        }
    }


    public void draw_squares(Graphics2D g, int window_width, int windhow_height)
    {
        for (int y = 0; y < windhow_height; y += 200)
        {
            for (int x = 0; x < window_width; x += 200)
            {
                g.setColor(BEIGE);
                g.fillRect(x, y, 100, 100);
            }
        }

        for (int y = 100; y < windhow_height; y += 200)
        {
            for (int x = 100; x < windhow_height; x += 200)
            {
                g.setColor(BEIGE);
                g.fillRect(x, y, 100, 100);
            }
        }
    }



    public void select(Graphics2D g, int x_coord, int y_coord)
    {
        selection = true;
        selected[0] = x_coord / 100;
        selected[1] = y_coord / 100;
    }

}
