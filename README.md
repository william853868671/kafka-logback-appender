# kafka-logback-appender



## example 

### maven pom.xml
```xml
        <dependency>
            <groupId>io.github.william853868671</groupId>
            <artifactId>kafka-logback-appender</artifactId>
            <version>1.0.0</version>
        </dependency>

```
### logback.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <!-- 彩色日志(IDE下载插件才可以生效) -->
    <!-- 彩色日志依赖的渲染类 -->
    <conversionRule conversionWord="clr"
                    converterClass="org.springframework.boot.logging.logback.ColorConverter" />
    <conversionRule conversionWord="wex"
                    converterClass="org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter" />
    <conversionRule conversionWord="wEx"
                    converterClass="org.springframework.boot.logging.logback.ExtendedWhitespaceThrowableProxyConverter" />

    <springProperty scope="context" name="applicationName" source="spring.application.name"/>
    <springProperty scope="context" name="address" source="spring.kafka.bootstrap-servers"/>
    <springProperty scope="context" name="topic" source="spring.kafka.template.default-topic"/>


    <!--彩色日志输出格式-->
    <property name="CONSOLE_LOG_PATTERN" value="%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr(${PID}){magenta} --- %clr([%thread]){orange} %clr(%logger){cyan} %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}}"/>
    <!--非彩色日志输出格式-->
    <property name="PATTERN" value="%d{yyyy-MM-dd HH:mm:ss.SSS} ${LOG_LEVEL_PATTERN:-%5p} ${PID} [%thread] %-5level %logger{36} - %msg%n" />

    <!--编码-->
    <property name="ENCODING" value="UTF-8" />

    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <target>System.out</target>
        <encoder>
            <pattern>${CONSOLE_LOG_PATTERN}</pattern>
        </encoder>
    </appender>

    <appender name="file-log" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- 指定过滤策略 -->
        <file>logs/${applicationName}.log</file>
        <!-- 指定收集策略：滚动策略-->
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <!-- 指定生成日志保存地址 -->
            <fileNamePattern>logs/%d{yyyy-MM-dd}/${applicationName}.%d{yyyy-MM-dd}.%i.log.gz</fileNamePattern>
            <maxFileSize>200MB</maxFileSize>
            <maxHistory>60</maxHistory>
        </rollingPolicy>
        <layout class="ch.qos.logback.classic.PatternLayout">
            <Pattern>${CONSOLE_LOG_PATTERN}</Pattern>
        </layout>
    </appender>

    <!-- This example configuration is probably most unreliable under
    failure conditions but wont block your application at all -->
    <appender name="kafkaAppender" class="com.github.william853868671.logback.KafkaAppender">
        <encoder>
            <pattern>${CONSOLE_LOG_PATTERN}</pattern>
        </encoder>
        <topic>${topic}</topic>
        <!-- we don't care how the log messages will be partitioned  -->
        <keyingStrategy class="com.github.william853868671.logback.keying.NoKeyKeyingStrategy" />

        <!-- use async delivery. the application threads are not blocked by logging -->
        <deliveryStrategy class="com.github.william853868671.logback.delivery.AsynchronousDeliveryStrategy" />

        <!-- each <producerConfig> translates to regular kafka-client config (format: key=value) -->
        <!-- producer configs are documented here: https://kafka.apache.org/documentation.html#newproducerconfigs -->
        <!-- bootstrap.servers is the only mandatory producerConfig -->
        <producerConfig>bootstrap.servers=${address}</producerConfig>
        <!-- don't wait for a broker to ack the reception of a batch.  -->
        <producerConfig>acks=0</producerConfig>
        <!-- wait up to 1000ms and collect log messages before sending them as a batch -->
        <producerConfig>linger.ms=1000</producerConfig>
        <!-- even if the producer buffer runs full, do not block the application but start to drop messages -->
        <producerConfig>max.block.ms=0</producerConfig>
        <!-- define a client-id that you use to identify yourself against the kafka broker -->
        <producerConfig>client.id=${HOSTNAME}-${CONTEXT_NAME}-logback-log</producerConfig>

        <!-- there is no fallback <appender-ref>. If this appender cannot deliver, it will drop its messages. -->
        <appender-ref ref="STDOUT"/>
    </appender>

    <!--异步写入kafka，尽量不占用主程序的资源-->
    <appender name="ASYNC" class="ch.qos.logback.classic.AsyncAppender">
        <neverBlock>true</neverBlock>
        <includeCallerData>true</includeCallerData>
        <discardingThreshold>0</discardingThreshold>
        <queueSize>2048</queueSize>
        <appender-ref ref="kafkaAppender" />
    </appender>
    <!-- 开发环境和测试环境 -->
    <springProfile name="dev,test">
        <root level="INFO">
            <appender-ref ref="STDOUT"/>
            <appender-ref ref="ASYNC"/>
        </root>
    </springProfile>

    <!-- 生产环境 -->
    <springProfile name="prod">
        <root level="INFO">
            <appender-ref ref="file-log" />
            <appender-ref ref="ASYNC"/>
        </root>
    </springProfile>

</configuration>
```
### 引用
- [ ] https://github.com/danielwegener/logback-kafka-appender