public interface unit {
//Getters
    public String getType();
    public String getName();
    public int getMaxHealth();
    public int getCurrentHealth();
    public int getSpeed();
    public int getMeleeDamage();
    public int getRange();
    public int getRangeDamage();
    public double getExhausted();
    public unit getTarget();
//setters
    public void setTarget(unit target);
    public void setCurrentHealth(int health);
    public void takeDamage(int damage);
    public void setExhausted(double value);
    public void changeExhausted(double value);
    public void setSpeed(int newSpeed);
//other
  //  public void move(int distance);
    public void dealDamage(unit target);
}
