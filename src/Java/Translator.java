package TranslationLibrary;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Translator {
    private static String pythonScriptPath = "src/translator.py";
    private static String pythonInterpreter = "python";
    private static String prefferedLanguage = "english"; 

    public Translator(String pythonScriptPath, String pythonInterpreter, String prefferedLanguage) {
        
        if (pythonScriptPath.isEmpty() || pythonInterpreter.isEmpty() || prefferedLanguage.isEmpty()) {
            System.out.println("One of the constructor arguments was empty. Please recheck your inputs");
            System.out.printf(
                "pythonScriptPath=%s, pythonInterpreter=%s, prefferedLanguage=%s\n", 
                pythonScriptPath, pythonInterpreter, prefferedLanguage
            );
        }

        Translator.pythonScriptPath = pythonScriptPath;
        Translator.pythonInterpreter = pythonScriptPath;
        Translator.prefferedLanguage = prefferedLanguage;
    }

    public static String getTranslation(String messageToGet, String sectionName, Object... args) {
        
        // Build the command. Example: "python translator.py translation english welcome sectionname arg1 arg2 ..."
        ArrayList<String> command = new ArrayList<>();

        command.add(pythonInterpreter);
        command.add(pythonScriptPath);
        command.add("translation");
        command.add(prefferedLanguage);
        command.add(messageToGet);
        command.add(sectionName);

        for (Object arg : args) {
            command.add(arg.toString());
        }

        // System.out.println(command.toString());

        return runCommand(command); 
    }

    public static List<String> getAvailableLanguages() {
        ArrayList<String> command = new ArrayList<>();
        command.add(pythonInterpreter);
        command.add(pythonScriptPath);
        command.add("list");

        String[] languages = runCommand(command).replace("[", "").replace("]", "").trim().split(",");
        return Arrays.asList(languages);
    }

    private static String runCommand(ArrayList<String> command) {
        String outputLines = "", errorLines = "", line = "";
        try {

            // Build and start the process to run the translator Python script
            Process process = new ProcessBuilder(command).start();

            // Read output from the Python script
            BufferedReader inputReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            // Store translated output string into line variable
            while ((line = inputReader.readLine()) != null) {
                if (line.contains("TRANSLATION") || line.contains("OUTPUT")){
                    outputLines = line.replace("TRANSLATION: ", "").replace("OUTPUT: ", "").trim();
                    break;
                }
                outputLines += (line + "\n");
            }

            // Wait for the process to exit
            int exitCode = process.waitFor();
            System.out.println("Exited with code: " + exitCode);

            // If there was an error, get and print the error output  
            if (exitCode != 0){
                BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()));

                while ((errorLines = errorReader.readLine()) != null) {
                    outputLines += (errorLines + '\n');
                }
                errorReader.close();
                outputLines += (
                    "An error occured when interating with " + pythonScriptPath + ". " +
                    "Please check the terminal for more information."
                ); 
            }

            // Destroy the created process and close buffers 
            inputReader.close();
            process.destroy();

        } 
        catch (Exception e) {
            e.printStackTrace();
        }

        // Return python script output 
        return outputLines;
    }
}