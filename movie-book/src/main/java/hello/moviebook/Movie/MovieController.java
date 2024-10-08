package hello.moviebook.Movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/movie")
public class MovieController {
    @Autowired
    private MovieService movieService;

    // tmdb5000 엑셀 파일 -> 데이터베이스 저장
    @PostMapping("/tmdb")
    public String uploadMoviesFile(@RequestParam("file") MultipartFile file) {
        try {
            // 파일을 임시 디렉토리에 저장
            File tempFile = File.createTempFile("movies", ".xlsx");
            file.transferTo(tempFile);

            // 서비스 호출하여 엑셀 데이터를 DB에 저장
            movieService.saveMoviesFromExcel(tempFile.getAbsolutePath());

            return "Movies data has been uploaded and saved!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error occurred while uploading the file!";
        }
    }

//    @PostMapping("/kmdb")
//    public String setMovieData(@RequestParam("page") String page, @RequestParam("genre") String genre) {
//        movieService.insertMovieData(page, genre);
//    }

    // Movie DB 순회 -> 데이터 업데이트
    @PatchMapping("/tmdb-update")
    public void translateMovieData() {
        movieService.enToKoMovieData();
    }
}
