package org.example;

import org.example.game.Simulation;

public class Main {

    public static int SCREEN_W = 1280;
    public static int SCREEN_H = 900;

    public static void main(String[] args){
        if(args.length == 3 && args[0].equals("Game"))
           gameMode(args);
        else if(args.length == 4 && args[0].equals("Simulation")) {
            simulationMode(args);
        }else
            help();
    }

    public static void gameMode(String[] args){
        int firstPlayerMode;
        int secondPlayerMode;
        try{
            firstPlayerMode = Integer.parseInt(args[1]);
            secondPlayerMode = Integer.parseInt(args[2]);
            if(firstPlayerMode < 0 || firstPlayerMode > 4)
                throw new Exception();
            if(secondPlayerMode < 0 || secondPlayerMode > 4)
                throw new Exception();
        }catch(Exception ex){
            help();
            return;
        }
        new Window(firstPlayerMode, secondPlayerMode);
    }

    public static void simulationMode(String[] args){
            int numberOfMatches;
            int firstBotMode;
            int secondBotMode;
            try{
                numberOfMatches = Integer.parseInt(args[1]);
                firstBotMode = Integer.parseInt(args[2]);
                secondBotMode = Integer.parseInt(args[3]);
            }catch(Exception ex){
                help();
                return;
            }
            Simulation simulation = new Simulation(numberOfMatches, firstBotMode, secondBotMode);
            simulation.simulate();
            simulation.printResults();
    }

    public static void help(){
        System.out.println("Wrong parameters.");
        System.out.println("First parameter should be 'Game' or 'Simulation'");
        System.out.println("If 'Game':");
        System.out.println("\tPattern:");
        System.out.println("\t\tGame firstPlayerMode secondPlayerMode");
        System.out.println("\tHuman Mode 0, Bot Modes 1-4");
        System.out.println("\tExample:");
        System.out.println("\t\tGame 1 0");
        System.out.println("If 'Simulation':");
        System.out.println("\tPattern:");
        System.out.println("\t\tSimulation numberOfMatches firstBotMode secondBotMode");
        System.out.println("\tBot Modes 1-4");
        System.out.println("\tExample:");
        System.out.println("\t\tSimulation 100 1 2");
    }
}
