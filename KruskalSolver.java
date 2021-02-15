import java.util.Collections;
import java.util.Stack;

public class KruskalSolver {
	static int edgeCount = 0;
	static boolean DEBUG = true;
	static boolean PRINTEDGES = false;
	static boolean EDGES = true;
	static int N_size;
	static int dim3;
	static int dim2;
	static Cell[] cells;
	static byte[] maze;
	static Stack<Integer> unusedIndices = new Stack<Integer>();
	
	public static class Cell{
		public int ind;
		public Cell root;
		Cell(int coord){
			root = this;
			ind = coord;
		}
		
		// returns false if the current node has no legal new connections
		public boolean connectRandAdj(){
			//connects the current cell to a randomly selected adjacent, legal cell.
			//this operation connects the added cell to the "this" tree by setting them to have the same root
			
			//stack to only attempt an option once
			Stack<Integer> options = new Stack<>();
			options.add(0);
			options.add(1);
			options.add(2);
			options.add(3);
			options.add(4);
			options.add(5);
			options.add(6);
			options.add(7);
			Collections.shuffle(options);
			// formulas for calculating 4Space coordinates
			int t, z, y, x;
			t = Math.floorDiv( this.ind, dim3);
			z = Math.floorDiv( this.ind % dim3, dim2 );
			y = Math.floorDiv( (this.ind % dim3) % dim2 , N_size);
			x = this.ind % N_size;
			
			Cell.rootSkipSet(cells[ind]); // path-compress the current selected node for connection
			while(!options.empty()){      // every check is bounded by the walls of the maze
									      // if the bound check is passed, checked node gets path-compressed
										  // Connection is made if both nodes dont share a root post-compression
				int c = options.pop();
				switch(c){
					case 0: // down t
						if(t > 0 && !rootSkipSet(cells[this.ind-dim3]).root.equals(rootSkipSet(this))){
							rootSkipSet(cells[this.ind-dim3]).root = this.root; // union the trees together
							maze[this.ind] = breakAndSet(maze[this.ind], 7); // break wall of current
							maze[this.ind-dim3] = breakAndSet(maze[this.ind-dim3],6); // break corresponding of connected
							if(PRINTEDGES) formattedPrint(this.ind, this.ind-dim3, N_size); // print edge if active
							return true;
						}
						break;
					case 1: // up t
						if(t < N_size-1 && !rootSkipSet(cells[this.ind+dim3]).root.equals(rootSkipSet(this))){
							rootSkipSet(cells[this.ind+dim3]).root = this.root;
							maze[this.ind] = breakAndSet(maze[this.ind], 6);
							maze[this.ind+dim3] = breakAndSet(maze[this.ind+dim3],7);
							if(PRINTEDGES) formattedPrint(this.ind, this.ind+dim3, N_size);
							return true;
						}
						break;
					case 2: // down z
						if(z > 0 && !rootSkipSet(cells[this.ind-dim2]).root.equals(rootSkipSet(this))){
							rootSkipSet(cells[this.ind-dim2]).root = this.root;
							maze[this.ind] = breakAndSet(maze[this.ind], 5);
							maze[this.ind-dim2] = breakAndSet(maze[this.ind-dim2],4);
							if(PRINTEDGES) formattedPrint(this.ind, this.ind-dim2, N_size);
							return true;
						}
						break;
					case 3: // up z
						if(z < N_size-1 && !rootSkipSet(cells[this.ind+dim2]).root.equals(rootSkipSet(this))){
							rootSkipSet(cells[this.ind+dim2]).root = this.root;
							maze[this.ind] = breakAndSet(maze[this.ind], 4);
							maze[this.ind+dim2] = breakAndSet(maze[this.ind+dim2],5);
							if(PRINTEDGES) formattedPrint(this.ind, this.ind+dim2, N_size);
							return true;
						}
						break;
					case 4: // down y
						if(y > 0 && !rootSkipSet(cells[this.ind-N_size]).root.equals(rootSkipSet(this))){
							rootSkipSet(cells[this.ind-N_size]).root = this.root;
							maze[this.ind] = breakAndSet(maze[this.ind], 3);
							maze[this.ind-N_size] = breakAndSet(maze[this.ind-N_size],2);
							if(PRINTEDGES) formattedPrint(this.ind, this.ind-N_size, N_size);
							return true;
						}
						break;
					case 5: // up y
						if( y < N_size-1 && !rootSkipSet(cells[this.ind+N_size]).root.equals(rootSkipSet(this))){
							rootSkipSet(cells[this.ind+N_size]).root = this.root;
							maze[this.ind] = breakAndSet(maze[this.ind], 2);
							maze[this.ind+N_size] = breakAndSet(maze[this.ind+N_size],3);
							if(PRINTEDGES) formattedPrint(this.ind, this.ind+N_size, N_size);
							return true;
						}
						break;
					case 6: // down x
						if(x > 0 && !rootSkipSet(cells[this.ind-1]).root.equals(rootSkipSet(this))){
							rootSkipSet(cells[this.ind-1]).root = this.root;
							maze[this.ind] = breakAndSet(maze[this.ind], 1);
							maze[this.ind-1] = breakAndSet(maze[this.ind-1],0);
							if(PRINTEDGES) formattedPrint(this.ind, this.ind-1, N_size);
							return true;
						}
						break;
					case 7: // up x
						if(x < N_size-1 && !rootSkipSet(cells[this.ind+1]).root.equals(rootSkipSet(this))){
							rootSkipSet(cells[this.ind+1]).root = this.root;
							maze[this.ind] = breakAndSet(maze[this.ind], 0);
							maze[this.ind+1] = breakAndSet(maze[this.ind+1],1);
							if(PRINTEDGES) formattedPrint(this.ind, this.ind+1, N_size);
							return true;
						}
						break;
					default:
						break;
				}
			}
			return false;
		}
		
