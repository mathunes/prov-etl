const pathnameURL = window.location.pathname;
const originURL = window.location.origin;

let queryResultTable;

const sidebarItems = document.querySelectorAll(".sidebar-item");

for (let i = 0; i < sidebarItems.length; i++) {
    const anchor = sidebarItems[i].querySelector("a");
    const href = anchor.getAttribute("href");

    if (pathnameURL.startsWith(href)) {
        anchor.classList.add("sidebar-item-active");
    }
}

const sidebarState = localStorage.getItem("sidebar");

if (sidebarState == "deactivated") {
    toggleSidebar();
}

const appState = {
    clickedNodeId: null,
    clickedEdgeId: null,
    transformationName: null
};

function setClickedNodeId(nodeId) {
    appState.clickedNodeId = nodeId;
}

function getClickedNodeId() {
    return appState.clickedNodeId;
}

function setClickedEdgeId(edgeId) {
    appState.clickedEdgeId = edgeId;
}

function getClickedEdgeId() {
    return appState.clickedEdgeId;
}

function setTransformationName(transformationName) {
    appState.transformationName = transformationName;
}

function getTransformationName() {
    return appState.transformationName;
}

function toggleSidebar() {
    const sidebar = document.getElementById("sidebar");
    const container = document.getElementById("container");
    const sidebarText = document.querySelectorAll(".sidebar-text");

    for (let i = 0; i < sidebarText.length; i++) {
        if (sidebar.classList.contains("sidebar-deactivated")) {
            const textOptions = ["Visualization", "Documentation"];
            sidebarText[i].textContent = textOptions[i];
        } else {
            sidebarText[i].textContent = "";
        }
    }

    if (sidebar.classList.contains("sidebar-deactivated")) {
        sidebar.classList.remove("sidebar-deactivated");
        container.classList.remove("container-sidebar-deactivated");
        localStorage.setItem("sidebar", "activated");
    } else {
        sidebar.classList.add("sidebar-deactivated");
        container.classList.add("container-sidebar-deactivated");

        localStorage.setItem("sidebar", "deactivated");
    }
}

function generateIdsString(dataList) {
    const uniqueDataSchemaIds = new Set();
    const uniqueDataTransformationIds = new Set();

    const schemaStrings = dataList.reduce((result, item) => {
        const dataSetSchemaLabel = (item.dataSetSchemaName) ? item.dataSetSchemaName : `${item.relationType}_${item.dataTransformationName}`.toLowerCase();
        const dataSetSchemaColor = item.relationType === "INPUT" ? "#FDFFB6" : "#A0C4FF";

        if (!uniqueDataSchemaIds.has(item.dataSetSchemaId)) {
            uniqueDataSchemaIds.add(item.dataSetSchemaId);
            result.push(`data_set_schema_${item.dataSetSchemaId} [label="${dataSetSchemaLabel}" color=${dataSetSchemaColor} is_transformation="false"]`);
        }

        const dataTransformationLabel = `${item.dataTransformationName}`.toLowerCase();
        const dataTransformationColor = "#FFADAD";

        if (!uniqueDataTransformationIds.has(item.dataTransformationId)) {
            uniqueDataTransformationIds.add(item.dataTransformationId);
            result.push(`data_transformation_${item.dataTransformationId} [label="${dataTransformationLabel}" title="${item.dataTransformationId}" color=${dataTransformationColor} is_transformation="true"]`);
        }

        return result;
    }, []);

    return schemaStrings.join('; ');
}

function generateIdPairsString(dataList) {
    const groupedSet = dataList.reduce((result, set) => {
        if (!result[set.dataTransformationId]) {
            result[set.dataTransformationId] = { input: [], output: [] };
        }

        if (set.relationType === "INPUT") {
            result[set.dataTransformationId].input.push(set);
        }

        if (set.relationType === "OUTPUT") {
            result[set.dataTransformationId].output.push(set);
        }

        return result;
    }, {});

    const pairs = [];

    for (const set in groupedSet) {
        const { input, output } = groupedSet[set];
        input.forEach(inputItem => {
            output.forEach(outputItem => {
                pairs.push(`data_set_schema_${inputItem.dataSetSchemaId} -> data_transformation_${inputItem.dataTransformationId} [title="${inputItem.dataTransformationId}" color="black"];`);
                pairs.push(`data_transformation_${inputItem.dataTransformationId} -> data_set_schema_${outputItem.dataSetSchemaId} [title="${inputItem.dataTransformationId}" color="black"];`);
            });
        });
    }

    return pairs.join(" ");
}

function setDataSetSchemaName(dataSetSchemaName) {
    var h2ElementQueryDashboard = document.querySelector("#container .aside-content #query-dashboard h2");

    h2ElementQueryDashboard.innerHTML = "Query Dashboard: <b>" + dataSetSchemaName + "</b>";
}

