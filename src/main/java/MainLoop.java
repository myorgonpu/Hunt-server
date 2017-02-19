package main.java;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.database.UserRepository;
import main.java.messaging.Message;
import main.java.messaging.MessageFields;
import main.java.messaging.MessageFormatException;
import main.java.messaging.ServerMessageFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class MainLoop {

    private static final int ENCOUNTER_DISTANCE = 200;
    private static final int MESSAGE_TIMEOUT_SEC = 10;
    public static final int MIN_REST_TIME_SEC = 5;
    public static final int LOOP_SLEEP_SEC = 5;

    private UserRepository repository;

    public MainLoop(UserRepository repository) {
        this.repository = repository;
    }

    public void loop(){
        while (true){
            askUsers();
            try {
                TimeUnit.SECONDS.sleep(LOOP_SLEEP_SEC);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            updateUsers(getUserResponse());
            makeEncounters(checkEncounters());
        }
    }

    private void askUsers(){
        Message serverRequest = ServerMessageFactory.createTrackingRequest();
        for(User user : ActiveUsers.getUsers()) {
            try {
                user.getMessenger().send(serverRequest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Map<User, Message> getUserResponse() {
        final Map<User, Message> messages = new HashMap<>();
        ArrayList<Thread> threads = new ArrayList<>();
        for(User user : ActiveUsers.getUsers()) {
            Thread thread = new Thread(() -> {
                try {
                    Message userResponse = user.getMessenger().receive(MESSAGE_TIMEOUT_SEC * 1000);
                    messages.put(user, userResponse);
                    System.out.println("SERVER| " + user.getLogin() + " sent " + userResponse.toJSON());
                    user.setAFK(false);
                } catch (Exception e) {
                    e.printStackTrace();
                    user.setAFK(true);
                }
            });
            threads.add(thread);
            thread.start();
        }
        for (Thread t: threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return messages;
    }


    private void updateUsers(Map<User, Message> inputMessages){
        Objects.requireNonNull(inputMessages);

        for(Map.Entry<User, Message> pair: inputMessages.entrySet()){
            Message message = pair.getValue();
            try {
                String locationJSON = message.getValue(MessageFields.LOCATION);
                Location location = new ObjectMapper().readValue(locationJSON, Location.class);
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

                System.out.println(message.toJSON());

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (MessageFormatException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Map<User, User> checkEncounters() {
        List<User> hunters = new ArrayList<>();
        List<User> runners = new ArrayList<>();
        Map<User, User> pairs = new HashMap<>();

        for(User user: ActiveUsers.getUsers()) {
            if(user.getLocation() == null || user.isInteracting()
            ||(System.currentTimeMillis() - user.getLastEncounterTime() < MIN_REST_TIME_SEC * 1000)  ){
                continue;
            }
            switch (user.getRole()) {
                case HUNTER:
                    hunters.add(user);
                    break;
                case RUNNER:
                    runners.add(user);
                    break;
            }
        }

        for (User runner : runners) {
            List<Double> distances = new ArrayList<>();
            for (User hunter : hunters) {
                distances.add(runner.getLocation().distanceTo(hunter.getLocation()));
            }

            double distance = Collections.min(distances);
            if (distance <= ENCOUNTER_DISTANCE) {
                User hunter = hunters.get(distances.indexOf(distance));
                hunters.remove(hunter);
                pairs.put(hunter, runner);
            }
        }
        return pairs;
    }

    private void makeEncounters(Map<User,User> pairs) {
        for (Map.Entry<User, User> pair: pairs.entrySet()) {
            User hunter = pair.getKey();
            User runner = pair.getValue();
            new Thread(()->{
               Encounter encounter = new Encounter(hunter, runner, repository);
               encounter.startGameLoop();
            }).start();
        }
    }
}
