package hello.moviebook.Movie;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    private final String tmdbURL = "https://api.themoviedb.org/3/movie/";
    private final String kmdbURL = "http://api.koreafilm.or.kr/openapi-data2/wisenut/search_api/search_json2.jsp?collection=kmdb_new2";
    private final String imageURL = "https://image.tmdb.org/t/p/w400";


    @Value("${tmdb.api-key}")
    private String tmdbKey;

    @Value("${kmdb.api-key}")
    private String kmdbKey;

    private final String tmdbOption = "?language=ko-kr";
    private final String kmdbOption = "?use=극장용&listCount=500&detail=N&nation=대한민국6&releaseDts=20000101";

//    public void insertMovieData(String page, String genre) {
//        StringBuffer result = new StringBuffer(); // 매번 새로 생성
//        try {
//            // 영화 상세 정보 조회 api
//            URL movieUrl = new URL(kmdbURL + kmdbOption + "&ServiceKey=" + kmdbKey + "&start_count=" + page + "&genre=" + genre);
//            HttpURLConnection movieUrlConnection = (HttpURLConnection) movieUrl.openConnection();
//            movieUrlConnection.setRequestMethod("GET");
//            movieUrlConnection.setRequestProperty("Content-type", "application/json");
//
//            // BufferedReader로 응답을 UTF-8로 읽어오기
//            BufferedReader bf = new BufferedReader(new InputStreamReader(movieUrlConnection.getInputStream(), StandardCharsets.UTF_8));
//
//            String line;
//            while ((line = bf.readLine()) != null) {
//                result.append(line);
//            }
//
//            // JSON 파싱
//            JSONObject jsonResponse = new JSONObject(result.toString());
//            JSONArray movieArray = jsonResponse.getJSONArray("Result");
//            // MovieId, movie_name, description, genre, keyword, poster
//            // 이미지 경로
//            String moviePath = jsonResponse.getString("poster_path");
//            movie.setPoster(imageURL + moviePath);
//
//            // 영화 제목
//            String movieName = jsonResponse.getString("title");
//            movie.setMovieName(movieName);
//
//            JSONArray genresArray = jsonResponse.getJSONArray("genres");
//
//            // 영화 설명
//            String description = jsonResponse.getJSONObject("overview").toString();
//            if (description.length() > 255) {
//                description = description.substring(0, 255);
//            }
//            movie.setDescription(description);
//
//            // 영화 장르
//            List<String> genres = new ArrayList<>();
//            for (int i = 0; i < genresArray.length(); i++) {
//                JSONObject genreObject = genresArray.getJSONObject(i);
//                genres.add(genreObject.getString("name"));
//            }
//            String genresString = String.join(", ", genres);
//            movie.setGenre(genresString);
//
//            log.info("Movies revised successfully into the database.");
//        } catch (IOException e) {
//            log.error("An error occurred while parsing and saving movie data: ", e);
//        }
//        // 장소 리스트 DB에 저장
//        movieRepository.saveAll(movieList);
//    }

    // Convert List<String> to a single comma-separated string and truncate to 255 characters if necessary
    public String convertListToString(List<String> list) {
        String result = String.join(", ", list);
        // Truncate to 255 characters
        return result.length() > 255 ? result.substring(0, 255) : result;
    }

    // Truncate any string to 255 characters
    public String truncateString(String data) {
        return data.length() > 255 ? data.substring(0, 255) : data;
    }

    public void saveMoviesFromExcel(String filePath) {
        try (FileInputStream fis = new FileInputStream(new File(filePath))) {
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Skip the header row
                }

                Movie movie = new Movie();
                movie.setMovieId((long) row.getCell(2).getNumericCellValue());
                movie.setMovieName(row.getCell(7).getStringCellValue());

                // Truncate description to 255 characters
                movie.setDescription(truncateString(row.getCell(5).getStringCellValue()));

                // Parse genres and keywords, then truncate to 255 characters
                String genresString = row.getCell(0).getStringCellValue();
                List<String> genresList = parseJsonToStringList(genresString);
                movie.setGenre(convertListToString(genresList)); // Truncate to 255 characters

                String keywordsString = row.getCell(3).getStringCellValue();
                List<String> keywordsList = parseJsonToStringList(keywordsString);
                movie.setKeyword(convertListToString(keywordsList)); // Truncate to 255 characters

                movieRepository.save(movie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> parseJsonToStringList(String json) {
        // Replace curly braces and quotes, split by commas
        json = json.replace("[", "").replace("]", "").replace("\"", "");
        String[] items = json.split(",");
        List<String> list = new ArrayList<>();
        for (String item : items) {
            if (item.contains("name")) {
                String[] parts = item.split(":");
                if (parts.length > 1) {
                    list.add(parts[1].trim());
                }
            }
        }
        return list;
    }

    // tmdb 영화 id -> 이미지 경로 데이터 추가 + 영화명, 줄거리, 장르 한국어로 수정
    public void enToKoMovieData() {
        List<Movie> movieList = movieRepository.findAll();

        for (Movie movie : movieList) {
            StringBuffer result = new StringBuffer(); // 매번 새로 생성
            try {
                // 영화 상세 정보 조회 api
                URL movieUrl = new URL(tmdbURL + movie.getMovieId().toString() + tmdbOption + "&api_key=" + tmdbKey);
                HttpURLConnection movieUrlConnection = (HttpURLConnection) movieUrl.openConnection();
                movieUrlConnection.setRequestMethod("GET");
                movieUrlConnection.setRequestProperty("Content-type", "application/json");

                // BufferedReader로 응답을 UTF-8로 읽어오기
                BufferedReader bf = new BufferedReader(new InputStreamReader(movieUrlConnection.getInputStream(), StandardCharsets.UTF_8));

                String line;
                while ((line = bf.readLine()) != null) {
                    result.append(line);
                }

                // JSON 파싱
                JSONObject jsonResponse = new JSONObject(result.toString());

                // 이미지 경로
                String moviePath = jsonResponse.getString("poster_path");
                movie.setPoster(imageURL + moviePath);

                // 영화 제목
                String movieName = jsonResponse.getString("title");
                movie.setMovieName(movieName);

                JSONArray genresArray = jsonResponse.getJSONArray("genres");

                // 영화 설명
                String description = jsonResponse.getJSONObject("overview").toString();
                if (description.length() > 255) {
                    description = description.substring(0, 255);
                }
                movie.setDescription(description);

                // 영화 장르
                List<String> genres = new ArrayList<>();
                for (int i = 0; i < genresArray.length(); i++) {
                    JSONObject genreObject = genresArray.getJSONObject(i);
                    genres.add(genreObject.getString("name"));
                }
                String genresString = String.join(", ", genres);
                movie.setGenre(genresString);

                log.info("Movies revised successfully into the database.");
            } catch (IOException e) {
                log.error("An error occurred while parsing and saving movie data: ", e);
            }
        }
        // 장소 리스트 DB에 저장
        movieRepository.saveAll(movieList);
    }
}
