import java.util.Collection;  	
import java.util.Random;	  	
import java.util.regex.Matcher;	
import java.util.regex.Pattern;
import java.util.*;

public class OurAgent implements Agent
{
    private Environment env = new Environment();
    private State state = new State();
    private Stack<String> route = new Stack<String>();
    //private Search search = new BFS(env);


    public void init(Collection<String> percepts) {
		/*
			- "(SIZE x y)" denoting the size of the environment, where x,y are integers
			- "(HOME x y)" with x,y >= 1 denoting the initial position of the robot
			- "(ORIENTATION o)" with o in {"NORTH", "SOUTH", "EAST", "WEST"} denoting the initial orientation of the robot
			- "(AT o x y)" with o being "DIRT" or "OBSTACLE" denoting the position of a dirt or an obstacle
			Moving north increases the y coordinate and moving east increases the x coordinate of the robots position.
			The robot is turned off initially, so don't forget to turn it on.
		*/
		Pattern perceptNamePattern = Pattern.compile("\\(\\s*([^\\s]+).*");
	
		for (String percept:percepts) {
		
	    try {

		Matcher perceptNameMatcher = perceptNamePattern.matcher(percept);
			if (perceptNameMatcher.matches()) {
				
		    String perceptName = perceptNameMatcher.group(1);
				if (perceptName.equals("HOME")) {
					Matcher m = Pattern.compile("\\(\\s*HOME\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
					if (m.matches()) {
			    env.home = new Point2D(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
					}
				}
		    else if(perceptName.equals("SIZE"))
		    {
					Matcher m = Pattern.compile("\\(\\s*SIZE\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
					if (m.matches()) {
			    env.c = Integer.parseInt(m.group(1));
			    env.r = Integer.parseInt(m.group(2));
					}
		    }
		    else if(perceptName.equals("ORIENTATION"))
		    {
					Matcher m = Pattern.compile("\\(\\s*ORIENTATION\\s*(\\S+)\\s*\\)").matcher(percept);
			if(m.matches()) {
			    switch(m.group(1))
			    {
				case "NORTH":
				    state.direction = 0;
				    break;
				case "EAST":
				    state.direction = 1;
				    break;
				case "SOUTH":
				    state.direction = 2;                          
				    break;
				case "WEST":
				    state.direction = 3;
				    break;
				default:
				    System.out.println("Something went horribly, horribly wrong.");    
			    }
			}
				}
		    else if(perceptName.equals("AT"))
		    {
					Matcher m = Pattern.compile("\\(\\s*AT\\s*DIRT\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
					if (m.matches()) {
				state.dirts.add(new Point2D(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2))));
					}
			
			m = Pattern.compile("\\(\\s*AT\\s*OBSTACLE\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
			if(m.matches()) {
				env.obstacles.add(new Point2D(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2))));                           
			}
		    }              
		    else 
		    {

			System.out.println("other percept:" + percept);
		    }
			}
		else {

				System.err.println("strange percept that does not match pattern: " + percept);
			}
	    }
	    catch(Exception e) {

		System.out.println(e);
	    }
		}
    
	/***********************Call algorithms and begin search***************************/
    
	System.out.println("Obstacles located at:");
	for(Point2D p : env.obstacles) {
	    System.out.println("x: " + p.x() + " - y: " + p.y() );
	}
	System.out.println("\n\n");
	System.out.println("Dirt located at:");
	for(Point2D p : state.dirts) {
	    System.out.println("x: " + p.x() + " - y: " + p.y() );
	}
	System.out.println("\n");

	//Initialize the searching algorithm and find optimal path.
	state.location = new Point2D(env.home.x(), env.home.y());
	Search searcher = new Astar(env);
	route = searcher.search(state);
	return;
    }

    public String nextAction(Collection<String> percepts) {
		
        if(route.empty()) return null;
	   return route.pop();
	}
}
