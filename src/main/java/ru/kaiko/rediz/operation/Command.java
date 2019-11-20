package ru.kaiko.rediz.operation;

class Command {
    private final static String END = "\r\n";

    static String simpleCommand(String command, String arg1) {
        if (arg1 == null || arg1.isBlank())
            throw new IllegalArgumentException("bad arg1 argument: {" + arg1 + "},  must be not null and not blank");
        return String.join(" ", command, arg1 + END);
    }

    static String simpleCommand3(String command, String arg1, String arg2) {
        if (arg1 == null || arg1.isBlank())
            throw new IllegalArgumentException("bad arg1 argument: {" + arg1 + "}, must be not null and not blank");
        if (arg2 == null || arg2.isBlank())
            throw new IllegalArgumentException("bad arg2 argument: {" + arg2 + "}, must be not null and not blank");
        return String.join(" ", command, arg1, arg2 + END);
    }

    static String simpleCommand4(String command, String arg1, String arg2, String arg3) {
        if (arg1 == null || arg1.isBlank())
            throw new IllegalArgumentException("bad arg1 argument: {" + arg1 + "}, must be not null and not blank");
        if (arg2 == null || arg2.isBlank())
            throw new IllegalArgumentException("bad arg2 argument: {" + arg2 + "}, must be not null and not blank");
        if (arg3 == null || arg3.isBlank())
            throw new IllegalArgumentException("bad arg3 argument: {" + arg3 + "}, must be not null and not blank");
        return String.join(" ", command, arg1, arg2, arg3 + END);
    }
}
