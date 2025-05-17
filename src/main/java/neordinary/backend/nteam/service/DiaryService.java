package neordinary.backend.nteam.service;

import lombok.RequiredArgsConstructor;
import neordinary.backend.nteam.dto.DiaryCommentResponseDto;
import neordinary.backend.nteam.dto.DiaryRequestDto;
import neordinary.backend.nteam.dto.DiaryResponseDto;
import neordinary.backend.nteam.entity.Diary;
import neordinary.backend.nteam.entity.Member;
import neordinary.backend.nteam.global.apiPayload.code.status.ErrorStatus;
import neordinary.backend.nteam.global.exception.handler.DiaryHandler;
import neordinary.backend.nteam.global.exception.handler.MemberHandler;
import neordinary.backend.nteam.gpt.GPTApiClient;
import neordinary.backend.nteam.gpt.openai.diary.GPTResponseDiaryCommentDto;
import neordinary.backend.nteam.repository.DiaryRepository;
import neordinary.backend.nteam.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;
    private final GPTApiClient gptApiClient;

    public List<DiaryResponseDto> getDiariesByDate(UUID memberId, LocalDate date) {
        if (memberId == null) {
            throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
        }
        
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        
        List<Diary> diaries = diaryRepository.findByMemberAndDate(member, date);
        
        return diaries.stream()
                .map(this::mapToDiaryResponseDto)
                .collect(Collectors.toList());
    }

    public Long createDiary(UUID memberId, Diary diary) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        
        diary.setMember(member);
        Diary savedDiary = diaryRepository.save(diary);

        return savedDiary.getId();
    }

    @Transactional
    public DiaryResponseDto createDiary(DiaryRequestDto diaryRequest, String imageData) {
        Member member = memberRepository.findById(diaryRequest.getMemberId())
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        
        Diary diary = Diary.builder()
                .image(imageData)
                .ingredients(diaryRequest.getIngredients())
                .comment("") 
                .mealType(diaryRequest.getType())
                .member(member)
                .build();
        
        Diary savedDiary = diaryRepository.save(diary);
        
        GPTResponseDiaryCommentDto commentDto = gptApiClient.generateDiaryComment(savedDiary);
        savedDiary.setComment(commentDto.getComment());
        
        diaryRepository.save(savedDiary);
        
        return mapToDiaryResponseDto(savedDiary);
    }
    
    @Transactional
    public void deleteDiary(UUID memberId, LocalDate date) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."));
        
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        
        Diary diary = diaryRepository.findByMemberAndCreatedAtBetween(member, startOfDay, endOfDay)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 날짜의 일기를 찾을 수 없습니다."));
        
        diaryRepository.delete(diary);
    }
    
    private DiaryResponseDto mapToDiaryResponseDto(Diary diary) {
        return DiaryResponseDto.builder()
                .id(diary.getId())
                .image(diary.getImage())
                .ingredients(diary.getIngredients())
                .comment(diary.getComment())
                .mealType(diary.getMealType())
                .createdAt(diary.getCreatedAt())
                .build();
    }

    public List<DiaryCommentResponseDto> getDiarysByDate(UUID memberId, LocalDate date) {
        List<Diary> diaries = diaryRepository.findAllByMemberIdAndCreatedDate(memberId, date);

        return diaries.stream()
                .map(diary -> DiaryCommentResponseDto.builder()
                        .mealType(diary.getMealType())
                        .comment(diary.getComment())
                        .build())
                .toList();
    }
}