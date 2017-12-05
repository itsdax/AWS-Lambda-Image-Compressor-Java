import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.io.*;
import java.util.Iterator;
import java.util.Map;

public class Index implements RequestHandler<S3Event, String> {

//    public static void main(String[] args) throws IOException {
//        File file = new File("pic3.png");
//        compress(new FileInputStream(file), new FileOutputStream("compress.png"));
//    }

    private AmazonS3 amazonS3;

    public Index() {
        amazonS3 = AmazonS3ClientBuilder.standard().build();
    }

    public String handleRequest(S3Event o, Context context) {
        S3EventNotification.S3EventNotificationRecord record = o.getRecords().get(0);

        S3EventNotification.S3Entity s3Entity = record.getS3();
        String bucketName = s3Entity.getBucket().getName();
        String key = s3Entity.getObject().getKey();



        try (S3Object object = amazonS3.getObject(new GetObjectRequest(bucketName, key))) {
            Map<String, String> metadata =  object.getObjectMetadata().getUserMetadata();
            if (metadata.get("compressed") == null){
                metadata.put("compressed", "true");
            } else {
                return "Already Compressed";
            }

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setUserMetadata(metadata);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            compress(object.getObjectContent(), byteArrayOutputStream);

            objectMetadata.setContentLength(byteArrayOutputStream.size());

            amazonS3.putObject(new PutObjectRequest(bucketName, key, new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), objectMetadata));
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed: " + e.getMessage();
        }

        return "Success!";
    }

    public static void compress(InputStream inputStream, OutputStream outputStream) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            IOUtils.copy(inputStream, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();

            ImageType imageType = InputStreamIdentifier.getImageType(new ByteArrayInputStream(bytes));
            System.out.println("Input is: " + imageType);

            switch (imageType) {
                case PNG:
                case JPG:
                    compress(imageType, new ByteArrayInputStream(bytes), outputStream);
                    break;
            }
        }
    }

    public static void compress(ImageType imageType, InputStream inputStream, OutputStream outputStream) throws IOException {
        try {
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(imageType.getExtension());
            ImageWriter writer = writers.next();

            ImageOutputStream ios = ImageIO.createImageOutputStream(outputStream);
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();
            // Check if canWriteCompressed is true
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(0.75f);
            }
            // End of check
            writer.write(null, new IIOImage(ImageIO.read(inputStream), null, null), param);
        } finally {
            inputStream.close();
        }
    }

}
