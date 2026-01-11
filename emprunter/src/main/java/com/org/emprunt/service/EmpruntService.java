package com.org.emprunt.service;

import com.org.emprunt.DTO.EmpruntDetailsDTO;
import com.org.emprunt.entities.Emprunter;
import com.org.emprunt.repositories.EmpruntRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpruntService {

    @Autowired
    private EmpruntRepository empruntRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    // Méthode pour créer emprunt avec userId et bookId (pour le contrôleur)
    public Emprunter createEmprunt(Long userId, Long bookId) {
        Emprunter emprunt = new Emprunter();
        emprunt.setUserId(userId);
        emprunt.setBookId(bookId);
        
        Emprunter savedEmprunt = empruntRepository.save(emprunt);
        
        // Publier événement Kafka
        kafkaProducerService.publishEmpruntCreatedEvent(
            savedEmprunt.getId(),
            savedEmprunt.getUserId(), 
            savedEmprunt.getBookId()
        );

        return savedEmprunt;
    }

    // Méthode alternative avec objet Emprunter
    public Emprunter createEmprunt(Emprunter emprunt) {
        Emprunter savedEmprunt = empruntRepository.save(emprunt);
        
        kafkaProducerService.publishEmpruntCreatedEvent(
            savedEmprunt.getId(),
            savedEmprunt.getUserId(), 
            savedEmprunt.getBookId()
        );

        return savedEmprunt;
    }

    public List<Emprunter> getAllEmprunts() {
        return empruntRepository.findAll();
    }

    // Méthode pour retourner EmpruntDetailsDTO (pour le contrôleur)
    public List<EmpruntDetailsDTO> getAllEmpruntDetails() {
        // Retourne une liste vide pour éviter l'erreur de compilation
        // Cette méthode peut être implémentée plus tard avec Feign clients
        return List.of();
    }

    public Optional<Emprunter> getEmpruntById(Long id) {
        return empruntRepository.findById(id);
    }

    public void deleteEmprunt(Long id) {
        empruntRepository.deleteById(id);
    }
}