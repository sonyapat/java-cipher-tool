import java.util.Scanner;
/**
 * This class serves as the driver.
 * It will be used to perform encryption and decryption operations.
 *
 * @author Sonya Pathania
 * @version 1.0
 */
public class EncryptorDriver {
    /** Main method driving all of the files.
     *
     * @param args an array of command-line arguments
    */
    public static void main(String[] args) {
        String previous = "";
        String input = "";
        System.out.println("Get Started with the Encryption Tool!");
        System.out.println("Available Commands:");
        System.out.println("encode <scheme> <parameter> [target text]");
        System.out.println("decode <scheme> <parameter> [target text]");
        System.out.println("stats");
        System.out.println("exit");
        Scanner scan = new Scanner(System.in);
        while (!(input.equals("exit"))) {
            System.out.print("> ");
            input = scan.nextLine();

            String scheme = "";
            int parameter = 0;
            String text = "";
            Scanner commandScan = new Scanner(input);
            String command = commandScan.next();

            if (command.equals("exit")) {
                System.out.println("Goodbye!");
            } else if (command.equals("stats")) {
                System.out.println(Encryptor.encryptionTallyString());
            } else if (command.equals("encode") || command.equals("decode")) {
                scheme = commandScan.next();
                parameter = commandScan.nextInt();
                if (commandScan.hasNext()) {
                    text = commandScan.nextLine().trim();
                } else if (previous.isEmpty()) {
                    text = "my encryption string";
                } else {
                    text = previous;
                }
                if (scheme.equals("basen")) {
                    Encryptor e = new Encryptor(Scheme.BASE_N);
                    if (command.equals("encode")) {
                        previous = e.encode(text, parameter);
                        System.out.println(previous);
                    } else {
                        previous = e.decode(text, parameter);
                        System.out.println(previous);
                    }
                } else if (scheme.equals("caesar")) {
                    Encryptor f = new Encryptor(Scheme.CAESAR);
                    if (command.equals("encode")) {
                        previous = f.encode(text, parameter);
                        System.out.println(previous);
                    } else {
                        previous = f.decode(text, parameter);
                        System.out.println(previous);
                    }
                } else if (scheme.equals("rotate")) {
                    Encryptor g = new Encryptor(Scheme.ROTATE);
                    if (command.equals("encode")) {
                        previous = g.encode(text, parameter);
                        System.out.println(previous);
                    } else {
                        previous = g.decode(text, parameter);
                        System.out.println(previous);
                    }
                }
            } else {
                System.out.println("Error: Invalid command");
            }
            commandScan.close();
        }
        scan.close();
    }
}