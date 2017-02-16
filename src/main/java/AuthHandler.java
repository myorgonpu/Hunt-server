package main.java;


import main.java.database.AlreadyExistingException;
import main.java.database.UserRepository;
import main.java.database.UserRepositoryRDB;
import main.java.messaging.*;

import java.io.IOException;
import java.net.Socket;

public class AuthHandler {

    // FIXIT: find optimal timeout
    public static final int TIMEOUT_MILLIS = 2000;

    private Socket client;
    private UserRepository userRepository;

    public AuthHandler(Socket client) {
        this.client = client;
        this.userRepository = UserRepositoryRDB.getInstance();
    }

    public AuthHandler(Socket client, UserRepository userRepository) {
        this.client = client;
        this.userRepository = userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void processUserInfo() throws MessageFormatException{

        try {
            Messenger messenger = new Messenger(client);
            Message message = messenger.receive(TIMEOUT_MILLIS);


            if(message.getValue(MessageFields.TYPE).equals(Type.REQUEST.getName())){
                if(message.getValue(MessageFields.TARGET)
                        .equals(Target.AUTHORIZATION.getName())){
                    messenger.send(this.authorize(message, messenger));
                }else if(message.getValue(MessageFields.TARGET)
                        .equals(Target.REGISTRATION.getName())){
                    messenger.send(this.register(message));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Message register(Message message) throws MessageFormatException {
        Message response = new Message(Type.RESPONSE, Target.REGISTRATION);

        String login = message.getValue(MessageFields.LOGIN);
        String password = message.getValue(MessageFields.PASSWORD);

        if(login == null || login.isEmpty()){
            throw new MessageFormatException("Login can not be null");
        }
        if(password == null || password.isEmpty()){
            throw new MessageFormatException("Password can not be null");
        }

        User user = new User(login, password, 0);
        try {
            userRepository.create(user);
        } catch (AlreadyExistingException e) {
            response.addExtraField(MessageFields.STATUS, "error");
            response.addExtraField(MessageFields.ERROR, "User with this login already exists");
            return response;
        }

        message.addExtraField(MessageFields.STATUS, "success");
        return response;
    }

    private Message authorize(Message message, Messenger messenger) throws MessageFormatException {
        Message response = new Message(Type.RESPONSE, Target.AUTHORIZATION);

        String login = message.getValue(MessageFields.LOGIN);
        String password = message.getValue(MessageFields.PASSWORD);

        if(login == null || login.isEmpty()){
            throw new MessageFormatException("Login can not be null");
        }
        if(password == null || password.isEmpty()){
            throw new MessageFormatException("Password can not be null");
        }

        User user = userRepository.get(login, password);
        if(user == null){
            response.addExtraField(MessageFields.STATUS, "error");
            response.addExtraField(MessageFields.ERROR, "Wrong authorization info: user is not exists");
            return response;
        }

        if(message.getValue(MessageFields.ROLE).equalsIgnoreCase(Role.HUNTER.getName())){
            user.setRole(Role.HUNTER);
        }else if(message.getValue(MessageFields.ROLE).equalsIgnoreCase(Role.RUNNER.getName())){
            user.setRole(Role.RUNNER);
        }
        else{
            response.addExtraField(MessageFields.STATUS, "error");
            response.addExtraField(MessageFields.ERROR, "Wrong authorization info: role");
            return response;
        }
        user.setMessenger(messenger);
        ActiveUsers.getUsers().add(user);

        message.addExtraField(MessageFields.STATUS, "success");
        return response;
    }

}

