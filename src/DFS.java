//*********************************************************************************//
//																				   //
//		DFS IS PRETTY MUCH THE SAME AS BFS, BUT USES A STACK INSTEAD OF A CUE.	   //
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

    public Stack<String> search(State state)
    {
	     Stack<Node> f = new Stack<Node>();
       Node root = new Node(state, null, null);

       // If the initial state is goal we are done.
       if(is_goal(root.state))
		return new Stack<String>();


       f.push(root);


       while(!f.empty())
       {
		Node n = f.pop();
		State s = n.state;

		for (String m : s.get_legal_moves(env))
		{
			Node child = new Node(s.next_state(m), n, m);
			
			if(is_goal(child.state)) {
				return path(child);
	    }
			else
				f.push(child);
		}
       }
       // We should never get here.
       System.out.println("Something has gone awry! The search returned no solution!");
       System.out.println("Exiting.");
       System.exit(1);
       return new Stack<String>();
    }

    private boolean is_goal(State state)
    {
       if(state.dirts.isEmpty() && env.at_home(state.location) && !state.ON)
		return true;
       return false;
    }

    private Stack<String> path(Node goal)
    {
       Stack<String> strat = new Stack<String>();

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

      dirtlist.add(new Point2D(0, 0));
      State state = new State(false, new Point2D(0, 0), 3, dirtlist);

      env.r = 1;
      env.c = 1;

      env.home = new Point2D(0, 0);
      env.obstacles = obstaclelist;

      Search searcher = new DFS(env);
      Stack<String> moves = searcher.search(state);

      while(!moves.isEmpty()) {
	String s = moves.pop();
	System.out.println(s);
      }


    }

}