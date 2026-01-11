package com.org.emprunt.service;

import com.org.emprunt.DTO.EmpruntEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class KafkaProducerService {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    
    private final KafkaTemplate<String, EmpruntEvent> kafkaTemplate;
    private static final String TOPIC_NAME = "emprunt-created";
    
    public KafkaProducerService(KafkaTemplate<String, EmpruntEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    public void publishEmpruntCreatedEvent(Long empruntId, Long userId, Long bookId) {
        try {
            // Créer l'événement
            EmpruntEvent event = new EmpruntEvent(
                empruntId,
                userId, 
                bookId,
                "EMPRUNT_CREATED",
                LocalDateTime.now()
            );
            
            // Publier dans Kafka
            kafkaTemplate.send(TOPIC_NAME, String.valueOf(empruntId), event);
            
            logger.info("📤 Événement Kafka publié avec succès:");
            logger.info("   📋 Topic: {}", TOPIC_NAME);
            logger.info("   🔑 Key: {}", empruntId);
            logger.info("   👤 User ID: {}", userId);
            logger.info("   📚 Book ID: {}", bookId);
            logger.info("   ⏰ Timestamp: {}", event.getTimestamp());
            
        } catch (Exception e) {
            logger.error("❌ Erreur lors de la publication de l'événement Kafka pour l'emprunt {}: {}", 
                        empruntId, e.getMessage());
        }
    }
}