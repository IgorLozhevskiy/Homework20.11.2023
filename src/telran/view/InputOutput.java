package telran.view;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public interface InputOutput {
    String readString(String prompt);
    void write(String str);
    default void writeLine(Object obj) {
        write(obj.toString() + "\n");
    }
    default <T> T readObject(String prompt, String errorPrompt,
                             Function<String, T> mapper) {
        boolean running = false;
        T res = null;
        do {
            running = false;
            try {
                String str = readString(prompt);
                res = mapper.apply(str);

            } catch (Exception e) {
                running = true;
                writeLine(errorPrompt + ": " + e.getMessage());
            }
        }while(running);
        return res;
    }
    default int readInt(String prompt, String errorPrompt) {
        return readObject(prompt, errorPrompt, Integer::parseInt);
    }
    default int readInt(String prompt, String errorPrompt, int min, int max) {
        return readObject(String.format("%s [%d-%d]" , prompt, min, max), errorPrompt, str -> {
            int num = Integer.parseInt(str);
            if (num < min || num > max) {
                throw new RuntimeException
                        (String.format("must be in the range [%d-%d]", min, max));
            }
            return num;
        });
    }
    default long readLong(String prompt, String errorPrompt) {
        return readObject(prompt, errorPrompt, Long::parseLong);
    }
    default long readLong(String prompt, String errorPrompt, long min, long max) {
        return readObject(String.format("%s [%d-%d]" , prompt, min, max), errorPrompt, str -> {
            long num = Long.parseLong(str);
            if (num < min || num > max) {
                throw new RuntimeException
                        (String.format("must be in the range [%d-%d]", min, max));
            }
            return num;
        });
    }
    default LocalDate readDate(String prompt, String errorPrompt) {
        //example: 2023-11-20
        return readObject(prompt, errorPrompt, LocalDate::parse);
    }
    default LocalDate readDate(String prompt, String errorPrompt,
                               LocalDate from, LocalDate to) {
        return readObject(prompt, errorPrompt , str -> {
            LocalDate date = LocalDate.parse(str);
            if (date.compareTo(from) < 0 || date.compareTo(to) > 0) {
                throw new RuntimeException(String.format("date must be in the range [%s - %s]",
                        from, to));
            }
            return date;
        });

    }
    default String readPredicate(String prompt, String errorPrompt, Predicate<String> predicate) {
        boolean running = false;
        String result = null;

        do {
            running = false;
            try {
                String input = readString(prompt);
                if (predicate.test(input)) {
                    result = input;
                } else {
                    throw new RuntimeException(errorPrompt);
                }
            } catch (Exception e) {
                running = true;
                writeLine(errorPrompt + ": " + e.getMessage());
            }
        } while (running);

        return result;
    }

    default String readOptions(String prompt, String errorPrompt, Set<String> options) {
        // TODO
        // Returns a string included in the given options

        return readPredicate(prompt, errorPrompt, options::contains);
    }

    default String readEmail(String prompt, String errorPrompt) {
        // TODO
        // Returns a string with an email address

        String emailPattern = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b";
        Predicate<String> emailPredicate = email -> email.matches(emailPattern);

        return readPredicate(prompt, errorPrompt, emailPredicate);
    }
    default double readDouble(String prompt, String errorPrompt) {
        return readObject(prompt, errorPrompt, Double::parseDouble);
    }
    default double readDouble(String prompt,
                              String errorPrompt, double min, double max) {
        return readObject(String.format("%s [%d-%d]" , prompt, min, max), errorPrompt, str -> {
            double num = Double.parseDouble(str);
            if (num < min || num > max) {
                throw new RuntimeException
                        (String.format("must be in the range [%d-%d]", min, max));
            }
            return num;
        });
    }
}