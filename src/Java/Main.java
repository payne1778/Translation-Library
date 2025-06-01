package TranslationLibrary;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println(Translator.getTranslation(
            "welcome", 
            "start"
        ));

        List<String> list = Translator.getAvailableLanguagesList();
        // System.out.println(list.get(0));
        // System.out.println(list.get(1));
    }
}