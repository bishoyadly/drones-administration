package com.musalasoft.dronesadministration.schedulers;

import com.musalasoft.dronesadministration.drone.adapters.DroneComponent;
import com.musalasoft.dronesadministration.drone.adapters.DroneComponentException;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingRequest;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingResponse;
import com.musalasoft.dronesadministration.drone.usecases.medicationloading.MedicationLoadingResponsePage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DroneSchedulerService {

    private final TaskScheduler threadPoolTaskScheduler;
    private final DroneComponent droneComponent;
    private final int droneSchedulerTaskDelay;

    @Autowired
    public DroneSchedulerService(TaskScheduler threadPoolTaskScheduler, DroneComponent droneComponent,
                                 @Value("${droneSchedulerTaskDelayInMS:10000}") int droneSchedulerTaskDelay) {
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.droneComponent = droneComponent;
        this.droneSchedulerTaskDelay = droneSchedulerTaskDelay;
        scheduleDronesTask();
    }

    public void scheduleDronesTask() {
        threadPoolTaskScheduler.scheduleWithFixedDelay(this::checkDronesBatteryLevels, droneSchedulerTaskDelay);
    }

    public void checkDronesBatteryLevels() {
        try {
            logDronesBatteryLevel();
        } catch (DroneComponentException exception) {
            log.error("DroneComponentException : {}", exception.getProblemDto());
        }
    }

    private void logDronesBatteryLevel() {
        MedicationLoadingRequest request = new MedicationLoadingRequest();
        request.setDronesPageNumber(0);
        request.setDronesPageSize(10);
        MedicationLoadingResponsePage responsePage = droneComponent.getDronesPage(request);
        for (MedicationLoadingResponse response : responsePage.getMedicationLoadingResponseList()) {
            log.info("[{}] Drone With Serial Number: {} Has Battery Capacity Level: {}",
                    Thread.currentThread().getThreadGroup().getName(),
                    response.getDroneSerialNumber(), response.getDroneBatteryCapacityInPercentage());
        }
    }
}
