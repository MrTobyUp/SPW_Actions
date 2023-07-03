package spw4.game2048;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameImpl implements Game {
    private static final int SIZE = 4;
    public static Random random;
    private int[][] board;
    private int score;
    private int moves;
    private boolean won;
    private boolean over;


    public GameImpl() {
        random = new Random();
        // Initialize the game board
        board = new int[SIZE][SIZE];
        score = 0;
        moves = 0;
        won = false;
        over = false;
    }

    public int getMoves() {
        return moves;
    }

    public int getScore() {
        return score;
    }

    public int getValueAt(int row, int col) {
        return board[row][col];
    }

    public boolean isOver() {
        return over;
    }

    public boolean isWon() {
        return won;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : board) {
            for (int value : row) {
                sb.append(value).append("\t");
            }
            sb.append("\n");
        }
        sb.append("Moves: ").append(moves).append("\t Score: ").append(score);
        return sb.toString();
    }

    public void initialize() {
        // Reset the game board
        board = new int[SIZE][SIZE];
        score = 0;
        moves = 0;
        won = false;
        over = false;

        // Add two random tiles at the beginning
        addRandomTile();
        addRandomTile();
    }

    public void move(Direction direction) {
        boolean moved = false;

        switch (direction) {
            case up:
                moved = moveTiles(-1, 0);
                break;
            case down:
                moved = moveTiles(1, 0);
                break;
            case left:
                moved = moveTiles(0, -1);
                break;
            case right:
                moved = moveTiles(0, 1);
                break;
        }

        if (moved) {
            moves++;
            addRandomTile();
        }

        over = isGameOver();
    }

    private boolean moveTiles(int rowOffset, int colOffset) {
        boolean moved = false;

        int startRow = rowOffset >= 0 ? SIZE - 1 : 0;
        int startCol = colOffset >= 0 ? SIZE - 1 : 0;

        int endRow = rowOffset >= 0 ? -1 : SIZE;
        int endCol = colOffset >= 0 ? -1 : SIZE;

        int stepRow = rowOffset >= 0 ? -1 : 1;
        int stepCol = colOffset >= 0 ? -1 : 1;

        for (int row = startRow; row != endRow; row += stepRow) {
            for (int col = startCol; col != endCol; col += stepCol) {
                int newRow = row + rowOffset;
                int newCol = col + colOffset;

                if (isValidCell(newRow, newCol) && board[row][col] != 0) {
                    while (isValidCell(newRow, newCol) && board[newRow][newCol] == 0) {
                        board[newRow][newCol] = board[row][col];
                        board[row][col] = 0;
                        row = newRow;
                        col = newCol;
                        newRow += rowOffset;
                        newCol += colOffset;
                        moved = true;
                    }

                    if (isValidCell(newRow, newCol) && board[newRow][newCol] == board[row][col]) {
                        board[newRow][newCol] *= 2;
                        if (board[newRow][newCol] == 2048) {
                            won = true;
                        }
                        score += board[newRow][newCol];
                        board[row][col] = 0;
                        moved = true;
                    }
                }
            }
        }

        return moved;
    }

    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    public void addRandomTile() {
        List<int[]> emptyCells = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == 0) {
                    emptyCells.add(new int[]{i, j});
                }
            }
        }

        if (emptyCells.isEmpty()) {
            return;
        }

        int[] cell = emptyCells.get((int) (random.nextDouble() * emptyCells.size()));
        double insertValue =  random.nextDouble();
        if (insertValue >= 0.9) {
            board[cell[0]][cell[1]] = 4;
        } else if (insertValue >= 0) {
            board[cell[0]][cell[1]] = 2;
        } else {
            board[cell[0]][cell[1]] = 0;
        }
    }

    private boolean isGameOver() {
        // Check if there are any empty cells
        for (int[] row : board) {
            for (int value : row) {
                if (value == 0) {
                    return false;
                }
            }
        }

        // Check if there are any adjacent cells with the same value
        for (int row = 1; row < SIZE - 1; row++) {
            for (int col = 1; col < SIZE - 1; col++) {
                int value = board[row][col];
                if (board[row - 1][col] == value || board[row + 1][col] == value ||
                        board[row][col - 1] == value || board[row][col + 1] == value) {
                    return false;
                }
            }
        }

        return true;
    }

    public void setBoard(int[][] board){
        this.board = board;
    }

    public int[][] getBoard(){
        return board;
    }
}