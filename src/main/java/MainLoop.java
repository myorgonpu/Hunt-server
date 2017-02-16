package main.java;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.messaging.Message;
import main.java.messaging.MessageFields;
import main.java.messaging.MessageFormatException;
import main.java.messaging.ServerMessageFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class MainLoop {

    private static final int MESSAGE_TIMEOUT_SEC = 15;

    public void loop(){
        while (true){
            Map<User, Message> messages = askUsers();
            updateUsers(messages);
        }
    }

    private Map<User, Message> askUsers(){
        //TODO: проверить сколько длиться
        //TODO: убрать АФК
        final Map<User, Message> messages = new HashMap<>();
        for(User user : ActiveUsers.getUsers()){
            new Thread(()->{
                try {
                    Message serverRequest = ServerMessageFactory.createTrackingRequest();
                    user.getMessenger().send(serverRequest);

                    Message userResponse = user.getMessenger().receive(MESSAGE_TIMEOUT_SEC * 1000);
                    messages.put(user, userResponse);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        try {
            TimeUnit.SECONDS.sleep(MESSAGE_TIMEOUT_SEC);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return messages;
    }

    private void updateUsers(Map<User, Message> inputMessages){
        Objects.requireNonNull(inputMessages);

        for(Map.Entry<User, Message> pair: inputMessages.entrySet()){
            Message message = pair.getValue();
            try {
                String locationJSON = message.getValue(MessageFields.LOCATION);
                Location location =
                        new ObjectMapper().readValue(locationJSON, Location.class);
                User user = pair.getKey();
                user.setLocation(location);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sendLocations(new ArrayList(inputMessages.keySet()));
    }

    private void sendLocations(final ArrayList<User> allUsers) {
        for(User receiver: allUsers){
            List<Location> locations = new ArrayList<>();
            for(User user: allUsers){
                if(receiver.equals(user)){
                    continue;
                }
                locations.add(user.getLocation());
            }
            try {
                Message message = ServerMessageFactory.createTrackingListResponse(locations);
                receiver.getMessenger().send(message);

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (MessageFormatException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
