package test.java;


import com.fasterxml.jackson.core.JsonProcessingException;
import main.java.messaging.Message;
import main.java.messaging.Target;
import main.java.messaging.Type;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;

public class MessageTest {

    @Test
    public void testToJSON() throws JsonProcessingException{
        Message message = new Message(Type.REQUEST, Target.LOCATION);
        message.addField("field", "1");
        System.out.println(message.toJSON());
    }

    @Test
    public void testJSONConstructor() throws  IOException{
        Message message = new Message(Type.REQUEST, Target.LOCATION);
        message.addField("field", "1");
        System.out.println(new Message(message.toJSON()).toJSON());
    }
}
