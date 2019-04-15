package app.main;

import graph.utils.Edge;
import graph.utils.Graph;
import javafx.util.Pair;
import model.Atm;
import model.CardType;
import model.CreditCard;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.*;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("all")
public class HamiltonianPaths {
    private final static String USER_START = "UserStart";
    private final static int NUMBER_HAM_PATHS = 100;

    private static ArrayList<Atm>[] hamiltonianPaths = new ArrayList[NUMBER_HAM_PATHS];
    private static ArrayList<Atm> atmList = new ArrayList<Atm>();

    private static HashMap<Pair<Atm, Atm>, Integer> atmDistances = new HashMap<Pair<Atm, Atm>, Integer>(); // <ATM1 to ATM2> and Distance Value
    private static List<Atm> atmMinimumRoutes;
    private static ArrayList<CreditCard> creditCards = new ArrayList<CreditCard>();


    private static int counter = 0;
    private static int minimumTotalDuration = Integer.MAX_VALUE;

    private static void initializeData() {
        Atm start_point = new Atm(USER_START, new LocalTime(), new LocalTime(), 0, false, false);
        Atm atm_1 = new Atm("ATM 1", new LocalTime("12:00"), new LocalTime("18:00"), 5000, false, false);
        Atm atm_2 = new Atm("ATM 2", new LocalTime("10:00"), new LocalTime("17:00"), 5000, false, false);
        Atm atm_3 = new Atm("ATM 3", new LocalTime("22:00"), new LocalTime("13:00"), 5000, false, false);
        Atm atm_4 = new Atm("ATM 4", new LocalTime("17:00"), new LocalTime("01:00"), 5000, false, false);

        // add ATM's
        atmList.add(start_point);
        atmList.add(atm_1);
        atmList.add(atm_2);
        atmList.add(atm_3);
        atmList.add(atm_4);

        // initializa distances HasMap
        // from to
        atmDistances.put(new Pair<Atm, Atm>(start_point, atm_1), 5);
        atmDistances.put(new Pair<Atm, Atm>(start_point, atm_2), 60);
        atmDistances.put(new Pair<Atm, Atm>(start_point, atm_3), 30);
        atmDistances.put(new Pair<Atm, Atm>(start_point, atm_4), 45);

        atmDistances.put(new Pair<Atm, Atm>(atm_1, atm_2), 40);
        atmDistances.put(new Pair<Atm, Atm>(atm_1, atm_4), 45);

        atmDistances.put(new Pair<Atm, Atm>(atm_2, atm_3), 15);

        atmDistances.put(new Pair<Atm, Atm>(atm_3, atm_1), 40);
        atmDistances.put(new Pair<Atm, Atm>(atm_3, atm_4), 15);

        atmDistances.put(new Pair<Atm, Atm>(atm_4, atm_2), 30);

        // to from
        /*atmDistances.put(new Pair<Atm, Atm>(atm_2, atm_1), 40);
        atmDistances.put(new Pair<Atm, Atm>(atm_4, atm_1), 45);

        atmDistances.put(new Pair<Atm, Atm>(atm_3, atm_2), 15);
        atmDistances.put(new Pair<Atm, Atm>(atm_1, atm_3), 40);

        atmDistances.put(new Pair<Atm, Atm>(atm_4, atm_3), 15);
        atmDistances.put(new Pair<Atm, Atm>(atm_2, atm_4), 30);*/


        // initialize credit cards
        CreditCard creditCard_1 = new CreditCard(CardType.SILVER, 0.2d, 2000, new LocalDate("2020-05-23"), 20000);
        CreditCard creditCard_2 = new CreditCard(CardType.GOLD, 0.1d, 2000, new LocalDate("2018-08-15"), 25000);
        CreditCard creditCard_3 = new CreditCard(CardType.PLATINUM, 0.0d, 2000, new LocalDate("2019-03-20"), 3000);
        creditCards.add(creditCard_1);
        creditCards.add(creditCard_2);
        creditCards.add(creditCard_3);
    }


    private static void initializePathArray() {
        for (int i = 0; i < NUMBER_HAM_PATHS; i++) {
            hamiltonianPaths[i] = new ArrayList<Atm>();
        }
    }


    private static boolean isCreditCardExpried(CreditCard creditCard, LocalDate currentDate) {
        return (creditCard.getExpirationDate()).isBefore(currentDate);
    }

