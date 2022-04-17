
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.event.MouseInputListener;

public class RegularPolygon{
	private Point cursorPoint, clickPoint;
	private JLabel label;
	private static int x1, x2, y1, y2;

	private static int numSidesRegularPolygon = 5;
	private static Color RegularPolygonColor ;
	private static Color lineColor;
	
	public void updateCursorPoint(int x, int y) {
		cursorPoint.x = x;
		cursorPoint.y = y;
		updateLabel();
	}

	public void updateClickPoint(Point p) {
		clickPoint = p;
		updateLabel();
	}
	
	//Method used to display Center of Polygon and Cursor Point
		protected void updateLabel() {
			String text = "";

			if (clickPoint != null) {
				text += "Center of Polygon (" + clickPoint.x + "," + clickPoint.y+ ")                           ";
			}

			text += "Cursor Point (" + cursorPoint.x + "," + cursorPoint.y + ")";

			label.setText(text);
		}

	public static void setRegularPolygonColor(Color c1, Color c2) {
		lineColor = c1;
		RegularPolygonColor = c2;;
	}

	//Set n.o of sides of polygon
	public static void setRegularPolygonNumSides(int n) {
		numSidesRegularPolygon = n;
	}

	private void build(Container container) {
		//Building grid
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
		Handler handler = new Handler(this);
		container.add(handler);

		cursorPoint = new Point();
		label = new JLabel();
		container.add(label);
		
		container.setBackground(MainMenu.paleBrown);
	}

	public static JInternalFrame display() {
		JInternalFrame frame = new JInternalFrame("Regular Polygon");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		RegularPolygon system = new RegularPolygon();
		system.build(frame.getContentPane());

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setPreferredSize(new Dimension(700, 500));
		frame.setMaximumSize(new Dimension(screenSize));
		frame.setMinimumSize(new Dimension(700, 500));
		frame.setMaximizable(true);
		frame.pack();
		frame.setVisible(true);
		return frame;
	}

	public static class Handler extends JComponent implements MouseInputListener, MouseWheelListener {
		private static int scale = 10;
		private static double increment = 5;
		private Point virtual = null;
		private RegularPolygon system;
		private int radIncrement = 1;

		public Handler(RegularPolygon system) {
			this.system = system;
			addMouseListener(this);
			addMouseMotionListener(this);
			addMouseWheelListener(this);
		}

		private Point pointTranslate(int x, int y) {
			double virtualX = (x - (getWidth() / 2)) / increment;
			double virtualY = ((getHeight() / 2) - y) / increment;
			virtual = new Point((int) virtualX, (int) virtualY);
			return virtual;
		}

		protected void paintComponent(Graphics g) {//CREATING SCALABLE COORDINATE SYSTEM
			super.paintComponent(g);
			Graphics2D ga = (Graphics2D) g;

			ga.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			ga.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			//ANTI ALIASING TEXT AND VALUES
			
			int halfWidth = getWidth() / 2;//Half width of the display
			int halfHeight = getHeight() / 2;//Half Height of the display

			AffineTransform save = ga.getTransform();
			//The AffineTransform class represents a 2D affinetransform that performs a linear mapping from 2D coordinates to other 2Dcoordinates that preserves the "straightness" and"parallelness" of lines. 
			ga.translate(halfWidth, halfHeight);

			Graph.drawGraph(ga, scale);
			
			ga.setTransform(save);

			//Draws polygon
			Polygon p = new Polygon();
			int n = numSidesRegularPolygon;
			int[] X = new int[n];
			int[] Y = new int[n];

			double R = Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
			R = R / radIncrement;

			Color col2 = new Color(0, 51, 153);
			ga.setColor(col2);
			for (int i = 0; i < n; i++) {
				X[i] = (int) (x1 + R * Math.cos(2.0 * Math.PI * i / n));
				Y[i] = (int) (y1 + R * Math.sin(2.0 * Math.PI * i / n));
				p.addPoint(X[i], Y[i]);

				pointTranslate(X[i], Y[i]);
				ga.drawString(("(" + virtual.x + "," + virtual.y + ")"), X[i], Y[i]);
			}
			

			ga.setColor(RegularPolygonColor);
			ga.fill(p);
			ga.setColor(lineColor);
			ga.setStroke(new BasicStroke(1.0f));
			ga.draw(p);
			
			
		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		//Updates Cursor point value each time cursor is moved
		public void mouseMoved(MouseEvent e) {
			pointTranslate(e.getX(), e.getY());
			system.updateCursorPoint(virtual.x, virtual.y);
		}

		//Gets Center of polygon
		public void mousePressed(MouseEvent e) {
			x1 = e.getX();
			y1 = e.getY();

			pointTranslate(e.getX(), e.getY());
			system.updateClickPoint(virtual);
		}

		public void mouseDragged(MouseEvent e) {
			x2 = e.getX();
			y2 = e.getY();
			repaint();
		}

		//Zoom in/zoom out
		public void mouseWheelMoved(MouseWheelEvent e) {
			if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
				int amount = e.getWheelRotation() * 10;

				if ((scale == 10) && (amount == -10)) {
					return;
				}

				if ((scale == 150) && (amount == 10)) {
					return;
				}

				else {
					scale += amount;
					increment = 50.0 / scale;
					radIncrement += (amount / 10);

					int wid2 = getWidth() / 2;
					int hei2 = getHeight() / 2;
					int oldX = x1;
					int oldY = y1;

					int transX = Math.abs(wid2 - oldX) / radIncrement;
					int transY = Math.abs(hei2 - oldY) / radIncrement;

					if (amount == 10) {
						if ((x1 <= wid2) && (y1 <= hei2)) {
							x1 = x1 + transX;
							y1 = y1 + transY;
							x2 = x2 + transX;
							y2 = y2 + transY;
						}

						else if ((x1 >= wid2) && (y1 <= hei2)) {
							x1 = x1 - transX;
							y1 = y1 + transY;
							x2 = x2 - transX;
							y2 = y2 + transY;
						}

						else if ((x1 <= wid2) && (y1 >= hei2)) {
							x1 = x1 + transX;
							y1 = y1 - transY;
							x2 = x2 + transX;
							y2 = y2 - transY;
						}

						else if ((x1 >= wid2) && (y1 >= hei2)) {
							x1 = x1 - transX;
							y1 = y1 - transY;
							x2 = x2 - transX;
							y2 = y2 - transY;
						}
					}

					else if (amount == -10) {
						if ((x1 <= wid2) && (y1 <= hei2)) {
							x1 = x1 - transX;
							y1 = y1 - transY;
							x2 = x2 - transX;
							y2 = y2 - transY;
						}

						else if ((x1 >= wid2) && (y1 <= hei2)) {
							x1 = x1 + transX;
							y1 = y1 - transY;
							x2 = x2 + transX;
							y2 = y2 - transY;
						}

						else if ((x1 <= wid2) && (y1 >= hei2)) {
							x1 = x1 - transX;
							y1 = y1 + transY;
							x2 = x2 - transX;
							y2 = y2 + transY;
						}

						else if ((x1 >= wid2) && (y1 >= hei2)) {
							x1 = x1 + transX;
							y1 = y1 + transY;
							x2 = x2 + transX;
							y2 = y2 + transY;
						}
					}
				}
			}
			repaint();
		}
	}
}