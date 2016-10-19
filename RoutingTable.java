import java.util.*;
import java.awt.*;
import javax.swing.*;

class  RoutingTable extends JDialog
{
	//draw the table using table Information
	public RoutingTable(Vector table, String title)
	{	
		Vector colomnNames = new Vector(4);
		colomnNames.add("Destination");
		colomnNames.add("Next Hop");
		colomnNames.add("Metric");
		colomnNames.add("Sequence Number");
		JScrollPane jsp = new JScrollPane(new JTable(table, colomnNames));

		getContentPane().add(jsp, BorderLayout.CENTER);	
		setTitle(title);
		setModal(true);
		setSize(450, 44+16*table.size());
		setResizable(false);
		setLocation(150, 150);
		setVisible(true);
	}
}
