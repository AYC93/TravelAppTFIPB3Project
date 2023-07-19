package travel.app.service.WeatherService;

public class WeatherException extends Exception{
    public WeatherException() {
    }

	public WeatherException(String msg) { 
		super(msg);
	}
}
