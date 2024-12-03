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
    public static int cavalryBaseSpeed;
    public static int cavalryMeleeDamage;
    public static int cavalryRange;
    public static int cavalryRangeDamage;
    public static int catapultBaseSpeed;
    public static int catapultMeleeDamage;
    public static int catapultRange;
    public static int catapultRangeDamage;

    static {
        Properties properties = new Properties();
        try (FileReader reader = new FileReader(FilePathHelper.getConfigFilePath("config.ini"))) {
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

            // Load Cavalry properties
            cavalryBaseSpeed = parseIntProperty(properties, "Cavalry.baseSpeed");
            cavalryMeleeDamage = parseIntProperty(properties, "Cavalry.meleeDamage");
            cavalryRange = parseIntProperty(properties, "Cavalry.range");
            cavalryRangeDamage = parseIntProperty(properties, "Cavalry.rangeDamage");

            // Load Catapult properties
            catapultBaseSpeed = parseIntProperty(properties, "Catapult.baseSpeed");
            catapultMeleeDamage = parseIntProperty(properties, "Catapult.meleeDamage");
            catapultRange = parseIntProperty(properties, "Catapult.range");
            catapultRangeDamage = parseIntProperty(properties, "Catapult.rangeDamage");

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
            System.out.println("Cavalry Base Speed: " + cavalryBaseSpeed);
            System.out.println("Cavalry Melee Damage: " + cavalryMeleeDamage);
            System.out.println("Cavalry Range: " + cavalryRange);
            System.out.println("Cavalry Range Damage: " + cavalryRangeDamage);
            System.out.println("Catapult Base Speed: " + catapultBaseSpeed);
            System.out.println("Catapult Melee Damage: " + catapultMeleeDamage);
            System.out.println("Catapult Range: " + catapultRange);
            System.out.println("Catapult Range Damage: " + catapultRangeDamage);

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
