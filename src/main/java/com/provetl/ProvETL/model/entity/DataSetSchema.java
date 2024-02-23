package com.provetl.ProvETL.model.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "data_set_schema")
public class DataSetSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "dataSetSchema", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Attribute> attributes;

    @OneToMany(mappedBy = "dataSetSchema")
    private List<DataTransformationDataSetSchema> dataTransformationDataSetSchemas = new ArrayList<>();

    public List<DataTransformationDataSetSchema> getDataTransformationDataSetSchemas() {
        return dataTransformationDataSetSchemas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public void setDataTransformationDataSetSchemas(List<DataTransformationDataSetSchema> dataTransformationDataSetSchemas) {
        this.dataTransformationDataSetSchemas = dataTransformationDataSetSchemas;
    }
}
