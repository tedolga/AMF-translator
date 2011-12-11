package edu.leti.jmeter.proxy;

import java.io.IOException;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public interface ProxyInterface {

    void start() throws IOException;

    void stop();
}
