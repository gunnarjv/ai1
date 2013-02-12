//*********************************************************************************//
//																				   //
//		DFS IS PRETTY MUCH THE SAME AS BFS, BUT USES A java.util.Stack INSTEAD OF A CUE.	   //
//		THUS, WITHOUT HEURISTICS, WE'LL TRAVEL DOWN AN INFINITE BRANCH, AND		   //
//		NEVER RETURN HOME. :-(													   //
//																				   //
//***************************[-=Captain's comments=-]******************************//


import java.util.*;

public class DFS implements Search
{

	public Environment env;

	public DFS(Environment lu_env) {

		this.env = lu_env;

	}

	public java.util.Stack<String> search(State state)
	{
		java.util.Stack<Node> f = new java.util.Stack<Node>();
		HashSet<Node> f_hash = new HashSet<Node>();
		HashSet<Node> explored = new HashSet<Node>();


		Node root = new Node(state, null, null);

			 // If the initial state is goal we are done.
		if(is_goal(root.state)) return new java.util.Stack<String>();


		f.push(root);
		f_hash.add(root);


		while(!f.empty())
		{
			Node n = f.pop();
			f_hash.remove(n);

			if(!explored.contains(n)) {
				explored.add(n);
				State s = n.state;

				for (String m : s.get_legal_moves(env))
				{
					Node child = new Node(s.next_state(m), n, m);

					if(is_goal(child.state)) return path(child);
					else if (!f_hash.contains(child) && !explored.contains(child))
						f.push(child);
						//if(child.move.equals("TURN_ON") && child.state.ON) continue;
						//else if(child.move.equals("SUCK") && !child.state.dirts.contains(child.state.location)) continue;
						//else if(child.move.equals("TURN_OFF") && (!child.state.location.equals(env.home) || child.state.dirts.size() != 0)) continue;
					
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
		dirtlist.add(new Point2D(19, 3));
		dirtlist.add(new Point2D(19, 19));

		State state = new State(false, new Point2D(1, 1), 0, dirtlist);

		env.r = 20;
		env.c = 20;
		env.home = new Point2D(1, 1);
		env.obstacles = obstaclelist;

		Stopwatch watch = new Stopwatch();
		Search searcher = new DFS(env);	

		java.util.Stack<String> moves = searcher.search(state);

		while(!moves.isEmpty()) {
			String s = moves.pop();
			System.out.println(s);
		}

		System.out.println("time was " +watch.elapsedTime());
			}

		}