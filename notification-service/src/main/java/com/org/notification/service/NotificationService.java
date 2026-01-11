package com.org.notification.service;

import com.org.notification.dto.EmpruntEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    
    @KafkaListener(topics = "emprunt-created", groupId = "notification-group")
    public void handleEmpruntCreated(EmpruntEvent event) {
        try {
            logger.info("🔔 NOTIFICATION REÇUE - Nouvel emprunt créé:");
            logger.info("   📋 Emprunt ID: {}", event.getEmpruntId());
            logger.info("   👤 User ID: {}", event.getUserId());
            logger.info("   📚 Book ID: {}", event.getBookId());
            logger.info("   📅 Timestamp: {}", event.getTimestamp());
            logger.info("   🎯 Event Type: {}", event.getEventType());
            
            // Simulation d'envoi de notification
            sendNotification(event);
            
            logger.info("✅ Notification traitée avec succès pour l'emprunt {}", event.getEmpruntId());
            
        } catch (Exception e) {
            logger.error("❌ Erreur lors du traitement de la notification pour l'emprunt {}: {}", 
                        event.getEmpruntId(), e.getMessage());
        }
    }
    
    private void sendNotification(EmpruntEvent event) {
        // Simulation d'envoi de notification (email, SMS, push, etc.)
        logger.info("📧 [SIMULATION] Envoi email à l'utilisateur {} pour l'emprunt du livre {}", 
                   event.getUserId(), event.getBookId());
        
        logger.info("📱 [SIMULATION] Envoi notification push: 'Votre emprunt a été confirmé!'");
        
        // Ici on pourrait ajouter la logique réelle d'envoi:
        // - Email service
        // - SMS service  
        // - Push notification service
        // - Slack/Discord webhook
        // etc.
    }
}