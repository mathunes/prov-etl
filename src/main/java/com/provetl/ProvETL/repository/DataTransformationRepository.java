package com.provetl.ProvETL.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.provetl.ProvETL.model.dto.DataTransformationDTO;
import com.provetl.ProvETL.model.entity.DataTransformation;

public interface DataTransformationRepository extends JpaRepository<DataTransformation, Long> {

    @Query("SELECT new com.provetl.ProvETL.model.dto.DataTransformationDTO(dt.name, dss.name, dt.id, dss.id, dtdss.relationType) " + 
        "FROM DataTransformation dt " + 
        "INNER JOIN dt.dataTransformationDataSetSchemas dtdss " +
        "INNER JOIN dtdss.dataSetSchema dss " + 
        "WHERE dt.id = ?1")
    List<DataTransformationDTO> getAllInformationFromDataTransformation(Long dataTransformationId);

}