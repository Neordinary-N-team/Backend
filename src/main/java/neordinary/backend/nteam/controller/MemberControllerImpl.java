package neordinary.backend.nteam.controller;

import neordinary.backend.nteam.dto.MemberDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static neordinary.backend.nteam.dto.MemberDto.toResponse;
import static neordinary.backend.nteam.dto.MemberDto.toUpdatedResponse;

@RestController
public class MemberControllerImpl implements MemberController {

    private static final AtomicLong idGenerator = new AtomicLong(1);
    private static final Map<Long, MemberDto.MemberResponse> members = new ConcurrentHashMap<>();

    @Override
    public ResponseEntity<MemberDto.MemberResponse> createMember(MemberDto.MemberCreateRequest request) {
        MemberDto.MemberResponse response = toResponse(request, idGenerator.getAndIncrement());
        members.put(response.getId(), response);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<MemberDto.MemberResponse> updateMember(Long id, MemberDto.MemberUpdateRequest request) {
        MemberDto.MemberResponse existing = members.get(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        MemberDto.MemberResponse updated = toUpdatedResponse(id, request, existing);
        members.put(id, updated);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<MemberDto.MemberResponse> getMember(Long id) {
        MemberDto.MemberResponse member = members.get(id);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(member);
    }
}