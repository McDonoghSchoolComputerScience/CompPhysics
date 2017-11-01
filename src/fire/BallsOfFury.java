package fire;

import processing.core.PApplet;
import vector.Vector;

//Made by: Ian
public class BallsOfFury extends PApplet {
    Balls balls [];
    int ballsIndex;
    Balls zero; //COME BACK TO
    
    Vector g;
    double yOffset;
        
    double manRadius;
    double manX;
    double manY;
    boolean left;
    boolean right;
    boolean drawMan;
    
    double ballRadius;
    double ballMass;
    double startingY;
    double startingVelocityX;
    double startingVelocityY;
    
    int blue;
    int green;
    int gray;
    
    double level;
    int score;
    
    double dt;
    
    double b;
    
    public double dist (double x, double y, double x2, double y2) {
        double dist = Math.pow(Math.pow((x2 - x), 2) + Math.pow((y2 - y), 2), 0.5);
        return dist;
    }
    
    public void movement () {   
        for (int index = 0; index < ballsIndex; index++) {
            Balls ball = balls[index];
            
            ball.F = Vector.add(ball.weight, ball.dragForce);

            //ball.dragForce = Vector.mult(ball.v, -b * ball.v.mag);
            
            ball.a = Vector.mult(ball.F, 1/ball.mass);
            ball.v.add(Vector.mult(ball.a, dt));
            ball.r.add(Vector.mult(ball.v, dt));
            
            if (ball.r.j + ball.radius/2 > yOffset) {
                ball.v.j *= -0.99; //NEED TO REDO WITH PHYSICS
            }
            
            fill(175);
            ellipse ((float)ball.r.i, (float)ball.r.j, (float)ball.radius, (float)ball.radius);
            
            if (ball.r.i - ball.radius/2 > width) {
                if (drawMan) {
                    score += 10;
                    ball.r.i = -width;
                    level += 0.01;
                    for (int i = 0; i < level; i++) {
                        // createNewBall();
                    }
                }
            }
            
            if (!drawMan) {
                textSize (50);
                fill (255, 0, 0);
                text ("GAME OVER!", width/2 - 200, height/2 - 20);
                text ("Click the mouse to play again", width/2 - 200, height/2 + 50);
            }
        }
    }
    
    public void createNewBall () {
        ballRadius = random(20, 200);
        ballMass = random (10, 1000);
        startingY = random (0, (float) height/4);
        startingVelocityX = random (50, 150);
        startingVelocityY = random (0, 10);
            
        Balls a = new Balls (ballRadius, ballMass, new Vector (0, startingY, 0),
            new Vector (startingVelocityX, -startingVelocityY, 0), new Vector (0, 0, 0),
            new Vector (0, 0, 0), Vector.mult (g, ballMass), new Vector (0, 0, 0));
        balls[ballsIndex++] = a;
    }
    
    public void settings() {
        size (1200, 600);
    }
    
    public void setup() {
        balls = new Balls [1000];
        ballsIndex = 0;
        
        yOffset = 7 * height / 8;
        
        manRadius = 50;
        manX = width/2;
        manY = yOffset - manRadius;
        left = false;
        right = false;
        drawMan = true;
        
        level = 0;
        score = 0;
        
        dt = 0.1;
    
        g = new Vector (0, 9.8, 0);

        b = 0.5;

        background (255);
        
        createNewBall();
    }
    
    public void draw () {        
        if (score < 100) {
            //Sky Level 1
            noStroke();
            blue = 255;
            green = 191;
            for (double layer = 0; layer < yOffset; layer += yOffset/6) {
                fill (0, green, blue);
                rect (0, (float)layer, width, (float) (layer + yOffset/6));
                blue -= 10;
                green -= 5;
            }

            //Ground Level 1
            stroke (0);
            strokeWeight (2);
            fill (20, 200, 70);
            rect (0, (float)yOffset, width, (float)(height - yOffset));
        }
        
        if (score >= 100) {
            //Sky Level 2
            noStroke();
            gray = 50;
            for (double layer = 0; layer < yOffset; layer += yOffset/6) {
                fill (gray);
                gray += 10;
                rect (0, (float)layer, width, (float) (layer + yOffset/6));
            }

            //Ground Level 2
            stroke (0);
            strokeWeight (2);
            fill (0);
            rect (0, (float)yOffset, width, (float)(height - yOffset)); 
        }
        
        //Score
        textSize(30);
        fill (0);
        stroke (0);
        text ("Score: " + score, width/2 - 30, height/20);
        
        if (drawMan){
            //Man
            fill (255, 0, 0);
            rect ((float)(manX), (float)(manY), (float)manRadius, (float)manRadius);

            //ManMove
            if (left) {
                manX -= 10;
            }

            if (right) {
                manX += 10;
            }
/*
            if (manX + manRadius > width) {
                manX = width - manRadius;
            }

            if (manX < 0) {
                manX = 0;
            }
*/
        }
          //Balls
        // movement();
        
        fill(0);
        text ("right " + (right? 1 : 0), 50, height-50);
        text ("left  " + (left? 1 : 0), 50, height-100);
        text ("manx  " + manX, 50, height-150);
    }
    
    public void keyPressed () {
        if (key == 'a') {
            left = true;
        }         
        else
        if (key == 'x') {
            right = true;
        }
        System.out.println("keyPressed");
    }
    
    public void keyReleased () {
        if (key == 'a') {
            left = false;
        }
        else
        if (key == 'x') {
            right = false;
        }
        System.out.println("keyReleased");
    }
    
    public void mouseClicked () {
        if (!drawMan) {
            background (255);
            setup();
        }
    }
    
    public static void main(String[] args) {
        PApplet.main(new String[]{BallsOfFury.class.getName()});
    }
}
