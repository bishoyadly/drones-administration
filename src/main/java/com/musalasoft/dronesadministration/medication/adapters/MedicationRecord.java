package com.musalasoft.dronesadministration.medication.adapters;

import com.musalasoft.dronesadministration.drone.adapters.DroneRecord;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity(name = "medication")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class MedicationRecord {
    @Id
    private String code;
    private String name;
    private Integer weightInGram;
    private String imageUrl;

    @ManyToMany(mappedBy = "medicationRecordList")
    private List<DroneRecord> droneRecordList;

    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    private Date createdDate;

    @Column(name = "modified_date")
    @LastModifiedDate
    private Date modifiedDate;
}
