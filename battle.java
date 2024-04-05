public class battle {
   private set armyOne;
   private set armyTwo;
   private set turnOrder;
   
public battle(){
   this.armyOne = new set();
   armyOne.insertName("idiots play games");
   this.armyTwo = new set();
   armyTwo.insertName("Tongas Gillroy");
    fillArmy();
}

   private void fillArmy(){
        this.armyTwo.insert(1,new swordsman(2000, "U1"));
        this.armyTwo.insert(2,new spearmen(4000, "U2"));
        this.armyTwo.insert(3,new archers(2000, "U3"));
       // this.armyTwo.insert(4,new cavalry(1000, "U1"));
        this.armyTwo.insert(5,new archers(1000, "U4"));
   //----------------------------------
         this.armyOne.insert(101,new swordsman(2000, "devon"));
        this.armyOne.insert(102,new spearmen(4000, "sam"));
        this.armyOne.insert(103,new archers(2000, "devon"));
       // this.armyOne.insert(104,new cavalry(1000, "U1"));
        this.armyOne.insert(105,new archers(1000, "hunter"));
   }
   public set turnOrder(){
     this.turnOrder = this.armyOne.Union(armyTwo, armyOne);
     return turnOrder;
     }
   public void selectTarget(String belongsTo){
      set attacker = armyOne.getFromName(belongsTo);
   }
//getter
   public set getA1(){
      return armyOne;
   }
   public set getA2(){
   return armyTwo;
   }
}