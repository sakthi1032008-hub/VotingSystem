package com.voting.main;

import com.voting.exception.VotingException;
import com.voting.model.*;
import com.voting.service.VotingService;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        VotingService service = new VotingService();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== VOTING MANAGEMENT SYSTEM =====");
            System.out.println("1. Register Voter");
            System.out.println("2. Add Candidate");
            System.out.println("3. View Voters & Candidates");
            System.out.println("4. Cast Vote");
            System.out.println("5. View Results");
            System.out.println("6. Declare Winner");
            System.out.println("7. Exit");
            System.out.print("Choice: ");

            try {
                int choice = Integer.parseInt(sc.nextLine().trim());
                switch (choice) {
                    case 1 -> {
                        System.out.print("Voter ID: ");   int id = Integer.parseInt(sc.nextLine().trim());
                        System.out.print("Name: ");       String name = sc.nextLine();
                        System.out.print("Age: ");        int age = Integer.parseInt(sc.nextLine().trim());
                        System.out.print("City: ");       String city = sc.nextLine();
                        service.registerVoter(new Voter(id, name, age, city, false));
                        System.out.println("Voter registered successfully.");
                    }
                    case 2 -> {
                        System.out.print("Candidate ID: "); int id = Integer.parseInt(sc.nextLine().trim());
                        System.out.print("Name: ");         String name = sc.nextLine();
                        System.out.print("Age: ");          int age = Integer.parseInt(sc.nextLine().trim());
                        System.out.print("Party: ");        String party = sc.nextLine();
                        service.addCandidate(new Candidate(id, name, age, party, 0));
                        System.out.println("Candidate added successfully.");
                    }
                    case 3 -> {
                        // Polymorphism: one list of Person, each object prints itself differently
                        List<Person> people = new ArrayList<>();
                        people.addAll(service.getAllVoters());
                        people.addAll(service.getResults());
                        for (Person p : people) p.displayInfo();
                    }
                    case 4 -> {
                        System.out.print("Your Voter ID: ");  int vId = Integer.parseInt(sc.nextLine().trim());
                        for (Candidate c : service.getResults()) c.displayInfo();
                        System.out.print("Candidate ID to vote for: ");
                        int cId = Integer.parseInt(sc.nextLine().trim());
                        service.castVote(vId, cId);
                        System.out.println("Vote cast successfully!");
                    }
                    case 5 -> {
                        System.out.println("--- Candidate Results ---");
                        service.getResults().forEach(Candidate::displayInfo);
                        System.out.println("--- Party Totals ---");
                        service.getPartyTotals().forEach((party, v) ->
                                System.out.println(party + " : " + v));
                    }
                    case 6 -> {
                        Optional<Candidate> w = service.getWinner();
                        if (w.isPresent()) {
                            System.out.println("WINNER: " + w.get().getName()
                                    + " (" + w.get().getParty() + ") with " + w.get().getVotes() + " votes");
                        } else {
                            System.out.println("No votes have been cast yet.");
                        }
                    }
                    case 7 -> { System.out.println("Goodbye!"); sc.close(); return; }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: please enter a valid number.");
            } catch (VotingException e) {          // custom exceptions (and subclasses)
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e);
            }
        }
    }
}
