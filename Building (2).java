/* this project was designed to get students comfortable with tertiary tree structures 
 * and gain experience in writing functions to interact with the tree. 
 *
 * The idea is that we must create a database that stores and organizes 
 * information about buildings that are built on different years and have different heights
 * To do this we created "building" objects and gave them attributes

 * Newer buildings go to the right of the tree while older buildings go to 
 * the left. As well, buildings are organized by height vertically which gives
 * each "building node" a maximum degree of 4
 * (its parent, its elder building, its younger building, and those of the same year but
 * of different height)

 * as well there is other information stored at each building node such as its year for repair 
 * and cost for repair. this is handy for later calculations and helper functions. 
 */


public class Building {

	OneBuilding data;
	Building older;
	Building same;
	Building younger;
	
	public Building(OneBuilding data){
		this.data = data;
		this.older = null;
		this.same = null;
		this.younger = null;
	}
	
	public String toString(){
		String result = this.data.toString() + "\n";
		if (this.older != null){
			result += "older than " + this.data.toString() + " :\n";
			result += this.older.toString();
		}
		if (this.same != null){
			result += "same age as " + this.data.toString() + " :\n";
			result += this.same.toString();
		}
		if (this.younger != null){
			result += "younger than " + this.data.toString() + " :\n";
			result += this.younger.toString();
		}
		return result;
	}
	
        //adding a building to the tree structure 
	public Building addBuilding (OneBuilding b){
		Building scoot = this;
		Building boi = new Building(b);
		while(scoot != null) {
			//check to see if b is younger
			if(b.yearOfConstruction > scoot.data.yearOfConstruction) {
				if (scoot.younger == null) { //he will be a leaf boi
					scoot.younger = boi;
					return this;
				} else if (b.yearOfConstruction == scoot.younger.data.yearOfConstruction && 
						b.height > scoot.younger.data.height){
					Building tmp = scoot.younger;
					scoot.younger = boi; //insert new node
					boi.younger = tmp.younger;
					boi.older = tmp.older;
					//then flush away old data references 
					tmp.older = null;
					tmp.younger = null;
					
					boi.same = tmp;
					return this;
				} else {
					scoot = scoot.younger;
				}
			//check to see if b is older
			} else if (b.yearOfConstruction < scoot.data.yearOfConstruction) {
				if (scoot.older == null) { //he will be a leaf boi
					scoot.older = boi;
					return this;
				//what if its smoller 
				} else if (b.yearOfConstruction == scoot.older.data.yearOfConstruction &&
						b.height > scoot.older.data.height){
					Building tmp = scoot.older;
					scoot.older = boi;
					boi.younger = tmp.younger;
					boi.older = tmp.older;
					//flush away old data refrences 
					tmp.older = null;
					tmp.younger = null;
					
					boi.same = tmp;
					return this;
				} else {
					scoot = scoot.older;
				}
			} else if (b.yearOfConstruction == scoot.data.yearOfConstruction) {
			    //check the root to see if he is larger
				if (b.height > scoot.data.height) {
					//want to connect boi to his ancestors or else it doesn't work
					Building tmp = scoot;
					boi.older = scoot.older;
					boi.younger = scoot.younger;
					boi.same = scoot;
					//flush connections from older and younger scoot
					tmp.older = null;
					tmp.younger = null;
					if (this == tmp) { //make sure to change the root if you changed it
						return boi;
					} else {
						return this;
					}
				//check if he is a leaf, check if he is larger than then next one down the line, if not move on.
				} else if(b.height <= scoot.data.height) {
					if (scoot.same == null) {
						scoot.same = boi;
						return this;
					} else { //look further down the line maybe the next one hes larger then
						if (b.height > scoot.same.data.height) {
							Building tmp = scoot.same;
							scoot.same = boi;
							boi.same = tmp;
							return this;
						} else { //if he is not larger then the next boi just go down tree one
							scoot = scoot.same; //go to the same as scoots
						}
					}
				} 
			}
		}
		//shouldnt ever get to here but im leaving it for funnsies
		return this; 
	}
	
