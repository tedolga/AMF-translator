package edu.leti.amf;

import flex.messaging.io.ClassAliasRegistry;
import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.*;
import flex.messaging.messages.AcknowledgeMessageExt;
import flex.messaging.messages.AsyncMessageExt;
import flex.messaging.messages.CommandMessage;
import flex.messaging.messages.CommandMessageExt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Класс для сериализации и десериализации amf сообщений
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

        AmfMessageDeserializer deserializer = new AmfMessageDeserializer();
        ActionMessage message = new ActionMessage();
        ActionContext context = new ActionContext();
        AmfTrace amfTrace = new AmfTrace();
        SerializationContext serializationContext = SerializationContext.getSerializationContext();
        deserializer.initialize(serializationContext, inputStream, amfTrace);
        deserializer.readMessage(message, context);
        return message;
    }

    /**
     * Метод преобразует amf сообщение в читаемый вид
     *
     * @param inputStream поток бинарных данных
     * @return строка сообщения
     * @throws IOException            в случае ошибки чтения/записи
     * @throws ClassNotFoundException в случае ошибки работы с классами
     */
    public String getTrace(InputStream inputStream) throws IOException, ClassNotFoundException {
        AmfMessageDeserializer deserializer = new AmfMessageDeserializer();
        ActionMessage message = new ActionMessage();
        ActionContext context = new ActionContext();
        AmfTrace amfTrace = new AmfTrace();
        SerializationContext serializationContext = SerializationContext.getSerializationContext();
        deserializer.initialize(serializationContext, inputStream, amfTrace);
        deserializer.readMessage(message, context);
        return amfTrace.toString();
    }

    /**
     * Метод преобразует сообщение в поток бинарных данных
     *
     * @param message
     * @param outputStream
     * @return
     * @throws IOException
     */
    public OutputStream serializeMessage(ActionMessage message, OutputStream outputStream) throws IOException {
        AmfMessageSerializer serializer = new AmfMessageSerializer();
        AmfTrace amfTrace = new AmfTrace();
        SerializationContext serializationContext = SerializationContext.getSerializationContext();
        serializer.initialize(serializationContext, outputStream, amfTrace);
        serializer.writeMessage(message);
        return outputStream;
    }

    public ActionMessage makePingMessage() {
        ActionMessage actionMessage = new ActionMessage();
        CommandMessage commandMessage = new CommandMessage(5);
        actionMessage.getBody(1).setData(commandMessage);
        return actionMessage;
    }

}
