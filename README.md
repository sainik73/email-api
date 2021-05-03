# Demo Email Api

Summary
-----------------
This repository shows the working example of how to send Email using SendGrid Mail Api.
 
Future Enhancements:
1) Secure the email api using spring security
2) Validate SendGrid input payload with a model jar
3) Have spam filter (use send grid api capabilities for spam filtering)
4) Use Webclient instead of Rest Template
5) Enhance to use Dynamic Template based Email personalization
6) Reporting Metrics

Instructions
-----------------
#### Build Instructions:
1. Clone this repository:

`git clone https://github.com/sainik73/email-api`

2. Create a new file `apikey.properties` under `src/main/resources` and add SendGrid API Key with property name as `sendgrid.api.key`

3.Build the project with Maven:

```
> cd email-api/
> mvn clean install
```
Integration Testing
-------------------
```
> cd email-api/
> mvn verify -P integrationtest
```

Testing 
-----------------
1. Run the spring boot application 'Email Api'
2. Test the application using postman [![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/329fecc4a733d1e803a0)

OAS API Documentation 
------------------
<b>OAS Doc:</b> http://<serverhost>:<port>/api-docs

<b>UI:</b> http://<serverhost>:<port>/email-api-ui.html
```
http://localhost:8180/api-docs
http://localhost:8180/email-api-ui.html
```