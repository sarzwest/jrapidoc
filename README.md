# JRAPIDoc
##### Remote API Reference Generator for JEE Applications
Tool focuses on analyzing and documenting your remote API, based on JAX-RS 2.0 and JAX-WS 2.0. Yours project API documentation can be generated automatically during build process and will be always up to date. No changes to your source code are required, but can be optionally added JRAPIDoc annotations into your API for more specific and better documentation. 

Process of generating API documentation composes of these consecutive steps: introspection of remote API, creation of remote API model and serializing API model in JSON format to file. Custom code can be plugged-in for analysis and modification of API model before serialization to file.

###Artifacts
#####Rest Plugin
Responsible for generating API documentation for RESTful web services build with JAX-RS 2.0.
#####SOAP Plugin
Responsible for generating API documentation for SOAP-based web services build with JAX-WS 2.0.
#####Annotations
Library containing additional annotations for usage in APIs based on JAX-RS and JAX-WS. Helps with creation more specific and better API documentation.

###Integration to Maven build lifecycle
Tool is currently in SNAPSHOT version and was not published in Maven central repository yet. For using it, is workaround to download and build JRAPIDoc locally on your machine. Follow these steps to get local build:
```
$ git clone git@github.com:sarzwest/jrapidoc.git
$ cd jrapidoc
$ mvn install -Prelease
```
####Add Rest Plugin or SOAP Plugin or both to your project build lifecycle
Put code below in WAR module `pom.xml`
#####Rest Plugin
```
<plugin>
    <groupId>org.jrapidoc</groupId>
    <artifactId>jrapidoc-rest-plugin</artifactId>
    <version>1.0-SNAPSHOT</version>
    <executions>
        <execution>
            <id>run</id>
            <goals>
                <goal>run</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <groups>
            <group>
                <baseUrl>http://localhost:8080/example</baseUrl>
                <description>Description of service group</description>
                <includes>
                    <include>org.example.resource</include>
                </includes>
            </group>
        </groups>
    </configuration>
</plugin>
```

####
JRAPIDoc is developed as Apache Maven plugin. Add plugin to your build lifecycle, do simple basic configuration and run build of your project. 
