package main.java;

import main.java.database.UserRepository;
import main.java.messaging.*;

import java.io.IOException;

public class Encounter {
    public int encounterTimeoutMillis;
    private User hunter;
    private User runner;
    private UserRepository repository;

    public Encounter(User hunter, User runner, UserRepository repository, int encounterTimeoutMillis) {
        this.hunter = hunter;
        this.runner = runner;
        this.repository = repository;
        this.encounterTimeoutMillis = encounterTimeoutMillis;
    }

    public void startGameLoop() {
        try {
            notifyUsers(ServerMessageFactory.createEncounterStartEvent());
        } catch (MessageFormatException e) {
            e.printStackTrace();
        }

        try {
            Message hunterResponse = hunter.getMessenger().receive(encounterTimeoutMillis);;
            Message runnerResponse = runner.getMessenger().receive(encounterTimeoutMillis);;
            while (true) {
                if(hunterResponse != null && !hunterResponse.getValue(MessageFields.TARGET).equals(Target.ENCOUNTER.getName())){
                    hunterResponse = hunter.getMessenger().receive(encounterTimeoutMillis);
                }
                if(runnerResponse != null && !runnerResponse.getValue(MessageFields.TARGET).equals(Target.ENCOUNTER.getName())){
                    runnerResponse = runner.getMessenger().receive(encounterTimeoutMillis);
                }

                if(runnerResponse.toJSON().equals(hunterResponse.toJSON())) {
                    switch (hunterResponse.getValue(MessageFields.WINNER)) {
                        case "hunter":
                            submitResults(hunter);
                            break;
                        case "runner":
                            submitResults(runner);
                            break;
                    }
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
