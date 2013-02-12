import java.util.*;

public class Uniform implements Search
{

	public Environment env;

	public Uniform(Environment lu_env) 
    {

		this.env = lu_env;
	}

	public java.util.Stack<String> search(State state)
	{
		PriorityQueue<Node> f = new PriorityQueue<Node>();
		HashSet<Node> f_hash = new HashSet<Node>();
		HashSet<Node> explored = new HashSet<Node>();


		Node root = new Node(state, null, null);
		root.cost = 0;

			 // If the initial state is goal we are done.
		if(is_goal(root.state))
			return new java.util.Stack<String>();

		f.add(root);
		f_hash.add(root);
		
		while(f.peek() != null)
		{
			
			Node n = f.poll();
			f_hash.remove(n);
				
			if(!explored.contains(n))
			{
				explored.add(n);
				State s = n.state;

				if(is_goal(n.state)) return path(n);
				

				for (String m : s.get_legal_moves(env))
				{
					Node child = new Node(s.next_state(m), n, m);
					evalCost(child, n.cost);

					if(!f_hash.contains(child) && !explored.contains(child)) 
                    {
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

	private java.util.Stack<String> path(Node goal)
	{
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

	private void evalCost(Node m, int parentCost) 
    {

		switch(m.move) 
        {

			case "TURN_OFF":
			if(env.at_home(m.state.location))
				m.cost = 1 + (15 * m.state.dirts.size()) + parentCost;
			else
				m.cost = 100 + (15 * m.state.dirts.size()) + parentCost;
			break;
			case "SUCK":
			if(!m.parent.state.dirts.contains(m.state.location)) 
            {
				m.cost = 5 + parentCost;
				break;
			}
			default:
			m.cost = 1 + parentCost;
		}
	}

	public static void main(String args[]) 
    {
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
        Search searcher = new Uniform(env); 

        java.util.Stack<String> moves = searcher.search(state);
        System.out.println(watch.elapsedTime());

        while(!moves.isEmpty()) {
            String s = moves.pop();
            System.out.println(s);
        }
    }

}