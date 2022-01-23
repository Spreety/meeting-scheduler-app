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

<img width="444" alt="Login screenshot" src="https://user-images.githubusercontent.com/25159870/150700235-9ab33ead-9730-4640-8fa6-f1944479e7d9.png"><img width="446" alt="Upcoming Appointments screenshot" src="https://user-images.githubusercontent.com/25159870/150700236-60a211f2-0dbc-4f7d-b43f-161744515ca7.png">

**Schedule Screen**
This is the first screen you see upon logging on. It lists all of the scheduled meetings. From this screen, you can add, cancel, or modify meetings. You can sort meetings by week, month, or show all. You can also access the employee screen as well as reports screen from here.

<img width="706" alt="Schedule Screenshot" src="https://user-images.githubusercontent.com/25159870/150700336-d218202c-694b-4a9e-b854-830ec328f57a.png">

**Add Meeting Screen**
This screen allows you to create a new appointment.It will alert you if you are scheduling an appointment outside of business hours. It will also alert you if the selected contact has a conflicting appointment already scheduled. 

<img width="598" alt="Add Appointment Screenshot" src="https://user-images.githubusercontent.com/25159870/150700422-c6acdfa1-13bc-4799-ad17-2590f0841a22.png">

**Update Meeting Screen**
This screen is where you can update existing appointments. It auto-populates with the current meeting info. It will alert you if you are scheduling an appointment outside of business hours. It will also alert you if the selected contact has a conflicting appointment already scheduled. 

<img width="601" alt="Update Meeting screenshot" src="https://user-images.githubusercontent.com/25159870/150700453-3d9a452d-378d-425f-84ed-d4b31e9c97b6.png">

**Employee Screen**
This screen lists all of the employees and their information. From this screen, you can add employees, modify employees or delete them. You also can access the schedule screen as well as reports screen from here.

<img width="1410" alt="Employee Screenshot" src="https://user-images.githubusercontent.com/25159870/150700562-a5956fd3-9e31-4dd2-b894-1a70c49d408e.png">

**Add Employee**
This screen allows you to add an employee. The State combo box is populated with options based on the country selected. 

<img width="598" alt="Add Employee screenshot" src="https://user-images.githubusercontent.com/25159870/150700590-26671545-63b3-464c-8a8c-a2ef707a7c3b.png">

**Update Employee**
This allows you to make changes to an existing employee. It auto-populates with the current employee info.

<img width="599" alt="Update Employee" src="https://user-images.githubusercontent.com/25159870/150700683-2100a87c-2f43-4c77-947f-774010f8de3a.png">

**Reports Screen**
This screen allows you to run reports. The report options are:
Employee Meeting Totals - shows total number of scheduled appointments per employee
Employees by Country - shows the number of employees by country and divison
Contact Schedules - This shows the scheduled appointments per contact.

<img width="1407" alt="Reports Screen" src="https://user-images.githubusercontent.com/25159870/150700778-bb42e49d-947a-4ffc-9abc-2da73be28207.png">
