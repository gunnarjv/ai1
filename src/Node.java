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

	public boolean equals(Object other) {
		
		//System.out.println("Whoop");
		if(other == this) return true;
		if(other == null) return false;
		if(other.getClass() != this.getClass()) return false;
		Node that = (Node) other;
		if(!this.state.equals(that.state)) return false;
		return this.fCost == that.fCost;

	}

	public static final Comparator<Node> HeuristicCompare = new HeuristicCompare();
    public State state;
    public Node parent;
    public int cost, fCost, id;
    public String move;
}