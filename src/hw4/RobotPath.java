package hw4;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * 
 * @author Sebastian Vang
 * 
 * Finds the shortest plan and the quickest plan.
 *
 */
public class RobotPath
{
	// attributes
	private Node[][] grid;
	private ArrayList<int[]> obsList = new ArrayList<>();
	private PriorityQueue<Node> queue = new PriorityQueue<>();
	private PriorityQueue<Node> queue2 = new PriorityQueue<>();
	private Node start;
	private Node end;
	private int dimX;
	private int dimY;
	public Node[][] sGrid;
	private Node[][] qGrid;
	
	public void readInput(String filename) throws IOException
	{
		//DONE
		File f = new File(filename);
		Scanner in = new Scanner(f);
		
		int row = 0;
		int col = 0;
		
		start = new Node("S");
		start.distance = 0;
		start.visited = true;
		end = new Node("D");
		
		while(in.hasNext())
		{
			String str = in.next();
			if(str.equals("nrows"))
			{
				row = in.nextInt();
			}
			if(str.equals("ncols"))
			{
				col = in.nextInt();
			}
			if(str.equals("start"))
			{
				start.x = in.nextInt();
				start.y = in.nextInt();
			}
			if(str.equals("dest"))
			{
				end.x = in.nextInt();
				end.y = in.nextInt();
			}
			if(str.equals("obstacles"))
			{
				while(in.hasNextInt())
				{
					int[] obs = new int[2];
					int r = in.nextInt();
					int c = in.nextInt();
					obs[0] = r;
					obs[1]= c;
					obsList.add(obs);
				}
			}
		}
		
		grid = new Node[row][col];
		sGrid = new Node[row][col];
		qGrid = new Node[row][col];
		
		dimX = row;
		dimY = col;
		
		for(int i = 0; i < grid.length; ++i)
		{
			for(int j = 0; j < grid[0].length; ++j)
			{
				grid[i][j] = new Node("0",i,j);
				sGrid[i][j] = new Node("0",i,j);
				qGrid[i][j] = new Node("0",i,j);
			}
		}

		grid[start.x][start.y] = start;
		grid[end.x][end.y] = end;
		
		for(int i = 0; i < obsList.size(); ++i)
		{
			int x = obsList.get(i)[0];
			int y = obsList.get(i)[1];
			grid[x][y] = new Node("*",x,y);
			sGrid[x][y] = new Node("*",x,y);
			qGrid[x][y] = new Node("*",x,y);
		}
		in.close();
	}
	
