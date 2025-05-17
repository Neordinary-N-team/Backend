package neordinary.backend.nteam.example.service;

import lombok.RequiredArgsConstructor;
import neordinary.backend.nteam.example.dto.ExampleResponse;
import neordinary.backend.nteam.example.entity.Example;
import neordinary.backend.nteam.example.repository.ExampleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class ExampleService {

    private final ExampleRepository exampleRepository;

    @Transactional
    public ExampleResponse saveExample(MultipartFile image) throws IOException {
        String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
        String contentType = image.getContentType();
        String imagePrefix = "data:" + contentType + ";base64,";
        String imageBase64 = imagePrefix + base64Image;
        
        Example example = Example.builder()
                .imageBase64(imageBase64)
                .build();
        Example savedExample = exampleRepository.save(example);
        return ExampleResponse.of(savedExample);
    }

    @Transactional(readOnly = true)
    public ExampleResponse getExample(Long id) {
        Example example = exampleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예제를 찾을 수 없습니다. ID: " + id));
        return ExampleResponse.of(example);
    }
} 