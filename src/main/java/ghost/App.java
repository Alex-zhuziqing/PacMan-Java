package ghost;

import processing.core.PApplet;
import processing.core.PFont;

public class App extends PApplet {

    public static final int WIDTH = 448;
    public static final int HEIGHT = 576;

    public int time;

    private Game game;

    public App() {
        this.game = new Game(this);
        this.time = millis();
    }

    public void setup() {
        frameRate(60);  

        PFont font = this.createFont("PressStart2P-Regular", 25);
        this.textFont(font);
        
        this.game.map.readJsonFile("config.json");
        this.game.map.readMapFile();
    }

    public void settings() {
        size(WIDTH, HEIGHT);
    }


    public void draw() {

        background(0, 0, 0);

        this.time = millis();

        this.game.manageGameFlow();

    }

    public void keyPressed() {
        // Check if the key is encoded
        if (key == CODED) {
            // Check if it is the UP key
            if (keyCode == UP) {
                this.game.waka.nextDirection = Direction.UP;
            }

            if (keyCode == DOWN) {
                this.game.waka.nextDirection = Direction.DOWN;
            }

            if (keyCode == LEFT) {
                this.game.waka.nextDirection = Direction.LEFT;
            }

            if (keyCode == RIGHT) {
                this.game.waka.nextDirection = Direction.RIGHT;
            }
        }

        if (keyCode == 32) {
            if (this.game.isDebug) {
                this.game.isDebug = false;
            } else {
                this.game.isDebug = true;
            }
        }
    }

    public static void main(String[] args) {
        PApplet.main("ghost.App");
    }

}
