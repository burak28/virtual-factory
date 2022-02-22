package virtualfactory;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Burak
 */
public class ServerFactory {

    private static Jobs jobs;
    private static ArrayBlockingQueue<Machine> machines;
    private static ArrayBlockingQueue<String> finishedJobs;
    private static String[] users;
    private static ServerSocket serverSocket;
    public static final int PORT = 1234;

    public static void main(String[] args) throws IOException {
        
        finishedJobs = new ArrayBlockingQueue<String>(100);
        users = new String[]{"ali password false", "user password123 false", "user2 word123 false", "user3 password2 false"};
        machines = new ArrayBlockingQueue<Machine>(100);
        jobs = new Jobs(machines);
        jobs.start();
        
        try {
            System.out.println("Starting Server...");
            serverSocket = new ServerSocket(PORT);
        } catch (IOException ex) {
            Logger.getLogger(ServerFactory.class.getName()).log(Level.SEVERE, null, ex);
        }

        do {
            Socket client = serverSocket.accept();
            System.out.println("Client accepted...");

            ClientHandler handler = new ClientHandler(client, machines, jobs, finishedJobs, users);

            handler.start();

        } while (true);
    }
}

class ClientHandler extends Thread {

    private Socket client;
    private Scanner input;
    private ArrayBlockingQueue<Machine> machines;
    private ArrayBlockingQueue<String> finishedJobs;
    private String[] users;
    private Jobs jobs;
    
    public ClientHandler(Socket socket, ArrayBlockingQueue<Machine> machines, Jobs jobs, ArrayBlockingQueue<String> finishedJobs, String[] users) {
        this.jobs = jobs;
        client = socket;
        this.machines = machines;
        this.finishedJobs = finishedJobs;
        this.users = users;
        
        try {
            input = new Scanner(client.getInputStream());

        } catch (IOException ex) {
            
        }
    }

    @Override
    public void run() 
    {
        String request = "";
        request = input.nextLine();
        System.out.println(request);
        if(request.equals("MACH"))
        {
            HandlerMachine handler = null;
            try {
                handler = new HandlerMachine(client, machines, finishedJobs);
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            handler.start();
        }
        else if(request.equals("USER"))
        {
            HandlerUser handler = null;
            try {
                handler = new HandlerUser(client, machines, jobs, finishedJobs, users);
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            handler.start();
        }

    }
}
