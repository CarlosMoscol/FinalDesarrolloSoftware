package galaga_game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    Board board = new Board();

    @Test
    void draw() {
        System.out.println("Probando el tablero de la batalla en la pantalla ...");
        System.out.println(board.draw());
    }

    @Test
    void countSwarm() {
        System.out.println("Probando el conteo del número de enemigos actuales...");
        System.out.println(board.countSwarm());
    }
}
