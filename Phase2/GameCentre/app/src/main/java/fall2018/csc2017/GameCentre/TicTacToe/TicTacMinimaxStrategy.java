package fall2018.csc2017.GameCentre.TicTacToe;

import java.util.ArrayList;
import java.util.Random;

/**
 * Minimax strategy class for AI
 */
public class TicTacMinimaxStrategy extends TicTacStrategy {

    /**
     * a class for the the moves of the AI in minimax
     */
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

    /**
     * call super class Strategy
     * @param depth
     */
    public TicTacMinimaxStrategy(int depth) {
        super(depth);
    }

    //return the next position for movement

    /**
     * gets the next move for the AI player, uses recursion
     * @param boardmanager looks at the board
     * @param depth used to calculate next move
     * @return the move for the ai
     */
    public int getNextMovement(TicTacBoardManager boardmanager, int depth) {
        Move move = miniMax(boardmanager, boardmanager.getBoard().getCurrentPlayer(), 0);
        return move.id;
    }

    /**
     * calculates the best possible move for the get nextmovement
     * @param boardManager looks at the board to see the valid moves
     * @param current_player switches between players to see all the moves each player can make
     * @param depth used to help calcualte the best move
     * @return
     */
    public Move miniMax(TicTacBoardManager boardManager, int current_player, int depth) {

        TicTacBoard board = new TicTacBoard(boardManager.getBoard());
        ArrayList<Integer> validMoves = boardManager.getValidMoves();
        //checks for win, lose, tie and return value accordingly

        //(boardManager.getWinner(current_player)))
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
            TicTacBoardManager newBoardManager = new TicTacBoardManager(board);
            if (current_player == board.getPlayer1()) {
                newMove = miniMax(newBoardManager, board.getPlayer2(), depth+ 1);
                move.score = newMove.score;
            } else {
                newMove = miniMax(newBoardManager, board.getPlayer1(), depth + 1);
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

    /**
     * returns if the strategy is valid
     * @return true because random strategy is AI strategy
     */
    @Override
    public boolean isValid() {
        return true;
    }
}