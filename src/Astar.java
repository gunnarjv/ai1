import java.util.*;
import java.lang.Math;

public class Astar implements Search
{

	public Environment env;

	public Astar(Environment lu_env) {

		this.env = lu_env;
	}

	public java.util.Stack<String> search(State state)
	{
		PriorityQueue<Node> f = new PriorityQueue<Node>(20, Node.HeuristicCompare);

		Node root = new Node(state, null, null);
		root.cost = 0;

	  	// If the initial state is goal we are done.
		if(is_goal(root.state)) return new java.util.Stack<String>();

		f.add(root);
		int i = 0;
		while(f.peek() != null)
		{
			i++;
			Node n = f.poll();
		//System.out.println(n.fCost);
			State s = n.state;
			if(is_goal(n.state)){
				System.out.println(i);
				return path(n);
			}
			
			for (String m : s.get_legal_moves(env))
			{
				i++;
				Node child = new Node(s.next_state(m), n, m);
				evalCost(child, n.cost);
				child.fCost = child.cost + heuristicEstimate(child);

				f.add(child);
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

	private java.util.Stack<String> path(Node goal)
	{
		java.util.Stack<String> strat = new java.util.Stack<String>();

		strat.push(goal.move);
		Node next_node = goal.parent;

	   while(next_node.move != null) //While we are not asking root
	   {
	   	strat.push(next_node.move);
                //System.out.println("COOOOOST" + next_node.fCost + "  :  " + next_node.move);
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

		Point2D d[] = new Point2D[1];
		Point2D dirt_array[] = s.dirts.toArray(d);

		if(no_dirts > 1)
		{	
			for(int i = 0; i < no_dirts; i++)
			{
				for(int j = i+1; j < no_dirts; j++)
				{
					double manhattan = manhattan(dirt_array[i], dirt_array[j]);
					if(manhattan != 0)
					{
						Edge edge = new Edge(i, j, manhattan);
						G.addEdge(edge);					
					}
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

		if (nearestDirt != null)
			return (int)prim.weight() + no_dirts + manhattan(current_location, nearestDirt);
		else
			return (int)prim.weight() + no_dirts;
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

 		dirtlist.add(new Point2D(0, 1));
      	//dirtlist.add(new Point2D(3, 3));

		State state = new State(false, new Point2D(0, 0), 3, dirtlist);

		env.r = 3;
		env.c = 3;
		env.home = new Point2D(0, 0);
		env.obstacles = obstaclelist;

		Search searcher = new Astar(env);
		java.util.Stack<String> moves = searcher.search(state);

		while(!moves.isEmpty()) {
			String s = moves.pop();
			System.out.println(s);
		}
	}

}
