package com.provetl.ProvETL.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.provetl.ProvETL.model.entity.DataSetSchema;
import com.provetl.ProvETL.model.entity.Attribute;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DataSetSchemaRepository extends JpaRepository<DataSetSchema, Long> {

    @Query("SELECT a FROM DataSetSchema ds " +
           "JOIN ds.attributes a " +
           "WHERE ds.id = :dataSetSchemaId")
    List<Attribute> findAttributesByDataSetSchemaId(@Param("dataSetSchemaId") Long dataSetSchemaId);
}
