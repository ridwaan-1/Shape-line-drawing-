import java.awt.GridLayout;
import java.awt.Dimension;
import javax.swing.*;
import javax.swing.JColorChooser;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.border.LineBorder;


public class MainMenu extends JFrame{

	static JFrame frameMainMenu = new JFrame("Main Menu");
	static JSplitPane frameSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);//Split menu into 2 parts
	static JPanel panelMainMenu = new JPanel();//left panel containing all buttons
	static JPanel panelGrid = new JPanel();
	static Color lineColor = new Color(0, 0, 0);
	static Color shapeColor = new Color(96, 54, 1);
	static Color newColor = new Color(255,0,0);
	static Color Brown = new Color(173, 139, 115);
	static Color paleBrown = new Color(227, 202, 165);

	MainMenu() {

		panelMainMenu.setSize(100, 100);
		panelMainMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panelMainMenu.setLayout(new GridLayout(7 , 1 , 0 , 20));
		panelMainMenu.setBackground(paleBrown);
		
		JButton RegPolygon = new JButton("Draw A Regular Polygon");
		RegPolygon.setBorder(new LineBorder(Color.BLACK));
		RegPolygon.setBackground(Brown);
		RegPolygon.setToolTipText("Click to draw a regular polygon");

		JButton IrregPolygon = new JButton("Draw An Irregular Polygon");
		IrregPolygon.setBackground(Brown);
		IrregPolygon.setBorder(new LineBorder(Color.BLACK));
		IrregPolygon.setToolTipText("Click to draw an irregular polygon");
		
		JButton BrCircle = new JButton("Draw A Bresenham Circle");
		BrCircle.setBackground(Brown);
		BrCircle.setBorder(new LineBorder(Color.BLACK));
		BrCircle.setToolTipText("Click to draw a circle using Bresenham Midpoint Algorithm");
		
		JButton LineColorPicker = new JButton("Change the Color of line");
		LineColorPicker.setBackground(Brown);
		LineColorPicker.setBorder(new LineBorder(Color.BLACK));
		LineColorPicker.setToolTipText("Click to change colour of the line drawn");
		
		JButton FillColorPicker = new JButton("Change the Color of Polygon");
		FillColorPicker.setBackground(Brown);
		FillColorPicker.setBorder(new LineBorder(Color.BLACK));
		FillColorPicker.setToolTipText("Click to change colour of the shape");
		
		JButton LineDrawing = new JButton("Draw a line");
		LineDrawing.setBackground(Brown);
		LineDrawing.setBorder(new LineBorder(Color.BLACK));
		LineDrawing.setToolTipText("Click to draw a line");
		
		Dimension btnSize = new Dimension(200, 70);

		LineDrawing.setPreferredSize(btnSize);
		RegPolygon.setPreferredSize(btnSize);
		IrregPolygon.setPreferredSize(btnSize);
		BrCircle.setPreferredSize(btnSize);
		LineColorPicker.setPreferredSize(btnSize);
		FillColorPicker.setPreferredSize(btnSize);
		
		panelMainMenu.add(LineDrawing);
		panelMainMenu.add(IrregPolygon);
		panelMainMenu.add(RegPolygon);
		panelMainMenu.add(BrCircle);
		panelMainMenu.add(LineColorPicker);
		panelMainMenu.add(FillColorPicker);

		//add Grid Panel and splitting frame
		panelGrid.setBorder(BorderFactory.createLineBorder(Color.blue));
		frameSplit.setLeftComponent(panelMainMenu);
		frameSplit.setRightComponent(Line.display());
		frameMainMenu.setLocation(0, 0);
		frameMainMenu.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frameMainMenu.add(frameSplit);
		frameMainMenu.setVisible(true);

		// Add listener to button
		
		handlerMainMenu handlerMainMenu = new handlerMainMenu();

		BrCircle.addActionListener(handlerMainMenu);
		RegPolygon.addActionListener(handlerMainMenu);
		IrregPolygon.addActionListener(handlerMainMenu);
		LineColorPicker.addActionListener(handlerMainMenu);
		FillColorPicker.addActionListener(handlerMainMenu);
		LineDrawing.addActionListener(handlerMainMenu);


		// Change color of buttons on hovering 
		RegPolygon.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				RegPolygon.setBackground(paleBrown);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				RegPolygon.setBackground(Brown);
			}
		});

		IrregPolygon.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				IrregPolygon.setBackground(paleBrown);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				IrregPolygon.setBackground(Brown);
			}
		});

		BrCircle.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				BrCircle.setBackground(paleBrown);

			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				BrCircle.setBackground(Brown);
			}
		});
		
		LineColorPicker.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				LineColorPicker.setBackground(paleBrown);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				LineColorPicker.setBackground(Brown);
			}
		});
		
		FillColorPicker.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				FillColorPicker.setBackground(paleBrown);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				FillColorPicker.setBackground(Brown);
			}
		});

		LineDrawing.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				LineDrawing.setBackground(paleBrown);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				LineDrawing.setBackground(Brown);
			}
		});

	}
	

	// Action listener
	public static class handlerMainMenu implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String action = event.getActionCommand();
			try {
				if (action.equals("Draw a line")) {
					Line.setLineColour(lineColor);
					new MenuLine();
				}

				if (action.equals("Draw A Regular Polygon")) {
					JFrame frameRegularPolygonNumSides = new JFrame("RegPolygonNumSides");
					String numSidesString = JOptionPane.showInputDialog(frameRegularPolygonNumSides,"<html>Enter number of sides<br/>Right-click to choose the coordinate of the centre of the polygon<br/>Drag the cursor to resize the polygon accordingly<br>Release the mouse to draw</html>", null);
					int numSides = Integer.parseInt(numSidesString);
					
					RegularPolygon.setRegularPolygonNumSides(numSides);
					RegularPolygon.setRegularPolygonColor(lineColor, shapeColor);
					frameSplit.setRightComponent(RegularPolygon.display());

				}
				if (action.equals("Draw An Irregular Polygon")) {
					IrregularPolygon.setIrregularPolygonColor(lineColor, shapeColor);
					frameSplit.setRightComponent(IrregularPolygon.display());
					JOptionPane.showMessageDialog(null, "<html>Left Click to plot each coordinate of the polygon<br/>Right-click to construct polygon after plotting the coordinates", "How to?", JOptionPane.INFORMATION_MESSAGE);
				}
				
				if (action.equals("Draw A Bresenham Circle")) {
					MidPointCircle.setBrCircleColor(lineColor);
					frameSplit.setRightComponent(MidPointCircle.display());
					JOptionPane.showMessageDialog(null, "<html>Right-click to indicate the centre of the circle you wish to draw<br/>Drag the cursor to make it the size you wish for and then release to draw", "How to?", JOptionPane.INFORMATION_MESSAGE);
				}

				
				if (action.equals("Change the Color of line")) {
					 newColor = JColorChooser.showDialog(null, "Choose a line color", lineColor);
					 lineColor = newColor;
				}
				
				if (action.equals("Change the Color of Polygon")) {
					 newColor = JColorChooser.showDialog(null, "Choose a shape color", shapeColor);
					 shapeColor = newColor;
				}
			}

			catch (Throwable e) {
			}

		}
	}

	public static void main(String[] args) {
		new MainMenu();
	}

}
