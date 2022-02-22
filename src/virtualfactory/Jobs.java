/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualfactory;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

/**
 *
 * @author Burak
 */
public class Jobs extends Thread {

    private ArrayBlockingQueue<String> myJobs;
    private ArrayBlockingQueue<Machine> myMachines;

    public Jobs(ArrayBlockingQueue<Machine> machines) {
        myMachines = machines;
        myJobs = new ArrayBlockingQueue<String>(100);
    }

    public synchronized void add(String message) {
        myJobs.add(message);
        notifyAll();
    }

    public synchronized void sendAllJobs(Messages message) {
        String job;
        if (myJobs.size() != 0) {
            message.add("START");
            Iterator iteratorValues;
            iteratorValues = myJobs.iterator();
            while (iteratorValues.hasNext()) {
                job = (String) iteratorValues.next();
                message.add(job);
            }
            message.add("END");
        } else {
            message.add("ERROR HAVE_NO_JOB");
        }
        notifyAll();
    }

    @Override
    public void run() {
        Machine machine;
        String message;
        boolean isDone;
        Iterator iteratorValues;
        String[] messageSplit;

        do {
            System.out.print("");
            while (!myJobs.isEmpty()) {
                message = myJobs.peek();
                isDone = false;
                messageSplit = message.split(" ");
                if (!myMachines.isEmpty()) {
                    iteratorValues = myMachines.iterator();
                    while (iteratorValues.hasNext()) {
                        machine = (Machine) iteratorValues.next();
                        if (machine.type.equals(messageSplit[0]) && "EMPTY".equals(machine.condition)) {
                            machine.myMessages.add("WORK " + messageSplit[1]);
                            machine.condition = "BUSY";
                            isDone = true;
                            break;
                        }
                    }
                    if (isDone) {
                        myJobs.poll();
                    } else {
                        message = myJobs.poll();
                        myJobs.add(message);
                    }
                }
            }
        } while (true);

    }
}
