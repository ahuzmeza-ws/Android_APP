
from config import *

import mysql.connector
import sys

print("Connecting to:")
print("Host:"+HOST + "\nUser: " + USR + "\nPassword: " + PWD +"\nPort: " + PORT +'\n');

try:
    MyGrades_db =  mysql.connector.connect(host=HOST, user=USR, passwd=PWD, database=DATABASE_NAME)
except:
    sys.exit("Error connecting to the database. Please check your inputs.")

db_cursor = MyGrades_db.cursor()

# Users Table
try:
    db_cursor.execute("""
    	CREATE TABLE Users_profile (
		    id          INT              NOT NULL AUTO_INCREMENT PRIMARY KEY,
		    username    varchar(100)     NOT NULL UNIQUE,
		    email       varchar(255)     NOT NULL,
		    password    text             NOT NULL,

		    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
		);
    """);

    print("users table created successfully.")

except mysql.connector.DatabaseError:
    sys.exit("Error creating the table. It may already exist.")


describe_query = "DESCRIBE Users_profile"
db_cursor.execute(describe_query)
records = db_cursor.fetchall()
print(records)


