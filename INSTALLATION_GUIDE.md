
![Java](https://img.shields.io/badge/Java-11+-blue?logo=java)
![JavaFX](https://img.shields.io/badge/JavaFX-21-blue?logo=openjdk)
![Maven](https://img.shields.io/badge/Maven-3.6+-blue?logo=apache-maven)



# Installation & Setup Guide

## Prerequisites

Before installing the Hotel Management System, ensure you have the following installed:

### System Requirements
- **Operating System:** Windows 10/11, macOS 10.14+, or Linux (Ubuntu 18.04+)
- **RAM:** 2GB minimum (4GB recommended)
- **Disk Space:** 500MB for JDK + 200MB for project

### Required Software

#### 1. Java Development Kit (JDK)
- **Version:** Java 21 or higher (required for JavaFX support)
- **Official Download:** [oracle.com/java/technologies/downloads](https://www.oracle.com/java/technologies/downloads/)



## Installation:

**Windows:**

```bash
Download JDK installer and run
Add to PATH:
1. Right-click "This PC" → Properties
2. Click "Advanced system settings"
3. Click "Environment Variables"
4. Click "New" under System variables
5.   Variable name: JAVA_HOME
6.    Variable value: C:\Program Files\Java\jdk-21 (your JDK path)
7. Edit PATH variable, add: %JAVA_HOME%\bin
```

**macOS/Linux:**
```bash
# Verify Java installation
java -version

# If not installed, use package manager:
# macOS (Homebrew):
brew install openjdk@21

# Linux (Ubuntu):
sudo apt-get update
sudo apt-get install openjdk-21-jdk
```

#### 2. Apache Maven
- **Version:** 3.6.0 or higher
- **Official Download:** [maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)

**Installation:**

**Windows:**
```bash
1. Download binary zip archive
2. Extract to: C:\Program Files\maven
3. Add to PATH:
4.    Variable name: MAVEN_HOME
5.    Variable value: C:\Program Files\maven
6. Edit PATH variable, add: %MAVEN_HOME%\bin
7. Verify installation:
mvn -version
```

**macOS:**
```bash
# Using Homebrew:
brew install maven

# Verify:
mvn -version
```

**Linux:**
```bash
# Download and extract:
wget https://archive.apache.org/dist/maven/maven-3/3.8.1/binaries/apache-maven-3.8.1-bin.tar.gz
tar -xzf apache-maven-3.8.1-bin.tar.gz
sudo mv apache-maven-3.8.1 /opt/maven

# Add to PATH (add to ~/.bashrc or ~/.zshrc):
export MAVEN_HOME=/opt/maven
export PATH=$MAVEN_HOME/bin:$PATH

# Apply changes:
source ~/.bashrc

# Verify:
mvn -version
```

#### 3. Git (Optional - for cloning repository)
- **Official Download:** [git-scm.com](https://git-scm.com/downloads)

## Step-by-Step Installation

### Option 1: Clone from GitHub

#### 1. Navigate to desired location
```bash
cd ~/Projects  # or your preferred directory
```

#### 2. Clone the repository
```bash
git clone https://github.com/DeonLondn/hotelmanagementsystem.git
cd hotelmanagementsystem
```

#### 3. Verify project structure
```bash
ls -la  # macOS/Linux
dir     # Windows
```


### Option 2: Download ZIP Archive

#### 1. Download from GitHub
- Go to repository page
- Click "Code" → "Download ZIP"

#### 2. Extract the archive
```bash
unzip hotelmanagementsystem-main.zip
cd hotelmanagementsystem-main
```

## Building the Project

### Step 1: Verify Maven Installation
```bash
mvn --version
```

Expected output:
```
Apache Maven 3.8.1
Maven home: /path/to/maven
Java version: 21.0.1
OS name: windows / mac / linux
```

### Step 2: Clean Previous Builds
```bash
mvn clean
```

### Step 3: Build Project
```bash
mvn clean install
```
This command will:
- Download all dependencies from Maven Central Repository
- Compile source code
- Run unit tests
- Package the application


### Step 4: Verify Build Success
Look for output:
```
[INFO] BUILD SUCCESS
[INFO] Total time: X.XXs
```

**If build fails:**
```bash
# Clear Maven cache:
rm -rf ~/.m2/repository  # macOS/Linux
rmdir %USERPROFILE%\.m2\repository /s  # Windows

# Rebuild:
mvn clean install
```



### Method 4: Run from IDE
**IntelliJ IDEA:**
1. Open project
2. Right-click `HotelManagementGUI.java` or `HotelManagementCLI.java`
3. Select "Run 'HotelManagementGUI.main()'" or "Run 'HotelManagementCLI.main()'"

**Eclipse:**
1. Open project
2. Navigate to HotelManagementGUI.java or HotelManagementCLI.java
3. Right-click → "Run As" → "Java Application"

**VS Code:**
1. Install "Extension Pack for Java"
2. Open HotelManagementGUI.java or HotelManagementCLI.java
3. Click "Run" button above main() method

## First Time Setup

### Initial Application Launch
```
========== HOTEL RESERVATION SYSTEM ==========
1. Guest Portal
2. Admin Dashboard
3. Exit

Enter your choice: 
```

### Test Guest Portal
```
Enter your choice: 1

========== GUEST PORTAL ==========
1. Search Available Rooms
2. View Bookings
3. Check-in
4. Check-out
5. Back to Main Menu

Enter your choice: 1
Enter check-in date (YYYY-MM-DD): 2025-06-01
Enter check-out date (YYYY-MM-DD): 2025-06-05
Enter room type (STANDARD/DELUXE/EXECUTIVE_SUITE/VIP_SUITE/VVIP_PENTHOUSE_SUITE): DELUXE
```

### Test Admin Dashboard
```
Enter your choice: 2

========== ADMIN LOGIN ==========
Enter username: admin
Enter password: admin123

You are logged in as: System Administrator

========== ADMIN DASHBOARD ==========
1. Staff Management
2. Room Management
3. Financial Reports
4. Housekeeping
5. Inventory Management
6. Room Rate Management
7. Logout
```

## Troubleshooting

### Error: "mvn: command not found"
**Solution:**
- Maven not in PATH
- Run: `echo $PATH` (macOS/Linux) or `echo %PATH%` (Windows)
- Reinstall Maven and add to system PATH

### Error: "java: command not found"
**Solution:**
- Java not in PATH
- Verify: `java -version`
- Add JDK bin directory to PATH

### Error: "BUILD FAILURE - Could not find artifact"
**Solution:**
```bash
# Clear Maven cache and rebuild:
rm -rf ~/.m2/repository
mvn clean install
```

### Error: "No main manifest attribute"
**Solution:**
- Use Maven Exec Plugin method:
```bash
mvn exec:java -Dexec.mainClass="com.lantel.ui.HotelManagementGUI"
```

Or for CLI version:
```bash
mvn exec:java -Dexec.mainClass="com.lantel.ui.HotelManagementCLI"
```

### Application starts but crashes on menu input
**Solution:**
- Check Java version compatibility
- Verify all dependencies installed: `mvn dependency:tree`
- Rebuild: `mvn clean install -X` (debug mode)


## Development Environment Setup

### Code Style & Formatting
```bash
# Format code to standard:
# Using Maven Compiler Plugin (built-in)
mvn clean compile

# Or use IDE formatting:
# IntelliJ: Ctrl+Alt+L
# Eclipse: Ctrl+Shift+F
# VS Code: Shift+Alt+F
```

### Running Tests
```bash
# Run all tests:
mvn test

# Run specific test:
mvn test -Dtest=StaffManagerTest

# Run with coverage (if configured):
mvn clean test jacoco:report
```

### Running Application with Debug Mode
```bash
# Maven debug mode (GUI):
mvn exec:java -Dexec.mainClass="com.lantel.ui.HotelManagementGUI" -e -X

# Java debug mode (GUI):
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005 \
     -cp target/hotelmanagementsystem-1.0-SNAPSHOT.jar com.lantel.ui.HotelManagementGUI

# Debug CLI version:
mvn exec:java -Dexec.mainClass="com.lantel.ui.HotelManagementCLI" -e -X
```

## Next Steps

1. **Explore the Code** - Read through main classes in `src/main/java`
2. **Review ARCHITECTURE.md** - Understand system design
3. **Run Tests** - Execute: `mvn test`
4. **Modify & Extend** - Add features or customize
5. **Deploy** - Prepare for production

## Getting Help

If you encounter issues:

1. **Check Build Output**
```bash
mvn clean install -X  # Verbose output
```

2. **Check Maven Dependencies**
```bash
mvn dependency:tree
```

3. **Check Java Version**
```bash
java -version
javac -version
```

4. **Review Project Logs**
- Check `target/` directory for error logs

5. **Search Issues**
- GitHub Issues page
- Stack Overflow with tags: `java`, `maven`, `hotel-management`

## Uninstalling

To remove the application:

```bash
# Remove project directory:
rm -rf ~/Projects/hotelmanagementsystem  # macOS/Linux
rmdir /s C:\Users\YourUsername\Projects\hotelmanagementsystem  # Windows

# Optional: Remove Maven cache:
rm -rf ~/.m2/repository
```




