package TranslationLibrary;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println(Translator.getTranslation(
            "setting", 
            ""
        ));

        System.out.println(Translator.getTranslation(
            "hello", 
            "",
            "name=Blake"
        ));
        
        System.out.println(Translator.getTranslation(
            "section_name", 
            "start"
        ));

        System.out.println(Translator.getTranslation(
            "welcome", 
            "start",
            "name=Blake"
        ));

        for (String lang : Translator.getAvailableLanguagesList()) {
            System.out.println(lang);
        }

        for (String lang : Translator.getAnglicizedAvailableLanguagesList()) {
            System.out.println(lang);
        }

        System.out.println(Translator.isSupportedLanguage("English"));
    }
}