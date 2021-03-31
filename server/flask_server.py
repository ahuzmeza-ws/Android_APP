#! /usr/local/bin/python3
import flask
import mysql.connector
import sys
import json
from flask import jsonify

app = flask.Flask(__name__)

@app.route('/', methods = ['GET', 'POST'])
# chat between APP and Flask using .json object.
# .Json recieved: - subject
#				  - body
def chat():
    msg_received = flask.request.get_json()
    msg_subject = msg_received["subject"]

    if msg_subject == "register":
        return register_user(msg_received)
    elif msg_subject == "login":
        return login(msg_received)
    elif msg_subject == "insert_subject":
        return insertSubject(msg_received)
    elif msg_subject == "get_all_subjects":
        return getAllSubjectsForUser(msg_received)
    else:
        return "Invalid request."

def register_user(msg_received):
    username = msg_received["username"]
    email	 = msg_received["email"]
    password = msg_received["password"]

	# check if a User with recieved username has already been registered
    select_query = "SELECT * FROM Users_profile where username = " + "'" + username + "'"
    db_cursor.execute(select_query)
    records = db_cursor.fetchall()
    if len(records) != 0:
        return ("username_already_exists")

	# should check if password is secure
	# (...)
	
	# if recieved fields are valid -> insert user
    insert_query = "INSERT INTO Users_profile (username, email, password) VALUES (%s, %s, MD5(%s))"
    insert_values = (username, email, password)
    try:
        db_cursor.execute(insert_query, insert_values)
        chat_db.commit()
        return "success"
    except Exception as e:
        print("Error while inserting the new record: ", repr(e))
        return "failure"


def login(msg_received):
    username = msg_received["username"]
    password = msg_received["password"]

    select_query = "SELECT username FROM Users_profile where username = " + "'" + username + "' and password = " + "MD5('" + password + "')"
    db_cursor.execute(select_query)
    records = db_cursor.fetchall()

    if len(records) == 0:
        return "failure"
    else:
        return "success"
        
        
def insertSubject(msg_received):
    username = msg_received["usr_username"]
    subj_name = msg_received["name"]

	# if recieved fields are valid -> insert Subject
    insert_query = "INSERT INTO Subjects (usr_username, name) VALUES (%s, %s)"
    insert_values = (username, subj_name)
    try:
        db_cursor.execute(insert_query, insert_values)
        chat_db.commit()
        return "success"
    except Exception as e:
        print("Error while inserting the new record: ", repr(e))
        return "failure"

def getAllSubjectsForUser(msg_received):
    usr_username = msg_received["usr_username"]
    
    select_query = "SELECT name FROM Subjects WHERE usr_username = " + "'" + usr_username + "'"
    db_cursor.execute(select_query)
    records = db_cursor.fetchall()
       
    print(records)
    
    if len(records) != 0:
        return  jsonify(records)
    # else no Subjects were found for user
    return ("no_subject_entries");

# mysql-connector connection to database        
try:
    chat_db = mysql.connector.connect(host="localhost", user="root", passwd="", database="MyGrades_DB")
except:
    sys.exit("Error connecting to the database. Please check your inputs.")

# cursor return from db
db_cursor = chat_db.cursor(dictionary=True)

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000, debug=True, threaded=True)
 

