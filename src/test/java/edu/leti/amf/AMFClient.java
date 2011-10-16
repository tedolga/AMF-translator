package edu.leti.amf;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Клиент, отправляющий amf сообщение
 *
 * @author Tedikova O.
 * @version 1.0
 */
public class AMFClient {
    private String hostURL;
    private DefaultHttpClient httpClient;
    private BasicHttpContext httpContext;
    private BasicCookieStore cookieStore;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        AMFClient client = new AMFClient("localhost:8400/registration");
        MessageDecoder messageDecoder = new MessageDecoder();
//        File file = new File("C:/projects/AMF-translator/src/test/resources/Ping.binary");
////        CommandMessage actionMessage=messageDecoder.makePingMessage();
////        HttpResponse response = client.sendMessage(file);
//        System.out.println(response.getStatusLine());
//        File responseFile = new File("Response.binary");
//        FileOutputStream fileOutputStream = null;
//        System.out.println(messageDecoder.getTrace(response.getEntity().getContent()));
//        try {
//            fileOutputStream = new FileOutputStream(responseFile);
//            response.getEntity().writeTo(fileOutputStream);
//        } finally {
//            if (fileOutputStream != null)
//                fileOutputStream.close();
//        }
    }

    public AMFClient(String host) {
        this.hostURL = host;
        httpClient = new DefaultHttpClient();
        cookieStore = new BasicCookieStore();
        httpContext = new BasicHttpContext();
        httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
    }

    public HttpResponse sendMessage(byte[] message) throws IOException {
        cookieStore.clear();
        HttpPost post = new HttpPost("http://" + hostURL + "/messagebroker/amf");
        ByteArrayInputStream stream = new ByteArrayInputStream(message);
        InputStreamEntity entity = new InputStreamEntity(stream, message.length);
        entity.setContentType("application/x-amf");
        post.setEntity(entity);
        return httpClient.execute(post, httpContext);
    }


}
