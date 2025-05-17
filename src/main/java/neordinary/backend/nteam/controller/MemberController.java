package neordinary.backend.nteam.controller;

import lombok.Getter;
import neordinary.backend.nteam.dto.MemberDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/members")
public interface MemberController{

    @PostMapping
    ResponseEntity<MemberDto.MemberResponse> createMember(MemberDto.MemberCreateRequest request);

    @PutMapping("/{id}")
    ResponseEntity<MemberDto.MemberResponse> updateMember(@PathVariable Long id, @RequestBody MemberDto.MemberUpdateRequest request);

    @GetMapping("/{id}")
    ResponseEntity<MemberDto.MemberResponse> getMember(@PathVariable Long id);
}