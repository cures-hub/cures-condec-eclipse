# cures-condec-eclipse

[![Build Status](https://travis-ci.org/cures-hub/cures-condec-eclipse.svg?branch=master)](https://travis-ci.org/cures-hub/cures-condec-eclipse)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/8e6c96d73a3c4b4aa30d9a2173795233)](https://www.codacy.com/app/UHD/cures-condec-eclipse?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=cures-hub/cures-condec-eclipse&amp;utm_campaign=Badge_Grade)
[![Codecoverage](https://codecov.io/gh/cures-hub/cures-condec-eclipse/branch/master/graph/badge.svg)](https://codecov.io/gh/cures-hub/cures-condec-eclipse/branch/master)

The CURES ConDec Eclipse plug-in enables the user to capture and explore decision knowledge in Eclipse. Decision knowledge covers knowledge about decisions, the problems they address, solution proposals, their context, and justifications (rationale). The user can capture decision knowledge in code and commit messages and explore relevant knowledge for code. Relevant knowledge is also extracted from the JIRA project.

## Installation

### Prerequisites
The following prerequisites are necessary to compile the plug-in from source code:
- Java 8 JDK
- [Maven 3](https://maven.apache.org)

### Compilation via Terminal
The source code can be compiled via terminal.
Navigate into the cures-condec-eclipse folder and run the following command:
```
mvn clean install
```
The cures-condec-eclipse.jar file is created in the de.uhd.ifi.se.decision.management.eclipse/target subfolder.

### Download of Precompiled .jar-File
The precompiled .jar-File for the latest release can be found here: https://github.com/cures-hub/cures-condec-eclipse/releases/latest

### Develop in Eclipse
To evolve the ConDec Eclipse plug-in, it needs to be imported into Eclipse.
- [Install Eclipse IDE for RCP and RAP Developers](https://www.eclipse.org/downloads/packages/) and open it.
- Import the project as an Existing Maven Project.
- Run *mvn clean install* in the parent project (cures-condec-eclipse folder) to download the necessary dependendencies into the de.uhd.ifi.se.decision.management.eclipse/lib folder.
- Configure the de.uhd.ifi.se.decision.management.eclipse project: Properties / Java Build Path / Libraries / 
    - Add JARs... / select all jar-files in the lib folder (if not already included).
    - Add Library... / select the *Plug-in Dependencies* (if not already included).

### Installation in Eclipse
- Download or compile the cures-condec-eclipse.jar file.
- [Install Eclipse](https://www.eclipse.org/downloads/packages/).
- Navigate to the Eclipse installation folder and copy the cures-condec-eclipse.jar file into the dropins folder (or a similar folder depending on your operation system).
- Open Eclipse.

