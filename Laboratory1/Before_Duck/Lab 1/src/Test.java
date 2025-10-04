import java.util.Random;

public class Test {

    String[] args;

    public Test(String[] args){
        this.args = args;
    }

    public static void testMethod0(){

        Message msj = new Message("Kai","Cole","Business stuff");
        msj.writeAMessage("Hi. Just a reminder that the mitting starts at 9 P.M.");
        MessageTask MT = new MessageTask("00-1", "Sender of messages", msj);
        MT.execute();

        int[] arr = {1, 2, 5, 3, 7, 9, 8, 4, 6};
        QuickSort quickSort = new QuickSort();
        SortingTask ST = new SortingTask("00-2", "Sorts an array of numbers", arr, quickSort);
        ST.execute();
        System.out.println("\n");
    }

    public static void testMethod1(){

        MessageTask[] MTS = new MessageTask[5];
        Message[] MS = new Message[5];
        String[] Students = {"Ana", "Nia", "Luca", "Loyd", "Banny"};
        Random rand = new Random();

        for(int i = 0; i < 5; i++){

            MS[i] = new Message("prof. Emanuel",Students[i],"Test Grade");
            MS[i].writeAMessage("You got : "+rand.nextInt(1,10+1));
            MTS[i] = new MessageTask(String.valueOf(i), "Announcements", MS[i]);
        }

        for(int i = 0; i < 5; i++){

            System.out.println(MTS[i]);
        }
    }

    public static void testMethod2() {

        System.out.println("\n");
        Message m1 = new Message("Ana", "Bogdan", "Salut");
        m1.writeAMessage("Ne vedem la 5.");
        Message m2 = new Message("Ion", "Maria", "Reminder");
        m2.writeAMessage("Ai întâlnirea la 10.");
        Message m3 = new Message("Elena", "Radu", "Info");
        m3.writeAMessage("Documentul a fost trimis.");

        Task t1 = new MessageTask("T1", "Mesaj 1", m1);
        Task t2 = new MessageTask("T2", "Mesaj 2", m2);
        Task t3 = new MessageTask("T3", "Mesaj 3", m3);

        AbstractSorter bubble = new BubbleSort();
        int[] nums = {5, 3, 8, 1};
        Task t4 = new SortingTask("T4", "Sortare bubble", nums, bubble);

        AbstractSorter quick = new QuickSort();
        int[] nums2 = {10, 2, 7, 4};
        Task t5 = new SortingTask("T5", "Sortare quick", nums2, quick);

        Container stack = new StackContainer();
        stack.add(t1);
        stack.add(t2);
        stack.add(t4);

        System.out.println("=== StackContainer ===");
        System.out.println("Size: " + stack.size());

        while (!stack.isEmpty()) {
            Task task = stack.remove();
            System.out.println("Removed: " + task);
        }

        Container queue = new QueueContainer();
        queue.add(t3);
        queue.add(t5);
        queue.add(t1);

        System.out.println();
        System.out.println("=== QueueContainer ===");

        System.out.println("Size: " + queue.size());
        while (!queue.isEmpty()) {
            Task task = queue.remove();
            System.out.println("Removed: " + task);
        }
    }

    public static void testMethod3() {

        System.out.println("\n");
        Factory factory = TaskContainerFactory.getInstance();

        Container c1 = factory.createContainer(Strategy.FIFO);
        Container c2 = factory.createContainer(Strategy.LIFO);

        System.out.println("FIFO container este de tip QueueContainer: " + (c1 instanceof QueueContainer));
        System.out.println("LIFO container este de tip StackContainer: " + (c2 instanceof StackContainer));
    }

    public static void testMethod4(String[] args) {

        Task t1 = new MessageTask("T1", "Primul task", new Message("A", "B", "Test"));
        Task t2 = new MessageTask("T2", "Al doilea task", new Message("A", "B", "Test"));
        Task t3 = new MessageTask("T3", "Al treilea task", new Message("A", "B", "Test"));

        if(args[0].equals("FIFO")){

            System.out.println("=== Test FIFO ===");
            StrategyTaskRunner fifoRunner = new StrategyTaskRunner(Strategy.FIFO);
            fifoRunner.addTask(t1);
            fifoRunner.addTask(t2);
            fifoRunner.addTask(t3);
            fifoRunner.executeOneTask();
            fifoRunner.executeAll();
        }

        if(args[0].equals("LIFO")){

            System.out.println("=== Test LIFO ===");
            StrategyTaskRunner lifoRunner = new StrategyTaskRunner(Strategy.LIFO);
            lifoRunner.addTask(t1);
            lifoRunner.addTask(t2);
            lifoRunner.addTask(t3);
            lifoRunner.executeOneTask();
            lifoRunner.executeAll();
        }

    }

    public static void testMethod5() {

        Message m = new Message("Alice", "Bob", "Test");
        m.writeAMessage("Mesaj de test");

        Task t1 = new MessageTask("T1", "Primul task", m);
        Task t2 = new MessageTask("T2", "Al doilea task", m);
        Task t3 = new MessageTask("T3", "Al treilea task", m);

        TaskRunner baseRunner = new StrategyTaskRunner(Strategy.FIFO);
        TaskRunner printerRunner = new PrinterTaskRunner(baseRunner);

        printerRunner.addTask(t1);
        printerRunner.addTask(t2);
        printerRunner.addTask(t3);

        System.out.println("=== Test PrinterTaskRunner ===");
        printerRunner.executeAll();
    }

    public static void testMethod6(String[] args) {

        if (args.length == 0) {
            System.out.println("Te rog să specifici o strategie: FIFO sau LIFO");
            return;
        }

        Strategy strategy;

        try {
            strategy = Strategy.valueOf(args[0].toUpperCase());

        } catch (IllegalArgumentException e) {

            System.out.println("Strategie invalidă. Folosește FIFO sau LIFO.");
            return;
        }

        Message m = new Message("Alice", "Bob", "Test");
        m.writeAMessage("Mesaj de test");

        Task t1 = new MessageTask("T1", "Primul task", m);
        Task t2 = new MessageTask("T2", "Al doilea task", m);
        Task t3 = new MessageTask("T3", "Al treilea task", m);

        TaskRunner runner = new DelayTaskRunner(new StrategyTaskRunner(strategy));

        runner.addTask(t1);
        runner.addTask(t2);
        runner.addTask(t3);

        System.out.println("=== Executăm cu strategia: " + strategy + " ===");
        runner.executeAll();
    }

    public static void testAllMethod(String[] args) {

        testMethod0();
        testMethod1();
        testMethod2();
        testMethod3();
        testMethod4(args);
        testMethod5();
        testMethod6(args);

    }

 }