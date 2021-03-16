package helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.credentials;

import java.io.File;
import java.io.IOException;

public class basicAuth {
    public String getuser() throws IOException {
        ObjectMapper mapeador = new ObjectMapper();
        credentials creds = mapeador.readValue(new File("src/main/resources/credentials.json"), credentials.class);
        String username = creds.getUser();
        return username;
    }

    public String getpass() throws IOException {
        ObjectMapper mapeador = new ObjectMapper();
        credentials creds = mapeador.readValue(new File("src/main/resources/credentials.json"), credentials.class);
        String password = creds.getPassword();
        return password;
    }
}
