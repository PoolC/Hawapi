package com.moviePicker.api.movie.infra;

import com.opencsv.CSVWriter;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;

@Component
public class MovieCrawler {
    private final String baseUrl = "https://movie.naver.com";
    private final String directoryUrl = "https://movie.naver.com/movie/sdb/browsing/bmovie.naver?open=";

    private final String basicDataUrl = "https://movie.naver.com/movie/bi/mi/basic.naver?code=";
    private final String peopleDataUrl = "https://movie.naver.com/movie/bi/mi/detail.naver?code=";
    private final String photoDataUrl = "https://movie.naver.com/movie/bi/mi/photoView.naver?code=";


    private final String boxOfficeUrl = "https://movie.naver.com/movie/sdb/rank/rmovie.naver?sel=cur&date=";

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    String date = sdf.format(Calendar.getInstance().getTime());

    public ArrayList<String> crawlMovieCodesByYear(int year, int endPage) {

        StringBuilder url = new StringBuilder();
        url.append(directoryUrl);
        url.append(year);
        url.append("&page=");
        ArrayList<String> resultList = new ArrayList<>();
        for (int i = 1; i <= endPage; i++) {

            String url_ = url.toString() + i;
//            System.out.println("url_ = " + url_);
            try {
                Connection conn = Jsoup.connect(url_);
                Document doc = conn.get();
                Elements directoryLists = doc.select("ul.directory_list");


                Element directoryList = directoryLists.get(0);
                Elements ElemsA = directoryList.select("a");
                for (Element a : ElemsA) {
                    String targetUrl = a.attr("href");
                    if (targetUrl.charAt(0) == '/') {
                        String[] movieCode = targetUrl.split("code=");
//                        System.out.println(movieCode[1]);

                        resultList.add(movieCode[1]);
                    }

                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return resultList;
    }

    public ArrayList<String> crawlBoxOfficeMovieCodes() {

        StringBuilder url = new StringBuilder();
        url.append(boxOfficeUrl);
        url.append(date);
        ArrayList<String> resultList = new ArrayList<>();

        try {
            Connection conn = Jsoup.connect(url.toString());
            Document doc = conn.get();

            Optional<Elements> elems = Optional.ofNullable(doc.select("td.title div.tit5 a"));

            if (elems.isPresent()) {
                for (Element elem : elems.get()) {

                    resultList.add(elem.attr("href").toString().split("code=")[1]);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultList;
    }


    public void writeMovieDataToCsv(ArrayList<ArrayList<String>> movieData, String filePath) {
        ClassPathResource resource = new ClassPathResource(filePath);


        try {
            Path path = Paths.get(resource.getURI());

            CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(path.toString()), "EUC-KR"));
            String[] tableOfContent = new String[14];
            tableOfContent[0] = "movieCode";
            tableOfContent[1] = "title";
            tableOfContent[2] = "subtitle";
            tableOfContent[3] = "score";
            tableOfContent[4] = "genre";
            tableOfContent[5] = "country";
            tableOfContent[6] = "runningTime";
            tableOfContent[7] = "pubDate";
            tableOfContent[8] = "plot";
            tableOfContent[9] = "filmRating";
            tableOfContent[10] = "director";
            tableOfContent[11] = "actors";
            tableOfContent[12] = "poster";
            tableOfContent[13] = "stillCuts";

            writer.writeNext(tableOfContent);
            for (ArrayList<String> dataRow : movieData) {

                writer.writeNext(dataRow.toArray(new String[0]));
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();

        }

    }


    public ArrayList<ArrayList<String>> crawlMovieData(ArrayList<String> movieCodes) {

        ArrayList<ArrayList<String>> movieData = new ArrayList<>();
        for (String movieCode : movieCodes) {
            ArrayList<String> dataRow = new ArrayList<>();
            dataRow.add(movieCode);
            crawlBasicData(dataRow, movieCode);
            crawlPeopleData(dataRow, movieCode);
            crawlPhotoData(dataRow, movieCode);

            movieData.add(dataRow);

        }

        return movieData;


    }


    private void crawlBasicData(ArrayList<String> dataRow, String movieCode) {
        String url = basicDataUrl + movieCode;
        try {
            Connection conn = Jsoup.connect(url);
            Document doc = conn.get();
            String title = parseTitle(doc);
            String subtitle = parseSubtitle(doc);
            String score = parseScore(doc);
            String genre = parseGenre(doc);
            String country = parseCountry(doc);
            String runningTime = parseRunningTime(doc);
            String pubDate = parsePubDate(doc);
            String plot = parsePlot(doc);
            String filmRating = parseFilmRating(doc);

            dataRow.add(title);
            dataRow.add(subtitle);
            dataRow.add(score);
            dataRow.add(genre);
            dataRow.add(country);
            dataRow.add(runningTime);
            dataRow.add(pubDate);
            dataRow.add(plot);
            dataRow.add(filmRating);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void crawlPeopleData(ArrayList<String> dataRow, String movieCode) {
        String url = peopleDataUrl + movieCode;
        try {
            Connection conn = Jsoup.connect(url);
            Document doc = conn.get();
            String director = parseDirector(doc);
            String actors = parseActors(doc);

            dataRow.add(director);
            dataRow.add(actors);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void crawlPhotoData(ArrayList<String> dataRow, String movieCode) {
        String url = photoDataUrl + movieCode;
        try {
            Connection conn = Jsoup.connect(url);
            Document doc = conn.get();
            String poster = parsePoster(doc);
            String stillCuts = parseStillCuts(doc);
            dataRow.add(poster);
            dataRow.add(stillCuts);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String parseTitle(Element doc) {
        Optional<Element> elem = Optional.ofNullable(doc.select("h3.h_movie a").first());
        String title = "";
        if (elem.isPresent()) {
            title = elem.get().ownText();
        }
        return title;

    }

    private String parseSubtitle(Element doc) {
        Optional<Element> elem = Optional.ofNullable(doc.select("strong.h_movie2").first());
        String subtitle = "";
        if (elem.isPresent()) {
            subtitle = elem.get().ownText();
        }
        return subtitle;
    }

    private String parseScore(Document doc) {
        Optional<Element> elem = Optional.ofNullable(doc.select("div.main_score div.score a div.star_score span.st_off span.st_on").first());
        String score = "";
        if (elem.isPresent()) {
            score = elem.get().ownText();
            if (score.equals("관람객 평점 없음")) {
                score = "";
            } else {
                score = score.split(" ")[2];
            }
        }
        return score;
    }


    private String parseGenre(Element doc) {
        Optional<Elements> elems = Optional.ofNullable(doc.select("dl.info_spec dd p span:nth-of-type(1) a"));
        String genre = "";
        if (elems.isPresent()) {
            for (Element elem : elems.get()) {
                genre += elem.ownText();
                genre += "|";
            }
        }
        return genre;
    }

    private String parseCountry(Element doc) {
        Optional<Elements> elems = Optional.ofNullable(doc.select("dl.info_spec dd p span:nth-of-type(2) a"));
        String country = "";
        if (elems.isPresent()) {
            for (Element elem : elems.get()) {
                country += elem.ownText();
                country += "|";
            }
        }
        return country;


    }

    private String parseRunningTime(Document doc) {
        Optional<Element> elem = Optional.ofNullable(doc.select("dl.info_spec dd p span:nth-of-type(3)").first());
        String runningTime = "";
        if (elem.isPresent()) {
            runningTime = elem.get().ownText();
            if (runningTime.equals("개봉")) {
                runningTime = "";
            }
        }
        return runningTime;


    }

    private String parsePubDate(Document doc) {
        Optional<Element> elem = Optional.ofNullable(doc.select("dl.info_spec dd p span:nth-of-type(4):nth-child(n+3):nth-child(-n+4)").first());
        String pubDate = "";
        if (elem.isPresent()) {
            pubDate = elem.get().text().replace(" ", "");

        }
        return pubDate;
    }

    private String parseFilmRating(Document doc) {
        Optional<Element> elem = Optional.ofNullable(doc.select("dl.info_spec dd:nth-of-type(4) a").first());
        String filmRate = "";
        if (elem.isPresent()) {
            filmRate = elem.get().ownText();
        }
        return filmRate;
    }


    private String parsePlot(Document doc) {
        Optional<Element> elem = Optional.ofNullable(doc.select("div.story_area p.con_tx").first());
        String plot = "";
        if (elem.isPresent()) {
            plot = elem.get().ownText();
        }
        return plot;

    }

    private String parseDirector(Document doc) {
        Optional<Element> elem = Optional.ofNullable(doc.select("div.dir_product a.k_name").first());
        String director = "";
        if (elem.isPresent()) {
            director = elem.get().ownText();
        }
        return director;

    }

    private String parseActors(Element doc) {
        Optional<Elements> elems = Optional.ofNullable(doc.select("div.p_info a.k_name"));
        String actors = "";
        if (elems.isPresent()) {
            for (Element elem : elems.get()) {
                actors += elem.ownText();
                actors += "|";
            }
        }
        return actors;


    }

    private String parsePoster(Element doc) {
        Optional<Element> elem = Optional.ofNullable(doc.select("div.poster img").first());
        String poster = "";
        if (elem.isPresent()) {
            poster = elem.get().attr("src");
        }
        return poster;

    }

    private String parseStillCuts(Element doc) {
        Optional<Elements> elems = Optional.ofNullable(doc.select("div.rolling_list li._list"));
        String stillCuts = "";
        if (elems.isPresent()) {
            for (Element elem : elems.get()) {
                JSONObject jsonObject = new JSONObject(elem.attr("data-json").toString());

                stillCuts += jsonObject.getString("fullImageUrl665px");
                stillCuts += "|";
            }
        }
        return stillCuts;


    }


}
