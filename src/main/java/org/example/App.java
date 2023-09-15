package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Hello world!
 *
 */
public class App
{
    private static final List<Integer> TERMINATION_STEPS = Arrays.asList(18, 20);
    public static void main(String[] args) {
        List<String> queries = loadFile("queries.txt");
        checkQueries(queries);

        Scanner scanner = new Scanner(System.in);
        User user = new User();

        int step = 0;
        while (true) {
            String[] parts = queries.get(step).split("#");
            System.out.println("ChatBot - " + parts[0]);

            if (isTerminationStep(step)) break;

            if (parts[1].contains("next")) {
                step++;
                continue;
            }

            if (step == 14) System.out.println(user.toString());

            String userMessage = getUserMessage(user.getName(), scanner);

            step = handleUserMessage(parts, userMessage, step, queries, user);

            System.out.println("**********************************");
        }
    }

    private static void checkQueries(List<String> queries) {
        if (queries.isEmpty()) {
            System.out.println("Error: Failed to load data.");
            System.exit(0);
        }
    }

    private static boolean isTerminationStep(int step) {
        return TERMINATION_STEPS.contains(step);
    }

    private static String getUserMessage(String userName, Scanner scanner) {
        String nickname = (userName != null) ? userName : "Guest";
        System.out.print("                           " + nickname + ": ");
        return scanner.nextLine();
    }

    private static int handleUserMessage(String[] parts, String userMessage, int step, List<String> queries, User user) {
        if (userMessage.isEmpty()) {
            System.out.println("I didn't understand what you mean. Please try again!");
        } else if (parts[1].contains("noEmpty") && !userMessage.isEmpty()) {
            step = processNotEmpty(parts, userMessage, queries, user);
        } else if (responseContains(parts[1], "yes", userMessage) || responseContains(parts[2], "no", userMessage)) {
            step = getStep(parts[1].split("-"), queries);
        } else {
            System.out.println("I didn't understand what you mean. Please try again!");
        }
        return step;
    }

    private static int processNotEmpty(String[] parts, String userMessage, List<String> queries, User user) {
        switch (parts[0]) {
            case "name":
                user.setName(userMessage);
                break;
            case "date":
                user.setDate(userMessage);
                break;
            case "phone":
                user.setPhone(userMessage);
                break;
            default:
                ChatGPTClient.sendRequest("The short answer about this -" + userMessage);
                break;
        }
        return getStep(parts[1].split("-"), queries);
    }

    private static boolean responseContains(String part, String keyword, String userMessage) {
        return part.toLowerCase().contains(keyword) && userMessage.toLowerCase().contains(keyword);
    }

    private static int getStep(String[] cases, List<String> queries) {
        int step;
        if (cases.length == 3) {
            int caseId = Integer.parseInt(cases[1]);
            System.out.println(queries.get(caseId));
            step = Integer.parseInt(cases[2]);
        } else {
            step = Integer.parseInt(cases[1]);
        }
        return step;
    }

    public static List<String> loadFile(String filename) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("File upload error " + filename);
        }
        return lines;
    }
}
