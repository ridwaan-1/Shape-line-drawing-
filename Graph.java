import java.awt.*;

public class Graph {

    public static void drawGraph(Graphics2D ga, int scale) {
        String scaleLabel;
        int scaleWidth = 800;
        int scaleHeight = 350;
        Point center = new Point(0, 0); //center of coordinate system

        int scaleDiv = 50;
        int scaleLab = scale;
        for (int i = 1; i <= 15; i++) {
            ga.setColor(Color.WHITE); //drawing lines
            ga.setColor(Color.WHITE); //drawing lines
            ga.drawLine(scaleDiv, scaleHeight, scaleDiv, -scaleHeight); //(drawing lines y = +ve)
            ga.drawLine(-scaleDiv, scaleHeight, -scaleDiv, -scaleHeight); //drawing lines y = -ve)
            ga.drawLine(-scaleWidth, scaleDiv, scaleWidth, scaleDiv); //drawing lines x = +ve)
            ga.drawLine(-scaleWidth, -scaleDiv, scaleWidth, -scaleDiv); //drawing line x =-ve)

            ga.setColor(Color.BLACK); //TEXT OF Coordinates on graph
            scaleLabel = "" + scaleLab + "";
            ga.drawString(scaleLabel, scaleDiv, center.y+20);
            ga.drawString(scaleLabel, center.x+10, -scaleDiv);

            scaleLabel = "" + (-scaleLab) + "";
            ga.drawString(scaleLabel, -scaleDiv, center.y-5);
            ga.drawString(scaleLabel, center.x-25, scaleDiv);

            scaleDiv += 50; //Increments by 50
            scaleLab += scale;
        }

        ga.drawLine(center.x, -scaleHeight, center.x, scaleHeight); //y axis
        ga.drawLine(-scaleWidth, center.y, scaleWidth, center.y); //x axis
        ga.drawString("0", 7, 15);
    }
}