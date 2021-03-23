#! /usr/local/bin/python3
import flask
import mysql.connector
import sys
import json

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
    else:
        return "Invalid request."

def register_user(msg_received):
    username = msg_received["username"]
    email	 = msg_received["email"]
    password = msg_received["password"]

    select_query = "SELECT * FROM Users_profile where username = " + "'" + username + "'"
    db_cursor.execute(select_query)
    records = db_cursor.fetchall()
    
    if len(records) != 0:
        return ("username")

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
        
# mysql-connector connection to database        
try:
    chat_db = mysql.connector.connect(host="localhost", user="root", passwd="", database="MyGrades_DB")
except:
    sys.exit("Error connecting to the database. Please check your inputs.")

# cursor return from db
db_cursor = chat_db.cursor()

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000, debug=True, threaded=True)
 
