package edu.leti.amf;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Простой класс для тестирования чтения и записи amf сообщений через файл.
 * <p/>
 * todo написать javadoc
 *
 * @author Tedikova O.
 * @version 1.0
 */
public class AMFMessageTranslator {

    /**
     * @param message
     * @param file
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void writeMessage(ActionMessage message, File file) throws IOException, ClassNotFoundException {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            AmfMessageSerializer serializer = new AmfMessageSerializer();
            serializer.initialize(SerializationContext.getSerializationContext(), fileOutputStream, new AmfTrace());
            serializer.writeMessage(message);
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }

    /**
     * @param message
     * @param file
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void readMessage(ActionMessage message, File file) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            AmfMessageDeserializer deserializer = new AmfMessageDeserializer();
            ActionContext context = new ActionContext();
            deserializer.initialize(SerializationContext.getSerializationContext(), fileInputStream, new AmfTrace());
            deserializer.readMessage(message, context);
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }
    }


}