	public void planShortest()
	{
		//DONE
		/**
		 * The objective of this method is to use the input grid and identify all possible shortest plans.
			The length of a plan is defined by the number of moves made the Robot. A plan with minimal
			length is a shortest plan. Note that, there may be more than one shortest plan.
		 */
		
		Node sStart = sGrid[start.x][start.y];
		sStart.symbol = "S";
		sStart.distance = 0;
		sStart.visited = true;
		sStart.x = start.x;
		sStart.y = start.y;
		
		Node sEnd = sGrid[end.x][end.y];
		sEnd.symbol = "D";
		sEnd.x = end.x;
		sEnd.y = end.y;
		
		queue.add(sStart);
		while(queue.peek() != null && !queue.peek().equals(sEnd))
		{
			Node cur = queue.peek();
			queue.poll();
			ArrayList<Node> neighbors = new ArrayList<>();
			neighbors = getNeighbors(sGrid,cur);
			
			for(int i = 0; i < neighbors.size(); ++i)
			{
				Node neighbor = neighbors.get(i);
				String loc = getPrevLoc(neighbor,cur);
				
				int a = Math.abs(cur.x - neighbor.x);
				a = a*a;
				int b = Math.abs(cur.y - neighbor.y);
				b = b*b;
				int distance = a+b;
				
				// not visited
				if(neighbor.visited == false && loc.equals("S"))
				{
					sGrid[neighbor.x][neighbor.y].distance = cur.distance + distance;
					sGrid[neighbor.x][neighbor.y].visited = true;
					
					// need to get multiple neighbors.
					//sGrid[neighbor.x][neighbor.y].prev = cur;
					sGrid[neighbor.x][neighbor.y].north = cur;
					//cur.north = sGrid[neighbor.x][neighbor.y];

					
					queue.add(sGrid[neighbor.x][neighbor.y]);
					
				}
				else if(neighbor.visited == false && loc.equals("N"))
				{
					sGrid[neighbor.x][neighbor.y].distance = cur.distance + distance;
					sGrid[neighbor.x][neighbor.y].visited = true;
					
					// need to get multiple neighbors.
					//sGrid[neighbor.x][neighbor.y].prev = cur;
					sGrid[neighbor.x][neighbor.y].south = cur;
					//cur.south = sGrid[neighbor.x][neighbor.y];

					
					queue.add(sGrid[neighbor.x][neighbor.y]);
					
				}
				else if(neighbor.visited == false && loc.equals("W"))
				{
					sGrid[neighbor.x][neighbor.y].distance = cur.distance + distance;
					sGrid[neighbor.x][neighbor.y].visited = true;
					
					// need to get multiple neighbors.
					//sGrid[neighbor.x][neighbor.y].prev = cur;
					sGrid[neighbor.x][neighbor.y].east = cur;
					//cur.east = sGrid[neighbor.x][neighbor.y];

					
					queue.add(sGrid[neighbor.x][neighbor.y]);
					
				}
				else if(neighbor.visited == false && loc.equals("E"))
				{
					sGrid[neighbor.x][neighbor.y].distance = cur.distance + distance;
					sGrid[neighbor.x][neighbor.y].visited = true;
					
					// need to get multiple neighbors.
					//sGrid[neighbor.x][neighbor.y].prev = cur;
					sGrid[neighbor.x][neighbor.y].west = cur;
					//cur.west = sGrid[neighbor.x][neighbor.y];

					
					queue.add(sGrid[neighbor.x][neighbor.y]);
					
				}
				// already visited
				else
				{
					if(cur.distance + distance <= neighbor.distance && loc.equals("S"))
					{
						sGrid[neighbor.x][neighbor.y].distance = cur.distance + distance;
						//sGrid[neighbor.x][neighbor.y].prev = cur;
						sGrid[neighbor.x][neighbor.y].north = cur;
						//cur.north = sGrid[neighbor.x][neighbor.y];

					}
					if(cur.distance + distance <= neighbor.distance && loc.equals("N"))
					{
						sGrid[neighbor.x][neighbor.y].distance = cur.distance + distance;
						//sGrid[neighbor.x][neighbor.y].prev = cur;
						sGrid[neighbor.x][neighbor.y].south = cur;
						//cur.south = sGrid[neighbor.x][neighbor.y];

					}
					if(cur.distance + distance <= neighbor.distance && loc.equals("W"))
					{
						sGrid[neighbor.x][neighbor.y].distance = cur.distance + distance;
						//sGrid[neighbor.x][neighbor.y].prev = cur;
						sGrid[neighbor.x][neighbor.y].east = cur;
						//cur.east = sGrid[neighbor.x][neighbor.y];

					}
					if(cur.distance + distance <= neighbor.distance && loc.equals("E"))
					{
						sGrid[neighbor.x][neighbor.y].distance = cur.distance + distance;
						//sGrid[neighbor.x][neighbor.y].prev = cur;
						sGrid[neighbor.x][neighbor.y].west = cur;
						//cur.west = sGrid[neighbor.x][neighbor.y];

					}
				}
			}
		}
		grid = sGrid;
		Node node = sGrid[end.x][end.y];
		backTrack(node,sGrid);
		//Node cur = sGrid[end.x][end.y];
//		while(cur.prev != null && !cur.prev.equals(start))
//		{
//			int x = cur.x - cur.prev.x;
//			int y = cur.y - cur.prev.y;
//			
//			if(x == 1 && y == 0)
//			{
//				moveSouth(sGrid,cur.prev);
//			}
//			if(x == -1 && y == 0)
//			{
//				moveNorth(sGrid,cur.prev);
//			}
//			if(x == 0 && y == 1)
//			{
//				moveEast(sGrid,cur.prev);
//			}
//			if(x == 0 && y == -1)
//			{
//				moveWest(sGrid,cur.prev);
//			}
//			cur = cur.prev;
//		}
		queue.clear();
	}
	
