public class Node
{
	public Node(State state, Node parent, String move)
	{
		this.state = state;
		this.parent = parent;
		this.move = move;
	}

    public State state;
    public Node parent;
    public int cost;
    public String move;
}