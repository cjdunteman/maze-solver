import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * A* algorithm search
 *
 * You should fill the search() method of this class.
 */
public class AStarSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 *
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public AStarSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main a-star search algorithm.
	 *
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {

		// FILL THIS METHOD

		// explored list is a Boolean array that indicates if a state associated with a given position in the maze has already been explored.
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];
		// ...

		PriorityQueue<StateFValuePair> frontier = new PriorityQueue<StateFValuePair>();

		// initialize root state and add to frontier list
				State rootState = new State(maze.getPlayerSquare(), null, 0, 0);
				StateFValuePair root = new StateFValuePair(rootState, (double) rootState.getDepth() +
						calcHeuristic(rootState.getSquare(), maze.getGoalSquare()));
				frontier.add(root);
				maxSizeOfFrontier = 1;
				StateFValuePair currPair = null;

		while (!frontier.isEmpty()) {
			// extract the minimum stateFValuePair.
			currPair = frontier.poll();
			explored[currPair.getState().getX()][currPair.getState().getY()] = true;

			// maintain maxDepthSearched and noOfNodesExpanded
			if (maxDepthSearched <= currPair.getState().getDepth())
			{
				maxDepthSearched = currPair.getState().getDepth();
			}
			noOfNodesExpanded++;

			// if at goal update maze and return true
			if (currPair.getState().isGoal(maze))
			{
				State currState = currPair.getState();
				cost = currState.getGValue();
				currState = currState.getParent();

				while(currState.getParent() != null)
				{
					maze.setOneSquare(currState.getSquare(), '.');
					currState = currState.getParent();
				}

				return true;
			}

			ArrayList<State> successors = currPair.getState().getSuccessors(explored, maze);

			// iterate through successors and update frontier
			for (int size = successors.size() - 1; size >= 0; size--)
			{
				StateFValuePair newPair = new StateFValuePair(successors.get(size),
						(double) successors.get(size).getDepth() +
						calcHeuristic(successors.get(size).getSquare(), maze.getGoalSquare()));
				boolean switchPair = false;
				for (StateFValuePair frontierPair : frontier)
				{
					// if successor is in frontier
					if (newPair.getState().getX() == frontierPair.getState().getX() &&
							newPair.getState().getY() == frontierPair.getState().getY())
					{
						// if g(n) < g(m) remove m from Frontier and insert n in Frontier
						if(newPair.getState().getGValue() < frontierPair.getState().getGValue())
						{
							frontier.remove(frontierPair);
							frontier.add(newPair);
							switchPair = true;
							break;
						}
					}
				}
				if (!switchPair)
				{
					frontier.add(newPair);
				}
			}
			// maintain maxSizeOfFrontier
			if (maxSizeOfFrontier < frontier.size())
			{
				maxSizeOfFrontier = frontier.size();
			}
		}

		// no solution found
		return false;
	}

	/**
	 * Calculates Euclidean distance from the current position to the goal position
	 *
	 * @param s1
	 * @param s2
	 * @return Euclidean distance
	 */
	private double calcHeuristic(Square s1, Square s2)
	{
		return (double) Math.sqrt(Math.pow((s1.X - s2.X), 2) + Math.pow(s1.Y - s2.Y, 2));
	}
}
