import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class  DSDV extends JFrame implements ActionListener
{
	JLabel sourceLabel = new JLabel();
	JLabel destinationLabel = new JLabel();
	JTextField sourceTextField = new JTextField();
	JTextField destinationTextField = new JTextField();
	JButton findRouteButton = new JButton();
	JButton moveButton = new JButton();
	JComboBox list = new JComboBox();

	boolean move = true;
	Graph graph;
	static int N;
	Container _container;

	public static final String FIND_ACTION_COMMAND  = "Find";
	public static final String TABLE_ACTION_COMMAND = "Table";
	public static final String MOVE_ACTION_COMMAND = "Move";

	public DSDV()
	{
		init();
		setSize(800, 575);
		setVisible(true);
		setResizable(false);
		setTitle("Simulation of DSDV Protocol");
		while(true)
		{
			try{
				Thread.sleep(50);
			}catch(Exception e){}
			if(move) graph.repaint();
		}
	}

	//initilizes the DSDV 
	public void init()
	{	
		graph = new Graph(N);
		sourceLabel.setText("Source :");
		destinationLabel.setText("Destination :");
		findRouteButton.setText("Find Route");
		findRouteButton.setMnemonic('F');
		moveButton.setText("Stop");
		moveButton.setMnemonic('S');
		graph.setBounds(new Rectangle(0,10,793,400));
		sourceLabel.setBounds(new Rectangle(50,450,100,25));
		destinationLabel.setBounds(new Rectangle(50,500,100,25));
		sourceTextField.setBounds(new Rectangle(130,450,100,25));
		destinationTextField.setBounds(new Rectangle(130,500,100,25));
		findRouteButton.setBounds(new Rectangle(300,450,110,30));
		moveButton.setBounds(new Rectangle(560,500,100,30));
		list.setBounds(new Rectangle(550,450,120,30));
		list.addItem("Routing Table");
		for(int i=0;i<N;i++)
			list.addItem("H"+(i+1));

		sourceTextField.setFont(new Font("Vardana", Font.BOLD, 11));
		destinationTextField.setFont(new Font("Vardana", Font.BOLD, 11));
		findRouteButton.setFont(new Font("Vardana", Font.BOLD, 12));
		moveButton.setFont(new Font("Vardana", Font.BOLD, 12));
		list.setFont(new Font("Vardana", Font.BOLD, 12));

		sourceLabel.setForeground(Color.lightGray);
		destinationLabel.setForeground(Color.lightGray);

		Color bg = new Color(240,240,195);
		Color f = new Color(20, 0, 144);
		moveButton.setBackground(bg);
		findRouteButton.setBackground(bg);
		list.setBackground(bg);
		moveButton.setForeground(f);
		findRouteButton.setForeground(f);
		list.setForeground(f);

		setBackground(new Color(90,65,214));
		_container = this.getContentPane();
		_container.setBackground(new Color(90,65,214));
		_container.setLayout(null);

		_container.add(graph, null);
		_container.add(sourceLabel, null);
        _container.add(destinationLabel, null);
		_container.add(sourceTextField, null);
		_container.add(destinationTextField, null);
		_container.add(findRouteButton, null);
		_container.add(moveButton, null);
		_container.add(list, null);
		this.getRootPane().setDefaultButton(moveButton);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		findRouteButton.setActionCommand(FIND_ACTION_COMMAND);
		moveButton.setActionCommand(MOVE_ACTION_COMMAND);
		list.setActionCommand(TABLE_ACTION_COMMAND);
		findRouteButton.addActionListener(this);
		moveButton.addActionListener(this);
		list.addActionListener(this);
	}

	public void stopMove()
	{
		move = false;
		graph.move = false;
		moveButton.setText("Start");
	}

	public void startMove()
	{
		move = true;
		graph.move = true;
		moveButton.setText("Stop");
	}

	public void actionPerformed(ActionEvent event)
	{
		//Move Button pressed
		if(event.getActionCommand().equals(MOVE_ACTION_COMMAND))
		{
			if(move) stopMove();
			else startMove();
		}

		//Find Route Button Pressed
		if(event.getActionCommand().equals(FIND_ACTION_COMMAND))
		{
			String src = sourceTextField.getText();
			String dest = destinationTextField.getText();
			try
			{
				int s, d;
				try{
					s = Integer.parseInt(src.substring(1))-1;
					d = Integer.parseInt(dest.substring(1))-1;
				}catch(Exception e) {throw new Exception("Improper Source OR Destination Names");}

				if( !((src.substring(0,1)).equalsIgnoreCase("H") && s<N  && s>=0)) throw new Exception("Improper Source Name");
				if(!((dest.substring(0,1)).equalsIgnoreCase("H") && d<N && d>=0)) throw new Exception("Improper Destination Name");
				
				if(move) stopMove();
				graph.path = graph.path(s, d, graph.node(s));
				if(graph.path == null)	throw new Exception("There is no Path between H"+(s+1)+" and H"+(d+1) );
				graph.repaint();
			}
			catch(Exception e)
			{
				JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR...", JOptionPane.ERROR_MESSAGE);
			}
		}

		//Routing Table selected
		if(event.getActionCommand().equals(TABLE_ACTION_COMMAND))
		{
			if(list.getSelectedIndex() > 0)
			{	 
				if(move) stopMove();
				graph.showRoutingTable(list.getSelectedIndex()-1);
			}
			list.setSelectedIndex(0);
			graph.repaint();
		}
	}
	
	//main method takes the no of nodes and creates the DSDV object
	public static void main(String args[])
	{
		while(true)
		{
			try{
				N = Integer.parseInt(JOptionPane.showInputDialog("Enter Number of Nodes:"));
			}
			catch(Exception e)	{ JOptionPane.showMessageDialog(null, "default It is taken as 16 nodes", "Given data is Not a Number", JOptionPane.WARNING_MESSAGE);	 N =16; }
			if(N > 16)		{ JOptionPane.showMessageDialog(null, "Max 16 nodes can only posible", "No of Nodes", JOptionPane.INFORMATION_MESSAGE); N = 16; }
			else if(N < 2)		{ JOptionPane.showMessageDialog(null, "Min 2 nodes must be neaded", "No of Nodes", JOptionPane.INFORMATION_MESSAGE); N = 2; }
			else break;
		}
		DSDV _DSDV = new DSDV();
	}
}
