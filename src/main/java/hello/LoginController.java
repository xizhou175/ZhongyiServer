package hello;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.storage.BasicAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class LoginController {

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity authenticate(@RequestBody BasicAuth auth){
        String username = auth.getUsername();
        String password = auth.getPassword();

        // Query store for corresponding user and authenticate
        return new ResponseEntity(HttpStatus.OK);
    }
}
