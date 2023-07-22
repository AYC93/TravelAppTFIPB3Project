package travel.app.controller;

import org.springframework.stereotype.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import travel.app.model.WeatherApiModel.TempInfo;
import travel.app.model.WeatherApiModel.WeatherTempInfo;
import travel.app.service.WeatherService.WeatherException;
import travel.app.service.WeatherService.WeatherService;

@CrossOrigin
@Controller
@RequestMapping(path="/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class WeatherController {
    
    @Autowired
	private WeatherService weatherSvc;

	@GetMapping(path="/weather")
	@ResponseBody
	public ResponseEntity<String> getWeather(@RequestParam String city) {

			JsonObjectBuilder objBuilder = Json.createObjectBuilder();
		try {
			List<WeatherTempInfo> weatherTempInfoList = weatherSvc.getWeather(city);
			
			// JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
			weatherTempInfoList.forEach(weatherTempInfo -> {
				JsonArrayBuilder weatherArrBuilder = Json.createArrayBuilder();
				weatherTempInfo.getWeatherInfoList().forEach(d -> 
				weatherArrBuilder.add(Json.createObjectBuilder()
								.add("main", d.main())
								.add("description", d.description())
								.add("icon", d.icon())
								));
				objBuilder.add("weather", weatherArrBuilder);

				JsonObjectBuilder tempObjBuilder = Json.createObjectBuilder();
				TempInfo tempInfo = weatherTempInfo.getTempInfo();
				tempObjBuilder.add("temp", tempInfo.temp())
								.add("tempMax", tempInfo.tempMax())
								.add("tempMin", tempInfo.tempMin());

					objBuilder.add("main", tempObjBuilder);
			});
			return ResponseEntity.ok(objBuilder.build().toString());

		} catch (WeatherException ex) {
			return ResponseEntity.status(400)
				.body(
					Json.createObjectBuilder()
						.add("error", ex.getMessage())
						.build().toString()
				);
		}
	}
}
