package travel.app.model.WeatherApiModel;

import java.util.List;

public class WeatherTempInfo {
    private List<WeatherInfo> weatherInfoList;
    private TempInfo tempInfo;

    public WeatherTempInfo(List<WeatherInfo> weatherInfoList, TempInfo tempInfo) {
        this.weatherInfoList = weatherInfoList;
        this.tempInfo = tempInfo;
    }

    public List<WeatherInfo> getWeatherInfoList() {
        return weatherInfoList;
    }

    public TempInfo getTempInfo() {
        return tempInfo;
    }
}
