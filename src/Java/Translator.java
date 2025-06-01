package TranslationLibrary;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Translator {
    private static String pythonScriptPath = "src/Python/translator.py";
    private static String pythonInterpreter = "python";
    private static String prefferedLanguage = "English";

    // Getters and setters for above fields 

    public static String getPythonScriptPath() {
        return Translator.pythonScriptPath; 
    }

    public static String getPythonInterpreter() {
        return Translator.pythonInterpreter; 
    }

    public static String getPrefferedLanguage() {
        return Translator.prefferedLanguage; 
    }

    public static void setPythonScriptPath(String newPythonScriptPath) {
        if (isEmptyParameter(newPythonInterpreter, "setPythonScriptPath")) return; 
        Translator.pythonScriptPath = newPythonScriptPath;
    }

    public static void setPythonInterpreter(String newPythonInterpreter) {
        if (!containsIgnoreCase("python", newPythonInterpreter)) return;
        if (isEmptyParameter(newPythonInterpreter, "setPythonInterpreter")) return; 
        Translator.pythonInterpreter = newPythonInterpreter; 
    }

    public static void setPrefferedLanguage(String newPrefferedLanguage) {
        if (isEmptyParameter(newPythonInterpreter, "setPrefferedLanguage")) return; 
        if (!getAvailableLanguages().contains(newPrefferedLanguage) ||
            !getAnglicizedAvailableLanguagesList().contains(newPrefferedLanguage)
        ) {
            System.out.printf(
                "The given preffered language \"%s\" is not supported.\n", newPrefferedLanguage
            );
            System.out.println("Corroborate your spelling with the relevant TOML file/entry.");
            return;
        }
        Translator.prefferedLanguage = newPrefferedLanguage;
    }

    /**
     * Prints what is currently stored in each of the fields. 
     */
    public static void printCurrentFields() {
        
        System.out.printf(
            "Current Settings: pythonScriptPath=%s, pythonInterpreter=%s, prefferedLanguage=%s\n", 
            getPythonScriptPath(), getPythonInterpreter(), getPrefferedLanguage()
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
    private static boolean isEmptyParameter(String paramToCheck, String methodName) {
        
        if (paramToCheck.isEmpty()) {
            System.out.println("The parameter passed to " + methodName + " was empty.");
            printCurrentFields();
            return true;
        }
        return false;
    }

    /**
     * Returns the translated message based on the given parameters. 
     * 
     * @param messageToGet  The name of the variable in the language toml file that 
     *                      contains the message to be translated
     * @param sectionName   The name of the section that the message might be under
     * @param args          Optional objects of all placeholder args to be inserted 
     *                      into the message
     * @return              The translated message string
     */
    public static String getTranslation(
        String messageToGet, String sectionName, Object... args
    ) {
        
        // Build the command
        ArrayList<String> command = new ArrayList<>(Arrays.asList(
            pythonInterpreter, 
            pythonScriptPath, 
            "get-translation", 
            prefferedLanguage, 
            messageToGet,
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
     * Returns the translated message based on the given parameters. 
     * 
     * @param messageToGet  The name of the variable in the language toml file that 
     *                      contains the message to be translated
     * @param args          Optional objects of all placeholder args to be inserted
     *                      into the message
     * @return              The translated message string
     */
    public static String getTranslation(String messageToGet, Object... args) {
        
        // Build the command
        ArrayList<String> command = new ArrayList<>(Arrays.asList(
            pythonInterpreter, 
            pythonScriptPath, 
            "get-translation", 
            prefferedLanguage, 
            messageToGet,
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
        
        ArrayList<String> command = new ArrayList<>(Arrays.asList(
            pythonInterpreter, 
            pythonScriptPath, 
            "list"
        ));

        String[] languages = runCommand(command)
            .replace("[", "")
            .replace("]", "")
            .trim()
            .split(",");

        return Arrays.asList(languages);
    }

    /**
     * Returns a list of all supported languages. Each entry in the list is the language
     * spelled in its anglicized spelling. 
     * 
     * @return  A list of all of the supported languages with anglicized spelling  
     */
    public static List<String> getAnglicizedAvailableLanguagesList() {
        
        ArrayList<String> command = new ArrayList<>(Arrays.asList(
            pythonInterpreter, 
            pythonScriptPath, 
            "list-anglicized"
        ));

        String[] languages = runCommand(command)
            .replace("[", "")
            .replace("]", "")
            .trim()
            .split(",");

        return Arrays.asList(languages);
    }

    /**
     * Checks to see if a given language is supported. 
     * 
     * @return  True if the language is supported, false otherwise
     */
    public static boolean isSupportedLanguage(String language) {
        
        ArrayList<String> command = new ArrayList<>(Arrays.asList(
            pythonInterpreter, 
            pythonScriptPath, 
            "is-supported",
            language
        ));

        boolean result = false;
        if (runCommand(command).equalsIgnoreCase("true")) {
            result = true;
        }
        return result;
    }

    /**
     * Takens in a built command represented as a list of arguments. (Scary!)
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
                    outputLines = line.replace("TRANSLATION: ", "").replace("OUTPUT: ", "").trim();
                    break;
                }
                outputLines += (line + "\n");
            }

            // Wait for the process to exit
            int exitCode = process.waitFor();
            System.out.println("Exited with code: " + exitCode);

            // If there was an error, append error output to outputLines for it to get returned
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