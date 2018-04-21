package com.TimUdemy;

public class Main {

    public static void main(String[] args) {
//        boolean gameOver = true;
//        int score = 800;
//        int levelCompleted = 5;
//        int bonus = 100;


        int highScorePosition = calculateHighScorePosition(1500);
        displayHighScorePosition("vamsi", highScorePosition);

        highScorePosition = calculateHighScorePosition(900);
        displayHighScorePosition("bhaskar", highScorePosition);

        highScorePosition = calculateHighScorePosition(400);
        displayHighScorePosition("Bob", highScorePosition);

        highScorePosition = calculateHighScorePosition(400);
        displayHighScorePosition("Percy", highScorePosition);

        highScorePosition = calculateHighScorePosition(50);
        displayHighScorePosition("Gilbert", highScorePosition);

        highScorePosition = calculateHighScorePosition(1000);
        displayHighScorePosition("Tom", highScorePosition);


        highScorePosition = calculateHighScorePosition(500);
        displayHighScorePosition("Brown", highScorePosition);


        highScorePosition = calculateHighScorePosition(100);
        displayHighScorePosition("Gibby", highScorePosition);





    }

    public static void displayHighScorePosition(String PlayerName, int highScorePosition) {

        System.out.println(PlayerName + " managed to get into position " + "" + highScorePosition + "" + " on the high score table");


    }

    public static int calculateHighScorePosition(int playerScore) {

        if (playerScore >= 1000) {
            return 1;
        } else if (playerScore >= 500 && playerScore < 1000) {
            return 2;
        } else if (playerScore >= 100 && playerScore < 500) {
            return 3;
        } else {
            return 4;
        }
    }

//    public static int calculateScore(boolean gameOver, int score, int levelCompleted, int bonus) {
//
//        if (gameOver) {
//            int finalScore = score + (levelCompleted * bonus);
//            finalScore += 2000;
//
//            return finalScore;
//
//        }
//        return -1;
//    }
}


