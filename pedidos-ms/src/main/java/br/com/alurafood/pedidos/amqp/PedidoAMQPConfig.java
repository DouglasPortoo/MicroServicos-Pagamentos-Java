package br.com.alurafood.pedidos.amqp;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PedidoAMQPConfig {
    // Configura o conversor de mensagens
    @Bean
    public Jackson2JsonMessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    // Configura o template do RabbitMQ
    @Bean
    public RabbitTemplate rabbitTemplate (ConnectionFactory conn, Jackson2JsonMessageConverter converter){
        RabbitTemplate template = new RabbitTemplate(conn);
        template.setMessageConverter(converter);
        return template;

    }

    // Cria a fila de pagamento concluído
    @Bean
    public Queue filaDetalhesPedido(){
        return QueueBuilder.durable("pagamentos.detalhes-pedido").build();
    }

    // Cria a exchange de pagamento
    @Bean
    public FanoutExchange fanoutExchange(){
        return ExchangeBuilder.fanoutExchange("pagamentos.ex").build();
    }

    // Cria o binding entre a fila e a exchange
    @Bean
    public Binding bindingPagamentoPedido(FanoutExchange fanoutExchange){
        return BindingBuilder.bind(filaDetalhesPedido()).to(fanoutExchange);
    }

    // Cria o RabbitAdmin
    @Bean
    public RabbitAdmin CriaRabbitAdmin(ConnectionFactory conn){
        return new RabbitAdmin(conn);
    }

    // Inicializa o RabbitAdmin
    @Bean
    public ApplicationListener<ApplicationReadyEvent> inicializaAdmin(RabbitAdmin admin){
        return event -> admin.initialize();
    }
}
