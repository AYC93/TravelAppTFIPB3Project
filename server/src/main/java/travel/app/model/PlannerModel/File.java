package travel.app.model.PlannerModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class File {
    private String fileId;
    private String fileName;
    private String fileContentType;
    private byte[] fileData;
}
