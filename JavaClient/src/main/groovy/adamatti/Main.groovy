package adamatti

import groovy.util.logging.Slf4j
import org.apache.camel.CamelContext
import org.apache.camel.Endpoint
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.apache.camel.ProducerTemplate
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.impl.DefaultCamelContext
import org.apache.camel.spring.SpringRouteBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericGroovyApplicationContext
import org.springframework.stereotype.Component

@Component
@Configuration
class Main extends SpringRouteBuilder implements Processor {
    private static final Logger log = LoggerFactory.getLogger(Main.class)
    private static final String ENDPOINT = "kafka:localhost:9092?topic=Topic1&groupId=JavaClient&consumerId=JavaClient"

    static void main(String [] args){
        log.info "Starting"
        def context = buildContext()
        log.info "Started"

        context.getBean(Main.class).sendMessage()

        System.in.read()
        log.info "Shutting down"
        context.close()
    }

    @Autowired
    private ProducerTemplate producerTemplate

    void configure(){
        from(ENDPOINT).bean("main")
    }

    void process(Exchange exchange) throws Exception {
        log.info "Msg received: ${exchange.in.body}"
    }

    void sendMessage(){
        producerTemplate.sendBody(ENDPOINT,"sample msg")
        log.info "Msg sent"
    }

    private static ApplicationContext buildContext(){
        def context = new GenericGroovyApplicationContext("classpath:spring.groovy")
        context.registerShutdownHook()
        context
    }
}