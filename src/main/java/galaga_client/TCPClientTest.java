package galaga_client;


import org.junit.jupiter.api.Test;

class TCPClientTest {
    TCPClient client = new TCPClient("192.0.0.1",8080, null);

    @Test
    void sendMessage() {
        System.out.println("Testing that the sendMessage method correctly sends the message...");
        String messageOne= "Wait...";
        client.sendMessage(messageOne);
    }

    @Test
    void stopClient() {
        System.out.println("Testing that the stopClient method successfully stops the client server...");
        client.stopClient();
    }

    @Test
    void run() {
        System.out.println("Testing that the run method runs the client server correctly...");
        client.run();
    }
}