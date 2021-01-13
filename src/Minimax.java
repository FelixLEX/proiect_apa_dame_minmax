import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Minimax {
    public Map<Float, Board> algorithm(Board board_state, int depth, float alpha, float beta, boolean max_player)
    {
        Map<Float, Board> best = new HashMap<>();

        if (depth == 0 || board_state.check_winner() != Color.GREEN)
        {
            System.out.println("Retrurned branch value " + board_state.get_game_value());
            best.put(board_state.get_game_value(), board_state);
            return best;
        }

        if(max_player)
        {
            Map<Float, Board> score = new HashMap<>();
            float max_eval = -999999;
            float eval = 0;
            Board temp_best_board = new Board();
            for (Board state : get_all_moves(new Board(board_state), Color.WHITE))
            {
                score.putAll(algorithm(state, depth -1, alpha, beta, false));
                for (Float key : score.keySet())
                {
                    eval = key;
                }
                score.get(score.keySet());
                max_eval = Math.max(max_eval, eval);
                if (max_eval == eval)
                {
                    temp_best_board = new Board(state);
                }
                alpha = Math.max(alpha, max_eval);
                if (beta <= alpha)
                {
                    break;
                }
            }
            score = new HashMap<>();
            score.put(max_eval, temp_best_board);
            return score;
        }
        else
        {
            Map<Float, Board> score = new HashMap<>();
            float min_eval = 999999;
            float eval = 0;
            Board temp_best_board = new Board();
            for (Board state : get_all_moves(new Board(board_state), Color.BLACK))
            {
                score.putAll(algorithm(state, depth -1, alpha, beta, true));
                for (Float key : score.keySet())
                {
                    eval = key;
                }
                score.get(score.keySet());
                min_eval = Math.min(min_eval, eval);
                if (min_eval == eval)
                {
                    temp_best_board = new Board(state);
                }
                beta = Math.min(beta, min_eval);
                if (beta <= alpha)
                {
                    break;
                }
            }
            score = new HashMap<>();
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
                List<Piece> skip = valid_moves.get(key);
                Board new_board = new Board((simulate_move(new Point(piece.row, piece.col), key, temp_board, skip)));
                moves.add(new_board);
            }
        }
        return moves;
    }


    public Board simulate_move(Point piece, Point move, Board board, List<Piece> skip)
    {
        Board next_board = new Board(board);
        Piece piece_to_move = next_board.get_piece(piece.y, piece.x);
        next_board.move_piece(piece_to_move, move.x, move.y);
        if (!skip.isEmpty()) {
            for (Piece x : skip)
            {
                next_board.delete_piece(x.row, x.col);
            }
        }
        return next_board;
    }
}