	/
	public Building addBuildings (Building b){ 
		//first add root of tree
		Building root = this;
		if (b==null) {
			return root;
		}
		root = this.addBuilding(b.data); 
		root.addBuildings(b.same);
		root.addBuildings(b.older);
		root.addBuildings(b.younger);
		return root; 
	}
	
	public Building removeBuilding (OneBuilding b){
		// ADD YOUR CODE HERE
		Building root = this;
		//check to see if it is the root, if so then so these things
		if (root.data.yearOfConstruction == b.yearOfConstruction && root.data.yearForRepair == b.yearForRepair &&
				root.data.name == b.name && root.data.height == b.height) {
				if (root.same != null) {  //check to see 
					root.same.younger = root.younger;
					root.same.older = root.older;
					root = root.same;	
					return root;
				} else if (root.older != null) { //if we have two sides we need to worry about children converging
					Building old = root.older;
					Building yon = root.younger;
					root.older = null;
					root.younger = null;
					root = old;
					root.addBuildings(yon);
					return root;
				} else if (root.younger != null) { //i dont need to worry about children if theres only youngers
					root = root.younger;
					return root;
				} else {
					root = null;
				}
		} 
		//next see if this happens later in the tree
		findBuilding(root, b);
		return root;
	}

	//helper method to traverse tree//////////////////////////////////////////////////
	public void findBuilding(Building b, OneBuilding findee) {
		//exit if i hit the end 
		if (b == null) {
			return;
		}
		//checking older folks 
		if (b.older != null) {
			if (b.older.data.yearOfConstruction == findee.yearOfConstruction && b.older.data.yearForRepair == findee.yearForRepair &&
					b.older.data.name == findee.name && b.older.data.height == findee.height) {
				//see if you can replace it with its children nodes from bellow
				if (b.older.same != null) { 
					//making it jump over my target node
					b.older.same.older = b.older.older;
					b.older.same.younger = b.older.younger;
					b.older = b.older.same;	
				//see if you can replace it with its older nodes
				} else if (b.older.older != null) {
					Building old = b.older.older;
					Building yon = b.older.younger;
					b.older.older = null;
					b.older.younger = null;
					b.older = old;
					b.older.addBuildings(yon);
			    //see if you can replace it with its younger nodes
				} else if (b.older.younger != null) {
					b.older = b.older.younger;
				//if it cant be replaced, just delete it
				} else {
					b.older = null;
				}
				return;
			//keep checking down the line if its not located here
			} else {
				findBuilding(b.older, findee);
			}
		}
		//checking younger folks
		if (b.younger != null) {
			if (b.younger.data.yearOfConstruction == findee.yearOfConstruction && b.younger.data.yearForRepair == findee.yearForRepair &&
					b.younger.data.name == findee.name && b.younger.data.height == findee.height) {
				if (b.younger.same != null) { 
					//making it jump over my target node. we dont need to be fancy for this case
					b.younger.same.older = b.younger.older;
					b.younger.same.younger = b.younger.younger;
					b.younger = b.younger.same;	
				//see if you can replace it with its older nodes
				} else if (b.younger.older != null) {
					Building old = b.younger.older;
					Building yon = b.younger.younger;
					b.younger.older = null;
					b.younger.younger = null;
					b.younger = old;
					b.younger.addBuildings(yon);
			    //see if you can replace it with its younger nodes
				} else if (b.younger.younger != null) {
					b.younger = b.younger.younger;
				//if it cant be replaced, just delete it
				} else {
					b.younger = null;
				}
				return;
			} else {
				findBuilding(b.younger, findee);
			}
		}
		//checking the ones along the stem
		if (b.same != null) {
			if (b.same.data.yearOfConstruction == findee.yearOfConstruction && b.same.data.yearForRepair == findee.yearForRepair &&
					b.same.data.name == findee.name && b.same.data.height == findee.height) {
				//then i want to skip over it otherwise
				b.same = b.same.same;
				return;
			} else {
				findBuilding(b.same, findee);
			}
		}
	}
	
	public int oldest(){
	
		Building tmp = this;
		while (tmp.older != null) {
			tmp = tmp.older;
		}
		return tmp.data.yearOfConstruction;
		// DON'T FORGET TO MODIFY THE RETURN IF NEEDS BE
	}

