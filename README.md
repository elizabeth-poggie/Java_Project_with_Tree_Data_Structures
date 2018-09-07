# Building Database

## Main idea

this project was designed to get students comfortable with tertiary tree structures and gain experience in writing functions to interact with the tree. 

The idea is that we must create a database that stores and organizes information about buildings that are built on different years and have different heights. To do this we created "building" objects and gave them attributes
 
Newer buildings go to the right of the tree while older buildings go to the left. As well, buildings are organized by height vertically which gives each "building node" a maximum degree of 4 (its parent, its elder building, its younger building, and those of the same year but of different height)

As well there is other information stored at each building node such as its year for repair and cost for repair. this is handy for later calculations and helper functions. 