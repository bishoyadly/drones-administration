/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (6.6.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package com.musalasoft.dronesadministration.controller;

import com.musalasoft.dronesadministration.model.MedicationModelDto;
import com.musalasoft.dronesadministration.model.ProblemDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.NativeWebRequest;

import javax.annotation.Generated;
import javax.validation.Valid;
import java.util.Optional;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
@Validated
@Tag(name = "Medication", description = "Medication API Endpoints")
public interface MedicationApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /medication : Add New Medication
     * Add New Medication
     *
     * @param medicationModelDto Medication Request Body (required)
     * @return Success Addition Response (status code 200)
     * or Bad Medication Request (status code 400)
     */
    @Operation(
            operationId = "addMedication",
            summary = "Add New Medication",
            description = "Add New Medication",
            tags = {"Medication"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success Addition Response", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = MedicationModelDto.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Bad Medication Request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDto.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/medication",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    default ResponseEntity<Object> addMedication(
            @Parameter(name = "MedicationModelDto", description = "Medication Request Body", required = true) @Valid @RequestBody MedicationModelDto medicationModelDto
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"weightInGram\" : 10, \"code\" : \"ABC_123\", \"imageUrl\" : \"https://imageurl.com\", \"name\" : \"Medicine-123_1mg\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * DELETE /medication/{medicationCode} : Delete Medication By Code
     * Delete Existing Medication By Medication Code
     *
     * @param medicationCode Medication Code (required)
     * @return No Content Success Response (status code 204)
     * or Medication Does Not Exist (status code 404)
     * or Bad Update Request (status code 400)
     */
    @Operation(
            operationId = "deleteMedicationByCode",
            summary = "Delete Medication By Code",
            description = "Delete Existing Medication By Medication Code",
            tags = {"Medication"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content Success Response"),
                    @ApiResponse(responseCode = "404", description = "Medication Does Not Exist", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDto.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Bad Update Request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDto.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/medication/{medicationCode}",
            produces = {"application/json"}
    )
    default ResponseEntity<Object> deleteMedicationByCode(
            @Parameter(name = "medicationCode", description = "Medication Code", required = true, in = ParameterIn.PATH) @PathVariable("medicationCode") String medicationCode
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /medication/{medicationCode} : Get Medication By Code
     * Get Existing Medication Data By Medication Code
     *
     * @param medicationCode Medication Code (required)
     * @return Success Response (status code 200)
     * or Medication Does Not Exist (status code 404)
     * or Bad Update Request (status code 400)
     */
    @Operation(
            operationId = "getMedicationByCode",
            summary = "Get Medication By Code",
            description = "Get Existing Medication Data By Medication Code",
            tags = {"Medication"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success Response", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = MedicationModelDto.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Medication Does Not Exist", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDto.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Bad Update Request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDto.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/medication/{medicationCode}",
            produces = {"application/json"}
    )
    default ResponseEntity<Object> getMedicationByCode(
            @Parameter(name = "Object", description = "Medication Code", required = true, in = ParameterIn.PATH) @PathVariable("medicationCode") String medicationCode
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"weightInGram\" : 10, \"code\" : \"ABC_123\", \"imageUrl\" : \"https://imageurl.com\", \"name\" : \"Medicine-123_1mg\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * PUT /medication/{medicationCode} : Update Medication
     * Update Existing Medication Data
     *
     * @param medicationCode     Medication Code (required)
     * @param medicationModelDto Update Medication Request Body (required)
     * @return Success Update Response (status code 200)
     * or Medication Does Not Exist (status code 404)
     * or Bad Update Request (status code 400)
     */
    @Operation(
            operationId = "updateMedication",
            summary = "Update Medication",
            description = "Update Existing Medication Data",
            tags = {"Medication"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success Update Response", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = MedicationModelDto.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Medication Does Not Exist", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDto.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Bad Update Request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDto.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.PUT,
            value = "/medication/{medicationCode}",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    default ResponseEntity<Object> updateMedication(
            @Parameter(name = "medicationCode", description = "Medication Code", required = true, in = ParameterIn.PATH) @PathVariable("medicationCode") String medicationCode,
            @Parameter(name = "MedicationModelDto", description = "Update Medication Request Body", required = true) @Valid @RequestBody MedicationModelDto medicationModelDto
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"weightInGram\" : 10, \"code\" : \"ABC_123\", \"imageUrl\" : \"https://imageurl.com\", \"name\" : \"Medicine-123_1mg\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
