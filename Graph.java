import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.geom.Point2D;
class  Graph extends JPanel
{
	static int N;
	String path = null;
	boolean move = true;
	int points[][];
	int edge[][];
	int d[];
	Random rand = new Random(1000);

	//Graph constructor take the N nodes of x and y coordinates 
	//and its directions using random number generation
	public Graph(int N)
	{
		this.N = N;
		points = new int[N][2];
		edge = new int[N][N];
		d = new int[N];

		for(int i=0;i<N;i++)
		{
			points[i][0] = 25 + rand.nextInt(750);
			points[i][1] = 15 + rand.nextInt(370);
			d[i] = rand.nextInt(8);
		}
	}

	//this Algorithm is used to find the path between src to dest
	//It returns all nodes in the path from source to destination
	//each node name length is tacken as 2 chars lenth
	public String path(int src, int dest, String p)
	{
		if(src == dest) return p;
		if(edge[src][dest] == 1) return p+node(dest);

		String path = null;
		for(int i =0; i<N; i++)
			if(edge[src][i] == 1) 
			{
				if(p.indexOf(node(i)) == -1)
				{
					String newpath = path(i, dest, p+node(i));
					if(newpath != null)
						if(path == null || newpath.length() < path.length())
							path = newpath;
				}
			}
		return path;
	}

	//to represent the node no in to string format
	//length represents 2 chars. If length is 1 char it adds 0 as prefix
	String node(int i)
	{
		if(i>9) return String.valueOf(i);
		else return "0"+String.valueOf(i);
	}

	//changes the Node Positions based on directions and calculates the edges
	public void changeNodesPos()
	{
		for(int i =0; i<N; i++)
		{
			int x=0;
			int y=0;
			switch(d[i])
			{
				case	0: x++; break;
				case	1: x++; y--;break;
				case	2: y--; break;
				case	3: x--; y--; break;
				case	4: x--; break;
				case	5: x--; y++; break;
				case	6: y++; break;
				case	7: x++; y++; break;
				default:
			}
			points[i][0] += x;
			points[i][1] += y;
			if(points[i][0] < 20 || points[i][0] > 775) { d[i] += 4; d[i] %= 8; }
			if(points[i][1] < 15 || points[i][1] > 385) { d[i] += 4; d[i] %= 8; }
		}

		//sets the edges	
		edge = new int[N][N];	
		for(int i = 0; i<N; i++)
			for(int j = i+1; j<N; j++)
			{
				int dest = (int)(Point2D.distance(points[i][0], points[i][1], points[j][0], points[j][1]));
				if(dest < 200)
					edge[i][j] = edge[j][i] = 1;
			}
	}

	//paints the graph if path is existed it draws the path
	public void paint(Graphics g)
	{
		if((path == null) && move) changeNodesPos();

		//clear panel & draw the border
		g.clearRect(0,0, 793, 400);
		g.setColor(new Color(148,146,206));
		g.fillRoundRect(3,0, 785, 399,10,10);

		//Draw Edges
		g.setColor(Color.lightGray);
		for(int i = 0; i<N; i++)			
			for(int j = i+1; j<N; j++)
				if(edge[i][j] == 1)
					g.drawLine(points[i][0], points[i][1], points[j][0], points[j][1]);
	
		//Draw nodes 
		g.setFont(new Font("Default", Font.BOLD, 12));
		for(int i = 0; i<N; i++)			
			drawNode(g, i, Color.white);
	
		//Draw path between source & destination
		if(path != null)
		{
			int p1, p2 = 0;
			g.setColor(Color.red);
			for(int i = 0; i < path.length()-3; i+=2)
			{
				p1 = Integer.parseInt(path.substring(i, i+2));
				p2 = Integer.parseInt(path.substring(i+2, i+4));
				g.drawLine(points[p1][0], points[p1][1], points[p2][0], points[p2][1]);
				drawNode(g, p1, Color.red);
			}
			drawNode(g, p2, Color.red);
			path = null;
		}
	}

	//Dosplay the node Text
	public void drawNode(Graphics g, int i, Color c)
	{
			int x =  points[i][0];
			int y =  points[i][1];		
			String node ="H" + (i+1);

			g.setColor(Color.blue);
			g.fillOval(x-13,y-13, 28, 28);

			g.setColor(c);
			if(i<9) g.drawString(node, x-7, y+5);
			else g.drawString(node, x-12, y+5);
	}

	//sets the table data in a vector and calls the RoutingTable as visible 
	public void showRoutingTable(int hop)
	{
		Vector table = new Vector(N);
		for(int j = 0; j<N; j++)
		{	
			Vector row = new Vector(4);
			row.add(" H"+(j+1));	

			String p = path(hop, j, "");
			if(p == null) 
			{
				row.add(" NONE ");
				row.add(" Infinity");
			}
			else 
			{		
				if (hop == j)	 row.add("  -");
				else row.add(" H"+(Integer.parseInt(p.substring(0, 2))+1));
				row.add( "  "+String.valueOf(p.length()/2) );
			}
			row.add(" S"+(rand.nextInt(1000))+"_H"+(j+1));
			table.add(row);
		}
		new RoutingTable(table, "H"+(hop+1)+" Routing Table"); 
	}
}
