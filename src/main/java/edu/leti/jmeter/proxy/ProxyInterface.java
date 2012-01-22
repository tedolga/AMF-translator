package edu.leti.jmeter.proxy;

/**
 * Прокси представляет собой поток вычитывающий поток данных из сокета.
 *
 * @author Tedikova O.
 * @version 1.0
 */
public interface ProxyInterface {

    /**
     * Вызывается приложением для начала записи трафика.
     */
    void start();

    /**
     * Вызывается приложением для завершения записи трафика.
     */
    void stop();

    /**
     * Возвращает порт подключения прокси сервера.
     */
    int getLocalPort();
}
