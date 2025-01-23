package br.com.alurafood.pagamentos.amqp;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PagamentoAMQPConfig {

    // Cria a fila de pagamento concluído
    @Bean
    public Queue criaFila(){
        return new Queue("pagamento.concluido",false);
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
}
