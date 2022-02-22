/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualfactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Burak
 */
public class HandlerSender extends Thread{
    
    private Socket client;
    private PrintWriter output;
    private Messages myMessages;
    
    public HandlerSender(Socket socket , Messages msg){
        client = socket;
        myMessages = msg;
        try
        {
            output = new PrintWriter(client.getOutputStream(),true);
        }
        catch(IOException ioEx)
        {
        }
    }
    
    @Override
    public void run(){
        String message;
        do{
            message =  myMessages.take(); 
            output.println(message);
        }
        while(!"QUIT".equals(message));
        
        try
        {
            if (client!=null)
            {
                System.out.println("Closing down connection...");
                client.close();
            }
        }
        catch(IOException ioEx)
        {
                System.out.println("Unable to disconnect!");
        }
    }
}
