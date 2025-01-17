package com.github.william853868671.logback.delivery;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.errors.TimeoutException;

/**
 * @author 王卫东
 */
public class AsynchronousDeliveryStrategy implements com.github.william853868671.logback.delivery.DeliveryStrategy {

    @Override
    public <K, V, E> boolean send(Producer<K, V> producer, ProducerRecord<K, V> record, final E event,
                                  final com.github.william853868671.logback.delivery.FailedDeliveryCallback<E> failedDeliveryCallback) {
        try {
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception != null) {
                        failedDeliveryCallback.onFailedDelivery(event, exception);
                    }
                }
            });
            return true;
        } catch (TimeoutException e) {
            failedDeliveryCallback.onFailedDelivery(event, e);
            return false;
        }
    }

}
