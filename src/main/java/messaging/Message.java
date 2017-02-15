package main.java.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Message {
    private Type type;
    private Target target;
    private Map<String, String> fields;

    public Message(Type type, Target target, Map<String, String> fields) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(target);
        Objects.requireNonNull(fields);
        this.type = type;
        this.target = target;
        this.fields = fields;
    }

    public Message(Type type, Target target){
        Objects.requireNonNull(type);
        Objects.requireNonNull(target);
        this.type = type;
        this.target = target;
        this.fields = new HashMap<String, String>();
    }

    public Message(String json) throws IOException {
        Objects.requireNonNull(json);
        HashMap<String,String> result =
                new ObjectMapper().readValue(json, HashMap.class);
        for(Target target: Target.values()){
            if(result.get(MessageFields.TARGET).equals(target.getName())){
                this.target = target;
                result.remove(MessageFields.TARGET);
                break;
            }
        }
        for(Type type: Type.values()){
            if(result.get(MessageFields.TYPE).equals(type.getName())){
                this.type = type;
                result.remove(MessageFields.TYPE);
                break;
            }
        }
        this.fields = result;
    }


    public void addExtraField(String key, String value) throws MessageFormatException{
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        if(MessageFields.TARGET.equals(key)){
            throw new MessageFormatException("Target isn't allowed to add.");
        }
        if(MessageFields.TYPE.equals(key)){
            throw new MessageFormatException("Type isn't allowed to add.");
        }
        fields.put(key, value);
    }

    public String getValue(String key){
        Objects.requireNonNull(key);
        if(MessageFields.TARGET.equals(key)){
            return target.getName();
        }
        if(MessageFields.TYPE.equals(key)){
            return type.getName();
        }
        return fields.get(key);
    }

    public String toJSON() throws JsonProcessingException {
        Map<String, String> allFields = new HashMap<>(fields);
        allFields.put(MessageFields.TYPE , type.getName());
        allFields.put(MessageFields.TARGET , target.getName());
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(allFields);
    }
}
