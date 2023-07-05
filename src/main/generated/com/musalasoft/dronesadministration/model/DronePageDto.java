package com.musalasoft.dronesadministration.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.Generated;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * DronePageDto
 */

@JsonTypeName("DronePage")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class DronePageDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer pageNumber;

    private Integer pageSize;

    private Integer totalElements;

    @Valid
    private List<@Valid DroneModelDto> content;

    public DronePageDto pageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    /**
     * Get pageNumber
     *
     * @return pageNumber
     */

    @Schema(name = "pageNumber", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("pageNumber")
    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public DronePageDto pageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    /**
     * Get pageSize
     *
     * @return pageSize
     */

    @Schema(name = "pageSize", example = "10", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("pageSize")
    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public DronePageDto totalElements(Integer totalElements) {
        this.totalElements = totalElements;
        return this;
    }

    /**
     * Get totalElements
     *
     * @return totalElements
     */

    @Schema(name = "totalElements", example = "100", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("totalElements")
    public Integer getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Integer totalElements) {
        this.totalElements = totalElements;
    }

    public DronePageDto content(List<@Valid DroneModelDto> content) {
        this.content = content;
        return this;
    }

    public DronePageDto addContentItem(DroneModelDto contentItem) {
        if (this.content == null) {
            this.content = new ArrayList<>();
        }
        this.content.add(contentItem);
        return this;
    }

    /**
     * Get content
     *
     * @return content
     */
    @Valid
    @Schema(name = "content", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("content")
    public List<@Valid DroneModelDto> getContent() {
        return content;
    }

    public void setContent(List<@Valid DroneModelDto> content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DronePageDto dronePage = (DronePageDto) o;
        return Objects.equals(this.pageNumber, dronePage.pageNumber) &&
                Objects.equals(this.pageSize, dronePage.pageSize) &&
                Objects.equals(this.totalElements, dronePage.totalElements) &&
                Objects.equals(this.content, dronePage.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageNumber, pageSize, totalElements, content);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DronePageDto {\n");
        sb.append("    pageNumber: ").append(toIndentedString(pageNumber)).append("\n");
        sb.append("    pageSize: ").append(toIndentedString(pageSize)).append("\n");
        sb.append("    totalElements: ").append(toIndentedString(totalElements)).append("\n");
        sb.append("    content: ").append(toIndentedString(content)).append("\n");
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

