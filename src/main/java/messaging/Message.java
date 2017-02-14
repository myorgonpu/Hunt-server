package main.java.messaging;


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

    public void addField(String key, String value){
        fields.put(key, value);
    }

    public String getValue(String key){
        return fields.get(key);
    }

    public String toJSON(){
        Map<String, String> allFields = new HashMap<String, String>(fields);
        allFields.put("type" , type.getName());
        allFields.put("target" , target.getName());
        //ObjectMapper objectMapper;
        return "";
    }
}
