# cures-condec-confluence

[![Continuous integration](https://github.com/cures-hub/cures-condec-confluence/actions/workflows/maven.yml/badge.svg)](https://github.com/cures-hub/cures-condec-confluence/actions/workflows/maven.yml)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/89126793a77b4295ad1c000899aa2880)](https://www.codacy.com/gh/cures-hub/cures-condec-confluence/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=cures-hub/cures-condec-confluence&amp;utm_campaign=Badge_Grade)
[![Codecoverage](https://codecov.io/gh/cures-hub/cures-condec-confluence/branch/master/graph/badge.svg)](https://codecov.io/gh/cures-hub/cures-condec-confluence/branch/master)
[![GitHub contributors](https://img.shields.io/github/contributors/cures-hub/cures-condec-confluence.svg)](https://github.com/cures-hub/cures-condec-confluence/graphs/contributors)

The ConDec Confluence plugin enables to integrate decision knowledge into meeting agendas.
It enables the meeting creator to filter for decision knowledge to be shown in the meeting agenda.
For example, it enables the meeting creator to filter for decision knowledge created and relevant for the last sprint.
Thus, the developers get an overview which issues (decision problems) need to be solved or were solved, i.e., which decisions were made during the last sprint or need to be made during the upcoming sprint.

## Installation

### Prerequisites
The following prerequisites are necessary to compile the plug-in from source code:
- Java 11 JDK
- [Atlassian SDK](https://developer.atlassian.com/docs/getting-started/set-up-the-atlassian-plugin-sdk-and-build-a-project)

### Compilation via Terminal
The source code is compiled via terminal.
Navigate into the cures-condec-confluence folder and run the following command:
```
atlas-mvn package
```
The .jar file is created.

Run the plug-in locally via:
```
atlas-run
```

### Download of Precompiled .jar-File
The precompiled .jar-File for the latest release can be found here: https://github.com/cures-hub/cures-condec-confluence/releases/latest

## Usage

### Application Link between Confluence and Jira
To share decision knowledge between Jira and Confluence, they need to be linked via an [application link](https://confluence.atlassian.com/adminjiraserver/using-applinks-to-link-to-other-applications-938846918.html).
The authentication type needs to be **OAuth (impersonation)**.

### Decision Knowledge Import Macro
The *Decision Knowledge Import Macro* can be used to create a stand-up table in meetings. 
The stand-up table contains relevant requirements, as well as open and solved decision problems for a certain time frame.

![Decision Knowledge Import Macro](https://github.com/cures-hub/cures-condec-confluence/raw/master/doc/macro_edit_dialog.png)
*Macro to import decision knowledge from Jira*

![Decision Knowledge List](https://github.com/cures-hub/cures-condec-confluence/raw/master/doc/imported_decision_knowledge.png)
*List of decision knowledge as part of a meeting agenda/protocol (used as stand-up table)*