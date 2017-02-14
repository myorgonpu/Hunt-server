package main.java.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Message {
    private Type type;
    private Target target;
    private Map<String, String> fields;

    public Message(Type type, Target target, Map<String, String> fields) {
        this.type = type;
        this.target = target;
        this.fields = fields;
    }

    public Message(Type type, Target target){
        this.type = type;
        this.target = target;
        this.fields = new HashMap<String, String>();
    }

    public Message(String json) throws IOException {
        HashMap<String,String> result =
                new ObjectMapper().readValue(json, HashMap.class);
        for(Target target: Target.values()){
            if(result.get("target").equals(target.getName())){
                this.target = target;
                result.remove("target");
                break;
            }
        }
        for(Type type: Type.values()){
            if(result.get("type").equals(type.getName())){
                this.type = type;
                result.remove("type");
                break;
            }
        }
        this.fields = result;
    }

    public void addField(String key, String value){
        fields.put(key, value);
    }

    public String getValue(String key){
        return fields.get(key);
    }

    public String toJSON() throws JsonProcessingException {
        Map<String, String> allFields = new HashMap<String, String>(fields);
        allFields.put("type" , type.getName());
        allFields.put("target" , target.getName());
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(allFields);
    }
}
