import java.util.*;

class task1 {
    static class Task {
        String name;
        int duration;
        List<Task> dependencies;
        int earliestStart;
        int earliestFinish;
        int latestStart;
        int latestFinish;

        public Task(String name, int duration) {
            this.name = name;
            this.duration = duration;
            this.dependencies = new ArrayList<>();
            this.earliestStart = 0;
            this.earliestFinish = 0;
            this.latestStart = Integer.MAX_VALUE;
            this.latestFinish = Integer.MAX_VALUE;
        }

    }

    Map<String, Task> tasks = new HashMap<>();
    List<Task> taskList = new ArrayList<>();

    void addTask(String name, int duration) { // O(V+E)
        Task task = new Task(name, duration);
        tasks.put(name, task);
        taskList.add(task);
    }

    void addDependency(String taskName, String dependencyName) {// O(V+E)
        Task task = tasks.get(taskName);
        Task dependency = tasks.get(dependencyName);
        task.dependencies.add(dependency);
        // T1-> (T2,T3)
        // T2-> (T4)
        // T3->(T4)
        // T4->(T5)
    }

    void calculateTimes() {
        calculateEarliestTimes();
        int maxTime = 0;
        for (Task task : taskList) {
            if (task.earliestFinish > maxTime) {
                maxTime = task.earliestFinish;
            }
        }
        calculateLatestTimes(maxTime);
    }

    void calculateEarliestTimes() { // O(V+E)
        Queue<Task> queue = new LinkedList<>();
        Map<Task, Integer> indegree = new HashMap<>();

        for (Task task : taskList) {
            indegree.put(task, 0);
        }
        for (Task task : taskList) {
            for (Task dependency : task.dependencies) {
                indegree.put(dependency, indegree.get(dependency) + 1);
            }
        }
        for (Task task : taskList) {
            if (indegree.get(task) == 0) {
                queue.add(task);
                task.earliestFinish = task.duration;
            }
        }

        while (!queue.isEmpty()) { // O(V+E)
            Task current = queue.poll();
            int currentFinish = current.earliestFinish;

            for (Task dependency : current.dependencies) {
                dependency.earliestStart = Math.max(dependency.earliestStart, currentFinish);
                dependency.earliestFinish = dependency.earliestStart + dependency.duration;
                indegree.put(dependency, indegree.get(dependency) - 1);
                if (indegree.get(dependency) == 0) {
                    queue.add(dependency);
                }
            }
        }
    }

    void calculateLatestTimes(int maxTime) {// O(V+E)
        for (Task task : taskList) {
            task.latestFinish = maxTime;
            task.latestStart = task.latestFinish - task.duration;
        }

        for (int i = taskList.size() - 1; i >= 0; i--) { // O(V)
            Task task = taskList.get(i);
            for (Task dependency : task.dependencies) {// O(E)
                task.latestFinish = Math.min(task.latestFinish, dependency.latestStart);
                task.latestStart = task.latestFinish - task.duration;
            }
        }
    }

    void printCompletionTimes() {
        int earliestCompletion = 0;
        int latestCompletion = 0;

        for (Task task : taskList) {
            if (task.earliestFinish > earliestCompletion) {
                earliestCompletion = task.earliestFinish;
            }
            if (task.latestFinish > latestCompletion) {
                latestCompletion = task.latestFinish;
            }
        }

        System.out.println("Earliest time all tasks will be completed: " + earliestCompletion);
        System.out.println("Latest time all tasks will be completed: " + latestCompletion);
    }

    void printTaskTimes() {
        for (Task task : taskList) {
            System.out.println("Task: " + task.name);
            System.out.println("  Earliest Start: " + task.earliestStart);
            System.out.println("  Earliest Finish: " + task.earliestFinish);
            System.out.println("  Latest Start: " + task.latestStart);
            System.out.println("  Latest Finish: " + task.latestFinish);
        }
    }

    public static void main(String[] args) {
        task1 scheduler = new task1();

        scheduler.addTask("T1", 5);
        scheduler.addTask("T2", 3);
        scheduler.addTask("T3", 2);
        scheduler.addTask("T4", 4);
        scheduler.addTask("T5", 6);

        scheduler.addDependency("T1", "T2");
        scheduler.addDependency("T1", "T3");
        scheduler.addDependency("T2", "T4");
        scheduler.addDependency("T3", "T4");
        scheduler.addDependency("T4", "T5");

        scheduler.calculateTimes();
        scheduler.printCompletionTimes();
        scheduler.printTaskTimes();
    }
}
