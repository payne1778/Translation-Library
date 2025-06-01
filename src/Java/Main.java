package TranslationLibrary;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println(Translator.getTranslation(
            "welcome", 
            "start",
            "name=Blake"
        ));

        for (String lang : Translator.getAvailableLanguagesList()) {
            System.out.println(lang);
        }
    }
}