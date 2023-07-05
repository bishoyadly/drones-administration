package com.musalasoft.dronesadministration.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.musalasoft.dronesadministration.model.MedicationModelDto;
import java.util.ArrayList;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * DroneModelDto
 */

@JsonTypeName("DroneModel")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class DroneModelDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String serialNumber;

  private Integer loadWeightLimitInGram;

  private Integer batteryCapacityInPercentage;

  /**
   * Gets or Sets model
   */
  public enum ModelEnum {
    LIGHT_WEIGHT("LIGHT_WEIGHT"),
    
    MIDDLE_WEIGHT("MIDDLE_WEIGHT"),
    
    CRUISE_WEIGHT("CRUISE_WEIGHT"),
    
    HEAVY_WEIGHT("HEAVY_WEIGHT");

    private String value;

    ModelEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ModelEnum fromValue(String value) {
      for (ModelEnum b : ModelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ModelEnum model;

  /**
   * Gets or Sets state
   */
  public enum StateEnum {
    IDLE("IDLE"),
    
    LOADING("LOADING"),
    
    LOADED("LOADED"),
    
    DELIVERING("DELIVERING"),
    
    DELIVERED("DELIVERED"),
    
    RETURNING("RETURNING");

    private String value;

    StateEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StateEnum fromValue(String value) {
      for (StateEnum b : StateEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StateEnum state;

  @Valid
  private List<@Valid MedicationModelDto> medicationModelList;

  public DroneModelDto serialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
    return this;
  }

  /**
   * Get serialNumber
   * @return serialNumber
  */
  @Size(max = 100) 
  @Schema(name = "serialNumber", example = "ABCD12345", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("serialNumber")
  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public DroneModelDto loadWeightLimitInGram(Integer loadWeightLimitInGram) {
    this.loadWeightLimitInGram = loadWeightLimitInGram;
    return this;
  }

  /**
   * Get loadWeightLimitInGram
   * @return loadWeightLimitInGram
  */
  
  @Schema(name = "loadWeightLimitInGram", example = "300", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loadWeightLimitInGram")
  public Integer getLoadWeightLimitInGram() {
    return loadWeightLimitInGram;
  }

  public void setLoadWeightLimitInGram(Integer loadWeightLimitInGram) {
    this.loadWeightLimitInGram = loadWeightLimitInGram;
  }

  public DroneModelDto batteryCapacityInPercentage(Integer batteryCapacityInPercentage) {
    this.batteryCapacityInPercentage = batteryCapacityInPercentage;
    return this;
  }

  /**
   * Get batteryCapacityInPercentage
   * @return batteryCapacityInPercentage
  */
  
  @Schema(name = "batteryCapacityInPercentage", example = "100", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("batteryCapacityInPercentage")
  public Integer getBatteryCapacityInPercentage() {
    return batteryCapacityInPercentage;
  }

  public void setBatteryCapacityInPercentage(Integer batteryCapacityInPercentage) {
    this.batteryCapacityInPercentage = batteryCapacityInPercentage;
  }

  public DroneModelDto model(ModelEnum model) {
    this.model = model;
    return this;
  }

  /**
   * Get model
   * @return model
  */
  
  @Schema(name = "model", example = "HEAVY_WEIGHT", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("model")
  public ModelEnum getModel() {
    return model;
  }

  public void setModel(ModelEnum model) {
    this.model = model;
  }

  public DroneModelDto state(StateEnum state) {
    this.state = state;
    return this;
  }

  /**
   * Get state
   * @return state
  */
  
  @Schema(name = "state", example = "DELIVERED", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("state")
  public StateEnum getState() {
    return state;
  }

  public void setState(StateEnum state) {
    this.state = state;
  }

  public DroneModelDto medicationModelList(List<@Valid MedicationModelDto> medicationModelList) {
    this.medicationModelList = medicationModelList;
    return this;
  }

  public DroneModelDto addMedicationModelListItem(MedicationModelDto medicationModelListItem) {
    if (this.medicationModelList == null) {
      this.medicationModelList = new ArrayList<>();
    }
    this.medicationModelList.add(medicationModelListItem);
    return this;
  }

  /**
   * Get medicationModelList
   * @return medicationModelList
  */
  @Valid 
  @Schema(name = "MedicationModelList", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("MedicationModelList")
  public List<@Valid MedicationModelDto> getMedicationModelList() {
    return medicationModelList;
  }

  public void setMedicationModelList(List<@Valid MedicationModelDto> medicationModelList) {
    this.medicationModelList = medicationModelList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DroneModelDto droneModel = (DroneModelDto) o;
    return Objects.equals(this.serialNumber, droneModel.serialNumber) &&
        Objects.equals(this.loadWeightLimitInGram, droneModel.loadWeightLimitInGram) &&
        Objects.equals(this.batteryCapacityInPercentage, droneModel.batteryCapacityInPercentage) &&
        Objects.equals(this.model, droneModel.model) &&
        Objects.equals(this.state, droneModel.state) &&
        Objects.equals(this.medicationModelList, droneModel.medicationModelList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(serialNumber, loadWeightLimitInGram, batteryCapacityInPercentage, model, state, medicationModelList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DroneModelDto {\n");
    sb.append("    serialNumber: ").append(toIndentedString(serialNumber)).append("\n");
    sb.append("    loadWeightLimitInGram: ").append(toIndentedString(loadWeightLimitInGram)).append("\n");
    sb.append("    batteryCapacityInPercentage: ").append(toIndentedString(batteryCapacityInPercentage)).append("\n");
    sb.append("    model: ").append(toIndentedString(model)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    medicationModelList: ").append(toIndentedString(medicationModelList)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

