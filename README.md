# meeting-scheduler-app
This is an app for scheduling meetings.

**Title:** Scheduling App

**Description:**
The purpose of this app is to provide a meeting scheduling system. It allows the user to schedule, update, and cancel meetings. 
The user has the ability to view upcoming meetings for the week or month, as well as all scheduled meetings. The app also allows 
the user to add, update, and delete employees. Reports are able to be generated that show either employee appointment totals, 
employees by Country, or employee schedules.

This project was my Capstone project for my BS in Software Developement. 

Author: Melissa Epperson
Capstone version 1
Date: 7/27/2021

IntelliJ IDEA Community 2020.2
Java Version 11.0.9
Java SDK Version 11
MySQL Connector driver: mysql-connector-java:5.1.48

To run the program, login using the following:

Username: test
Password: test

Once you successfully log in, you will be taken to the meeting scheduler screen.


**Login Screen**
The login screen shows what area the user is from. When logging in, an alert will display if the user has any upcoming appointments scheduled within 15 minutes of the time they are logging on. A log is also kept of all login atempts, whether successful or not.

<img width="444" alt="Login screenshot" src="https://user-images.githubusercontent.com/25159870/150700988-8838db29-9bc6-4192-88ec-b635fbcb73dd.jpg"><img width="446" alt="Login alert screenshot" src="https://user-images.githubusercontent.com/25159870/150700963-7482aae2-741a-4865-ba3a-1014061f7254.jpg">

**Schedule Screen**
This is the first screen you see upon logging on. It lists all of the scheduled meetings. From this screen, you can add, cancel, or modify meetings. You can sort meetings by week, month, or show all. You can also access the employee screen as well as reports screen from here.

<img width="706" alt="Schedule Screenshot" src="https://user-images.githubusercontent.com/25159870/150701027-c0089e8d-fc4f-49d5-b9bf-4915b94bc1cc.jpg">

**Add Meeting Screen**
This screen allows you to create a new appointment.It will alert you if you are scheduling an appointment outside of business hours. It will also alert you if the selected contact has a conflicting appointment already scheduled. 

<img width="598" alt="Add Appointment Screenshot" src="https://user-images.githubusercontent.com/25159870/150700900-3a342313-8e8b-4d51-b2f5-bd9f46e96d03.jpg">

**Update Meeting Screen**
This screen is where you can update existing appointments. It auto-populates with the current meeting info. It will alert you if you are scheduling an appointment outside of business hours. It will also alert you if the selected contact has a conflicting appointment already scheduled. 

<img width="601" alt="Update Meeting screenshot" src="https://user-images.githubusercontent.com/25159870/150701070-7743b35f-e411-4837-80e1-697196bd59c3.jpg">

**Employee Screen**
This screen lists all of the employees and their information. From this screen, you can add employees, modify employees or delete them. You also can access the schedule screen as well as reports screen from here.

<img width="1410" alt="Employee Screenshot" src="https://user-images.githubusercontent.com/25159870/150701099-34acc7ea-0b66-47bd-b29c-267ccd24cad5.jpg">

**Add Employee**
This screen allows you to add an employee. The State combo box is populated with options based on the country selected. 

<img width="598" alt="Add Employee screenshot" src="https://user-images.githubusercontent.com/25159870/150701135-860760ca-1dde-4b5c-a18d-443f0ae111b4.jpg">

**Update Employee**
This allows you to make changes to an existing employee. It auto-populates with the current employee info.

<img width="599" alt="Update Employee" src="https://user-images.githubusercontent.com/25159870/150701151-83d345f6-ce65-4ddd-9f9b-b776510fd6c9.jpg">

**Reports Screen**
This screen allows you to run reports. The report options are:
Employee Meeting Totals - shows total number of scheduled appointments per employee
Employees by Country - shows the number of employees by country and divison
Contact Schedules - This shows the scheduled appointments per contact.

<img width="1407" alt="Reports Screenshot" src="https://user-images.githubusercontent.com/25159870/150700919-d8842446-35c4-441a-8531-63c1b703ddbc.jpg">

