package hello.moviebook.Data;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
public class DataController {
    private final DataService dataService;

    // tmdb5000 엑셀 파일 -> 데이터베이스 저장
    // MultipartFile을 사용하는 게 일반적, 추후 사용할 일 있다면 변경
    @PostMapping("/excel")
    public String uploadMoviesFile(@RequestParam("file") MultipartFile file) {
        try {
            // 파일을 임시 디렉토리에 저장
            File tempFile = File.createTempFile("movies", ".xlsx");
            file.transferTo(tempFile);

            // 서비스 호출하여 엑셀 데이터를 DB에 저장
            dataService.saveMoviesFromExcel(tempFile.getAbsolutePath());

            return "Movies data has been uploaded and saved!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error occurred while uploading the file!";
        }
    }

    //Movie DB 순회 -> 이미지 경로 데이터 추가 + 영화명, 줄거리, 장르 한국어로 수정
    @PatchMapping("/update/kor")
    public void getKoMovieData() {
        dataService.insertKorData();
    }

    // Movie DB 순회 -> 영어 제목, 줄거리, 장르 업데이트
    @PatchMapping("/update/en")
    public void getEnMovieData() {
        dataService.insertEnData();
    }

    // TMDB - 한국 영화 데이터 추가
    @PostMapping("/tmdb-kor/{page}")
    public void insertKoMovie(@PathVariable("page") Long page) { dataService.getKoreaMovie(page);}

    // Movie DB 순회 -> 키워드 업데이트
    @PostMapping("/set-keyword/en")
    public void setEnKeywords() { dataService.insertEnKeywords();}
}
