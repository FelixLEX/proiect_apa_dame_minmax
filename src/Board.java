import javafx.beans.binding.When;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.security.PrivateKey;
import java.util.*;
import java.util.List;
import java.util.concurrent.BlockingDeque;

import static java.awt.Color.*;

public class Board {

    // Board colors: beige and the default black
    private Color BEIGE = new Color(199, 171, 127);
    private int white_left = 12;
    public int black_left = 12;
    public int white_kings = 0;
    private int black_kings = 0;
    private Piece[][] board = new Piece[8][8];
    public int selected[] = new int[2];
    public boolean selection = false;
    public Color turn = BLACK;
    public Map<Point, List<Piece>> valid_moves = new HashMap<Point, List<Piece>>();

    public Board() {
        selected[0] = 99;
        selected[1] = 99;
        fill_board();
    }

    public Board(Board other)
    {
        white_left = other.white_left;
        black_left = other.black_left;
        white_kings = other.white_kings;
        black_kings = other.black_kings;
        board = new Piece[8][8];

        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                board[i][j] = new Piece(other.board[i][j]);
            }
        }
        selection = false;
        selected[0] = 99;
        selected[1] = 99;

        turn = other.turn;
    }

    public void update(Graphics2D g) {
    }

    public void move_piece(Piece piece, int row, int col) {
        Piece temp;
        temp = piece;
        valid_moves = get_valid_moves(piece);
        Set<Point> keys = valid_moves.keySet();
        Point selected_pos = new Point(row, col);
        for (Point x : keys)
        {
            if(x.equals(selected_pos))
            {
                board[piece.row][piece.col] = board[row][col];
                board[row][col] = temp;
                board[row][col].move(row, col);
                if (row == 7 || row == 0) {
                    if(!board[row][col].is_king) {
                        board[row][col].make_king();
                        if (board[row][col].color == WHITE) {
                            white_kings++;
                        } else if (board[row][col].color == BLACK) {
                            black_kings++;
                        }
                    }
                }


                List <Piece> pieces_to_delete = valid_moves.get(new Point(row, col));
                if (!pieces_to_delete.isEmpty())
                {
                    for (Piece to_delete : pieces_to_delete)
                    {
                        delete_piece(to_delete.row, to_delete.col);
                    }
                }

                selected[0] = 99;
                switch_turn();
                selection = false;
                valid_moves = new HashMap<Point, List<Piece>>();
            }
        }
    }

    public Piece get_piece(int x, int y) {
        return board[y][x];
    }


    public List<Piece> get_all_pieces(Color color)
    {
        List<Piece> pieces = new ArrayList<>();
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (!board[i][j].is_fake && board[i][j].color == color)
                {
                    pieces.add(new Piece(board[i][j]));
                }
            }
        }

        return pieces;
    }

    public void draw(Graphics2D g, int window_width, int window_height) {
        draw_squares(g, window_width, window_height);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (!board[i][j].is_fake) {
                    board[i][j].draw(g);
                }
            }
        }

        if (selection) {
            g.setColor(RED);
            g.fillOval(selected[0] * 100 + 40, selected[1] * 100 + 40, 20, 20);
        }

        draw_selected(g);
    }


    public void fill_board() {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (j % 2 == ((i + 1) % 2)) {
                    if (i < 3) {
                        board[i][j] = new Piece(i, j, WHITE);
                    } else if (i > 4) {
                        board[i][j] = new Piece(i, j, BLACK);
                    } else {
                        board[i][j] = new Piece(true);
                    }
                } else {
                    board[i][j] = new Piece(true);
                }
            }
        }
    }

    public float get_game_value()
    {
        return (float) (white_left - black_left + (white_kings * 0.5 - black_kings * 0.5));
//        return (float) (white_left - black_left);
    }


    public void draw_squares(Graphics2D g, int window_width, int window_height) {
        for (int y = 0; y < window_height; y += 200) {
            for (int x = 0; x < window_width; x += 200) {
                g.setColor(BEIGE);
                g.fillRect(x, y, 100, 100);
            }
        }

        for (int y = 100; y < window_height; y += 200) {
            for (int x = 100; x < window_height; x += 200) {
                g.setColor(BEIGE);
                g.fillRect(x, y, 100, 100);
            }
        }
    }


    public void select(Graphics2D g, int x_coord, int y_coord) {
        if (board[y_coord / 100][x_coord / 100].color != turn)
        {
            return;
        }
        if (!board[y_coord / 100][x_coord / 100].is_fake) {
            selection = true;
            selected[0] = x_coord / 100;
            selected[1] = y_coord / 100;
            valid_moves = get_valid_moves(board[y_coord / 100][x_coord / 100]);

        }
    }


    private void draw_selected(Graphics2D g)
    {
        Set<Point> keys = valid_moves.keySet();
        int n = keys.size();
        List<Point> keys_list = new ArrayList<Point>(n);
        for (Point x : keys)
            keys_list.add(x);

        g.setColor(RED);
        for (Point move: keys_list) {
            g.fillOval(move.y * 100 + 40, move.x * 100 + 40, 20, 20);
        }

    }


    public Color check_winner() {
        if (white_left == 0) {
            return WHITE;
        } else if (black_left == 0) {
            return BLACK;
        } else {
            // TODO: Find a better way to say the game is not over
            // Green means that we do not have a winner yet
            return GREEN;
        }
    }


    public boolean is_ocupied(int x_coord, int y_coord) {
        if (board[y_coord / 100][x_coord / 100].is_fake) {
            return false;
        } else {
            return true;
        }
    }


    public Map<Point, List<Piece>> get_valid_moves(Piece piece) {
        Map<Point, List<Piece>> moves = new HashMap<Point, List<Piece>>();
        valid_moves = new HashMap<Point, List<Piece>>();

        int left = piece.col - 1;
        int right = piece.col + 1;
        int row = piece.row;

        if (piece.color == BLACK || piece.is_king) {

            Map<Point, List<Piece>> rec_trav = traverse_left(row - 1, Math.max(row - 3, -1), -1, piece.color, left, new ArrayList<>());
            Map<Point, List<Piece>> rec_trav_right = traverse_right(row - 1, Math.max(row - 3, -1), -1, piece.color, right, new ArrayList<>());

            if (!rec_trav.keySet().isEmpty())
            {
                for (Point key : rec_trav.keySet())
                {
                    moves.put(key, rec_trav.get(key));
                }

            }

            if (!rec_trav_right.keySet().isEmpty())
            {
                for (Point key : rec_trav_right.keySet())
                {
                    moves.put(key, rec_trav_right.get(key));
                }
            }
        }

        if (piece.color == WHITE || piece.is_king) {

            Map<Point, List<Piece>> rec_trav = traverse_left(row + 1, Math.min(row + 3, 8), 1, piece.color, left, new ArrayList<>());
            Map<Point, List<Piece>> rec_trav_right = traverse_right(row + 1, Math.min(row + 3, 8), 1, piece.color, right, new ArrayList<>());

            if (!rec_trav.keySet().isEmpty())
            {
                for (Point key : rec_trav.keySet())
                {
                    moves.put(key, rec_trav.get(key));
                }

            }

            if (!rec_trav_right.keySet().isEmpty())
            {
                for (Point key : rec_trav_right.keySet())
                {
                    moves.put(key, rec_trav_right.get(key));
                }
            }
        }


        return moves;
    }


    private Map<Point, List<Piece>> traverse_left(int start, int stop, int step, Color color, int left, List<Piece> skipped) {
        Map<Point, List<Piece>> moves = new HashMap<Point, List<Piece>>();
        List<Piece> last = new ArrayList<>();


        if (step == -1)
        {

            for (int i = start; i >= stop; i += step) {
                if (left < 0) {
                    break;
                }
                if (i < 0 || i > 7)
                {
                    break;
                }

                Piece current = board[i][left];

                if (current.is_fake) {
                    if (!skipped.isEmpty() && last.isEmpty()) {
                        break;
                    } else if (!skipped.isEmpty()) {
                        List<Piece> temp = new ArrayList<>();
                        temp.addAll(last);
                        temp.addAll(skipped);
                        moves.put(new Point(i, left), temp);
                    } else {
                        List<Piece> temp = new ArrayList<>();
                        temp.addAll(last);
                        moves.put(new Point(i, left), temp);
                    }

                    if (!last.isEmpty()) {
                        int row;
                        if (step == -1) {
                            row = Math.max(i - 3, 0);
                        } else {
                            row = Math.min(i + 3, 8);
                        }
                        List<Piece> temp = new ArrayList<>();
                        temp.addAll(last);

                        Map<Point, List<Piece>> rec_trav = traverse_left(i + step, row, step, color, left - 1, temp);
                        Map<Point, List<Piece>> rec_trav_right = traverse_right(i + step, row, step, color, left + 1, temp);

                        if (!rec_trav.keySet().isEmpty())
                        {
                            for (Point key : rec_trav.keySet())
                            {
                                moves.put(key, rec_trav.get(key));
                            }

                        }

                        if (!rec_trav_right.keySet().isEmpty())
                        {
                            for (Point key : rec_trav_right.keySet())
                            {
                                moves.put(key, rec_trav_right.get(key));
                            }
                        }
                    }
                    break;
                } else if (current.color == color) {
                    break;
                } else {
                    if (!last.isEmpty())
                    {
                        if (last.get(last.size() - 1).color == current.color)
                        {
                            break;
                        }
                    }

                    List<Piece> temp = new ArrayList<>();
                    temp.add(current);
                    last.addAll(temp);
                }

                left--;
            }

        }
        else
        {
            for (int i = start; i <= stop; i += step) {
                if (left < 0) {
                    break;
                }
                if (i < 0 || i > 7)
                {
                    break;
                }
                Piece current = board[i][left];

                if (current.is_fake) {
                    if (!skipped.isEmpty() && last.isEmpty()) {
                        break;
                    } else if (!skipped.isEmpty()) {
                        List<Piece> temp = new ArrayList<>();
                        temp.addAll(last);
                        temp.addAll(skipped);
                        moves.put(new Point(i, left), temp);
                    } else {
                        List<Piece> temp = new ArrayList<>();
                        temp.addAll(last);
                        moves.put(new Point(i, left), temp);
                    }

                    if (!last.isEmpty()) {
                        int row;
                        if (step == -1) {
                            row = Math.max(i - 3, 0);
                        } else {
                            row = Math.min(i + 3, 8);
                        }
                        List<Piece> temp = new ArrayList<>();
                        temp.addAll(last);

                        Map<Point, List<Piece>> rec_trav = traverse_left(i + step, row, step, color, left - 1, temp);
                        Map<Point, List<Piece>> rec_trav_right = traverse_right(i + step, row, step, color, left + 1, temp);


                        if (!rec_trav.keySet().isEmpty())
                        {
                            for (Point key : rec_trav.keySet())
                            {
                                moves.put(key, rec_trav.get(key));
                            }

                        }

                        if (!rec_trav_right.keySet().isEmpty())
                        {
                            for (Point key : rec_trav_right.keySet())
                            {
                                moves.put(key, rec_trav_right.get(key));
                            }
                        }
                    }
                    break;
                } else if (current.color == color) {
                    break;
                } else {
                    if (!last.isEmpty())
                    {
                        if (last.get(last.size() - 1).color == current.color)
                        {
                            break;
                        }
                    }
                    List<Piece> temp = new ArrayList<>();
                    temp.add(current);
                    last.addAll(temp);
                }

                left--;
            }

        }
        return moves;
    }


    private Map<Point, List<Piece>> traverse_right(int start, int stop, int step, Color color, int right, List<Piece> skipped) {
        Map<Point, List<Piece>> moves = new HashMap<Point, List<Piece>>();
        List<Piece> last = new ArrayList<>();

        if (step == -1)
        {
            for (int i = start; i >= stop; i += step) {
                if (right > 7) {
                    break;
                }
                if (i < 0 || i > 7)
                {
                    break;
                }

                Piece current = board[i][right];

                if (current.is_fake) {
                    if (!skipped.isEmpty() && last.isEmpty()) {
                        break;
                    } else if (!skipped.isEmpty()) {
                        List<Piece> temp = new ArrayList<>();
                        temp.addAll(last);
                        temp.addAll(skipped);
                        moves.put(new Point(i, right), temp);
                    } else {
                        List<Piece> temp = new ArrayList<>();
                        temp.addAll(last);
                        moves.put(new Point(i, right), temp);
                    }

                    if (!last.isEmpty()) {
                        int row;
                        if (step == -1) {
                            row = Math.max(i - 3, 0);
                        } else {
                            row = Math.min(i + 3, 8);
                        }
                        List<Piece> temp = new ArrayList<>();
                        temp.addAll(last);

                        Map<Point, List<Piece>> rec_trav = traverse_left(i + step, row, step, color, right - 1, temp);
                        Map<Point, List<Piece>> rec_trav_right = traverse_right(i + step, row, step, color, right + 1, temp);

                        if (!rec_trav.keySet().isEmpty())
                        {
                            for (Point key : rec_trav.keySet())
                            {
                                moves.put(key, rec_trav.get(key));
                            }

                        }

                        if (!rec_trav_right.keySet().isEmpty())
                        {
                            for (Point key : rec_trav_right.keySet())
                            {
                                moves.put(key, rec_trav_right.get(key));
                            }
                        }
                    }
                    break;
                } else if (current.color == color) {
                    break;
                } else {
                    if (!last.isEmpty())
                    {
                        if (last.get(last.size() - 1).color == current.color)
                        {
                            break;
                        }
                    }
                    List<Piece> temp = new ArrayList<>();
                    temp.add(current);
                    last.addAll(temp);
                }

                right++;
            }
        }
        else
        {
            for (int i = start; i <= stop; i += step) {
                if (right > 7) {
                    break;
                }
                if (i < 0 || i > 7)
                {
                    break;
                }

                Piece current = board[i][right];

                if (current.is_fake) {
                    if (!skipped.isEmpty() && last.isEmpty()) {
                        break;
                    } else if (!skipped.isEmpty()) {
                        List<Piece> temp = new ArrayList<>();
                        temp.addAll(last);
                        temp.addAll(skipped);
                        moves.put(new Point(i, right), temp);
                    } else {
                        List<Piece> temp = new ArrayList<>();
                        temp.addAll(last);
                        moves.put(new Point(i, right), temp);
                    }

                    if (!last.isEmpty()) {
                        int row;
                        if (step == -1) {
                            row = Math.max(i - 3, 0);
                        } else {
                            row = Math.min(i + 3, 8);
                        }
                        List<Piece> temp = new ArrayList<>();
                        temp.addAll(last);

                        Map<Point, List<Piece>> rec_trav = traverse_left(i + step, row, step, color, right - 1, temp);
                        Map<Point, List<Piece>> rec_trav_right = traverse_right(i + step, row, step, color, right + 1, temp);

                        if (!rec_trav.keySet().isEmpty())
                        {
                            for (Point key : rec_trav.keySet())
                            {
                                moves.put(key, rec_trav.get(key));
                            }

                        }

                        if (!rec_trav_right.keySet().isEmpty())
                        {
                            for (Point key : rec_trav_right.keySet())
                            {
                                moves.put(key, rec_trav_right.get(key));
                            }
                        }
                    }
                    break;
                } else if (current.color == color) {
                    break;
                } else {
                    if (!last.isEmpty())
                    {
                        if (last.get(last.size() - 1).color == current.color)
                        {
                            break;
                        }
                    }
                    List<Piece> temp = new ArrayList<>();
                    temp.add(current);
                    last.addAll(temp);
                }
                right++;
            }
        }
        return moves;
    }


    public void delete_piece(int x, int y)
    {
        if(board[x][y].color == WHITE)
        {
            white_left--;
            if (board[x][y].is_king)
            {
                white_kings--;
            }
        }
        else if(board[x][y].color == BLACK)
        {
            black_left--;
            if (board[x][y].is_king)
            {
                black_kings--;
            }
        }
        board[x][y] = new Piece(true);
    }

    public void switch_turn()
    {
        if(turn == WHITE)
        {
            turn = BLACK;
        }
        else
        {
            turn = WHITE;
        }


    }

}
