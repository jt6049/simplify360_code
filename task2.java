import java.util.*;

public class task2 {
    Map<String, Set<String>> friends = new HashMap<>();

    static class Pair<K, V> {
        String key;
        int value;

        Pair(String key, int value) {
            this.key = key;
            this.value = value;
        }

        String getKey() {
            return key;
        }

        int getValue() {
            return value;
        }
    }

    void addFriendship(String person1, String person2) {// O(1) O(N+E)
        friends.putIfAbsent(person1, new HashSet<>());
        friends.putIfAbsent(person2, new HashSet<>());
        friends.get(person1).add(person2);
        friends.get(person2).add(person1);
    }

    Set<String> getFriends(String person) {// O(1) O(1)
        return friends.get(person);
    }

    Set<String> getCommonFriends(String person1, String person2) {// O(Common)

        Set<String> common = new HashSet<>(friends.get(person1));
        Set<String> friendsOfPerson1 = friends.get(person1);
        Set<String> friendsOfPerson2 = friends.get(person2);

        for (String friend : friendsOfPerson1) {
            if (friendsOfPerson2.contains(friend)) {
                common.add(friend);
            }
        }
        return common;
    }

    int findConnection(String person1, String person2) {// O(N+E) O(N)
        if (person1.equals(person2))
            return 0;
        Set<String> visited = new HashSet<>();
        Queue<Pair<String, Integer>> queue = new LinkedList<>();
        queue.add(new Pair<>(person1, 0));
        visited.add(person1);

        while (!queue.isEmpty()) {
            Pair<String, Integer> current = queue.remove();
            String currentPerson = current.getKey();
            int level = current.getValue();

            for (String friend : friends.get(currentPerson)) {
                if (friend.equals(person2))
                    return level + 1;
                if (!visited.contains(friend)) {
                    visited.add(friend);
                    queue.add(new Pair<>(friend, level + 1));
                }
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        task2 network = new task2();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("Menu:");
            System.out.println("1. Add friendship");
            System.out.println("2. Get friends");
            System.out.println("3. Get common friends");
            System.out.println("4. Find connection");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                System.out.print("Enter the first person: ");
                String person1 = scanner.nextLine();
                System.out.print("Enter the second person: ");
                String person2 = scanner.nextLine();
                network.addFriendship(person1, person2);
            } else if (choice == 2) {
                System.out.print("Enter the person's name: ");
                String person = scanner.nextLine();
                Set<String> friends = network.getFriends(person);
                System.out.println("Friends of " + person + ": " + friends);
            } else if (choice == 3) {
                System.out.print("Enter the first person: ");
                String person1 = scanner.nextLine();
                System.out.print("Enter the second person: ");
                String person2 = scanner.nextLine();
                Set<String> commonFriends = network.getCommonFriends(person1, person2);
                System.out.println("Common friends of " + person1 + " and " + person2 + ": " + commonFriends);
            } else if (choice == 4) {
                System.out.print("Enter the first person: ");
                String person1 = scanner.nextLine();
                System.out.print("Enter the second person: ");
                String person2 = scanner.nextLine();
                int connection = network.findConnection(person1, person2);
                if (connection != -1) {
                    System.out.println("Connection between " + person1 + " and " + person2 + ": " + connection);
                } else {
                    System.out.println(person1 + " and " + person2 + " are not connected.");
                }
            }
        } while (choice != 5);

        scanner.close();
    }

}
