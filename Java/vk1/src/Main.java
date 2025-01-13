import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Random randelin = new Random();
        int correct = randelin.nextInt(100) + 1;

        int guesses = 7;
        int userGuess;
        boolean runloop = true;

        while (runloop) {
            System.out.println("Guess a number between 1 and 100:");
            userGuess = Integer.parseInt(System.console().readLine());
            if (userGuess == correct) {
                runloop = false;
                System.out.println("You guessed correctly! Congratulations!");
            } else {
                guesses--;
                System.out.println("Incorrect. You have " + guesses + " guesses left.");
            }

            if (guesses == 0) {
                runloop = false;
                System.out.println("Game over!");
            }
        }
    }
}