package com.provetl.ProvETL.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import com.provetl.ProvETL.model.dto.DataTransformationDTO;
import com.provetl.ProvETL.model.entity.Dataflow;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class UIController {

    private final RestTemplate restTemplate;
    private final HttpServletRequest request;
    
    public UIController(RestTemplate restTemplate, HttpServletRequest request) {
        this.restTemplate = restTemplate;
        this.request = request;
    }

    @GetMapping("/dataflows")
    public String getDataflows(Model model) {
        String host = request.getServerName();
        int port = request.getServerPort();
        String apiUrl = "http://" + host + ":" + port + "/api/v1/dataflows";

        Dataflow[] dataflows = restTemplate.getForObject(apiUrl, Dataflow[].class);

        model.addAttribute("dataflows", dataflows);

        return "dataflows";
    }

    @GetMapping("/dataflows/{dataflowId}")
    public String getDataflow(Model model, @PathVariable Long dataflowId) throws JsonProcessingException {
        String host = request.getServerName();
        int port = request.getServerPort();

        String apiUrl = "http://" + host + ":" + port + "/api/v1/dataflows/" + dataflowId;

        Dataflow dataflow = restTemplate.getForObject(apiUrl, Dataflow.class);

        model.addAttribute("dataflow", dataflow);

        apiUrl = "http://" + host + ":" + port + "/api/v1/dataflows/" + dataflowId + "/transformations-dataset";

        DataTransformationDTO[] dataTransformationDTO = restTemplate.getForObject(apiUrl, DataTransformationDTO[].class);

        ObjectMapper objectMapper = new ObjectMapper();
        String dataTransformationDTOJson = objectMapper.writeValueAsString(dataTransformationDTO);

        model.addAttribute("dataTransformationDTOJson", dataTransformationDTOJson);

        return "dataflow";
    }

    @GetMapping("/documentation")
    public String getRestfulApiDocs() {
        return "restful-api-docs";
    }

}
