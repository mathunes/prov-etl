# ProvETL

ProvETL tool extends a BIDW with provenance support, enabling the monitoring of user activities and data transformations, along with the compilation of an execution summary for each ETL task. Accordingly, ProvETL offers an additional BIDW analytical layer that allows visualizing data flows through provenance graphs.

## Installation

1. Download the [package available](https://github.com/mathunes/prov-etl)
2. Extract the compressed folder
3. Grant execution permission to the `mvnw` script

```
chmod +x mvnw
```

4. Start the application

```
./mvnw spring-boot:run
```

5. Check the application is running on 8080 port:

```
http://localhost:8080/dataflows
```

## Usage


The purpose of this guide is to present the methods and endpoints available for dataflow instrumentation. In this version, the documentation is focused on instrumenting the data flow of a Data Warehouse loading process, although it is not limited to this process and can be adapted for other scenarios.

To provide context for the entities presented below, we will detail their descriptions and relationships. The central entity, the starting point, is the Dataflow, which represents the data flow in an abstract manner and includes descriptive attributes such as "name" and "description". The second entity is the DataTransformation, which represents data transformations that occur in the data flow. A Dataflow can contain multiple DataTransformations, each with name from transformation. The third entity is the DataSetSchema, which represents the structure of transformed data, both at the input (before transformation) and at the output (after transformation). A DataTransformation can be related to multiple DataSetSchemas, and this relationship is categorized as "input" or "output". Furthermore, a DataSetSchema includes a list of attributes to represent the attributes and their types. Finally, the last entity is the DataSet itself, which represents the actual instance of the data in the transformation, not just the description but also the data values.

To perform instrumentation, it is necessary to make calls to the endpoints of this API, as documented here, in order to visualize the data flow through the interface. According to the previous description, the process begins with the creation of a Dataflow. With the returned ID, you can proceed with additional operations. After creating the Dataflow and obtaining the corresponding ID, you can create as many DataTransformations as needed, also saving the corresponding IDs for future use. With the IDs of the DataTransformations in hand, you can create the DataSetSchemas, specifying the attributes, which consist of "name" and "type" pairs, with possible types including VARCHAR, INT, FLOAT, DATE, DATETIME.

After creating the entire structure, data is sent to the DataSet, following the structure defined in the DataSetSchema. After this step, the data can be queried through the graphical interface.

## Entities

### Dataflow

**Create Dataflow**

- **Method**: POST
- **URL**: /api/v1/dataflows
- **Body Example:**

```json
{
    "name": "prograd_alunos",
    "description": "description about prograd_alunos"
}
```

**Get All Dataflows**

- **Method**: GET
- **URL**: /api/v1/dataflows

**Get Dataflow by ID**

- **Method**: GET
- **URL**: /api/v1/dataflows/{dataflow-id}

**Get Dataflow with Datasets**

- **Method**: GET
- **URL**: /api/v1/dataflows/{dataflow-id}/transformations-dataset

**Update Dataflow**

- **Method**: PUT
- **URL**: /api/v1/dataflows/{dataflow-id}
- **Body Example:**

```json
{
    "name": "prograd_alunos",
    "description": "description about prograd_alunos"
}
```

**Delete Dataflow**

- **Method**: DELETE
- **URL**: /api/v1/dataflows/{dataflow-id}

### DataTransformation

**Create DataTransformation**

- **Method**: POST
- **URL**: /api/v1/dataflows/{dataflow-id}/transformations
- **Body Example:**

```json
{
    "name": "normalization"
}
```

**Get All DataTransformations**

- **Method**: GET
- **URL**: /api/v1/dataflows/{dataflow-id}/transformations

**Get DataTransformation by ID**

- **Method**: GET
- **URL**: /api/v1/dataflows/{dataflow-id}/transformations/{datatransformation-id}

**Update DataTransformation**

- **Method**: PUT
- **URL**: /api/v1/dataflows/{dataflow-id}/transformations/{datatransformation-id}
- **Body Example:**

```json
{
    "executedBy": "Name",
    "startedAt": "2023-11-15T08:00:00",
    "finishedAt": "2023-11-15T08:30:00",
    "numberOfInputTuples": 100,
    "numberOfOutputTuples": 100
}
```

### DataSetSchema

**Create DataSetSchema**

- **Method**: POST
- **URL**: /api/v1/dataflows/{dataflow-id}/transformations/{datatransformation-id}/data-set-schema
- **Body Example:**

```json
{
    "relationType": "INPUT",
    "dataSetSchema": {
        "attributes": [
            {
                "name": "identification",
                "type": "VARCHAR"
            },
            {
                "name": "description",
                "type": "VARCHAR"
            }
        ]
    }
}
```

**Get All DataSetSchema**

- **Method**: GET
- **URL**: /api/v1/dataflows/{dataflow-id}/transformations/{datatransformation-id}/data-set-schema

**Get DataSetSchema by ID**

- **Method**: GET
- **URL**: /api/v1/dataflows/{dataflow-id}/transformations/{datatransformation-id}/data-set-schema/{data-set-schema-id}

**Get Attributes by DataSetSchema**

- **Method**: GET
- **URL**: /api/v1/dataflows/{dataflow-id}/data-set-schema/{data-set-schema-id}/attributes

### DataSet

**Create DataSet**

- **Method**: POST
- **URL**: /api/v1/dataflows/{dataflow-id}/transformations/{datatransformation-id}/data-set-schema/{data-set-schema-id}/data-set
- **Body Example:**

```json
{
    "identification": "432684",
    "description": "test"
}
```

**Get All DataSet Data**

- **Method**: GET
- **URL**: /api/v1/dataflows/{dataflow-id}/transformations/{datatransformation-id}/data-set-schema/{data-set-schema-id}/data-set?attributes=identification,description&conditions=description=test

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

After forking the project, follow the instructions below to work locally on the project:

- This project uses maven as dependency manager
- This project uses gitflow as a branch management technique

## License

MIT License

Copyright (c) 2024 ProvETL

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
