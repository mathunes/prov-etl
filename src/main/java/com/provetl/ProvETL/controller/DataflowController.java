package com.provetl.ProvETL.controller;

import com.provetl.ProvETL.model.dto.DataTransformationDTO;
import com.provetl.ProvETL.model.entity.Dataflow;
import com.provetl.ProvETL.service.DataflowService;
import com.provetl.ProvETL.utility.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dataflows")
public class DataflowController {

    private final DataflowService dataflowService;

    @Autowired
    public DataflowController(DataflowService dataflowService) {
        this.dataflowService = dataflowService;
    }

    @PostMapping
    public ResponseEntity<?> createDataflow(@RequestBody Dataflow dataflow) {
        try {
            Dataflow createdDataflow = dataflowService.createDataflow(dataflow);
            return ResponseEntity.ok(createdDataflow);
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping
    public List<Dataflow> getAllDataflows() {
        return dataflowService.getAllDataflows();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDataflowById(@PathVariable Long id) {
        Dataflow dataflow = dataflowService.getDataflowById(id);
        
        if (dataflow != null) {
            return ResponseEntity.ok(dataflow);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/transformations-dataset")
    public ResponseEntity<?> getAllInformationFromDataflow(@PathVariable Long id) {
        List<DataTransformationDTO> dataTransformationDTOs = dataflowService.getAllInformationFromDataflow(id);

        return ResponseEntity.ok(dataTransformationDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDataflow(@PathVariable Long id, @RequestBody Dataflow dataflow) {
        try {
            Dataflow updatedDataflow = dataflowService.updateDataflow(id, dataflow);

            if (updatedDataflow != null) {
                return ResponseEntity.ok(updatedDataflow);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDataflow(@PathVariable Long id) {
        if (dataflowService.deleteDataflow(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
