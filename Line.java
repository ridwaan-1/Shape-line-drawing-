
import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.event.MouseInputListener;
import static javax.swing.SwingUtilities.invokeLater;


public class Line extends Frame{
    private static int lineThickness = 3;
	private static Color lineColor = new Color(0, 0, 0);
	private static String lineStyle = "solid";
	private static String algoUsed = "DDA";
	private static int x1, x2, y1, y2;
	private static Point cursorPoint; 
	private static JLabel label;

	
	public static void setLineThickness(int t) {
		lineThickness = t;
	}

	public static void setAlgoUsed(String a) {
		algoUsed = a;
	}

	public static void setLineColour(Color c) {
		lineColor = c;
	}

	public static void setLineStyle(String s) {
		lineStyle = s;
	}

    public void updateCursorPoint(double x, double y) { 
		cursorPoint.x = (int) x;
		cursorPoint.y = (int) y;
		updateLabel();
	}

	protected void updateLabel() {
		String text = "The cursor is at (" + cursorPoint.x + ","+ cursorPoint.y + ")";
		label.setText(text);
	}

	private void build(Container container) { //container for main menu
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
		Handler handler = new Handler();
		container.add(handler);

		label = new JLabel();
		container.add(label);

		container.setBackground(MainMenu.paleBrown);
	}

