[![DepShield Badge](https://depshield.sonatype.org/badges/sonatype-nexus-community/struts2-rce/depshield.svg)](https://depshield.github.io)

## Exploit Demo for CVE-2017-5638

Completely based on https://github.com/piesecurity/apache-struts2-CVE-2017-5638

### Usage:
Pre-requisites: have Docker and a JRE installed, and Python if not on Mac

1. fork this repo
1. run `./mvnw clean package` in project root
1. run `docker build -t hack \.`
1. run `docker run -d -p 9080:8080 hack`
1. once container comes online - verify by running in browser http://localhost:9080

To begin testing RCE - run the exploit.py file.

1. `python exploit.py http://localhost:9080/orders/3 "CMD"` or if on a Mac you can run `./dist/exploit http://localhost:9080/orders/3 "CMD"`

Try with different CMDs like
* `pwd` - where are we?
* `whomai` - what user are we running this?
* `ls -la` - what's in my directory?
* `ls /` - what's my machine
* `ls /etc` - what else we can find?

## How to Fix!
Use the Nexus Lifecycle Component Information Panel to identify a non-vulnerable version of struts2-core. 
Update the POM to that version and rebuild. You can also rebuild the Docker image and run it to retry the attack.

Also, look in the Issues here to see DepShield findings


## Original readme

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
