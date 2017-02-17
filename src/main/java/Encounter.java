package main.java;


import com.sun.xml.internal.bind.marshaller.MinimumEscapeHandler;
import main.java.database.UserRepository;
import main.java.messaging.Message;
import main.java.messaging.MessageFields;
import main.java.messaging.MessageFormatException;
import main.java.messaging.ServerMessageFactory;

import java.io.IOException;

public class Encounter {
    public static final int ENCOUNTER_TIMEOUT_MILLIS = 2000000;
    private User hunter;
    private User runner;
    private UserRepository repository;

    public Encounter(User hunter, User runner, UserRepository repository) {
        this.hunter = hunter;
        this.runner = runner;
        this.repository = repository;
    }

    public void startGameLoop() {

        try {
            notifyUsers(ServerMessageFactory.createEncounterStartEvent());
        } catch (MessageFormatException e) {
            e.printStackTrace();
        }


        try {
            Message hunterResponse = hunter.getMessenger().receive(ENCOUNTER_TIMEOUT_MILLIS);
            Message runnerResponse = runner.getMessenger().receive(ENCOUNTER_TIMEOUT_MILLIS);
            if(hunterResponse.toJSON().equals(runnerResponse.toJSON())) {
                switch (hunterResponse.getValue(MessageFields.WINNER)) {
                    case "hunter":
                        submitResults(hunter);
                        break;
                    case "runner":
                        submitResults(runner);
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            submitResults(runner);
        }

        ActiveUsers.getUsers().add(hunter);
        ActiveUsers.getUsers().add(runner);
    }

    private void submitResults(User winner) {
        winner.setScore(winner.getScore() + 1);
        repository.update(winner);
    }

    private void notifyUsers(Message message) {
        try {
            hunter.getMessenger().send(message);
            runner.getMessenger().send(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
