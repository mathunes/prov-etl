package com.provetl.ProvETL.model.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "attributes")
public class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Enumerated(EnumType.STRING)
    @Column
    private AttributeType type;

    @ManyToOne
    @JoinColumn(name = "data_set_schema_id")
    @JsonBackReference
    private DataSetSchema dataSetSchema;

    public Attribute() {
    }

    public Attribute(String name, AttributeType type) {
        this.name = name;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AttributeType getType() {
        return type;
    }

    public void setType(AttributeType type) {
        this.type = type;
    }

    public DataSetSchema getDataSetSchema() {
        return dataSetSchema;
    }

    public void setDataSetSchema(DataSetSchema dataSetSchema) {
        this.dataSetSchema = dataSetSchema;
    }

}
