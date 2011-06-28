package edu.leti.amf;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.*;
import flex.messaging.messages.AbstractMessage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class MessageDecoder {
    private File binaryFile;


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        File file = new File("Registration.binary");
        String dsId;
        MessageDecoder decoder = new MessageDecoder();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            dsId = decoder.getDSId(decoder.printMessage(fileInputStream));
        } finally {
            if (fileInputStream != null)
                fileInputStream.close();
        }
        System.out.println("DSId = " + dsId);
    }

    public ActionMessage printMessage(InputStream inputStream) throws IOException, ClassNotFoundException {
        ActionMessage message = new ActionMessage();
        AmfMessageDeserializer deserializer = new AmfMessageDeserializer();
        ActionContext context = new ActionContext();
        AmfTrace amfTrace = new AmfTrace();
        SerializationContext serializationContext = SerializationContext.getSerializationContext();
        deserializer.initialize(serializationContext, inputStream, amfTrace);
        deserializer.readMessage(message, context);
        System.out.println(amfTrace.toString());
        return message;
    }

    public String getDSId(ActionMessage message) {
        String dsID = null;
        for (int i = 0; i < message.getBodyCount(); i++) {
            MessageBody body = message.getBody(i);
            Object[] bodyData = (Object[]) body.getData();
            for (int j = 0; j < bodyData.length; j++) {
                if (bodyData[j] instanceof AbstractMessage) {
                    AbstractMessage abstractMessage = (AbstractMessage) bodyData[0];
                    Map headers = abstractMessage.getHeaders();
                    if (headers.containsKey("DSId")) {
                        dsID = (String) headers.get("DSId");
                    }
                }
            }
        }
        return dsID;
    }

}
