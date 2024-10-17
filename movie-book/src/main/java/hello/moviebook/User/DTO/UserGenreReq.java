package hello.moviebook.User.DTO;

import lombok.Data;

@Data
public class UserGenreReq {
    private Boolean action;
    private Boolean drama;
    private Boolean comedy;
    private Boolean romance;
    private Boolean thriller;
    private Boolean horror;
    private Boolean sf;
    private Boolean fantasy;
    private Boolean animation;
    private Boolean documentary;
    private Boolean crime;
}
