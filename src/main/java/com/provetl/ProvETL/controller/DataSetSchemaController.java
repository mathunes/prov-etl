package com.provetl.ProvETL.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.provetl.ProvETL.model.dto.DataSetSchemaDTO;
import com.provetl.ProvETL.model.dto.RelationTypeDTO;
import com.provetl.ProvETL.model.entity.Attribute;
import com.provetl.ProvETL.model.entity.DataSetSchema;
import com.provetl.ProvETL.service.DataSetSchemaService;
import com.provetl.ProvETL.utility.ErrorResponse;

@RestController
@RequestMapping("/api/v1/dataflows")
public class DataSetSchemaController {

    private final DataSetSchemaService dataSetSchemaService;
    
    @Autowired
    public DataSetSchemaController(DataSetSchemaService dataSetSchemaService) {
        this.dataSetSchemaService = dataSetSchemaService;
    }

    @PostMapping("/{dataflowId}/transformations/{dataTransformationId}/data-set-schema")
    public ResponseEntity<?> createDataSetSchemaForDataTransformation(@PathVariable Long dataflowId, @PathVariable Long dataTransformationId, @RequestBody DataSetSchemaDTO dataSetSchemaDTO) {
        try {
            DataSetSchema createdDataSetSchema = dataSetSchemaService.createDataSetSchemaForDataTransformation(dataflowId, dataTransformationId, dataSetSchemaDTO.getRelationType(), dataSetSchemaDTO.getDataSetSchema());
            return ResponseEntity.ok(createdDataSetSchema);
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/{dataflowId}/transformations/{dataTransformationId}/data-set-schema/{dataSetSchemaId}")
    public ResponseEntity<?> createDataSetSchemaForDataTransformation(@PathVariable Long dataflowId, @PathVariable Long dataTransformationId, @RequestBody RelationTypeDTO relationTypeDTO, @PathVariable Long dataSetSchemaId) {
        try {
            DataSetSchema createdDataSetSchema = dataSetSchemaService.createDataSetSchemaForDataTransformation(dataflowId, dataTransformationId, relationTypeDTO.getRelationType(), dataSetSchemaId);
            return ResponseEntity.ok(createdDataSetSchema);
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/{dataflowId}/transformations/{dataTransformationId}/data-set-schema")
    public ResponseEntity<?> getAllDataSetSchemaByDataTransformation(@PathVariable Long dataflowId, @PathVariable Long dataTransformationId) {
        try {
            return ResponseEntity.ok(dataSetSchemaService.getAllDataSetSchemaByDataTransformation(dataflowId, dataTransformationId));
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("{dataflowId}/transformations/{dataTransformationId}/data-set-schema/{dataSetSchemaId}")
    public ResponseEntity<?> getDataSetSchemaById(@PathVariable Long dataflowId, @PathVariable Long dataTransformationId, @PathVariable Long dataSetSchemaId) {
        try {
            DataSetSchema dataSetSchema = dataSetSchemaService.getDataSetSchemaById(dataflowId, dataTransformationId, dataSetSchemaId);
        
            if (dataSetSchema != null) {
                return ResponseEntity.ok(dataSetSchema);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("{dataflowId}/data-set-schema/{dataSetSchemaId}/attributes")
    public ResponseEntity<?> getAttributesByDataSetSchemaId(@PathVariable Long dataflowId, @PathVariable Long dataSetSchemaId) {
        try {
            List<Attribute> attributes = dataSetSchemaService.getAttributesByDataSetSchemaId(dataflowId, dataSetSchemaId);

            return ResponseEntity.ok(attributes);
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
