package neordinary.backend.nteam.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.executable.ValidateOnExecution;
import lombok.RequiredArgsConstructor;
import neordinary.backend.nteam.dto.MemberRequestDto;
import neordinary.backend.nteam.dto.MemberResponseDto;
import neordinary.backend.nteam.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import neordinary.backend.nteam.global.apiPayload.ApiResponse;

import java.util.UUID;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Validated
@Tag(name = "Member API", description = "회원 관련 API")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "회원 정보 저장", description = "회원 정보를 저장합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 정보 저장 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("")
    public ApiResponse<?> createMember(@Valid @RequestBody MemberRequestDto request) {
        MemberResponseDto response = memberService.createMember(request);
        return ApiResponse.onCreated(response);
    }

    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다. ")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 정보 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음")
    })
    @PatchMapping("/{id}")
    public ApiResponse<?> updateMember(@PathVariable UUID id, @Valid @RequestBody MemberRequestDto requestDto) {
        MemberResponseDto response = memberService.updateMember(id, requestDto);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "회원 조회", description = "회원 정보를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 정보 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원이 존재하지 않음")
    })
    @GetMapping("/{id}")
    public ApiResponse<?> getMember(@PathVariable UUID id) {
        MemberResponseDto response = memberService.getMember(id);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "멤버 레벨 업그레이드", description = "회원의 멤버 레벨을 업그레이드합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "멤버 레벨 업그레이드 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원이 존재하지 않음")
    })
    @PutMapping("/{id}/member-level/upgrade")
    public ApiResponse<?> upgradeMemberLevel(@PathVariable UUID id) {
        MemberResponseDto upgradedMember = memberService.upgradeMemberLevel(id);
        return ApiResponse.onSuccess(upgradedMember);
    }
}