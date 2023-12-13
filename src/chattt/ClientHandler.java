package chattt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private Server server;
    private PrintWriter writer;
    private String clientName;

    public ClientHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    public String getClientName() {
        return clientName;
    }

    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream(), true);

            // Demander et enregistrer le nom du client
            clientName = reader.readLine();
            System.out.println("Client " + clientName + " connected.");

            // Informer tous les clients du nouveau client connecté
            server.broadcastMessage("Client " + clientName + " joined the chat.", this);

            String clientMessage;
            while ((clientMessage = reader.readLine()) != null) {
                // Diffuser le message à tous les autres clients
                server.broadcastMessage(clientName + ": " + clientMessage, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Retirer le client de la liste et informer les autres clients de la déconnexion
            server.removeClient(this);
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
    }
}