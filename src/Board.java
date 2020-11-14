import java.awt.*;

import static java.awt.Color.BLACK;

public class Board {

    // Board colors: beige and the default black
    private Color BEIGE = new Color(199, 171, 127);


    public void update()
    {

    }

    public void draw(Graphics2D g, int window_width, int windhow_height)
    {
        /*
        The 2 for loops bellow will draw the board.
        Should consider making a separate function for this.
         */

        for (int y = 0; y < windhow_height; y += 200)
        {
            for (int x = 100; x < windhow_height; x += 200)
            {
                g.setColor(BEIGE);
                g.fillRect(x, y, 100, 100);
            }
        }

        for (int y = 100; y < windhow_height; y += 200)
        {
            for (int x = 0; x < windhow_height; x += 200)
            {
                g.setColor(BEIGE);
                g.fillRect(x, y, 100, 100);
            }
        }
    }

}
