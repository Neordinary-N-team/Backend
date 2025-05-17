package neordinary.backend.nteam.example.dto;

import lombok.Builder;
import lombok.Getter;
import neordinary.backend.nteam.example.entity.Example;

@Getter
@Builder
public class ExampleResponse {
    private Long id;
    private String imageBase64;

    public static ExampleResponse of(Example example) {
        return ExampleResponse.builder()
                .id(example.getId())
                .imageBase64(example.getImageBase64())
                .build();
    }
} 