package edu.leti.amf;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.*;
import flex.messaging.messages.AbstractMessage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class MessageDecoder {
    private File binaryFile;

    public MessageDecoder(File binaryFile) {
        this.binaryFile = binaryFile;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        MessageDecoder decoder = new MessageDecoder(new File("SAYHELLO.binary"));
        String dsId = decoder.getDSId(decoder.printMessage());
        System.out.println("DSId = " + dsId);
    }

    public ActionMessage printMessage() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = null;
        ActionMessage message = new ActionMessage();
        try {
            fileInputStream = new FileInputStream(binaryFile);
            AmfMessageDeserializer deserializer = new AmfMessageDeserializer();
            ActionContext context = new ActionContext();
            AmfTrace amfTrace = new AmfTrace();
            deserializer.initialize(SerializationContext.getSerializationContext(), fileInputStream, amfTrace);
            deserializer.readMessage(message, context);
            System.out.println(amfTrace.toString());
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }
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
