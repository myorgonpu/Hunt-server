package main.java;


import main.java.messaging.Message;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainLoop {

    private Map<User, Message> messageMap = new HashMap<>();

    public void loop(){}

    private void ask(){
        for(User user: ActiveUsers.getUsers()){
            Message message = null;
            try {
                user.getMessenger().recieve(200);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void update(){

    }
}
