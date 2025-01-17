package com.github.william853868671.logback;

import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.spi.AppenderAttachable;
import com.github.william853868671.logback.delivery.AsynchronousDeliveryStrategy;
import com.github.william853868671.logback.delivery.DeliveryStrategy;
import com.github.william853868671.logback.keying.KeyingStrategy;
import com.github.william853868671.logback.keying.NoKeyKeyingStrategy;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;

/**
 * @author 王卫东
 * @param <E> e
 */
public abstract class KafkaAppenderConfig<E> extends UnsynchronizedAppenderBase<E> implements AppenderAttachable<E> {

    /**
     *
     */
    protected String topic = null;

    /**
     *
     */
    protected Encoder<E> encoder = null;
    /**
     *
     */
    protected KeyingStrategy<? super E> keyingStrategy = null;
    /**
     *
     */
    protected DeliveryStrategy deliveryStrategy;

    /**
     *
     */
    protected Integer partition = null;
    /**
     *
     */
    protected boolean appendTimestamp = true;

    /**
     *
     */
    protected Map<String,Object> producerConfig = new HashMap<String, Object>();

    /**
     *
     * @return boolean
     */
    protected boolean checkPrerequisites() {
        boolean errorFree = true;

        if (producerConfig.get(BOOTSTRAP_SERVERS_CONFIG) == null) {
            addError("No \"" + BOOTSTRAP_SERVERS_CONFIG + "\" set for the appender named [\""
                    + name + "\"].");
            errorFree = false;
        }

        if (topic == null) {
            addError("No topic set for the appender named [\"" + name + "\"].");
            errorFree = false;
        }

        if (encoder == null) {
            addError("No encoder set for the appender named [\"" + name + "\"].");
            errorFree = false;
        }

        if (keyingStrategy == null) {
            addInfo("No explicit keyingStrategy set for the appender named [\"" + name + "\"]. Using default NoKeyKeyingStrategy.");
            keyingStrategy = new NoKeyKeyingStrategy();
        }

        if (deliveryStrategy == null) {
            addInfo("No explicit deliveryStrategy set for the appender named [\""+name+"\"]. Using default asynchronous strategy.");
            deliveryStrategy = new AsynchronousDeliveryStrategy();
        }

        return errorFree;
    }

    /**
     *
     * @param encoder encoder
     */
    public void setEncoder(Encoder<E> encoder) {
        this.encoder = encoder;
    }

    /**
     *
     * @param topic topic
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     *
     * @param keyingStrategy keyingStrategy
     */
    public void setKeyingStrategy(KeyingStrategy<? super E> keyingStrategy) {
        this.keyingStrategy = keyingStrategy;
    }

    /**
     *
     * @param keyValue keyValue
     */
    public void addProducerConfig(String keyValue) {
        String[] split = keyValue.split("=", 2);
        if(split.length == 2) {
            addProducerConfigValue(split[0], split[1]);
        }
    }

    /**
     *
     * @param key key
     * @param value v
     */
    public void addProducerConfigValue(String key, Object value) {
        this.producerConfig.put(key,value);
    }

    /**
     *
     * @return map
     */
    public Map<String, Object> getProducerConfig() {
        return producerConfig;
    }

    /**
     *
     * @param deliveryStrategy deliveryStrategy
     */
    public void setDeliveryStrategy(DeliveryStrategy deliveryStrategy) {
        this.deliveryStrategy = deliveryStrategy;
    }

    /**
     *
     * @param partition partition
     */
    public void setPartition(Integer partition) {
        this.partition = partition;
    }

    /**
     *
     * @return bool
     */
    public boolean isAppendTimestamp() {
        return appendTimestamp;
    }

    /**
     *
     * @param appendTimestamp appendTimestamp
     */
    public void setAppendTimestamp(boolean appendTimestamp) {
        this.appendTimestamp = appendTimestamp;
    }

}
