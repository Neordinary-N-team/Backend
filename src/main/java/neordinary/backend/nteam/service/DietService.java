package neordinary.backend.nteam.service;

import lombok.RequiredArgsConstructor;
import neordinary.backend.nteam.dto.DietResponseDto;
import neordinary.backend.nteam.entity.Diet;
import neordinary.backend.nteam.entity.Member;
import neordinary.backend.nteam.global.apiPayload.code.status.ErrorStatus;
import neordinary.backend.nteam.global.exception.handler.DietHandler;
import neordinary.backend.nteam.global.exception.handler.MemberHandler;
import neordinary.backend.nteam.gpt.GPTApiClient;
import neordinary.backend.nteam.gpt.openai.meal_plan.GPTResponseMealPlanDto;
import neordinary.backend.nteam.repository.DietRepository;
import neordinary.backend.nteam.repository.MemberRepository;
import org.springframework.scheduling.annotation.Scheduled;
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
    private final GPTApiClient gptApiClient;

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

    @Transactional
    public UUID createDiet(UUID memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<GPTResponseMealPlanDto> gptResponseMealPlanDtos = gptApiClient.generateMealPlan(member);

        List<Diet> diets = gptResponseMealPlanDtos.stream()
                .map(GPTResponseMealPlanDto::toEntity)
                .toList();

        dietRepository.saveAll(diets);

        return memberId;
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * MON") // 매주 일요일 0시 실행
    public void createDietScheduled() {
        // TODO : 멀티 스레드 리팩토링 필요
        List<Member> members = memberRepository.findAll();

        for (Member member : members) {
            List<GPTResponseMealPlanDto> gptResponseMealPlanDtos = gptApiClient.generateMealPlan(member);
            List<Diet> diets = gptResponseMealPlanDtos.stream()
                    .map(GPTResponseMealPlanDto::toEntity)
                    .toList();
            dietRepository.saveAll(diets);
        }
    }

    // TODO : get diets 하면 사진 없을 때 사진 생성 필요
    // TODO : get recipe 하면 레시피 없을 때 레시피 생성 필요
} 