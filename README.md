[![DepShield Badge](https://depshield.sonatype.org/badges/sonatype-nexus-community/struts2-rce/depshield.svg)](https://depshield.github.io)

## Exploit Demo for CVE-2017-5638

Completely based on https://github.com/piesecurity/apache-struts2-CVE-2017-5638

A realistic scenario where a reference project for a framework is deployed on a container but with terrible consequences. To familiarise yourself look at the code and compile it. Also investigate the dockerfile - does anything specific rise to get our attention?

Shows how easily every day decisions and multiple vulnerabilities in chain can be leveraged.

### Usage:
Pre-requisites:
1. have a JDK installed,
1. have Docker installed, Docker Desktop for Mac or Win
1. ideally have Python installed (can be replaced by [Jython](https://www.jython.org/))

To prepare:
1. clone this repo
1. run `./mvnw clean package` in project root
1. run `docker build -t hackme \.`
1. run `docker run -d -p 9080:8080 hackme`
1. once container comes online - verify by running in browser http://localhost:9080

Notice: if you don't have Docker installed, you can run `./mvnw jetty:run`

To begin testing RCE - run the `exploit.py` file:
* run `python exploit.py http://localhost:9080/orders/3 "CMD"`
* If you don't have Python, use the [Jython Standalone](https://www.jython.org/downloads.html) and\
  run `java -jar jython*.jar exploit.py http://localhost:9080/orders/3 "CMD"`

Try with different CMDs like
* `pwd` - where are we?
* `whomai` - what user are we running this?
* `ls -la` - what's in my directory?
* `ls /` - what's my machine
* `ls /etc` - what else we can find?

## How to Fix!
Use the Nexus Lifecycle [Component Information Panel](https://help.sonatype.com/iqserver/reporting/application-composition-report/resolving-security-issues) to identify a non-vulnerable version of struts2-core. 
Update the POM to that version and rebuild. You can also rebuild the Docker image and run it to retry the attack.

Also, look in the Issues here to see [DepShield](https://www.sonatype.com/depshield) findings


## Original readme

https://github.com/apache/struts/tree/master/apps/rest-showcase

```
README.txt - Rest Showcase Webapp

Rest Showcase is a simple example of REST app build with the REST plugin.

For more on getting started with Struts, see 

* http://cwiki.apache.org/WW/home.html

I18N:
=====
Please note that this project was created with the assumption that it will be run
in an environment where the default locale is set to English. This means that
the default messages defined in package.properties are in English. If the default
locale for your server is different, then rename package.properties to package_en.properties
and create a new package.properties with proper values for your default locale.
```
