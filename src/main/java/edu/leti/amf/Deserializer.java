package edu.leti.amf;

import flex.messaging.io.amf.ActionMessageInput;
import flex.messaging.io.amf.AmfMessageDeserializer;

/**
 * @author Tedikova O.
 * @version 1.0
 */

public class Deserializer extends AmfMessageDeserializer {
    public ActionMessageInput getAmfInput() {
        return this.amfIn;
    }
}
