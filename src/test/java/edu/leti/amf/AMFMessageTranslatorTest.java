package edu.leti.amf;

import flex.messaging.io.MessageIOConstants;
import flex.messaging.io.amf.ActionMessage;
import flex.messaging.io.amf.MessageBody;
import flex.messaging.io.amf.MessageHeader;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class AMFMessageTranslatorTest {
    private AMFMessageTranslator translator = new AMFMessageTranslator();
    private File file = new File("Messages.txt");

    @Test
    public void testWriteReadMessages() throws ClassNotFoundException, IOException {
        ActionMessage originalMessage = new ActionMessage(MessageIOConstants.AMF0);
        ActionMessage receivedMessage = new ActionMessage();
        MessageBody messageBody = new MessageBody("1/onResult", null, "Hello!");
        MessageHeader header = new MessageHeader("POST", false, null);
        originalMessage.addHeader(header);
        originalMessage.addBody(messageBody);
        translator.writeMessage(originalMessage, file);
        translator.readMessage(receivedMessage, file);
        Assert.assertEquals(originalMessage.getVersion(), receivedMessage.getVersion());
        Assert.assertEquals(originalMessage.getHeader(0).getName(), receivedMessage.getHeader(0).getName());
        Assert.assertEquals(originalMessage.getBody(0).getTargetURI(), receivedMessage.getBody(0).getTargetURI());
        Assert.assertEquals(originalMessage.getBody(0).getData(), receivedMessage.getBody(0).getData());
    }

}
