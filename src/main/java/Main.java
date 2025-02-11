import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static final String[] BUILTIN_COMMANDS = { "echo", "exit", "type" };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("$ ");
            String input = scanner.nextLine().trim();
            if (input.equals("exit 0")) {
                break;
            }

            String[] str = input.split(" ", 2);
            String command = str[0];
            String parameter = (str.length > 1) ? str[1] : "";

            if (command.equals("echo")) {
                System.out.println(parameter);
            } else if (command.equals("type")) {
                handleTypeCommand(parameter);
            } else {
                System.out.println(command + ": command not found");
            }
        }
        scanner.close();
    }

    private static void handleTypeCommand(String parameter) {
        if (parameter.isEmpty()) {
            System.out.println("type: missing operand");
            return;
        }

        if (Arrays.asList(BUILTIN_COMMANDS).contains(parameter)) {
            System.out.println(parameter + " is a shell builtin");
            return;
        }

    
        String executablePath = findExecutableInPath(parameter);
        if (executablePath != null) {
            System.out.println(parameter + " is " + executablePath);
        } else {
            System.out.println(parameter + ": not found");
        }
    }

    private static String findExecutableInPath(String command) {
        String pathEnv = System.getenv("PATH");
        if (pathEnv == null || pathEnv.isEmpty()) {
            return null;
        }

        for (String dir : pathEnv.split(":")) {  
            Path fullPath = Path.of(dir, command);
            if (Files.isRegularFile(fullPath) && Files.isExecutable(fullPath)) {
                return fullPath.toString();
            }
        }
        return null;
    }
}
