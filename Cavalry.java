import java.lang.Math;

public class Cavalry implements Unit {
    // x and y refer to field
    private int x;
    private int y;
    // name and target properties
    public String type = "Cavalry";
    private String name;
    private Unit target;
    private String designation;
    // stats
    private int baseSpeed; // 150 // make static?
    private int maxHealth = 10000; // 10k troops max
    private int currentHealth; // number of troops left in the unit out of 15k
    private int speed;
    private int meleeDamage; // 100
    private int range; // 50
    private int rangeDamage; // 0
    private double exhausted;

    // Constructor
    // Default
    public Cavalry(int currentHealth, String name, String designation) {
        this.designation = designation;
        this.name = name;
        this.currentHealth = currentHealth;
        this.exhausted = 1.0;
        this.speed = baseSpeed;
        init();
    }

    // From txt file
    public Cavalry(int currentHealth, String name, double exhausted, String designation) {
        this.designation = designation;
        this.name = name;
        this.currentHealth = currentHealth;
        this.exhausted = exhausted;
        init();
        this.speed = (int) (baseSpeed * this.exhausted);
        System.out.println("created a Cavalry unit");
    }

    // Init
    private void init() {
        // ini file:
        this.baseSpeed = UnitConfig.cavalryBaseSpeed;
        this.meleeDamage = UnitConfig.cavalryMeleeDamage;
        this.range = UnitConfig.cavalryRange;
        this.rangeDamage = UnitConfig.cavalryRangeDamage;
    }

    public int dealDamage(Unit target) {
        if (this.exhausted - 0.1 >= 0) {
            this.exhausted = this.exhausted - 0.1;
            if (target != null) {
                int damage = (int) (meleeDamage * ((double) currentHealth / (double) maxHealth));
                target.takeDamage(damage);
                System.out.println("did " + damage + " damage");
                return damage;
            }
        } else {
            System.out.println("too tired to fight");
        }
        return 0;
    }

    // Setters
    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    public void setSpeed(int newSpeed) {
        this.speed = newSpeed;
    }

    public void setTarget(Unit target) {
        this.target = target;
    }

    public void setCurrentHealth(int health) {
        this.currentHealth = health;
        throw new UnsupportedOperationException("Unimplemented method 'setCurrentHealth'");
    }

    public void takeDamage(int damage) {
        this.currentHealth = currentHealth - damage;
    }

    // Change exhaustion and speed based on changed value given
    public void changeExhausted(double value) {
        if (this.exhausted + value > 1) {
            this.exhausted = 1.0;
        } else if (this.exhausted + value < 0) {
            this.exhausted = 0.0;
        } else {
            this.exhausted = this.exhausted + value;
        }
        this.speed = (int) (baseSpeed * this.exhausted);
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getName() {
        return this.name;
    }

    public double getExhausted() {
        return this.exhausted;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getSpeed() {
        return (int) Math.round(speed * exhausted);
    }

    public int getMeleeDamage() {
        return meleeDamage;
    }

    public int getRange() {
        return range;
    }

    public int getRangeDamage() {
        return rangeDamage;
    }

    public String getType() {
        return this.type;
    }

    public Unit getTarget() {
        return this.target;
    }

    public String getDesignation() {
        return this.designation;
    }
}
