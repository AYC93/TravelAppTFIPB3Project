package travel.app.service.WeatherService;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import travel.app.model.WeatherApiModel.TempInfo;
import travel.app.model.WeatherApiModel.WeatherInfo;
import travel.app.model.WeatherApiModel.WeatherLoc;
import travel.app.model.WeatherApiModel.WeatherTempInfo;

@Service
public class WeatherService {

    public static final String URL = "https://api.openweathermap.org/data/2.5/weather";

    public static final String URL_LOC = "http://api.openweathermap.org/geo/1.0/direct";

    // double check why this not working
    @Value("${openweathermap.api.key}")
    private String appId;

    // Have to be used together with getWeatherLocation to get lat and lon
    public List<WeatherTempInfo> getWeather(String city) throws WeatherException {

        List<WeatherLoc> weatherLocations = getWeatherLocation(city);

        List<WeatherTempInfo> weatherTempInfoList = new ArrayList<>();

        for (WeatherLoc weatherLoc : weatherLocations) {
            String lat = weatherLoc.lat();
            String lon = weatherLoc.lon();

            // https://api.openweathermap.org/data/2.5/weather?lat=<lat>&lon=<lon>&units=<units>&appid=<appid>            
            String url = UriComponentsBuilder.fromUriString(URL)
                    .queryParam("lat", lat)
                    .queryParam("lon", lon)
                    .queryParam("units", "metric")
                    .queryParam("appid", appId)
                    .toUriString();

            RequestEntity<Void> req = RequestEntity.get(url)
                    .accept(MediaType.APPLICATION_JSON)
                    .build();

            RestTemplate template = new RestTemplate();
            ResponseEntity<String> resp = null;
            try {
                resp = template.exchange(req, String.class);
            } catch (RestClientException ex) {
                // any status code not in 200, 300
                throw new WeatherException(ex.getMessage());
            }

            String payload = resp.getBody();
            JsonReader reader = Json.createReader(new StringReader(payload));
            JsonObject data = reader.readObject();
            List<WeatherInfo> weatherInfo = data.getJsonArray("weather").stream()
                    .map(v -> v.asJsonObject())
                    .map(o -> new WeatherInfo(o.getString("main"), o.getString("description"),
                            o.getString("icon")))
                    .collect(Collectors.toList());

            JsonObject jsonTempInfo = data.getJsonObject("main");
            TempInfo tempInfo = new TempInfo(
                    jsonTempInfo.getJsonNumber("temp").toString(),
                    jsonTempInfo.getJsonNumber("temp_min").toString(),
                    jsonTempInfo.getJsonNumber("temp_max").toString());


            WeatherTempInfo weatherTempInfo = new WeatherTempInfo(weatherInfo, tempInfo, weatherLoc);
            weatherTempInfoList.add(weatherTempInfo);
        }

        return weatherTempInfoList;
    }

    public List<WeatherLoc> getWeatherLocation(String city) throws WeatherException {

        // http://api.openweathermap.org/geo/1.0/direct?q={city name}&appid={API key}
        String url = UriComponentsBuilder.fromUriString(URL_LOC)
                .queryParam("q", city.replaceAll(" ", "+"))
                .queryParam("appid", appId)
                .toUriString();

        RequestEntity<Void> req = RequestEntity.get(url)
                .accept(MediaType.APPLICATION_JSON)
                .build();

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = null;
        try {
            resp = template.exchange(req, String.class);
        } catch (RestClientException ex) {
            // any status code not in 200, 300
            throw new WeatherException(ex.getMessage());
        }

        List<WeatherLoc> weatherLocList = new ArrayList<>();

        String payload = resp.getBody();
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonArray weatherArray = reader.readArray();
        for (JsonValue value : weatherArray) {
            JsonObject locObject = value.asJsonObject();
            JsonNumber latNo = locObject.getJsonNumber("lat");
            JsonNumber lonNo = locObject.getJsonNumber("lon");
            String lat = latNo.toString();
            String lon = lonNo.toString();
            WeatherLoc weatherLoc = new WeatherLoc(lat, lon);
            weatherLocList.add(weatherLoc);
        }
        return weatherLocList;

    }
}
