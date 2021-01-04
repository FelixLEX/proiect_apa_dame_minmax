import javafx.beans.binding.When;

import java.awt.*;
import java.security.PrivateKey;
import java.util.*;
import java.util.List;
import java.util.concurrent.BlockingDeque;

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
    private Color turn = WHITE;
    private Map<Vector2i, List<Piece>> valid_moves = new HashMap<Vector2i, List<Piece>>();

    public Board() {
        selected[0] = 99;
        selected[1] = 99;
        fill_board();
    }

    public void update() {

    }

    public void move_piece(Piece piece, int row, int col) {
        Piece temp;
        temp = piece;
        board[piece.row][piece.col] = board[row][col];
        board[row][col] = temp;
        board[row][col].move(row, col);
        if (row == 7 || row == 0) {
            board[row][col].make_king();
            if (board[row][col].color == WHITE) {
                white_kings++;
            } else {
                black_kings++;
            }
        }
    }

    public Piece get_piece(int x, int y) {
        return board[y][x];
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


    private void fill_board() {

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


    public void draw_squares(Graphics2D g, int window_width, int windhow_height) {
        for (int y = 0; y < windhow_height; y += 200) {
            for (int x = 0; x < window_width; x += 200) {
                g.setColor(BEIGE);
                g.fillRect(x, y, 100, 100);
            }
        }

        for (int y = 100; y < windhow_height; y += 200) {
            for (int x = 100; x < windhow_height; x += 200) {
                g.setColor(BEIGE);
                g.fillRect(x, y, 100, 100);
            }
        }
    }


    //TODO: Very poor design for this function
    public void select(Graphics2D g, int x_coord, int y_coord) {

        if (!board[y_coord / 100][x_coord / 100].is_fake) {
            selection = true;
            selected[0] = x_coord / 100;
            selected[1] = y_coord / 100;
            valid_moves = get_valid_moves(board[y_coord / 100][x_coord / 100]);
        }
    }


    private void draw_selected(Graphics2D g)
    {
        Set<Vector2i> keys = valid_moves.keySet();
        int n = keys.size();
        List<Vector2i> keys_list = new ArrayList<Vector2i>(n);
        for (Vector2i x : keys)
            keys_list.add(x);

        g.setColor(RED);
        for (Vector2i move: keys_list) {
            System.out.println(move.x + " " + move.y + "\n");
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
            // Green means there we do not have a winner yet
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


    public Map<Vector2i, List<Piece>> get_valid_moves(Piece piece) {
        Map<Vector2i, List<Piece>> moves = new HashMap<Vector2i, List<Piece>>();
        valid_moves = new HashMap<Vector2i, List<Piece>>();

        int left = piece.col - 1;
        int right = piece.col + 1;
        int row = piece.row;
        System.out.println(piece.row);

        if (piece.color == BLACK || piece.is_king) {
            List<Piece> temp = new ArrayList<>();
            Map<Vector2i, List<Piece>> rec_trav = traverse_left(row - 1, Math.max(row - 3, -1), -1, piece.color, left, temp);
            Set<Vector2i> keys = rec_trav.keySet();
            int n = keys.size();
            List<Vector2i> keys_list = new ArrayList<Vector2i>(n);
            for (Vector2i x : keys)
                keys_list.add(x);

            Map<Vector2i, List<Piece>> rec_trav_right = traverse_right(row - 1, Math.max(row - 3, -1), -1, piece.color, right, temp);
            Set<Vector2i> keys_right = rec_trav_right.keySet();
            int n_right = keys_right.size();
            List<Vector2i> keys_list_right = new ArrayList<Vector2i>(n_right);
            for (Vector2i x : keys_right)
                keys_list_right.add(x);

            if (!keys_list.isEmpty())
            {
                moves.put(keys_list.get(0), rec_trav.get(keys_list.get(0)));
            }

            if (!keys_list_right.isEmpty())
            {
                moves.put(keys_list_right.get(0), rec_trav_right.get(keys_list_right.get(0)));
            }
        }

        if (piece.color == WHITE || piece.is_king) {
            List<Piece> temp = new ArrayList<>();
            Map<Vector2i, List<Piece>> rec_trav = traverse_left(row + 1, Math.min(row + 3, 8), 1, piece.color, left, temp);
            Set<Vector2i> keys = rec_trav.keySet();
            int n = keys.size();
            List<Vector2i> keys_list = new ArrayList<Vector2i>(n);
            for (Vector2i x : keys)
                keys_list.add(x);

            Map<Vector2i, List<Piece>> rec_trav_right = traverse_right(row + 1, Math.min(row + 3, 8), 1, piece.color, right, temp);
            Set<Vector2i> keys_right = rec_trav_right.keySet();
            int n_right = keys_right.size();
            List<Vector2i> keys_list_right = new ArrayList<Vector2i>(n_right);
            for (Vector2i x : keys_right)
                keys_list_right.add(x);

            if (!keys_list.isEmpty())
            {
                moves.put(keys_list.get(0), rec_trav.get(keys_list.get(0)));
            }

            if (!keys_list_right.isEmpty())
            {
                moves.put(keys_list_right.get(0), rec_trav_right.get(keys_list_right.get(0)));
            }
        }


        return moves;
    }


    private Map<Vector2i, List<Piece>> traverse_left(int start, int stop, int step, Color color, int left, List<Piece> skipped) {
        Map<Vector2i, List<Piece>> moves = new HashMap<Vector2i, List<Piece>>();
        List<Piece> last = new ArrayList<>();


        if (step == -1)
        {
            for (int i = start; i > stop; i += step) {
                if (left < 0) {
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
                        moves.put(new Vector2i(i, left), temp);
                    } else {
                        List<Piece> temp = new ArrayList<>();
                        temp.addAll(last);
                        moves.put(new Vector2i(i, left), temp);
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

                        Map<Vector2i, List<Piece>> rec_trav = traverse_left(i + step, row, step, color, left - 1, temp);
                        Set<Vector2i> keys = rec_trav.keySet();
                        int n = keys.size();
                        List<Vector2i> keys_list = new ArrayList<Vector2i>(n);
                        for (Vector2i x : keys)
                            keys_list.add(x);

                        Map<Vector2i, List<Piece>> rec_trav_right = traverse_right(i + step, row, step, color, left + 1, temp);
                        Set<Vector2i> keys_right = rec_trav_right.keySet();
                        int n_right = keys_right.size();
                        List<Vector2i> keys_list_right = new ArrayList<Vector2i>(n_right);
                        for (Vector2i x : keys_right)
                            keys_list_right.add(x);

                        if (!keys_list.isEmpty())
                        {
                            moves.put(keys_list.get(0), rec_trav.get(keys_list.get(0)));
                        }

                        if (!keys_list_right.isEmpty())
                        {
                            moves.put(keys_list_right.get(0), rec_trav_right.get(keys_list_right.get(0)));
                        }
                    }
                    break;
                } else if (current.color == color) {
                    break;
                } else {
                    List<Piece> temp = new ArrayList<>();
                    temp.add(current);
                    last = temp;
                }

                left--;
            }
        }
        else
        {
            for (int i = start; i < stop; i += step) {
                if (left < 0) {
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
                        moves.put(new Vector2i(i, left), temp);
                    } else {
                        List<Piece> temp = new ArrayList<>();
                        temp.addAll(last);
                        moves.put(new Vector2i(i, left), temp);
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

                        Map<Vector2i, List<Piece>> rec_trav = traverse_left(i + step, row, step, color, left - 1, temp);
                        Set<Vector2i> keys = rec_trav.keySet();
                        int n = keys.size();
                        List<Vector2i> keys_list = new ArrayList<Vector2i>(n);
                        for (Vector2i x : keys)
                            keys_list.add(x);

                        Map<Vector2i, List<Piece>> rec_trav_right = traverse_right(i + step, row, step, color, left + 1, temp);
                        Set<Vector2i> keys_right = rec_trav_right.keySet();
                        int n_right = keys_right.size();
                        List<Vector2i> keys_list_right = new ArrayList<Vector2i>(n_right);
                        for (Vector2i x : keys_right)
                            keys_list_right.add(x);

                        if (!keys_list.isEmpty())
                        {
                            moves.put(keys_list.get(0), rec_trav.get(keys_list.get(0)));
                        }

                        if (!keys_list_right.isEmpty())
                        {
                            moves.put(keys_list_right.get(0), rec_trav_right.get(keys_list_right.get(0)));
                        }
                    }
                    break;
                } else if (current.color == color) {
                    break;
                } else {
                    List<Piece> temp = new ArrayList<>();
                    temp.add(current);
                    last = temp;
                }

                left--;
            }
        }

        return moves;
    }


    private Map<Vector2i, List<Piece>> traverse_right(int start, int stop, int step, Color color, int right, List<Piece> skipped) {
        Map<Vector2i, List<Piece>> moves = new HashMap<Vector2i, List<Piece>>();
        List<Piece> last = new ArrayList<>();

        if (step == -1)
        {
            for (int i = start; i > stop; i += step) {
                if (right > 7) {
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
                        moves.put(new Vector2i(i, right), temp);
                    } else {
                        List<Piece> temp = new ArrayList<>();
                        temp.addAll(last);
                        moves.put(new Vector2i(i, right), temp);
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

                        Map<Vector2i, List<Piece>> rec_trav = traverse_left(i + step, row, step, color, right - 1, temp);
                        Set<Vector2i> keys = rec_trav.keySet();
                        int n = keys.size();
                        List<Vector2i> keys_list = new ArrayList<Vector2i>(n);
                        for (Vector2i x : keys)
                            keys_list.add(x);

                        Map<Vector2i, List<Piece>> rec_trav_right = traverse_right(i + step, row, step, color, right + 1, temp);
                        Set<Vector2i> keys_right = rec_trav.keySet();
                        int n_right = keys_right.size();
                        List<Vector2i> keys_list_right = new ArrayList<Vector2i>(n);
                        for (Vector2i x : keys_right)
                            keys_list_right.add(x);

                        if (!keys_list.isEmpty())
                        {
                            moves.put(keys_list.get(0), rec_trav.get(keys_list.get(0)));
                        }

                        if (!keys_list_right.isEmpty())
                        {
                            moves.put(keys_list_right.get(0), rec_trav_right.get(keys_list_right.get(0)));
                        }
                    }
                    break;
                } else if (current.color == color) {
                    break;
                } else {
                    List<Piece> temp = new ArrayList<>();
                    temp.add(current);
                    last = temp;
                }

                right++;
            }
        }
        else
        {
            for (int i = start; i < stop; i += step) {
                if (right > 7) {
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
                        moves.put(new Vector2i(i, right), temp);
                    } else {
                        List<Piece> temp = new ArrayList<>();
                        temp.addAll(last);
                        moves.put(new Vector2i(i, right), temp);
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

                        Map<Vector2i, List<Piece>> rec_trav = traverse_left(i + step, row, step, color, right - 1, temp);
                        Set<Vector2i> keys = rec_trav.keySet();
                        int n = keys.size();
                        List<Vector2i> keys_list = new ArrayList<Vector2i>(n);
                        for (Vector2i x : keys)
                            keys_list.add(x);

                        Map<Vector2i, List<Piece>> rec_trav_right = traverse_right(i + step, row, step, color, right + 1, temp);
                        Set<Vector2i> keys_right = rec_trav.keySet();
                        int n_right = keys_right.size();
                        List<Vector2i> keys_list_right = new ArrayList<Vector2i>(n);
                        for (Vector2i x : keys_right)
                            keys_list_right.add(x);

                        if (!keys_list.isEmpty())
                        {
                            moves.put(keys_list.get(0), rec_trav.get(keys_list.get(0)));
                        }

                        if (!keys_list_right.isEmpty())
                        {
                            moves.put(keys_list_right.get(0), rec_trav_right.get(keys_list_right.get(0)));
                        }
                    }
                    break;
                } else if (current.color == color) {
                    break;
                } else {
                    List<Piece> temp = new ArrayList<>();
                    temp.add(current);
                    last = temp;
                }

                right++;
            }
        }

        return moves;
    }


    private void delete_piece(int x, int y)
    {
        board[x][y] = new Piece(true);
    }

    private void switch_turn()
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
