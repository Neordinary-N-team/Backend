package neordinary.backend.nteam.example.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import neordinary.backend.nteam.example.entity.Example;

@Getter
@NoArgsConstructor
public class ExampleRequest {
    
    public Example toEntity(String imageBase64) {
        return Example.builder()
                .imageBase64(imageBase64)
                .build();
    }
} 