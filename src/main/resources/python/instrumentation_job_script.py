import cx_Oracle
import requests
import sys
import getpass
from datetime import datetime

# UPDATE QUERIES TO SUMMARIZE DATA
PREJOB_QUERY = """
    ...
"""

POSTJOB_QUERY = """
    ...
"""

# UPDATE IDS
INPUT_DATASET_ENDPOINT = "..."
OUTPUT_DATASET_ENDPOINT = "..."
TRANSFORMATION_ENDPOINT = "..."

# UPDATE CONNECTION VALUES
DB_HOST = "..."
DB_PORT = "..."
DB_SID = "..."
DB_USER = "..."
DB_PASSWORD = "..."

def connect_to_database():
    cx_Oracle.init_oracle_client(lib_dir=r"C:\\oracle\\instantclient_21_12")

    dsn = cx_Oracle.makedsn(DB_HOST, DB_PORT, DB_SID)
    conn = cx_Oracle.connect(DB_USER, password=DB_PASSWORD, dsn=dsn)
    return conn

def query_prejob_db(conn):
    cursor = conn.cursor()
    cursor.execute(PREJOB_QUERY)
    data = cursor.fetchall()
    cursor.close()
    return data

def query_postjob_db(conn):
    cursor = conn.cursor()
    cursor.execute(POSTJOB_QUERY)
    data = cursor.fetchall()
    cursor.close()
    return data

def send_data_to_endpoint(data, method, endpoint):
    if method.upper() == 'POST':
        response = requests.post(endpoint, json=data)
    elif method.upper() == 'PUT':
        response = requests.put(endpoint, json=data)

    if not response.status_code == 200:
        print("Failed to send data to", endpoint)
        print("Error:", response.text)

def prejob_process(conn):
    current_time = datetime.now().strftime("%Y-%m-%dT%H:%M:%S.%f")
    current_username = getpass.getuser()
    prejob_data = query_prejob_db(conn)

    json_data = {
        "executedBy": current_username,
        "startedAt": current_time,
        "numberOfInputTuples": len(prejob_data)
    }

    send_data_to_endpoint(json_data, 'PUT', TRANSFORMATION_ENDPOINT)

    for line in prejob_data:
        column_name, data_value, count = line

        json_data = {
            "column_name": column_name,
            "data_value": data_value,
            "count": count
        }

        send_data_to_endpoint(json_data, 'POST', INPUT_DATASET_ENDPOINT)

def postjob_process(conn):
    current_time = datetime.now().strftime("%Y-%m-%dT%H:%M:%S.%f")
    postjob_data = query_postjob_db(conn)

    json_data = {
        "finishedAt": current_time,
        "numberOfOutputTuples": len(postjob_data)
    }

    send_data_to_endpoint(json_data, 'PUT', TRANSFORMATION_ENDPOINT)

    for line in postjob_data:
        column_name, data_value, count = line

        json_data = {
            "column_name": column_name,
            "data_value": data_value,
            "count": count
        }

        send_data_to_endpoint(json_data, 'POST', OUTPUT_DATASET_ENDPOINT)

def main():
    if len(sys.argv) != 2:
        print("Usage: python script.py <PREJOB or POSTJOB>")
        return

    arg = sys.argv[1]

    conn = connect_to_database()

    if arg == "PREJOB":
        prejob_process(conn)

    elif arg == "POSTJOB":
        postjob_process(conn)
    else:
        print("Invalid argument. Please use PREJOB or POSTJOB.")

    conn.close()

if __name__ == "__main__":
    main()