	public void quickPlan()
	{
		//TODO
		/**
		 * The term “quick” in quickPlan refers to quickly finding a plan (not necessarily the shortest
			plan). The objective of this method is to use the input grid and identify a plan using the
			following strategy:
			(a) No Back-paddling. the plan must not include directives that will make the Robot to go
			through the same location more than ones.
			
			(b) Predictive Selection. Recall that, from each location l, the Robot can make multiple
			direct moves leading to (at most four) different locations. Let us refer to these locations
			as C, the set of choices. The quickPlan algorithm proceeds by considering a choice in C
			and if by using that choice, the algorithm can find a path leading to destination, then the
			3
			algorithm outputs the plan; otherwise, it considers a different choice in C (until there is
			no more choices to consider).
			The quickPlan algorithm considers these choices in specific order, such that any choice
			that is closer to the D is considered before any choice that is further from D. The
			closeness of measured using Euclidean distance 1 between the choice and the D.
			If there are two choices at same distance from D, then the choice with smaller value for
			the row will be considered to be closer to D; if the row values of the choices are also
			same, then the choice with the smaller value for the column will be considered to be
			closer to D.
			For instance, in our running example, if for the location (3, 3), there are 4 possible moves
			(e, w, n, s) leading to choices C = {(3, 4), (3, 2), (2, 3), (4, 3)}. The choice (4, 3) is closest
			to D = (8.7) and the next choice closest to D is (3.4), and so on. Hence, the algorithm
			will first consider the move to south from (3, 3) to look for a plan; if no plan is found
			using that choice, then the algorithm will consider the move to east from (3, 3) to look
			for a plan; and after than will consider the move to west and finally, the move to north.
			
			(c) Quick Stop. The search for a plan must terminate as soon as a plan is found.
		 */
		
		Node qStart = qGrid[start.x][start.y];
		qStart.symbol = "S";
		qStart.totalWeight=0;
		qStart.distance = 0;
		qStart.visited = true;
		qStart.x = start.x;
		qStart.y = start.y;
		
		Node qEnd = qGrid[end.x][end.y];
		qEnd.symbol = "D";
		qEnd.x = end.x;
		qEnd.y = end.y;
		
		queue.add(qStart);
		while(!queue.isEmpty() && queue.peek() != null && !queue.peek().equals(qEnd))
		{
			Node cur = queue.peek();
			ArrayList<Node> neighbors = new ArrayList<>();
			neighbors = getNeighbors(qGrid,cur);
			
			for(int i = 0; i < neighbors.size(); ++i)
			{
				Node neighbor = neighbors.get(i);
				
				// get the edge distance from current node to neighbor.
				int a = Math.abs(cur.x - neighbor.x);
				a = a*a;
				int b = Math.abs(cur.y - neighbor.y);
				b = b*b;
				
				int edgeWeight = a+b;
				
				int weightTotal = cur.totalWeight + edgeWeight;
				//int distance = a+b;
				
				// get the distance from cur to end.
				int a2 = Math.abs(cur.x - qEnd.x);
				int b2 = Math.abs(cur.x - qEnd.y);
				a2 = a2*a2;
				b = b2*b2;
				
				int distanceToEnd = a2+b2;
				
				if(!queue.contains(neighbor) && !queue2.contains(neighbor))
				{
					neighbor.prev = cur;
					neighbor.totalWeight = weightTotal;
					neighbor.distance = neighbor.totalWeight + distanceToEnd;
					
					queue.add(neighbor);
				}
				else
				{
					if(weightTotal < neighbor.totalWeight)
					{
						neighbor.prev = cur;
						neighbor.totalWeight = weightTotal;
						neighbor.distance = neighbor.totalWeight + distanceToEnd;
					}
					else if(weightTotal == neighbor.totalWeight)
					{
						if(cur.x < neighbor.x)
						{
							neighbor.prev = cur;
							neighbor.totalWeight = weightTotal;
							neighbor.distance = neighbor.totalWeight + distanceToEnd;
						}
					}
				}
			}
			queue.remove(cur);
			queue2.add(cur);
		}
		
		grid = qGrid;
		Node cur = qEnd;
		while(cur.prev != null && !cur.prev.equals(qStart))
		{
			int x = cur.x - cur.prev.x;
			int y = cur.y - cur.prev.y;
			
			if(x == 0 && y == 1)
			{
				moveFromWest(qGrid,cur.prev);
			}
			if(x == 0 && y == -1)
			{
				moveFromEast(qGrid,cur.prev);
				
			}
			if(x == -1 && y == 0)
			{
				moveFromSouth(qGrid,cur.prev);
			}
			if(x == 1 && y == 0)
			{
				moveFromNorth(qGrid,cur.prev);
				
			}
			cur = cur.prev;
		}
		
		queue.clear();
		queue2.clear();
	}
	
	public void output()
	{
		// backtrack to start
		for(int row = 0; row < grid.length; ++row)
		{
			for(int col = 0; col < grid[0].length; ++col)
			{
				System.out.printf("%5s",grid[row][col].symbol);
			}
			System.out.println();
		}
	}
	
	// Any other util methods
	
