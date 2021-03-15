package main.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Console {

    private ArrayList<String> consoleText;

    public Console(){
        consoleText = new ArrayList<>();
        consoleText.add(formatMessage("Welcome to the Runway Re-declaration tool."));
    }

    public String formatMessage(String message){
        String log = ("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "] " + message);
        return log;
    }

    public void addLog(String message){
        consoleText.add(formatMessage(message));
        update();
    }

    public void update(){
        Model.centerScreenController.updateConsole(this.getText());
    }

    public String getText(){
        return String.join("\n",consoleText);
    }
}
