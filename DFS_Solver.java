import java.io.File;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Stack;

public class DFS_Solver {
	
	/**  Author: Aidan Gray
	 *   Date: 1/28/2021
	 *   Randomly Generated Four Dimensional Maze via DFS implementation
	 *      Creates a randomly generated maze using depth-first traversal of random adjacent nodes
	 *      When all adjacent nodes are used, the current node is backtracked using a stack
	 *      The program terminates when all points have been included in the maze-tree
	 */
	
	static Stack<Integer> dfsVisited = new Stack<Integer>();
	static HashSet<Integer> unusedPoints = new HashSet<Integer>();

	public static void formattedPrint(int cur, int nex, int n){
		System.out.println(translated(cur, n)+" --> "+translated(nex, n));
	}
	
	public static String translated(int i, int n){
		String s = "(";
		for(int j = 3; j >= 0; j--){
			s = s + "" + Math.floorDiv(i, (int)Math.pow(n, j) ) + ",";
			i = i % (int)Math.pow(n, j);
		}
		s = s.substring(0,8)+")";
		return s;
	}
	
	public static void populateSet(int TOTAL_MAZE_POINTS){
		for(int i = 0; i < TOTAL_MAZE_POINTS; ++i){
			unusedPoints.add(i);
		}
	}
	
	public static byte breakAndSet(byte b, int wall){
		return  (byte)(b & ( (byte)0xFF ^ ( (byte)1 << wall) ));
	}
	
	public static void updateStructures(int nextPoint){
		unusedPoints.remove(nextPoint);
		dfsVisited.push(nextPoint);
		//formattedPrint(currPoint, nextPoint, n);
	}
	
