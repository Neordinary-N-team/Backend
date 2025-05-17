package neordinary.backend.nteam.gpt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Component
public class PhotoApiClientImpl implements PhotoApiClient {

    @Value("${google.api.key}")
    private String apiKey;

    @Value("${google.api.cx}")
    private String cx;

    @Override
    public String getPhotoUrl(String keyword) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl("https://www.googleapis.com/customsearch/v1")
                    .queryParam("key", apiKey)
                    .queryParam("cx", cx)
                    .queryParam("q", keyword)
                    .queryParam("searchType", "image")
                    .queryParam("num", 1)
                    .build()
                    .toUriString();

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            List<Map<String, Object>> items = (List<Map<String, Object>>) response.getBody().get("items");
            if (items != null && !items.isEmpty()) {
                return (String) items.get(0).get("link");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
