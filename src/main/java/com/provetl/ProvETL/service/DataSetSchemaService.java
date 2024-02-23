package com.provetl.ProvETL.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.provetl.ProvETL.model.entity.Attribute;
import com.provetl.ProvETL.model.entity.DataSetSchema;
import com.provetl.ProvETL.model.entity.DataTransformation;
import com.provetl.ProvETL.model.entity.DataTransformationDataSetSchema;
import com.provetl.ProvETL.model.entity.DataTransformationDataSetSchemaRelationType;
import com.provetl.ProvETL.model.entity.Dataflow;
import com.provetl.ProvETL.repository.DataSetSchemaRepository;
import com.provetl.ProvETL.repository.DataTransformationRepository;
import com.provetl.ProvETL.repository.DataflowRepository;
import com.provetl.ProvETL.repository.DataTransformationDataSetSchemaRepository;

@Service
public class DataSetSchemaService {
    
    private final DataSetSchemaRepository dataSetSchemaRepository;
    private final DataTransformationRepository dataTransformationRepository;
    private final DataflowRepository dataflowRepository;
    private final DataTransformationDataSetSchemaRepository dataTransformationDataSetSchemaRepository;

    @Autowired
    public DataSetSchemaService(DataSetSchemaRepository dataSetSchemaRepository, DataTransformationRepository dataTransformationRepository, DataflowRepository dataflowRepository, DataTransformationDataSetSchemaRepository dataTransformationDataSetSchemaRepository) {
        this.dataSetSchemaRepository = dataSetSchemaRepository;
        this.dataTransformationRepository = dataTransformationRepository;
        this.dataflowRepository = dataflowRepository;
        this.dataTransformationDataSetSchemaRepository = dataTransformationDataSetSchemaRepository;
    }

    public DataSetSchema createDataSetSchemaForDataTransformation(Long dataflowId, Long dataTransformationId, DataTransformationDataSetSchemaRelationType relationType, DataSetSchema dataSetSchema) {
        Dataflow dataflow = dataflowRepository.findById(dataflowId)
            .orElseThrow(() -> new IllegalArgumentException("Dataflow not found with id: " + dataflowId));
    
        DataTransformation dataTransformation = dataTransformationRepository.findById(dataTransformationId)
            .orElseThrow(() -> new IllegalArgumentException("DataTransformation not found with id: " + dataTransformationId));
        
        if (dataTransformation.getDataflow().equals(dataflow)) {
            
            DataSetSchema savedDataSetSchema = dataSetSchemaRepository.save(dataSetSchema);

            DataTransformationDataSetSchema dataTransformationDataSetSchema = new DataTransformationDataSetSchema();

            dataTransformationDataSetSchema.setDataSetSchema(dataSetSchema);
            dataTransformationDataSetSchema.setDataTransformation(dataTransformation);
            dataTransformationDataSetSchema.setRelationType(relationType);
    
            dataTransformationDataSetSchemaRepository.save(dataTransformationDataSetSchema);
    
            return savedDataSetSchema;
        } else {
            throw new IllegalArgumentException("DataTransformation does not belong to the specified Dataflow.");
        }
    }

    public DataSetSchema createDataSetSchemaForDataTransformation(Long dataflowId, Long dataTransformationId, DataTransformationDataSetSchemaRelationType relationType, Long dataSetSchemaId) {
        Dataflow dataflow = dataflowRepository.findById(dataflowId)
            .orElseThrow(() -> new IllegalArgumentException("Dataflow not found with id: " + dataflowId));

        DataTransformation dataTransformation = dataTransformationRepository.findById(dataTransformationId)
            .orElseThrow(() -> new IllegalArgumentException("DataTransformation not found with id: " + dataTransformationId));

        DataSetSchema dataSetSchema = dataSetSchemaRepository.findById(dataSetSchemaId)
            .orElseThrow(() -> new IllegalArgumentException("DataSetSchema not found with id: " + dataSetSchemaId));

        if (dataTransformation.getDataflow().equals(dataflow)) {
        
            DataTransformationDataSetSchema dataTransformationDataSetSchema = new DataTransformationDataSetSchema();
            
            dataTransformationDataSetSchema.setDataTransformation(dataTransformation);
            dataTransformationDataSetSchema.setDataSetSchema(dataSetSchema);
            dataTransformationDataSetSchema.setRelationType(relationType);

            dataTransformationDataSetSchemaRepository.save(dataTransformationDataSetSchema);

            return dataSetSchema;

        } else {
            throw new IllegalArgumentException("DataTransformation does not belong to the specified Dataflow.");
        }
    }

    public List<DataSetSchema> getAllDataSetSchemaByDataTransformation(Long dataflowId, Long dataTransformationId) {
        Dataflow dataflow = dataflowRepository.findById(dataflowId)
            .orElseThrow(() -> new IllegalArgumentException("Dataflow not found with id: " + dataflowId));

        DataTransformation dataTransformation = dataTransformationRepository.findById(dataTransformationId)
            .orElseThrow(() -> new IllegalArgumentException("DataTransformation not found with id: " + dataTransformationId));

        if (dataTransformation.getDataflow().equals(dataflow)) {
        
            List<DataSetSchema> dataSetSchemas = new ArrayList<>();

            for (DataTransformationDataSetSchema dataTransformationDataSetSchema : dataTransformation.getDataTransformationDataSetSchemas()) {
                dataSetSchemas.add(dataTransformationDataSetSchema.getDataSetSchema());
            }

            return dataSetSchemas;
        } else {
            throw new IllegalArgumentException("DataTransformation does not belong to the specified Dataflow.");
        }

    }

    public DataSetSchema getDataSetSchemaById(Long dataflowId, Long dataTransformationId, Long dataSetSchemaId) {
        Dataflow dataflow = dataflowRepository.findById(dataflowId)
            .orElseThrow(() -> new IllegalArgumentException("Dataflow not found with id: " + dataflowId));

        DataTransformation dataTransformation = dataTransformationRepository.findById(dataTransformationId)
            .orElseThrow(() -> new IllegalArgumentException("DataTransformation not found with id: " + dataTransformationId));

        DataSetSchema dataSetSchema = dataSetSchemaRepository.findById(dataSetSchemaId)
            .orElseThrow(() -> new IllegalArgumentException("DataSetSchema not found with id: " + dataSetSchemaId));

        if (dataTransformation.getDataflow().equals(dataflow)) {

            for (DataTransformationDataSetSchema dataTransformationDataSetSchema : dataSetSchema.getDataTransformationDataSetSchemas()) {
                if (dataTransformationDataSetSchema.getDataTransformation().equals(dataTransformation)) {
                    return dataSetSchemaRepository.findById(dataSetSchemaId).orElse(null);
                }
            }

            throw new IllegalArgumentException("DataSetSchema does not belong to the specified DataTransformation.");
        } else {
            throw new IllegalArgumentException("DataTransformation does not belong to the specified Dataflow.");
        }
        
    }

    public List<Attribute> getAttributesByDataSetSchemaId(Long dataflowId, Long dataSetSchemaId) {
        dataflowRepository.findById(dataflowId)
            .orElseThrow(() -> new IllegalArgumentException("Dataflow not found with id: " + dataflowId));

        dataSetSchemaRepository.findById(dataSetSchemaId)
            .orElseThrow(() -> new IllegalArgumentException("DataSetSchema not found with id: " + dataSetSchemaId));

        return dataSetSchemaRepository.findAttributesByDataSetSchemaId(dataSetSchemaId);
    }

}
