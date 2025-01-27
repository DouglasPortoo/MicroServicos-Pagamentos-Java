package br.com.alurafood.pagamentos.amqp;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
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
}
