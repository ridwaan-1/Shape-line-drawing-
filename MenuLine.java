import javax.swing.*;
import java.awt.*;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MenuLine extends JFrame{
    static JFrame frameMenuLine = new JFrame("Straight Line Menu");
	static JPanel panelLine = new JPanel();
    static ButtonGroup algoSelected = new ButtonGroup();
    static ButtonGroup btnStyles = new ButtonGroup();

    MenuLine() {
        panelLine.setLayout(new GridLayout(6, 1, 20, 10));
        panelLine.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panelLine.setBackground(MainMenu.paleBrown);

        JRadioButton solid_btn = new JRadioButton("Solid");
        solid_btn.setActionCommand("solid");
        solid_btn.setBackground(MainMenu.paleBrown);

        JRadioButton dotted_btn = new JRadioButton("Dashed");
        dotted_btn.setActionCommand("dotted");
        dotted_btn.setBackground(MainMenu.paleBrown);

        JRadioButton dashed_btn = new JRadioButton("Dotted");
        dashed_btn.setActionCommand("dashed");
        dashed_btn.setBackground(MainMenu.paleBrown);
        
        btnStyles.add(solid_btn);
        btnStyles.add(dotted_btn);
        btnStyles.add(dashed_btn);
        solid_btn.setSelected(true); // Default value

        JRadioButton DDAalgo_btn = new JRadioButton("DDA Algorithm");
        DDAalgo_btn.setActionCommand("DDA");
        DDAalgo_btn.setBackground(MainMenu.paleBrown);

        JRadioButton Balgo_btn = new JRadioButton("Bresenham Algorithm");
        Balgo_btn.setActionCommand("Bresenham");
        Balgo_btn.setBackground(MainMenu.paleBrown);
        
        algoSelected.add(DDAalgo_btn);
        algoSelected.add(Balgo_btn);
        DDAalgo_btn.setSelected(true);

        GridLayout layout1 = new GridLayout(1, 2);
        
        JPanel algoSelection = new JPanel(layout1);
        algoSelection.add(DDAalgo_btn);
        algoSelection.add(Balgo_btn);

        layout1 = new GridLayout(1, 3);
        JPanel styleSelection = new JPanel(layout1);
        styleSelection.add(solid_btn);
        styleSelection.add(dashed_btn);
        styleSelection.add(dotted_btn);

        JLabel text1 = new JLabel("Select Algorithm");
        JLabel text2 = new JLabel("Select Line Style");
        JButton thicknessBtn = new JButton("Change Thickness of line");
		thicknessBtn.setBackground(MainMenu.Brown);
        JButton startBtn = new JButton("Start");
		startBtn.setBackground(MainMenu.Brown);
        
		text1.setPreferredSize(new Dimension(150, 75));
		algoSelection.setPreferredSize(new Dimension(150, 75));
		text2.setPreferredSize(new Dimension(150, 75));
		styleSelection.setPreferredSize(new Dimension(150, 75));
		thicknessBtn.setPreferredSize(new Dimension(150, 75));
		startBtn.setPreferredSize(new Dimension(150, 75));

        panelLine.removeAll();
		panelLine.add(text1);
		panelLine.add(algoSelection);
		panelLine.add(text2);
		panelLine.add(styleSelection);
		panelLine.add(thicknessBtn);
		panelLine.add(startBtn);

        frameMenuLine.add(panelLine);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		frameMenuLine.setLocation((int) width / 2, (int) height / 4);

		frameMenuLine.setSize(400, 300);
		frameMenuLine.setVisible(true);

        LineHandler LineHandler = new LineHandler();
		startBtn.addActionListener(LineHandler);
		thicknessBtn.addActionListener(LineHandler);

		startBtn.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				startBtn.setBackground(MainMenu.paleBrown);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				startBtn.setBackground(MainMenu.Brown);
			}
		});

		thicknessBtn.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				thicknessBtn.setBackground(MainMenu.paleBrown);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				thicknessBtn.setBackground(MainMenu.Brown);
			}
		});
    }

    public static class LineHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String action = event.getActionCommand();
			try {
				//Click and drag the cursor.Click and drag the cursor.
				if (action.equals("Start")) {
                    Line.setAlgoUsed(algoSelected.getSelection().getActionCommand());;
                    Line.setLineStyle(btnStyles.getSelection().getActionCommand());
                    JInternalFrame jfr =  Line.display();
                    MainMenu.frameSplit.setRightComponent(jfr);
                    frameMenuLine.setVisible(false);
                    JOptionPane.showMessageDialog(null, "<html>Left Click to select starting point of the line to be drawn<br/>Drag the cursor and release at the ending coordinate, to draw the desired line", "How to?", JOptionPane.INFORMATION_MESSAGE);
				}
				
				if (action.equals("Change Thickness of line")) {
					JFrame framethicknessBtn = new JFrame("thicknessBtn");
					framethicknessBtn.setSize(200, 200);
					framethicknessBtn.setVisible(false);
					String thicknessString = JOptionPane.showInputDialog(framethicknessBtn, "Enter thickness of line", null);
					int thickness = Integer.parseInt(thicknessString);
					Line.setLineThickness(thickness);
				}

			} catch (Throwable e) {}
		}

	}

}
