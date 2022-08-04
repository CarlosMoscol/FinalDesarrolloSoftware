package galaga_game;

import galaga_client.GalagaGUI;
import galaga_client.TCPClient;

import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Galaga {
    public int id;
    public int status = 0;
    public char symbol;	//Id del player 1, 2, 3, ...
    public GalagaGUI galagaGUI;
    public TCPClient tcpClient;

    Board newBoard;
    Game newGame;
    private ArrayList<Ship> ships = new ArrayList<>();
    Swarm newSwarm;
    Bomb newBomb;

    public Galaga() {
        this.galagaGUI = new GalagaGUI(this);
        newBoard = new Board();
        newGame = new Game(newBoard, galagaGUI);
        newSwarm = new Swarm(newBoard);
    }

    public void init() {
        status = 1;
        galagaGUI.setMessageText("");
        // initialize all threads
        newSwarm.start();
        newBomb = new Bomb(newBoard, newSwarm, tcpClient, this);
        newBomb.start();

        //for(Ship ship : ships)	new Thread(ship).start();

        try {
            newGame.join();
        }
        catch (InterruptedException err) {
            err.printStackTrace();
        }

    }

    public void startClient(String IP, int PORT) throws InterruptedException {
        tcpClient = new TCPClient(IP, PORT, new TCPClient.OnMessageReceived() {
            @Override
            public void messageReceived(String message) throws InterruptedException {
                System.out.println(message);
                received(message);
            }
        });
        new Thread(tcpClient).start();
        Thread.sleep(10);
        new Thread(newGame).start();
    }

    public void setID(int id) {
        this.id = id;
        this.symbol = (char) (id + '0');
        System.out.println("id " + id);
        System.out.println("symbol " + symbol);
    }

    public void addNewShip(String addNewShipMessage) {
        String[] message = addNewShipMessage.split(" ");
        int numberShips = Integer.parseInt(message[1]);
        if(ships.size() == 0) {
            for(int i = 0; i < numberShips; i++) {
                char symbol = (char) (i + '0');
                int newPos = Integer.parseInt(message[i + 2]);
                ships.add(new Ship(symbol, newPos, 1, newBoard));
                ships.get(i).start();
            }
        }
        else {
            for(int i = 0; i < numberShips; i++) {
                int newPos = Integer.parseInt(message[i + 2]);
                if(i != numberShips - 1) ships.get(i).moveTo(newPos);
                else {
                    char symbol = (char) (i + '0');
                    ships.add(new Ship(symbol, newPos, 1, newBoard));
                    ships.get(i).start();
                }
            }
        }
    }

    public void received(String message) throws InterruptedException {
        int idx;
        System.out.println("Server > " + message);
        if(message != null && !message.equals("")) {
            String[] splitted = message.split(" ");
            String command = splitted[0];
            switch (command) {
                case "id":
                    int id = Integer.parseInt(splitted[1]);
                    setID(id);
                    break;
                case "players":
                    addNewShip(message);
                    break;
                case "start":
                    init();
                    break;
                case "pos":
                    idx = Integer.parseInt(splitted[1]);
                    int pos = Integer.parseInt(splitted[2]);
                    ships.get(idx).moveTo(pos);
                    break;
                case "shoot":
                    idx = Integer.parseInt(splitted[1]);
                    ships.get(idx).shoot();
                    break;
                case "status":
                    idx = Integer.parseInt(splitted[1]);
                    int status = Integer.parseInt(splitted[2]);
                    ships.get(idx).boom();
                    ships.get(idx).setStatus(status);
            }
        }
    }
}

class Board {
    public int gameState, shipState, swarmState, shipFrontPos, swarmFrontPos;
    public int height = 11, width = 39;
    public char[] line0 = { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
    public char[] line1 = { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
    public char[] line2 = { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
    public char[] line3 = { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
    public char[] line4 = { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
    public char[] line5 = { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
    public char[] line6 = { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
    public char[] line7 = { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
    public char[] line8 = { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
    public char[] line9 = { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
    public char[] line10 = { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};

    public List<char[]> boardList = new ArrayList<>();

    //Establece estados en 1 y agrega los espacios en blanco a board
    Board() {
        gameState = 1;
        shipState = 1;
        swarmState = 1;
        boardList.add(line0);
        boardList.add(line1);
        boardList.add(line2);
        boardList.add(line3);
        boardList.add(line4);
        boardList.add(line5);
        boardList.add(line6);
        boardList.add(line7);
        boardList.add(line8);
        boardList.add(line9);
        boardList.add(line10);
    }

    public String draw() {
        String board = "";

        // Dibuja la pantalla de la batalla
        for(char[] line : boardList) {
            board += ("|");
            for (int i = 0; i < width; i++) {
                board += (line[i]);
            }
            board += ("|\n");
        }

        return board;
    }

    //Cuenta el numero de enemigos
    public int countSwarm(){
        int count = 0;
        for (char[] element : boardList) {
            for (int i = 0; i < width; i++) {
                if (element[i] == '*')	count++;
            }
        }
        return count;
    }
}


class Game extends Thread {
    public int state = 0;
    private Board board;
    private GalagaGUI galagaGUI = null;

    Game(Board board, GalagaGUI galagaGUI) {
        this.board = board;
        this.galagaGUI = galagaGUI;
    }

    @ Override
    public void run() {
        try {
            // Mientras el juego no haya acabado se va a imprimir la pantalla del juego cada 75 ms
            while (!gameOver()) {
                if(galagaGUI != null) {
                    String boardText = board.draw();
                    galagaGUI.setBoardText(boardText);
                    Thread.sleep(80);
                }

            }
        }
        catch (InterruptedException err) {
            err.printStackTrace();
        }
    }

    //
    public synchronized void setMessage() {
        String message = "";
        if (board.shipState == 0) {
            message += ("+=====================+\n");
            message += ("+== G A M E O V E R ==+\n");
            message += ("+=====================+\n");
        }
        else {
            message += ("+=====================+\n");
            message += ("+==== YOU  WIN!!! ====+\n");
            message += ("+=====================+\n");
        }
        if(galagaGUI != null)	galagaGUI.setMessageText(message);
    }

    //Verifica si es que ya existe un perdedor
    private synchronized boolean gameOver(){
        return false;
		/*if (board.swarmState == 0 || board.shipState == 0)
			return true;
		else
			return false;*/
    }

}

