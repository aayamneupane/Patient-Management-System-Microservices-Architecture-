package com.pm.patientservice.repository;

import com.pm.patientservice.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
    // Returns true if a record with this email already exists
    // (e.g., to block duplicate sign-ups).
    boolean existsByEmail(String email);

    // Same check, but ignores the record with the given id
    // (e.g., so a patient can be updated while keeping their own email).
    boolean existsByEmailAndIdNot(String email, UUID id);
}
