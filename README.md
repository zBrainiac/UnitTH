UnitTH
======
fork from http://junitth.sourceforge.net/
updating & clean-up


INSTALL
---
* unzip
* [OPTIONAL] edit and move the unitth.properties file to the working directory or $HOME

Configuration
---
* unitth.properties
* System properties
* -D flags

unitth.properties
---
Make sure to get the paths correct in here, especially if running on Microsoft
platforms. There should be enough examples in the provided properties file.

System properties
---
If preferring to use system properties instead feel free to do so. All properties
defined as system properties will override the default ones and those in the
unitth.properties file.

D flags
---
Used with the java invokation  e.g. -Dunitth.report.

USAGE
---
Run 'java -jar unitth.jar' to get full usage descriptions.

java -jar unitth-x.y.z.jar <reports to parse>
java -jar unitth-x.y.z.jar -j <jenkins build folder e.g. ../../builds>

NOTES
---
The reports shall not be moved around after generation since all the links
will be generated as relative paths.


Marcel(private):
----
from desktop: java -jar unitth.jar ./report/*
IDE Terminal: java -jar ./target/unitth-2.1.0-SNAPSHOT.jar ./src/test/resources/junitsamplereport/*
debug: java -jar unitth.jar /Users/marcel/Desktop/unitth/report/*


/Users/marcel/Documents/workspace/UnitTH/src/test/resources/junitsamplereport