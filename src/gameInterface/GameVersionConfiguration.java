package gameInterface;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class GameVersionConfiguration {

    private Map<String, String> streetNames;
    private static GameVersionConfiguration instance;

    public static GameVersionConfiguration getInstance() {
        if (GameVersionConfiguration.instance == null) {
            instance = new GameVersionConfiguration();
            instance.readConfigurationFile("src/CanadianMonopoly.json");
        }
        return instance;
    }

    public Map<String, String> getStreetNames() {
        return this.streetNames;
    }

    public static String readFileAsString(String file)
    {
        try {
            return new String(Files.readAllBytes(Paths.get(file)));
        }
        catch(Exception e){
            return "";
        }
    }

    public void readConfigurationFile(String file) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInput = readFileAsString(file);
        TypeReference<HashMap<String, String>> typeRef
                = new TypeReference<HashMap<String, String>>() {};
        try {
            this.streetNames = mapper.readValue(jsonInput, typeRef);
        } catch (Exception e) {

        }
        System.out.println(this.streetNames);
    }
}