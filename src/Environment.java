import java.util.*;

public class Environment{
    
    public Environment() {
    	obstacles = new ArrayList<Point2D>();
    }
    public List<Point2D> obstacles;
    
    // r is rows, c i columns
    public int r, c;
    
    // Home
    public Point2D home;
    
    public boolean at_home(Point2D location)
    {
        return home.equals(location);
    }
}