	public static JInternalFrame display() {
        String outputMsg = (lineStyle=="" && algoUsed=="") ? "Straight Line" : "Straight " + lineStyle + " Line " + "using " + algoUsed + " algorithm";
		JInternalFrame frame = new JInternalFrame(outputMsg);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		Line system = new Line();
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

	//For Scalable Coordinate System
	public static class Handler extends JComponent implements MouseInputListener, MouseWheelListener {
		private static int scale = 10;
		private static double increment = 5;
		public Point virtual = null;
		private int radIncrement = 1;


		public Handler() {
            addMouseListener(this);
			addMouseMotionListener(this);
			addMouseWheelListener(this);
		}

        public Point pointTranslate(int x, int y) {
			double virtualX = (x - (getWidth() / 2)) / increment;
			double virtualY = ((getHeight() / 2) - y) / increment;
			virtual = new Point((int) virtualX, (int) virtualY);
			return virtual;
		}

		protected void paintComponent(Graphics g) { //CREATING SCALABLE COORDINATE SYSTEM
			super.paintComponent(g);
			Graphics2D ga = (Graphics2D) g;

			ga.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			ga.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			//ANTI ALIASING TEXT AND VALUES

			int halfWidth = getWidth() / 2; //Half width of the display
			int halfHeight = getHeight() / 2; //Half Height of the display

			AffineTransform save = ga.getTransform();
			//The AffineTransform class represents a 2D affinetransform that performs a linear mapping from 2D coordinates to other 2Dcoordinates that preserves the "straightness" and"parallelness" of lines. 
			ga.translate(halfWidth, halfHeight);

			Graph.drawGraph(ga, scale);

			ga.setTransform(save);

			ga.setColor(lineColor);

			if (algoUsed=="DDA") {
				if (lineStyle=="dashed") {
					ddaDashedLine(ga);
				} else if (lineStyle=="solid"){
					ddaSolidLine(ga);	
				} else {
					ddaDottedLine(ga);
				}
			} else {
				if (lineStyle=="dashed") {
					brDashedLine(ga);
				} else if (lineStyle=="solid"){
					brSolidLine(ga);	
				} else {
					brDottedLine(ga);
				}
			}
		}

        public void brDottedLine(Graphics2D ga) {
            double x, y, dx, dy, dy2, dx2, pk, step;
            int left = lineThickness / 2;
            int right = lineThickness / 2;
    
            if ((lineThickness % 2) == 0) {
                right = right + 1;
            }
    
            x = x1;
            y = y1;
    
            dx = x2 - x1;
            dy = y2 - y1;
            dx2 = 2 * dx;
            dy2 = 2 * dy;
    
            pointTranslate((int) x1, (int) y1);
            ga.drawString(("( " + virtual.x + "," + virtual.y + " )"), (int) x1, (int) y1);
    
            double m = dy / dx;
            pk = dy2 - dx;
    
            if ((m > 0) && (m < 1)) {
    
                step = Math.abs(dx);
                for (int i = 0; i < step; i = i + (2 * lineThickness)) {
                    for (int on = 0; on < lineThickness; on++) {
                        if (pk < 0) {
                            x = x + 1;
                            pk = pk + dy2;
                        } else {
                            x = x + 1;
                            y = y + 1;
                            pk = pk + (dy2 - dx2);
                        }
    
    
                        for (int z = 1; z <= left; z++) {
                            ga.drawLine((int) x, (int)(y - z), (int) x, (int)(y - z));
                        }
                        ga.drawLine((int) x, (int) y, (int) x, (int) y);
                        for (int z = 1; z <= right; z++) {
                            ga.drawLine((int) x, (int)(y + z), (int) x, (int)(y + z));
                        }
    
                    }
                    for (int off = 0; off < lineThickness; off++) {
                        if (pk < 0) {
                            x = x + 1;
                            pk = pk + dy2;
                        } else {
                            x = x + 1;
                            y = y + 1;
                            pk = pk + (dy2 - dx2);
                        }
                    }
                }
                pointTranslate((int) x, (int) y);
                ga.drawString(("( " + virtual.x + "," + virtual.y + " )"), (int) x, (int) y);
    
            } else if (m >= 1) {
    
                step = Math.abs(dy);
                for (int i = 0; i < step; i = i + (2 * lineThickness)) {
                    for (int on = 0; on < lineThickness; on++) {
                        if (pk < 0) {
                            y = y + 1;
                            pk = pk + dx2;
                        } else {
                            x = x + 1;
                            y = y + 1;
                            pk = pk + dx2 - dy2;
                        }
    
                        for (int z = 1; z <= left; z++) {
                            ga.drawLine((int)(x - z), (int) y, (int)(x - z), (int) y);
                        }
                        ga.drawLine((int) x, (int) y, (int) x, (int) y);
                        for (int z = 1; z <= right; z++) {
                            ga.drawLine((int)(x + z), (int) y, (int)(x + z), (int) y);
                        }
                    }
                    for (int off = 0; off < lineThickness; off++) {
                        if (pk < 0) {
                            y = y + 1;
                            pk = pk + dx2;
                        } else {
                            x = x + 1;
                            y = y + 1;
                            pk = pk + dx2 - dy2;
                        }
                    }
                }
                pointTranslate((int) x, (int) y);
                ga.drawString(("    ( " + virtual.x + "," + virtual.y + " )"), (int) x, (int) y);
    
            } else if ((m > -1) && (m <= 0)) {
    
                step = Math.abs(dx);
    
                if (dy > 0) {
                    for (int i = 0; i < step; i = i + (2 * lineThickness)) {
                        for (int on = 0; on < lineThickness; on++) {
                            if (pk < 0) {
                                x = x + 1;
                                ga.drawLine((int) x, (int) y, (int) x, (int) y);
                                pk = pk + dy2;
                            } else {
                                x = x + 1;
                                y = y - 1;
                                ga.drawLine((int) x, (int) y, (int) x, (int) y);
                                pk = pk + (dy2 - dx2);
                            }
    
                            for (int z = 1; z <= left; z++) {
                                ga.drawLine((int) x, (int)(y - z), (int) x, (int)(y - z));
                            }
                            ga.drawLine((int) x, (int) y, (int) x, (int) y);
                            for (int z = 1; z <= right; z++) {
                                ga.drawLine((int) x, (int)(y + z), (int) x, (int)(y + z));
                            }
                        }
    
                        for (int off = 0; off < lineThickness; off++) {
                            if (pk < 0) {
                                x = x + 1;
                                pk = pk + dy2;
                            } else {
                                x = x + 1;
                                y = y - 1;
                                pk = pk + (dy2 - dx2);
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < step; i = i + (2 * lineThickness)) {
                        for (int on = 0; on < lineThickness; on++) {
                            if (pk < 0) {
                                x = x + 1;
                                pk = pk - dy2;
                            } else {
                                x = x + 1;
                                y = y - 1;
                                pk = pk - dy2 - dx2;
                            }
    
                            for (int z = 1; z <= left; z++) {
                                ga.drawLine((int) x, (int)(y - z), (int) x, (int)(y - z));
                            }
                            ga.drawLine((int) x, (int) y, (int) x, (int) y);
                            for (int z = 1; z <= right; z++) {
                                ga.drawLine((int) x, (int)(y + z), (int) x, (int)(y + z));
                            }
                        }
                        for (int off = 0; off < lineThickness; off++) {
                            if (pk < 0) {
                                x = x + 1;
                                pk = pk - dy2;
                            } else {
                                x = x + 1;
                                y = y - 1;
                                pk = pk - dy2 - dx2;
                            }
                        }
                    }
                }
                pointTranslate((int) x, (int) y);
                ga.drawString(("( " + virtual.x + "," + virtual.y + " )"), (int) x, (int) y);
    
            } else if (m <= -1) {
    
                step = Math.abs(dy);
                if (dy > 0) {
                    for (int i = 0; i < step; i = i + (2 * lineThickness)) {
                        for (int on = 0; on < lineThickness; on++) {
                            if (pk < 0) {
                                y = y - 1;
                                ga.drawLine((int) x, (int) y, (int) x, (int) y);
                                pk = pk + dx2;
                            } else {
                                x = x + 1;
                                y = y - 1;
                                pk = pk + dx2 - dy2;
                            }
                            for (int z = 1; z <= left; z++) {
                                ga.drawLine((int)(x - z), (int) y, (int)(x - z), (int) y);
                            }
                            ga.drawLine((int) x, (int) y, (int) x, (int) y);
                            for (int z = 1; z <= right; z++) {
                                ga.drawLine((int)(x + z), (int) y, (int)(x + z), (int) y);
                            }
                        }
                        for (int off = 0; off < lineThickness; off++) {
                            if (pk < 0) {
                                y = y - 1;
                                pk = pk + dx2;
                            } else {
                                x = x + 1;
                                y = y - 1;
                                pk = pk + dx2 - dy2;
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < step; i = i + (2 * lineThickness)) {
                        for (int on = 0; on < lineThickness; on++) {
                            if (pk < 0) {
                                y = y - 1;
                                pk = pk + dx2;
                            } else {
                                x = x + 1;
                                y = y - 1;
                                pk = pk + dx2 + dy2;
                            }
                            for (int z = 1; z <= left; z++) {
                                ga.drawLine((int)(x - z), (int) y, (int)(x - z), (int) y);
                            }
                            ga.drawLine((int) x, (int) y, (int) x, (int) y);
                            for (int z = 1; z <= right; z++) {
                                ga.drawLine((int)(x + z), (int) y, (int)(x + z), (int) y);
                            }
                        }
                        for (int off = 0; off < lineThickness; off++) {
                            if (pk < 0) {
                                y = y - 1;
                                pk = pk + dx2;
                            } else {
                                x = x + 1;
                                y = y - 1;
                                pk = pk + dx2 + dy2;
                            }
                        }
                    }
                }
                pointTranslate((int) x, (int) y);
                ga.drawString(("( " + virtual.x + "," + virtual.y + " )"), (int) x, (int) y);
    
            }
        }
    
        public void brDashedLine(Graphics2D ga) {
            int dashlen = 15;
    
            double x, y, dx, dy, dy2, dx2, pk, step;
            int left = lineThickness / 2;
            int right = lineThickness / 2;
    
            if ((lineThickness % 2) == 0) {
                right = right + 1;
            }
    
            x = x1;
            y = y1;
    
            dx = x2 - x1;
            dy = y2 - y1;
            dx2 = 2 * dx;
            dy2 = 2 * dy;
    
    
            pointTranslate((int) x1, (int) y1);
            ga.drawString(("    ( " + virtual.x + "," + virtual.y + " )"), (int) x1, (int) y1);
    
            double m = dy / dx;
            pk = dy2 - dx;
    
            if ((m > 0) && (m < 1)) {
                step = Math.abs(dx);
                for (int i = 0; i < step; i = i + (2 * lineThickness) + dashlen) {
    
                    for (int on = 0; on < lineThickness + dashlen; on++) {
                        if (pk < 0) {
                            x = x + 1;
                            pk = pk + dy2;
                        } else {
                            x = x + 1;
                            y = y + 1;
                            pk = pk + (dy2 - dx2);
                        }
    
    
                        for (int z = 1; z <= left; z++) {
                            ga.drawLine((int) x, (int)(y - z), (int) x, (int)(y - z));
                        }
    
                        ga.drawLine((int) x, (int) y, (int) x, (int) y);
    
                        for (int z = 1; z <= right; z++) {
                            ga.drawLine((int) x, (int)(y + z), (int) x, (int)(y + z));
                        }
                    }
    
                    for (int off = 0; off < lineThickness; off++) {
                        if (pk < 0) {
                            x = x + 1;
                            pk = pk + dy2;
                        } else {
                            x = x + 1;
                            y = y + 1;
                            pk = pk + (dy2 - dx2);
                        }
                    }
    
                }
                pointTranslate((int) x, (int) y);
                ga.drawString(("    ( " + virtual.x + "," + virtual.y + " )"), (int) x, (int) y);
            } else if (m >= 1) {
                
                step = Math.abs(dy);
                for (int i = 0; i < step; i = i + (2 * lineThickness) + dashlen) {
                    for (int on = 0; on < lineThickness + dashlen; on++) {
                        if (pk < 0) {
                            y = y + 1;
                            pk = pk + dx2;
                        } else {
                            x = x + 1;
                            y = y + 1;
                            pk = pk + dx2 - dy2;
                        }
    
                        for (int z = 1; z <= left; z++) {
                            ga.drawLine((int)(x - z), (int) y, (int)(x - z), (int) y);
                        }
    
                        ga.drawLine((int) x, (int) y, (int) x, (int) y);
    
                        for (int z = 1; z <= right; z++) {
                            ga.drawLine((int)(x + z), (int) y, (int)(x + z), (int) y);
                        }
                    }
    
                    for (int off = 0; off < lineThickness; off++) {
                        if (pk < 0) {
                            y = y + 1;
                            pk = pk + dx2;
                        } else {
                            x = x + 1;
                            y = y + 1;
                            pk = pk + dx2 - dy2;
                        }
                    }
                }
                pointTranslate((int) x, (int) y);
                ga.drawString(("    ( " + virtual.x + "," + virtual.y + " )"), (int) x, (int) y);
            } else if ((m > -1) && (m <= 0)) {
                step = Math.abs(dx);
    
                if (dy > 0) {
                    for (int i = 0; i < step; i = i + (2 * lineThickness) + dashlen) {
                        for (int on = 0; on < lineThickness + dashlen; on++) {
                            if (pk < 0) {
                                x = x + 1;
                                ga.drawLine((int) x, (int) y, (int) x, (int) y);
                                pk = pk + dy2;
                            } else {
                                x = x + 1;
                                y = y - 1;
                                ga.drawLine((int) x, (int) y, (int) x, (int) y);
                                pk = pk + (dy2 - dx2);
                            }
    
                            for (int z = 1; z <= left; z++) {
                                ga.drawLine((int) x, (int)(y - z), (int) x, (int)(y - z));
                            }
    
                            ga.drawLine((int) x, (int) y, (int) x, (int) y);
    
                            for (int z = 1; z <= right; z++) {
                                ga.drawLine((int) x, (int)(y + z), (int) x, (int)(y + z));
                            }
                        }
    
                        for (int off = 0; off < lineThickness; off++) {
                            if (pk < 0) {
                                x = x + 1;
                                pk = pk + dy2;
                            } else {
                                x = x + 1;
                                y = y - 1;
                                pk = pk + (dy2 - dx2);
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < step; i = i + (2 * lineThickness) + dashlen) {
                        for (int on = 0; on < lineThickness + dashlen; on++) {
                            if (pk < 0) {
                                x = x + 1;
                                pk = pk - dy2;
                            } else {
                                x = x + 1;
                                y = y - 1;
                                pk = pk - dy2 - dx2;
                            }
    
                            for (int z = 1; z <= left; z++) {
                                ga.drawLine((int) x, (int)(y - z), (int) x, (int)(y - z));
                            }
    
                            ga.drawLine((int) x, (int) y, (int) x, (int) y);
    
                            for (int z = 1; z <= right; z++) {
                                ga.drawLine((int) x, (int)(y + z), (int) x, (int)(y + z));
                            }
                        }
    
                        for (int off = 0; off < lineThickness; off++) {
                            if (pk < 0) {
                                x = x + 1;
                                pk = pk - dy2;
                            } else {
                                x = x + 1;
                                y = y - 1;
                                pk = pk - dy2 - dx2;
                            }
                        }
                    }
                }
    
                pointTranslate((int) x, (int) y);
                ga.drawString(("    ( " + virtual.x + "," + virtual.y + " )"), (int) x, (int) y);
            } else if (m <= -1) {
                step = Math.abs(dy);
                if (dy > 0) {
                    for (int i = 0; i < step; i = i + (2 * lineThickness) + dashlen) {
                        for (int on = 0; on < lineThickness + dashlen; on++) {
                            if (pk < 0) {
                                y = y - 1;
                                ga.drawLine((int) x, (int) y, (int) x, (int) y);
                                pk = pk + dx2;
                            } else {
                                x = x + 1;
                                y = y - 1;
                                pk = pk + dx2 - dy2;
                            }
    
                            for (int z = 1; z <= left; z++) {
                                ga.drawLine((int)(x - z), (int) y, (int)(x - z), (int) y);
                            }
    
                            ga.drawLine((int) x, (int) y, (int) x, (int) y);
    
                            for (int z = 1; z <= right; z++) {
                                ga.drawLine((int)(x + z), (int) y, (int)(x + z), (int) y);
                            }
                        }
                        for (int off = 0; off < lineThickness; off++) {
                            if (pk < 0) {
                                y = y - 1;
                                pk = pk + dx2;
                            } else {
                                x = x + 1;
                                y = y - 1;
                                pk = pk + dx2 - dy2;
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < step; i = i + (2 * lineThickness) + dashlen) {
                        for (int on = 0; on < lineThickness + dashlen; on++) {
                            if (pk < 0) {
                                y = y - 1;
                                pk = pk + dx2;
                            } else {
                                x = x + 1;
                                y = y - 1;
                                pk = pk + dx2 + dy2;
                            }
    
                            for (int z = 1; z <= left; z++) {
                                ga.drawLine((int)(x - z), (int) y, (int)(x - z), (int) y);
                            }
    
                            ga.drawLine((int) x, (int) y, (int) x, (int) y);
    
                            for (int z = 1; z <= right; z++) {
                                ga.drawLine((int)(x + z), (int) y, (int)(x + z), (int) y);
                            }
                        }
    
                        for (int off = 0; off < lineThickness; off++) {
                            if (pk < 0) {
                                y = y - 1;
                                pk = pk + dx2;
                            } else {
                                x = x + 1;
                                y = y - 1;
                                pk = pk + dx2 + dy2;
                            }
                        }
                    }
                }
            }
    
            pointTranslate((int) x, (int) y);
            ga.drawString(("    ( " + virtual.x + "," + virtual.y + " )"), (int) x, (int) y);
        }
    
        public void brSolidLine(Graphics2D ga) {
            double x, y, dx, dy, sumy, sumx, pk, step;
            x = x1;
            y = y1;
            int left = lineThickness / 2;
            int right = lineThickness / 2;
    
            if ((lineThickness % 2) == 0) {
                right = right + 1;
            }
    
            dx = x2 - x1;
            dy = y2 - y1;
            sumx = 2 * dx;
            sumy = 2 * dy;
    
            pointTranslate((int) x1, (int) y1);
            ga.drawString(("    ( " + virtual.x + "," + virtual.y + " )"), (int) x1, (int) y1);
    
            pk = sumy - dx;
            double m = dy / dx;
            step = Math.abs(dx);
            if ((m > 0) && (m < 1)) {
                step = Math.abs(dx);
                for (int i = 0; i < step; i++) {
                    if (pk < 0) {
                        x = x + 1;
                        pk = pk + sumy;
                    } else {
                        x = x + 1;
                        y = y + 1;
                        pk = pk + (sumy - sumx);
                    }
    
                    ga.drawLine((int) x, (int) y, (int) x, (int) y);
    
                    if ((lineThickness - 1) % 2 == 0) {
                        for (int v = 1; v <= (lineThickness - 1) / 2; v++) {
                            ga.drawLine((int) x, (int) y + v, (int) x1, (int) y1 + v);
                            ga.drawLine((int) x, (int) y - v, (int) x1, (int) y1 - v);
                        }
                    } else {
                        ga.drawLine((int) x, (int) y + 1, (int) x1, (int) y1 + 1);
                        for (int v = 1; v <= (lineThickness - 2) / 2; v++) {
                            ga.drawLine((int) x, (int) y - v, x1, y1 - v);
                            ga.drawLine((int) x, (int) y + (v + 1), (int) x1, (int) y1 + (v + 1));
                        }
                    }
                }
                pointTranslate((int) x, (int) y);
                ga.drawString(("( " + virtual.x + "," + virtual.y + " )"), (int) x, (int) y);
    
            } else if (m >= 1) {
    
                step = Math.abs(dy);
                for (int i = 0; i < step; i++) {
                    if (pk < 0) {
                        y = y + 1;
                        pk = pk + sumx;
                    } else {
                        x = x + 1;
                        y = y + 1;
                        pk = pk + sumx - sumy;
                    }
    
                    ga.drawLine((int) x, (int) y, (int) x, (int) y);
    
                    for (int z = 1; z <= left; z++) {
                        ga.drawLine((int) x - z, (int) y, (int) x - z, (int) y);
                    }
                    ga.drawLine((int) x, (int) y, (int) x, (int) y);
                    for (int z = 1; z <= right; z++) {
                        ga.drawLine((int) x + z, (int) y, (int) x + z, (int) y);
                    }
                }
                pointTranslate((int) x, (int) y);
                ga.drawString(("( " + virtual.x + "," + virtual.y + " )"), (int) x, (int) y);
    
            } else if ((m > -1) && (m <= 0)) {
    
                step = Math.abs(dx);
    
                if (dy > 0) {
                    // sign = 1;
                    for (int i = 0; i < step; i++) {
                        if (pk < 0) {
                            x = x + 1;
                            pk = pk + sumy;
                        } else {
                            x = x + 1;
                            y = y - 1;
                            pk = pk + (sumy - sumx);
                        }
    
                        ga.drawLine((int) x, (int) y, (int) x, (int) y);
    
                        if ((lineThickness - 1) % 2 == 0) {
                            for (int v = 1; v <= (lineThickness - 1) / 2; v++) {
                                ga.drawLine((int) x, (int) y + v, (int) x1, (int) y1 + v);
                                ga.drawLine((int) x, (int) y - v, (int) x1, (int) y1 - v);
                            }
                        } else {
                            ga.drawLine((int) x, (int) y + 1, (int) x1, (int) y1 + 1);
                            for (int v = 1; v <= (lineThickness - 2) / 2; v++) {
                                ga.drawLine((int) x, (int) y - v, x1, y1 - v);
                                ga.drawLine((int) x, (int) y + (v + 1), (int) x1, (int) y1 + (v + 1));
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < step; i++) {
                        if (pk < 0) {
                            x = x + 1;
                            pk = pk - sumy;
                        } else {
                            x = x + 1;
                            y = y - 1;
                            pk = pk - sumy - sumx;
                        }
    
                        ga.drawLine((int) x, (int) y, (int) x, (int) y);
    
                        if ((lineThickness - 1) % 2 == 0) {
                            for (int v = 1; v <= (lineThickness - 1) / 2; v++) {
                                ga.drawLine((int) x, (int) y + v, (int) x1, (int) y1 + v);
                                ga.drawLine((int) x, (int) y - v, (int) x1, (int) y1 - v);
                            }
                        } else {
                            ga.drawLine((int) x, (int) y + 1, (int) x1, (int) y1 + 1);
                            for (int v = 1; v <= (lineThickness - 2) / 2; v++) {
                                ga.drawLine((int) x, (int) y - v, x1, y1 - v);
                                ga.drawLine((int) x, (int) y + (v + 1), (int) x1, (int) y1 + (v + 1));
                            }
                        }
                    }
                }
                pointTranslate((int) x, (int) y);
    
                ga.drawString(("( " + virtual.x + "," + virtual.y + " )"), (int) x, (int) y);
    
            } else if (m <= -1) {
    
                step = Math.abs(dy);
                if (dy > 0) {
                    for (int i = 0; i < step; i++) {
                        if (pk < 0) {
                            y = y - 1;
                            pk = pk + sumx;
                        } else {
                            x = x + 1;
                            y = y - 1;
                            pk = pk + sumx - sumy;
                        }
    
                        ga.drawLine((int) x, (int) y, (int) x, (int) y);
    
                        if ((lineThickness - 1) % 2 == 0) {
                            for (int v = 1; v <= (lineThickness - 1) / 2; v++) {
                                ga.drawLine((int) x, (int) y + v, (int) x1, (int) y1 + v);
                                ga.drawLine((int) x, (int) y - v, (int) x1, (int) y1 - v);
                            }
                        } else {
                            ga.drawLine((int) x, (int) y + 1, (int) x1, (int) y1 + 1);
                            for (int v = 1; v <= (lineThickness - 2) / 2; v++) {
                                ga.drawLine((int) x, (int) y - v, x1, y1 - v);
                                ga.drawLine((int) x, (int) y + (v + 1), (int) x1, (int) y1 + (v + 1));
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < step; i++) {
                        if (pk < 0) {
                            y = y - 1;
                            pk = pk + sumx;
                        } else {
                            x = x + 1;
                            y = y - 1;
                            pk = pk + sumx + sumy;
                        }
    
                        ga.drawLine((int) x, (int) y, (int) x, (int) y);
    
                        for (int z = 1; z <= left; z++) {
                            ga.drawLine((int) x - z, (int) y, (int) x - z, (int) y);
                        }
                        ga.drawLine((int) x, (int) y, (int) x, (int) y);
                        for (int z = 1; z <= right; z++) {
                            ga.drawLine((int) x + z, (int) y, (int) x + z, (int) y);
                        }
                    }
                }
                pointTranslate((int) x, (int) y);
                ga.drawString(("    ( " + virtual.x + "," + virtual.y + " )"), (int) x, (int) y);
            }
        }
    
        public void ddaDashedLine(Graphics2D ga) {
            float xB, yB, x, y, dx, dy, xincr, yincr, steps;
            int prevX, prevY, X, Y;
    
            dx = x2 - x1;
            dy = y2 - y1;
    
            if (Math.abs(dy) <= Math.abs(dx)) {
                steps = Math.abs(dx);
            } else {
                steps = Math.abs(dy);
            }
    
            xincr = dx / steps;
            yincr = dy / steps;
    
            xB = x1;
            yB = y1;
            x = x1;
            y = y1;
            prevX = Math.round(xB);
            prevY = Math.round(yB);
            X = Math.round(x);
            Y = Math.round(y);
            Color col2 = new Color(0, 51, 153); // Colour of Coordinates
            ga.setColor(col2);
            pointTranslate((int) x1, (int) y1);
            ga.drawString(("    ( " + virtual.x + "," + virtual.y + " )"), (int) x1, (int) y1);
    
            for (int i = 1; i <= (int) steps; i = i + (2 * (lineThickness) + 12)) {
                for (int t = 0; t < lineThickness + 10; t++) {
    
                    x = xB + xincr;
                    y = yB + yincr;
                    X = Math.round(x);
                    Y = Math.round(y);
    
                    if (Math.abs(dy) <= Math.abs(dx)) {
    
                        ga.drawLine(prevX, prevY, X, Y);
                        if ((lineThickness - 1) % 2 == 0) {
                            for (int v = 1; v <= (lineThickness - 1) / 2; v++) {
                                ga.drawLine(prevX, prevY + v, X, Y + v);
                            }
                        } else {
                            ga.drawLine(prevX, prevY + 1, X, Y + 1);
                            for (int v = 1; v <= (lineThickness - 2) / 2; v++) {
                                ga.drawLine(prevX, prevY - v, X, Y - v);
                            }
                        }
    
                    } else {
                        ga.drawLine(prevX, prevY, X, Y);
                        if ((lineThickness - 1) % 2 == 0) {
                            for (int v = 1; v <= (lineThickness - 1) / 2; v++) {
                                ga.drawLine(prevX + v, prevY, X + v, Y);
                            }
                        } else {
                            ga.drawLine(prevX + 1, prevY, X + 1, Y);
                            for (int v = 1; v <= (lineThickness - 2) / 2; v++) {
                                ga.drawLine(prevX - v, prevY, X - v, Y);
                            }
                        }
                    }
    
                    xB = x;
                    yB = y;
    
                    prevX = Math.round(xB);
                    prevY = Math.round(yB);
                }
    
                for (int z = 0; z < lineThickness + 2; z++) {
                    x = xB + xincr;
                    y = yB + yincr;
                    xB = x;
                    yB = y;
                }
    
                prevX = Math.round(xB);
                prevY = Math.round(yB);
            }
            pointTranslate((int) x, (int) y);
            ga.setColor(col2);
            ga.drawString(("    ( " + virtual.x + "," + virtual.y + " )"), (int) x, (int) y);
        }
    
        public void ddaSolidLine(Graphics2D ga) {
            float xB, yB, x, y, dx, dy, xincr, yincr, steps;
            int prevX, prevY, X, Y;
    
            dx = x2 - x1;
            dy = y2 - y1;
    
            if (Math.abs(dy) <= Math.abs(dx)) {
                steps = Math.abs(dx);
                
            } else {
                steps = Math.abs(dy);
            }
    
            xincr = dx / steps;
            yincr = dy / steps;
    
            xB = x1;
            yB = y1;
            x = x1;
            y = y1;
            prevX = Math.round(xB);
            prevY = Math.round(yB);
            X = Math.round(x);
            Y = Math.round(y);
    
            pointTranslate((int) x1, (int) y1);
            ga.drawString(("    ( " + virtual.x + "," + virtual.y + " )"), (int) x1, (int) y1);
    
            for (int i = 1; i <= (int) steps; i++) {
                x = xB + xincr;
                y = yB + yincr;
                X = Math.round(x);
                Y = Math.round(y);
                
                if (Math.abs(dy) <= Math.abs(dx)) {
                    ga.drawLine(prevX, prevY, X, Y);
                    if ((lineThickness - 1) % 2 == 0) {
                        for (int v = 1; v <= (lineThickness - 1) / 2; v++) {
                            ga.drawLine((int) x, (int) y + v, (int) x1, (int) y1 + v);
                            ga.drawLine((int) x, (int) y - v, (int) x1, (int) y1 - v);
                        }
                    } else {
                        ga.drawLine((int) x, (int) y + 1, (int) x1, (int) y1 + 1);
                        for (int v = 1; v <= (lineThickness - 2) / 2; v++) {
                            ga.drawLine((int) x, (int) y - v, x1, y1 - v);
                            ga.drawLine((int) x, (int) y + (v + 1), (int) x1, (int) y1 + (v + 1));
                        }
                    }
                } else {
                    ga.drawLine(prevX, prevY, X, Y);
                    if ((lineThickness - 1) % 2 == 0) {
                        for (int v = 1; v <= (lineThickness - 1) / 2; v++) {
                            ga.drawLine((int) x, (int) y + v, (int) x1, (int) y1 + v);
                            ga.drawLine((int) x, (int) y - v, (int) x1, (int) y1 - v);
                        }
                    } else {
                        ga.drawLine((int) x, (int) y + 1, (int) x1, (int) y1 + 1);
                        for (int v = 1; v <= (lineThickness - 2) / 2; v++) {
                            ga.drawLine((int) x, (int) y - v, x1, y1 - v);
                            ga.drawLine((int) x, (int) y + (v + 1), (int) x1, (int) y1 + (v + 1));
                        }
                    }
                }
    
                xB = x;
                yB = y;
                prevX = Math.round(xB);
                prevY = Math.round(yB);
    
            }
            pointTranslate((int) x, (int) y);
            ga.drawString(("    ( " + virtual.x + "," + virtual.y + " )"), (int) x, (int) y);
    
        }
    
        public void ddaDottedLine(Graphics2D ga) {
            double x, y, dx, dy, step;
            double xIncr, yIncr;
    
            x = x1;
            y = y1;
    
            dx = x2 - x1;
            dy = y2 - y1;
            pointTranslate((int) x1, (int) y1);
            ga.drawString(("( " + virtual.x + "," + virtual.y + " )"), (int) x1, (int) y1); // SA 2 LA LINE LA
    
            int right = lineThickness / 2;
            if ((lineThickness % 2) == 0) {
                right = right - 1;
            }
    
            if (Math.abs(dy) <= Math.abs(dx)) {
                step = Math.abs(dx);
    
                xIncr = dx / step;
                yIncr = dy / step;
    
                for (int i = 0; i <= step; i = i + (2 * lineThickness)) {
                    for (int dotLength = 0; dotLength < lineThickness; dotLength++) {
    
                        for (int j = 1; j <= lineThickness / 2; j++) { // left hand
                            ga.drawLine((int) x, (int)(y - j), (int) x, (int)(y - j));
                        }
    
                        ga.drawLine((int) x, (int) y, (int) x, (int) y);
    
                        for (int j = 1; j <= right; j++) { // right hand
                            ga.drawLine((int) x, (int)(y + j), (int) x, (int)(y + j));
                        }
    
                        x = x + xIncr;
                        y = y + yIncr;
                    }
                    for (int gapLength = 0; gapLength < lineThickness; gapLength++) {
                        x = x + xIncr;
                        y = y + yIncr;
                    }
                }
    
                pointTranslate((int) x, (int) y);
                ga.drawString(("    ( " + virtual.x + "," + virtual.y + " )"), (int) x, (int) y);
            } else {
                step = Math.abs(dy);
    
                xIncr = dx / step;
                yIncr = dy / step;
    
                for (int i = 0; i <= step; i = i + (2 * lineThickness)) {
                    for (int dotLength = 0; dotLength < lineThickness; dotLength++) {
                        for (int j = 1; j <= lineThickness / 2; j++) {
                            ga.drawLine((int)(x - j), (int) y, (int)(x - j), (int) y);
                        }
    
                        ga.drawLine((int) x, (int) y, (int) x, (int) y);
    
                        for (int j = 1; j <= right; j++) {
                            ga.drawLine((int)(x + j), (int) y, (int)(x + j), (int) y);
                        }
    
                        x = x + xIncr;
                        y = y + yIncr;
                    }
                    for (int gapLength = 0; gapLength < lineThickness; gapLength++) {
                        x = x + xIncr;
                        y = y + yIncr;
                    }
                }
            }
    
            pointTranslate((int) x, (int) y);
            ga.drawString(("    ( " + virtual.x + "," + virtual.y + " )"), (int) x, (int) y);
    
        }
    

        public void mouseEntered(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
			x1 = e.getX();
			y1 = e.getY();
		}

		public void mouseDragged(MouseEvent e) {
			x2 = e.getX();
			y2 = e.getY();
			repaint();
		}

		public void mouseMoved(MouseEvent e) {
			
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

				else {//increment graph scale zoom in and zoom out
					scale += amount;
					increment = 50.0 / scale;
					radIncrement += (amount / 10);

					int wid2 = getWidth() / 2;
					int hei2 = getHeight() / 2;

					int oldX = x1;
					int oldY = y1;
					int oldX2 = x2;
					int oldY2 = y2;

					int transX = Math.abs(wid2 - oldX) / radIncrement;
					int transY = Math.abs(hei2 - oldY) / radIncrement;
					int transX2 = Math.abs(wid2 - oldX2) / radIncrement;
					int transY2 = Math.abs(hei2 - oldY2) / radIncrement;

					if (amount == 10) {
						if ((x1 <= wid2) && (y1 <= hei2)) {
							x1 = x1 + transX;
							y1 = y1 + transY;
						}

						else if ((x1 >= wid2) && (y1 <= hei2)) {
							x1 = x1 - transX;
							y1 = y1 + transY;
						}

						else if ((x1 <= wid2) && (y1 >= hei2)) {
							x1 = x1 + transX;
							y1 = y1 - transY;
						}

						else if ((x1 >= wid2) && (y1 >= hei2)) {
							x1 = x1 - transX;
							y1 = y1 - transY;
						}

						if ((x2 <= wid2) && (y2 <= hei2)) {
							x2 = x2 + transX2;
							y2 = y2 + transY2;
						}

						else if ((x2 >= wid2) && (y2 <= hei2)) {
							x2 = x2 - transX2;
							y2 = y2 + transY2;
						}

						else if ((x2 <= wid2) && (y2 >= hei2)) {
							x2 = x2 + transX2;
							y2 = y2 - transY2;
						}

						else if ((x2 >= wid2) && (y2 >= hei2)) {
							x2 = x2 - transX2;
							y2 = y2 - transY2;
						}
					}

					else if (amount == -10) {
						if ((x1 <= wid2) && (y1 <= hei2)) {
							x1 = x1 - transX;
							y1 = y1 - transY;
						}

						else if ((x1 >= wid2) && (y1 <= hei2)) {
							x1 = x1 + transX;
							y1 = y1 - transY;
						}

						else if ((x1 <= wid2) && (y1 >= hei2)) {
							x1 = x1 - transX;
							y1 = y1 + transY;
						}

						else if ((x1 >= wid2) && (y1 >= hei2)) {
							x1 = x1 + transX;
							y1 = y1 + transY;
						}

						if ((x2 <= wid2) && (y2 <= hei2)) {
							x2 = x2 - transX2;
							y2 = y2 - transY2;
						}

						else if ((x2 >= wid2) && (y2 <= hei2)) {
							x2 = x2 + transX2;
							y2 = y2 - transY2;
						}

						else if ((x2 <= wid2) && (y2 >= hei2)) {
							x2 = x2 - transX2;
							y2 = y2 + transY2;
						}

						else if ((x2 >= wid2) && (y2 >= hei2)) {
							x2 = x2 + transX2;
							y2 = y2 + transY2;
						}
					}
				}
			}
			repaint();
		}

		public static void main(String[] args) {

			try {
				invokeLater(new Runnable() {
					public void run() {
						display();

					}
				});
			} catch (Throwable e) {
			}

		}
	}
}

