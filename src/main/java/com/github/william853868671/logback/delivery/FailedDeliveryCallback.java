package com.github.william853868671.logback.delivery;

/**
 * @author 王卫东
 * @param <E> e
 */
public interface FailedDeliveryCallback<E> {
    /**
     * 投递失败的回调方法
     * @param evt evt
     * @param throwable throwable
     */
    void onFailedDelivery(E evt, Throwable throwable);
}
