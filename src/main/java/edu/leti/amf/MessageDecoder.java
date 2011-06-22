package edu.leti.amf;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.ActionContext;
import flex.messaging.io.amf.ActionMessage;
import flex.messaging.io.amf.AmfMessageDeserializer;
import flex.messaging.io.amf.AmfTrace;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
        decoder.decodeBody();
    }

    public ActionMessage decodeBody() throws IOException, ClassNotFoundException {
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

}
