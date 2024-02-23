package com.provetl.ProvETL.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.provetl.ProvETL.model.entity.DataSetSchema;
import com.provetl.ProvETL.model.entity.DataTransformation;
import com.provetl.ProvETL.model.entity.DataTransformationDataSetSchema;
import com.provetl.ProvETL.model.entity.Dataflow;
import com.provetl.ProvETL.repository.DataSetRepository;
import com.provetl.ProvETL.repository.DataSetSchemaRepository;
import com.provetl.ProvETL.repository.DataTransformationRepository;
import com.provetl.ProvETL.repository.DataflowRepository;

@Service
public class DataSetService {

    private final DataSetRepository dataSetRepository;
    private final DataSetSchemaRepository dataSetSchemaRepository;
    private final DataTransformationRepository dataTransformationRepository;
    private final DataflowRepository dataflowRepository;

    @Autowired
    public DataSetService(DataSetSchemaRepository dataSetSchemaRepository, DataTransformationRepository dataTransformationRepository, DataflowRepository dataflowRepository, DataSetRepository dataSetRepository) {
        this.dataSetRepository = dataSetRepository;
        this.dataSetSchemaRepository = dataSetSchemaRepository;
        this.dataTransformationRepository = dataTransformationRepository;
        this.dataflowRepository = dataflowRepository;
    }
    
    public void createDataSetTable(Long dataflowId, Long dataTransformationId, Long dataSetSchemaId, Map<String, Object> dataSet) {
        Dataflow dataflow = dataflowRepository.findById(dataflowId)
            .orElseThrow(() -> new IllegalArgumentException("Dataflow not found with id: " + dataflowId));

        DataTransformation dataTransformation = dataTransformationRepository.findById(dataTransformationId)
            .orElseThrow(() -> new IllegalArgumentException("DataTransformation not found with id: " + dataTransformationId));

        DataSetSchema dataSetSchema = dataSetSchemaRepository.findById(dataSetSchemaId)
            .orElseThrow(() -> new IllegalArgumentException("DataSetSchema not found with id: " + dataSetSchemaId));

        boolean dataSetSchemaBelongsDataTransformation = false;

        if (dataTransformation.getDataflow().equals(dataflow)) {
            for (DataTransformationDataSetSchema dataTransformationDataSetSchema : dataSetSchema.getDataTransformationDataSetSchemas()) {
                
                if (dataTransformationDataSetSchema.getDataTransformation().equals(dataTransformation)) {
                    dataSetSchemaBelongsDataTransformation = true;

                    String tableName = dataflow.getId() + "_" + dataTransformation.getName() + "_" + dataTransformationDataSetSchema.getRelationType().toString().toLowerCase() + "_" + dataSetSchemaId;

                    dataSetRepository.createTable(tableName, dataSetSchema);
                    dataSetRepository.insertData(tableName, dataSetSchema, dataSet);
                }
            }

            if (!dataSetSchemaBelongsDataTransformation) {
                throw new IllegalArgumentException("DataSetSchema does not belong to the specified DataTransformation.");
            }
        } else {
            throw new IllegalArgumentException("DataTransformation does not belong to the specified Dataflow.");
        }
    }

    public List<Map<String, Object>> getDataSetTableData(Long dataflowId, Long dataTransformationId, Long dataSetSchemaId, List<String> attributes, String conditions) {
        Dataflow dataflow = dataflowRepository.findById(dataflowId)
            .orElseThrow(() -> new IllegalArgumentException("Dataflow not found with id: " + dataflowId));

        DataTransformation dataTransformation = dataTransformationRepository.findById(dataTransformationId)
            .orElseThrow(() -> new IllegalArgumentException("DataTransformation not found with id: " + dataTransformationId));

        DataSetSchema dataSetSchema = dataSetSchemaRepository.findById(dataSetSchemaId)
            .orElseThrow(() -> new IllegalArgumentException("DataSetSchema not found with id: " + dataSetSchemaId));

        if (dataTransformation.getDataflow().equals(dataflow)) {
            for (DataTransformationDataSetSchema dataTransformationDataSetSchema : dataSetSchema.getDataTransformationDataSetSchemas()) {
                if (dataTransformationDataSetSchema.getDataTransformation().equals(dataTransformation)) {
                    String tableName = dataflow.getId() + "_" + dataTransformation.getName() + "_" + dataTransformationDataSetSchema.getRelationType().toString().toLowerCase() + "_" + dataSetSchemaId;

                    return dataSetRepository.select(tableName, attributes, conditions);
                }
            }

            throw new IllegalArgumentException("DataSetSchema does not belong to the specified DataTransformation.");
        } else {
            throw new IllegalArgumentException("DataTransformation does not belong to the specified Dataflow.");
        }
    }

    public Boolean dropTable(Long dataflowId, Long dataTransformationId, Long dataSetSchemaId) {
        Dataflow dataflow = dataflowRepository.findById(dataflowId)
            .orElseThrow(() -> new IllegalArgumentException("Dataflow not found with id: " + dataflowId));

        DataTransformation dataTransformation = dataTransformationRepository.findById(dataTransformationId)
            .orElseThrow(() -> new IllegalArgumentException("DataTransformation not found with id: " + dataTransformationId));

        DataSetSchema dataSetSchema = dataSetSchemaRepository.findById(dataSetSchemaId)
            .orElseThrow(() -> new IllegalArgumentException("DataSetSchema not found with id: " + dataSetSchemaId));

        if (dataTransformation.getDataflow().equals(dataflow)) {
            for (DataTransformationDataSetSchema dataTransformationDataSetSchema : dataSetSchema.getDataTransformationDataSetSchemas()) {
                if (dataTransformationDataSetSchema.getDataTransformation().equals(dataTransformation)) {
                    String tableName = dataflow.getId() + "_" + dataTransformation.getName() + "_" + dataTransformationDataSetSchema.getRelationType().toString().toLowerCase() + "_" + dataSetSchemaId;

                    return dataSetRepository.dropTable(tableName);
                }
            }

            throw new IllegalArgumentException("DataSetSchema does not belong to the specified DataTransformation.");
        } else {
            throw new IllegalArgumentException("DataTransformation does not belong to the specified Dataflow.");
        }
    }

}
