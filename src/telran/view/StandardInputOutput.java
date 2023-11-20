package telran.view;

import java.util.Scanner;

public class StandardInputOutput implements telran.view.InputOutput {
    private Scanner scanner = new Scanner(System.in);
    @Override
    public String readString(String prompt) {
        writeLine(prompt);
        return scanner.nextLine();
    }

    @Override
    public void write(String str) {
        System.out.print(str);

    }

}