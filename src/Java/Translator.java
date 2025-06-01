package TranslationLibrary;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Translator {
    private static String pythonScriptPath = "src/Python/translator.py";
    private static String pythonInterpreter = "python";
    private static String preferredLanguage = "English";

    // Getters and setters for above fields 

    public static String getPythonScriptPath() {
        return Translator.pythonScriptPath; 
    }

    public static String getPythonInterpreter() {
        return Translator.pythonInterpreter; 
    }

    public static String getPreferredLanguage() {
        return Translator.preferredLanguage; 
    }

    public static void setPythonScriptPath(String newPythonScriptPath) {
        if (isEmptyParam(newPythonInterpreter, "setPythonScriptPath")) return; 
        Translator.pythonScriptPath = newPythonScriptPath;
    }

    public static void setPythonInterpreter(String newPythonInterpreter) {
        if (!containsIgnoreCase("python", newPythonInterpreter)) return;
        if (isEmptyParam(newPythonInterpreter, "setPythonInterpreter")) return; 
        Translator.pythonInterpreter = newPythonInterpreter; 
    }

    public static void setPreferredLanguage(String newPreferredLanguage) {
        if (isEmptyParam(newPythonInterpreter, "setPreferredLanguage")) return; 
        if (!getAvailableLanguages().contains(newPreferredLanguage) ||
            !getAnglicizedAvailableLanguagesList().contains(newPreferredLanguage)
        ) {
            System.out.printf(
                "The given preferred language \"%s\" is not supported.\n", 
                newPreferredLanguage
            );
            System.out.println(
                "Corroborate your spelling with the relevant TOML file/entry."
            );
            return;
        }
        Translator.preferredLanguage = newPreferredLanguage;
    }

    /**
     * Prints what is currently stored in each of the fields. 
     */
    public static void printCurrentFields() {
        
        System.out.printf(
            "Current: pythonScriptPath=%s, pythonInterpreter=%s, preferredLanguage=%s\n", 
            getPythonScriptPath(), getPythonInterpreter(), getPreferredLanguage()
        );
    }

    /**
     * Checks to see if a case-insensitive version of the target string is contained
     * within a case-insensitive version of the source string.
     * 
     * @param source    The string to check whether it contains the target string 
     * @param target    The string to check whether it is contained in the source string
     * @return          True if the source contains target, false otherwise
     */
    public static boolean containsIgnoreCase(String source, String target) {
        
        return source != null && target != null &&
           source.toLowerCase().contains(target.toLowerCase());
    }

    /**
     * Checks to see whether a parameter passed into a given function is empty. 
     * 
     * @param paramToCheck  The parameter string to check 
     * @param methodName    The name of the method that the parameter was passed to 
     * @return              True if the parameter is empty, false otherwise
     */
    private static boolean isEmptyParam(String paramToCheck, String methodName) {
        
        if (paramToCheck.isEmpty()) {
            System.out.println("The parameter passed to " + methodName + " was empty.");
            printCurrentFields();
            return true;
        }
        return false;
    }

    /**
     * Returns the translated message string based on the given parameters. 
     * 
     * @param variableName  The name of the variable in the language toml file that 
     *                      contains the message to be translated
     * @param sectionName   The name of the section that the variable might be under
     * @param args          Optional objects of all placeholder args to be inserted 
     *                      into the message string 
     * @return              The translated message string
     */
    public static String getTranslation(
        String variableName, String sectionName, Object... args
    ) {
        
        // Checks to see if the user was wanting to call the other getTranslation() method
        if(sectionName.contains("=")) {
            
            // If this condition is true, the other getTranslation() method is called 
            Object[] newArgs = new Object[args.length + 1];
            newArgs[0] = sectionName;
            if(args.length >= 1) {
                for (int i = 1; i < args.length + 1; i++) {
                    newArgs[i] = args[i - 1];
                }
            }
            return getTranslation(variableName, newArgs);
        }

        // Build the "get-translation" command
        ArrayList<String> command = new ArrayList<>(Arrays.asList(
            pythonInterpreter, 
            pythonScriptPath, 
            "get-translation", 
            preferredLanguage, 
            variableName,
            sectionName
        ));

        // Append optional args to the command
        for (Object arg : args) {
            command.add(arg.toString());
        }

        // Run the command and return its output 
        return runCommand(command); 
    }

    /**
     * Returns the translated message string based on the given parameters. 
     * 
     * @param variableName  The name of the variable in the language toml file that 
     *                      contains the message to be translated
     * @param args          Optional objects of all placeholder args to be inserted
     *                      into the message string
     * @return              The translated message string
     */
    public static String getTranslation(String variableName, Object... args) {
        
        // Build the "get-translation" command with no "section_name" argument 
        ArrayList<String> command = new ArrayList<>(Arrays.asList(
            pythonInterpreter, 
            pythonScriptPath, 
            "get-translation", 
            preferredLanguage, 
            variableName,
            ""
        ));

        // Append optional args to the command
        for (Object arg : args) {
            command.add(arg.toString());
        }

        // Run the command and return its output 
        return runCommand(command); 
    }

    /**
     * Returns a list of all supported languages. Each entry in the list is the language 
     * spelled in its local spelling. 
     * 
     * @return A list of all of the supported languages with local spelling 
     */
    public static List<String> getAvailableLanguagesList() {
        
        // Build the "list" command
        ArrayList<String> command = new ArrayList<>(Arrays.asList(
            pythonInterpreter, 
            pythonScriptPath, 
            "list"
        ));

        // Convert the string version of Python's languages list into a Java string array 
        String[] languages = runCommand(command)
            .replace("[", "")
            .replace("]", "")
            .trim()
            .split(",");
        
        // Return the languages string array as a list 
        return Arrays.asList(languages);
    }

    /**
     * Returns a list of all supported languages. Each entry in the list is the language
     * spelled in its anglicized spelling. 
     * 
     * @return  A list of all of the supported languages with anglicized spelling  
     */
    public static List<String> getAnglicizedAvailableLanguagesList() {
        
        // Build the "list-anglicized" command
        ArrayList<String> command = new ArrayList<>(Arrays.asList(
            pythonInterpreter, 
            pythonScriptPath, 
            "list-anglicized"
        ));

        // Convert the string version of Python's languages list into a Java string array 
        String[] languages = runCommand(command)
            .replace("[", "")
            .replace("]", "")
            .trim()
            .split(",");

        // Return the languages string array as a list 
        return Arrays.asList(languages);
    }

    /**
     * Checks to see if a given language is supported. 
     * 
     * @return  True if the language is supported, false otherwise
     */
    public static boolean isSupportedLanguage(String language) {
        
        // Build the "is-supported" command
        ArrayList<String> command = new ArrayList<>(Arrays.asList(
            pythonInterpreter, 
            pythonScriptPath, 
            "is-supported",
            language
        ));

        // Initialize a return boolean to false. Change it if the Python output was true 
        boolean result = false;
        if (runCommand(command).equalsIgnoreCase("true")) {
            result = true;
        }
        return result;
    }

    /**
     * Takes in a built command represented as a list of arguments. (Scary!)
     * 
     * @param command   The built Python command to be run 
     * @return          The output of said Python command 
     */
    private static String runCommand(ArrayList<String> command) {
        
        String outputLines = "", errorLines = "", line = "";
        try {
            // Build and start the process to run the translator Python script
            Process process = new ProcessBuilder(command).start();

            // Read output from the Python script
            BufferedReader inputReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            // Store output from line variable into outputLines variable
            // Will break loop if the string containing "TRANSLATION" or "OUTPUT" is found 
            while ((line = inputReader.readLine()) != null) {
                if (line.contains("TRANSLATION") || line.contains("OUTPUT")){
                    outputLines = line
                        .replace("TRANSLATION: ", "")
                        .replace("OUTPUT: ", "")
                        .trim();
                    break;
                }
                outputLines += (line + "\n");
            }

            // Wait for the process to exit
            int exitCode = process.waitFor();
            // System.out.println("Exited with code: " + exitCode); // Good for debugging

            // If there was an error, append error to outputLines so it can get returned
            if (exitCode != 0){
                BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()));

                while ((errorLines = errorReader.readLine()) != null) {
                    outputLines += (errorLines + '\n');
                }
                errorReader.close();
                outputLines += (
                    "An error occurred when interacting with " + pythonScriptPath + ". " +
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