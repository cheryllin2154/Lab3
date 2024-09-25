package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    // countries = {"afg", "alb"...} all alpha3 codes
    private List<String> countries = new ArrayList<>();
    // translations = [{"ar":"أفغانستان","bg":"Афганистан".....} {"ar":"ألبانيا","bg":"Албания"}
    // JSON objects that is f(language code) -> countryName, for each country
    private JSONArray translations = new JSONArray();

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);

            // this for loop makes the instance variables what's said in comments
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                final String country = jsonObject.getString("alpha3");
                jsonObject.remove("alpha3");
                jsonObject.remove("alpha2");
                jsonObject.remove("id");
                countries.add(country);
                translations.put(jsonObject);
            }

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {

        List<String> languages = new ArrayList<>();
        JSONObject temp;

        // Get the country in translations
        // temp = the country JSONObject
        int index = countries.indexOf(country);
        temp = translations.getJSONObject(index);

        // keys = language codes only, without actual translation
        Iterator<String> keys = temp.keys();

        // change type from iterator to list
        while (keys.hasNext()) {
            languages.add(keys.next());
        }
        return languages;
    }

    @Override
    public List<String> getCountries() {
        return countries;
    }

    @Override
    public String translate(String countryCode, String languageCode) {
        JSONObject temp;

        // Get the country in translations
        // temp = the country JSONObject
        int index = countries.indexOf(countryCode);
        temp = translations.getJSONObject(index);

        // temp can map languageCode to finalTranslation
        String finalTranslation = temp.getString(languageCode);
        return finalTranslation;
    }
}
