package main.java;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.database.UserRepository;
import main.java.database.UserRepositoryRDB;
import main.java.messaging.Message;
import main.java.messaging.MessageFields;
import main.java.messaging.MessageFormatException;
import main.java.messaging.ServerMessageFactory;
import test.java.RepositoryStub;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MainLoop {

    private static final int ENCOUNTER_DISTANCE = 200;
    private static final int MESSAGE_TIMEOUT_SEC = 15;
    private int encounterTimeoutMillis;

    private UserRepository repository;

    public MainLoop(int encounterTimeoutMillis, UserRepository repository) {
        this.encounterTimeoutMillis = encounterTimeoutMillis;
        this.repository = repository;
    }

    public void loop(){
        while (true){
            Map<User, Message> messages = askUsers();
            updateUsers(messages);
            makeEncounters(checkEncounters());
            try {
                TimeUnit.SECONDS.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print(((RepositoryStub) repository).getScores());
        }
    }

    private Map<User, Message> askUsers(){
        //TODO: проверить сколько длиться
        //TODO: убрать АФК
        final Map<User, Message> messages = new HashMap<>();
        for(User user : ActiveUsers.getUsers()){
            if(user.isInteracting()){
                continue;
            }
            new Thread(()->{
                try {
                    Message serverRequest = ServerMessageFactory.createTrackingRequest();
                    user.getMessenger().send(serverRequest);

                    Message userResponse = user.getMessenger().receive(MESSAGE_TIMEOUT_SEC * 1000 * 0);

                    System.out.println(user.getLogin() + " " + userResponse.toJSON());

                    messages.put(user, userResponse);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }).start();
        }
        return messages;
    }

    private void updateUsers(Map<User, Message> inputMessages){
        Objects.requireNonNull(inputMessages);

        for(Map.Entry<User, Message> pair: inputMessages.entrySet()){
            Message message = pair.getValue();
            try {
                String locationJSON = message.getValue(MessageFields.LOCATION);
                System.out.println(locationJSON);
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

    private Map<User, User> checkEncounters() {
        List<User> hunters = new ArrayList<>();
        List<User> runners = new ArrayList<>();
        Map<User, User> pairs = new HashMap<>();

        System.out.println("check started");

        for(User user: ActiveUsers.getUsers()) {
            if(user.getLocation() == null){
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
        // TODO: определить момент инициализации роли!
        for (Map.Entry<User, User> pair: pairs.entrySet()) {
            User hunter = pair.getKey();
            User runner = pair.getValue();
            new Thread(()->{
               Encounter encounter = new Encounter(hunter, runner, repository, encounterTimeoutMillis);
               encounter.startGameLoop();
            }).start();
        }
    }
}
