import java.util.*;
import java.lang.Math;
import java.lang.Object.*;

public class Astar implements Search
{

	public Environment env;

	public Astar(Environment lu_env) {

		this.env = lu_env;
	}

	public java.util.Stack<String> search(State state) {
		
		PriorityQueue<Node> f = new PriorityQueue<Node>(20, Node.HeuristicCompare);
		//List<Node> explored = new ArrayList<Node>();
		HashSet<Node> f_hash = new HashSet<Node>();
		HashSet<Node> explored = new HashSet<Node>();

		Node root = new Node(state, null, null);
		root.cost = 0;

	  	// If the initial state is goal we are done.
		if(is_goal(root.state)) return new java.util.Stack<String>();
		
		f.add(root);
		f_hash.add(root);
		
		while(f.peek() != null)
		{
		//	System.out.println("bjarni");
			Node n = f.poll();
			f_hash.remove(n);

			if(!explored.contains(n)) {

				explored.add(n);
				State s = n.state;

				for (String m : s.get_legal_moves(env))
				{

					if(is_goal(n.state)){
						System.out.println("Queue size: " + f.size() + "\nExplored size: " + explored.size());
						return path(n);
					}	

					Node child = new Node(s.next_state(m), n, m);
					evalCost(child, n.cost);
					child.fCost = child.cost + heuristicEstimate(child);

					if(!f_hash.contains(child) && !explored.contains(child)) {
						f.add(child);
						f_hash.add(child);
					}
				}
			}
		}
	   	// We should never get here.
		System.out.println("Something has gone awry! The search returned no solution!");
		System.out.println("Exiting.");
		System.exit(1);
		return new java.util.Stack<String>(); 
	}


	private boolean is_goal(State state)
		{
			if(state.dirts.isEmpty() && env.at_home(state.location) && !state.ON)
				return true;
			return false;
		}

		private java.util.Stack<String> path(Node goal) {
			java.util.Stack<String> strat = new java.util.Stack<String>();
			strat.push(goal.move);
			Node next_node = goal.parent;

		   while(next_node.move != null) //While we are not asking root
		   {
		   	strat.push(next_node.move);
		   	next_node = next_node.parent;
		   }

		   return strat;
	}

	private void evalCost(Node m, int parentCost) {

		switch(m.move) {

			case "TURN_OFF":
			if(env.at_home(m.state.location))
				m.cost = 1 + (15 * m.state.dirts.size()) + parentCost;
			else
				m.cost = 100 + (15 * m.state.dirts.size()) + parentCost;
			break;
			case "SUCK":
			if(!m.parent.state.dirts.contains(m.state.location)) {
				m.cost = 5 + parentCost;
				break;
			}
			default:
			m.cost = 1 + parentCost;
		}
	}

	private int heuristicEstimate(Node n)
	{

		State s = n.state;
		int no_dirts = s.dirts.size();
		EdgeWeightedGraph G = new EdgeWeightedGraph(no_dirts);
		Point2D current_location = s.location;

		Point2D dirt_array[] = s.dirts.toArray(new Point2D[1]);

		if(no_dirts > 1)
		{	
			for(int i = 0; i < no_dirts; i++)
			{
				Point2D dirt_array_i = dirt_array[i];
				for(int j = i+1; j < no_dirts; j++)
				{
					//System.out.println("iteration:" + i + " and j is " + j);
					double manhattan = manhattan(dirt_array_i, dirt_array[j]);
					//System.out.println(manhattan);
					Edge edge = new Edge(i, j, manhattan);
					G.addEdge(edge);					
				}
			}
		}
		PrimMST prim = new PrimMST(G);

		Point2D nearestDirt = null;
		for(Point2D p: s.dirts)
		{
			if(nearestDirt == null)
				nearestDirt = p;
			else
			{
				if(manhattan(p, current_location) < manhattan(nearestDirt, current_location))
				{
					nearestDirt = p;
				}
			}
		}

		//System.out.println("There were " + no_dirts + " dirts. ");
		//System.out.println("The weight is then" + (int)prim.weight());
		if (nearestDirt != null)
		{

			int turning_cost = 0;
			if(nearestDirt.x() > n.state.location.x() && n.state.direction == 3)
			turning_cost++;
			else if(nearestDirt.x() < n.state.location.x() && n.state.direction == 1)
			turning_cost++;
			else if(nearestDirt.y() > n.state.location.y() && n.state.direction == 2)
			turning_cost++;
			else if(nearestDirt.y() < n.state.location.y() && n.state.direction == 0)
			turning_cost++;

			/*System.out.println("The manhattan to dirt is then " + manhattan(current_location, nearestDirt));
			System.out.println("The total cost is then" + (((int)prim.weight()) + no_dirts + manhattan(current_location, nearestDirt) + manhattan(current_location, env.home)));
			System.out.println("no_dirts: " + no_dirts);
			System.out.println("manhattan(current, nearestDirt" + manhattan(current_location, nearestDirt));
			System.out.println("manhattan(current, nearestDirt" + manhattan(current_location, env.home));				
			/**/
			return (int)prim.weight() + no_dirts + manhattan(current_location, nearestDirt) + manhattan(current_location, env.home) + turning_cost;
		}
		/**/
		
		else
		{
			int turning_cost = 0;
			if(env.home.x() > n.state.location.x() && n.state.direction == 3)
			turning_cost++;
			else if(env.home.x() < n.state.location.x() && n.state.direction == 1)
			turning_cost++;
			else if(env.home.y() > n.state.location.y() && n.state.direction == 2)
			turning_cost++;
			else if(env.home.y() < n.state.location.y() && n.state.direction == 0)
			turning_cost++;

			//System.out.println("The total cost is then " + manhattan(current_location, env.home));
			return manhattan(current_location, env.home) + turning_cost;
		}
		/**/

	}
	

	private int manhattan(Point2D p1, Point2D p2)
	{
		int xDist = Math.abs(p1.x() - p2.x());
		int yDist = Math.abs(p1.y() - p2.y());

		return xDist + yDist;


	}

	public static void main(String args[]) {

		Environment env = new Environment();
		List<Point2D> dirtlist = new ArrayList<Point2D>();
		List<Point2D> obstaclelist = new ArrayList<Point2D>();


		obstaclelist.add(new Point2D(1, 2));
		obstaclelist.add(new Point2D(3, 3));
		obstaclelist.add(new Point2D(3, 4));
		obstaclelist.add(new Point2D(3, 5));
		obstaclelist.add(new Point2D(5, 3));

		dirtlist.add(new Point2D(1, 3));
		dirtlist.add(new Point2D(2, 4));
		dirtlist.add(new Point2D(4, 1));
		dirtlist.add(new Point2D(3, 2));
		dirtlist.add(new Point2D(5, 5));
		dirtlist.add(new Point2D(20, 20));
		dirtlist.add(new Point2D(20, 19));

		State state = new State(false, new Point2D(1, 1), 0, dirtlist);

		env.r = 20;
		env.c = 20;
		env.home = new Point2D(1, 1);
		env.obstacles = obstaclelist;

		Stopwatch watch = new Stopwatch();
		Search searcher = new Astar(env);	

		java.util.Stack<String> moves = searcher.search(state);
		System.out.println(watch.elapsedTime());

		while(!moves.isEmpty()) {
			String s = moves.pop();
			System.out.println(s);
		}
	}

}
