package com.github.william853868671.logback.keying;

/**
 * @author 王卫东
 */
public class NoKeyKeyingStrategy implements KeyingStrategy<Object> {

    @Override
    public byte[] createKey(Object e) {
        return null;
    }
}
