
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.event.MouseInputListener;

public class MidPointCircle {
	private Point cursorPoint, clickPoint;
	private JLabel label;
	private static int x1, x2, y1, y2;
	private static Color BrCircleColor = (Color.green);

	public static void setBrCircleColor(Color c) {
		BrCircleColor = c;
	}

	public void updateCursorPoint(int x, int y) {
		cursorPoint.x = x;
		cursorPoint.y = y;
		updateLabel();
	}

	public void updateClickPoint(Point p) {
		clickPoint = p;
		updateLabel();
	}

	protected void updateLabel() {
		String text = "";

		if (clickPoint != null) {
			text += "Center of Circle (" + clickPoint.x + "," + clickPoint.y+ ")";
		}
		text += "Cursor Point (" + cursorPoint.x + "," + cursorPoint.y + ")";

		label.setText(text);
	}

	private void build(Container container) {
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
		Handler handler = new Handler(this);
		container.add(handler);

		cursorPoint = new Point();
		label = new JLabel();
		container.add(label);

		container.setBackground(MainMenu.paleBrown);
	}

	public static JInternalFrame display() { 
		JInternalFrame frame = new JInternalFrame("Bresenham Mid-Point Circle");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		MidPointCircle system= new MidPointCircle();
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
		private MidPointCircle system;
		private int radIncrement = 1;

		public Handler(MidPointCircle system) {
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
			//ANTI ALIASING TEXT AND VALUES-if antialising is off, the drawings become grainy and ugly
			
			int halfWidth = getWidth() / 2;//Half width of the display
			int halfHeight = getHeight() / 2;//Half Height of the display

			AffineTransform save = ga.getTransform();
			//The AffineTransform class represents a 2D affinetransform that performs a linear mapping from 2D coordinates to other 2Dcoordinates that preserves the "straightness" and"parallelness" of lines. 
			ga.translate(halfWidth, halfHeight);

			Graph.drawGraph(ga, scale);
			
			ga.setTransform(save);

			ga.setPaint(BrCircleColor);

			double x, y, P;
			double r = Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));  //x2 is given by mouse released
			r = r / radIncrement;

			x = 0;
			y = r;
			P = 1 - r;

			while (x < y) {
				if (P < 0) {
					x = x + 1;
					P = P + (x * 2) + 1;
				}

				else {
					x = x + 1;
					y = y - 1;
					P = P + (x * 2) + 1 - (y * 2);
				}

				ga.draw(new Line2D.Double(x + x1, y + y1, x + x1, y + y1));
				ga.draw(new Line2D.Double(y + x1, x + y1, y + x1, x + y1));
				ga.draw(new Line2D.Double(y + x1, -x + y1, y + x1, -x + y1));
				ga.draw(new Line2D.Double(x + x1, -y + y1, x + x1, -y + y1));
				ga.draw(new Line2D.Double(-x + x1, -y + y1, -x + x1, -y + y1));
				ga.draw(new Line2D.Double(-y + x1, -x + y1, -y + x1, -x + y1));
				ga.draw(new Line2D.Double(-y + x1, x + y1, -y + x1, x + y1));
				ga.draw(new Line2D.Double(-x + x1, y + y1, -x + x1, y + y1));

			}
		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mouseMoved(MouseEvent e) {
			pointTranslate(e.getX(), e.getY());
			system.updateCursorPoint(virtual.x, virtual.y);
		}

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

					int width2 = getWidth() / 2;
					int height2 = getHeight() / 2;
					int oldX = x1;
					int oldY = y1;

					int translateX = Math.abs(width2 - oldX) / radIncrement;
					int translateY = Math.abs(height2 - oldY) / radIncrement;

					if (amount == 10) {
						if ((x1 <= width2) && (y1 <= height2)) {  //NW quadrant
							x1 = x1 + translateX;
							y1 = y1 + translateY;
							x2 = x2 + translateX;
							y2 = y2 + translateY;
						}

						else if ((x1 >= width2) && (y1 <= height2)) {  //NE
							x1 = x1 - translateX;
							y1 = y1 + translateY;
							x2 = x2 - translateX;
							y2 = y2 + translateY;
						}

						else if ((x1 <= width2) && (y1 >= height2)) { //SW
							x1 = x1 + translateX;
							y1 = y1 - translateY;
							x2 = x2 + translateX;
							y2 = y2 - translateY;
						}

						else if ((x1 >= width2) && (y1 >= height2)) { //SE
							x1 = x1 - translateX;
							y1 = y1 - translateY;
							x2 = x2 - translateX;
							y2 = y2 - translateY;
						}
					}

					else if (amount == -10) {
						if ((x1 <= width2) && (y1 <= height2)) {
							x1 = x1 - translateX;
							y1 = y1 - translateY;
							x2 = x2 - translateX;
							y2 = y2 - translateY;
						}

						else if ((x1 >= width2) && (y1 <= height2)) {
							x1 = x1 + translateX;
							y1 = y1 - translateY;
							x2 = x2 + translateX;
							y2 = y2 - translateY;
						}

						else if ((x1 <= width2) && (y1 >= height2)) {
							x1 = x1 - translateX;
							y1 = y1 + translateY;
							x2 = x2 - translateX;
							y2 = y2 + translateY;
						}

						else if ((x1 >= width2) && (y1 >= height2)) {
							x1 = x1 + translateX;
							y1 = y1 + translateY;
							x2 = x2 + translateX;
							y2 = y2 + translateY;
						}
					}
				}
			}
			repaint();
		}
	}
}