    private static int withdrawMoney(CreditCard creditCard, Atm atm, int withdrawNeded) {
        int availableMoney = creditCard.getAvailableAmount();
        int withdrawLimit = (int) (creditCard.getWithdrawLimit() - creditCard.getFee() * creditCard.getWithdrawLimit());
        int withdrawTotalAmount = 0;

        System.out.println("Limita= " + creditCard.getWithdrawLimit() + " Pot sa extrag= " + withdrawLimit + " Mai am nevoie de = " + withdrawNeded);
        while (availableMoney > 0 && withdrawTotalAmount < withdrawNeded) {
            // daca am mai mult decat limita pe card, retrag limita de 2000 - 2000*fee
            if (availableMoney >= withdrawLimit) {
                if ((withdrawTotalAmount + withdrawLimit) > withdrawNeded) {
                    withdrawLimit = withdrawNeded - withdrawTotalAmount;
                }
                withdrawTotalAmount += withdrawLimit;
                availableMoney -= withdrawLimit;

                System.out.println("Am retras [" + withdrawLimit + "] lei"); // retrag 2000 lei

                atm.setMoneyAmount(atm.getMoneyAmount() - withdrawLimit);

            } else if (availableMoney > 0) { // daca mai am bani retrag cat am
                System.out.println("Am retras [" + availableMoney + "] lei"); // retrag cat mai am
                withdrawTotalAmount += availableMoney - creditCard.getFee() * availableMoney;
                availableMoney -= availableMoney;

                atm.setMoneyAmount(atm.getMoneyAmount() - availableMoney);
            }
        }
        System.out.println("Am retras in total de la " + atm.getName() + " [" + withdrawTotalAmount + "] lei cu card: " + creditCard.getCardType()); // retrag cat mai a
        creditCard.setAvailableAmount(atm.getMoneyAmount() - withdrawTotalAmount);
        return withdrawTotalAmount;
    }

