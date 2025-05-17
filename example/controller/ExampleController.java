package neordinary.backend.nteam.example.controller;

import lombok.RequiredArgsConstructor;
import neordinary.backend.nteam.example.dto.ExampleResponse;
import neordinary.backend.nteam.example.service.ExampleService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/examples")
@RequiredArgsConstructor
public class ExampleController {

    private final ExampleService exampleService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ExampleResponse> uploadImage(
            @RequestPart MultipartFile image) throws IOException {
        return ResponseEntity.ok(exampleService.saveExample(image));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExampleResponse> getExample(@PathVariable Long id) {
        return ResponseEntity.ok(exampleService.getExample(id));
    }
} 