import java.util.*;

public class State {
    public State(boolean ON, Point2D location, int direction, List<Point2D> dirts)
    {
        this.ON = ON;
        this.location = location;
        this.direction = direction;
        this.dirts = dirts;
    }
    
    public boolean ON = false;
    //Current coordinates
    public Point2D location;
    // 1 is North. We will use modular arithmetic for directions.
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
        moves.add("SUCK");
        moves.add("TURN_RIGHT");
        moves.add("TURN_LEFT");
        // We know we must be ON.
        moves.add("TURN_OFF");
        
        //If Go is inside of boundaries and not facing an obstacle add GO                
        switch(direction)
        {
            case 1: // North
                //If we are at the northmost location
                if(location.y() == env.c)
                {
                    //Create new point with yCord+1 and checks if there is an obstacle with that coordinate
                    if(env.obstacles.contains(new Point2D(location.x(),location.y()+1)))
                        break;
                }
                else
                    moves.add("GO");
            break;
            case 2: // East
            break;
            case 3: // South
            break;
            case 4: // West
            break;
        }
                    
        return moves;
    }
    
   /* public State next_state(String move)
    {
        
    }
    */
}