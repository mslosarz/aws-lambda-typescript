# What is this?

Here you can find template for AWS Lambda written in TS.

To build project you need to type:
`npm run all`
Build process consists of:
  - linting project (TSLint)
  - running tests
  - generating js files (tsc) 
  - trimming node_modules to production only
  - creating zip file with lambda code and dependencies (zip is stored in dist directory)
  
When you have zip file, you can create your lambda function. It can be done by AWS Web Console.
Navigate to lambda functions, then click `Create Function`, pickup name for you function, and click `Create`.

AWS should redirect you to lambda configuration page. Now to deploy your lambda you need to do the following steps:
 - navigate to section `Function code`, and change `code entry type` to `upload a .zip file`.
 - upload generated lambda.zip
 - set `Runtime` to `Node.js 8.10`
 - change handler to `dist/handler.handler`
 
 That's all :) now you can enjoy playing with lambda written in TypeScript
 
# There is another way to do that!
As you can see this project contains Jenkinsfile so you can easily build and deploy this lambda automatically!

(If you don't have remote jenkins you can spin up your local copy you can find project [here](https://github.com/mslosarz/aws-jenkins))

What needs to be done:
1. create folder in your existing Jenkins e.g. dev/test/prod (it's very important because folder is used to distinguish environment)
2. Inside folder create the pipeline job (with the name you want), and configure it to use jenkinsfile from this project
3. Run build!


# How to get rid of all created resources?

To clean up AWS resources you need to navigate AWS Console / cloudformation and delete two stacks `<env>-ts-lambda` and `<env>-deployment-bucket`. 
S3 Bucket [will not be deleted automatically](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-attribute-deletionpolicy.html) so you need to remove it manual :(  

