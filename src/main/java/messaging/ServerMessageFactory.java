package main.java.messaging;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.Location;
import main.java.Role;

import java.util.List;

public class ServerMessageFactory {

    public static Message createRegistrationResponse() throws MessageFormatException {
        Message message = new Message(Type.RESPONSE, Target.REGISTRATION);
        message.addExtraField(MessageFields.STATUS, "success");
        return message;
    }

    public static Message createRegistrationResponse(String errorMessage) throws MessageFormatException {
        Message message = new Message(Type.RESPONSE, Target.REGISTRATION);
        message.addExtraField(MessageFields.STATUS, "error");
        message.addExtraField(MessageFields.ERROR, errorMessage);
        return message;
    }

    public static Message createAuthorizationResponse() throws MessageFormatException {
        Message message = new Message(Type.RESPONSE, Target.AUTHORIZATION);
        message.addExtraField(MessageFields.STATUS, "success");
        return message;
    }

    public static Message createAuthorizationResponse(String errorMessage) throws MessageFormatException {
        Message message = new Message(Type.RESPONSE, Target.AUTHORIZATION);
        message.addExtraField(MessageFields.STATUS, "error");
        message.addExtraField(MessageFields.ERROR, errorMessage);
        return message;
    }

    public static Message createTrackingRequest(){
        return new Message(Type.REQUEST, Target.LOCATION);
    }

    public static Message createTrackingListResponse(List<Location> locations)
            throws JsonProcessingException, MessageFormatException {
        Message message =  new Message(Type.RESPONSE, Target.LOCATION);
        String locationsJSON = new ObjectMapper().writeValueAsString(locations);
        message.addExtraField(MessageFields.LOCATION, locationsJSON);
        return message;
    }

    public static Message createEncounterStartEvent() throws MessageFormatException{
        Message message = new Message(Type.EVENT, Target.ENCOUNTER);
        message.addExtraField(MessageFields.MESSAGE, "start_encounter");
        return message;
    }

    public static Message createCatchingStartEvent() throws MessageFormatException{ //поменять
        Message message = new Message(Type.EVENT, Target.ENCOUNTER);
        message.addExtraField(MessageFields.MESSAGE, "start_catching");
        return message;
    }

    public static Message createEncounterEndEvent(Role role) throws MessageFormatException{
        Message message = new Message(Type.EVENT, Target.ENCOUNTER);
        message.addExtraField(MessageFields.WINNER, role.getName());
        return message;
    }
}
