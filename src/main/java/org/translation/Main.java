package org.translation;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Main class for this program.
 * Complete the code according to the "to do" notes.<br/>
 * The system will:<br/>
 * - prompt the user to pick a country name from a list<br/>
 * - prompt the user to pick the language they want it translated to from a list<br/>
 * - output the translation<br/>
 * - at any time, the user can type quit to quit the program<br/>
 */
public class Main {

    /**
     * This is the main entry point of our Translation System!<br/>
     * A class implementing the Translator interface is created and passed into a call to runProgram.
     * @param args not used by the program
     */
    public static void main(String[] args) {

        Translator translator = new JSONTranslator("sample.json");

        runProgram(translator);
    }

    /**
     * This is the method which we will use to test your overall program, since
     * it allows us to pass in whatever translator object that we want!
     * See the class Javadoc for a summary of what the program will do.
     * @param translator the Translator implementation to use in the program
     */
    public static void runProgram(Translator translator) {
        while (true) {
            // make instance of converters
            CountryCodeConverter countryCodeConverter = new CountryCodeConverter("country-codes.txt");
            LanguageCodeConverter languageConverter = new LanguageCodeConverter("language-codes.txt");

            // quit command
            String quit = "quit";

            // start, call promptForCountry, get the countryName user inputs.
            String countryName = promptForCountry(translator);
            if (quit.equals(countryName)) {
                break;
            }
            // converts countryName into alpha3 countryCode
            String countryCode = countryCodeConverter.fromCountry(countryName);

            // Same for promptForLanguage
            String languageName = promptForLanguage(translator, countryCode);
            if (languageName.equals(quit)) {
                break;
            }
            String languageCode = languageConverter.fromLanguage(languageName);

            // Final output
            System.out.println(countryName + " in " + languageName + " is "
                    + translator.translate(countryCode, languageCode));

            // Start to while loop
            System.out.println("Press enter to continue or quit to exit.");
            Scanner s = new Scanner(System.in);
            String textTyped = s.nextLine();

            if ("quit".equals(textTyped)) {
                break;
            }
        }
    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods
    private static String promptForCountry(Translator translator) {
        List<String> countries = translator.getCountries();
        CountryCodeConverter countryCodeConverter = new CountryCodeConverter("country-codes.txt");

        // Changing the countryCode to countryName
        for (int i = 0; i < countries.size(); i++) {
            String countryCode = countries.get(i);
            String countryName = countryCodeConverter.fromCountryCode(countryCode);
            countries.set(i, countryName);
        }

        // Sort the countries
        Collections.sort(countries);

        // print line by line
        for (String country : countries) {
            System.out.println(country);
        }
        System.out.println("select a country from above:");

        Scanner s = new Scanner(System.in);
        return s.nextLine();

    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods

    private static String promptForLanguage(Translator translator, String country) {

        List<String> languages = translator.getCountryLanguages(country);
        LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter("language-codes.txt");

        // Changing the languagesCode to languagesName
        for (int i = 0; i < languages.size(); i++) {
            String languagesCode = languages.get(i);
            // here assumed String country is the country code, not the country name, may need to change
            String languagesName = languageCodeConverter.fromLanguageCode(languagesCode);
            languages.set(i, languagesName);
        }

        // Sort the languages
        Collections.sort(languages);

        // print line by line
        for (String language : languages) {
            System.out.println(language);
        }
        System.out.println("select a language from above:");

        Scanner s = new Scanner(System.in);
        return s.nextLine();
    }
}
