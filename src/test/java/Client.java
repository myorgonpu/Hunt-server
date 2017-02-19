package test.java;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.Location;
import main.java.Role;
import main.java.messaging.*;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 1234;
    public static final int TIMEOUT = 0*5*1000;

    private Messenger messenger;
    private Location location;
    private Role role;
    private String login;

    public Client(String login, String password, Role role, Location location) throws IOException {
        this.role = role;
        this.location = location;
        this.login = login;
        Socket socket = new Socket(HOST, PORT);
        messenger = new Messenger(socket);

        messenger.send(makeAuth(login, password));

        Message serverResponse = messenger.receive(0);
        System.out.println(login + ": " + serverResponse.toJSON());
        if(serverResponse.getValue(MessageFields.STATUS).equals("success")) {
            startMessaging();
        } else {
            System.out.println(login + ": Auth error");
        }
    }

    private Message makeAuth(String login, String password) {
        Message message = new Message(Type.REQUEST, Target.AUTHORIZATION);
        try {
            message.addExtraField(MessageFields.LOGIN, login);
            message.addExtraField(MessageFields.PASSWORD, password);
            message.addExtraField(MessageFields.ROLE, role.getName());
        } catch (MessageFormatException e) {
            e.printStackTrace();
        }
        return message;
    }

    private Message makeLocationMessage() throws JsonProcessingException, MessageFormatException {
        Message message = new Message(Type.RESPONSE, Target.LOCATION);
        message.addExtraField(MessageFields.LOCATION, new ObjectMapper().writeValueAsString(location));
        return message;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    private void startMessaging() {
        while (true) {
            try {
                Message message = messenger.receive(TIMEOUT);

                System.out.println("CLIENT| " + this.login + ": " + message.toJSON());

                if(message.getValue(MessageFields.TARGET).equals(Target.LOCATION.getName())) {
                    messenger.send(makeLocationMessage());
                } else if (message.getValue(MessageFields.TARGET).equals(Target.ENCOUNTER.getName())) {
                    if(message.getValue(MessageFields.MESSAGE) != null
                            && message.getValue(MessageFields.MESSAGE).equals("start_encounter")){
                        System.out.println("CLIENT| " + this.login + ": ENCOUNTER! - ");

                    } else if(message.getValue(MessageFields.MESSAGE) != null
                            && message.getValue(MessageFields.MESSAGE).equals("start_catching")){
                        System.out.println("CLIENT| " + this.login + ": CATCHING! - ");

                    } else if(message.getValue(MessageFields.WINNER) != null
                            && message.getValue(MessageFields.WINNER).equals("start_catching")){
                        System.out.println("CLIENT| " + this.login + ": WINNER! - ");
                    }
                }

            } catch (Exception e) {
//                e.printStackTrace();
                System.out.println("CLIENT| " + this.login + " - Read timeout");
            }

        }
    }

//    private Message makeHunterWinMessage() {
//        Message message = new Message(Type.EVENT, Target.ENCOUNTER);
//        try {
//            message.addExtraField(MessageFields.WINNER, Role.HUNTER.getName());
//        } catch (MessageFormatException e) {
//            e.printStackTrace();
//        }
//        return message;
//    }
//
//    private void enterEncounter() {
//        // TODO: sound check
//        // 0. начать таймер енкаунтера (15 мин)
//        // 1. в цикле опрашиваем модуль сорокина
//        //    - если захват - таймер++
//        //    - иначе: таймер = 0
//        // 2. если таймер >= критическое время захвата:
//        try {
//            messenger.send(makeHunterWinMessage());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        //      - отправка серверу сообщения о битве {
//        //          [winner: hunter]
//        //      }
//        // 3. если таймер енкантера > 15 min:
//        //      - отправка серверу сообщения о битве {
//        //           если данный клиент жертва: [winner: runner]
//        //           если данный клиент охотник: [winner: hunter]
//        //      }
//        //    иначе продолжить
//        //
////        new Thread(this::startMessaging).start();
//        return;
//
//    }
}
