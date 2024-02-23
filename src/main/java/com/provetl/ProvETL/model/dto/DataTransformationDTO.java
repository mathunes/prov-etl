package com.provetl.ProvETL.model.dto;

import com.provetl.ProvETL.model.entity.DataTransformationDataSetSchemaRelationType;

public class DataTransformationDTO {

    private String dataTransformationName;
    private String dataSetSchemaName;
    private Long dataTransformationId;
    private Long dataSetSchemaId;
    private DataTransformationDataSetSchemaRelationType relationType;

    public DataTransformationDTO() {
    }

    public DataTransformationDTO(String dataTransformationName, String dataSetSchemaName, Long dataTransformationId, Long dataSetSchemaId, DataTransformationDataSetSchemaRelationType relationType) {
        this.dataTransformationName = dataTransformationName;
        this.dataSetSchemaName = dataSetSchemaName;
        this.dataTransformationId = dataTransformationId;
        this.dataSetSchemaId = dataSetSchemaId;
        this.relationType = relationType;
    }

    public String getDataTransformationName() {
        return dataTransformationName;
    }

    public void setDataTransformationName(String dataTransformationName) {
        this.dataTransformationName = dataTransformationName;
    }
    
    public String getDataSetSchemaName() {
        return dataSetSchemaName;
    }

    public void setDataSetSchemaName(String dataSetSchemaName) {
        this.dataSetSchemaName = dataSetSchemaName;
    }

    public Long getDataTransformationId() {
        return dataTransformationId;
    }
    
    public void setDataTransformationId(Long dataTransformationId) {
        this.dataTransformationId = dataTransformationId;
    }
    
    public Long getDataSetSchemaId() {
        return dataSetSchemaId;
    }
    
    public void setDataSetSchemaId(Long dataSetSchemaId) {
        this.dataSetSchemaId = dataSetSchemaId;
    }
    
    public DataTransformationDataSetSchemaRelationType getRelationType() {
        return relationType;
    }
    
    public void setRelationType(DataTransformationDataSetSchemaRelationType relationType) {
        this.relationType = relationType;
    }

}
