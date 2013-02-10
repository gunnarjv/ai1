import java.util.*;

public class BFS implements Search
{

  public Environment env;

  public BFS(Environment lu_env) {

    this.env = lu_env;

  }

    public Stack<String> search(State state)
    {
	     Queue<Node> f = new LinkedList<Node>();
       Node root = new Node(state, null, null);

       // If the initial state is goal we are done.
       if(is_goal(root.state))
       		return new Stack<String>();

       f.offer(root);

       while(f.peek() != null)
       {
       		Node n = f.remove();
       		State s = n.state;

       		for (String m: s.get_legal_moves(env))
       		{
       			Node child = new Node(s.next_state(m), n, m);
       			
       			if(is_goal(child.state))
       				return path(child);
       			else
	       			f.add(child);
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
       if(state.dirts.isEmpty() && env.at_home(state.location))
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


      dirtlist.add(new Point2D(0, 1));
      dirtlist.add(new Point2D(0, 0));


      State state = new State(true, new Point2D(1, 1), 3, dirtlist);

      env.r = 2;
      env.c = 2;
      env.home = new Point2D(1, 1);

      Search searcher = new BFS(env);
      Stack<String> moves = searcher.search(state);

      for(String s : moves) {
        System.out.println(s);
      }


    }

}
