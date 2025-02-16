import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        List<String> commandList = List.of("cd", "exit", "echo", "type", "pwd");
        String[] command;
        String[] paths = System.getenv("PATH").split(File.pathSeparator);
        while (true) {
            System.out.print("$ ");
            command = scanner.nextLine().split(" ");
            switch (command[0]) {
                case "exit":
                    if (command.length > 1) {
                        scanner.close();
                        System.exit(Integer.parseInt(command[1]));
                    }
                    scanner.close();
                    System.exit(0);
                case "echo":
                    StringBuilder output = new StringBuilder();
                    for (int i = 1; i < command.length; i++) {
                        output.append(command[i]).append(" ");
                    }
                    System.out.println(output.toString().trim());
                    break;
                case "type":
                    if (commandList.contains(command[1])) {
                        System.out.println(command[1] + " is a shell builtin");
                    } else {
                        boolean fileFound = false;
                        for (String path : paths) {
                            File file = new File(path, command[1]);
                            if (file.exists()) {
                                fileFound = true;
                                System.out.println(command[1] + " is " + file.getAbsolutePath());
                                break;
                            }
                        }
                        if (!fileFound) {
                            System.out.println(command[1] + ": not found");
                        }
                    }
                    break;
                case "pwd":
                    System.out.println(System.getProperty("user.dir"));
                    break;
                case "cd":
                if (command.length < 2) {
                    // If no argument is provided, change to the user's home directory
                    System.setProperty("user.dir", System.getProperty("user.home"));
                } else {
                    String newDir = command[1];
                    Path newPath = Path.of(newDir);
                    if (Files.isDirectory(newPath)) {
                        // Change the current working directory
                        System.setProperty("user.dir", newPath.toAbsolutePath().toString());
                    } else {
                        System.out.println("cd: " + newDir + ": No such file or directory");
                    }
                }
                break;
                default:
                    File file;
                    boolean fileFound = false;
                    for (String path : paths) {
                        file = new File(path, command[0]);
                        if (file.exists()) {
                            fileFound = true;
                            break;
                        }
                    }
                    if (fileFound) {
                        ProcessBuilder processBuilder = new ProcessBuilder();
                        for (String arg : command) {
                            processBuilder.command().add(arg);
                        }
                        Process process = processBuilder.start();
                        process.getInputStream().transferTo(System.out);
                        break;
                    } else {
                        System.out.println(command[0] + ": command not found");
                    }
            }
        }
    }
}