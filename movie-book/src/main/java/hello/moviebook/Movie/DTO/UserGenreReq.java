package hello.moviebook.movie.dto;

import hello.moviebook.movie.domain.UserGenre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
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

    public static UserGenreReq preferGenreBuilder(UserGenre userGenre) {
        return UserGenreReq.builder()
                .action(userGenre.getAction() == 1L)
                .drama(userGenre.getDrama() == 1L)
                .comedy(userGenre.getComedy() == 1L)
                .romance(userGenre.getRomance() == 1L)
                .thriller(userGenre.getThriller() == 1L)
                .horror(userGenre.getHorror() == 1L)
                .sf(userGenre.getSf() == 1L)
                .fantasy(userGenre.getFantasy() == 1L)
                .animation(userGenre.getAnimation() == 1L)
                .documentary(userGenre.getDocumentary() == 1L)
                .crime(userGenre.getCrime() == 1L)
                .build();
    }

    public static UserGenreReq dislikeGenreBuilder(UserGenre userGenre) {
        return UserGenreReq.builder()
                .action(userGenre.getAction() == -1L)
                .drama(userGenre.getDrama() == -1L)
                .comedy(userGenre.getComedy() == -1L)
                .romance(userGenre.getRomance() == -1L)
                .thriller(userGenre.getThriller() == -1L)
                .horror(userGenre.getHorror() == -1L)
                .sf(userGenre.getSf() == -1L)
                .fantasy(userGenre.getFantasy() == -1L)
                .animation(userGenre.getAnimation() == -1L)
                .documentary(userGenre.getDocumentary() == -1L)
                .crime(userGenre.getCrime() == -1L)
                .build();
    }
}