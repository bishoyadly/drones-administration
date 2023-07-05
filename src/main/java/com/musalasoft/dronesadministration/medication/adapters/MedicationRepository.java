package com.musalasoft.dronesadministration.medication.adapters;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRepository extends CrudRepository<MedicationRecord, String> {
}
