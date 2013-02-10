import java.util.*;

public class BFS implements Search
{
	Queue<Node> f = new Queue<Node>();

    public Stack<String> search(Environment env, State state)
    {
       Stack<String> strat = new Stack<String>();

       Node root = new Node(state, null, null);

       // If the initial state is goal we are done.
       if(is_goal(root))
       		return strategy;

       while(!f.isEmpty())
       {
       		Node n = f.pop();
       		State s = n.state;

       		for (String c: s.get_legal_moves())
       		{
       			Node child = new Node(s.next_state(c), n, c);
       			


       			f.add(c);       			
       		}
       }
       return strategy;
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

}
