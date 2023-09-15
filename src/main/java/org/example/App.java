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
    public static void main(String[] args) {
        List<String> queries = loadFile("queries.txt");

        if (queries.isEmpty()) {
            System.out.println("Error: Failed to load data.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        System.out.println("ChatGPT - " + ChatGPTClient.sendRequest("привіт"));

        int step = 0;
        User user = new User();

        while (true) {
            String[] parts = queries.get(step).split("#");
            System.out.println("ChatBot - " + parts[0]);
            if (step == 20 || step == 18) {
                break;
            }

            if (parts[1].contains("next")) {
                step += 1;
                continue;
            }

            if (step == 14) {
                System.out.println(user.toString());
            }

            String nikeName = (user.getName() != null) ? user.getName() : "Guest";
            System.out.print("                           " + nikeName + ": ");
            String userMessage = scanner.nextLine();
             if (userMessage.isEmpty()) {
                 System.out.println("I didn't understand what you mean. Please try again!");
                continue;
            } else if (parts[1].contains("noEmpty") && !userMessage.isEmpty()) {
                String[] cases = parts[1].split("-");
                step = getStep(cases, queries);

                 if (parts[0].contains("name")) {
                     user.setName(userMessage);
                 } else if (parts[0].contains("date")) {
                     user.setDate(userMessage);
                 } else if (parts[0].contains("phone")) {
                     user.setPhone(userMessage);
                 } else {
                     ChatGPTClient.sendRequest("The short answer about this -" + userMessage);
                 }
            } else if (parts[1].toLowerCase().contains("yes")
                     && userMessage.toLowerCase().contains("yes")) {
                String[] cases = parts[1].split("-");
                step = getStep(cases, queries);
            } else if (parts[2].toLowerCase().contains("no")
                     &&  userMessage.toLowerCase().contains("no")) {
                String[] cases = parts[2].split("-");
                step = getStep(cases, queries);
            } else {
                System.out.println("I didn't understand what you mean. Please try again!");
             }
            System.out.println("**********************************");
        }
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
