package neordinary.backend.nteam.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import neordinary.backend.nteam.dto.MemberRequestDto;
import neordinary.backend.nteam.dto.MemberResponseDto;
import neordinary.backend.nteam.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Validated
@Tag(name = "Member API", description = "회원 관련 API")
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    @Operation(summary = "회원 정보 저장", description = "회원 정보를 저장합니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원 정보 저장 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<MemberResponseDto> createMember(@Valid @RequestBody MemberRequestDto request) {
        MemberResponseDto response = memberService.createMember(request);
        return ResponseEntity.status(201).body(response);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 정보 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음")
    })
    public ResponseEntity<MemberResponseDto> updateMember(@PathVariable UUID id, @RequestBody MemberRequestDto requestDto) {
        MemberResponseDto response = memberService.updateMember(id, requestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "회원 조회", description = "회원 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "회원이 존재하지 않음")
    })
    public ResponseEntity<MemberResponseDto> getMember(@PathVariable UUID id) {
        MemberResponseDto response = memberService.getMember(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/member-level/upgrade")
    @Operation(summary = "멤버 레벨 업그레이드", description = "회원의 멤버 레벨을 업그레이드합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "멤버 레벨 업그레이드 성공"),
            @ApiResponse(responseCode = "404", description = "회원이 존재하지 않음")
    })
    public ResponseEntity<MemberResponseDto> upgradeMemberLevel(@PathVariable UUID id) {
        MemberResponseDto upgradedMember = memberService.upgradeMemberLevel(id);
        return ResponseEntity.ok(upgradedMember);
    }
}