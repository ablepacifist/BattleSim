import java.io.*;
import java.util.Properties;

public class UnitConfig {
    public static int archerBaseSpeed;
    public static int archerMeleeDamage;
    public static int archerRange;
    public static int archerRangeDamage;
    public static int swordsmenBaseSpeed;
    public static int swordsmenMeleeDamage;
    public static int swordsmenRange;
    public static int swordsmenRangeDamage;
    public static int spearmenBaseSpeed;
    public static int spearmenMeleeDamage;
    public static int spearmenRange;
    public static int spearmenRangeDamage;

    static {
        Properties properties = new Properties();
        try (FileReader reader = new FileReader("config.ini")) {
            System.out.println("Loading properties from config.ini...");
            properties.load(reader);

            // Load Archer properties
            archerBaseSpeed = parseIntProperty(properties, "Archer.baseSpeed");
            archerMeleeDamage = parseIntProperty(properties, "Archer.meleeDamage");
            archerRange = parseIntProperty(properties, "Archer.range");
            archerRangeDamage = parseIntProperty(properties, "Archer.rangeDamage");

            // Load Swordsmen properties
            swordsmenBaseSpeed = parseIntProperty(properties, "Swordsmen.baseSpeed");
            swordsmenMeleeDamage = parseIntProperty(properties, "Swordsmen.meleeDamage");
            swordsmenRange = parseIntProperty(properties, "Swordsmen.range");
            swordsmenRangeDamage = parseIntProperty(properties, "Swordsmen.rangeDamage");

            // Load Spearmen properties
            spearmenBaseSpeed = parseIntProperty(properties, "Spearmen.baseSpeed");
            spearmenMeleeDamage = parseIntProperty(properties, "Spearmen.meleeDamage");
            spearmenRange = parseIntProperty(properties, "Spearmen.range");
            spearmenRangeDamage = parseIntProperty(properties, "Spearmen.rangeDamage");

            // Print properties to confirm loading
            System.out.println("Archer Base Speed: " + archerBaseSpeed);
            System.out.println("Archer Melee Damage: " + archerMeleeDamage);
            System.out.println("Archer Range: " + archerRange);
            System.out.println("Archer Range Damage: " + archerRangeDamage);
            System.out.println("Swordsmen Base Speed: " + swordsmenBaseSpeed);
            System.out.println("Swordsmen Melee Damage: " + swordsmenMeleeDamage);
            System.out.println("Swordsmen Range: " + swordsmenRange);
            System.out.println("Swordsmen Range Damage: " + swordsmenRangeDamage);
            System.out.println("Spearmen Base Speed: " + spearmenBaseSpeed);
            System.out.println("Spearmen Melee Damage: " + spearmenMeleeDamage);
            System.out.println("Spearmen Range: " + spearmenRange);
            System.out.println("Spearmen Range Damage: " + spearmenRangeDamage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int parseIntProperty(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            System.err.println("Property " + key + " is missing or null.");
            throw new NumberFormatException("Property " + key + " is missing or null.");
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format for property " + key + ": " + value);
            throw e;
        }
    }
}