		public static Cell rootSkipSet(Cell u){
			if(u.root.equals(u)){
				return u; // if the current node is the root node, return the root
			}else{
				u.root = noSkip(u); // path-compress towards root
				return u.root ; // return root post-compression
			}
		}
		
		public static Cell noSkip(Cell u){
			Cell v = u.root; // set the temp to the initial root
			while(!v.equals(v.root)){ // travel to root
				v = v.root;
			}
			u.root = v; // set parent of initial node to be the root of the tree
			return v;  // return the root of the tree
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			Cell cell = (Cell) o;
			return ind == cell.ind;
		}
		
		@Override
		public int hashCode() {
			return ind;
		}
		
		@Override
		public String toString(){
			return ""+root.ind;
		}
	}
	
	public static class SPC{
		
		// -t +t -z +z -y +y -x +x
		//  0  1  2  3  4  5  6  7
		// bk fwd dw up lft rgt rev str
		//int s;
		String s;
		int bk, fwd, up, dw, lft, rgt, str, rev;
		SPC(byte b){
			s = Integer.toBinaryString(((b+256)%256));
			while(s.length() < 8){
				s = "0"+s;
			}
			try{
				bk = Character.getNumericValue(s.charAt(0))*2;
				fwd = Character.getNumericValue(s.charAt(1))*2;
				dw = Character.getNumericValue(s.charAt(2))*2;
				up = Character.getNumericValue(s.charAt(3))*2;
				lft = Character.getNumericValue(s.charAt(4))*2;
				rgt = Character.getNumericValue(s.charAt(5))*2;
				str = Character.getNumericValue(s.charAt(6))*2;
				rev = Character.getNumericValue(s.charAt(7))*2;
			}catch (StringIndexOutOfBoundsException e){
				System.out.println("String not large enough: "+s);
				System.out.println("Byte before string: "+ (byte) b);
			}
		}
		
		@Override
		public String toString(){
			return bk+""+fwd+""+up+""+dw+""+lft+""+rgt+""+str+""+rev;
		}
		
		public boolean isUsed(){
			return (bk+fwd+dw+up+lft+rgt+rev+str == 16);
		}
		
		public static void buildPath(SPC[][][][] maze){
			String path = "";
			int t = 0 , z = 0, y = 0, x =0;
			// -t +t -z +z -y +y -x +x
			//  0  1  2  3  4  5  6  7
			// bk fwd dw up lft rgt rev str
			while(!maze[0][0][0][0].isUsed()){
				if( maze[t][z][y][x].isUsed() && t != 0 && z != 0 && y != 0 && x != 0 ){
					System.out.println("Cycle detected.");
					break;
				}
				if(maze[t][z][y][x].fwd == 0){
					maze[t][z][y][x].fwd++;
					maze[++t][z][y][x].bk++;
					continue;
				}else if(maze[t][z][y][x].bk == 0){
					maze[t][z][y][x].bk++;
					maze[--t][z][y][x].fwd++;
					continue;
				}else if(maze[t][z][y][x].dw == 0){
					maze[t][z][y][x].dw++;
					maze[t][--z][y][x].up++;
					continue;
				}else if(maze[t][z][y][x].up == 0){
					maze[t][z][y][x].up++;
					maze[t][++z][y][x].dw++;
					continue;
				}else if(maze[t][z][y][x].rgt == 0){
					maze[t][z][y][x].rgt++;
					maze[t][z][++y][x].lft++;
					continue;
				}else if(maze[t][z][y][x].lft == 0){
					maze[t][z][y][x].lft++;
					maze[t][z][--y][x].rgt++;
					continue;
				}else if(maze[t][z][y][x].rev == 0){
					maze[t][z][y][x].rev++;
					maze[t][z][y][++x].str++;
					continue;
				}else if(maze[t][z][y][x].str == 0){
					maze[t][z][y][x].str++;
					maze[t][z][y][--x].rev++;
					continue;
				}else if(maze[t][z][y][x].fwd == 1){
					maze[t][z][y][x].fwd++;
					maze[++t][z][y][x].bk++;
					continue;
				}else if(maze[t][z][y][x].bk == 1){
					maze[t][z][y][x].bk++;
					maze[--t][z][y][x].fwd++;
					continue;
				}else if(maze[t][z][y][x].dw == 1){
					maze[t][z][y][x].dw++;
					maze[t][--z][y][x].up++;
					continue;
				}else if(maze[t][z][y][x].up == 1){
					maze[t][z][y][x].up++;
					maze[t][++z][y][x].dw++;
					continue;
				}else if(maze[t][z][y][x].rgt == 1){
					maze[t][z][y][x].rgt++;
					maze[t][z][++y][x].lft++;
					continue;
				}else if(maze[t][z][y][x].lft == 1){
					maze[t][z][y][x].lft++;
					maze[t][z][--y][x].rgt++;
					continue;
				}else if(maze[t][z][y][x].rev == 1){
					maze[t][z][y][x].rev++;
					maze[t][z][y][++x].str++;
					continue;
				}else if(maze[t][z][y][x].str == 1){
					maze[t][z][y][x].str++;
					maze[t][z][y][--x].rev++;
					continue;
				}
			}
			System.out.println("done with path building");
		}
	}
	
