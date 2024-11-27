package hello.moviebook.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.moviebook.book.domain.Book;
import hello.moviebook.book.repository.BookRepository;
import hello.moviebook.movie.domain.Movie;
import hello.moviebook.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataService {

    private final MovieRepository movieRepository;
    private final BookRepository bookRepository;
    private final String tmdbURL = "https://api.themoviedb.org/3/movie/";
    private final String imageURL = "https://image.tmdb.org/t/p/w400";
    @Value("${tmdb.api-key}")
    private String tmdbKey;
    private final String tmdbOption = "?language=ko-kr";
    private final String defaultURL = "https://openapi.naver.com/v1/search/book.json?";
    private final String option = "display=100&sort=sim&";
    private final String clientId = "luVmm20hRvg7dhaITaOy"; // 클라이언트 아이디
    private final String clientSecret = "jjIk8pafab"; // 클라이언트 시크릿


    // books_data.json 파일을 읽고 데이터베이스에 저장하는 메서드
    public void saveBooksFromJson(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // JSON 파일에서 Book 리스트를 읽어들임
            List<Book> books = objectMapper.readValue(new File(filePath), new TypeReference<List<Book>>() {});
            Long cnt = 0L;

            for (Book book : books) {
                log.info("Description length: {} characters", book.getDescription().length());
                if (isValidBook(book)) {
                    bookRepository.save(book);
                    log.info("Book : {}", book);
                    cnt++;
                }
                log.info("Book : {}", book.getBookName());
            }
            log.info("Books inserted successfully into the database. count : {}", cnt);
        } catch (IOException e) {
            log.error("An error occurred while parsing and saving book data: ", e);
        }
    }

    // Book_Keywords.xlsx 파일을 읽고 DB에 저장하는 메서드
    public void saveBookKeywords(String filePath) throws IOException {
        // 파일 읽기
        FileInputStream fileInputStream = new FileInputStream(new File(filePath));
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(1); // 두 번째 시트 가져오기

        for (Row row : sheet) {
            Cell titleCell = row.getCell(0); // A열
            Cell keywordCell = row.getCell(1); // B열

            if (titleCell != null && keywordCell != null) {
                Book book = bookRepository.findBookByBookName(getCellValue(titleCell));

                if (book == null)
                    continue;
                // 키워드 저장 ([, ] 제거)
                String rawKeywords = getCellValue(keywordCell)
                                        .replaceAll("[\\[\\]]", "");

                book.setKeyword(rawKeywords);
                bookRepository.save(book);
            }
        }

        workbook.close();
        fileInputStream.close();
    }

    private String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "UNKNOWN";
        }
    }

    // 유효한 책인지 확인하는 메서드
    private boolean isValidBook(Book book) {
        if (book.getIsbn() == null || book.getBookName() == null || book.getDescription() == null ||
                book.getAuthor() == null || book.getImage() == null || book.getPubDate() == null || book.getPublisher() == null) {
            return false;
        }

        if (bookRepository.existsByIsbn(book.getIsbn())) {
            return false;
        }

        if (book.getDescription().length() > 60000) {
            log.info("Description too long: {} characters", book.getDescription().length());
            book.setDescription(book.getDescription().substring(0, 60000)); // description을 60000자 제한
        }

        return true;
    }
    public void getKoreaMovie(Long page) {
        for (int n = 0; n < 10; n++) {
            try {
                StringBuffer result = new StringBuffer(); // 매번 새로 생성
                ObjectMapper objectMapper = new ObjectMapper();

                String defaultUrl = "https://api.themoviedb.org/3/discover/movie";
                String certificationOption = "&certification_country=KR&certification.lte=15&with_origin_country=KR&page=";

                page += n;

                // tmdb 한국 영화 조회 api(청불 이하 관람가)
                URL movieUrl = new URL(defaultUrl + tmdbOption + certificationOption + page + "&api_key=" + tmdbKey);
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
                JSONArray items = jsonResponse.getJSONArray("results");

                Long cnt = 0L;
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    Movie movie = objectMapper.readValue(item.toString(), Movie.class);

                    Long movieId = movie.getMovieId();
                    String description = movie.getDescription();
                    String movieName = movie.getMovieName();

                    if (movieId == null || description == null || movieName == null)
                        continue;

                    if (movieRepository.existsById(movieId))
                        continue;

                    if (description.length() > 255)
                        movie.setDescription(description.substring(0, 255));


                    StringBuffer infoResult = new StringBuffer(); // 매번 새로 생성

                    // 영화 상세 정보 조회 API
                    URL infoURL = new URL(tmdbURL + movieId + tmdbOption + "&api_key=" + tmdbKey);
                    HttpURLConnection infoUrlConnection = (HttpURLConnection) infoURL.openConnection();
                    infoUrlConnection.setRequestMethod("GET");
                    infoUrlConnection.setRequestProperty("Content-type", "application/json");

                    // BufferedReader로 응답을 UTF-8로 읽어오기
                    BufferedReader infoBf = new BufferedReader(new InputStreamReader(infoUrlConnection.getInputStream(), StandardCharsets.UTF_8));

                    String infoLine;
                    while ((infoLine = infoBf.readLine()) != null) {
                        infoResult.append(infoLine);
                    }

                    // JSON 파싱
                    JSONObject jsonInfoResponse = new JSONObject(infoResult.toString());

                    log.info("movie ID : {}", movieId);

                    // 이미지 경로
                    if (!jsonInfoResponse.isNull("poster_path")) {
                        String moviePath = jsonInfoResponse.getString("poster_path");
                        movie.setPoster(imageURL + moviePath);
                    }
                    else
                        continue; // poster_path가 null일 경우 건너뛰기

                    // 영화 장르
                    if (!jsonInfoResponse.isNull("genres")) {
                        JSONArray genresArray = jsonInfoResponse.getJSONArray("genres");
                        List<String> genres = new ArrayList<>();

                        for (int j = 0; j < genresArray.length(); j++) {
                            JSONObject genreObject = genresArray.getJSONObject(j);
                            genres.add(genreObject.getString("name"));
                        }
                        String genresString = String.join(", ", genres);
                        movie.setGenre(genresString);
                    }
                    else
                        continue; // genres가 null일 경우 건너뛰기

                    // 장소 리스트 DB에 저장
                    movieRepository.save(movie);
                    cnt++;
                }
                log.info("Movies inserted successfully into the database. count : {}", cnt);
            } catch (IOException e) {
                log.error("An error occurred while parsing and saving movie data: ", e);
            }
        }
    }

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
    public void insertKorData() {
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

    // tmdb 영화 id -> 영화명, 줄거리, 장르 영어 추가
    public void insertEnData() {
        List<Movie> movieList = movieRepository.findAll();

        for (Movie movie : movieList) {
            StringBuffer result = new StringBuffer(); // 매번 새로 생성
            try {
                // 영화 상세 정보 조회 api
                URL movieUrl = new URL(tmdbURL + movie.getMovieId().toString() + "?api_key=" + tmdbKey);
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

                log.info(jsonResponse.toString());
                log.info("Movie ID : {}", movie.getMovieId());

                // 영화 제목
                String movieName = jsonResponse.getString("title");
                movie.setMovieNameEn(movieName);

                // 영화 설명
                String description = jsonResponse.getString("overview");
                if (description.length() > 255) {
                    description = description.substring(0, 255);
                }
                movie.setDescriptionEn(description);

                // 영화 장르
                JSONArray genresArray = jsonResponse.getJSONArray("genres");
                List<String> genres = new ArrayList<>();

                for (int i = 0; i < genresArray.length(); i++) {
                    JSONObject genreObject = genresArray.getJSONObject(i);
                    genres.add(genreObject.getString("name"));
                }
                String genresString = String.join(", ", genres);
                movie.setGenreEn(genresString);

                log.info("Movies revised successfully into the database.");
            } catch (IOException e) {
                log.error("An error occurred while parsing and saving movie data: ", e);
            }
        }
        // 장소 리스트 DB에 저장
        movieRepository.saveAll(movieList);
    }

    public void insertEnKeywords() {
        List<Movie> movieList = movieRepository.findByKeywordIsNull();

        for (Movie movie : movieList) {
            StringBuffer result = new StringBuffer(); // 매번 새로 생성
            try {

                // 영화 상세 정보 조회 api
                URL movieUrl = new URL(tmdbURL + movie.getMovieId() + "/keywords" + "?api_key=" + tmdbKey);
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
                JSONArray keywordsArray = jsonResponse.getJSONArray("keywords");

                // 영화 장르
                List<String> keywords = new ArrayList<>();

                for (int i = 0; i < keywordsArray.length(); i++) {
                    JSONObject genreObject = keywordsArray.getJSONObject(i);
                    keywords.add(genreObject.getString("name"));
                }
                String keywordsString = String.join(", ", keywords);

                if (keywordsString.length() > 255) {
                    keywordsString = keywordsString.substring(0, 255);
                }

                movie.setKeyword(keywordsString);



                log.info("Movies revised successfully into the database.");
            } catch (IOException e) {
                log.error("An error occurred while parsing and saving movie data: ", e);
            }
        }
        // 장소 리스트 DB에 저장
        movieRepository.saveAll(movieList);
    }

    // Book 데이터베이스 Insert
    public void updateBookData(String keyword, List<Book> bookList) {

        ObjectMapper objectMapper = new ObjectMapper();

        for (int i = 1; i <= 901; i+=100) {
            try {
                StringBuffer result = new StringBuffer();

                String encodedQuery = URLEncoder.encode(keyword, StandardCharsets.UTF_8.toString());
                String startNo = "&start=" + i;

                // 도서 검색 api
                URL bookUrl = new URL(defaultURL + option + "query=" + encodedQuery + startNo);
                HttpURLConnection bookSearchUrlConnection = (HttpURLConnection) bookUrl.openConnection();
                bookSearchUrlConnection.setRequestMethod("GET");
                bookSearchUrlConnection.setRequestProperty("Content-type", "application/json");
                bookSearchUrlConnection.setRequestProperty("X-Naver-Client-Id", clientId);
                bookSearchUrlConnection.setRequestProperty("X-Naver-Client-Secret", clientSecret);

                log.info(bookUrl.toString());

                // BufferedReader로 응답을 UTF-8로 읽어오기
                BufferedReader bf = new BufferedReader(new InputStreamReader(bookSearchUrlConnection.getInputStream(), StandardCharsets.UTF_8));

                String line;
                while ((line = bf.readLine()) != null) {
                    result.append(line);
                }

                // JSON 파싱
                JSONObject jsonResponse = new JSONObject(result.toString());
                JSONArray items = jsonResponse.getJSONArray("items");

                for (int j = 0; j < items.length(); j++) {
                    JSONObject item = items.getJSONObject(j);
                    Book book = objectMapper.readValue(item.toString(), Book.class);

                    String isbn = book.getIsbn();
                    String author = book.getAuthor();
                    String bookName = book.getBookName();
                    String description = book.getDescription();
                    String publisher = book.getPublisher();

                    if (isbn == null || author == null || bookName == null || description == null || publisher == null)
                        continue;

                    if (description.length() > 255) {
                        book.setDescription(description.substring(0, 255));
                    }

                    if (!bookRepository.existsByIsbn(isbn))
                        bookList.add(book);
                }
            }
            catch (IOException e) {
                log.error("An error occurred while parsing and saving book data: ", e);
            }
        }
        // 책 리스트 DB에 저장
        bookRepository.saveAll(bookList);
        log.info("Books inserted successfully into the database.");
    }

    public void updateMovieDescriptFull() {
        String korParam = "?language=ko-kr";
//        String apiKeyParam = "&api_key=" + tmdbKey;
        String apiKeyParamEn = "?api_key=" + tmdbKey;

        List<Movie> movieList = movieRepository.findAll();

        for (Movie movie : movieList) {
             StringBuilder result = new StringBuilder(); // 매번 새로 생성
            try {
                // 영화 상세 정보 조회 api
                URL movieUrl = new URI(tmdbURL + movie.getMovieId().toString() + apiKeyParamEn).toURL();
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

                log.info(jsonResponse.toString());
                log.info("Movie ID : {}", movie.getMovieId());

                // 영화 설명
                String description = jsonResponse.getString("overview");
                log.info("Overview : {}", description);
                if (description.length() > 60000) {
//                    movie.setDescription(description.substring(0, 60000)); // description을 60000자 제한
                    movie.setDescriptionEn(description.substring(0, 60000)); // description을 60000자 제한
                }
//                else
//                    movie.setDescription(description);
                else
                    movie.setDescriptionEn(description);

                movieRepository.save(movie);

                log.info("Movie revised successfully into the database.");
                } catch (IOException e) {
                    log.error("An error occurred while parsing and saving movie data: ", e);
                } catch (URISyntaxException e) {
                    log.error("An error occurred while parsing URL: ", e);
            }
        }
    }
}
