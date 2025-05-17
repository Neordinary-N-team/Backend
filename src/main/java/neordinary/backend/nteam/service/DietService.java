package neordinary.backend.nteam.service;

import lombok.RequiredArgsConstructor;
import neordinary.backend.nteam.dto.DietResponseDto;
import neordinary.backend.nteam.entity.Diet;
import neordinary.backend.nteam.entity.Member;
import neordinary.backend.nteam.repository.DietRepository;
import neordinary.backend.nteam.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DietService {
    private static final int MAX_PERIOD_MONTHS = 2;
    
    private final DietRepository dietRepository;
    private final MemberRepository memberRepository;
    
    public List<DietResponseDto> getDietsByPeriod(UUID memberId, LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "시작일이 종료일보다 늦을 수 없습니다.");
        }
        
        long monthsBetween = Period.between(startDate, endDate).toTotalMonths();
        
        if (monthsBetween > MAX_PERIOD_MONTHS) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "최대 " + MAX_PERIOD_MONTHS + "개월까지만 조회 가능합니다.");
        }
        
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."));
        
        List<Diet> diets = dietRepository.findByMemberAndDateBetweenOrderByDateAscMealTypeAsc(member, startDate, endDate);
        
        return diets.stream()
                .map(DietResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
} 