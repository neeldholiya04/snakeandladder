package org.example;

import org.example.engine.GameEngine;

public class Main {
    public static void main(String[] args) {
        GameEngine engine = new GameEngine();
        engine.start();
        System.out.println("Thanks for playing Snake & Ladder");
    }
}