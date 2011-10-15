package edu.leti.amf;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Tedikova O.
 * @version 1.0
 */

public class MessageDecoderUseCase {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ClassLoader loader=MessageDecoderUseCase.class.getClassLoader();
        InputStream inputStream=null;
        String message;
        MessageDecoder decoder = new MessageDecoder();
        try {
            inputStream = loader.getResourceAsStream("Registration.binary");
            message = decoder.getTrace(inputStream);
            decoder.getActionMessage(inputStream);
        } finally {
            if (inputStream != null)
                inputStream.close();
        }
        System.out.println(message);
    }
}
