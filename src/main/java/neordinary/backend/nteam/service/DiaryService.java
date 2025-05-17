package neordinary.backend.nteam.service;

import lombok.RequiredArgsConstructor;
import neordinary.backend.nteam.dto.DiaryResponseDto;
import neordinary.backend.nteam.entity.Diary;
import neordinary.backend.nteam.entity.Member;
import neordinary.backend.nteam.repository.DiaryRepository;
import neordinary.backend.nteam.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryService {
    private static final int MAX_PERIOD_MONTHS = 2;
    
    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;
    
    public List<DiaryResponseDto> getDiariesByPeriod(UUID memberId, LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "시작일이 종료일보다 늦을 수 없습니다.");
        }
        
        long monthsBetween = Period.between(startDate, endDate).toTotalMonths();
        
        if (monthsBetween > MAX_PERIOD_MONTHS) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "최대 " + MAX_PERIOD_MONTHS + "개월까지만 조회 가능합니다.");
        }
        
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."));
        
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        
        List<Diary> diaries = diaryRepository.findByMemberAndCreatedAtBetweenOrderByCreatedAtAsc(
                member, startDateTime, endDateTime);
        
        return diaries.stream()
                .map(this::mapToDiaryResponseDto)
                .collect(Collectors.toList());
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
                .memberId(diary.getMember().getId())
                .image(diary.getImage())
                .ingredients(diary.getIngredients())
                .satisfiedComment(diary.getSatisfiedComment())
                .dissatisfiedComment(diary.getDissatisfiedComment())
                .createdAt(diary.getCreatedAt())
                .build();
    }
} 