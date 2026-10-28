package com.banco.ms.cuentas.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitMqConfig {

    private static final String PATRON_EVENTOS_CLIENTE = "cliente.#";

    private final MensajeriaProperties properties;

    public RabbitMqConfig(MensajeriaProperties properties) {
        this.properties = properties;
    }

    @Bean
    public TopicExchange clientesExchange() {
        return ExchangeBuilder.topicExchange(properties.exchange()).durable(true).build();
    }

    @Bean
    public DirectExchange clientesDeadLetterExchange() {
        return ExchangeBuilder.directExchange(properties.exchangeErrores()).durable(true).build();
    }

    @Bean
    public Queue colaEventosCliente() {
        return QueueBuilder.durable(properties.cola())
                .deadLetterExchange(properties.exchangeErrores())
                .deadLetterRoutingKey(properties.colaErrores())
                .build();
    }

    @Bean
    public Queue colaErroresEventosCliente() {
        return QueueBuilder.durable(properties.colaErrores()).build();
    }

    @Bean
    public Binding bindingEventosCliente() {
        return BindingBuilder.bind(colaEventosCliente()).to(clientesExchange()).with(PATRON_EVENTOS_CLIENTE);
    }

    @Bean
    public Binding bindingErroresEventosCliente() {
        return BindingBuilder.bind(colaErroresEventosCliente())
                .to(clientesDeadLetterExchange())
                .with(properties.colaErrores());
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        JacksonJsonMessageConverter converter = new JacksonJsonMessageConverter();
        converter.setAlwaysConvertToInferredType(true);
        return converter;
    }
}
