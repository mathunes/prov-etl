package com.provetl.ProvETL.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.provetl.ProvETL.model.entity.DataTransformation;
import com.provetl.ProvETL.service.DataTransformationService;
import com.provetl.ProvETL.utility.ErrorResponse;

@RestController
@RequestMapping("/api/v1/dataflows")
public class DataTransformationController {

    private final DataTransformationService dataTransformationService;
    
    @Autowired
    public DataTransformationController(DataTransformationService dataTransformationService) {
        this.dataTransformationService = dataTransformationService;
    }

    @PostMapping("/{dataflowId}/transformations")
    public ResponseEntity<?> createDataTransformationForDataflow(@PathVariable Long dataflowId, @RequestBody DataTransformation dataTransformation) {
        try {
            DataTransformation createdDataTransformation = dataTransformationService.createDataTransformationForDataflow(dataflowId, dataTransformation);
            return ResponseEntity.ok(createdDataTransformation);
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/{dataflowId}/transformations")
    public ResponseEntity<?> getAllDataTransformationByDataflow(@PathVariable Long dataflowId) {
        try {
            return ResponseEntity.ok(dataTransformationService.getAllDataTransformationByDataflow(dataflowId));
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PutMapping("/{dataflowId}/transformations/{dataTransformationId}")
    public ResponseEntity<?> updateDataTransformationById(@PathVariable Long dataflowId, @PathVariable Long dataTransformationId, @RequestBody DataTransformation dataTransformation) {
        try {
            DataTransformation updatedDataTransformation = dataTransformationService.updateDataTransformationById(dataflowId, dataTransformationId, dataTransformation);

            if (updatedDataTransformation != null) {
                return ResponseEntity.ok(updatedDataTransformation);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("{dataflowId}/transformations/{dataTransformationId}")
    public ResponseEntity<?> getDataTransformationById(@PathVariable Long dataflowId, @PathVariable Long dataTransformationId) {
        try {
            DataTransformation dataTransformation = dataTransformationService.getDataTransformationById(dataflowId, dataTransformationId);
        
            if (dataTransformation != null) {
                return ResponseEntity.ok(dataTransformation);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

}