	public static int goNext(byte[] maze, int currPoint, int n, HashSet<Integer> unusedPoints){
		int dim3 = (int)Math.pow(n,3);
		int dim2 = (int)Math.pow(n,2);
		int x, y, z, t, nextPoint;
		Integer ran = 0;
		t = Math.floorDiv( currPoint, dim3);
		z = Math.floorDiv( currPoint % dim3, dim2 );
		y = Math.floorDiv( (currPoint % dim3) % dim2 , n);
		x = currPoint % n;
		
		Stack<Integer> options = new Stack<Integer>();
		options.push(0);
		options.push(1);
		options.push(2);
		options.push(3);
		options.push(4);
		options.push(5);
		options.push(6);
		options.push(7);
		
		Collections.shuffle(options);
		
		while( !options.empty() ){
			ran = options.pop();
			//  7  6  5  4  3  2  1  0
			// -t +t -z +z -y +y -x +x
			switch(ran){
				case 0: // down t
					if(t > 0){ // if the current point has a value in the X place
						nextPoint =  currPoint - dim3; // t--
						if(unusedPoints.contains(nextPoint)){
							updateStructures(nextPoint);
							maze[currPoint] = breakAndSet(maze[currPoint], 7); // break wall TOO NEXT
							maze[nextPoint] = breakAndSet(maze[nextPoint], 6); // break wall FROM PREV
							return nextPoint;
						}
					}
					break;
				case 1: // up t
					if( t < n-1 ){
						nextPoint = currPoint + dim3; // t++
						if(unusedPoints.contains(nextPoint)){
							updateStructures(nextPoint);
							maze[currPoint] = breakAndSet(maze[currPoint], 6); // break wall TOO NEXT
							maze[nextPoint] = breakAndSet(maze[nextPoint], 7); // break wall FROM PREV
							return nextPoint;
						}
					}
					break;
				case 2: // down z
					if( z > 0 ){
						nextPoint = currPoint - dim2; // z--
						if(unusedPoints.contains(nextPoint)){
							updateStructures(nextPoint);
							maze[currPoint] = breakAndSet(maze[currPoint], 5); // break wall TOO NEXT
							maze[nextPoint] = breakAndSet(maze[nextPoint], 4); // break wall FROM PREV
							return nextPoint;
						}
					}
					break;
				case 3: // up z
					if( z < n-1 ){
						nextPoint = currPoint + dim2; // z++
						if(unusedPoints.contains(nextPoint)){
							updateStructures(nextPoint);
							maze[currPoint] = breakAndSet(maze[currPoint], 4); // break wall TOO NEXT
							maze[nextPoint] = breakAndSet(maze[nextPoint], 5); // break wall FROM PREV
							return nextPoint;
						}
					}
					break;
				case 4: // down y
					if( y > 0){
						nextPoint = currPoint - n; // y--
						if(unusedPoints.contains(nextPoint)){
							updateStructures(nextPoint);
							maze[currPoint] = breakAndSet(maze[currPoint], 3); // break wall TOO NEXT
							maze[nextPoint] = breakAndSet(maze[nextPoint], 2); // break wall FROM PREV
							return nextPoint;
						}
					}
					break;
				case 5: // up y
					if( y < n-1 ) {
						nextPoint = currPoint + n; // y++
						if(unusedPoints.contains(nextPoint)){
							updateStructures(nextPoint);
							maze[currPoint] = breakAndSet(maze[currPoint], 2); // break wall TOO NEXT
							maze[nextPoint] = breakAndSet(maze[nextPoint], 3); // break wall FROM PREV
							return nextPoint;
						}
					}
					break;
				case 6: // down x
					if( x > 0){
						nextPoint = currPoint-1; // x--
						if(unusedPoints.contains(nextPoint)){
							updateStructures(nextPoint);
							maze[currPoint] = breakAndSet(maze[currPoint], 1); // break wall TOO NEXT
							maze[nextPoint] = breakAndSet(maze[nextPoint], 0); // break wall FROM PREV
							return nextPoint;
						}
					}
					break;
				case 7: // up x
					if(x < n-1){
						nextPoint = currPoint+1; // x++
						if(unusedPoints.contains(nextPoint)){
							updateStructures(nextPoint);
							maze[currPoint] = breakAndSet(maze[currPoint], 0); // break wall TOO NEXT
							maze[nextPoint] = breakAndSet(maze[nextPoint], 1); // break wall FROM PREV
							return nextPoint;
						}
					}
					break;
				default:
					break;
			}
		}
		return -1;
	}
	
	public static int DFS(byte[] G, int n, int currPoint){
		if(unusedPoints.size() == 0){
			return -1;
		}
		// progress to next random unused child node
		int nextPoint = goNext(G, currPoint, n, unusedPoints);
		// if nextPoints are ALL illegal, goes back a depth and tries again
		if(nextPoint == -1){
			nextPoint = dfsVisited.pop();
		}
		return nextPoint;
	}
	
	public static byte[] solve(int n) {
		int TOTAL_MAZE_POINTS = (int) Math.pow(n, 4);
		byte[] maze = new byte[TOTAL_MAZE_POINTS];
		for(int i = 0; i < TOTAL_MAZE_POINTS; ++i){
			maze[i] = (byte)255;
		}
		populateSet(TOTAL_MAZE_POINTS);
		int startPoint = (int)(Math.random()*TOTAL_MAZE_POINTS);
		unusedPoints.remove(startPoint);
		dfsVisited.push(startPoint);
		int nextPoint = DFS(maze, n, startPoint);
		while(nextPoint != -1){
			nextPoint = DFS(maze, n, nextPoint);
		}
		return maze;
	}
	
	public static void main(String[] args){
		long startTime = System.nanoTime();
		byte[] maze = solve(40);
		long endTime = System.nanoTime();
		System.out.println("Elapsed time: "+ (endTime-startTime)/1000000000.0 + " seconds.");
		File outputfile = new File("maze.txt");
		try{
			FileOutputStream outputStream =  new FileOutputStream(outputfile);
			outputStream.write(maze);
			outputStream.close();
		}catch(Exception ex){
			System.out.println("idk");
		}
	}
}
