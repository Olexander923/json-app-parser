package shadrin.dev.json_app_parser.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileProperties {
    private final String filePath;

    public FileProperties(@Value("${mypath:classpath:tickets.json}") String filePath) {

        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}
