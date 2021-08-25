package com.moviePicker.api.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.moviePicker.api.auth.dto.PasswordResetRequest;
import com.moviePicker.api.auth.exception.ExpiredTokenException;
import com.moviePicker.api.auth.exception.UnauthenticatedException;
import com.moviePicker.api.auth.exception.UnauthorizedException;
import com.moviePicker.api.auth.exception.WrongTokenException;
import com.moviePicker.api.comment.domain.Comment;
import com.moviePicker.api.common.domain.TimestampEntity;
import com.moviePicker.api.member.dto.MemberUpdateRequest;
import com.moviePicker.api.movie.domain.MovieWatched;
import com.moviePicker.api.movie.domain.MovieWished;
import com.moviePicker.api.review.domain.Recommendation;
import com.moviePicker.api.review.domain.Report;
import com.moviePicker.api.review.domain.Review;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity(name = "members")
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Member extends TimestampEntity implements UserDetails {
    @Id
    @Column(name = "id", length = 40)
    private String UUID;

    @Column(name = "email", unique = true, nullable = false, columnDefinition = "varchar(40)")
    private String email;

    @Column(name = "nickname", unique = true, nullable = false, columnDefinition = "varchar(40)")
    private String nickname;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "password_reset_token", columnDefinition = "varchar(255)")
    private String passwordResetToken;

    @Column(name = "password_reset_token_valid_until")
    private LocalDateTime passwordResetTokenValidUntil;

    @Column(name = "authorization_token", columnDefinition = "varchar(255)")
    private String authorizationToken;

    @Column(name = "authorization_token_valid_until")
    private LocalDateTime authorizationTokenValidUntil;

    @Column(name = "report_count")
    private Integer reportCount = 0;

    @OneToMany(mappedBy = "member")
    List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    List<Recommendation> recommendationList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    List<Report> reportList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    List<MovieWatched> movieWatchedList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    List<MovieWished> movieWishedList = new ArrayList<>();

    @Embedded
    private MemberRoles roles;

    protected Member() {
    }


    @Builder
    public Member(String UUID, String email, String nickname, String passwordHash, String passwordResetToken, LocalDateTime passwordResetTokenValidUntil, String authorizationToken, LocalDateTime authorizationTokenValidUntil, int reportCount, MemberRoles roles) {
        this.UUID = UUID;
        this.email = email;
        this.nickname = nickname;
        this.passwordHash = passwordHash;
        this.passwordResetToken = passwordResetToken;
        this.passwordResetTokenValidUntil = passwordResetTokenValidUntil;
        this.authorizationToken = authorizationToken;
        this.authorizationTokenValidUntil = authorizationTokenValidUntil;
        this.reportCount = reportCount;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.getAuthorities();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public boolean isEnabled() {
        return roles.isAcceptedMember();
    }

    @Override
    public boolean isAccountNonExpired() {
        return roles.isExpelled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return getUUID().equals(member.getUUID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUUID());
    }


    public void updateAuthorizationToken(String authorizationToken) {
        this.authorizationToken = authorizationToken;
        this.authorizationTokenValidUntil = LocalDateTime.now().plusDays(1L);
    }

    public void checkAuthorizationTokenAndChangeMemberRole(String authorizationToken) {
        checkAuthorizationToken(authorizationToken);
        changeMember();
    }

    public void updatePasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
        this.passwordResetTokenValidUntil = LocalDateTime.now().plusDays(1L);
    }

    public void checkPasswordResetTokenAndUpdatePassword(String passwordResetToken, PasswordResetRequest request) {
        checkPasswordResetToken(passwordResetToken);
        updatePassword(request);
    }

    public void checkAuthorized() {
        if (this.isEnabled())
            throw new UnauthorizedException("이미 인증된 회원입니다.");
    }

    public void loginAndCheckExpelled() {
        if (this.isAccountNonExpired()) {
            throw new UnauthenticatedException("추방된 회원입니다.");
        }
    }

    public void updateMemberInfo(MemberUpdateRequest request, String passwordHash) {
        this.passwordHash = passwordHash;
        this.nickname = request.getNickname();
    }

    public void withdraw() {
        this.getRoles().changeRole(MemberRole.WITHDRAWAL);
    }

    public void checkNicknameMatches(String nickname) {
        if (!this.nickname.equals(nickname)) {
            throw new UnauthorizedException("잘못된 요청(닉네임불일치)입니다.");
        }
    }

    private void checkAuthorizationToken(String authorizationToken) {
        checkTokenExpired(this.authorizationTokenValidUntil);
        checkTokenCorrect(this.authorizationToken, authorizationToken);
    }

    private void checkPasswordResetToken(String passwordResetToken) {
        checkTokenExpired(this.passwordResetTokenValidUntil);
        checkTokenCorrect(this.passwordResetToken, passwordResetToken);
    }

    private void checkTokenExpired(LocalDateTime memberTokenValidUntil) {
        if (!memberTokenValidUntil.isAfter(LocalDateTime.now()))
            throw new ExpiredTokenException("토큰이 만료되었습니다.");
    }

    private void checkTokenCorrect(String memberToken, String inputToken) {
        if (!memberToken.equals(inputToken))
            throw new WrongTokenException("인증 토큰이 틀렸습니다.");
    }

    private void changeMember() {
        this.roles.changeRole(MemberRole.MEMBER);
        this.authorizationToken = null;
        this.authorizationTokenValidUntil = null;
    }

    private void updatePassword(PasswordResetRequest request) {
        this.passwordHash = request.getPassword();
    }


}
