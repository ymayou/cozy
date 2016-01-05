package irc.cpe.cozy.Rest;

import java.io.IOException;

public class CozyClient {

    public String addDevice(String deviceId, String password) {
        String result = null;
        RestClient client = new RestClient();
        try {
            result = client.post("https://gustiaux.cozycloud.cc/device",
                    "{\"login\":\"" + deviceId + "\", \"permissions\": {\"File\": {\"description\": \"Synchronize files\"}} }",
                    "owner",
                    password);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
        // TODO : retourner password si réussite / gérer le cas si déjà enregistré / retourner null en cas d'erreur
    }

    public boolean addDocument() {
        return false;
    }

    public boolean deleteDocument() {
        return false;
    }

    public boolean updateDocument() {
        return false;
    }
}
