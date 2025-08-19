package models;

public class PlayerDto {
    public int age;
    public String login;
    public String screenName;
    public String gender;
    public String role;
    public String password;

    public PlayerDto(int age, String login, String screenName, String gender, String role, String password) {
        this.age = age;
        this.login = login;
        this.screenName = screenName;
        this.gender = gender;
        this.role = role;
        this.password = password;
    }
}

