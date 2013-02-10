import java.util.*;

public class BFS implements Search
{
    public Stack<String> search(Environment env, State state)
    {
	   Queue<Node> f = new Queue<Node>();
       Node root = new Node(state, null, null);

       // If the initial state is goal we are done.
       if(is_goal(root))
       		return new Stack<String>();

       f.push(root);

       while(!f.isEmpty())
       {
       		Node n = f.pop();
       		State s = n.state;

       		for (String m: s.get_legal_moves())
       		{
       			Node child = new Node(s.next_state(m), n, m);
       			
       			if(is_goal(child.state))
       				return path(child.state);
       			else
	       			f.add(c);
       		}
       }
       // We should never get here.
       System.out.println("Something has gone awry! The search returned no solution!");
       System.out.println("Exiting.");
       System.exit();
    }

    private boolean is_goal(State state)
    {
       if(state.dirts.isEmpty() && env.at_home(state.location))
       		return true;
       return false;
    }

    private Stack<string> path(Node goal)
    {
       Stack<String> strat = new Stack<String>();

       strat.push(goal.move);
       next_state = goal.parent;

       while(next_state.move) //While we are not asking root
       {
       		strat.push(next_state.move);
       		next_state = next_state.parent
       }

       return strat;
    }

}
