import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Birthday {
    public static void main(String[] args) {
        String envBirthdate = System.getenv("BIRTHDATE");

        if (envBirthdate == null) {
            System.out.println("BIRTHDATE-ympäristömuuttujaa ei löytynyt.");
            System.out.println("Insert enviroment variable in run configurations.");
            return;
        }
        try{
            LocalDate localBirthdate = LocalDate.parse(envBirthdate);
            LocalDate today = LocalDate.now();



        if (today.equals(localBirthdate)){
            System.out.println("Congratulations! Today is your birthday.");
            return;
        }
        if (localBirthdate.isAfter(today)) {
            System.out.println("Wow you haven't been born yet!");
            return;
        }
        if (localBirthdate.isBefore(today)) {
            long daysOld = ChronoUnit.DAYS.between(localBirthdate, today);
            System.out.println("You are " + daysOld + " days old.");
            if (daysOld % 1000 == 0) {
                System.out.println("That's a nice round number!");
            }
        }

        } catch (Exception e) {
            System.out.println("Provided date wasn't valid.");
        }

    }
}
