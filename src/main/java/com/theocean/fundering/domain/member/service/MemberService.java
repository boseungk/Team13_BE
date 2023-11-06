package com.theocean.fundering.domain.member.service;

import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.member.dto.MemberSettingRequestDTO;
import com.theocean.fundering.domain.member.dto.MemberSettingResponseDTO;
import com.theocean.fundering.domain.member.dto.MemberSignUpRequestDTO;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.global.errors.exception.Exception400;
import com.theocean.fundering.global.errors.exception.Exception500;
import com.theocean.fundering.global.utils.AWSS3Uploader;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final AWSS3Uploader awss3Uploader;

    @Transactional
    public void signUp(final MemberSignUpRequestDTO requestDTO) {
        sameCheckEmail(requestDTO.getEmail());
        requestDTO.encodePassword(passwordEncoder.encode(requestDTO.getPassword()));
        try {
            memberRepository.save(requestDTO.mapToEntity());
        } catch (final RuntimeException e) {
            throw new Exception500("회원가입 실패");
        }
    }

    public void sameCheckEmail(final String email) {
        memberRepository.findByEmail(email).ifPresent(n -> {
            throw new Exception400("이메일이 존재합니다.");
        });
    }

    public MemberSettingResponseDTO findAllUserSetting(final Long id) {
        final Member member = memberRepository.findById(id).orElseThrow(
                () -> new Exception400("회원을 찾을 수 없습니다.")
        );
        return MemberSettingResponseDTO.from(member);
    }

    @Transactional
    public void updateUserSetting(@Valid final MemberSettingRequestDTO requestDTO, final Long id, final MultipartFile thumbnail) {
        final Member member = memberRepository.findById(id).orElseThrow(
                () -> new Exception400("회원을 찾을 수 없습니다.")
        );
        final String encodePassword = passwordEncoder.encode(requestDTO.getModifyPassword());
        final String img = uploadImage(thumbnail);
        member.updateUserSetting(requestDTO.getNickname(), encodePassword, requestDTO.getPhoneNumber(), img);
        try{
            memberRepository.save(member);
        } catch (final RuntimeException e) {
            throw new Exception500("회원 정보 수정에 실패했습니다.");
        }
    }

    @Transactional
    public void cancellationUser(final Long userId) {
        final Member member = memberRepository.findById(userId).orElseThrow(
                () -> new Exception400("회원을 찾을 수 없습니다.")
        );
        try{
            memberRepository.delete(member);
        } catch (final RuntimeException e) {
            throw new Exception500("회원 탈퇴에 실패했습니다.");
        }
    }
    private String uploadImage(final MultipartFile img){
        return awss3Uploader.uploadToS3(img);
    }
}