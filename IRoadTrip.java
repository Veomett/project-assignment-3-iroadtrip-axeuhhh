import java.util.*;

import java.io.*;

public class IRoadTrip {
	
	// file names for the data
	String border;
	String capDist;
	String stateName;
	
	//This is the graph Storing the country of origin and its neighbors
	HashMap<String,HashMap<String, Integer>> Graph;
	
	//stores the cost of each country
	
	// stores the special cases of the border name file
	HashMap<String,String> specialCases;
	
	// 

	public IRoadTrip (String [] args) throws IOException {
		border = args[0];
		capDist = args[1];
		stateName = args[2];
		
		Graph = new HashMap<>();

		
		specialCases = new HashMap<>();
		
//		sort();
//		test();
		specialCases();
		
		//this is creating the graphby adding the content
		addBorder();
		

	}

    
    /*
     * file sorting
     */
    public void sort() throws FileNotFoundException {
 
    	
    	for (String s : Graph.keySet()) {
    		File f = new File(stateName);
        	Scanner scan = new Scanner(f);
        	System.out.println("\n"+s);
        	
    		while (scan.hasNextLine()) {
    			String[] line = scan.nextLine().split("\t");
//    			System.out.println(line[2]);
    			if (line[2].trim().toLowerCase().equals(s.trim().toLowerCase())){
    				System.out.println("Found "+line[2]+" "+s+"\n");
    			} 
    		}
    		
		}
    	
    }
    
    /*
     * testing and finding the special cases
     */
    
    public void test() throws FileNotFoundException{
    	File f = new File("specialCases.txt");
    	Scanner scan = new Scanner(f);
    	String s =""
;    	while(scan.hasNextLine()){
    		s=scan.nextLine();
    			if (s.length()==0) {
//    				s = scan.nextLine();
    			}else if(scan.hasNextLine()) {
    				String s2 = scan.nextLine();
    				if  (s2.length()==0) {
    					System.out.println(s);
    				}
    			}
//    		scan.nextLine();
    	}
		scan.close();

    }
    
	

 
  	
  	
  	public void negativeOne(){
  		for (String s : Graph.keySet()) {
  			for (String j : Graph.get(s).keySet()) {
  				if (Graph.get(s).get(j)==-1) {
  					System.out.println(s +"  to  "+j+" the distance is: "+Graph.get(s).get(j));
  				}
  			}
  		}
  	}

  		

  	private class Node implements Comparable<Node>{
  		int cost;
  		String name;
  		
  		Node(String s, int i){
  			cost =i;
  			name=s;
  		}
  		@Override
  		public int compareTo(Node n) {
  			// TODO Auto-generated method stub
  			return Integer.compare(this.cost, n.cost);
  		}
  		
  	}



  // here 
 
  	public HashMap<String, String> dijkstra(String source, String dest){ // return HashMap<String, String> which is the path hash map
  	    


	    PriorityQueue<Node> costMinHeap = new PriorityQueue<>();
		//Node[] finalCosts = new Node[Graph.size()];// here we store the final distance from the source of that vertex
		// Add another hash map here that will help you trace the min path.  Put "previous" here.  It should get re-made every time dijkstra's is called
		HashMap<String, Integer> currentCosts = new HashMap<>(); // this should be *current* costs.  Also include a set to mark which countries are finalized
		
		HashMap<String, Integer> NodeCost = new HashMap<>();
		
		HashMap<String, String> prev = new HashMap<>();
		
		Set<String> finalized = new HashSet<>();
		
		
		for (String country: Graph.keySet()){
            if (country.equals(source)){
                currentCosts.put(source, 0);
                //System.out.println("Destination is " + country1);
                if (Graph.get(source) != null){
                    for (String neighbor: Graph.get(source).keySet()){
                        currentCosts.put(neighbor, getDistance(source, neighbor));
                        costMinHeap.add(new Node(neighbor, getDistance(source, neighbor)));
                        //System.out.println("Putting in neighbor " + neighbor + " with distance " + getDistance(country1, neighbor));
                        prev.put(neighbor, source);
                    }
                }
            }
        }
	
	    while (!finalized.contains(dest) && !costMinHeap.isEmpty()){
	        Node curNode = costMinHeap.poll();
	        //System.out.println("Pulling country " + curNode.country);
	        //Integer dist = curNode.distance;  // don't need?
	        if (curNode != null && !finalized.contains(curNode.name) && Graph.get(curNode.name) != null){
	            finalized.add(curNode.name);
	            for (String s : Graph.get(curNode.name).keySet()){
	                Integer neighborDist = getDistance(curNode.name, s);
	                if (neighborDist != -1 && !currentCosts.containsKey(s)){ // neighbor not yet initialized
	                	currentCosts.put(s, currentCosts.get(curNode.name) + neighborDist);
	                    prev.put(s, curNode.name);
	                    Node insertedNode = new Node(s, currentCosts.get(s));
	                    costMinHeap.add(insertedNode);
	                }
	                else if (neighborDist != -1 && currentCosts.get(curNode.name) + neighborDist < currentCosts.get(s)){
	                	currentCosts.replace(s, currentCosts.get(curNode.name) + neighborDist);
	                    prev.put(s, curNode.name);
	                    Node insertedNode = new Node(s, currentCosts.get(s));
	                    costMinHeap.add(insertedNode);
	                }
	            }
	        }
	    }
	    if(finalized.contains(dest) && (currentCosts.get(dest)!=null)){
	        return prev;
	    }
	    else{
	        return null;
	    }
	 
	}

