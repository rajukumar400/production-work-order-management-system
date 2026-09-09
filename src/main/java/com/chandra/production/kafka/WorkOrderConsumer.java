package com.chandra.production.kafka;



import com.chandra.production.event.WorkOrderCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class WorkOrderConsumer {

    @KafkaListener(
            topics = "work-order-events",
            groupId = "production-group"
    )
    public void consumeWorkOrderCreatedEvent(
            WorkOrderCreatedEvent event) {

        System.out.println(
                "Work Order event received from Kafka: "
                        + event.getWorkOrderId()
        );

        System.out.println(
                "Product ID: " + event.getProductId()
        );

        System.out.println(
                "Quantity: " + event.getQuantity()
        );

        System.out.println(
                "Status: " + event.getStatus()
        );
    }
}