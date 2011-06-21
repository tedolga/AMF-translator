package edu.leti.amf;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class AMFMessageTranslator {

    AmfMessageDeserializer deserializer = new AmfMessageDeserializer();
    AmfMessageSerializer serializer = new AmfMessageSerializer();
    ActionContext context = new ActionContext();

    public void writeMessage(ActionMessage message, File file) throws IOException, ClassNotFoundException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        serializer.initialize(SerializationContext.getSerializationContext(), fileOutputStream, new AmfTrace());
        serializer.writeMessage(message);
        fileOutputStream.close();

    }

    public void readMessage(ActionMessage message, File file) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file);
        deserializer.initialize(SerializationContext.getSerializationContext(), fileInputStream, new AmfTrace());
        deserializer.readMessage(message, context);
        fileInputStream.close();
    }


}
