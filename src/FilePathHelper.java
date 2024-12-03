import java.io.File;

// help locate the config and data files relitive to the dist folder
public class FilePathHelper {
    public static String getExecutableDir() {
        try {
            String path = new File(FilePathHelper.class.getProtectionDomain().getCodeSource().getLocation().toURI())
                    .getParent();
            return new File(path).getAbsolutePath();
        } catch (Exception e) {
            throw new RuntimeException("Could not determine executable directory.", e);
        }
    }

    public static String getRootDir() {
        return new File(getExecutableDir()).getParent();
    }

    public static String getConfigFilePath(String filename) {
        System.out.println("debug for config helper");
        return getRootDir() + File.separator + "config" + File.separator + filename;
    }

    public static String getDataFilePath(String filename) {
        System.out.println("debug for data helper");
        return getRootDir() + File.separator + "data" + File.separator + filename;
    }
}
