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
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;

/**
 *
 * @author Burak
 */
public class HandlerUser extends Thread {

    private Socket client;
    private Scanner input;
    private ArrayBlockingQueue<Machine> machines;
    private ArrayBlockingQueue<String> finishedJobs;
    private HandlerSender handlerSender;
    private Messages myMessages;
    private Jobs jobs;
    private String[] users;

    public HandlerUser(Socket socket, ArrayBlockingQueue<Machine> machines, Jobs jobs, ArrayBlockingQueue<String> finishedJobs, String[] users) throws IOException {
        client = socket;
        this.machines = machines;
        this.finishedJobs = finishedJobs;
        this.jobs = jobs;
        this.users = users;
        input = new Scanner(client.getInputStream());
        myMessages = new Messages();
        handlerSender = new HandlerSender(socket, myMessages);
        handlerSender.start();
    }

    @Override
    public void run() {
        boolean isHave, isLogin, userFound,isQuit = false;
        String request = "", tempFinishedJobs;
        String[] requestSplit, finishedJob, userSplit;
        Machine temp;
        int i, userIndex = 0;

        do {
            isLogin = false;
            userFound = false;
            request = input.nextLine();
            System.out.println(request);
            requestSplit = request.split(" ");
            if (!requestSplit[0].equals("USER") && requestSplit.length == 1) {
                myMessages.add("ERROR NEED_USERNAME");
                continue;
            }
            for(i = 0 ; i< users.length ; i++){
                userSplit = users[i].split(" ");
                if(userSplit[0].equals(requestSplit[1]))
                {
                    userFound = true;
                    myMessages.add("200 OK");
                    request = input.nextLine();
                    requestSplit = request.split(" ");
                    if (!requestSplit[0].equals("PASSWORD") && requestSplit.length == 1) {
                        myMessages.add("ERROR NEED_PASSWORD");
                    }
                    else if(userSplit[1].equals(requestSplit[1]))
                    {
                        if("true".equals(userSplit[2]))
                        {
                            myMessages.add("ERROR THIS_USER_STILL_USING");
                            isLogin = false;
                            break;
                        }
                        userSplit[2] = "true";
                        isLogin = true;
                        users[i] = userSplit[0] + " " + userSplit[1] + " " + userSplit[2];
                        userIndex = i;
                        myMessages.add("200 OK");
                    }else
                    {
                        myMessages.add("ERROR WRONG_PASSWORD");
                        isLogin = false;
                        break;
                    }
                }
            }
            if(!userFound)
                myMessages.add("ERROR USER_NOT_FOUND");
            if(isLogin)
                break;
        } while (true);

        do {
            request = input.nextLine();
            requestSplit = request.split(" ");
            Iterator iteratorValues, iteratorFinishedJobs;
            switch (requestSplit[0]) {
                case "LOOK":

                    isHave = false;
                    iteratorValues = machines.iterator();
                    while (iteratorValues.hasNext()) {
                        temp = (Machine) iteratorValues.next();
                        if (temp.type.equals(requestSplit[1])) {
                            myMessages.add("START");
                            myMessages.add(Integer.toString(temp.id) + " " + temp.name + " " + temp.condition);
                            myMessages.add("END");
                            isHave = true;
                        }
                    }

                    if (!isHave) {
                        myMessages.add("ERROR THERE_IS_NO_MACHINE");
                    }
                    break;

                case "FINDMACH":
                    isHave = false;
                    iteratorValues = machines.iterator();
                    while (iteratorValues.hasNext()) {
                        temp = (Machine) iteratorValues.next();
                        if (temp.id == Integer.parseInt(requestSplit[1])) {
                            myMessages.add("START");
                            myMessages.add(Integer.toString(temp.id) + " " + temp.name + " " + temp.condition);
                            iteratorFinishedJobs = finishedJobs.iterator();
                            while (iteratorFinishedJobs.hasNext()) {
                                tempFinishedJobs = (String) iteratorFinishedJobs.next();
                                finishedJob = tempFinishedJobs.split(" ");
                                if (temp.id == Integer.parseInt(finishedJob[2])) {
                                    myMessages.add(finishedJob[0] + " " + finishedJob[1]);
                                }
                            }
                            myMessages.add("END");
                            isHave = true;
                        }
                    }
                    if (!isHave) {
                        myMessages.add("ERROR NO_MACHINE_WITH_ID");
                    }
                    break;

                case "JOBS":
                    jobs.sendAllJobs(myMessages);
                    break;

                case "NEED":
                    request = requestSplit[1] + " " + requestSplit[2] + " " + UUID.randomUUID().toString();
                    jobs.add(request);
                    break;
                    
                case "QUIT":
                    isQuit = true;
                    userSplit = users[userIndex].split(" ");
                    userSplit[2] = "false";
                    users[userIndex] = userSplit[0] + " " + userSplit[1] + " " + userSplit[2];
                    break;
            }
            if(isQuit)
                break;
        } while (true);
    }
}
