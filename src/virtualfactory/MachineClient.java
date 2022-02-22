/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualfactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Burak
 */
public class MachineClient extends Thread {

    String name;
    int id;
    String type;
    float speed;
    String condition;
    String workTime;
    boolean isStarted = false;
    private static InetAddress host;
    private static final int PORT = 1234;
    private static Scanner serverInput;
    private static PrintWriter serverOutput;

    public void createMachine() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Machine Name:");
        name = scanner.nextLine();
        System.out.println("Machine Id:");
        id = Integer.parseInt(scanner.nextLine());
        System.out.println("Machine Type:");
        type = scanner.nextLine();
        System.out.println("Machine Speed:");
        speed = scanner.nextFloat();
        condition = "EMPTY";
    }

    @Override
    public void run() {
        while(true)
        {
            System.out.print("");
            if (this.isStarted) {
                this.condition = "BUSY";
                float workMin = Float.parseFloat(this.workTime);
                workMin = (workMin / this.speed) * 60000;
                System.out.println(workMin);
                try {
                    Thread.sleep((long) workMin);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MachineClient.class.getName()).log(Level.SEVERE, null, ex);
                }

                System.out.println("Fınısh Work");
                serverOutput.println("DONE " + this.type + " " + this.workTime);
                this.condition = "EMPTY";
                this.isStarted = false;
            }
        }
    }

    public static void main(String[] args) throws UnknownHostException, IOException {
        host = InetAddress.getLocalHost();
        Socket socket = new Socket(host, PORT);
        serverInput = new Scanner(socket.getInputStream());
        serverOutput = new PrintWriter(socket.getOutputStream(), true);

        String message, response;
        String[] responseSplit;
        MachineClient machine = new MachineClient();
        serverOutput.println("MACH");
        do {
            machine.createMachine();
            message = "MACH " + machine.name + " " + Integer.toString(machine.id) + " " + machine.type + " " + Float.toString(machine.speed);
            serverOutput.println(message);
            response = serverInput.nextLine();
            responseSplit = response.split(" ");
            if (responseSplit[0].equals("200")) {
                System.out.println("Client accepted...");
                break;
            } else {
                System.out.println(responseSplit[1]);
            }
        } while (true);

        machine.start();

        do {
            response = serverInput.nextLine();
            responseSplit = response.split(" ");
            if ("EMPTY".equals(machine.condition) && responseSplit[0].equals("WORK") && !machine.isStarted) {
                System.out.println("Start Working...");
                machine.workTime = responseSplit[1];
                machine.isStarted = true;
            }

        } while (true);
    }
}
