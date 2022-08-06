package galaga_game;

import org.junit.jupiter.api.Test;


class GalagaTest {
    Galaga galaga = new Galaga();

    @Test
    void init() {
        System.out.println("Testing initialization of all galaga threads...");
        galaga.init();
    }

    @Test
    void startClient() throws InterruptedException {
        System.out.println("Testing the initialization of a client server...");
        String IP = "192.0.0.1";
        int PORT = 8008;
        galaga.startClient(IP,PORT);
    }

    @Test
    void setID() {
        System.out.println("Testing the assignment of the ID to the Galaga object...");
        int ID = 404;
        galaga.setID(ID);
    }

    @Test
    void received() throws InterruptedException {
        System.out.println("testing if the command is received from the server...");
        String server = "8080";
        galaga.received(server);
    }
}