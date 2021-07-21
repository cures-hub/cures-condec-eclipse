# cures-condec-eclipse

[![Build Status](https://app.travis-ci.com/cures-hub/cures-condec-eclipse.svg?branch=master)](https://app.travis-ci.com/cures-hub/cures-condec-eclipse)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/d05c885619e24c5d8fb9113e203d10a4)](https://www.codacy.com/gh/cures-hub/cures-condec-eclipse/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=cures-hub/cures-condec-eclipse&amp;utm_campaign=Badge_Grade)
[![Codecoverage](https://codecov.io/gh/cures-hub/cures-condec-eclipse/branch/master/graph/badge.svg)](https://codecov.io/gh/cures-hub/cures-condec-eclipse/branch/master)
[![GitHub contributors](https://img.shields.io/github/contributors/cures-hub/cures-condec-eclipse.svg)](https://github.com/cures-hub/cures-condec-eclipse/graphs/contributors)

The ConDec Eclipse plug-in enables the user to capture and explore decision knowledge in Eclipse. 
Decision knowledge covers knowledge about decisions, the problems they address, solution proposals, their context, and justifications (rationale). 
The user explores linked knowledge elements for code, such as requirements, work items, decision knowledge elements, and commits. 
The knowledge elements are extracted from the Jira and git projects associated to the Eclipse project. 
Trace links between code files and Jira issues are created via the Jira issue identifier in commit messages. 
The user captures decision knowledge in code comments and commit messages. 

## User Interface and Usage Description

### Configuration
Before you can start using the plug-in, you need to configure the git repository and Jira project associated to your Eclipse project. Select `Project` -> `Properties` -> `ConDec` and add your configuration there.

![Project Settings](https://github.com/cures-hub/cures-condec-eclipse/raw/master/doc/configuration.png)

*Project Settings*

### Knowledge Graph View in Eclipse
The knowledge graph consists of knowledge elements (i.e. decision knowledge elements but also other artifacts) and links (i.e. trace links). You can view the **entire knowledge graph** consisting of code files, commits, decision knowledge elements, and Jira issues (e.g. requirements, work items) for a project. The knowledge graph is interactive: You can create, update, and delete knowledge elements and links. The view provides various filters.

![Entire Knowledge Graph for a Project](https://github.com/cures-hub/cures-condec-eclipse/raw/master/doc/knowledge_graph_condec_eclipse.png)
*Entire Knowledge Graph for a Project*

To explore linked knowledge elements for a specific code file, select a file in the `Project Explorer` and open the context menu. There, you find the new entry `ConDec` with different options for knowledge exploration:

![Context Menu on a Code File](https://github.com/cures-hub/cures-condec-eclipse/raw/master/doc/context_menu.png)
*Context Menu on a Code File*

### Knowledge Graph View in Jira
The Eclipse ConDec plugin enables to quickly navigate from the currently active code file to Jira, 
where decision knowledge and other knowledge elements are shown in various views on the knowledge graph (e.g. as a tree, graph, and matrix view).
![Knowledge Graph View in Jira](https://github.com/cures-hub/cures-condec-eclipse/raw/master/doc/subgraph_from_code_file.png)
*Knowledge Graph View in Jira*

### Textual Representation of Knowledge Graph in Eclipse
The plug-in offers two views for textual knowledge presentation. Go to `Window` -> `Show View` -> `Other...` and add the views *KnowledgeExploration* and *ChangeImpactAnalysis*. Then, select a file in the `Project Explorer` and open the context menu. There, you find the  entry `ConDec` with the submenu `Extract Knowledge`.

![Textual Knowledge Exploration](https://github.com/cures-hub/cures-condec-eclipse/raw/master/doc/knowledge_exploration_text.png)
*Textual Knowledge Exploration*

## Installation

### Prerequisites
The following prerequisites are necessary to compile the plug-in from source code:
- Java 15 JDK
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

### Installation in Eclipse
- [Download](https://github.com/cures-hub/cures-condec-eclipse/releases/latest) or compile the cures-condec-eclipse.jar file.
- [Install Eclipse](https://www.eclipse.org/downloads/packages/).
- Navigate to the Eclipse installation folder and copy the cures-condec-eclipse.jar file into the dropins folder (or a similar folder depending on your operation system).
- Open Eclipse. (Make sure that the eclipse.ini file points to Java 15 JDK.)

## Develop in Eclipse
The following subsections are important if you want to contribute as a developer.

### Prerequisites
- [Install Eclipse IDE for RCP and RAP Developers](https://www.eclipse.org/downloads/packages/) and open it. (Make sure that the eclipse.ini file points to Java 15 JDK.)
- [Install the Tycho Maven plug-in.](http://codeandme.blogspot.com/2012/12/tycho-build-1-building-plug-ins.html)
- [Install the M2E Connector for the Maven Dependency Plug-in.](https://marketplace.eclipse.org/content/m2e-connector-maven-dependency-plugin)

### Import the Plug-in
To evolve the ConDec Eclipse plug-in, it needs to be imported into Eclipse.
- Import the project as an Existing Maven Project.
- Run *mvn clean install* in the parent project (cures-condec-eclipse folder) to download the necessary dependendencies into the de.uhd.ifi.se.decision.management.eclipse/lib folder.
- Configure the de.uhd.ifi.se.decision.management.eclipse project: Properties / Java Build Path / Libraries / 
    - Add JARs... / select all jar-files in the lib folder (if not already included).
    - Add Library... / select the *Plug-in Dependencies* (if not already included).

### Setting up Dependencies
The following steps might be necessary to correctly setup Eclipse, in particular, after you added a or updated an existing dependency:

1. Update the pom.xml in the main/parent folder (add new dependency or update the version of an existing dependency, such as jira-rest-java-client-core from 4.0.0 to 5.2.2). Do not change the nested pom.xml files. (There are four pom.xml files in total.)
2. Delete the contents of the de.uhd.ifi.se.decision.management.eclipse/lib folder
3. Run `mvn clean package -DskipTests` which will refill the lib folder including the new or updated dependencies. (It might fail but the dependencies are copied into de.uhd.ifi.se.decision.management.eclipse/lib, that is fine.) 
4. Update the .classpath of the de.uhd.ifi.se.decision.management.eclipse project. Add all .jar-files in the lib folder and export them so that they can be found by the test project.
5. Update the `MANIFEST.MF` and the `build.properties` using the Eclipse GUI. Navigate to `Runtime` and add all libraries as `Exported Packages` and make sure that the `Classpath` contains all .jar files.
6. Make sure that the file `MANIFEST.MF` contains the entry `Bundle-ClassPath: .,`.

![Runtime Tab](https://github.com/cures-hub/cures-condec-eclipse/raw/master/doc/runtime_tab.png)
*Runtime Tab*

### Run as a second Eclipse Application
You can start a second Eclipse instance containing the plug-in  from within your initial Eclipse by running the de.uhd.ifi.se.decision.management.eclipse project as an `Eclipse Application`. 