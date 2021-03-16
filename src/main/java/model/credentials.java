package model;

public class credentials {
    private String user;
    private String password;

    public credentials(){
        super();
    }

    public credentials(String user, String password){
        this.user = user;
        this.password = password;
    }
    public String getUser(){return user;}
    public void setUser(){this.user = user;}
    public String getPassword(){return password;}
    public void setPassword(){this.password = password;}
}