	private void moveFromSouth(Node[][] grid, Node cur)
	{
		//if(cur.x+1 < dimX)
		
			if(grid[cur.x][cur.y].symbol.equals("0"))
			{
				grid[cur.x][cur.y].symbol = "n";
			}
			else if(!grid[cur.x][cur.y].symbol.contains("n"))
			{
				grid[cur.x][cur.y].symbol += "n";
			}
		
	}
	private void moveFromNorth(Node[][] grid, Node cur)
	{
		//if(cur.x-1 > -1)
		
			if(grid[cur.x][cur.y].symbol.equals("0"))
			{
				grid[cur.x][cur.y].symbol = "s";
			}
			else if(!grid[cur.x][cur.y].symbol.contains("s"))
			{
				grid[cur.x][cur.y].symbol += "s";
			}
	}
	
	private void moveFromWest(Node[][] grid, Node cur)
	{
		//if(cur.y-1 > -1 )
		
			if(grid[cur.x][cur.y].symbol.equals("0"))
			{
				grid[cur.x][cur.y].symbol = "e";
			}
			else if(!grid[cur.x][cur.y].symbol.contains("e"))
			{
				grid[cur.x][cur.y].symbol += "e";
			}
		

	}
	
	private void moveFromEast(Node[][] grid, Node cur)
	{
		//if(cur.y+1 < dimY )
		
			if(grid[cur.x][cur.y].symbol.equals("0"))
			{
				grid[cur.x][cur.y].symbol = "w";
			}
			else if(!grid[cur.x][cur.y].symbol.contains("w"))
			{
				grid[cur.x][cur.y].symbol += "w";
			}
	}
	
	private ArrayList<Node> getNeighbors(Node[][] grid, Node node)
	{
		ArrayList<Node> list = new ArrayList<>();
		
		// north neighbor
		if(node.x-1 > -1 && grid[node.x-1][node.y].symbol != "*")
		{
			list.add(grid[node.x-1][node.y]);
		}
			
		// south neighbor
		if(node.x+1 < dimX && grid[node.x+1][node.y].symbol != "*")
		{
			list.add(grid[node.x+1][node.y]);
		}
		// east neighbor
		if(node.y+1 < dimY && grid[node.x][node.y+1].symbol != "*")
		{
			list.add(grid[node.x][node.y+1]);
		}
		// west neighbor
		if(node.y-1 > -1 && grid[node.x][node.y-1].symbol != "*")
		{
			list.add(grid[node.x][node.y-1]);
		}
		
		return list;
	}
	
	private String getPrevLoc(Node src, Node dest)
	{
		int x = src.x - dest.x;
		int y = src.y - dest.y;
		
		if(x == 1 && y == 0)
		{
			return "S";
		}
		if(x == -1 && y == 0)
		{
			return "N";
		}
		if(x == 0 && y == 1)
		{
			return "E";
		}
		if(x == 0 && y == -1)
		{
			return "W";
		}
		
		return "";
	}
	
	private void backTrack(Node cur, Node[][] grid)
	{
		Node start = grid[this.start.x][this.start.y];
		if(cur.equals(start))
		{
			return;
		}
		
		// look west
		if(cur.west != null && !cur.west.equals(start))
		{
			//moveWest(sGrid,cur);
			backTrack(cur.west,grid);
			moveFromWest(sGrid,cur.west);
			//move(sGrid,cur.west);
		}
		
		// look east
		if(cur.east != null && !cur.east.equals(start))
		{
			//moveEast(sGrid,cur);
			backTrack(cur.east,grid);
			moveFromEast(sGrid,cur.east);
			//move(sGrid,cur.east);
		}
		
		// look south
		if(cur.south != null && !cur.south.equals(start))
		{
			//moveSouth(sGrid,cur);
			backTrack(cur.south,grid);
			moveFromSouth(sGrid,cur.south);
			//move(sGrid,cur.south);
		}
		
		// look north
		if(cur.north != null && !cur.north.equals(start))
		{
			//moveNorth(sGrid,cur);
			backTrack(cur.north,grid);
			moveFromNorth(sGrid,cur.north);
			//move(sGrid,cur.north);
		}
	}
}

// Helper classes
class Node implements Comparable<Node>
{
	String symbol = "0";
	int distance = Integer.MAX_VALUE;
	int totalWeight = Integer.MAX_VALUE;
	boolean visited = false;
	Node prev;
	Node north;
	Node south;
	Node east;
	Node west;
	int x;
	int y;
	
	public Node() {}
	public Node(String symbol)
	{
		this.symbol = symbol;
	}
	public Node(String symbol, int x, int y)
	{
		this.symbol = symbol;
		this.x = x;
		this.y = y;
	}
	@Override
	public int compareTo(Node o) 
	{
		if(this.distance < o.distance)
		{
			return -1;
		}
		else
		{
			return 1;
		}
	}
}