	public int highest(){
		int max = findHeight(this, 0);
		return max; // DON'T FORGET TO MODIFY THE RETURN IF NEEDS BE
	}
	//helper traversal method. fastest and laziest way to do this
	public int findHeight(Building b, int max) {
		if (b == null) {
			return max;
		}
		if (b.data.height > max) {
			max = b.data.height;
		}
		max = findHeight(b.older, max);
		max = findHeight(b.younger, max);
		return max;
	}
	
	public OneBuilding highestFromYear (int year){
		
		Building scoot = this;
		while (scoot != null) {
			if (scoot.data.yearOfConstruction > year) {
				scoot = scoot.older;
			} else if (scoot.data.yearOfConstruction < year){
				scoot = scoot.younger;
			} else if (scoot.data.yearOfConstruction == year) {
				return scoot.data;
			}
		}
		return null;
	}
	
	public int numberFromYears (int yearMin, int yearMax){
		if (yearMin > yearMax) {
			return 0;
		}
		
		//the below code was my first try. this is where i realized i wanted to count recursions 
		//and not just go about the whole thing and hoping. my total would get resett back to 0 every time

		//i left it here to remind myself where oi went wrong
		
		/*if ( this.data.yearOfConstruction >= yearMin && this.data.yearOfConstruction <= yearMax) {
			total = total + 1;
			System.out.println(total);
		}
		if (this.older != null) {
			total = this.older.numberFromYears(yearMin, yearMax);
		}
		if (this.younger != null) {
			total = this.younger.numberFromYears(yearMin, yearMax);
		}
		if (this.same != null) {
			total = this.same.numberFromYears(yearMin, yearMax);
		}*/
		
		//check to see if data is within range. to do this you need to count the amount of recursions 
		//before i kept resetting my total to 0 and wouldnt add properly each time
		
		int total1=0, total2=0, total3=0;
		if ( this.data.yearOfConstruction >= yearMin && this.data.yearOfConstruction <= yearMax) {
			//check out every sub node if the node is within range
			if (this.older != null) {
				//see if its older children are in range
				total1 = this.older.numberFromYears(yearMin, yearMax);
			}
			if (this.younger != null) {
				//see if its younger children are in range
				total2 = this.younger.numberFromYears(yearMin, yearMax);
			}
			if (this.same != null) {
				//see if there are more same children of this year
				total3 = this.same.numberFromYears(yearMin, yearMax);
			}
			//add all values together
			return (1 + total1 + total2 + total3);
			
		} else if (this.data.yearOfConstruction < yearMin) {
			
			//          o
			//        /   \
			//       *     #
			//        \
			//         &                tree might look like this and we might want the & o and # 
			// so in this case you still want to go to the older node and check to see if its children
			// might be in range also.
				
			if (this.younger != null) {
				return this.younger.numberFromYears(yearMin, yearMax);
			}
		} else {
			//          o
			//        /   \
			//       *     #
			//            /
			//           &            tree might look like this and we might want the & o and #   
			
			if (this.older != null) {
				return this.older.numberFromYears(yearMin, yearMax);
			}
		}
		return 0;
	}
	
	public int[] costPlanning (int nbYears){
		
		//traverse all nodes. when they are of a given year store then in the array such that 
		//their year of repair - 2018 = cell of the array they belong to
		//the array size will be nbYears
		int a[] = new int [nbYears];
		int i=0;
		while(i<nbYears) {
			a[i] = costForYear(2018+i);
			i++;
		}
		return a; 
	}
	
	//helper for this boi
	public int costForYear(int Year) {
		if (this.data == null) {
			return 0;
		}
		int total = 0;
		if (this.older != null) {
			//see if its older children are in range
			total += this.older.costForYear(Year);
		}
		if (this.younger != null) {
			//see if its younger children are in range
			total += this.younger.costForYear(Year);
		}
		if (this.same != null) {
			//see if there are more same children of this year
			total += this.same.costForYear(Year);
		}
		if(this.data.yearForRepair == Year) {
			return total + this.data.costForRepair; 
		} 
		return total;
	}
	
}
