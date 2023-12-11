package greatrex.cameron;

//one non-javafx import for sounds :D
import java.io.File;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.skin.TextInputControlSkin.Direction;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class App extends Application {
    // stage renamed to window
    public static Stage window;
    // pane renamed to mainPane
    public static Pane mainPane = new Pane();
    // main snake that is the "head" or red part
    public static Rectangle snakeHead = new Rectangle(50, 50, 50, 50);
    // single berry that moves around the board and "spawns" randomly
    public static Circle berry;
    // location of the snake in pixels (x and y) for translating into row/col only
    // (because of the 50 offset)
    public static double snakeX, snakeY;
    // location of the snake in coordinates (0-7 x and y)
    public static int snakeXCol, snakeYRow, gameMode;
    // various text variables
    public static Text gameOverText = new Text(), instructionsText = new Text(),
            instructionsText1 = new Text(), scoreText = new Text(), pauseText = new Text(),
            pauseScreenText = new Text(), winText = new Text(),
            timeRemainingText = new Text();
    // array that stores all 64 rectangles
    public static Rectangle[][] bodyArray = new Rectangle[8][8];
    // 1d array that stores the location of the bodies
    public static Rectangle[] body = new Rectangle[1000];
    // the current direction of the snake
    public static Direction[] currentDirection = { Direction.BEGINNING };
    // the game loop of our game that keeps on going while the snake is not dead
    public static Timeline gameLoop = new Timeline();
    // timer loop to end the game after 60 seconds
    public static Timeline gameTimer = new Timeline();
    // timer loop to keep track of boost seconds
    public static Timeline boostTimer = new Timeline();
    // the score, and default row and col of the berries, amount of body parts, body
    // offset (i) to remove the tails of the snake
    public static int score = 0, berryRow = 4, berryCol = 4, bodyParts = 0, i = 0,
            timerRemainingSeconds = 45, boostRemainingSeconds = 10;
    // various boolean variables for the logic for the snake not going from left to
    // right and for if the boost is available or not
    public static boolean up = false, down = false, right = false, left = false, boostAvailable = true, easy = false,
            medium = false, hard = false;

    // fein song
    public static String filePath10 = "finalproject/src/main/java/fein.mp3";
    public static Media fein1 = new Media(new File(filePath10).toURI().toString());
    public static MediaPlayer feinPlayer = new MediaPlayer(fein1);
    public static MediaView feinView = new MediaView(feinPlayer);
    // snake sound effect for eating
    public static String filePath1 = "finalproject/src/main/java/SnakeEat.mp3";
    public static Media snakeEat = new Media(new File(filePath1).toURI().toString());
    public static MediaPlayer snakeEatPlayer = new MediaPlayer(snakeEat);
    public static MediaView snakeEatView = new MediaView(snakeEatPlayer);
    // snake sound effect for dying
    public static String filePath2 = "finalproject/src/main/java/SnakeDeath.mp3";
    public static Media snakeDie = new Media(new File(filePath2).toURI().toString());
    public static MediaPlayer snakeDiePlayer = new MediaPlayer(snakeDie);
    public MediaView snakeDieView = new MediaView(snakeDiePlayer);
    // snake sound effect for up
    public static String filePath3 = "finalproject/src/main/java/SnakeUp.mp3";
    public static Media snakeUp = new Media(new File(filePath3).toURI().toString());
    public static MediaPlayer snakeUpPlayer = new MediaPlayer(snakeUp);
    public MediaView snakeUpView = new MediaView(snakeUpPlayer);
    // snake sound effect for left
    public static String filePath4 = "finalproject/src/main/java/SnakeLeft.mp3";
    public static Media snakeLeft = new Media(new File(filePath4).toURI().toString());
    public static MediaPlayer snakeLeftPlayer = new MediaPlayer(snakeLeft);
    public MediaView snakeLeftView = new MediaView(snakeLeftPlayer);
    // snake sound effect for down
    public static String filePath5 = "finalproject/src/main/java/SnakeDown.mp3";
    public static Media snakeDown = new Media(new File(filePath5).toURI().toString());
    public static MediaPlayer snakeDownPlayer = new MediaPlayer(snakeDown);
    public MediaView snakeDownView = new MediaView(snakeDownPlayer);
    // snake sound effect for right
    public static String filePath6 = "finalproject/src/main/java/SnakeRight.mp3";
    public static Media snakeRight = new Media(new File(filePath6).toURI().toString());
    public static MediaPlayer snakeRightPlayer = new MediaPlayer(snakeRight);
    public MediaView snakeRightView = new MediaView(snakeRightPlayer);
    // various buttons throughout the game
    public static Button startGameButton = new Button("▷"), returnToMenuButton = new Button("㊂"),
            instructionsButton = new Button("ⓘ"), exitGameButton = new Button("X"),
            resumeButton = new Button("▷"), restartButton = new Button("↻"), rageButton = new Button("X"),
            easyButton = new Button("Easy"), mediumButton = new Button("Medium"), hardButton = new Button("Hard");
    // various groups that hold buttons, rectangles, etc for the scenes to use
    public static Group pauseGroup = new Group(resumeButton, restartButton, rageButton);
    public static Group gameGroup = new Group(snakeHead);
    public static Group gameOverGroup = new Group(returnToMenuButton);
    public static Group instructionsGroup = new Group(instructionsText, instructionsText1);
    public static Group winGroup = new Group(returnToMenuButton);
    public static Group gameModeGroup = new Group(easyButton, mediumButton, hardButton);
    // various scenes that change depending on what the user is trying to do
    public static Scene menuScene = new Scene(mainPane, 500, 500);
    public static Scene gameScene = new Scene(gameGroup, 500, 500, Color.BLACK);
    public static Scene pauseScene = new Scene(pauseGroup, 500, 500, Color.ORANGE);
    public static Scene gameOverScene = new Scene(gameOverGroup, 500, 500, Color.BLACK);
    public static Scene instructionsScene = new Scene(instructionsGroup, 500, 500, Color.ORANGE);
    public static Scene winScene = new Scene(winGroup, 500, 500, Color.BLACK);
    public static Scene gameModeScene = new Scene(gameModeGroup, 500, 500, Color.ORANGE);

    @Override
    public void start(Stage stage) {
        // set the pane to the main stage
        window = stage;
        // show the stage, make it un-resizable, and set the first scene to the menu
        // scene
        window.show();
        window.setResizable(false);
        window.setScene(menuScene);
        // set the background of the game to orange
        Rectangle background = new Rectangle(0, 0, 500, 500);
        background.setFill(Color.ORANGE);
        mainPane.getChildren().add(background);

        // set the title of the game to "BERRY EATER" with position color, and adds it
        // to the game
        Text title = new Text("BERRY EATER");
        title.setStyle("-fx-font-size: 70; -fx-text-fill: black;");
        title.setFont(Font.font("Impact", FontWeight.BOLD, 48));
        title.setX(75);
        title.setY(70);
        mainPane.getChildren().add(title);
        title.setFill(Color.BLACK);

        // add fein
        feinView.setVisible(false);
        gameGroup.getChildren().add(feinView);

        // add snake eating sound
        snakeEatView.setVisible(false);
        gameGroup.getChildren().add(snakeEatView);

        // add snake dying sound
        snakeDieView.setVisible(false);
        gameGroup.getChildren().add(snakeDieView);

        // add startGameButton to the pane and changes style format, location, etc
        startGameButton.setLayoutX(150);
        startGameButton.setLayoutY(100);
        mainPane.getChildren().add(startGameButton);
        startGameButton.setMinWidth(200);
        startGameButton.setMinHeight(100);
        startGameButton.setStyle(
                "-fx-font-size: 50px;-fx-background-color: Blue;-fx-text-fill: orange; -fx-background-radius: 15px;");
        easyButton.setLayoutX(100);
        easyButton.setLayoutY(25);
        easyButton.setMinWidth(300);
        easyButton.setMinHeight(100);
        easyButton.setStyle(
                "-fx-font-size: 50px;-fx-background-color: Blue;-fx-text-fill: orange; -fx-background-radius: 15px;");
        mediumButton.setLayoutX(100);
        mediumButton.setLayoutY(150);
        mediumButton.setMinWidth(300);
        mediumButton.setMinHeight(100);
        mediumButton.setStyle(
                "-fx-font-size: 50px;-fx-background-color: Blue;-fx-text-fill: orange; -fx-background-radius: 15px;");
        hardButton.setLayoutX(100);
        hardButton.setLayoutY(275);
        hardButton.setMinWidth(300);
        hardButton.setMinHeight(100);
        hardButton.setStyle(
                "-fx-font-size: 50px;-fx-background-color: Blue;-fx-text-fill: orange; -fx-background-radius: 15px;");
        // change returnToMenubutton looks, style, location etc
        returnToMenuButton.setStyle(
                "-fx-font-size: 30px;-fx-background-color: black;-fx-text-fill: white; -fx-background-radius: 15px;");
        returnToMenuButton.setLayoutX(0);
        returnToMenuButton.setLayoutY(0);
        returnToMenuButton.setMinWidth(50);
        returnToMenuButton.setMinHeight(50);
        // style, change location, and add instructionsButton to the main pane
        instructionsButton.setLayoutX(150);
        instructionsButton.setLayoutY(220);
        instructionsButton.setMinWidth(200);
        instructionsButton.setMinHeight(100);
        mainPane.getChildren().add(instructionsButton);
        instructionsButton.setStyle(
                "-fx-font-size: 50px;-fx-background-color: Blue;-fx-text-fill: orange; -fx-background-radius: 15px;");
        // style resume button to look better
        resumeButton.setMinWidth(275);
        resumeButton.setMinHeight(125);
        resumeButton.setLayoutX(100);
        resumeButton.setLayoutY(73);
        resumeButton.setStyle(
                "-fx-font-size: 60px;-fx-background-color: blue;-fx-text-fill: orange; -fx-background-radius: 15px;");
        // style restart button
        restartButton.setMinWidth(275);
        restartButton.setMinHeight(125);
        restartButton.setLayoutX(100);
        restartButton.setLayoutY(215);
        restartButton.setStyle(
                "-fx-font-size: 60px;-fx-background-color: blue;-fx-text-fill: orange; -fx-background-radius: 15px;");
        // style rage quit button
        rageButton.setMinWidth(275);
        rageButton.setMinHeight(125);
        rageButton.setLayoutX(100);
        rageButton.setLayoutY(357);
        rageButton.setStyle(
                "-fx-font-size: 60px;-fx-background-color: blue;-fx-text-fill: orange; -fx-background-radius: 15px;");
        // style the exit game button and add it to the main pane
        exitGameButton.setStyle(
                "-fx-font-size: 50px;-fx-background-color: Blue;-fx-text-fill: orange; -fx-background-radius: 15px;");
        exitGameButton.setLayoutX(200);
        exitGameButton.setLayoutY(340);
        exitGameButton.setMinWidth(50);
        exitGameButton.setMinHeight(50);
        mainPane.getChildren().add(exitGameButton);
        // change snake color to red and body to green
        snakeHead.setFill(Color.RED);
        // make the score display in game and styles it
        scoreText.setFill(Color.WHITE);
        scoreText.setStyle("-fx-font-size: 25px;");
        scoreText.setLayoutX(365);
        scoreText.setLayoutY(35);
        scoreText.setText("Score: " + score);
        gameGroup.getChildren().add(scoreText);
        // pauseText styling
        pauseText.setFill(Color.WHITE);
        pauseText.setStyle("-fx-font-size: 15px;");
        pauseText.setLayoutX(70);
        pauseText.setLayoutY(30);
        pauseText.setText("Press ESC to Pause");
        gameGroup.getChildren().add(pauseText);
        // style the instructions text, change the location, and change the scene to
        // instructionsScene
        instructionsText.setStyle("-fx-font-size: 48;-fx-text-fill: white;");
        instructionsText.setX(75);
        instructionsText.setY(50);
        instructionsText.setText("Rules & Instructions");
        // set the text inside the instructions scene to display the rules and
        // instructions
        instructionsText1.setText("The user has to control their game piece in up/down/left/right directions." +
                "\n\nThey can only move in one direction and never diagonally, the piece moves at a set speed in"
                + "\nthe desired direction."
                + "\n\nThe piece must stay within the boundaries of the stage."
                + "\n\nThe player will be allocated one “boost” to speed up their speed to eat more berries, but also\nmake the control of the Eater harder - lasts for 10s. Each berry the user eats will be worth 2 \npoints."
                +
                "\n\nPoints will be awarded every time a “BerryEater” eats a blueberry. The user wants to try to \neat 15 berries in the amount of time they have."
                +
                "\n\nEach berry eaten will be worth 1 point. (except for the berries eaten during a boost)" +
                "\n\nBerries will randomly pop up on the stage and to “eat” them you must overlap with them \nby moving with the arrow keys."
                +
                "\n\nThe first berry will always be in the same spot on the screen." +
                "\n\nIf the player touches the wall of the game, they will lose. Their score will displayed at \nthe game over scene."
                +
                "\n\nThere are 3 difficulties each allowing 45, 30, and 15 seconds to eat 15 berries. When the \ntimer ends, the game is over and your score is displayed.");
        instructionsText1.setX(5);
        instructionsText1.setY(75);
        // pauseScreen text styling
        pauseScreenText.setFill(Color.BLACK);
        pauseScreenText.setStyle("-fx-font-size: 55px;");
        pauseScreenText.setLayoutX(97);
        pauseScreenText.setLayoutY(56);
        pauseScreenText.setText("Game Paused");
        pauseScreenText.setFont(Font.font("Impact", FontWeight.BOLD, 48));
        pauseGroup.getChildren().add(pauseScreenText);
        // gameOver text styling
        gameOverText.setFill(Color.WHITE);
        gameOverText.setText("Game Over!");
        gameOverText.setStyle(
                "-fx-font-size: 75px;");
        gameOverText.setLayoutX(65);
        gameOverText.setLayoutY(260);
        gameOverGroup.getChildren().add(gameOverText);
        // gameWin text styling
        winText.setFill(Color.WHITE);
        winText.setStyle(
                "-fx-font-size: 40px;");
        winText.setText("You win, Congraduations!");
        winText.setLayoutX(32);
        winText.setLayoutY(260);
        winGroup.getChildren().add(winText);
        // timeRemaining text styling
        timeRemainingText.setFill(Color.WHITE);
        timeRemainingText.setStyle(
                "-fx-font-size: 25px;");
        timeRemainingText.setLayoutX(244);
        timeRemainingText.setLayoutY(482);
        gameGroup.getChildren().add(timeRemainingText);

        // big set on action for when the game button is pressed (main game code in
        // here)
        startGameButton.setOnAction(startButtonEvent -> {
            // add the returnToMenu button to the gameGroup if it is not already added
            if (!gameGroup.getChildren().contains(returnToMenuButton)) {
                gameGroup.getChildren().add(returnToMenuButton);
            }
            // request focus to the gameGroup/Scene to handle key presses & movement, etc
            gameGroup.requestFocus();

            // play fein
            feinPlayer.seek(Duration.ZERO);
            feinPlayer.play();

            // create all 64 background squares and add to the squares 2d array. make every
            // other square orange to add checkers
            Rectangle[][] squares = new Rectangle[8][8];
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    squares[row][col] = new Rectangle(50 * (col + 1), 50 * (row + 1), 50, 50);
                    if ((row + col) % 2 == 0) {
                        squares[row][col].setFill(Color.DARKORANGE);
                    } else {
                        squares[row][col].setFill(Color.ORANGE);
                    }
                    gameGroup.getChildren().add(squares[row][col]);
                }
            }

            // set the stage to gameScene because the start game button was pressed
            window.setScene(gameModeScene);
            // set the remaining seconds to the right time and the mode depending on the
            // button pressed
            easyButton.setOnAction(easyEvent -> {
                timerRemainingSeconds = 45;
                // set text here to prevent a frame from displaying the wrong time
                timeRemainingText.setText("Time Remaining: " + timerRemainingSeconds);
                easy = true;
                window.setScene(gameScene);
            });
            mediumButton.setOnAction(mediumEvent -> {
                timerRemainingSeconds = 30;
                timeRemainingText.setText("Time Remaining: " + timerRemainingSeconds);
                medium = true;
                window.setScene(gameScene);
            });
            hardButton.setOnAction(hardEvent -> {
                timerRemainingSeconds = 15;
                timeRemainingText.setText("Time Remaining: " + timerRemainingSeconds);
                hard = true;
                window.setScene(gameScene);
            });
            // timer that ends the game after x seconds (time to eat berries - depending on
            // game difficulty)
            gameTimer = new Timeline(new KeyFrame(Duration.seconds(1), gameTimerEvent -> {
                timerRemainingSeconds--;
                timeRemainingText.setText("Time Remaining: " + timerRemainingSeconds);
                if (timerRemainingSeconds <= 0) {
                    window.setScene(gameOverScene);
                    if (!gameOverGroup.getChildren().contains(returnToMenuButton)) {
                        gameOverGroup.getChildren().add(returnToMenuButton);
                    }
                    resetGame();
                }
            }));
            gameTimer.setCycleCount(Timeline.INDEFINITE);
            gameTimer.play();

            // handle key presses in the game scene
            gameScene.setOnKeyPressed(keyEvent -> {
                // handle/check what keys have been pressed and set the direction to the right
                // way based of the key pressed
                switch (keyEvent.getCode()) {
                    case UP:
                        // if the down is false, then do this b/c it prevents the snake from going back
                        // the other way without change in y
                        if (!down) {
                            // set the current direction, and play the sound associated
                            currentDirection[0] = Direction.UP;
                            snakeUpPlayer.seek(Duration.ZERO);
                            snakeUpPlayer.play();
                            up = true;
                            right = false;
                            left = false;
                        }
                        break;
                    case LEFT:
                        if (!right) {
                            currentDirection[0] = Direction.LEFT;
                            snakeLeftPlayer.seek(Duration.ZERO);
                            snakeLeftPlayer.play();
                            left = true;
                            up = false;
                            down = false;
                        }
                        break;
                    case DOWN:
                        if (!up) {
                            currentDirection[0] = Direction.DOWN;
                            snakeDownPlayer.seek(Duration.ZERO);
                            snakeDownPlayer.play();
                            down = true;
                            right = false;
                            left = false;
                        }
                        break;
                    case RIGHT:
                        if (!left) {
                            currentDirection[0] = Direction.RIGHT;
                            snakeRightPlayer.seek(Duration.ZERO);
                            snakeRightPlayer.play();
                            right = true;
                            up = false;
                            down = false;
                        }
                        break;
                    // start the boost timeline and makes the game 2x faster
                    case SPACE:
                        if (boostAvailable) {
                            gameLoop.setRate(2);
                            // timeline / timer to keep track of how long to boost / the duration counts
                            // down 10 seconds to allow for the timer to stop mid-game and reset before the
                            // next play (could bug out without this)
                            boostTimer = new Timeline(new KeyFrame(Duration.seconds(1), boostEvent -> {
                                boostRemainingSeconds--;
                                if (boostRemainingSeconds <= 0) {
                                    // reset the gameLoop rate to normal when the boost duration has expired
                                    gameLoop.setRate(1);
                                    boostAvailable = false;
                                    // stop the boost timer
                                    boostTimer.stop();
                                }
                            }));
                            boostTimer.setCycleCount(Timeline.INDEFINITE);
                            boostTimer.play();
                        }
                        break;
                    // pause the gameTimer and gameLoop and sets the scene to the pause scene
                    case ESCAPE:
                        gameLoop.pause();
                        gameTimer.pause();
                        window.setScene(pauseScene);
                        break;

                    default:
                        break;

                }
                // handle the resume button and start the game when pressed
                resumeButton.setOnAction(resumeButtonEvent -> {
                    window.setScene(gameScene);
                    gameTimer.play();
                    gameLoop.play();
                });
                // handle the restart button and restart the game when pressed
                restartButton.setOnAction(restartButtonEvent -> {
                    resetGame();
                    window.setScene(gameScene);
                    gameLoop.play();
                    gameTimer.play();
                });
                // handle the window close request and exit the program when a button to
                // (window.close) happens
                window.setOnCloseRequest((closeEvent) -> {
                    Platform.exit();
                });
                rageButton.setOnAction(rageEvent -> {
                    window.close();
                });
            });
            // style the berry, add it to the game group, set the location - styling
            berry = new Circle(75, 75, 20, Color.BLUE);
            gameGroup.getChildren().add(berry);
            berry.setLayoutX(berryRow * 50);
            berry.setLayoutY(berryCol * 50);

            // start the game loop and update every 325 miliseconds
            gameLoop = new Timeline(new KeyFrame(Duration.millis(325), event3 -> {
                // bring the snake parts to the front of the screen in front of the rectangle
                // background
                snakeHead.toFront();
                // get the location of the snake head in pixels and coordinates from 0-7
                // 50 offset from the border
                snakeX = snakeHead.localToScene(snakeHead.getBoundsInLocal()).getMinX() - 50;
                snakeY = snakeHead.localToScene(snakeHead.getBoundsInLocal()).getMinY() - 50;
                snakeXCol = (int) snakeX / 50;
                snakeYRow = (int) snakeY / 50;
                // add/create all 64 rectangles to the bodyArray and set every other on in a
                // checker pattern to dark or normal green
                for (int col = 0; col < 8; col++) {
                    for (int row = 0; row < 8; row++) {
                        Rectangle bodies = new Rectangle(50, 50);
                        bodies.setLayoutX(col * 50 + 50);
                        bodies.setLayoutY(row * 50 + 50);
                        if ((row + col) % 2 == 0) {
                            bodies.setFill(Color.GREEN);
                        } else {
                            bodies.setFill(Color.DARKGREEN);
                        }
                        // add them to the game group but put them behind everything so they aren't on
                        // top
                        gameGroup.getChildren().add(bodies);
                        bodyArray[col][row] = bodies;
                        bodyArray[col][row].toBack();
                    }
                }
                // handle anything that happens during left,right,up or down directions (this is
                // so the snake doesn't ever stop moving)
                switch (currentDirection[0]) {
                    case UP:
                        // move the snake the right way based off the direction x pixels
                        snakeHead.setTranslateY(snakeHead.getTranslateY() - 50);
                        // move the correct bodyparts to the front depending on the location of the
                        // snake and check if the snake has hit the border
                        miscHandler();
                        break;
                    case LEFT:
                        snakeHead.setTranslateX(snakeHead.getTranslateX() - 50);
                        miscHandler();
                        break;
                    case DOWN:
                        snakeHead.setTranslateY(snakeHead.getTranslateY() + 50);
                        miscHandler();
                        break;
                    case RIGHT:
                        snakeHead.setTranslateX(snakeHead.getTranslateX() + 50);
                        miscHandler();
                        break;
                    default:
                        break;
                }

                // send the end/tail of the snake back once the amount of bodyparts is too short
                body[i] = bodyArray[snakeXCol][snakeYRow];
                body[i - bodyParts].toBack();
                // increase the body offset/index to handle bodyparts being disconnected from
                // the snake
                i++;

                // handle actions if the snak eats a berry
                if (snakeHead.getBoundsInParent().intersects(berry.getBoundsInParent())) {
                    // add a new body segment every time the berry is eaten
                    bodyArray[snakeXCol][snakeYRow].toFront();
                    // set the location of the berry and regenerates the berry location if the snake
                    // coords and berry coords are the same
                    do {
                        // generate a new random location for the berry
                        berryRow = (int) (Math.random() * 8);
                        berryCol = (int) (Math.random() * 8);
                    } while (berryCol == snakeXCol && berryRow == snakeYRow);
                    berry.setLayoutX(berryCol * 50);
                    berry.setLayoutY(berryRow * 50);
                    // award the player 2 points per berry eaten if the boost is being used
                    if (gameLoop.getRate() == 2) {
                        score += 2;
                    } else {
                        score++;
                    }
                    // play the eating sound when the snake eats a berry
                    snakeEatPlayer.seek(Duration.ZERO);
                    snakeEatPlayer.play();
                    // set the score at the top right to the score when the player eats a berry
                    scoreText.setText("Score: " + score);
                    // increase value of the size of the sneak
                    bodyParts++;
                }
                // bring the berry to the front so it is always visible
                berry.toFront();

                // if the player has won, set the scene to winScene, add the menu button, and
                // reset the game
                if (hasWon()) {
                    window.setScene(winScene);
                    winGroup.getChildren().add(returnToMenuButton);
                    resetGame();
                }
            }));
            // keep the loop going forever while in the gameScene and after the start button
            // is pressed
            gameLoop.setCycleCount(Timeline.INDEFINITE);
            gameLoop.play();
        });
        // return the game to the main menu reset the game, and remove/add the
        // returnToMenu button as needed
        returnToMenuButton.setOnAction(returnMenuEvent -> {
            window.setScene(menuScene);
            gameGroup.getChildren().remove(returnToMenuButton);
            instructionsGroup.getChildren().remove(returnToMenuButton);
            resetGame();
        });

        // handle when the instructionsButton has been pressed
        instructionsButton.setOnAction(instructionsButtonEvent -> {
            // if the instructionsGroup already has the returnToMenu button, dont add it,
            // else, add it
            if (!instructionsGroup.getChildren().contains(returnToMenuButton)
                    && !gameGroup.getChildren().contains(returnToMenuButton)) {
                instructionsGroup.getChildren().add(returnToMenuButton);
            }
            // set the scene to instructionsScene
            window.setScene(instructionsScene);
        });
        // handle whether the window wants to close based off if exitGameButton is
        // pressed
        window.setOnCloseRequest((closeEvent) -> {
            Platform.exit();
        });
        exitGameButton.setOnAction(exitEvent -> {
            window.close();
        });

    }

    // methods

    // handle when the body needs to expand - sets the bodyArray rectangles of the
    // location of the snake to the front until all bodyparts have been done
    public static void bodyHandler() {
        for (int x = 0; x < bodyParts; x++) {
            bodyArray[snakeXCol][snakeYRow].toFront();
        }
    }

    // check if the snake has collided with the border, if so, return true
    public static boolean borderCheck() {
        // real location of the snake with no offset
        int headX = (int) snakeHead.getTranslateX();
        int headY = (int) snakeHead.getTranslateY();
        // check if the snake's head is outside the game grid boundaries
        if (headX < 0 || headX >= 400 || headY < 0 || headY >= 400) {
            // border collision detected
            return true;
        }
        // no border collision detected
        return false;
    }

    // handles the borderCheck and bodyHandler all in on method (prevents repeating
    // in the direction switch statement)
    public static void miscHandler() {
        bodyHandler();
        if (borderCheck()) {
            window.setScene(gameOverScene);
            gameOverGroup.getChildren().add(returnToMenuButton);
            resetGame();
            snakeDiePlayer.seek(Duration.ZERO);
            snakeDiePlayer.play();
        }
    }

    // reset the game variables to default values
    public static void defaultGame() {
        // reset score, boost, original berry rows and cols, snake location, berry
        // layout, and direction
        score = 0;
        scoreText.setText("Score: " + score);
        // set the other scores to the current score to be up-to-date
        boostAvailable = true;
        boostRemainingSeconds = 10;
        berryCol = 4;
        berryRow = 4;
        snakeHead.setTranslateX(0);
        snakeHead.setTranslateY(0);
        // makes sure to reset the berry layout only if the play has already been in a
        // game
        if (berry != null) {
            berry.setLayoutX(200);
            berry.setLayoutY(200);
        }
        // send all the added body (array) rectangles that are sent to the front, to the
        // back - based off the length
        for (int k = 0; k < body.length; k++) {
            // check to make sure that the loop isn't sending rectangles that don't exist to
            // the back
            if (body[k] == null) {
                break;
            } else {
                body[k].toBack();
            }
        }
        bodyParts = 0;
        i = 0;
        if (easy) {
            timerRemainingSeconds = 45;
        } else if (medium) {
            timerRemainingSeconds = 30;
        } else if (hard) {
            timerRemainingSeconds = 15;
        }
        timeRemainingText.setText("Time Remaining: " + timerRemainingSeconds);
        up = false;
        down = false;
        right = false;
        left = false;
        currentDirection[0] = Direction.BEGINNING;
    }

    // reset the game completely - when the player loses or
    // returns to menu
    public static void resetGame() {
        // stop the game loop and game timer
        gameLoop.stop();
        gameTimer.stop();
        boostTimer.stop();
        defaultGame();
    }

    // check if the player has won (if the score is 15 or higher to account for
    // boost points)
    public static boolean hasWon() {
        if (score >= 15) {
            return true;
        } else {
            return false;
        }

    }

    // main method to run the game
    public static void main(String[] args) {
        launch(args);
    }
}