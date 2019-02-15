#What is that?

Here you can find template for AWS Lambda written in TS.

To build project you need to type:
`npm run build`
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


 

