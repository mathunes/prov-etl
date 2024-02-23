package com.provetl.ProvETL.model.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "data_transformation_data_set_schema")
public class DataTransformationDataSetSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "data_transformation_id")
    private DataTransformation dataTransformation;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "data_set_schema_id")
    private DataSetSchema dataSetSchema;

    @Enumerated(EnumType.STRING)
    private DataTransformationDataSetSchemaRelationType relationType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DataTransformation getDataTransformation() {
        return dataTransformation;
    }

    public void setDataTransformation(DataTransformation dataTransformation) {
        this.dataTransformation = dataTransformation;
    }

    public DataSetSchema getDataSetSchema() {
        return dataSetSchema;
    }

    public void setDataSetSchema(DataSetSchema dataSetSchema) {
        this.dataSetSchema = dataSetSchema;
    }

    public DataTransformationDataSetSchemaRelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(DataTransformationDataSetSchemaRelationType relationType) {
        this.relationType = relationType;
    }

}
