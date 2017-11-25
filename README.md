# AWS-Lambda-Image-Compressor-Java
Lambda Function that compresses PNG and JPG files. Replaces original file in S3 with compressed. 

### Instructions:
Maven install and get the shaded JAR

Create Lambda function on AWS that is triggered by Object creation.
Provide the lambda function with the permissions for bucket read and write. Cloudwatch logging is also helpful.
Upload the shaded jar as source.

Now you can test upload an image to the bucket and check afterwards for the compressed image.
