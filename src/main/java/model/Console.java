package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Console {

    private String consoleText;

    public Console(){
//        consoleText = new ArrayList<>();
        consoleText = "Welcome to the Runway Re-declaration tool.";
    }

    public String formatMessage(String message){
        return ("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss")) + "] " + message);
    }

    public void addLog(String message){
        consoleText = formatMessage(message);
        update();
    }

    public void addLogWithoutTime(String message){
//        consoleText.add("                      " + message);
        consoleText = message;
        update();
    }

    public void update(){
        Model.centerScreenController.updateConsole(consoleText);

    }

//    public String getText(){
//        return String.join("\n",consoleText);
//    }
}
