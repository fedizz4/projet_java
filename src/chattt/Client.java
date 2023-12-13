package chattt;
import java.io.BufferedReader;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        new Client().startClient("192.168.1.121", 5555);
    }

    public void startClient(String serverAddress, int serverPort) {
        try {
            Socket socket = new Socket(serverAddress, serverPort);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            // Reading and printing server messages in a separate thread
            new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = reader.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Sending user input to the server
            System.out.print("Enter your name: ");
            String userName = consoleReader.readLine();
            writer.println(userName);

            String userInput;
            while ((userInput = consoleReader.readLine()) != null) {
                writer.println(userInput);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
