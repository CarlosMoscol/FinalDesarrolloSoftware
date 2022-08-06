package galaga_client;
import org.junit.jupiter.api.Test;

class TCPClientTest {
    TCPClient client = new TCPClient("192.0.0.1",8080, null);

    @Test
    void sendMessage() {
        System.out.println("Probando que el método 'sendMessage' envía correctamente el mensaje...");
        String messageOne= "En espera...";
        client.sendMessage(messageOne);
    }

    @Test
    void stopClient() {
        System.out.println("Prueba de que el método 'stopClient' detiene con éxito el servidor del cliente...");
        client.stopClient();
    }

    @Test
    void run() {
        System.out.println("Probar que el método de ejecución 'run' el servidor del cliente correctamente...");
        client.run();
    }
}