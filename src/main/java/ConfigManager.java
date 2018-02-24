import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Manages all your config needs
 */
public class ConfigManager {
    /**
     * Stores all core fields. The core fields are the minimum needed fields to run the bot.
     */
    private static Map<String, String> coreFields = new LinkedHashMap<>();

    /**
     * The file path of the default config file.
     */
    private static final String defaultPath = "drone.properties";

    /**
     * The file path of the current confing file.
     */
    private static String currentPath = defaultPath;

    /**
     * The bot properties.
     */
    public static Properties properties;

    /**
     * Sets the path of the config to load. You most likely have to reload the config using loadConfig() after changing the config path.
     *
     * @param configPath the path of the new config file
     */
    public static void setConfigPath(String configPath) {
        ConfigManager.currentPath = configPath;
    }

    /**
     * Loads the configuration file or creates a new one if it doesn't exist.
     */
    public static void loadConfig() {
        try {
            File file = new File(currentPath);
            if (!file.isFile()) {
                if (!file.createNewFile()) {
                    throw new IOException("Could not create new file: " + currentPath);
                }
            }

            properties = new Properties();

            FileInputStream inputStream = new FileInputStream(currentPath);
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the configuration file.
     */
    public static void saveConfig() {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(currentPath);
            properties.store(outputStream, "");
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}