package com.theocean.fundering.domain.celebrity.service;

import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.celebrity.repository.CelebRepository;
import com.theocean.fundering.domain.celebrity.repository.FollowRepository;
import com.theocean.fundering.global.errors.exception.Exception400;
import com.theocean.fundering.global.errors.exception.Exception500;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final CelebRepository celebRepository;
    private final ThreadLocal<Celebrity> threadLocal = new ThreadLocal<>();

    @Transactional
    public void followCelebs(final Long celebId, final Long memberId) {

        final Celebrity celebrity = celebRepository.findById(celebId).orElseThrow(
                () -> new Exception400("해당 셀럽을 찾을 수 없습니다.")
        );
        try{
            followRepository.saveFollow(celebrity.getCelebId(), memberId);
            celebrity.addFollowerCount();
            celebRepository.save(celebrity);
        } catch (final RuntimeException e) {
            throw new Exception500("팔로우에 실패했습니다.");
        }
    }
    @Transactional
    public void unFollowCelebs(final Long celebId, final Long memberId) {

        final Celebrity celebrity = celebRepository.findById(celebId).orElseThrow(
                () -> new Exception400("해당 셀럽을 찾을 수 없습니다.")
        );
        try{
            threadLocal.set(celebrity);
            followRepository.saveUnFollow(threadLocal.get().getCelebId(), memberId);
            threadLocal.get().minusFollowerCount();
            celebRepository.save(threadLocal.get());
        } catch (final RuntimeException e) {
            throw new Exception500("언팔로우에 실패했습니다.");
        }
    }
}