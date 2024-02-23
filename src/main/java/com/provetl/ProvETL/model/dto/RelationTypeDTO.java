package com.provetl.ProvETL.model.dto;

import com.provetl.ProvETL.model.entity.DataTransformationDataSetSchemaRelationType;

public class RelationTypeDTO {
    
    private DataTransformationDataSetSchemaRelationType relationType;

    public DataTransformationDataSetSchemaRelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(DataTransformationDataSetSchemaRelationType relationType) {
        this.relationType = relationType;
    }

}
