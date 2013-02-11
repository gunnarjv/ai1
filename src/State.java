import java.util.*;

public class State 
{
	public State(boolean ON, Point2D location, int direction, List<Point2D> dirts)
	{
		this.ON = ON;
		this.location = location;
		this.direction = direction;
		this.dirts = dirts;
	}

	public State(){

		dirts = new ArrayList<Point2D>();
	}

	public boolean ON = false;
    //Current coordinates
	public Point2D location;
    // 0 is North. We will use modular arithmetic for directions.
	public int direction;
    //List of dirt coordinates
	public List<Point2D> dirts;

	/**************Functions***********************/

	public List<String> get_legal_moves(Environment env)
	{
		List<String> moves = new ArrayList<String>();

		if(!ON)
		{
			moves.add("TURN_ON");
			return moves;
		}

		// Suck, turning is always legal.
		//if(dirts.contains(location)) 
		moves.add("SUCK");

		moves.add("TURN_RIGHT");
		moves.add("TURN_LEFT");
		// We know we must be ON.

		//if(env.home.equals(location) && dirts.size() == 0) 
		moves.add("TURN_OFF");

		//If Go is inside of boundaries and not facing an obstacle add GO                
		switch(direction)
		{
		    case 0: // North
			if(location.y() == env.c - 1 || env.obstacles.contains(new Point2D(location.x(), location.y() + 1)))//If in northmost row, or obstacle 
			break;
			else
				moves.add("GO");
			break;
		    case 1: // East
			//If we are at the rightmost location
			if(location.x() == env.r - 1 || env.obstacles.contains(new Point2D(location.x() + 1, location.y()))) //If in eastmost column, or obstacle in front.
			break;
			else
				moves.add("GO");
			break;
		    case 2: // South
			if(location.y() == 0 || env.obstacles.contains( new Point2D(location.x() , location.y() - 1 )))//If at bottom row, or obstacle in front.
			break;
			else
				moves.add("GO");
			break;
		    case 3: // West
			if(location.x() == 0 || env.obstacles.contains( new Point2D(location.x() - 1, location.y())))//If at westmost column, or obstacle in front.
			break;
			else
				moves.add("GO");
			break;
			default:
			System.out.println("Something went wrong when adding GO to the list of moves :/");
		}
		return moves;
	}

	public State next_state(String move)
	{
		if(move == "TURN_ON")
		{
		    //Turn the robot on
			return new State(true, new Point2D(location.x(),location.y()), direction, dirts);
		}
		if(move == "TURN_RIGHT")
		{
		    //Create a new state the same as the old but change direction
			return new State(true, new Point2D( location.x(),location.y() ), (direction + 1) % 4, dirts);
		}
		if(move == "TURN_LEFT")
		{
		    //Create a new state the same as the old but change direction
		    //For negative numbers take modulo add 3 and take modulo again
			int newDirection = (((direction-1) % 4) + 4) % 4;
			return new State(true, new Point2D(location.x(),location.y()), newDirection, dirts);
		}
		if(move == "GO")
		{
		    //Add one to the direction the robot is facing
			if(direction == 0)
				return new State(true,new Point2D(location.x(),location.y() + 1), direction, dirts);
			if(direction == 1)
				return new State(true,new Point2D(location.x() + 1,location.y()), direction, dirts);
			if(direction == 2)
				return new State(true,new Point2D(location.x(),location.y() - 1), direction, dirts);
			if(direction == 3)
				return new State(true,new Point2D(location.x() - 1,location.y()), direction, dirts);
		}
		if(move == "SUCK")
		{
		    //Remove the dirt at point location from the list of dirts
			Point2D newPoint = new Point2D(location.x(),location.y());

			if(dirts.contains(newPoint)) {
			//Creating a new instance of our dirt list in memory so the nodes won't all reference the same dirt list.
				List<Point2D> newDirts = new ArrayList<Point2D>();
				for(Point2D d : dirts) newDirts.add(new Point2D(d.x(), d.y()));
					Boolean didWeSuck = newDirts.remove(newPoint);
				return new State(true, newPoint, direction, newDirts);
			}
			else
			//return new State(true, newPoint, direction, dirts)
				return this;
		}
		else //if(move == "TURN_OFF")
		{
		    //Turn the robot off
			return new State(false, new Point2D(location.x(),location.y()), direction, dirts);
		}
	}

	public boolean equals(Object other) {

		if(other == this) return true;
		if(other == null) return false;
		if(other.getClass() != this.getClass()) return false;
		State that  = (State) other;
		return this.ON == that.ON && this.location == that.location && this.direction == that.direction && this.dirts.equals(that.dirts);
		
	}

}
