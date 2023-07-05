package com.musalasoft.dronesadministration.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.Generated;
import java.io.Serializable;
import java.util.Objects;

/**
 * ProblemDto
 */

@JsonTypeName("Problem")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class ProblemDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer httpStatusCode;

    private String httpStatusMessage;

    private String detailedErrorReason;

    public ProblemDto httpStatusCode(Integer httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
        return this;
    }

    /**
     * Get httpStatusCode
     *
     * @return httpStatusCode
     */

    @Schema(name = "httpStatusCode", example = "400", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("httpStatusCode")
    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(Integer httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public ProblemDto httpStatusMessage(String httpStatusMessage) {
        this.httpStatusMessage = httpStatusMessage;
        return this;
    }

    /**
     * Get httpStatusMessage
     *
     * @return httpStatusMessage
     */

    @Schema(name = "httpStatusMessage", example = "BAD REQUEST", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("httpStatusMessage")
    public String getHttpStatusMessage() {
        return httpStatusMessage;
    }

    public void setHttpStatusMessage(String httpStatusMessage) {
        this.httpStatusMessage = httpStatusMessage;
    }

    public ProblemDto detailedErrorReason(String detailedErrorReason) {
        this.detailedErrorReason = detailedErrorReason;
        return this;
    }

    /**
     * Get detailedErrorReason
     *
     * @return detailedErrorReason
     */

    @Schema(name = "detailedErrorReason", example = "Invalid Request Data", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("detailedErrorReason")
    public String getDetailedErrorReason() {
        return detailedErrorReason;
    }

    public void setDetailedErrorReason(String detailedErrorReason) {
        this.detailedErrorReason = detailedErrorReason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProblemDto problem = (ProblemDto) o;
        return Objects.equals(this.httpStatusCode, problem.httpStatusCode) &&
                Objects.equals(this.httpStatusMessage, problem.httpStatusMessage) &&
                Objects.equals(this.detailedErrorReason, problem.detailedErrorReason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(httpStatusCode, httpStatusMessage, detailedErrorReason);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ProblemDto {\n");
        sb.append("    httpStatusCode: ").append(toIndentedString(httpStatusCode)).append("\n");
        sb.append("    httpStatusMessage: ").append(toIndentedString(httpStatusMessage)).append("\n");
        sb.append("    detailedErrorReason: ").append(toIndentedString(detailedErrorReason)).append("\n");
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

