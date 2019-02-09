package fund_android.pos.inf.ufg.br.login;

public class User {
    private String name;
    private String token;
    private String photoUrl;

    public User(String name, String token, String photoUrl) {
        this.name = name;
        this.token = token;
        this.photoUrl = photoUrl;
    }

    public User() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
