package com.theocean.fundering;

import com.theocean.fundering.domain.account.repository.AccountRepository;
import com.theocean.fundering.domain.celebrity.repository.CelebRepository;
import com.theocean.fundering.domain.celebrity.repository.FollowRepository;
import com.theocean.fundering.domain.comment.repository.CommentRepository;
import com.theocean.fundering.domain.evidence.repository.EvidenceRepository;
import com.theocean.fundering.domain.heart.repository.HeartRepository;
import com.theocean.fundering.domain.member.repository.AdminRepository;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.domain.news.repository.NewsRepository;
import com.theocean.fundering.domain.payment.repository.PaymentRepository;
import com.theocean.fundering.domain.post.repository.PostRepository;
import com.theocean.fundering.domain.withdrawal.repository.WithdrawalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class InitialDataInjection {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Bean
    public CommandLineRunner initCeleb(final CelebRepository celebRepository) {
        return args -> {
            if (0 == celebRepository.count()) {
                final String query1 =
                        "INSERT INTO celebrity (follower_count, created_at, modified_at, name, celeb_group, approval_status, category, gender, profile_image) VALUES (2, '2023-09-30T12:00:00', CURRENT_TIMESTAMP, '손흥민', '토트넘 홋스퍼', 'PENDING', 'SPORT', 'MALE', 'profile1.jpg')";
                final String query2 =
                        "INSERT INTO celebrity (follower_count, created_at, modified_at, name, celeb_group, approval_status, category, gender, profile_image) VALUES (1, '2023-09-30T12:00:00', CURRENT_TIMESTAMP, '태연', '소녀시대', 'APPROVED', 'SINGER', 'FEMALE', 'profile2.jpg')";

                jdbcTemplate.update(query1);
                jdbcTemplate.update(query2);
            }
        };
    }

    @Bean
    public CommandLineRunner initMember(final MemberRepository memberRepository) {
        return args -> {
            if (0 == memberRepository.count()) {
                final String query1 =
                        "INSERT INTO member (created_at, modified_at, phone_number, nickname, email, password, profile_image, refresh_token, user_role) VALUES (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '01012341234', '1번유저', 'test1@naver.com', 'test1234!', '프로필_URL_1', NULL, 'USER')";
                final String query2 =
                        "INSERT INTO member (created_at, modified_at, phone_number, nickname, email, password, profile_image, refresh_token, user_role) VALUES (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '01043211234', '2번유저', 'test2@naver.com', 'test1234@', '프로필_URL_2', NULL, 'USER')";

                jdbcTemplate.update(query1);
                jdbcTemplate.update(query2);
            }
        };
    }

    @Bean
    public CommandLineRunner initFollow(final FollowRepository followRepository) {
        return args -> {
            if (0 == followRepository.count()) {
                final String query1 =
                        "INSERT INTO follow (celebrity_id, member_id) VALUES (1, 1)";
                final String query2 =
                        "INSERT INTO follow (celebrity_id, member_id) VALUES (1, 2)";
                final String query3 =
                        "INSERT INTO follow (celebrity_id, member_id) VALUES (2, 2)";

                jdbcTemplate.update(query1);
                jdbcTemplate.update(query2);
                jdbcTemplate.update(query3);
            }
        };
    }

    @Bean
    public CommandLineRunner initAccount(final AccountRepository accountRepository, final JdbcTemplate jdbcTemplate) {
        return args -> {
            if (0 == accountRepository.count()) {
                final String query1 = "INSERT INTO account (balance, manager_id, post_id) VALUES (50000000, 1, 1)";
                final String query2 = "INSERT INTO account (balance, manager_id, post_id) VALUES (10000000, 1, 2)";

                jdbcTemplate.update(query1);
                jdbcTemplate.update(query2);
            }
        };
    }

    @Bean
    public CommandLineRunner initPost(final PostRepository postRepository, final JdbcTemplate jdbcTemplate) {
        return args -> {
            if (0 == postRepository.count()) {
                final String query1 = "INSERT INTO post (heart_count, participants, target_price, account_id, celeb_id, created_at, deadline, modified_at, writer_id, title, introduction, post_status, thumbnail) VALUES (1, 1, 7000000, 1, 1, CURRENT_TIMESTAMP, '2023-11-11T12:00:00', CURRENT_TIMESTAMP, 1, '손흥민 100호골 기념 펀딩', '소개글 1', 'ONGOING', '썸네일1')";
                final String query2 = "INSERT INTO post (heart_count, participants, target_price, account_id, celeb_id, created_at, deadline, modified_at, writer_id, title, introduction, post_status, thumbnail) VALUES (2, 1, 10000000, 2, 2, CURRENT_TIMESTAMP, '2023-11-25T12:00:00', CURRENT_TIMESTAMP, 2, '태연 앨범 발매 기념 펀딩', '소개글 2', 'ONGOING', '썸네일2')";

                jdbcTemplate.update(query1);
                jdbcTemplate.update(query2);
            }
        };
    }

    @Bean
    public CommandLineRunner initHeart(final HeartRepository heartRepository, final JdbcTemplate jdbcTemplate) {
        return args -> {
            if (0 == heartRepository.count()) {
                final String query1 = "INSERT INTO heart (member_id, post_id) VALUES (1, 1)";
                final String query2 = "INSERT INTO heart (member_id, post_id) VALUES (1, 2)";
                final String query3 = "INSERT INTO heart (member_id, post_id) VALUES (2, 2)";

                jdbcTemplate.update(query1);
                jdbcTemplate.update(query2);
                jdbcTemplate.update(query3);
            }
        };
    }

    @Bean
    public CommandLineRunner initNews(final NewsRepository newsRepository, final JdbcTemplate jdbcTemplate) {
        return args -> {
            if (0 == newsRepository.count()) {
                final String query1 = "INSERT INTO news (created_at, modified_at, post_id, writer_id, title, content) VALUES (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, '손흥민 EPL100호골 광고 강남역에서 진행합니다!', '작성자:1번 유저, 게시글 1의 펀딩 업데이트 내용')";
                final String query2 = "INSERT INTO news (created_at, modified_at, post_id, writer_id, title, content) VALUES (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 2, '태연 앨범발매 기념 펀딩 목표액 달성했습니다~', '작성자:2번 유저, 게시글 2의 펀딩 업데이트 내용')";

                jdbcTemplate.update(query1);
                jdbcTemplate.update(query2);
            }
        };
    }

    @Bean
    public CommandLineRunner initComment(final CommentRepository commentRepository, final JdbcTemplate jdbcTemplate) {
        return args -> {
            if (0 == commentRepository.count()) {
                final String query1 = "INSERT INTO comment (is_deleted, created_at, modified_at, post_id, writer_id, comment_order, content) VALUES (false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 2, '1', '1번 게시글의 첫번째 댓글입니다, 작성자PK: 2')";
                final String query2 = "INSERT INTO comment (is_deleted, created_at, modified_at, post_id, writer_id, comment_order, content) VALUES (false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 1, '1', '2번 게시글의 첫번째 댓글입니다, 작성자PK: 1')";
                final String query3 = "INSERT INTO comment (is_deleted, created_at, modified_at, post_id, writer_id, comment_order, content) VALUES (false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, '1.1', '1번 게시글의 첫번째 댓글에 대한 첫번째 대댓글입니다, 작성자PK: 1')";
                final String query4 = "INSERT INTO comment (is_deleted, created_at, modified_at, post_id, writer_id, comment_order, content) VALUES (false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 2, '1.1', '2번 게시글의 첫번째 댓글에 대한 첫번째 대댓글입니다, 작성자PK: 2')";

                jdbcTemplate.update(query1);
                jdbcTemplate.update(query2);
                jdbcTemplate.update(query3);
                jdbcTemplate.update(query4);
            }
        };
    }

    @Bean
    public CommandLineRunner initPayment(final PaymentRepository paymentRepository, final JdbcTemplate jdbcTemplate) {
        return args -> {
            if (0 == paymentRepository.count()) {
                final String query1 = "INSERT INTO payment (amount, created_at, member_id, modified_at, post_id, imp_uid) VALUES  (500000, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1, '아엠포트_UID')";
                final String query2 = "INSERT INTO payment (amount, created_at, member_id, modified_at, post_id, imp_uid) VALUES (1000000, CURRENT_TIMESTAMP, 2, CURRENT_TIMESTAMP, 2, '아엠포트_UID')";

                jdbcTemplate.update(query1);
                jdbcTemplate.update(query2);
            }
        };
    }

    @Bean
    public CommandLineRunner initWithdrawal(final WithdrawalRepository withdrawalRepository, final JdbcTemplate jdbcTemplate) {
        return args -> {
            if (0 == withdrawalRepository.count()) {
                final String query1 = "INSERT INTO withdrawal (approval_status, balance, withdrawal_amount, applicant_id, created_at, modified_at, post_id, deposit_account, purpose) VALUES (1, 50000000,100000, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, '356-0000-0000-0000', '사리사욕')";
                final String query2 = "INSERT INTO withdrawal (approval_status, balance, withdrawal_amount, applicant_id, created_at, modified_at, post_id, deposit_account, purpose) VALUES (0, null, 1000000, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, '356-1111-1111-1111', '여러가지')";

                jdbcTemplate.update(query1);
                jdbcTemplate.update(query2);
            }
        };
    }

    @Bean
    public CommandLineRunner initEvidence(final EvidenceRepository evidenceRepository, final JdbcTemplate jdbcTemplate) {
        return args -> {
            if (0 == evidenceRepository.count()) {
                final String query1 = "INSERT INTO evidence (applicant_id, created_at, modified_at, post_id, withdrawal_id, url) VALUES (1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, '증빙 자료url')";

                jdbcTemplate.update(query1);
            }
        };
    }

    @Bean
    public CommandLineRunner initAdmin(final AdminRepository adminRepository, final JdbcTemplate jdbcTemplate) {
        return args -> {
            if (0 == adminRepository.count()) {
                final String query1 = "INSERT INTO admin(member_id, post_id) VALUES (1, 1)";
                final String query2 = "INSERT INTO admin(member_id, post_id) VALUES (1, 2)";

                jdbcTemplate.update(query1);
                jdbcTemplate.update(query2);
            }
        };
    }
}
