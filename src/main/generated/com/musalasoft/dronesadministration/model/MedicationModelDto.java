package com.musalasoft.dronesadministration.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.Generated;
import java.io.Serializable;
import java.util.Objects;

/**
 * MedicationModelDto
 */

@JsonTypeName("MedicationModel")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class MedicationModelDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;

    private String name;

    private Integer weightInGram;

    private String imageUrl;

    public MedicationModelDto code(String code) {
        this.code = code;
        return this;
    }

    /**
     * Get code
     *
     * @return code
     */

    @Schema(name = "code", example = "ABC_123", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public MedicationModelDto name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get name
     *
     * @return name
     */

    @Schema(name = "name", example = "Medicine-123_1mg", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MedicationModelDto weightInGram(Integer weightInGram) {
        this.weightInGram = weightInGram;
        return this;
    }

    /**
     * Get weightInGram
     *
     * @return weightInGram
     */

    @Schema(name = "weightInGram", example = "10", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("weightInGram")
    public Integer getWeightInGram() {
        return weightInGram;
    }

    public void setWeightInGram(Integer weightInGram) {
        this.weightInGram = weightInGram;
    }

    public MedicationModelDto imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    /**
     * Get imageUrl
     *
     * @return imageUrl
     */

    @Schema(name = "imageUrl", example = "https://imageurl.com", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("imageUrl")
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MedicationModelDto medicationModel = (MedicationModelDto) o;
        return Objects.equals(this.code, medicationModel.code) &&
                Objects.equals(this.name, medicationModel.name) &&
                Objects.equals(this.weightInGram, medicationModel.weightInGram) &&
                Objects.equals(this.imageUrl, medicationModel.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, weightInGram, imageUrl);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MedicationModelDto {\n");
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    weightInGram: ").append(toIndentedString(weightInGram)).append("\n");
        sb.append("    imageUrl: ").append(toIndentedString(imageUrl)).append("\n");
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

