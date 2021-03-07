package main;

import main.controllers.CenterScreenController;

/**
 * Model class used as the main class that holds all data from classes that represent Airport, Runway, Obstacles, etc.
 * Used for communicating between Controller and Model classes
 */
public class Model {

    // console that holds all the log texts to display
    public static Console console = new Console();
    // Center of the UI, main visualisation window and console textArea
    public static CenterScreenController centerScreenController;
}
