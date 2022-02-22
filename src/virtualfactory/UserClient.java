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

/**
 *
 * @author Burak
 */
public class UserClient {

    private static InetAddress host;
    private static final int PORT = 1234;
    private static Scanner serverInput;
    private static PrintWriter serverOutput;

    public static void main(String[] args) throws UnknownHostException, IOException {
        host = InetAddress.getLocalHost();
        Socket socket = new Socket(host, PORT);
        serverInput = new Scanner(socket.getInputStream());
        serverOutput = new PrintWriter(socket.getOutputStream(), true);

        String message, response;
        String[] responseSplit;
        int secim, machineId;
        Scanner scanner = new Scanner(System.in);
        serverOutput.println("USER");
        do {
            System.out.println("USERNAME:");
            message = scanner.nextLine();
            message = "USER " + message;
            serverOutput.println(message);
            response = serverInput.nextLine();
            responseSplit = response.split(" ");
            if (responseSplit[0].equals("ERROR")) {
                System.out.println(responseSplit[1]);
                continue;
            }
            System.out.println("Password:");
            message = scanner.nextLine();
            message = "PASSWORD " + message;
            serverOutput.println(message);
            response = serverInput.nextLine();
            responseSplit = response.split(" ");
            if (responseSplit[0].equals("ERROR")) {
                System.out.println(responseSplit[1]);
                continue;
            }
            break;
        } while (true);

        do {
            System.out.println("MENU");
            System.out.println("1-Sistemdeki makinelere bak");
            System.out.println("2-Makine durumu ve yaptigi isleri ogren");
            System.out.println("3-Sunucuda bekleyen is emirleri");
            System.out.println("4-Is emri gir");
            System.out.println("5-Cikis");
            System.out.println("Secimi giriniz(1-4):");
            secim = scanner.nextInt();
            switch (secim) {
                case 1:
                    System.out.println("Makinenin türünü giriniz:");
                    scanner.nextLine();
                    message = scanner.nextLine();
                    message = "LOOK "+ message;
                    serverOutput.println(message);
                    break;
                case 2:
                    System.out.println("Makine Id giriniz:");
                    machineId = scanner.nextInt();
                    message = "FINDMACH " + Integer.toString(machineId);
                    serverOutput.println(message);
                    break;

                case 3:
                    serverOutput.println("JOBS");
                    break;

                case 4:
                    System.out.println("Yapılacak isi giriniz:");
                    scanner.nextLine();
                    message = scanner.nextLine();
                    System.out.println("Miktar giriniz:");
                    machineId = scanner.nextInt();
                    message = "NEED " + message + " " + Integer.toString(machineId);
                    serverOutput.println(message);
                    break;

                case 5:
                    serverOutput.println("QUIT");
                    break;
                    
                default:
                    System.out.println("Tekrar deneyiniz...");
            }

            if (secim == 4) {
                continue;
            }
            
            if (secim == 5) {
                break;
            }

            do {
                response = serverInput.nextLine();
                responseSplit = response.split(" ");
                if (responseSplit[0].equals("ERROR")) {
                    System.out.println(responseSplit[1]);
                    break;
                }
                if (response.equals("END")) {
                    break;
                }
                if (response.equals("START")) {
                    continue;
                }
                System.out.println(response);
            } while (true);
        } while (true);
    }
}
