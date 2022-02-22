/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualfactory;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Burak
 */
public class Machine {
    
    String name;
    int id;
    String type;
    float speed;
    String condition; 
    Scanner input;
    Messages myMessages;
    
    public Machine(String name, int id, String type, float speed, Scanner input, Messages myMessages)
    {
        this.name = name;
        this.id = id;
        this.type = type;
        this.speed = speed;
        this.condition = "EMPTY";
        this.input = input;
        this.myMessages = myMessages;
    }
}
