# AWS-Lambda-Image-Compressor-Java
Lambda Function that compresses PNG and JPG files. Replaces original file in S3 with compressed.
Reduces image size while keeping quality loss at minimal.

### Instructions:
Maven install and get the shaded JAR

Create Lambda function on AWS that is triggered by S3 Object creation.
Provide the lambda function with the permissions for bucket read and write. Cloudwatch logging is also helpful for debugging purposes.
Upload the shaded jar as source.

Now you can test upload an image to the bucket and check afterwards for the compressed image.

## HappyFace.jpg
<img src="https://i.imgur.com/zIJeBvK.jpg" width="96">

## Before
![Before](https://i.imgur.com/RyMGsVW.png)


## After
![After](https://i.imgur.com/OQnRCvs.png)
