package com.provetl.ProvETL.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "data_transformation")
public class DataTransformation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(name = "executed_by")
    private String executedBy;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @Column(name = "number_of_input_tuples")
    private Integer numberOfInputTuples;
    
    @Column(name = "number_of_output_tuples")
    private Integer numberOfOutputTuples;

    @ManyToOne
    @JoinColumn(name = "dataflow_id")
    @JsonBackReference
    private Dataflow dataflow;

    @OneToMany(mappedBy = "dataTransformation")
    private List<DataTransformationDataSetSchema> dataTransformationDataSetSchemas = new ArrayList<>();

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

    public String getExecutedBy() {
        return executedBy;
    }

    public void setExecutedBy(String executedBy) {
        this.executedBy = executedBy;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    public Integer getNumberOfInputTuples() {
        return numberOfInputTuples;
    }

    public void setNumberOfInputTuples(Integer numberOfInputTuples) {
        this.numberOfInputTuples = numberOfInputTuples;
    }

    public Integer getNumberOfOutputTuples() {
        return numberOfOutputTuples;
    }

    public void setNumberOfOutputTuples(Integer numberOfOutputTuples) {
        this.numberOfOutputTuples = numberOfOutputTuples;
    }

    public Dataflow getDataflow() {
        return dataflow;
    }

    public void setDataflow(Dataflow dataflow) {
        this.dataflow = dataflow;
    }    

    public List<DataTransformationDataSetSchema> getDataTransformationDataSetSchemas() {
        return dataTransformationDataSetSchemas;
    }

    public void setDataTransformationDataSetSchemas(
            List<DataTransformationDataSetSchema> dataTransformationDataSetSchemas) {
        this.dataTransformationDataSetSchemas = dataTransformationDataSetSchemas;
    }

}
