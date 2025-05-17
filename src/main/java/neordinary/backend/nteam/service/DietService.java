package neordinary.backend.nteam.service;

import lombok.RequiredArgsConstructor;
import neordinary.backend.nteam.dto.DietResponseDto;
import neordinary.backend.nteam.entity.Diet;
import neordinary.backend.nteam.entity.Member;
import neordinary.backend.nteam.global.apiPayload.code.status.ErrorStatus;
import neordinary.backend.nteam.global.exception.handler.DietHandler;
import neordinary.backend.nteam.global.exception.handler.MemberHandler;
import neordinary.backend.nteam.repository.DietRepository;
import neordinary.backend.nteam.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new DietHandler(ErrorStatus.START_DATE_AFTER_END_DATE);
        }

        long monthsBetween = Period.between(startDate, endDate).toTotalMonths();
        if (monthsBetween > MAX_PERIOD_MONTHS) {
            throw new DietHandler(ErrorStatus.PERIOD_TOO_LONG);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));


        List<Diet> diets = dietRepository.findByMemberAndDateBetweenOrderByDateAscMealTypeAsc(member, startDate, endDate);

        return diets.stream()
                .map(DietResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
} 