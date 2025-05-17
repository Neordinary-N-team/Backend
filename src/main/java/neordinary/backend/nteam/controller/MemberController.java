package neordinary.backend.nteam.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import neordinary.backend.nteam.dto.MemberDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static neordinary.backend.nteam.dto.MemberDto.toResponse;
import static neordinary.backend.nteam.dto.MemberDto.toUpdatedResponse;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Validated
@Tag(name = "Member API", description = "회원 관련 API")
public class MemberController {

    @PostMapping
    @Operation(summary = "회원 정보 저장", description = "회원 정보를 저장합니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원 정보 저장 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<MemberDto.MemberResponse> createMember(@RequestBody MemberDto.MemberCreateRequest request) {
        UUID mockId = UUID.randomUUID();
        MemberDto.MemberResponse response = toResponse(request, mockId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 정보 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음")
    })
    public ResponseEntity<MemberDto.MemberResponse> updateMember(@PathVariable UUID id, @RequestBody MemberDto.MemberUpdateRequest request) {
        MemberDto.MemberResponse mockExisting = toResponse(new MemberDto.MemberCreateRequest("MockUser", "mock@example.com"), id);
        MemberDto.MemberResponse updated = toUpdatedResponse(id, request, mockExisting);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    @Operation(summary = "회원 조회", description = "회원 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "회원이 존재하지 않음")
    })
    public ResponseEntity<MemberDto.MemberResponse> getMember(@PathVariable UUID id) {
        MemberDto.MemberResponse mockMember = toResponse(new MemberDto.MemberCreateRequest("MockUser", "mock@example.com"), id);
        return ResponseEntity.ok(mockMember);
    }

    @PostMapping("/{id}/vegan-level/upgrade")
    @Operation(summary = "비건 레벨 업그레이드", description = "회원의 비건 레벨을 업그레이드합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "비건 레벨 업그레이드 성공"),
            @ApiResponse(responseCode = "404", description = "회원이 존재하지 않음")
    })
    public ResponseEntity<MemberDto.MemberResponse> upgradeVeganLevel(@PathVariable UUID id) {
        MemberDto.MemberResponse mockMember = toResponse(new MemberDto.MemberCreateRequest("MockUser", "mock@example.com"), id);

        // 비건 레벨 업그레이드 (필요 시 실제 로직 구현)
        // mockMember.setVeganLevel(mockMember.getVeganLevel().next());

        return ResponseEntity.ok(mockMember);
    }
}