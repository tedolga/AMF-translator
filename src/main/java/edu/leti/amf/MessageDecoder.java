package edu.leti.amf;

import flex.messaging.io.ClassAliasRegistry;
import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.*;
import flex.messaging.messages.AcknowledgeMessageExt;
import flex.messaging.messages.AsyncMessageExt;
import flex.messaging.messages.CommandMessageExt;

import java.io.IOException;
import java.io.InputStream;

/**
 * Класс для работы с бинарными amf сообщениями
 *
 * @author Tedikova O.
 * @version 1.0
 */
public class MessageDecoder {


    public MessageDecoder() {
        ClassAliasRegistry registry = ClassAliasRegistry.getRegistry();
        registry.registerAlias(AsyncMessageExt.CLASS_ALIAS, AsyncMessageExt.class.getName());
        registry.registerAlias(AcknowledgeMessageExt.CLASS_ALIAS, AcknowledgeMessageExt.class.getName());
        registry.registerAlias(CommandMessageExt.CLASS_ALIAS, CommandMessageExt.class.getName());
    }

    /**
     * Метод считывает amf сообщение  из входного потока и преобразует его в экземпляр
     * Action Message
     *
     * @param inputStream входной поток , содержащий amf сообщение
     * @return сообщение, преобразованное в ActionMessage
     * @throws IOException            в случае ошибки чтения/записи
     * @throws ClassNotFoundException в случае ошибки работы с классами
     */
    public ActionMessage getActionMessage(InputStream inputStream) throws IOException, ClassNotFoundException {

        Deserializer deserializer = new Deserializer();
        ActionMessage message = new ActionMessage();
        ActionContext context = new ActionContext();
        AmfTrace amfTrace = new AmfTrace();
        SerializationContext serializationContext = SerializationContext.getSerializationContext();
        deserializer.initialize(serializationContext, inputStream, amfTrace);
        deserializer.readMessage(message, context);
        System.out.println(amfTrace.toString());
        return message;
    }

    public String getTrace(InputStream inputStream) throws IOException, ClassNotFoundException {
        Deserializer deserializer = new Deserializer();
        ActionMessage message = new ActionMessage();
        ActionContext context = new ActionContext();
        AmfTrace amfTrace = new AmfTrace();
        SerializationContext serializationContext = SerializationContext.getSerializationContext();
        deserializer.initialize(serializationContext, inputStream, amfTrace);
        deserializer.readMessage(message, context);
        return amfTrace.toString();
    }

}
