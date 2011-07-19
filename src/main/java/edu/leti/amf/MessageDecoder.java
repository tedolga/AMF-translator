package edu.leti.amf;

import com.sun.xml.internal.ws.wsdl.writer.document.Message;
import flex.messaging.io.ClassAliasRegistry;
import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.*;
import flex.messaging.messages.AbstractMessage;
import flex.messaging.messages.AcknowledgeMessageExt;
import flex.messaging.messages.AsyncMessageExt;
import flex.messaging.messages.CommandMessageExt;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
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

    public ActionMessage printMessage(InputStream inputStream) throws IOException, ClassNotFoundException {
        ClassAliasRegistry registry = ClassAliasRegistry.getRegistry();
        registry.registerAlias(AsyncMessageExt.CLASS_ALIAS, AsyncMessageExt.class.getName());
        registry.registerAlias(AcknowledgeMessageExt.CLASS_ALIAS, AcknowledgeMessageExt.class.getName());
        registry.registerAlias(CommandMessageExt.CLASS_ALIAS, CommandMessageExt.class.getName());
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

    public String getDSId(ActionMessage message) throws ClassNotFoundException {
        String dsID = null;
        for (int i = 0; i < message.getBodyCount(); i++) {
            MessageBody body = message.getBody(i);
            System.out.println(message.getVersion());
            System.out.println(message.getHeaderCount());
            String bodyClassName = body.getData().getClass().getCanonicalName();
            System.out.println(bodyClassName);
//            Object bodyData=(Object) body.getData();
//                if (bodyData instanceof AbstractMessage) {
//                    AbstractMessage abstractMessage = (AbstractMessage) bodyData;
//                    Map headers = abstractMessage.getHeaders();
//                    if (headers.containsKey("DSId")) {
//                        dsID = (String) headers.get("DSId");
//                    }
//                }
            Object[] bodyData = (Object[]) body.getData();
            if (bodyData[0] instanceof AbstractMessage) {
                AbstractMessage abstractMessage = (AbstractMessage) bodyData[0];
                Object[] abstrMsgBody = (Object[]) abstractMessage.getBody();
                System.out.println(abstrMsgBody[4].getClass().getCanonicalName());
                Map headers = abstractMessage.getHeaders();
                if (headers.containsKey("DSId")) {
                    dsID = (String) headers.get("DSId");
                }
            }
        }
        return dsID;
    }

}
