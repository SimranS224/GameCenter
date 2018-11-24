package fall2018.csc2017.GameCentre.TicTacToe;

import java.util.ArrayList;
import java.util.Random;

public class TicTacMinimaxStrategy extends TicTacStrategy {

    class Move {
        public int id;
        public int score;

        //default constructor
        Move() {
            this.id = -1;
            this.score = -1;
        }
        //constructor with id and score
        Move(int id, int score) {
            this.id = id;
            this.score = score;
        }

    }

    TicTacMinimaxStrategy(int depth) {
        super(depth);
    }

    //return the next position for movement
    public int getNextMovement(TicTacBoardManager boardmanager, int depth) {
        Move move = miniMax(boardmanager.getBoard(), boardmanager.getBoard().getCurrentPlayer(), 0);
        return move.id;

        /*ArrayList<Integer> availableMoves = boardmanager.getValidMoves();
        Random rand = new Random();
        int n = rand.nextInt(availableMoves.size());
        return availableMoves.get(n);*/
    }

    public Move miniMax(TicTacBoard originboard, int current_player, int depth) {

        TicTacBoard board = new TicTacBoard(originboard);
        ArrayList<Integer> validMoves = board.getValidMoves();
        //checks for win, lose, tie and return value accordingly


        if (current_player == board.getPlayer1()  &&  (winning(board, current_player))) {
            //human wins so return -10
            return new Move(-1, -10 + depth);
        } else if (current_player == board.getPlayer2()  &&  (winning(board, current_player))) {
            //AI wins so return -10
            return new Move(-1, 10 - depth);
        } else if (validMoves.size() == 0) {
            //no more room on board for moves
            return new Move(-1, 0);
        }

        //Create a list for moves
        ArrayList<Move> moves = new ArrayList<>();

        //loop through available spots
        for (int i = 0; i < validMoves.size(); i++) {
            Move move = new Move();
            move.id = validMoves.get(i);

            //set spot to current player background
            int row;
            int col;
            row = move.id / board.getCols();
            col = move.id % board.getCols();
            board.setBackground(row, col, board.getPlayerBackground(current_player));

            //collect the score resulting from calling minimax on the opponent of the current player
            Move newMove;
            if (current_player == board.getPlayer1()) {
                newMove = miniMax(board, board.getPlayer2(), depth+ 1);
                move.score = newMove.score;
            } else {
                newMove = miniMax(board, board.getPlayer1(), depth + 1);
                move.score = newMove.score;
            }

            //add the move with id and score in the list
            moves.add(move);
        }

        Move bestMove = new Move();
        //choose move with highest score
        if (current_player == board.getPlayer2()) {
            int bestScore = -10000 ;
            for (int i = 0; i < moves.size(); i++ ) {
                if (moves.get(i).score > bestScore) {
                    bestScore = moves.get(i).score;
                    bestMove.id = moves.get(i).id;
                }
            }
            bestMove.score = bestScore;
        } else {
            // choose move and choose the lowest score
            int bestScore = 10000;
            for (int i = 0; i < moves.size(); i++ ) {
                if (moves.get(i).score < bestScore) {
                    bestScore = moves.get(i).score;
                    bestMove.id = moves.get(i).id;
                }
            }
            bestMove.score = (-1) * bestScore;
        }

        // return the chosen move from the moves arraylist
        return bestMove;

    }

    /**
     * return if the game is winning
     * @param board
     * @param current_player
     * @return true if one of the player wins, false otherwise
     */
    private boolean winning(TicTacBoard board, int current_player) {
        int background_id = 0;
        // after turn, check board to see if anyone has one
        if ((board.getMarker(0,0).getBackgroundId() ==
                board.getMarker(0,1).getBackgroundId()) &&
                ((board.getMarker(0,0).getBackgroundId() ==
                        board.getMarker(0,2).getBackgroundId()))) {
            background_id = board.getMarker(0,1).getBackgroundId();
        } else if ((board.getMarker(1,0).getBackgroundId() ==
                board.getMarker(1,1).getBackgroundId()) &&
                ((board.getMarker(1,0).getBackgroundId() ==
                        board.getMarker(1,2).getBackgroundId()))) {
            background_id = board.getMarker(1,0).getBackgroundId();
        } else if ((board.getMarker(2,0).getBackgroundId() ==
                board.getMarker(2,1).getBackgroundId()) &&
                ((board.getMarker(2,0).getBackgroundId() ==
                        board.getMarker(2,2).getBackgroundId()))) {
            background_id = board.getMarker(2,0).getBackgroundId();
        } else if ((board.getMarker(0,0).getBackgroundId() ==
                board.getMarker(1,0).getBackgroundId()) &&
                ((board.getMarker(2,0).getBackgroundId() ==
                        board.getMarker(1,0).getBackgroundId()))) {
            background_id = board.getMarker(0,0).getBackgroundId();
        } else if ((board.getMarker(0,1).getBackgroundId() ==
                board.getMarker(1,1).getBackgroundId()) &&
                ((board.getMarker(2,1).getBackgroundId() ==
                        board.getMarker(1,1).getBackgroundId()))) {
            background_id = board.getMarker(1,1).getBackgroundId();
        } else if ((board.getMarker(0,2).getBackgroundId() ==
                board.getMarker(1,2).getBackgroundId()) &&
                ((board.getMarker(2,2).getBackgroundId() ==
                        board.getMarker(1,2).getBackgroundId()))) {
            background_id = board.getMarker(2,2).getBackgroundId();
        } else if ((board.getMarker(0,0).getBackgroundId() ==
                board.getMarker(1,1).getBackgroundId()) &&
                ((board.getMarker(2,2).getBackgroundId() ==
                        board.getMarker(1,1).getBackgroundId()))) {
            background_id = board.getMarker(1,1).getBackgroundId();
        } else if ((board.getMarker(2,0).getBackgroundId() ==
                board.getMarker(1,1).getBackgroundId()) &&
                ((board.getMarker(0,2).getBackgroundId() ==
                        board.getMarker(1,1).getBackgroundId()))) {
            background_id = board.getMarker(1,1).getBackgroundId();

        }
        if (background_id == board.getPlayerBackground(current_player)) {
            // current player wins and game is over
            board.setGameOver(true);
            return true;
        }
        return false;

    }

    @Override
    public boolean isValid() {
        return true;
    }
}
