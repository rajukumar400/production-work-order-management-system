package com.chandra.production.kafka;


import com.chandra.production.event.WorkOrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkOrderProducer {

    private static final String TOPIC = "work-order-events";

    private final KafkaTemplate<String, WorkOrderCreatedEvent> kafkaTemplate;

    public void sendWorkOrderCreatedEvent(WorkOrderCreatedEvent event) {

        kafkaTemplate.send(
                TOPIC,
                event.getWorkOrderId().toString(),
                event
        );

        System.out.println(
                "Work Order event published to Kafka: "
                        + event.getWorkOrderId()
        );
    }
}