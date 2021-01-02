import java.awt.*;
import java.security.PrivateKey;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;

public class Board {

    // Board colors: beige and the default black
    private Color BEIGE = new Color(199, 171, 127);
    private int white_left = 12;
    private int black_left = 12;
    private int white_kings = 0;
    private int black_kings = 0;
    private Piece[][] board = new Piece[8][8];


    public Board()
    {
        fill_board();
    }

    public void update()
    {

    }

    public void draw(Graphics2D g, int window_width, int windhow_height)
    {
        draw_squares(g, window_width, windhow_height);

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



        // Creating the white pieces
        for (int i = 0; i < 3; i++)
        {
            if (i % 2 == 0)
            {
                for (int j = 1; j < 8; j += 2)
                {
                    board[i][j] = new Piece(i, j , WHITE);
                }
            }

            if (i % 2 != 0)
            {
                for (int j = 0; j < 8; j += 2)
                {
                    board[i][j] = new Piece(i, j, WHITE);
                }
            }
        }

        // Creating the black pieces
        for (int i = 5; i < 8; i++)
        {
            if (i % 2 == 0)
            {
                for (int j = 1; j < 8; j += 2)
                {
                    board[i][j] = new Piece(i, j , BLACK);
                }
            }

            if (i % 2 != 0)
            {
                for (int j = 0; j < 8; j += 2)
                {
                    board[i][j] = new Piece(i, j, BLACK);
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

}
