//-----------------------------------------
// NAME: Alexander Dyakin 
// STUDENT NUMBER: 7828027
// 
// REMARKS: this is the set class where all the work is done to the sets
// also contains the node class :)
//
//-----------------------------------------


public class Set{
    public String name;
    private Node top;
    private Node last;
    //constructor
    public Set(){
        // create dummy node for top and last
        top= new Node(-1,new Node(2147483647,null,new Archer(0, "dummie small","DD")),new Archer(0, "dummie large","DD"));

        last =top.link;
        last.link=top;
        top.unit.setSpeed(0);
        last.unit.setSpeed(100000);
    }
    //getters(not used normally but ill allow it)
    // get top -1st of the list
    public Node getTop(){
        return this.top;
    }
    // get last n+1th item in the list
    public Node getLast(){
    return this.last;
    }
    // get name of this current list
    public String getName(){
        return this.name;
    }
// changest the name of this set
    public void insertName(String name){
        this.name = name;
    }
    // will check if there is duplicate and insert number in correct spot
    //as long as number is > -1 and less than 2147483647
    //no need to check if empty or single set, there are dummies
    public void insert(int number,Unit insert){
        if(isDuplicate(number))
        {
           // System.out.print(number+" is already in list");
        }
        else
        {
         Node prev=top;
         Node next=top.link;
         //search the list for where to put data (ordered)
         while(next.unit.getSpeed()<insert.getSpeed())
         {
            prev=next;
            next=next.link;
         }
         //prev now links to new node and the new node now links to next
         prev.link=new Node(number,next,insert);
         }   
        // System.out.println("DONE");
        }
    
    //enter number it will search list for that number
    // returns true if there is a duplicate
    private boolean isDuplicate(int number){
        //ALLOW DUPLICATES
        boolean isDuplicate=false;
        // calls the search function that i probibly didnt need to make maybe
        if(search(number)==null)
        {
            isDuplicate=false;
        }
        else
        {
            isDuplicate = true;
        }
        return isDuplicate;
    }
    //search list for number"key" returns that node
    private Node search(int key){
        Node next = top.link;    
        boolean found=false;
        // go through entire list until found or reach end of list
        while(next!=top && !found)
        {
            if(next.data==key)
            {
                found=true;
            }
            next= next.link;
        }
        if(found)//found
        {
            return next;
        }
        else//not found
        {
            return null;
        }
    }
    public Unit getUnitFromName(String name){
        boolean found = false;
        Node next= top;
        while(!found && next != last){
            next=next.link;
            if(next.unit.getName().equalsIgnoreCase(name) || next.unit.getDesignation().equalsIgnoreCase(name)){
                found = true;
            }
        }
        if(found){
            return next.unit;
        }else{
            return null;
        }
    }
    public Set getFromName(String name){
        Set result = new Set();
        result.insertName("resultSet");
         // search time
            Node next = top;
            do{
                next= next.link;
                if(next.unit != null && next.unit.getName().equalsIgnoreCase(name)){
                    //found one, add it to the list
                    result.insert(result.getSize()+1, next.unit);
                }
            }
            while(next != top);//search till we get back to top
   
        return result;
    }

