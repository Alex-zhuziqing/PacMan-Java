package ghost;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import processing.core.PImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;



public class Map {

    /**
     * The filename for the map file.
     */
    public String mapFileName;

    public Game game;

    public Map(Game game) {
        this.mapFileName = "";
        this.game = game;
    }

    /**
     * Parse the JSON file
     * 
     * Parse the JSON file and assign the information to Waka and ghosts.
     * This includes the filename of the map, Waka's lives, Waka and ghosts' speed, 
     * ghosts' mode length and frightened mode length. 
     * 
     * @param filename The filename of the JSON file
     */
    public void readJsonFile(String filename) {

        JSONParser parser = new JSONParser();

		try {
			JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(filename));
 
            this.mapFileName = (String) jsonObject.get("map");

            int lives = (int) (long) (Long) jsonObject.get("lives");
            this.game.wakaLives = lives;
            this.game.waka.setLives(lives);

            int speed = (int) (long) (Long) jsonObject.get("speed");
            this.game.waka.setSpeed(speed);

            JSONArray modeLength = (JSONArray) jsonObject.get("modeLengths");
            ArrayList<Integer> intModeLength = new ArrayList<Integer>();

            this.game.frightenedLength = (int) (long) (Long) jsonObject.get("frightenedLength");

            @SuppressWarnings("unchecked")
			Iterator<Long> iter = modeLength.iterator();
			while (iter.hasNext()) {
				intModeLength.add((int) (long) iter.next());
            }
            this.game.modeLength = intModeLength;

		} catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }


    /**
     * Parse the map file
     * 
     * Parse the map file and add them to their corrosponding list. 
     */
    public void readMapFile() {

        ArrayList<String> wallList = new ArrayList<>(Arrays.asList("0", "1", "2", "3", "4", "5", "6"));
        ArrayList<String> pathList = new ArrayList<>(Arrays.asList("7", "8", "p", "a", "c", "i", "w"));
        ArrayList<String> ghostList = new ArrayList<>(Arrays.asList("a", "c", "i", "w"));

        File f = new File(this.mapFileName);
        try {
            Scanner scan = new Scanner(f);

            int wakaCounter = 0;
            int fruitCounter = 0;

            int row = 0;
            while (scan.hasNextLine()) {

                String line = scan.nextLine();
                String[] words = line.split("");

                for (int col = 0; col < words.length; col++) {
                    String character = words[col];

                    if (wallList.contains(character)) {

                        if (character.equals("0")) {
                            this.game.walls.add(new Wall());
    
                        } else if (character.equals("1")) {
                            this.game.walls.add(new Wall(col*16, row*16, this.game.app.loadImage("src/main/resources/horizontal.png")));
    
                        } else if (character.equals("2")) {
                            this.game.walls.add(new Wall(col*16, row*16, this.game.app.loadImage("src/main/resources/vertical.png")));                        
    
                        } else if (character.equals("3")) {
                            this.game.walls.add(new Wall(col*16, row*16, this.game.app.loadImage("src/main/resources/upLeft.png"))); 
    
                        } else if (character.equals("4")) {
                            this.game.walls.add(new Wall(col*16, row*16, this.game.app.loadImage("src/main/resources/upRight.png")));                       
    
                        } else if (character.equals("5")) {
                            this.game.walls.add(new Wall(col*16, row*16, this.game.app.loadImage("src/main/resources/downLeft.png")));                        
    
                        } else if (character.equals("6")) {
                            this.game.walls.add(new Wall(col*16, row*16, this.game.app.loadImage("src/main/resources/downRight.png")));                       
                        } 
                    }
                        
                    else if (pathList.contains(character)) {

                        this.game.paths.add(new int[]{col*16, row*16});

                        if (character.equals("7")) {
                            Fruit fruit = new Fruit(col*16, row*16, this.game.app.loadImage("src/main/resources/fruit.png"));
                            this.game.fruits.add(fruit);
                            fruitCounter++;
                        } 

                        else if (character.equals("8")) {
                            PImage fruitSprite = this.game.app.loadImage("src/main/resources/fruit.png");

                            fruitSprite.resize(32, 32);
                            Fruit superFruit = new SuperFruit(col*16, row*16, fruitSprite);
                            this.game.fruits.add(superFruit);
                            fruitCounter++;
                        }

                        else if (character.equals("p")) {
                            this.game.waka.setX(col*16);
                            this.game.waka.setY(row*16);
                            this.game.waka.setSprite(this.game.app.loadImage("src/main/resources/playerClosed.png"));
                            this.game.waka.initialPosition[0] = col*16;
                            this.game.waka.initialPosition[1] = row*16;
                            wakaCounter++;
                        }
                        
                        else if (ghostList.contains(character)) {
                        
                            Ghost ghost = new Ghost(0, 0, null);

                            if (character.equals("a")) {
                                ghost = new Ambusher(col*16, row*16, this.game.app.loadImage("src/main/resources/ambusher.png"));
                                Ambusher ambusher = (Ambusher) ghost;
                                this.game.ambushers.add(ambusher);
                            } 
                            else if (character.equals("c")) {
                                ghost = new Chaser(col*16, row*16, this.game.app.loadImage("src/main/resources/chaser.png"));
                                Chaser chaser = (Chaser) ghost;
                                this.game.chasers.add(chaser);
                            } 
                            else if (character.equals("i")) {
                                ghost = new Ignorant (col*16, row*16, this.game.app.loadImage("src/main/resources/ignorant.png"));
                                Ignorant ignorant = (Ignorant) ghost;
                                this.game.ignorants.add(ignorant);
                            } 
                            else if (character.equals("w")) {
                                ghost = new Whim(col*16, row*16, this.game.app.loadImage("src/main/resources/whim.png"));
                                Whim whim = (Whim) ghost;
                                this.game.whims.add(whim);
                                whim.chasers = this.game.chasers;
                            }

                            ghost.app = this.game.app;

                            ghost.setSpeed(this.game.waka.getSpeed());
                            ghost.setWaka(this.game.waka);

                            ghost.initialPosition[0] = col*16;
                            ghost.initialPosition[1] = row*16;

                            this.game.ghosts.add(ghost);
                        }
                    }
                }
                row++;
            }
            
            this.game.waka.setWalls(this.game.walls);
            this.game.waka.setPaths(this.game.paths);

            for (Ghost ghost : this.game.ghosts) {
                ghost.setWalls(this.game.walls);
                ghost.setPaths(this.game.paths);
            }

            if ((wakaCounter != 1) || (fruitCounter < 1)) {
                System.out.println("Invalid map");
                System.exit(1);
            }

            scan.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } 
    }
}