function getAttributesByDataSetSchemaId(dataSetSchemaId) {
    const dataflowId = pathnameURL.split("/")[2];

    dataSetSchemaId = dataSetSchemaId.split("_")[dataSetSchemaId.split("_").length - 1];

    const url = `${originURL}/api/v1/dataflows/${dataflowId}/data-set-schema/${dataSetSchemaId}/attributes`;

    return fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            return data;
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function toggleElementVisibility(selector, show) {
    var element = document.querySelector("#container .aside-content " + selector);

    if (show) {
        element.classList.remove("display-none");
        element.classList.add("display-block");
    } else {
        element.classList.remove("display-block");
        element.classList.add("display-none");
    }
}

function showQueryAttributes(attributes) {
    var attributesElement = document.querySelector("#container .aside-content #query-dashboard .attributes");

    while (attributesElement.firstChild) {
        attributesElement.firstChild.remove();
    }

    var ulElement = document.createElement("ul");
    attributesElement.append(ulElement);

    attributes.forEach(attribute => {
        var liElement = document.createElement("li");

        var inputElement = document.createElement("input");
        inputElement.type = "checkbox";
        inputElement.id = attribute.name;
        inputElement.checked = true;

        var labelElement = document.createElement("label");
        labelElement.htmlFor = attribute.name;
        labelElement.textContent = attribute.name;

        liElement.appendChild(inputElement);
        liElement.appendChild(labelElement);
        ulElement.append(liElement);
    });

    showQueryDashboardTab();
}

function showTransformationValues() {
    var h2DataTransformationElement = document.querySelector("#container .aside-content #data-transformation h2");

    h2DataTransformationElement.innerHTML = "Values of transformation: <b>" + getTransformationName() + "</b>";

    getDataTransformationValues();
}

function showDescription() {
    toggleElementVisibility("#data-transformation", false);
    toggleElementVisibility("#query-dashboard", false);
    toggleElementVisibility("#description", true);
}

function showQueryDashboardTab() {
    toggleElementVisibility("#description", false);
    toggleElementVisibility("#data-transformation", false);
    toggleElementVisibility("#query-dashboard", true);
}

function showTransformationTab() {
    toggleElementVisibility("#description", false);
    toggleElementVisibility("#query-dashboard", false);
    toggleElementVisibility("#data-transformation", true);
}

function handleCloseModalClick() {
    const modalElement = document.getElementById("query-result-modal-background");
    modalElement.classList.remove("display-flex");
    modalElement.classList.add("display-none");

    queryResultTable.destroy();
}

function showModal() {
    const modalElement = document.getElementById("query-result-modal-background");
    modalElement.classList.remove("display-none");
    modalElement.classList.add("display-flex");
}

function getDataSetValues(dataTransformationId, dataSetSchemaId, attributesSelected, conditions) {
    const dataflowId = pathnameURL.split("/")[2];
    const url = `${originURL}/api/v1/dataflows/${dataflowId}/transformations/${dataTransformationId}/data-set-schema/${dataSetSchemaId}/data-set?attributes=${attributesSelected}&conditions=${conditions}`;

    return fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            const tbody = document.querySelector("#query-result-modal-container .query-result-modal-body table tbody");
            const thead = document.querySelector("#query-result-modal-container .query-result-modal-body table thead");

            tbody.innerHTML = "";
            thead.innerHTML = "";

            const headerRow = document.createElement("tr");

            for (const key in data[0]) {
                if (data[0].hasOwnProperty(key)) {
                    const headerCell = document.createElement("th");
                    headerCell.textContent = key;
                    headerRow.appendChild(headerCell);
                }
            }

            thead.appendChild(headerRow);

            data.forEach(item => {
                const row = document.createElement("tr");

                for (const key in item) {
                    if (item.hasOwnProperty(key)) {
                        const cell = document.createElement("td");
                        cell.textContent = item[key];
                        row.appendChild(cell);
                    }
                }

                tbody.appendChild(row);
            });

            queryResultTable = new DataTable('#query-result-table',  {
                info: false,
                ordering: true,
                retrieve: true,
                paging: false
            });

            showModal();
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function createListItem(label, value) {
    const listItem = document.createElement("li");
    const strong = document.createElement("strong");
    strong.innerText = label + ": ";
    listItem.appendChild(strong);
    listItem.appendChild(document.createTextNode(value));
    return listItem;
}

function getDataTransformationValues() {
    const dataflowId = pathnameURL.split("/")[2];
    const url = `${originURL}/api/v1/dataflows/${dataflowId}/transformations/${getClickedEdgeId()}`;

    return fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            const values = document.querySelector("#data-transformation .values-list");

            values.innerHTML = "";

            innerHTML = "";

            values.appendChild(createListItem("Executed by", data.executedBy ? data.executedBy : "-"));
            values.appendChild(createListItem("Started at", data.startedAt ? data.startedAt : "-"));
            values.appendChild(createListItem("Finished at", data.finishedAt ? data.finishedAt : "-"));
            values.appendChild(createListItem("Number of input tuples", data.numberOfInputTuples ? data.numberOfInputTuples : "-"));
            values.appendChild(createListItem("Number of output tuples", data.numberOfOutputTuples ? data.numberOfOutputTuples : "-"));

        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function handleRunDataSetQueryButtonClick() {
    const attributesElements = document.querySelectorAll("#container .aside-content #query-dashboard .attributes ul li");
    var attributesSelected = [];

    const conditions = document.querySelector("#container .aside-content #query-dashboard textarea").value;

    attributesElements.forEach(attributeElement => {
        var checkbox = attributeElement.querySelector("input[type='checkbox']");
        if (checkbox.checked) {
            attributesSelected.push(checkbox.id);
        }
    });

    if (attributesSelected.length === 0) {
        alert("Please select at least one attribute.");
    } else {
        getDataSetValues(getClickedEdgeId(), getClickedNodeId(), attributesSelected, conditions);
    }
}

function exportToCSV() {
    const rows = document.querySelectorAll('table#query-result-table tr'),
          link = document.createElement('a');

    link.download = 'dataset.csv';
    link.href = URL.createObjectURL(
        new Blob(
            [
                Array.from(rows).map(row => {
                    return Array.from(row.querySelectorAll('td, th')).map(col => col.innerText).join(',');
                }).join('\n')
            ],
            { type: 'text/plain' }
        )
    );
    link.click();
}