    // returns the current size of the set 
    // doesnt count the dummie nodes
    public int getSize(){
        int counter=0;
        Node next = top.link;
        while(next !=last){
            counter++;
            next = next.link;
        }
        return counter;
    }
    // will "remove" an iteger from set if it apearrs
    public void Delete(int number){
        //check if that is actually in the list
        if(isDuplicate(number))
        {
            // search time
            Node next = top.link;
            Node prev = top;
            do{
                prev = next;
                next= next.link;
            }
            while(next.data != number);//search till next is number
            prev.link=next.link; // delete     
        }
        else
        {
            System.out.println("could not find int:"+number+"to delete");
        }
    }
    // take 2 sets setA, setB, and create new set with all items
    // in both lsits with no duplicates!!
    public static Set Union(Set setA,Set setB){
        Node nextA = setA.top.link;
        Node nextB = setB.top.link;
        //initialize new set
        Set newSet=new Set();
        // check extreme cases
        if(nextA.data==setA.last.data)// A is empty
        { 
            if(nextB.data==setB.last.data)// both are empty
            {
                System.out.println("union not possible, both empty sets");
            }
           //copy setB to newSet
           while(nextB.data!=setB.last.data)
           {
            newSet.insert(nextB.data,nextB.unit);
            nextB=nextB.link;
           }
        }
        else if(nextB.data==setB.last.data)//B is empty
        {
            //copy setA to new Set
            while(nextA.data!=setA.last.data)
            {
                newSet.insert(nextA.data,nextA.unit);
                nextA=nextA.link;
               }
        }
        else
        {
    
    // the two do while loops act like nested for loop
        do{
            do{
                if(nextB.data!=setB.top.data)
                {
                    if(nextB.data!=nextA.data)
                    { // if data dosnt match
                        if(nextB.data<nextA.data)//compare data
                        {
                            newSet.insert(nextB.data,nextB.unit);
                        }
                        else if(nextB.data>nextA.data)
                        {
                            newSet.insert(nextA.data,nextA.unit);
                        }
                    }
                }
                //if data matches go to next data in nextB
                nextB=nextB.link;
            }while(nextB.data!=setB.top.data);
            // finished comparing all of nextB with one element from nextA
            // set nextA to next element and compare all nextB to that
            nextA=nextA.link;
            nextB=setB.top;
        }  
        while(nextA.data!=setA.top.data);
// no return type, return newSet?
    }
       return newSet;
    }
// take two sets and makes a new set of all the elements
// that appear in both
public static Set Intersection(Set setA,Set setB){
    Node nextA = setA.top.link;
    Node nextB = setB.top.link;
    // initialize new set
    Set newSet=new Set();

    
// the two do while loops act like nested for loop
    do{
        do{
            if(nextB.data==nextA.data)
            { // if data do match
                newSet.insert(nextA.data, nextA.unit);
            }
            //if data matches go to next data in nextB
            nextB=nextB.link;
        }
        while(nextB.data!=setB.last.data);
        // finished comparing all of nextB with one element from nextA
        // set nextA to next element and compare all nextB to that
        nextA=nextA.link;
        nextB=setB.top;
    }
    while(nextA.data!=setA.last.data);
// no return type, return newSet?
return newSet;
}
// creates a new set from all items that
// appear in set A that arnt in set B
public static Set Diffrence(Set setA, Set setB){
    Node nextA = setA.top.link;
    Node nextB = setB.top.link;
    // initalize new Set and create boolean inBoth
    Set newSet=new Set();
    boolean inBoth=false;
// the two do while loops act like nested for loop
    do{
        
        do{
            
            if(nextB.data==nextA.data)
            { // if data do match
                 inBoth=true;
            }
            //if data matches go to next data in nextB
            nextB=nextB.link;
        }
        while(nextB.data!=setB.last.data);
        // we've compared th first item in set A with
        // all items in set B. lets see if that first Item
        // found a match in the other set
        if(!inBoth)
        {
        newSet.insert(nextA.data,nextA.unit);
        }
        //return inBoth to false
        inBoth=false;
        // finished comparing all of nextB with one element from nextA
        // set nextA to next element and compare all nextB to that
        nextA=nextA.link;
        nextB=setB.top;
    }
    while(nextA.data!=setA.last.data);
// no return type, return newSet?
return newSet;
}
// will print out the set given its place in the array created in main
public void print(int setNum){
    System.out.print("\n"+"Items in set "+name+": {");
    Node next = top.link;
    //counter counts number of items we have printed so far
    int counter=0;
    while(next!=last)
    {
        // 1 items per line
        if(counter%1 ==0){
        System.out.print("\n");
        }
        System.out.print(next.unit.getName()+"("+next.unit.getType()+"):"+next.unit.getCurrentHealth()+"/"+next.unit.getMaxHealth()+",Fatigue: "+next.unit.getExhausted()+".");
        counter++;
        next= next.link;
    }
    System.out.print("}");
}
// will search through list and look for all units who target given arg
public Set hasTarget(Unit inRange){
        Node next = top.link;
        Set targetedbyUnits= new Set();
        Unit toCheck;
        while(next!=last){
            toCheck = next.getUnit().getTarget();
            //compare the two units
            if(toCheck !=null && toCheck.getName().equals(inRange.getName())){
                targetedbyUnits.insert(next.getData(),next.getUnit());
            }
            //next unit in list
            next = next.link;
        }
        return targetedbyUnits;
}
    // private class of Node.. wish it were true
    // creates a interface that will act as a bridge for
    // Node class
    public interface NodeInterface {
        void print();
        NodeInterface getLink();
        int getMovementSpeed();
        Unit getUnit();
    }

    private class Node implements NodeInterface {
        private int data;
        private Unit unit;
        private Node link;

        public Node(int data, Node link, Unit unit) {
            this.data = data;
            this.link = link;
            this.unit = unit;
        }


        @Override
        public void print() {
            System.out.print(unit.getName() + "(" + unit.getType() + "):" + unit.getCurrentHealth() + "/" + unit.getMaxHealth() + ", Exh = " + unit.getExhausted() + ". ");
        }

        @Override
        public NodeInterface getLink() {
            return this.link;
        }

        @Override
        public int getMovementSpeed() {
            return unit.getSpeed();
        }

        @Override
        public Unit getUnit() {
            return this.unit;
        }
        
        private int getData() {
            return this.data;
        }
    }

    public NodeInterface getTopNode() {
        return getTop();
    }
    public NodeInterface getLastNode() {
        return getLast();
    }
}