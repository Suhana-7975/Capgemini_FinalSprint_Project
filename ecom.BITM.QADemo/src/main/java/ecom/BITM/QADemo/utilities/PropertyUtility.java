package ecom.BITM.QADemo.utilities;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtility {

    public static String getProperty(String key) {

        try (FileInputStream fis = new FileInputStream("src/main/resources/commondata.properties")) {

            Properties properties = new Properties();
            properties.load(fis);

            String value = properties.getProperty(key);
            if (value == null) {
                throw new RuntimeException("Key '" + key + "' not found in commondata.properties");
            }
            return value.trim();

        } catch (IOException e) {
            throw new RuntimeException("Unable to read commondata.properties", e);
        }
    }
}