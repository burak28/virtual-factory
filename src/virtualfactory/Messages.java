/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualfactory;

import java.util.concurrent.ArrayBlockingQueue;

/**
 *
 * @author Burak
 */
public class Messages {

    private ArrayBlockingQueue<String> myMessages;

    public Messages() {
        myMessages = new ArrayBlockingQueue<String>(100);
    }

    public synchronized void add(String message) {
        myMessages.add(message);
        notifyAll();
    }

    public synchronized String take() {
        try {
            while (myMessages.isEmpty()) {
                wait();
            }
            return myMessages.poll();

        } catch (InterruptedException e) {
            return e.getMessage();
        }
    }
}