	public static void formattedPrint(int cur, int nex, int n){
		String s, t;
		s = Integer.toBinaryString(((maze[cur]+256)%256));
		while(s.length() < 8){
			s = "0"+s;
		}
		t = Integer.toBinaryString(((maze[nex]+256)%256));
		while(t.length() < 8){
			t = "0"+t;
		}
		
		System.out.println(translated(cur, n)+" --> "+translated(nex, n)+"\t"+cur+"  -->  "+nex);
		if(EDGES)edgeCount++;
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
	
	public static byte breakAndSet(byte b, int wall){
		return  (byte)(b & ( (byte)0xFF ^ ( (byte)1 << wall) ));
	}
	
	public static byte[] solve(int n){
		
		N_size = n;
		dim3 = (int)Math.pow(N_size,3);
		dim2 = (int)Math.pow(N_size,2);
		
		cells = new Cell[dim3*N_size];
		maze = new byte[dim3*N_size];
		
		for(int i = 0; i < dim3*N_size; i++){
			cells[i] = new Cell(i);
			maze[i] = (byte) 0xFF;
			unusedIndices.push(i);
		}
		
		//randomly sort all indices for random, quick access
		Collections.shuffle(unusedIndices);
		
		int curr;
		
		// EDGE TOTAL SHOULD BE V-1
		// take a random index for access
		// attempt edge inclusion
		for(int i = 0; i < cells.length-1; i++){
			if(!unusedIndices.empty()){
				curr = unusedIndices.pop();
				if(!cells[curr].connectRandAdj()){
					i--; // i represents edge count; dont increment i if an edge isnt made
				}else{
					if(EDGES)edgeCount++;
				}
			}else{
				if(DEBUG)System.out.println("Only "+i+" edges created.");
				break;
			}
		}
		
		for(int i = 0; i < cells.length; i++){
			if(cells[i].connectRandAdj()){
				if(EDGES)edgeCount++;
			}
		}
		
		return maze;
	}
	
	public static SPC[][][][] byteArrayToSPCArray(byte[] m, int n){
		SPC[][][][] spaces = new SPC[n][n][n][n];
		int[] coords = new int[4];
		// 0 = t .... 3 = x;
		int i  = 0;
		int d = 0;
		for(int W = 0; W < dim3*N_size; W++){
			i = W;
			for(int j = 3; j >= 0; j--){
				coords[d++] =	Math.floorDiv(i, (int)Math.pow(n, j));
				i = i % (int)Math.pow(n, j);
			}
			d = 0;
			spaces[coords[0]][coords[1]][coords[2]][coords[3]] = new SPC(m[W]);
		}
		return spaces;
	}
	
	public static void checkEverySpace(SPC[][][][] maze){
			for(int t = 0; t < N_size; t++){
				for(int z = 0; z < N_size; z++){
					for(int y = 0; y < N_size; y++){
						for(int x = 0; x < N_size; x++){
							if(!maze[t][z][y][x].toString().equals("22222222")){
								//System.out.println("Not a through path...");
								System.out.println(maze[t][z][y][x].toString()+" detected at ("+t+","+z+","+y+","+x+")");
							}
						}
					}
				}
			}
		return;
	}

	public static void main(String[] args){
		
		long s, e;
		s= System.nanoTime();
		byte[] m = solve(40);
		
		if(DEBUG){
			SPC[][][][] verifyMaze = byteArrayToSPCArray(m, N_size);
			SPC.buildPath(verifyMaze);
			checkEverySpace(verifyMaze);
		}
		
		System.out.println("Edge total : "+edgeCount);
		e= System.nanoTime();
		
		System.out.println("Runtime: "+(e-s)/1000000000.0);
	}
}
