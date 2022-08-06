package galaga_game;

import org.junit.jupiter.api.Test;


class GalagaTest {
    Galaga galaga = new Galaga();

    @Test
    void init() {
        System.out.println("Probando la inicialización de todos los hilos galaga...");
        galaga.init();
    }

    @Test
    void startClient() throws InterruptedException {
        System.out.println("Probando la inicialización de un servidor cliente...");
        String IP = "192.0.0.1";
        int PORT = 8008;
        galaga.startClient(IP,PORT);
    }

    @Test
    void setID() {
        System.out.println("Probando la asignación de la ID al objeto Galaga...");
        int ID = 404;
        galaga.setID(ID);
    }

    @Test
    void received() throws InterruptedException {
        System.out.println("Probando si el comando se recibe del servidor...");
        String server = "8080";
        galaga.received(server);
    }
}