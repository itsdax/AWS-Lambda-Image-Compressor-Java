import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;


public class InputStreamIdentifier {

    public static ImageType getImageType(InputStream inputStream) throws IOException {
        try {
            ImageInputStream iis = ImageIO.createImageInputStream(inputStream);
            Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);

            while (imageReaders.hasNext()) {
                ImageReader reader = imageReaders.next();
                String format = reader.getFormatName();
                format = format.toLowerCase();
                if (format.equals("png")) {
                    return ImageType.PNG;
                }
                if (format.equals("jpg") || format.equals("jpeg")) {
                    return ImageType.JPG;
                }
                if (format.equals("gif")) {
                    return ImageType.GIF;
                }
                System.out.println("FORMAT IS " + format);
            }
            return ImageType.OTHER;
        } finally {
            inputStream.close();
        }
    }
}
