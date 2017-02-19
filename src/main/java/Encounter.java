package main.java;

import main.java.database.UserRepository;
import main.java.messaging.*;

import java.io.IOException;

public class Encounter {
    public static final int ENCOUNTER_TIME = 15; // TODO: TEMPORARY FOR TESTS
    public static final int HUNTING_DISTANCE = 200;
    public static final int HUNTING_TIME = 10;
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
            return;
        }

        this.hunter.setInteracting(true);
        this.runner.setInteracting(true);

        long startTime = System.currentTimeMillis();
        while(System.currentTimeMillis() - startTime < ENCOUNTER_TIME * 1000 /* TODO * 60 */){
            double distance = hunter.getLocation().distanceTo(runner.getLocation());
            if(distance < HUNTING_DISTANCE){
                boolean huntersWin = startHunting();
                if(huntersWin){
                    submitResults(hunter);
                    return;
                }
            }
        }
        submitResults(runner);
    }

    private boolean startHunting() {
        try {
            notifyUsers(ServerMessageFactory.createCatchingStartEvent());
        } catch (MessageFormatException e) {
            e.printStackTrace();
        }

        long startTime = System.currentTimeMillis();
        while(System.currentTimeMillis() - startTime < HUNTING_TIME * 1000){
            double distance = hunter.getLocation().distanceTo(runner.getLocation());
            boolean notNear = distance > HUNTING_DISTANCE;
            if(notNear){
                return false;
            }
        }
        return true;
    }

    private void submitResults(User winner) {
        this.hunter.setInteracting(false);
        this.runner.setInteracting(false);

        this.hunter.setLastEncounterTime(System.currentTimeMillis());
        this.runner.setLastEncounterTime(System.currentTimeMillis());

        winner.setScore(winner.getScore() + 1);
        repository.update(winner);
        try {
            notifyUsers(ServerMessageFactory.createEncounterEndEvent(winner.getRole()));
        } catch (MessageFormatException e) {
            e.printStackTrace();
        }
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


/*
timestamp of encounter start
loop(!encounter done):
    if users are nearly
        hunterWin = startHunting
        if(hunterWin)
            submitResults(hunter)
            return;
submitResults(runner)
 */

/* hunting
start = timestamp of hunting start
loop( now - start < 15 sec):
    if(!near)
        return false;
    continue;
return true;
 */
