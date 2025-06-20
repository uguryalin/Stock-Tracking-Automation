Stock Tracking System - Java Swing App
Project Overview
This project is a desktop-based inventory management system developed using Java and Swing. It is designed as part of the Software Engineering coursework at Fırat University, specifically for small-scale businesses and local store owners. It allows the user to add, edit, delete, and track products, all stored locally using a plain text file.

System Description
Problem Definition
Small businesses often lack a structured and easy-to-use system to manage product inventories. Manual tracking can lead to errors, lost data, and inefficiencies. The goal of this project is to provide a lightweight yet functional stock tracking system that doesn't require any database installation or internet connection.

Main Features
Secure user login system

Add/edit/delete product records

View current product stocks

Text-based data storage (no database required)

Intuitive and simple GUI built with Java Swing

Technologies Used
Component	Technology
Programming Language	Java
GUI Library	Java Swing
Data Storage	Plain text file (TXT)
Development IDE	NetBeans IDE
Diagram Tools	Visio (for UML, ERD)

Installation
Prerequisites
Java JDK 8 or above

Any Java IDE (e.g., NetBeans, IntelliJ IDEA)

Windows OS (recommended)

Java Runtime Environment (JRE) must be installed

Setup Instructions
Clone the repository:

git clone https://github.com/uguryalin/stock-tracking-system.git
cd stock-tracking-system
Open the project in your Java IDE.

Compile and run the Main.java file.

Usage Guide
Login: Enter valid username and password (stored in a text file).

View Products: See a list of all inventory items.

Add Product: Input product name, price, and quantity.

Edit Product: Modify the selected product's information.

Delete Product: Remove a product from the inventory.

Logout: Saves data and exits the session.

Example Inputs
Field	Example Value
Username	admin
Password	1234
Product Name	Wireless Mouse
Price	250.00
Quantity	35

Implementation Details
File Structure
Main.java → Entry point for the application

LoginPanel.java → Handles user authentication

ProductManager.java → Contains logic for add/edit/delete operations

Product.java → Product model with fields (id, name, price, quantity)

stok.txt → Data file storing products

users.txt → Stores valid usernames and passwords

Sample Data Format (stok.txt)
101,Wireless Mouse,250.0,35
102,Laptop Stand,320.5,20
Security & Constraints
No external libraries or databases used.

Only authorized users can access the system.

Data is stored in plain text (basic security).

Input fields are validated (e.g., price must be numeric).

Only one user session allowed at a time.

Testing
The system has been manually tested under the following scenarios:

Valid and invalid login attempts

Adding products with missing fields

Editing a non-selected product

Deleting a product that does not exist

Displaying product list when empty

Limitations and Future Work
Current Limitations
No encryption for login credentials

No backup or recovery system

Not scalable for enterprise use

Only one user at a time (no concurrency)

Future Improvements
Add password encryption (e.g., hash storage)

Use SQLite or JSON for structured data storage

Improve UI with JavaFX or migrate to web

Implement report generation (PDF/CSV)

UML & Diagrams
Use /docs/uml/ folder in your repo to include:

Use Case Diagram

Class Diagram

ERD (simulated)

Sequence Diagrams

Authors
Uğur Yalın – 230543014

Ömer Faruk Kavuğubüyük – 230543015

GitHub: https://github.com/uguryalin

Acknowledgements
We would like to thank our instructor and Fırat University Software Engineering Department for their support and feedback during the project.

This project is developed as part of the Principle of Software Engineering course at Fırat University, Technology Faculty.
