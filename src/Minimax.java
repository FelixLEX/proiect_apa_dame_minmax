import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Minimax {
    public Map<Float, Board> algorithm(Board board_state, int depth, boolean max_player)
    {
        Map<Float, Board> best = new HashMap<>();

        if (depth == 0 || board_state.check_winner() != Color.GREEN)
        {
            best.put(board_state.get_game_value(), board_state);
            return best;
        }

        if(max_player)
        {
            Map<Float, Board> score = new HashMap<>();
            float max_eval = -999999;
            float eval = 0;
            Board temp_best_board = new Board();
            for (Board state : get_all_moves(board_state, Color.WHITE))
            {
                score.putAll(algorithm(state, depth -1, false));
                for (Float key : score.keySet())
                {
                    eval = key;
                }
                score.get(score.keySet());
                max_eval = Math.max(max_eval, eval);
                if (max_eval == eval)
                {
                    temp_best_board = state;
                }
            }
            score.put(max_eval, temp_best_board);
            System.out.println("From the algorithm " + temp_best_board.get_game_value());
            return score;
        }
        else
        {
            Map<Float, Board> score = new HashMap<>();
            float min_eval = 999999;
            float eval = 0;
            Board temp_best_board = new Board();
            for (Board state : get_all_moves(board_state, Color.BLACK))
            {
                score.putAll(algorithm(state, depth -1, true));
                for (Float key : score.keySet())
                {
                    eval = key;
                }
                score.get(score.keySet());
                min_eval = Math.min(min_eval, eval);
                if (min_eval == eval)
                {
                    temp_best_board = state;
                }
            }

            score.put(min_eval, temp_best_board);
            return score;
        }
    }

    public List<Board> get_all_moves(Board board, Color color)
    {
        List<Board> moves = new ArrayList<>();

        for (Piece piece : board.get_all_pieces(color)) {
            Map<Point, java.util.List<Piece>> valid_moves = board.get_valid_moves(piece);
            for (Point key : valid_moves.keySet()) {
                Board temp_board = new Board(board);
//                Piece temp_piece = temp_board.get_piece(key.x, key.y);
                List<Piece> skip = valid_moves.get(key);
                Board new_board = simulate_move(piece, key, temp_board, skip);
                moves.add(new_board);
            }
        }
        return moves;
    }


    public Board simulate_move(Piece piece, Point move, Board board, List<Piece> skip)
    {
        board.move_piece(piece, move.x, move.y);
        if (!skip.isEmpty()) {
            for (Piece x : skip)
            {
                board.delete_piece(x.row, x.col);
            }
        }
        return board;
    }
}
