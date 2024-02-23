package com.provetl.ProvETL.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.provetl.ProvETL.service.DataSetService;
import com.provetl.ProvETL.utility.ErrorResponse;

@RestController
@RequestMapping("/api/v1/dataflows")
public class DataSetController {

    private final DataSetService dataSetService;

    @Autowired
    public DataSetController(DataSetService dataSetService) {
        this.dataSetService = dataSetService;
    }

    @PostMapping("/{dataflowId}/transformations/{dataTransformationId}/data-set-schema/{dataSetSchemaId}/data-set")
    public ResponseEntity<?> createDataSetTable(@PathVariable Long dataflowId, @PathVariable Long dataTransformationId, @PathVariable Long dataSetSchemaId, @RequestBody Map<String, Object> dataSet) {
        try {
            dataSetService.createDataSetTable(dataflowId, dataTransformationId, dataSetSchemaId, dataSet);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/{dataflowId}/transformations/{dataTransformationId}/data-set-schema/{dataSetSchemaId}/data-set")
    public ResponseEntity<?> getDataSetTableData(@PathVariable Long dataflowId, @PathVariable Long dataTransformationId, @PathVariable Long dataSetSchemaId, @RequestParam List<String> attributes, @RequestParam String conditions) {
        try {
            return ResponseEntity.ok(dataSetService.getDataSetTableData(dataflowId, dataTransformationId, dataSetSchemaId, attributes, conditions));
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @DeleteMapping("/{dataflowId}/transformations/{dataTransformationId}/data-set-schema/{dataSetSchemaId}/data-set")
    public ResponseEntity<?> dropDataSetTable(@PathVariable Long dataflowId, @PathVariable Long dataTransformationId, @PathVariable Long dataSetSchemaId) {
        try {
            return ResponseEntity.ok(dataSetService.dropTable(dataflowId, dataTransformationId, dataSetSchemaId));
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

}
