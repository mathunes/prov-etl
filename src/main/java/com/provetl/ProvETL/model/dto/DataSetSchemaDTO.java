package com.provetl.ProvETL.model.dto;

import com.provetl.ProvETL.model.entity.DataSetSchema;
import com.provetl.ProvETL.model.entity.DataTransformationDataSetSchemaRelationType;

public class DataSetSchemaDTO {
    
    private DataTransformationDataSetSchemaRelationType relationType;
    private DataSetSchema dataSetSchema;

    public DataTransformationDataSetSchemaRelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(DataTransformationDataSetSchemaRelationType relationType) {
        this.relationType = relationType;
    }

    public DataSetSchema getDataSetSchema() {
        return dataSetSchema;
    }

    public void setDataSetSchema(DataSetSchema dataSetSchema) {
        this.dataSetSchema = dataSetSchema;
    }

}
