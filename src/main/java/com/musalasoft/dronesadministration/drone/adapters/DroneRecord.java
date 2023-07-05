package com.musalasoft.dronesadministration.drone.adapters;

import com.musalasoft.dronesadministration.medication.adapters.MedicationRecord;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity(name = "drone")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class DroneRecord {
    @Id
    private String serialNumber;
    private Integer loadWeightLimitInGram;
    private Integer batteryCapacityInPercentage;
    private String model;
    private String state;

    @ManyToMany
    @JoinTable(name = "drone_medication", joinColumns = @JoinColumn(name = "drone_serial_number"),
            inverseJoinColumns = @JoinColumn(name = "medication_code"))
    private List<MedicationRecord> medicationRecordList;

    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    private Date createdDate;

    @Column(name = "modified_date")
    @LastModifiedDate
    private Date modifiedDate;
}
