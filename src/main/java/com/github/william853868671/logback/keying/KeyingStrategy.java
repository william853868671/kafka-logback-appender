package com.github.william853868671.logback.keying;

/**
 * @author 王卫东
 * @param <E> e
 */
public interface KeyingStrategy<E> {

    /**
     * creates a byte array key for the given {@link ch.qos.logback.classic.spi.ILoggingEvent}
     * @param e the logging event
     * @return a key
     */
    byte[] createKey(E e);

}
