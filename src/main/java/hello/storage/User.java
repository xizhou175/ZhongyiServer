package hello.storage;

import org.springframework.stereotype.Component;

@Component
public class User {

    /**
     * Folder location for storing files
     */
    private String username = "";
    private String password = "";

    public String getUsername() {
        return username;
    }

    public void setUsrname(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString(){
    	String info = String.format("username = %s, password = %s", username, password);
	return info;
    }

}
