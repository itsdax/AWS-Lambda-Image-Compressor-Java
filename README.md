# AWS-Lambda-Image-Compressor-Java
Lambda Function that compresses PNG and JPG files. Replaces original file in S3 with compressed.
Reduces image size while keeping quality loss at minimal.

### Instructions:
1) Install Maven.
```
brew install maven
```

2) Clone this repo.

3) cd into this repo.

4) Package as shaded JAR
```
mvn clean package shade:shade
```

5) A target folder with the final JAR artifact will be created.
In this case the name of the the JAR artifact will be 'image-compression-1.0-SNAPSHOT.jar'

6) Create a new Lambda Function on AWS. Make sure to add a role with all the appropiate permissions (S3 read and write, CloudWatch, etc).

7) Upload shaded JAR.

8) Set up a S3 trigger for Object creation in your desired bucket-> Object Created (all). 

9) Change Handler Field to:
```
Index::handleRequest
```

10) Increase Memory and Timeout Limit (otherwise function will timeout)

11) Done. Now whenever a PNG or JPG is uploaded to the bucket with the S3 event added on step 8, this function will get fired and the image will get compressed.

Optional:
If you wish to change the compression rate, you can change the value in line 96 of index.java

## HappyFace.jpg
<img src="https://i.imgur.com/zIJeBvK.jpg" width="96">

## Before
![Before](https://i.imgur.com/RyMGsVW.png)


## After
![After](https://i.imgur.com/OQnRCvs.png)
