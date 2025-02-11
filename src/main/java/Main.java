import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String typeSubstring;
        String[] commands = { "echo", "exit", "type" };
        String[] str = input.split(" ");

        String parameter = "";
        if (str.length > 2) {
            for (int i = 1; i < str.length; i++) {
            if (i < str.length - 1) {
                parameter += str[i] + " ";
            } else {
                parameter += str[i];
            }
            }
        } else if (str.length > 1) {
            parameter = str[1];
        }

        while (true) {
            System.out.print("$ ");
            input = scanner.nextLine();
            if (input.equals("exit 0")) {
                break;
            } else if (input.startsWith("echo")) {
                System.out.println(input.substring(5));
            } else if (input.startsWith("type")) {
                typeSubstring = input.substring(5);
                if (Arrays.asList(commands).contains(typeSubstring)) {
                    System.out.println(typeSubstring + " is a shell builtin");
                } else {
                    String path = getPath(parameter);
                    if (path != null) {
                        System.out.println(parameter + " is " + path);
                    } else {
                        System.out.println(parameter + ": not found");
                    }
                }
            } else {
                System.out.println(input + ": command not found");
            }
        }
    }

    private static String getPath(String parameter) {
        for (String path : System.getenv("PATH").split(":")) {
            Path fullPath = Path.of(path, parameter);
            if (Files.isRegularFile(fullPath)) {
                return fullPath.toString();
            }
        }
        return null;
    }
}