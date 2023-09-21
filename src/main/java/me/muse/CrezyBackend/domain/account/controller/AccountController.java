package me.muse.CrezyBackend.domain.account.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.account.controller.form.AccountInfoResponseForm;
import me.muse.CrezyBackend.domain.account.controller.form.AccountLoginRequestForm;
import me.muse.CrezyBackend.domain.account.controller.form.AccountLoginResponseForm;
import me.muse.CrezyBackend.domain.account.controller.form.AccountWarningCountsResponseForm;
import me.muse.CrezyBackend.domain.account.service.AccountService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {
    final private AccountService accountService;

    @GetMapping("/logout") // 로그아웃
    public void logout(@RequestParam("userToken") String userToken) {

        accountService.logout(userToken);
    }

    @GetMapping("/check-nickName/{nickname}") // 닉네임 중복 체크
    public boolean checkNickname(@PathVariable("nickname") String nickname) {
        return accountService.checkNickname(nickname);
    }

    @GetMapping("/change-nickname") // 닉네임 변경
    public String changeNickname(@RequestParam("userToken") String userToken, @RequestParam("nickname") String nickname) {
        return accountService.changeNickname(userToken, nickname);
    }

    @DeleteMapping("/withdraw") // 회원 탈퇴
    public Boolean withdrawal(@RequestHeader HttpHeaders headers){
        log.info("withdrawal()");
        return accountService.withdrawal(headers);
    }

    @GetMapping("/info") //마이페이지 - 회원 정보 닉네임 & 내가 등록한 플레이 리스트 개수 & 좋아요 한 플레이 리스트 개수 리턴
    public AccountInfoResponseForm returnAccount(@RequestHeader HttpHeaders headers) {
        log.info("returnAccount()");
        return accountService.returnAccountInfo(headers);
    }

    @GetMapping("/change-profileImage") // 프로필 이미지 변경
    public String changeProfileImage(@RequestHeader HttpHeaders headers, @RequestParam("profileImageName") String profileImageName) {
        log.info("changeProfileImage()");
        return accountService.changeProfileImage(headers, profileImageName);
    }

    @PostMapping("/login-admin")
    public AccountLoginResponseForm memberLogin(@RequestBody AccountLoginRequestForm accountLoginRequestForm) {
        return accountService.login(accountLoginRequestForm);
    }

    @GetMapping("/warning-count")
    public AccountWarningCountsResponseForm warningCounts(@RequestHeader HttpHeaders headers) {
        log.info("warningCount");
        return accountService.warningCounts(headers);
    }
}
