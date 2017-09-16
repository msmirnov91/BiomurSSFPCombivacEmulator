package ru.iimtech;

import java.util.Scanner;


public class App 
{
    public static void main( String[] args )
    {
        Scanner userInputReader = new Scanner(System.in);
        System.out.println("Enter port name");
        String comPortName = userInputReader.nextLine();
        System.out.println("Enter initial pressure value in mbar");
        double pressure = userInputReader.nextDouble();

        CombivacEmulator emulator = new CombivacEmulator(comPortName, pressure);

        try {
            emulator.emulate();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }


    }
}
