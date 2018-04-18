
# Cipher Key

The maven build creates two JAR artifacts. To execute this program at the 
command line as a standalone application:  

`java -jar cipherkey-jar-with-dependencies.jar`

Every minute, at the very start of a new minute, according to the clock
of the machine this application is running on, a Quartz job will assign
a new UUID String value as the current cipher key.

You can test this by calling the service from the command line as so:

*   `cat < /dev/tcp/127.0.0.1/5282`
*   `telnet 127.0.0.1 5282`

If the caller's IP address is listed in this program's configuration
(cipherkey.properties), then a UUID will be served.  
For example:  
`e02d3b8e-f3e2-4eb7-b17f-81314e082941`

If the caller's IP address is **not**  listed in this program's configuration
(cipherkey.properties), then a decoy String will be served.  
For example:  
`1524094500003`
