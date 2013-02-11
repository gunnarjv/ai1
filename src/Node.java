import java.util.Comparator;

public class Node implements Comparable<Node>
{
	public Node(State state, Node parent, String move)
	{
		this.state = state;
		this.parent = parent;
		this.move = move;
	}

	public int compareTo(Node that) {

		if(this.cost == that.cost) return 0;
		else if(this.cost < that.cost) return -1;
		else return 1;
	}

	private static class HeuristicCompare implements Comparator<Node>
	{
		public int compare(Node n1, Node n2)
		{
			if(n1.fCost > n2.fCost) return 1;
			else if(n1.fCost < n2.fCost) return -1;
			else return 0;
		}
	}

	public static final Comparator<Node> HeuristicCompare = new HeuristicCompare();
    public State state;
    public Node parent;
    public int cost, fCost;
    public String move;
}