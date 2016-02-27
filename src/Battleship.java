/**
 * @ AUTHOR NAME HERE
 * @ Starter Code By Guocheng
 *
 * 2016-01-30
 * For: Purdue Hackers - Battleship
 * Battleship Client
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Battleship {
	public static String API_KEY = "605363735"; ///////// PUT YOUR API KEY HERE /////////
	public static String GAME_SERVER = "battleshipgs.purduehackers.com";

	//////////////////////////////////////  PUT YOUR CODE HERE //////////////////////////////////////

	char[] letters;
	int[][] grid;
	int[][] pGrid;
	Ship[] enemyShips = new Ship[5];

	void placeShips(String opponentID) {
		initializeShips();

		// Fill Grid With -1s
		for(int i = 0; i < grid.length; i++) { for(int j = 0; j < grid[i].length; j++) grid[i][j] = -1; }

		// Place Ships
		ArrayList<String[]> coordList = generateNonConflicting();
		String[] destroyer = coordList.get(0);
		String[] submarine = coordList.get(1);
		String[] cruiser = coordList.get(2);
		String[] battleship = coordList.get(3);
		String[] carrier = coordList.get(4);	
		
		placeDestroyer(destroyer[0], destroyer[1]);
		placeSubmarine(submarine[0], submarine[1]);
		placeCruiser(cruiser[0], cruiser[1]);
		placeBattleship(battleship[0], battleship[1]);
		placeCarrier(carrier[0], carrier[1]);
	}
	
	public static ArrayList<String[]> generateNonConflicting() {
		boolean placed[][] = new boolean[8][8];
		ArrayList<String[]> coordList = new ArrayList<String[]>();
		
		String[] destroyer = null;
		String[] submarine = null;
		String[] cruiser = null;
		String[] battleship = null;
		String[] carrier = null;
		
		do {
			destroyer = generateRandomCoords(2);
		} while (isConflictingPlacement(destroyer, placed));
		placed = placeShip(destroyer, placed);
		coordList.add(destroyer);
		
		do {
			submarine = generateRandomCoords(3);
		} while (isConflictingPlacement(submarine, placed));
		placed = placeShip(submarine, placed);
		coordList.add(submarine);
		
		do {
			cruiser = generateRandomCoords(3);
		} while (isConflictingPlacement(cruiser, placed));
		placed = placeShip(cruiser, placed);
		coordList.add(cruiser);
		
		do {
			battleship = generateRandomCoords(4);
		} while (isConflictingPlacement(battleship, placed));
		placed = placeShip(battleship, placed);
		coordList.add(battleship);
		
		do {
			carrier = generateRandomCoords(5);
		} while (isConflictingPlacement(carrier, placed));
		placed = placeShip(carrier, placed);
		coordList.add(carrier);
		
		return coordList;
	}
	
	public static boolean[][] placeShip(String[] coords, boolean[][] board) {
		int row1 = Math.abs((int) ('A' - coords[0].charAt(0)));
		int col1 = Math.abs((int) ('0' - coords[0].charAt(1)));
		int row2 = Math.abs((int) ('A' - coords[1].charAt(0)));
		int col2 = Math.abs((int) ('0' - coords[1].charAt(1)));
		
		boolean vertical = (col1 == col2);
		
		if (vertical) {
			int size = row2 - row1;
			
			for (int i = 0; i < size + 1; i++) {
				board[row1 + i][col1] = true;
			}
			
		} else {
			int size = col2 - col1;
			
			for (int i = 0; i < size + 1; i++) {
				board[row1][col1 + i] = true;
			}
		}
		
		return board;
	}
	
	public static boolean isConflictingPlacement(String[] coords, boolean[][] board) {
		int row1 = Math.abs((int) ('A' - coords[0].charAt(0)));
		int col1 = Math.abs((int) ('0' - coords[0].charAt(1)));
		int row2 = Math.abs((int) ('A' - coords[1].charAt(0)));
		int col2 = Math.abs((int) ('0' - coords[1].charAt(1)));
		
		boolean vertical = (col1 == col2);
		
		if (vertical) {
			int size = row2 - row1;
			
			//check that path isn't obstructed
			for (int i = 0; i < size + 1; i++) {
				if (board[row1 + i][col1])
					return true;
			}
			
			//check touching above
			if (row1 > 0 && board[row1 - 1][col1]) {
				return true;
			}
			
			//check touching below
			if (row2 < 7 && board[row2 + 1][col1]) {
				return true;
			}
			
			//check touching left
			if (col1 > 0) {
				for (int i = 0; i < size + 1; i++) {
					if (board[row1 + i][col1 - 1])
						return true;
				}
			}
			
			//check touching right
			if (col1 < 7) {
				for (int i = 0; i < size + 1; i++) {
					if (board[row1 + i][col1 + 1])
						return true;
				}
			}
			
			return false;
		} else {
			int size = col2 - col1;
			
			//check that path isn't obstructed
			for (int i = 0; i < size + 1; i++) {
				if (board[row1][col1 + i])
					return true;
			}
			
			//check touching left
			if (col1 > 0 && board[row1][col1 - 1]) {
				return true;
			}
			
			//check touching right
			if (col2 < 7 && board[row1][col2 + 1]) {
				return true;
			}
			
			//check touching above
			if (row1 > 0) {
				for (int i = 0; i < size + 1; i++) {
					if (board[row1 - 1][col1 + i])
						return true;
				}
			}
			
			//check touching below
			if (row1 < 7) {
				for (int i = 0; i < size + 1; i++) {
					if (board[row1 + 1][col1 + i])
						return true;
				}
			}
			
			return false;
		}
	}
	
	public static String[] generateRandomCoords(int size) {
		String[] coords = new String[2];
		Random r = new Random();
		boolean vertical = (r.nextDouble() < 0.5);
		
		if (vertical) {
			int col = r.nextInt(8);
			int row1 = 0;
			
			do {
				row1 = r.nextInt(8);
			} while (row1 + (size - 1) > 7);
			
			int row2 = row1 + (size - 1);
			
			String coord1 = "" + ((char) ('A' + row1)) + col;
			String coord2 = "" + ((char) ('A' + row2)) + col;
			
			coords[0] = coord1;
			coords[1] = coord2;
		} else {
			int row = r.nextInt(8);
			int col1 = 0;
			
			do {
				col1 = r.nextInt(8);
			} while (col1 + (size - 1) > 7);
			
			int col2 = col1 + (size - 1);
			
			String coord1 = "" + ((char) ('A' + row)) + col1;
			String coord2 = "" + ((char) ('A' + row)) + col2;
			
			coords[0] = coord1;
			coords[1] = coord2;
		}
		
		return coords;
	}

	void makeMove() {
		generatePGrid();
		int iMax, jMax, max;
		iMax = jMax = max = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (pGrid[i][j] > max) {
					max = pGrid[i][j];
					iMax = i;
					jMax = j;
				}
			}
		}

		String wasHitSunkOrMiss = placeMove(this.letters[iMax] + String.valueOf(jMax));

		if (wasHitSunkOrMiss.equals("Hit") || 
				wasHitSunkOrMiss.equals("Sunk")) {
			this.grid[iMax][jMax] = 1;
		} else {
			this.grid[iMax][jMax] = 0;			
		}
		return;
	}

	void initializeShips() {
		enemyShips[0] = new Ship(2);
		enemyShips[1] = new Ship(3);
		enemyShips[2] = new Ship(3);
		enemyShips[3] = new Ship(4);
		enemyShips[4] = new Ship(5);
	}

	void generatePGrid() {
		pGrid = new int[8][8];
		for (Ship ship : enemyShips) {
			if (!ship.alive) 
				continue;
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					// can be placed tall
					if (j + ship.length < 8) {
						boolean check = true;
						for (int k = j; k < j + ship.length; k++)
							if (grid[i][k] == 0) {
								check = false;
								break;
							}
						if (check)
							for (int k = j; k < j + ship.length; k++)
								if (grid[i][k] == -1)
									pGrid[i][k]++;
					}
					// can be placed wide
					if (i + ship.length < 8) {
						boolean check = true;
						for (int k = i; k < i + ship.length; k++)
							if (grid[k][i] == 0) {
								check = false;
								break;
							}
						if (check)
							for (int k = i; k < i + ship.length; k++)
								if (grid[k][j] == -1)
									pGrid[k][j]++;
					}
				}
			}
		}
	}

	class Ship {
		boolean alive;
		int length;
		Ship(int length) {
			this.length = length;
			this.alive = true;
		}
	}

	////////////////////////////////////// ^^^^^ PUT YOUR CODE ABOVE HERE ^^^^^ //////////////////////////////////////

	Socket socket;
	String[] destroyer, submarine, cruiser, battleship, carrier;

	String dataPassthrough;
	String data;
	BufferedReader br;
	PrintWriter out;
	Boolean moveMade = false;

	public Battleship() {
		this.grid = new int[8][8];
		for(int i = 0; i < grid.length; i++) { for(int j = 0; j < grid[i].length; j++) grid[i][j] = -1; }
		this.letters = new char[] {'A','B','C','D','E','F','G','H'};

		destroyer = new String[] {"A0", "A0"};
		submarine = new String[] {"A0", "A0"};
		cruiser = new String[] {"A0", "A0"};
		battleship = new String[] {"A0", "A0"};
		carrier = new String[] {"A0", "A0"};
	}

	void connectToServer() {
		try {
			InetAddress addr = InetAddress.getByName(GAME_SERVER);
			socket = new Socket(addr, 23345);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

			out.print(API_KEY);
			out.flush();
			data = br.readLine();
		} catch (Exception e) {
			System.out.println("Error: when connecting to the server...");
			socket = null; 
		}

		if (data == null || data.contains("False")) {
			socket = null;
			System.out.println("Invalid API_KEY");
			System.exit(1); // Close Client
		}
	}



	public void gameMain() {
		while(true) {
			try {
				if (this.dataPassthrough == null) {
					this.data = this.br.readLine();
				}
				else {
					this.data = this.dataPassthrough;
					this.dataPassthrough = null;
				}
			} catch (IOException ioe) {
				System.out.println("IOException: in gameMain"); 
				ioe.printStackTrace();
			}
			if (this.data == null) {
				try { this.socket.close(); } 
				catch (IOException e) { System.out.println("Socket Close Error"); }
				return;
			}

			if (data.contains("Welcome")) {
				String[] welcomeMsg = this.data.split(":");
				placeShips(welcomeMsg[1]);
				if (data.contains("Destroyer")) { // Only Place Can Receive Double Message, Pass Through
					this.dataPassthrough = "Destroyer(2):";
				}
			} else if (data.contains("Destroyer")) {
				this.out.print(destroyer[0]);
				this.out.print(destroyer[1]);
				out.flush();
			} else if (data.contains("Submarine")) {
				this.out.print(submarine[0]);
				this.out.print(submarine[1]);
				out.flush();
			} else if (data.contains("Cruiser")) {
				this.out.print(cruiser[0]);
				this.out.print(cruiser[1]);
				out.flush();
			} else if (data.contains("Battleship")) {
				this.out.print(battleship[0]);
				this.out.print(battleship[1]);
				out.flush();
			} else if (data.contains("Carrier")) {
				this.out.print(carrier[0]);
				this.out.print(carrier[1]);
				out.flush();
			} else if (data.contains( "Enter")) {
				this.moveMade = false;
				this.makeMove();
			} else if (data.contains("Error" )) {
				System.out.println("Error: " + data);
				System.exit(1); // Exit sys when there is an error
			} else if (data.contains("Die" )) {
				System.out.println("Error: Your client was disconnected using the Game Viewer.");
				System.exit(1); // Close Client
			} else {
				System.out.println("Recieved Unknown Response:" + data);
				System.exit(1); // Exit sys when there is an unknown response
			}
		}
	}

	void placeDestroyer(String startPos, String endPos) {
		destroyer = new String[] {startPos.toUpperCase(), endPos.toUpperCase()}; 
	}

	void placeSubmarine(String startPos, String endPos) {
		submarine = new String[] {startPos.toUpperCase(), endPos.toUpperCase()}; 
	}

	void placeCruiser(String startPos, String endPos) {
		cruiser = new String[] {startPos.toUpperCase(), endPos.toUpperCase()}; 
	}

	void placeBattleship(String startPos, String endPos) {
		battleship = new String[] {startPos.toUpperCase(), endPos.toUpperCase()}; 
	}

	void placeCarrier(String startPos, String endPos) {
		carrier = new String[] {startPos.toUpperCase(), endPos.toUpperCase()}; 
	}

	String placeMove(String pos) {
		if(this.moveMade) { // Check if already made move this turn
			System.out.println("Error: Please Make Only 1 Move Per Turn.");
			System.exit(1); // Close Client
		}
		this.moveMade = true;

		this.out.print(pos);
		out.flush();
		try { data = this.br.readLine(); } 
		catch(Exception e) { System.out.println("No response after from the server after place the move"); }

		if (data.contains("Hit")) return "Hit";
		else if (data.contains("Sunk")) return "Sunk";
		else if (data.contains("Miss")) return "Miss";
		else {
			this.dataPassthrough = data;
			return "Miss";
		}
	}

	public static void main(String[] args) {
		Battleship bs = new Battleship();
		while(true) {
			bs.connectToServer();
			if (bs.socket != null) bs.gameMain();
		}	
	}
}

