# **************************************************************************** #
#                                                                              #
#                                                                              #
#    createMyGrade    										╦ ╦╔╦╗╔═╗╔═╗╔╦╗    #
#                             								║ ║║║║╠╣ ╚═╗ ║     #
#    By: alinhuzmezan            							╚═╝╩ ╩╚  ╚═╝ ╩     #
#                                                        George Emil Palade    #
#    Created: 2021/03/09 17:59 by alinhuzmezan                                 #
#    Updated: 2021/03/09 20:14 by alinhuzmezan                                 #
#                                                                              #
# **************************************************************************** #

from config import *

import mysql.connector
import sys

print("Connecting to:")
print("Host:"+HOST + "\nUser: " + USR + "\nPassword: " + PWD +"\nPort: " + PORT +'\n');

try:
    db = mysql.connector.connect(host=HOST, user=USR, passwd=PWD) 
except:
    sys.exit("Error connecting to the host. Please check your inputs.")

db_cursor = db.cursor()


stm = "CREATE DATABASE " + DATABASE_NAME;
try:
	db_cursor.execute(operation = stm) 
except mysql.connector.DatabaseError:
    sys.exit("Error creating "+DATABASE_NAME+". Check if database already exists!")

db_cursor.execute("SHOW DATABASES")
databases = db_cursor.fetchall()
print(databases)
