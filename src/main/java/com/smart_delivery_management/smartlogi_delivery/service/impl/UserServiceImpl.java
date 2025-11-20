package com.smart_delivery_management.smartlogi_delivery.service.impl;

import com.smart_delivery_management.smartlogi_delivery.dto.RegisterRequest;
import com.smart_delivery_management.smartlogi_delivery.entity.*;
import com.smart_delivery_management.smartlogi_delivery.entity.enums.Role;
import com.smart_delivery_management.smartlogi_delivery.repository.*;
import com.smart_delivery_management.smartlogi_delivery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private LivreurRepository livreurRepository;
    @Autowired private ClientExpediteurRepository clientRepository;
    @Autowired private DestinataireRepository destinataireRepository;
    @Autowired private ZoneRepository zoneRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public User register(RegisterRequest req) {

        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        String hashed = passwordEncoder.encode(req.getPassword());

        if (req.getRole() == Role.LIVREUR) {
            Livreur liv = new Livreur();
            liv.setNom(req.getNom());
            liv.setPrenom(req.getPrenom());
            liv.setEmail(req.getEmail());
            liv.setPassword(hashed);
            liv.setTelephone(req.getTelephone());
            liv.setAdresse(req.getAdresse());
            liv.setRole(Role.LIVREUR);

            liv.setVehicule(req.getVehicule());

            if (req.getZoneAssigneeId() != null) {
                Zone zone = zoneRepository.findById(req.getZoneAssigneeId())
                        .orElseThrow();
                liv.setZoneAssignee(zone);
            }

            return livreurRepository.save(liv);
        }

        if (req.getRole() == Role.CLIENT) {
            ClientExpediteur c = new ClientExpediteur();
            c.setNom(req.getNom());
            c.setPrenom(req.getPrenom());
            c.setEmail(req.getEmail());
            c.setPassword(hashed);
            c.setTelephone(req.getTelephone());
            c.setAdresse(req.getAdresse());
            c.setRole(Role.CLIENT);
            return clientRepository.save(c);
        }

        Destinataire d = new Destinataire();
        d.setNom(req.getNom());
        d.setPrenom(req.getPrenom());
        d.setEmail(req.getEmail());
        d.setPassword(hashed);
        d.setTelephone(req.getTelephone());
        d.setAdresse(req.getAdresse());
        d.setRole(Role.DESTINATAIRE);

        return destinataireRepository.save(d);
    }
}
