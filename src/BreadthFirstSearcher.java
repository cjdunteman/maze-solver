import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Breadth-First Search (BFS)
 *
 * You should fill the search() method of this class.
 */
public class BreadthFirstSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 *
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public BreadthFirstSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main breadth first search algorithm.
	 *
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {
		// explored list is a 2D Boolean array that indicates if a state associated with a given position in the maze has already been explored.
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];

		// ...

		// Queue implementing the Frontier list
		LinkedList<State> queue = new LinkedList<State>();

		queue.add(new State(maze.getPlayerSquare(), null, 0, 0));
		maxSizeOfFrontier = 1;
		State currState = null;

		while (!queue.isEmpty())
		{
			currState = queue.pop();
			// maintain maxDepthSearched
			if (maxDepthSearched <= currState.getDepth())
			{
				maxDepthSearched = currState.getDepth();
			}

			// if at goal update maze and return true
			if (currState.isGoal(maze))
			{
				cost = currState.getGValue();
				currState = currState.getParent();

				// update maze
				while(currState.getParent() != null)
				{
					maze.setOneSquare(currState.getSquare(), '.');
					currState = currState.getParent();
				}

				return true;
			}
			// maintain noOfNodesExpanded
			noOfNodesExpanded++;
			ArrayList<State> successors = currState.getSuccessors(explored, maze);
			for (int size = successors.size() - 1; size >= 0; size--)
			{
				State state = successors.get(size);
				queue.add(state);
				// state is in the frontier
				explored[state.getX()][state.getY()] = true;
			}
			// maintain maxSizeOfFrontier
			if (maxSizeOfFrontier < queue.size())
			{
				maxSizeOfFrontier = queue.size();
			}
		}
		return false;
		}
}
