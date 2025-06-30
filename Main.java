package br.univille.log;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

enum Level {
    WARNING,
    DEBUG,
    ERROR
}
interface Logger {
    void log(Level level, String message);
}

class LoggerConsole implements Logger {
    @Override
    public void log(Level level, String message) {
        String formattedMessage = formatMessage(level, message);
        printToConsole(level, formattedMessage);
    }
    private String formatMessage(Level level, String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return String.format("[%s] %s: %s", timestamp, level, message);
    }
    private void printToConsole(Level level, String message) {
        String color;
        switch (level) {
            case DEBUG:
                color = "\u001B[32m"; 
                break;
            case WARNING:
                color = "\u001B[33m";
                break;
            case ERROR:
                color = "\u001B[31m"; 
                break;
            default:
                color = "\u001B[0m";
        }
        System.out.println(color + message + "\u001B[0m"); 
    }
}

class LoggerFile implements Logger {
    private String filePath;
    public LoggerFile(String filePath) {
        this.filePath = filePath;
    }
    @Override
    public void log(Level level, String message) {
        String formattedMessage = formatMessage(level, message);
        writeToFile(formattedMessage);
    }
    private String formatMessage(Level level, String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return String.format("[%s] %s: %s", timestamp, level, message);
    }
    private void writeToFile(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }
}

class LoggerFactory {
    static Logger onConsole() {
        return new LoggerConsole();
    }
    static Logger onFile(String filePath) {
        return new LoggerFile(filePath);
    }
}