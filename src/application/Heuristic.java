package application;

/*
 * The heuristic that chooses the next best grid tile with shortest
 * cost to current tile
*/

@SuppressWarnings("unused")
public class Heuristic {
	// the method that determines the hCost, fastest distance to target goal
	public static float compute(int currentX, int currentY, int targetX, int targetY){
		float dx = targetX - currentX;
		float dy = targetY - currentY;
		//**Pick one:
		//1. Pythagorean Theorem : 
		//   to determine fastest distance to target goal
		return (float) (Math.sqrt((dx*dx)+(dy*dy)));
		//2. Manhattan distance: 
		// since less expensive for computation?
		// return (float) Math.abs(displacementX) + Math.abs(displacementY);
	}

	public static float compute(Vertex current, Vertex target) {
		return compute(current.getX(), current.getY(), target.getX(), target.getY());
	}
}
