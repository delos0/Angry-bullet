import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.String;

/**
 * Program uses projectile motion to simulate Angry Birds game
 *
 * @author Yeldos Urashev
 * @since 20.03.2024
 */

public class Angry_bullet {
    public static void main(String[] args) {

        // Game Parameters
        int width = 1600; //screen width
        int height = 800; // screen height
        double gravity = 9.80665; // gravity
        double x0 = 120; // x and y coordinates of the bullet’s starting position on the platform
        double y0 = 120;
        double bulletVelocity = 180; // initial velocity
        double bulletAngle = 45.0; // initial angle
        // Box coordinates for obstacles and targets
        // Each row stores a box containing the following information:
        // x and y coordinates of the lower left rectangle corner, width, and height
        double[][] obstacleArray = {
                {1200, 0, 60, 220},
                {1000, 0, 60, 160},
                {600, 0, 60, 80},
                {600, 180, 60, 160},
                {220, 0, 120, 180}
        };
        double[][] targetArray = {
                {1160, 0, 30, 30},
                {730, 0, 30, 30},
                {150, 0, 20, 20},
                {1480, 0, 60, 60},
                {340, 80, 60, 30},
                {1500, 600, 60, 60}
        };



        StdDraw.setCanvasSize(width, height);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        StdDraw.enableDoubleBuffering();
        StdDraw.clear(StdDraw.WHITE);


        double x1 = 160, y1 = 160;
        double lineLength =  40, scale = 2.5; // Linked length of line to value of velocity by scale 2.5
        int pauseDuration = 80;
        int pauseDurationBullet = 9;
        double radiusCircle = 4;
        double radiusPen = 0.0075;
        double coefficient = 1.725; // coefficient to scale velocity because initially it was too fast;

        while(true) {
            StdDraw.clear(StdDraw.WHITE);
            if(StdDraw.isKeyPressed(KeyEvent.VK_LEFT)){
                bulletVelocity -= 1;
                //lineLength -= 2;
            }
            if(StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)){
                bulletVelocity += 1;
                //lineLength += 2;
            }
            if(StdDraw.isKeyPressed(KeyEvent.VK_DOWN)){
                bulletAngle -= 1;
            }
            if(StdDraw.isKeyPressed(KeyEvent.VK_UP)){
                bulletAngle += 1;
            }

            // Just restrictions for variables not to get crazy values.
            lineLength = 40 + scale*(bulletVelocity - 180);
            if(bulletVelocity<0) bulletVelocity = 0;
            if(bulletAngle<-90) bulletAngle = -90;
            if(bulletAngle>90) bulletAngle = 90;
            if(lineLength<1) lineLength = 1;

            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.filledSquare(x0/2, y0/2, y0/2);

            x1 = x0 + lineLength * Math.cos(bulletAngle * Math.PI / 180);
            y1 = y0 + lineLength * Math.sin(bulletAngle * Math.PI / 180);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(radiusPen);
            StdDraw.line(x0, y0, x1, y1);

            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(x0/2, y0/2, String.format("a: %.1f", bulletAngle));
            StdDraw.text(x0/2, y0/2-20, String.format("v: %.1f", bulletVelocity));
            StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
            for (int i=0; i<targetArray.length; i++) {
                double halfWidth = targetArray[i][2] / 2.0;
                double halfHeight = targetArray[i][3] / 2.0;
                double x = targetArray[i][0] + halfWidth;
                double y = targetArray[i][1] + halfHeight;
                StdDraw.filledRectangle(x, y, halfWidth, halfHeight);
            }

            StdDraw.setPenColor(StdDraw.DARK_GRAY);
            for(int i=0; i<obstacleArray.length; i++) {
                double halfWidth = obstacleArray[i][2] / 2.0;
                double halfHeight = obstacleArray[i][3] / 2.0;
                double x = obstacleArray[i][0] + halfWidth;
                double y = obstacleArray[i][1] + halfHeight;
                StdDraw.filledRectangle(x, y, halfWidth, halfHeight);
            }

            if(StdDraw.isKeyPressed(KeyEvent.VK_SPACE)) {
                double time = 0;
                double velocityX = Math.cos(bulletAngle * Math.PI / 180) * bulletVelocity / coefficient; // I am using coefficient here
                double velocityY = Math.sin(bulletAngle * Math.PI / 180) * bulletVelocity / coefficient;
                double positionX = x0, positionY = y0;
                double x_temp = x0, y_temp = y0;
                boolean hitTarget = false;
                boolean hitObstacle = false;
                double textPositionY = 780, textPositionX = 10;

                // loop to draw projectile motion until bullet hits something
                while(true) {

                    StdDraw.setPenColor(Color.BLACK);
                    StdDraw.setPenRadius(radiusPen/3);
                    StdDraw.line(x_temp, y_temp, positionX, positionY);
                    x_temp = positionX;
                    y_temp = positionY;

                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.filledCircle(positionX, positionY, radiusCircle);
                    positionX = x0 + velocityX * time;
                    positionY = y0 + velocityY * time - (0.5*gravity*time*time);
                    time += 0.15;

                    // Check if the bullet hit something
                    for(int i=0; i<targetArray.length; i++) {
                        double tempWidth = targetArray[i][2];
                        double tempHeight = targetArray[i][3];
                        double x = targetArray[i][0];
                        double y = targetArray[i][1];

                        if(positionY >= y && positionY <= y+tempHeight && positionX >= x && positionX <= x+tempWidth) {
                            hitTarget = true;
                        }
                    }

                    for(int i=0; i<obstacleArray.length; i++) {
                        double tempWidth = obstacleArray[i][2];
                        double tempHeight = obstacleArray[i][3];
                        double x = obstacleArray[i][0];
                        double y = obstacleArray[i][1];

                        if(positionY >= y && positionY <= y+tempHeight && positionX >= x && positionX <= x+tempWidth) {
                            hitObstacle = true;
                        }
                    }

                    if(positionY<0) {
                        StdDraw.setPenColor(Color.BLACK);
                        StdDraw.setFont(new Font("", Font.BOLD, 17));
                        StdDraw.textLeft(textPositionX, textPositionY, "Hit the ground. Press 'r' to shoot again.");
                        break;
                    }

                    if(positionX>1600) {
                        StdDraw.setPenColor(Color.BLACK);
                        StdDraw.setFont(new Font("", Font.BOLD, 17));
                        StdDraw.textLeft(textPositionX, textPositionY, "Max X reached. Press 'r' to shoot again.");
                        break;
                    }

                    if(hitTarget) {
                        StdDraw.setFont(new Font("", Font.BOLD, 17));
                        StdDraw.setPenColor(Color.BLACK);
                        StdDraw.textLeft(textPositionX, textPositionY, "Congratulations, you hit the target!");
                        break;
                    }

                    if(hitObstacle) {
                        StdDraw.setPenRadius(radiusPen*10);
                        StdDraw.setFont(new Font("", Font.BOLD, 17));
                        StdDraw.textLeft(textPositionX, textPositionY, "Hit the obstacle. Press 'r' to shoot again.");
                        break;
                    }

                    StdDraw.show();
                    StdDraw.pause(pauseDurationBullet);
                }

                // Loop to wait until user restarts
                while(true) {
                    StdDraw.setPenColor(Color.BLACK);
                    StdDraw.setPenRadius(radiusPen/3);
                    StdDraw.line(x_temp, y_temp, positionX, positionY);

                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.filledCircle(positionX, positionY, radiusCircle);
                    if(StdDraw.isKeyPressed(KeyEvent.VK_R)) {
                        bulletAngle = 45.0;
                        bulletVelocity = 180;
                        break;
                    }
                    StdDraw.show();
                    StdDraw.pause(pauseDuration);
                }
            }

            StdDraw.pause(pauseDuration);
            StdDraw.show();
        }
    }
}
