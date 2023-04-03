package battleship;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Write your code here
        Field player1 = new Field(10,10);
        for (int i = 1; i <= 5 ; i++) {
            player1.printField(false);
            player1.inputShipPosition(i);
        }
        player1.printField(false);
        Main.pressEnter();
        Field player2 = new Field(10,10);
        for (int i = 1; i <= 5 ; i++) {
            player2.printField(false);
            player2.inputShipPosition(i);
        }
        player2.printField(false);

        int winner = 0; //1-p1,2-p2
        boolean p1Plays = true;
        while(winner == 0) {
            Main.pressEnter();
            if (p1Plays) {
                player2.printField(true);
                System.out.println("---------------------");
                player1.printField(false);
                System.out.println("Player 1, it's your turn:");
                player2.takeAShoot();
                if (player2.shipsToDestroy() == 0) winner = 1;
                p1Plays = false;
            } else {
                player1.printField(true);
                System.out.println("---------------------");
                player2.printField(false);
                System.out.println("Player 2, it's your turn:");
                player1.takeAShoot();
                if (player1.shipsToDestroy() == 0) winner = 2;
                p1Plays = true;
            }
        }

        if (winner == 1) {
            System.out.println("You sank the last ship. You won. Congratulations!");
        } else if (winner == 2) {
            System.out.println("You sank the last ship. You won. Congratulations!");
        }
    }

    public static void pressEnter() {
        while (true) {
            System.out.println("Press Enter and pass the move to another player");
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();
            if (input.equals("")) break;
        }
    }
}
