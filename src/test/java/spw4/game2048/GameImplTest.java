package spw4.game2048;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GameImplTest {

    private GameImpl game;

    @BeforeEach
    public void setup() {
        game = new GameImpl();
    }

    @Test
    public void testInitialize() {
        game.initialize();
        int[][] board = game.getBoard();

        // Ensure there are two random tiles added
        int tileCount = 0;
        for (int[] row : board) {
            for (int value : row) {
                if (value != 0) {
                    tileCount++;
                }
            }
        }
        assertEquals(2, tileCount);

        // Ensure score and moves are reset
        assertEquals(0, game.getScore());
        assertEquals(0, game.getMoves());

        // Ensure won and over are set to false
        assertFalse(game.isWon());
        assertFalse(game.isOver());
    }

    @Test
    public void testUpdateScore() {
        int[][] initialBoard = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {2, 2, 0, 0}
        };
        game.setBoard(initialBoard);

        game.move(Direction.left);
        assertEquals(4, game.getScore());
    }

    @ParameterizedTest
    @CsvSource({"3,0", "0,0", "1, 3"})
    public void testGetValueAt(int row, int col) {
        int[][] initialBoard = {
                {0, 0, 0, 0},
                {0, 0, 4, 0},
                {0, 0, 0, 0},
                {2, 2, 0, 0}
        };
        game.setBoard(initialBoard);

        int actual = game.getValueAt(row, col);
        int expected = initialBoard[row][col];
        assertEquals(expected, actual);
    }

    @Test
    public void testWonGame() {
        int[][] initialBoard = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 1024, 1024}
        };
        game.setBoard(initialBoard);

        game.move(Direction.right);
        assertTrue(game.isWon());
    }

    @Test
    public void testMoveNothingHappens() {
        int[][] initialBoard = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 2, 2}
        };
        game.setBoard(initialBoard);

        game.move(Direction.down);
        assertArrayEquals(initialBoard, game.getBoard());
    }

    @Test
    public void testMove() {
        int[][] initialBoard = {
                {2, 2, 2, 2},
                {2, 2, 2, 2},
                {2, 2, 2, 2},
                {2, 2, 2, 2}
        };

        GameImpl.random = new RandomStub<Double>(List.of(0.0, -1.0, 0.0, -1.0, 0.0, -1.0, 0.0, -1.0));
        game.setBoard(initialBoard);

        game.move(Direction.up);
        game.move(Direction.left);
        game.move(Direction.down);
        game.move(Direction.right);

        int[][] expectedBoard = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 32}
        };

        assertArrayEquals(expectedBoard, game.getBoard());
    }

    @Test
    public void testGameOver() {
        int[][] initialBoard = {
                {2, 4, 8, 16},
                {4, 8, 16, 32},
                {8, 16, 32, 64},
                {16, 32, 64, 128}
        };
        game.setBoard(initialBoard);

        game.move(Direction.right);
        assertTrue(game.isOver());
    }

    @Test
    public void testAlmostGameOver() {
        int[][] initialBoard = {
                {2, 2, 8, 16},
                {4, 8, 16, 32},
                {8, 16, 32, 64},
                {16, 32, 64, 128}
        };
        GameImpl.random = new RandomStub<Double>(List.of(0.0, 1.0));
        game.setBoard(initialBoard);

        game.move(Direction.left);
        assertFalse(game.isOver());
    }

    @Test
    public void testToString() {
        int[][] initialBoard = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 2, 2}
        };
        game.setBoard(initialBoard);

        String status = game.toString();

        String expected = "0\t0\t0\t0\t\n" +
                "0\t0\t0\t0\t\n" +
                "0\t0\t0\t0\t\n" +
                "0\t0\t2\t2\t\n" +
                "Moves: 0\t Score: 0";
        assertEquals(expected, status);
    }
}
