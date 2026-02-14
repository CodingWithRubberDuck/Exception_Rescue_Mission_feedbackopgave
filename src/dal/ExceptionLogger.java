package dal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExceptionLogger {
    private String filePath = "";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    //File writer can't handle ":" in its path
    private final DateTimeFormatter safeForFileFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm:ss");

    public void setFilePath() {
        this.filePath = "src\\exceptionlogs\\" + returnFileSafeDate() + "-log.txt";
    }

    public void logException(Throwable e){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))){
            writer.write("\n" + returnDate() + " : " + e.getMessage());
        } catch (IOException ioe) {
            final String failSafePath = "exceptionLog.txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(failSafePath, true))){
                writer.write("\n" + returnDate() + " : " + e.getMessage());
            } catch (IOException ioe2){
                //Well if this fails the exceptions aren't getting logged
            }
        }
    }


    private String returnDate(){
        LocalDateTime dateObject = LocalDateTime.now();
        return dateObject.format(formatter);
    }

    private String returnFileSafeDate(){
        LocalDateTime dateObject = LocalDateTime.now();
        return dateObject.format(safeForFileFormatter);
    }


}
