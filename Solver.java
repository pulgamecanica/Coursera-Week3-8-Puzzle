import edu.princeton.cs.algs4.MinPQ;
import java.util.Stack;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Solver {

    private class Move implements Comparable<Move> {
        private Move previous;
        private Board board;
        private int numMoves = 0;

        public Move(Board board) {
            this.board = board;
        }

        public Move(Board board, Move previous) {
            this.board = board;
            this.previous = previous;
            this.numMoves = previous.numMoves + 1;
        }

        public int compareTo(Move move) {
            return (this.board.manhattan() - move.board.manhattan()) + (this.numMoves - move.numMoves);
        }
    }

    private Move lastMove;

    public Solver(Board initial) {
        MinPQ<Move> moves = new MinPQ<Move>();
        moves.insert(new Move(initial));

        MinPQ<Move> twinMoves = new MinPQ<Move>();
        twinMoves.insert(new Move(initial.twin()));

        while(true) {
            lastMove = expand(moves);
            if (lastMove != null || expand(twinMoves) != null) return;
        }
    }

    private Move expand(MinPQ<Move> moves) {
        if(moves.isEmpty()) return null;
        Move bestMove = moves.delMin();
        if (bestMove.board.isGoal()) return bestMove;
        for (Board neighbor : bestMove.board.neighbors()) {
            if (bestMove.previous == null || !neighbor.equals(bestMove.previous.board)) {
                moves.insert(new Move(neighbor, bestMove));
            }
        }
        return null;
    }

    public boolean isSolvable() {
        return (lastMove != null);
    }

    public int moves() {
        return isSolvable() ? lastMove.numMoves : -1;
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) return null;

        Stack<Board> moves = new Stack<Board>();
        while(lastMove != null) {
            moves.push(lastMove.board);
            lastMove = lastMove.previous;
        }

        return moves;
    }
    public static void main(String[] args) {
        Board b;
        Solver s;
        try{
            Scanner scanner = new Scanner(new File(args[0]));
            int moves = Integer.parseInt(scanner.nextLine());
            ArrayList<ArrayList<Integer>> board = new ArrayList<>();
            while(scanner.hasNextLine()){
                ArrayList<Integer> box = new ArrayList<>();
                String[] line = scanner.nextLine().split(" ");
                for(int i = 0; i < line.length; i++){
                    if(!line[i].equals(""))
                        box.add(Integer.parseInt(line[i]));
                }
                board.add(box);
            }
            int[][] realBoard = new int[board.size()][board.size()];
            for(int i = 0; i < board.size(); i++)
                for(int j = 0; j < board.get(i).size(); j++)
                    realBoard[i][j] = board.get(i).get(j);
            b = new Board(realBoard);
            System.out.println(2);
            s = new Solver(b);
            System.out.println("Is this puzzle solvable????" + s.isSolvable());
            System.out.println(s.solution());

        }catch(FileNotFoundException e){
            System.err.println("ficheiro nao encontrado");
        }

    }
}