    private static void computeAllHamiltonianPaths(Graph g, int v,
                                                   boolean[] visited, List<Integer> path, int N) {
        // if all the vertices are visited, then
        // hamiltonian path exists
        if (path.size() == N) {
            // compute hamiltonian path duration
            int totalDuration = 0;
            int withDrawAmount = 8000;
            int numberOfPaths = 0;
            //  Current date and time is: *19th March, 2019 - 11:30*
            LocalTime localTime = new LocalTime(11, 30);
            LocalDate localDate = new LocalDate(2019, 3, 19);
            for (int i = 0; i < path.size() - 1; i++) {
                //System.out.println( " DateTime="+  dateTime );
                Atm atmFrom = atmList.get(path.get(i));
                Atm atmTo = atmList.get(path.get(i + 1));

                Pair<Atm, Atm> fromToAtmPair = new Pair<Atm, Atm>(atmFrom, atmTo);
                //System.out.println("Pair=" +pair.getKey() +"  Value= " + pair.getValue());

                for (Map.Entry entry : atmDistances.entrySet()) {
                    Pair<Atm, Atm> pairKey = (Pair<Atm, Atm>) entry.getKey();
                    if (fromToAtmPair.equals(pairKey)) { // we found the atms in hasmap
                        numberOfPaths++;

                        int fromToAtmDistance = (Integer) entry.getValue();
                        totalDuration += fromToAtmDistance;
                        localTime = localTime.plusMinutes(totalDuration);

                        LocalTime atmToOpeningTime = atmTo.getOpeningTime();
                        LocalTime atmToClosingTime = atmTo.getClosingTime();
                        if (localTime.isBefore(atmToOpeningTime) || localTime.isAfter(atmToClosingTime)) {
                            // atmu e inchis, userul asteapta
                            long waitingTimeMillis = Math.abs(atmToOpeningTime.getMillisOfDay() - localTime.getMillisOfDay());
                            int waitingTimeMinutes = (int) TimeUnit.MILLISECONDS.toMinutes(waitingTimeMillis);
                            System.out.println("From= [" + atmFrom.getName() + "] TO [" + atmTo.getName() + "] Distance= " + entry.getValue() + " La ora: [" + localTime + "] ATMu e Inchis, waiting time =" + waitingTimeMinutes);
                            // add the waiting duration
                            totalDuration += waitingTimeMinutes;
                            // add the waiting minutes to local time
                            localTime = localTime.plusMinutes(waitingTimeMinutes);
                        } //else
                        //{ // atmu e deschis retrage bani
                        System.out.println("From= [" + atmFrom.getName() + "] TO [" + atmTo.getName() + "] Distance= " + entry.getValue() + " La ora: [" + localTime + "] ATMu e deschis");


                        CreditCard silverCard = creditCards.get(0);
                        CreditCard goldCard = creditCards.get(1);
                        CreditCard platinumCard = creditCards.get(2);

                        //System.out.println("Este expirat platinum= " + isCreditCardExpried(platinumCard, localDate) + " data = " + platinumCard.getExpirationDate() + "  localdate=" + localDate);
                        //System.out.println("Este expirat goldu= " + isCreditCardExpried(goldCard, localDate) + " data = " + goldCard.getExpirationDate() + " localdate=" + localDate);
                        //System.out.println("Este expirat silveru= " + isCreditCardExpried(silverCard, localDate) + " data = " + silverCard.getExpirationDate() + " localdate=" + localDate);

                        if (!isCreditCardExpried(platinumCard, localDate) && platinumCard.getAvailableAmount() > 0) {
                            // daca Platinum nu e expirat
                            withDrawAmount -= withdrawMoney(platinumCard, atmTo, withDrawAmount);
                        } else if (!isCreditCardExpried(goldCard, localDate) && goldCard.getAvailableAmount() > 0) {
                            // daca goldu nu e expirat
                            withDrawAmount -= withdrawMoney(goldCard, atmTo, withDrawAmount);
                        } else if (!isCreditCardExpried(silverCard, localDate) && silverCard.getAvailableAmount() > 0) {
                            // daca silveru nu e expirat
                            withDrawAmount -= withdrawMoney(silverCard, atmTo, withDrawAmount);
                        }
                        //}
                    }
                }

                //System.out.print(atmDistances.get(pair));
                //System.out.print(" Distance= "+distance);

                hamiltonianPaths[counter].add(atmList.get(path.get(i)));
                //System.out.print(i + " ");
            } // end for
            // add the last path
            hamiltonianPaths[counter].add(atmList.get(path.get(path.size() - 1)));

            if ((totalDuration < minimumTotalDuration)) {
                if (numberOfPaths == 4) { // daca am parcurs toate cele 4 atmuri atunci salvez distanta minima si pathul
                    minimumTotalDuration = totalDuration;
                    atmMinimumRoutes = hamiltonianPaths[counter];
             /*      System.out.print("Route: ");
                    for (Atm atm : atmRoutes) {
                        System.out.print("[" + atm.getName() + "] ");
                    }*/
                }
            }

            System.out.println("Total duration for path [" + counter + "]= " + totalDuration);
            System.out.println("Number of paths = " + numberOfPaths);
            System.out.println();

            counter++;

            return;
        }

        // Check if every edge starting from vertex v leads
        // to a solution or not
        for (int w : g.adjList.get(v)) {
            // process only unvisited vertices as Hamiltonian
            // path visits each vertex exactly once
            if (!visited[w]) {
                visited[w] = true;
                path.add(w);

                // check if adding vertex w to the path leads
                // to solution or not
                computeAllHamiltonianPaths(g, w, visited, path, N);

                // Backtrack
                visited[w] = false;
                path.remove(path.size() - 1);
            }
        }
    }

    public static void atmProblemStart() {
        // vector of graph edges
        List<Edge> edges = Arrays.asList(
                new Edge(0, 1), new Edge(0, 2), new Edge(0, 3), new Edge(0, 4),
                new Edge(1, 2), new Edge(1, 4),
                new Edge(2, 3),
                new Edge(3, 1), new Edge(3, 4),
                new Edge(4, 2)
        );

        // Set number of vertices in the graph
        final int N = 5;

        // create a graph from edges
        Graph g = new Graph(edges, N);

        // starting node
        int start = 0;

        // add starting node to the path
        List<Integer> path = new ArrayList<Integer>();
        path.add(start);

        // mark start node as visited
        boolean[] visited = new boolean[N];
        visited[start] = true;

        initializeData();

        initializePathArray();

        computeAllHamiltonianPaths(g, start, visited, path, N);

        System.out.println("All hamiltonian paths:");
        for (int i = 0; i < counter; i++) {
            System.out.print("Path[" + i + "] = ");
            for (Atm atm : hamiltonianPaths[i]) {
                System.out.print(" " + atm.getName() + " ");
            }
            System.out.println();
        }

        // TODO: implemented function
        new HamiltonianPaths().getAtmsRoute();
    }

    public List<Atm> getAtmsRoute() {
        // TODO: add your code here
        System.out.println("\nMinimum route:");
        for (Atm atm : atmMinimumRoutes) {
            System.out.print("[" + atm.getName() + "] ");
        }
        System.out.println("With Duration= [" + minimumTotalDuration + "]");

        return atmMinimumRoutes;
    }
}
