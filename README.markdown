# S3Proxy sample program for scala.

## Quick Start

1. Set to __`<Azure Blob Account`>__ in conf/s3proxy.conf
2. Set to __`<Azure Blob Access Key`>__ in conf/s3proxy.conf
3. sbt assembly
4. java -jar target/scala-2.12/Hello-assembly-0.1.0-SNAPSHOT.jar
5. curl -X PUT http://localhost:8080/`<foldername`>
