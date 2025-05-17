package neordinary.backend.nteam.service;

import lombok.RequiredArgsConstructor;
import neordinary.backend.nteam.dto.DietDetailsResponseDto;
import neordinary.backend.nteam.dto.DietResponseDto;
import neordinary.backend.nteam.entity.Diet;
import neordinary.backend.nteam.entity.Member;
import neordinary.backend.nteam.global.apiPayload.code.status.ErrorStatus;
import neordinary.backend.nteam.global.exception.handler.DietHandler;
import neordinary.backend.nteam.global.exception.handler.MemberHandler;
import neordinary.backend.nteam.gpt.GPTApiClient;
import neordinary.backend.nteam.gpt.PhotoApiClient;
import neordinary.backend.nteam.gpt.openai.meal_plan.GPTResponseMealPlanDto;
import neordinary.backend.nteam.gpt.openai.recipe.GPTResponseRecipeDto;
import neordinary.backend.nteam.repository.DietRepository;
import neordinary.backend.nteam.repository.MemberRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
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
    private final PhotoApiClient photoApiClient;

    @Transactional
    public UUID createDiet(UUID memberId) {
        // TODO : 일기 반영 필요
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<GPTResponseMealPlanDto> gptResponseMealPlanDtos = gptApiClient.generateMealPlan(member);

        List<Diet> diets = new ArrayList<>();
        for (GPTResponseMealPlanDto gptResponseMealPlanDto : gptResponseMealPlanDtos) {
            Diet entity = gptResponseMealPlanDto.toEntity();
            entity.setMember(member);
            diets.add(entity);
        }

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

    @Transactional
    public List<DietResponseDto> getDietsByDate(UUID memberId, LocalDate date) {
        // 회원 존재 확인
        List<Diet> diets = dietRepository.findByMemberIdAndDate(memberId, date);

        for (Diet diet : diets) {
            if (diet.getImage() == null || diet.getImage().isEmpty()) {
                String imageUrl = photoApiClient.getPhotoUrl(diet.getName());
                diet.setImage(imageUrl);
            }
        }

        return diets.stream()
                .map(DietResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public DietDetailsResponseDto getDietsDetails(UUID memberId, Long dietId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Diet diet = dietRepository.findById(dietId)
                .orElseThrow(() -> new DietHandler(ErrorStatus.DIET_NOT_FOUND));

        // Recipe가 없을 경우 생성
        if (diet.getRecipe() == null) {
            List<String> instructions = gptApiClient.generateRecipe(member, diet);
            if (!instructions.isEmpty()) {
                diet.setRecipe(String.join("\n", instructions));
            }
        }

        return DietDetailsResponseDto.fromEntity(diet);
    }
}