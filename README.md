# BattleSim
Used for D&D but may expand beyond. Meant to simulate large scale battle between two armies. Two teams filled with different types of soldiers that take turns fighting and dealing damage. 
each "square" is 50 feet. As opposed to 5 ft spaces in D&D. health is based on how many troops are in the unit out of 10k. 10k is max unit size.
turn order is determined from movement speed which is determined by speed which changes based on exhaustion. there are defult values you can use but the program will ask for two txt files that contain the army data.

the txt files must have format: 
<Type>,<health>,<exhaustion>,<name>,<designation>,<x position>,<y position>

exhaustion changes:
attack -.1
move -how much of max move is used

Each unit has:
exhaustion (from 0 -1.0)
battle damage 
movement speed (meanured in feet where each grid square is 50ft)
health (out of 10000)
range(if any)
a designation (2 char)
name

#Unit types:
swordman
archer
spearmen
catipult(coming soon)
cavalry(coming soon)


