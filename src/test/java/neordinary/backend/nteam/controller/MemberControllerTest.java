package neordinary.backend.nteam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import neordinary.backend.nteam.dto.MemberRequestDto;
import neordinary.backend.nteam.dto.MemberResponseDto;
import neordinary.backend.nteam.global.apiPayload.code.status.ErrorStatus;
import neordinary.backend.nteam.global.exception.handler.MemberHandler;
import neordinary.backend.nteam.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberController memberController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UUID memberId;
    private MemberRequestDto memberRequestDto;
    private MemberResponseDto memberResponseDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
        memberId = UUID.randomUUID();
        
        memberRequestDto = MemberRequestDto.builder()
                .pregDate(LocalDate.of(2024, 5, 17))
                .height(160)
                .weight(60)
                .bmi(21.5f)
                .diseases("고혈압")
                .prePregnant(true)
                .hasMorningSickness(false)
                .veganLevel("OVO")
                .vegProteins("두부")
                .bannedVegetables("오이")
                .memberLevel(1)
                .build();
                
        memberResponseDto = MemberResponseDto.builder()
                .id(memberId)
                .pregDate(LocalDate.of(2024, 5, 17))
                .height(160)
                .weight(60)
                .bmi(21.5f)
                .diseases("고혈압")
                .prePregnant(true)
                .hasMorningSickness(false)
                .veganLevel("OVO")
                .vegProteins("두부")
                .bannedVegetables("오이")
                .memberLevel(1)
                .build();
    }

    @Test
    @DisplayName("회원 생성 성공 테스트")
    void createMember_Success() throws Exception {
        // given
        given(memberService.createMember(any(MemberRequestDto.class))).willReturn(memberResponseDto);

        // when, then
        mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.result.id").value(memberId.toString()))
                .andExpect(jsonPath("$.result.height").value(160))
                .andExpect(jsonPath("$.result.weight").value(60))
                .andExpect(jsonPath("$.result.veganLevel").value("OVO"));
    }

    @Test
    @DisplayName("회원 정보 업데이트 성공 테스트")
    void updateMember_Success() throws Exception {
        // given
        MemberResponseDto updatedResponse = MemberResponseDto.builder()
                .id(memberId)
                .pregDate(LocalDate.of(2024, 5, 17))
                .height(165)
                .weight(65)
                .bmi(23.9f)
                .diseases("당뇨")
                .prePregnant(true)
                .hasMorningSickness(true)
                .veganLevel("LACTO")
                .vegProteins("두부, 콩")
                .bannedVegetables("오이, 가지")
                .memberLevel(1)
                .build();
                
        given(memberService.updateMember(any(UUID.class), any(MemberRequestDto.class))).willReturn(updatedResponse);

        // when, then
        mockMvc.perform(patch("/api/members/{id}", memberId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").value(memberId.toString()))
                .andExpect(jsonPath("$.result.height").value(165))
                .andExpect(jsonPath("$.result.weight").value(65))
                .andExpect(jsonPath("$.result.diseases").value("당뇨"))
                .andExpect(jsonPath("$.result.veganLevel").value("LACTO"));
    }

    @Test
    @DisplayName("존재하지 않는 회원 정보 업데이트 실패 테스트")
    void updateMember_MemberNotFound_ThrowsException() throws Exception {
        // given
        given(memberService.updateMember(any(UUID.class), any(MemberRequestDto.class)))
                .willThrow(new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // when, then
        mockMvc.perform(patch("/api/members/{id}", memberId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberRequestDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("회원 조회 성공 테스트")
    void getMember_Success() throws Exception {
        // given
        given(memberService.getMember(memberId)).willReturn(memberResponseDto);

        // when, then
        mockMvc.perform(get("/api/members/{id}", memberId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").value(memberId.toString()))
                .andExpect(jsonPath("$.result.height").value(160))
                .andExpect(jsonPath("$.result.weight").value(60))
                .andExpect(jsonPath("$.result.veganLevel").value("OVO"));
    }

    @Test
    @DisplayName("존재하지 않는 회원 조회 실패 테스트")
    void getMember_MemberNotFound_ThrowsException() throws Exception {
        // given
        given(memberService.getMember(memberId)).willThrow(new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // when, then
        mockMvc.perform(get("/api/members/{id}", memberId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("회원 레벨 업그레이드 성공 테스트")
    void upgradeMemberLevel_Success() throws Exception {
        // given
        MemberResponseDto upgradedResponse = MemberResponseDto.builder()
                .id(memberId)
                .pregDate(LocalDate.of(2024, 5, 17))
                .height(160)
                .weight(60)
                .bmi(21.5f)
                .diseases("고혈압")
                .prePregnant(true)
                .hasMorningSickness(false)
                .veganLevel("OVO")
                .vegProteins("두부")
                .bannedVegetables("오이")
                .memberLevel(2) // Upgraded level
                .build();
                
        given(memberService.upgradeMemberLevel(memberId)).willReturn(upgradedResponse);

        // when, then
        mockMvc.perform(put("/api/members/{id}/member-level/upgrade", memberId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").value(memberId.toString()))
                .andExpect(jsonPath("$.result.memberLevel").value(2));
    }

    @Test
    @DisplayName("존재하지 않는 회원 레벨 업그레이드 실패 테스트")
    void upgradeMemberLevel_MemberNotFound_ThrowsException() throws Exception {
        // given
        given(memberService.upgradeMemberLevel(memberId))
                .willThrow(new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // when, then
        mockMvc.perform(put("/api/members/{id}/member-level/upgrade", memberId))
                .andExpect(status().isNotFound());
    }
} 