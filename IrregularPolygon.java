
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.event.MouseInputListener;
import javax.swing.*;

public class IrregularPolygon extends Frame {
	private static int[] X = new int[100];
	private static int[] Y = new int[100];
	private static int count, start;
	private static Color IrregularPolygonColor = new Color(0,0,0);
	private static Color lineColor;
	private static int paintLock = 1;
	private Point cursorPoint;
	private JLabel label;

	public static void setIrregularPolygonColor(Color c1, Color c2) {
		lineColor = c1;
		IrregularPolygonColor = c2;
	}

	public IrregularPolygon() {
		count = 0;
	}

	public void updateCursorPoint(int x, int y) {
		cursorPoint.x = x;
		cursorPoint.y = y;
		updateLabel();
	}

	protected void updateLabel() {
		String text = "The cursor is at (" + cursorPoint.x + ","+ cursorPoint.y + ")";
		label.setText(text);
	}
	
	private void build(Container container) {//container for main menu
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
		Handler handler = new Handler(this);
		container.add(handler);

		cursorPoint = new Point();
		label = new JLabel();
		container.add(label);

		container.setBackground(MainMenu.paleBrown);
	}

	public static JInternalFrame display() {
		JInternalFrame frame = new JInternalFrame("Irregular Polygon");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		IrregularPolygon system = new IrregularPolygon();
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

	public static class Handler extends JComponent implements MouseInputListener {
		private static int scale = 10;
		private static double increment = 5;
		private Point virtual = null;
		private IrregularPolygon system;

		public Handler(IrregularPolygon system) {
			this.system = system;
			addMouseListener(this);
			addMouseMotionListener(this);
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

			if (paintLock == 1) {
				Graphics2D gx = (Graphics2D) g;
				gx.setPaint(IrregularPolygonColor);
				for (int i = 0; i < count; i++) {
					gx.drawLine(X[i], Y[i], X[i], Y[i]);
					for (int j = 1; j <= 3; j++) {
						gx.drawLine(X[i] - j, Y[i], X[i] - j, Y[i]);
						gx.drawLine(X[i] + j, Y[i], X[i] + j, Y[i]);
						gx.drawLine(X[i], Y[i] + j, X[i], Y[i] + j);
						gx.drawLine(X[i], Y[i] - j, X[i], Y[i] - j);
						gx.drawString("" + (i + 1) + "", X[i] + 3, Y[i] + 3);
					}
				}
			}

			else {
				ga.setPaint(IrregularPolygonColor);
				start = 0;
				GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, X.length);
				polygon.moveTo(X[start], Y[start]);
				pointTranslate(X[start], Y[start]);
				ga.drawString(("(" + virtual.x + "," + virtual.y + ")"), X[start], Y[start]);

				for (int i = 1; i < count - 1; i++) {

					pointTranslate(X[i], Y[i]);
					ga.drawString(("(" + virtual.x + "," + virtual.y + ")"), X[i], Y[i]);
					polygon.lineTo(X[i], Y[i]);
				}

				polygon.closePath();
				
				ga.setColor(IrregularPolygonColor);
				ga.fill(polygon);
				ga.setColor(lineColor);
				ga.setStroke(new BasicStroke(1.0f));
				ga.draw(polygon);
				
				start = count;
				count = 0;
			}
		}

		public void mouseClicked(MouseEvent e) {
			X[count] = e.getX();
			Y[count] = e.getY();
			count++;
			paintLock = 1;
			repaint();

			if (SwingUtilities.isRightMouseButton(e)) {
				paintLock = 2;
				repaint();
			}
		}

		public void mouseMoved(MouseEvent e) {
			pointTranslate(e.getX(), e.getY());
			system.updateCursorPoint(virtual.x, virtual.y);
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mouseDragged(MouseEvent e) {
			if (SwingUtilities.isRightMouseButton(e)) {
				paintLock = 2;

			}
		}
	}

	

}