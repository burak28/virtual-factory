/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualfactory;

import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

/**
 *
 * @author Burak
 */
public class HandlerMachine extends Thread{
    
    private Socket client;
    private Scanner input;
    private ArrayBlockingQueue<Machine> machines;
    private ArrayBlockingQueue<String> finishedJobs;
    private HandlerSender handlerSender;
    private Messages myMessages;
    
    public HandlerMachine(Socket socket, ArrayBlockingQueue<Machine> machines, ArrayBlockingQueue<String> finishedJobs) throws IOException
    {
        client = socket;
        this.machines = machines;
        this.finishedJobs = finishedJobs;
        input = new Scanner(client.getInputStream());
        myMessages = new Messages();
        handlerSender = new HandlerSender(socket, myMessages);
        handlerSender.start();
    }
    
    @Override
    public void run()
    {
        Machine temp, machine;
        boolean isHave;
        String request = "";
        String[] requestSplit;
        
        do{
            isHave = false;
            request = input.nextLine();
            System.out.println(request);
            requestSplit = request.split(" ");
            if(requestSplit.length == 5){
                machine = new Machine(requestSplit[1],Integer.parseInt(requestSplit[2]),requestSplit[3],Float.parseFloat(requestSplit[4]), input, myMessages);
                
                Iterator iteratorValues = machines.iterator();
                
                while (iteratorValues.hasNext()) {
                    temp = (Machine) iteratorValues.next();
                    if(temp.id == machine.id)
                        isHave = true;
                }
                
                if (isHave)
                {
                    myMessages.add("ERROR ID_IS_NOT_UNIQUE");
                }
                else
                {
                    machines.add(machine);
                    myMessages.add("200 OK");
                    break;
                }
            }
            else
            {
                myMessages.add("ERROR MISSING_INFORMATION");
            }
        }while(true);
        
        do{
            request = input.nextLine();
            System.out.println(request);
            requestSplit = request.split(" ");
            request = requestSplit[1] + " " + requestSplit[2] + " " + Integer.toString(machine.id);
            finishedJobs.add(request);
            machine.condition = "EMPTY";
        }while(true);
    }
}
