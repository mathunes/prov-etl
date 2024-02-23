package com.provetl.ProvETL.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.provetl.ProvETL.model.entity.DataTransformationDataSetSchema;

public interface DataTransformationDataSetSchemaRepository extends JpaRepository<DataTransformationDataSetSchema, Long> {
    
}
