package me.vaysman.test;

public class App {

    public static void main(String[] args) {
        if (args.length == 0) throw new IllegalArgumentException();
        String arg = args[0];
        arg = arg.toLowerCase();
        if (arg.equals("queue")) QueueTest.main(args);
        else if (arg.equals("topic")) TopicTest.main(args);
        else throw new IllegalArgumentException();
    }

}
