public class Archer implements Unit {
    // x and y refer to field 
    private int x;
    private int y;
    //name and target properties
    public String type = "Archer";
    private String name;
    private Unit target;
    private String designation;
    // stats
    private int baseSpeed;
    private int maxHealth = 10000; // that is 10k troops. max amount
    private int currentHealth; // set this equal to number of troops left in the unit out of 10k
    private int speed; // in feet?
    private int meleeDamage;
    private int range;
    private int rangeDamage;
    private double exhausted;

    // defaults
    public Archer(int currentHealth, String name, String designation) {
        this.designation = designation;
        this.name = name;
        this.currentHealth = currentHealth;
        this.exhausted = 1.0;
        init();
        this.speed = (int) (baseSpeed * this.exhausted);
    }

    // from txt file
    public Archer(int currentHealth, String name, double exhausted, String designation) {
        this.designation = designation;
        this.name = name;
        this.currentHealth = currentHealth;
        this.exhausted = exhausted;
        init();
        this.speed = (int) (baseSpeed * this.exhausted);
    }

    private void init() {
        // ini file
        this.baseSpeed = UnitConfig.archerBaseSpeed;
        this.meleeDamage = UnitConfig.archerMeleeDamage;
        this.range =  UnitConfig.archerRange;
        this.rangeDamage = UnitConfig.archerRangeDamage;
    }
    //other:
    // calculate distance between target, deal melee or range damage acordingly
    public void dealDamage(Unit target){
        //check if have enough exhausted and change it
        if(this.exhausted -.1 >= 0){
            this.exhausted= this.exhausted-.1;
            if(target != null){
                // do some damage
                int damage = (int)( meleeDamage*((double)currentHealth/(double)maxHealth));
                if(this.getX()-target.getX() >1 || this.getY() - target.getY() >1){ // check range
                    damage = (int)( rangeDamage*((double)currentHealth/(double)maxHealth));
                    System.out.println("range damage");
                }
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
        target.takeDamage( meleeDamage/(currentHealth/maxHealth));
    }
    */
    //setters
    @Override
    public void setX(int x){
        this.x=x;
    }
    @Override
    public void setY(int y){
        this.y=y;
    }
    public void setSpeed(int newSpeed){
        this.speed = newSpeed;
    }
    public void setTarget(Unit target){
        this.target = target;
    }
    // change exhaustion and speed based on changed value given
    public void changeExhausted(double value){
        if(this.exhausted +value >1){
            this.exhausted =1.0;
        }else if(this.exhausted +value <0){
            this.exhausted=0.0;
        }else{
            this.exhausted = this.exhausted +value;
        }
        this.speed = (int)(baseSpeed * this.exhausted); 
    }
    public void setCurrentHealth(int health) {
        this.currentHealth = health;
        throw new UnsupportedOperationException("Unimplemented method 'setCurrentHealth'");
    }

    public void takeDamage(int damage){
        this.currentHealth = currentHealth-damage;
    }
//getters
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public String getType(){
        return this.type;
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
        return (int)Math.round(speed*exhausted);
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

    public String getName() {
        return this.name;
    }
    public Unit getTarget(){
        return this.target;
    }
    public String getDesignation(){
        return this.designation;
    }
}
