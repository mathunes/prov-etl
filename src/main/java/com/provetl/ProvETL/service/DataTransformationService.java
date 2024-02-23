package com.provetl.ProvETL.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.provetl.ProvETL.model.entity.DataTransformation;
import com.provetl.ProvETL.model.entity.Dataflow;
import com.provetl.ProvETL.repository.DataTransformationRepository;
import com.provetl.ProvETL.repository.DataflowRepository;

@Service
public class DataTransformationService {

    private final DataTransformationRepository dataTransformationRepository;
    private final DataflowRepository dataflowRepository;


    @Autowired
    public DataTransformationService(DataTransformationRepository dataTransformationRepository, DataflowRepository dataflowRepository) {
        this.dataTransformationRepository = dataTransformationRepository;
        this.dataflowRepository = dataflowRepository;
    }
    
    public DataTransformation createDataTransformationForDataflow(Long dataflowId, DataTransformation dataTransformation) {
        Dataflow dataflow = dataflowRepository.findById(dataflowId)
            .orElseThrow(() -> new IllegalArgumentException("Dataflow not found with id: " + dataflowId));

        dataTransformation.setDataflow(dataflow);

        return dataTransformationRepository.save(dataTransformation);
    }

    public List<DataTransformation> getAllDataTransformationByDataflow(Long dataflowId) {
        Dataflow dataflow = dataflowRepository.findById(dataflowId)
            .orElseThrow(() -> new IllegalArgumentException("Dataflow not found with id: " + dataflowId));

        return dataflow.getDataTransformations();
    }

    public DataTransformation updateDataTransformationById(Long dataflowId, Long dataTransformationId, DataTransformation updatedDataTransformation) {
        Dataflow dataflow = dataflowRepository.findById(dataflowId)
            .orElseThrow(() -> new IllegalArgumentException("Dataflow not found with id: " + dataflowId));

        DataTransformation dataTransformation = dataTransformationRepository.findById(dataTransformationId)
                .orElseThrow(() -> new IllegalArgumentException("DataTransformation not found with id: " + dataTransformationId));

        if (dataTransformation.getDataflow().equals(dataflow)) {
            
            DataTransformation existingDataTransformation = dataTransformationRepository.findById(dataTransformationId).orElse(null);

            if (existingDataTransformation != null) {
                if (updatedDataTransformation.getName() != null) {
                    existingDataTransformation.setName(updatedDataTransformation.getName());
                }
                if (updatedDataTransformation.getExecutedBy() != null) {
                    existingDataTransformation.setExecutedBy(updatedDataTransformation.getExecutedBy());
                }
                if (updatedDataTransformation.getStartedAt() != null) {
                    existingDataTransformation.setStartedAt(updatedDataTransformation.getStartedAt());
                }
                if (updatedDataTransformation.getFinishedAt() != null) {
                    existingDataTransformation.setFinishedAt(updatedDataTransformation.getFinishedAt());
                }
                if (updatedDataTransformation.getNumberOfInputTuples() != null) {
                    existingDataTransformation.setNumberOfInputTuples(updatedDataTransformation.getNumberOfInputTuples());
                }
                if (updatedDataTransformation.getNumberOfOutputTuples() != null) {
                    existingDataTransformation.setNumberOfOutputTuples(updatedDataTransformation.getNumberOfOutputTuples());
                }

                dataTransformationRepository.save(existingDataTransformation);
            }    

            return existingDataTransformation;

        } else {
            throw new IllegalArgumentException("DataTransformation does not belong to the specified Dataflow.");
        }
    }

    public DataTransformation getDataTransformationById(Long dataflowId, Long dataTransformationId) {
        Dataflow dataflow = dataflowRepository.findById(dataflowId)
            .orElseThrow(() -> new IllegalArgumentException("Dataflow not found with id: " + dataflowId));

        DataTransformation dataTransformation = dataTransformationRepository.findById(dataTransformationId)
                .orElseThrow(() -> new IllegalArgumentException("DataTransformation not found with id: " + dataTransformationId));

        if (dataTransformation.getDataflow().equals(dataflow)) {
            return dataTransformationRepository.findById(dataTransformationId).orElse(null);
        } else {
            throw new IllegalArgumentException("DataTransformation does not belong to the specified Dataflow.");
        }
    }

}
