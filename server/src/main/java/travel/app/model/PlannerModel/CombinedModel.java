package travel.app.model.PlannerModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import travel.app.model.WeatherApiModel.WeatherTempInfo;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CombinedModel {
    private WeatherTempInfo weatherTempInfo;
    private Planner planner;
}
