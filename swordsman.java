import java.lang.Math;
public class swordsman implements unit {
    private static int baseSpeed=100;
    public static String type = "Swordsmen";
    private String name;
    private int maxHealth= 10000;// that is 10k troops. max amount
    private int currentHealth; // set this equal to number of troops left in the unit out of 10k
    private int meleeDamage=100;
    private int range=50;
    private int rangeDamage=0;
    private double exhausted;
    private int speed; // in feet?
    private unit target;
//constructor
//defult
    public swordsman(int currentHealth, String name){
        this.name = name;
        this.currentHealth=currentHealth;
        this.exhausted = 1.0;
        this.speed=baseSpeed;
    }
//from txt file
    public swordsman(int currentHealth, String name,double exhausted){
        this.name = name;
        this.currentHealth=currentHealth;
        this.exhausted = exhausted;
        this.speed =(int)(baseSpeed * this.exhausted);
    }
    //other
    public void dealDamage(unit target ){
        if(this.exhausted -.1 >= 0){
            this.exhausted= this.exhausted-.1;
            if(target != null){
                int damage = (int)( meleeDamage*((double)currentHealth/(double)maxHealth));
                target.takeDamage(damage);
                System.out.println("did "+ damage +" damage");
            }
        }else{
            System.out.println("too tired to fight");
        }
    }
    /* 
    public void dealMeleeDamage(){
        this.exhausted= this.exhausted-.1;
        target.takeDamage( meleeDamage/(currentHealth/maxHealth));
    }
    public void dealRangeDamage(){
        target.takeDamage(rangeDamage/(currentHealth/maxHealth));
    }
    */
    //setters
    public void setSpeed(int newSpeed){
        this.speed = newSpeed;
    }
    public void setTarget(unit target){
        this.target = target;
    }
    public String getType(){
        return this.type;
    }
    public void setCurrentHealth(int health) {
        this.currentHealth = health;
        throw new UnsupportedOperationException("Unimplemented method 'setCurrentHealth'");
    }
    public void setMaxHealth(int health){
        this.currentHealth=health;
    }
    public void takeDamage(int damage){
        this.currentHealth = currentHealth-damage;
    }
    public void setExhausted(double value){
        this.exhausted = this.exhausted+value;
        this.speed =(int)(baseSpeed * this.exhausted);
    }
    public void changeExhausted(double value){
        if(this.exhausted +value >1){
            this.exhausted =1.0;
        }else if(this.exhausted +value <0){
            this.exhausted=0.0;
        }else{
            this.exhausted = this.exhausted +value;
        }
        this.speed =(int)(baseSpeed * this.exhausted);
    }
//getters
    public String getName() {
        return this.name;
    }
    public double getExhausted(){
        return this.exhausted;
    }
    public int getMaxHealth(){
        return maxHealth;
    }
    public int getCurrentHealth(){
        return currentHealth;
    }
    public int getSpeed(){
        return (int)Math.round(this.speed);
    }
    public int getMeleeDamage(){
        return meleeDamage;
    }
    public int getRange(){
        return range;
    }
    public int getRangeDamage(){
        return rangeDamage;
    }
    public unit getTarget(){
        return this.target;
    }
}

