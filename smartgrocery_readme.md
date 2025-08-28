# SmartGrocery - Grocery Management System

A JavaFX-based grocery management application that helps users track grocery items with expiry alerts and role-based access control.

## 📋 Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Installation](#installation)
- [Usage](#usage)
- [Database Schema](#database-schema)
- [Project Structure](#project-structure)
- [Screenshots](#screenshots)
- [Contributing](#contributing)
- [Authors](#authors)

## 🎯 Overview

SmartGrocery is a comprehensive grocery inventory management system designed to help users efficiently track their grocery items with automatic expiry date monitoring. Built with the Model-View-Controller (MVC) architecture, the application provides role-based access control and secure user authentication.

### Key Highlights
- **Expiry Alerts**: Automatic status calculation (Fresh, Expiring Soon, Expired)
- **Role-Based Access**: Admin and User roles with different permissions
- **Secure Authentication**: SHA-256 password hashing
- **Real-time Updates**: Dynamic status and days-left calculations
- **User Management**: Admin capabilities for managing users and all items

## ✨ Features

### User Management
- **Secure Registration & Login**: SHA-256 hashed password storage
- **Role-Based Access Control**: Admin and User roles
- **User Dashboard**: Personalized grocery item management

### Grocery Item Management
- **Add Items**: Insert new grocery items with name, quantity, and expiry date
- **Edit Items**: Modify existing grocery item details
- **Delete Items**: Remove items (Admin only)
- **Status Tracking**: Automatic categorization:
  - 🟢 **Fresh**: More than 3 days until expiry
  - 🟡 **Expiring Soon**: Within 3 days of expiry
  - 🔴 **Expired**: Past expiry date
- **Days Left Calculation**: Real-time countdown to expiry

### Admin Features
- **View All Items**: Access to all users' grocery items
- **User Management**: View and delete users (except self)
- **System Administration**: Complete control over the application

### User Features
- **Personal Dashboard**: View only your own grocery items
- **Item Management**: Add and edit your own items
- **Expiry Notifications**: Visual alerts for item status

## 🛠 Technology Stack

- **Frontend**: JavaFX with custom CSS styling
- **Backend**: Java with JDBC
- **Database**: MySQL
- **IDE**: Eclipse
- **Architecture**: Model-View-Controller (MVC)
- **Security**: SHA-256 password hashing

## 🚀 Installation

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- JavaFX SDK
- MySQL Server
- Eclipse IDE (recommended)

### Database Setup
1. Create a MySQL database with the following schema:
```sql
CREATE DATABASE smartgrocery;
USE smartgrocery;

-- Users table
CREATE TABLE users_sm (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'user'
);

-- Items table
CREATE TABLE items_sm (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    expiry_date DATE NOT NULL,
    added_by VARCHAR(50) NOT NULL,
    FOREIGN KEY (added_by) REFERENCES users_sm(username)
);
```

2. Update database connection settings in `DBConnection.java`

### Application Setup
1. Clone the repository:
```bash
git clone https://github.com/Shritin15/Smart.git
cd Smart
```

2. Import the project into Eclipse IDE

3. Add JavaFX SDK to your module path

4. Run `Main.java` to start the application

### Running the JAR File
```bash
java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -jar SmartGrocery.jar
```

## 📖 Usage

### Getting Started
1. **Run the Application**: Execute `Main.java` in Eclipse
2. **Login**: Use existing credentials or create a new account

### Default Admin Credentials
- **Username**: `shritin` | **Password**: `admin123`
- **Username**: `sarvesh` | **Password**: `admin123`

### Example User Credentials
- **Username**: `arya` | **Password**: `arya123`

### Creating New Users
1. Click "Sign Up" on the login screen
2. Enter username, password, and confirm password
3. New users are assigned the 'user' role by default

### Managing Grocery Items
1. **Add Item**: Fill in item name, quantity, and expiry date, then click "Insert"
2. **Edit Item**: Select an item from the table, modify details, and click "Edit"
3. **Delete Item**: Select an item and click "Delete" (Admin only)
4. **View Status**: Monitor item freshness with color-coded status indicators

### Admin Functions
- **Manage Users**: Click "Manage Users" to view and delete users
- **View All Items**: See grocery items from all users
- **System Control**: Full administrative access to all features

## 🗃 Database Schema

### users_sm Table
| Field    | Type         | Description                    |
|----------|--------------|--------------------------------|
| id       | INT          | Primary key (auto-increment)  |
| username | VARCHAR(50)  | Unique username                |
| password | VARCHAR(255) | SHA-256 hashed password        |
| role     | VARCHAR(20)  | User role (admin/user)         |

### items_sm Table
| Field       | Type         | Description                    |
|-------------|--------------|--------------------------------|
| id          | INT          | Primary key (auto-increment)  |
| name        | VARCHAR(100) | Grocery item name              |
| quantity    | INT          | Item quantity                  |
| expiry_date | DATE         | Item expiry date               |
| added_by    | VARCHAR(50)  | Username of item creator       |

## 📁 Project Structure

```
SmartGrocery/
├── src/
│   ├── model/
│   │   ├── GroceryItems.java
│   │   ├── User.java
│   │   └── DBConnection.java
│   ├── view/
│   │   ├── LoginController.java
│   │   ├── SignUpController.java
│   │   ├── MainController.java
│   │   └── UserManagementController.java
│   ├── controller/
│   │   └── [Controller classes]
│   ├── utils/
│   │   ├── PasswordHashingUtils.java
│   │   ├── PasswordTest.java
│   │   └── DBTest.java
│   └── Main.java
├── resources/
│   ├── style.css
│   └── [FXML files]
└── README.md
```

## 🎨 Features Showcase

### Security Features
- **Password Hashing**: All passwords are stored using SHA-256 hashing
- **Role-Based Security**: Different access levels for admin and regular users
- **Input Validation**: Form validation and error handling

### User Experience
- **Custom CSS Styling**: Professional UI with consistent design
- **Real-time Updates**: Dynamic status calculations and table updates
- **Intuitive Interface**: User-friendly design with clear navigation

### Extra Features
- **Runnable JAR**: Pre-compiled executable for easy deployment
- **Comprehensive Testing**: Database connection and password hashing tests
- **Visual Feedback**: Color-coded status indicators and user notifications

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Notes
- Follow MVC architecture patterns
- Maintain consistent code styling
- Add appropriate error handling
- Update documentation for new features

## 👥 Authors

- **Shritin Shetty** - Lead Developer
- **Sarvesh Saranyan** - Co-Developer

*ITMD-510 Final Project*

## 📄 License

This project is part of an academic assignment for ITMD-510.

## 🔗 Repository

**Private Repository**: [https://github.com/Shritin15/Smart](https://github.com/Shritin15/Smart)

*Note: This is a private repository. Please contact the authors for access.*

---

### 📞 Support

For questions or support, please contact the development team or refer to the project documentation.