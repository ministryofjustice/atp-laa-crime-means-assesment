laa-decision-automation-rules
=================================
**Installation and First Time Setup**

This repository needs to be run in conjunction with: 

`https://github.com/ministryofjustice/laa-decision-automation-model`

Download both repositories to your local system


**Build**

From the root of the laa-decision-automation-model folder run:

 `gradle publishToMavenLocal`
  
From the root of the atp-laa-crime-means-assesment folder run:

 `mvn clean install`
 
 Import both projects/respositories into Eclipse/STS (File > Open Projects From File System)
 
 **Running the Tests**
 
 Run CrimeMeansIntegrationTest.java as a JUnit Test

This is a drools based project written for crime means assessment.

**Business rules are **

https://dsdmoj.atlassian.net/wiki/spaces/LAAATPAA/pages/638353936/Crime+Automation+Business+Scenario

**Build**

`mvn clean install`


For example the audit message looks like this
