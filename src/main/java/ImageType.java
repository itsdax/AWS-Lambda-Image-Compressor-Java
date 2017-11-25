
public enum ImageType {

    PNG ("png"),
    JPG ("jpg"),
    GIF ("gif"),
    OTHER ("other");

    private String extension;

    ImageType(String extension){
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
