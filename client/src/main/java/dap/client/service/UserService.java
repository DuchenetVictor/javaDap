package dap.client.service;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

import dap.client.model.dto.UserResponse;

/**
 *
 * @author David_tepoche
 *
 */
public class UserService extends HttpClient {

    /**
     * create the user.
     *
     * @param userKey .
     * @return the user created
     * @throws IOException dunno.
     */
    public UserResponse addUser(final String userKey) throws IOException {
        InputStream inputStream = send("/add/" + userKey, "GET");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(inputStream, UserResponse.class);
    }

    @Override
    final String getControllerRootURL() {
        return "/user";
    }

}