  	public void specialCases() throws FileNotFoundException{
  		File f  = new File("diffnames.txt");
  		Scanner scan1 = new Scanner(f);
//  		System.out.println(f.length());
//  		File f2 = new  File(capDist);
  		
  		while (scan1.hasNextLine()) {
  			String[] line = scan1.nextLine().split("=");
  			specialCases.put(line[0],line[1]);
  		}
  		scan1.close();		
  	}

  	/*
  	 * addBoarder(): reads the file borders.txt to initialize the graphs countries with their neighbors
  	 */
      private void addBorder() throws IOException{
  		File f = new File("borders.txt");
      	Scanner scan = new Scanner(f);
      	
      	while (scan.hasNextLine()) {
      	 	String[] s = scan.nextLine().trim().split("[=;]");
//      	 	System.out.println(s.length +" "+s[0]);
      	 	if (s.length!=1) {// handles island and skip over them
      	 		Graph.put(s[0].trim(), new HashMap<>());
          	 	
          	 	for (int i = 1; i<s.length;i++) {
          	 		String temp = s[i].trim();
          	 		String name = "";
          	 		
          	 		// make sure to get everything before the number
          	 		for (int j = 0; j < temp.length();j++) {
          	 			if (Character.isDigit(temp.charAt(j))) {
          	 				break;
          	 			}
          	 			name += temp.charAt(j);
          	 		}
          	 		name=name.trim();

          	 		Graph.get(s[0].trim()).put(name, addDistance(s[0].trim(),name));// handle the weird case for the this thing'
//          	 		System.out.println(s[0]);
          	 	}
      	 	}
      	 	
      	}
      	scan.close();
  		
  	}

      /*
       * addDistance(): reads the file capdist and then gets the distances
       */
      private int  addDistance(String origin, String dest) throws FileNotFoundException{
//      	System.out.println(specialCases.size());
      	origin = specialCases.containsKey(origin)? specialCases.get(origin): origin;
      	dest = specialCases.containsKey(dest)? specialCases.get(dest): dest;
      	
      	origin = origin.trim();
      	dest = dest.trim();
      	
      	String originCode="";
      	String destCode="";
      	
      	File f  = new File("state_name.tsv");
  		Scanner scan = new Scanner(f);
  		
  		while (scan.hasNextLine()) {
  			String[] line = scan.nextLine().split("\t");
  			
  			if (line[2].equals(origin)) {
  				originCode=line[0];
  			}else if (line[2].equals(dest)) {
  				destCode=line[0];
  			}
  		}
  		
  		scan.close();
  		return capDist(originCode, destCode);
  		

      }
      
      
      
      public int capDist( String originCode, String destCode) throws FileNotFoundException {
      	
      	File f = new File("capdist.csv");
  		Scanner scan = new Scanner(f);
  		
  		while (scan.hasNextLine()) {
  			String[] line = scan.nextLine().split(",");
  			if (line[0].equals(originCode) && line[2].equals(destCode)) {
  				return Integer.valueOf(line[4]);
  			}
  		}
      	return -1;
      }
      
      
      
      
      public int getDistance (String country1, String country2) {
          // Replace with your code
      	
      	return Graph.get(country1).get(country2);

      	
      }


      public List<String> findPath (String country1, String country2) {
          // Replace with your code
      	
      	HashMap<String, String> map = dijkstra(country1, country2);
      	
      	if (map == null) {
      		return null;
      	}
      	
      	String s = country2;
      	while(s!=country1) {
      		System.out.println("* "+s + " -->" +map.get(s) + " ("+ getDistance(s, map.get(s))+"km.)");
      		s=map.get(s);
      	}
      	
          return null;
      }


      public void acceptUserInput() throws IndexOutOfBoundsException{
      	Scanner scan = new Scanner(System.in);
      	System.out.println("Here is our Road Trip generator");
      	while (true) {
      	    
      	    System.out.print("Please enter the first country (type EXIT to quit): ");
      	    String country1 = scan.nextLine().trim();
          	if (country1.equals("EXIT")){
          		System.exit(0);
          	}
          	if (!Graph.containsKey(country1)) {
                  System.out.println("Invalid country");
              }
          	
          	System.out.print("Please enter the second country (type EXIT to quit): ");
          	String country2 = scan.nextLine().trim();
          	if (country2.equals("EXIT")){
          		System.exit(0);
          	}
          	if (!Graph.containsKey(country2)) {
                  System.out.println("Invalid country");
              }
          	
          	findPath (country1,country2);
          	
          	
      	}
      	// Replace with your code
         
      }
      public static void main(String[] args) throws IOException {
          if (args.length<3) {
          	System.err.print("Please enter 3 file names!");
          }
      	IRoadTrip a3 = new IRoadTrip(args);
          //borders.txt capdist.csv state_name.tsv
          a3.acceptUserInput();
      }
   

}

