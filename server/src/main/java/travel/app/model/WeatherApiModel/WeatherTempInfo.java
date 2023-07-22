package travel.app.model.WeatherApiModel;

import java.util.List;

public class WeatherTempInfo {
    private List<WeatherInfo> weatherInfoList;
    private TempInfo tempInfo;
    private WeatherLoc weatherLoc;

    public WeatherTempInfo(List<WeatherInfo> weatherInfoList, TempInfo tempInfo, WeatherLoc weatherLoc) {
        this.weatherInfoList = weatherInfoList;
        this.tempInfo = tempInfo;
        this.weatherLoc = weatherLoc;
    }

    public List<WeatherInfo> getWeatherInfoList() {
        return weatherInfoList;
    }

    public TempInfo getTempInfo() {
        return tempInfo;
    }

    public WeatherLoc getWeatherLoc() {
        return weatherLoc;
    }
}
