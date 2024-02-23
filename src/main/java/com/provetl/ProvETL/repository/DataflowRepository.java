package com.provetl.ProvETL.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.provetl.ProvETL.model.entity.Dataflow;

public interface DataflowRepository extends JpaRepository<Dataflow, Long> {

    boolean existsByName(String name);

}
