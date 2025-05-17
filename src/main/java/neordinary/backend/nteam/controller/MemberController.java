package neordinary.backend.nteam.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import neordinary.backend.nteam.dto.MemberRequestDto;
import neordinary.backend.nteam.dto.MemberResponseDto;
import neordinary.backend.nteam.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import neordinary.backend.nteam.global.apiPayload.ApiResponse;

import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Validated
@Tag(name = "Member API", description = "회원 관련 API")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "회원가입", description = "회원 정보를 저장합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 정보 저장 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("/members")
    public ApiResponse<?> createMember(@RequestBody MemberRequestDto request) {
        try {
            MemberResponseDto response = memberService.createMember(request);
            return ApiResponse.onSuccess(response);
        } catch (IllegalArgumentException ex) {
            return ApiResponse.onFailure("CREATE_MEMBER_FAILED", ex.getMessage(), null);
        } catch (Exception ex) {
            return ApiResponse.onFailure("INTERNAL_ERROR", "서버 내부 오류가 발생했습니다.", null);
        }
    }

    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다. ")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 정보 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음")
    })
    @PatchMapping("/{id}")
    public ApiResponse<?> updateMember(@PathVariable UUID id, @RequestBody MemberRequestDto requestDto) {
        try {
            MemberResponseDto response = memberService.updateMember(id, requestDto);
            return ApiResponse.onSuccess(response);
        } catch (IllegalArgumentException e) {
            return ApiResponse.onFailure("UPDATE_FAILED", e.getMessage(), null);
        } catch (NoSuchElementException e) {
            return ApiResponse.onFailure("MEMBER_NOT_FOUND", "해당 회원을 찾을 수 없습니다.", null);
        }
    }

    @Operation(summary = "회원 조회", description = "회원 정보를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 정보 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원이 존재하지 않음")
    })
    @GetMapping("/{id}")
    public ApiResponse<?> getMember(@PathVariable UUID id) {
        try {
            MemberResponseDto response = memberService.getMember(id);
            return ApiResponse.onSuccess(response);
        } catch (NoSuchElementException e) {
            return ApiResponse.onFailure("MEMBER_NOT_FOUND", "해당 회원을 찾을 수 없습니다.", null);
        }
    }

    @Operation(summary = "멤버 레벨 업그레이드", description = "회원의 멤버 레벨을 업그레이드합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "멤버 레벨 업그레이드 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "회원이 존재하지 않음")
    })
    @PutMapping("/{id}/member-level/upgrade")
    public ResponseEntity<MemberResponseDto> upgradeMemberLevel(@PathVariable UUID id) {
        MemberResponseDto upgradedMember = memberService.upgradeMemberLevel(id);
        return ResponseEntity.ok(upgradedMember);
    }
}