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

    public State state;
    public Node parent;
    public int cost;
    public String move;
}