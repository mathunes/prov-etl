package com.provetl.ProvETL.service;

import com.provetl.ProvETL.model.dto.DataTransformationDTO;
import com.provetl.ProvETL.model.entity.DataTransformation;
import com.provetl.ProvETL.model.entity.Dataflow;
import com.provetl.ProvETL.repository.DataTransformationRepository;
import com.provetl.ProvETL.repository.DataflowRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataflowService {

    private final DataflowRepository dataflowRepository;
    private final DataTransformationRepository dataTransformationRepository;

    @Autowired
    public DataflowService(DataflowRepository dataflowRepository, DataTransformationRepository dataTransformationRepository) {
        this.dataflowRepository = dataflowRepository;
        this.dataTransformationRepository = dataTransformationRepository;
    }

    public Dataflow createDataflow(Dataflow dataflow) {
        if (dataflowRepository.existsByName(dataflow.getName())) {
            throw new IllegalArgumentException("Dataflow with the same name already exists.");
        }

        dataflow.setCreatedAt(LocalDateTime.now());
        dataflow.setUpdatedAt(LocalDateTime.now());
        return dataflowRepository.save(dataflow);
    }

    public List<Dataflow> getAllDataflows() {
        return dataflowRepository.findAll();
    }

    public Dataflow getDataflowById(Long id) {
        return dataflowRepository.findById(id).orElse(null);
    }

    public List<DataTransformationDTO> getAllInformationFromDataflow(Long id) {
        Dataflow dataflow = dataflowRepository.findById(id).orElse(null);
        
        List<DataTransformationDTO> dataTransformationDTOs = new ArrayList<>();

        if (dataflow != null) {

            List<DataTransformation> dataTransformations = dataflow.getDataTransformations();

            for (DataTransformation dataTransformation : dataTransformations) {

                dataTransformationDTOs.addAll(dataTransformationRepository.getAllInformationFromDataTransformation(dataTransformation.getId()));

            }

            return dataTransformationDTOs;

        } else {
            return null;
        }
    }

    public Dataflow updateDataflow(Long id, Dataflow updatedDataflow) {
        Dataflow existingDataflow = dataflowRepository.findById(id).orElse(null);

        if (existingDataflow != null) {
            if (dataflowRepository.existsByName(updatedDataflow.getName())) {
                throw new IllegalArgumentException("Dataflow with the same name already exists.");
            } else if (updatedDataflow.getName().equals("")) {
                throw new IllegalArgumentException("Dataflow name cannot be empty.");
            }

            existingDataflow.setName(updatedDataflow.getName());
            existingDataflow.setDescription(updatedDataflow.getDescription());
            existingDataflow.setUpdatedAt(LocalDateTime.now());
            dataflowRepository.save(existingDataflow);
        }

        return existingDataflow;
    }

    public Boolean deleteDataflow(Long id) {
        if (dataflowRepository.existsById(id)) {
            dataflowRepository.deleteById(id);
            return true;
        } 
        
        return false;
    }

}
