package com.provetl.ProvETL.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.provetl.ProvETL.model.entity.Attribute;
import com.provetl.ProvETL.model.entity.AttributeType;
import com.provetl.ProvETL.model.entity.DataSetSchema;

@Repository
public class DataSetRepository {

    private final JdbcTemplate jdbcTemplate;

    public DataSetRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createTable(String tableName, DataSetSchema dataSetSchema) {
        List<Attribute> columns = dataSetSchema.getAttributes();

        StringBuilder createTableSQL = new StringBuilder("CREATE TABLE IF NOT EXISTS ");

        createTableSQL.append(tableName).append(" (");

        for (Attribute column : columns) {
            String columnName = column.getName();
            String columnType = " "
                    + (column.getType().equals(AttributeType.VARCHAR) ? "VARCHAR(255)" : column.getType().toString())
                    + ", ";

            createTableSQL.append(columnName).append(columnType);
        }

        createTableSQL.delete(createTableSQL.length() - 2, createTableSQL.length());
        createTableSQL.append(");");

        jdbcTemplate.execute(createTableSQL.toString());
    }

    public void insertData(String tableName, DataSetSchema dataSetSchema, Map<String, Object> dataSet) {     
        Map<String, Object> caseInsensitiveDataSet = new HashMap<>();
        for (Map.Entry<String, Object> entry : dataSet.entrySet()) {
            caseInsensitiveDataSet.put(entry.getKey().toUpperCase(), entry.getValue());
        }
        
        List<Attribute> columns = dataSetSchema.getAttributes();
        List<String> columnNames = new ArrayList<>();

        for (Attribute column : columns) {
            columnNames.add(column.getName());
        }

        String insertSQL = "INSERT INTO " + tableName + " (" + String.join(", ", columnNames) + ") VALUES (" +
                String.join(", ", Collections.nCopies(columns.size(), "?")) + ")";

        List<Object[]> batchArgs = new ArrayList<>();

        Object[] rowValues = new Object[columnNames.size()];

        for (int i = 0; i < columnNames.size(); i++) {
            String columnName = columnNames.get(i).toUpperCase();
            rowValues[i] = caseInsensitiveDataSet.get(columnName);
        }

        batchArgs.add(rowValues);

        jdbcTemplate.batchUpdate(insertSQL, batchArgs);
    }

    public List<Map<String, Object>> select(String tableName, List<String> attributes, String conditions) {
        try {
            String attributeList = String.join(", ", attributes);

            String selectSQL = "SELECT " + attributeList + " FROM " + tableName;
            List<Object> params = new ArrayList<>();

            if (conditions != null && !conditions.isEmpty()) {
                String[] conditionsArray = conditions.split("\\s+and\\s+");
                StringBuilder whereClause = new StringBuilder();

                for (String condition : conditionsArray) {
                    String conditionChar = "";

                    if (condition.contains("=")) {
                        conditionChar = "=";
                    } else if (condition.contains(">")) {
                        conditionChar = ">";
                    } else if (condition.contains("<")) {
                        conditionChar = "<";
                    }

                    String[] parts = condition.split("\\s*" + conditionChar + "\\s*");
                    
                    if (parts.length == 2) {
                        String attributeName = parts[0].trim();
                        String paramValue = parts[1].trim();
                        whereClause.append(attributeName).append(" " + conditionChar + " ? and ");
                        params.add(paramValue);
                    }
                }

                if (whereClause.length() > 0) {
                    whereClause.setLength(whereClause.length() - 5);
                    selectSQL += " WHERE " + whereClause.toString();
                }
            }

            return jdbcTemplate.queryForList(selectSQL, params.toArray());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public Boolean dropTable(String tableName) {
        try {
            StringBuilder dropTableSQL = new StringBuilder("DROP TABLE ");

            dropTableSQL.append(tableName).append(";");
            
            jdbcTemplate.execute(dropTableSQL.toString());

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
