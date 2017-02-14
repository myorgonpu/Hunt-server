package test.java;



import main.java.messaging.Message;
import main.java.messaging.MessageFormatException;
import main.java.messaging.Target;
import main.java.messaging.Type;

import java.io.IOException;

import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;





public class MessageTest {

    @Test
    public void testJSONConstructor() throws  IOException{
        String JSON = "{\"type\":\"request\", \"target\":\"location\", \"field\":\"1\"}";

        Message message = new Message(JSON);

        assertThat(message.getValue("type"), is("request"));
        assertThat(message.getValue("target"), is("location"));
        assertThat(message.getValue("field"), is("1"));
    }

    @Test
    public void testAddingRandomField() throws MessageFormatException{
        Message message = new Message(Type.REQUEST, Target.LOCATION);
        message.addExtraField("field", "1");

        assertThat(message.getValue("field"), is("1"));
    }

    @Test(expected = MessageFormatException.class)
    public void testAddingTypeField() throws MessageFormatException{
        Message message = new Message(Type.REQUEST, Target.LOCATION);
        message.addExtraField("type", "any type");
    }

    @Test(expected = MessageFormatException.class)
    public void testAddingTargetField() throws MessageFormatException{
        Message message = new Message(Type.REQUEST, Target.LOCATION);
        message.addExtraField("target", "any target");
    }

    @Test
    public void testGettingValue() throws MessageFormatException{
        Message message = new Message(Type.REQUEST, Target.LOCATION);
        message.addExtraField("field", "1");

        assertThat(message.getValue("type"), is("request"));
        assertThat(message.getValue("target"), is("location"));
        assertThat(message.getValue("field"), is("1"));
    }


    @Test
    public void testToJSON() throws IOException, JSONException, MessageFormatException{
        Message message = new Message(Type.REQUEST, Target.LOCATION);
        message.addExtraField("field", "1");

        String actualJSON = message.toJSON();
        String expectedJSON = "{\"type\":\"request\", \"target\":\"location\", \"field\":\"1\"}";
        boolean strict = false;

        JSONAssert.assertEquals(expectedJSON, actualJSON, strict);
    }
}
