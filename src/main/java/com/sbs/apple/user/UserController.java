package com.sbs.apple.user;


import com.sbs.apple.board.BoardForm;
import com.sbs.apple.chat.ChatRoom;
import com.sbs.apple.chat.ChatRoomService;
import com.sbs.apple.cybermoney.CyberMoneyServiceImpl;
import com.sbs.apple.cybermoney.CyberMoneyTransaction;
import com.sbs.apple.cybermoney.CyberMoneyTransactionRepository;
import com.sbs.apple.exchange.ExchangeRepository;
import com.sbs.apple.exchange.ExchangeService;
import com.sbs.apple.interest.InterestService;
import com.sbs.apple.report.ReportForm;
import com.sbs.apple.report.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


//회원가입
@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {


    private final UserService userService;
    private final ReportService reportService;
    private final UserRepository userRepository;
    private final InterestService interestService;
    private final CyberMoneyTransactionRepository cyberMoneyTransactionRepository;
    private final ChatRoomService chatRoomService;
    private final ExchangeService exchangeService;
    private final ExchangeRepository exchangeRepository;
    private final CyberMoneyServiceImpl cyberMoneyService;


    @GetMapping("/signup")
    public String signup1(UserCreateForm userCreateForm) {
        return "user/signup_form";
    }

    @PostMapping("/signup")
    public String signup2(@Valid UserCreateForm userCreateForm, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes, Model model, MultipartFile file)
            throws Exception {
        if (bindingResult.hasErrors()) {
            return "user/signup_form";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "user/signup_form";
        }
        SiteUser user = userService.create(false, false, userCreateForm.getFile(), userCreateForm.getUsername(), userCreateForm.getPassword1(),
                userCreateForm.getNickname(), userCreateForm.getGender());
        redirectAttributes.addAttribute("id", user.getId());
        return "redirect:/user/add/" + user.getId();
    }


    @GetMapping("/add/{id}")
    public String add1(UserAddForm userAddForm, @PathVariable("id") Integer id, Model model) {
        model.addAttribute("userId", id);
        return "user/add_form";
    }

    @PostMapping("/add/{id}")
    public String add2(@PathVariable("id") Integer id, UserAddForm userAddForm, RedirectAttributes redirectAttributes) {
        SiteUser user = this.userService.getUser(id);
        userService.add_profile(user, userAddForm.getAge(), userAddForm.getLiving(), userAddForm.getHobbyList(),
                userAddForm.getTall(), userAddForm.getBody_type(), userAddForm.getSmoking(),
                userAddForm.getDrinking(), userAddForm.getStyleList(), userAddForm.getReligion(),
                userAddForm.getMbti(), userAddForm.getSchool(), userAddForm.getJob(), userAddForm.getAbout_Me());
        redirectAttributes.addAttribute("id", user.getId());
        return "redirect:/user/desired/" + user.getId();
    }

    //원하는 이상형 작성
    @GetMapping("/desired/{id}")
    public String desired1(UserDesiredForm userDesiredForm, @PathVariable("id") Integer id, Model model) {
        model.addAttribute("userId", id);
        return "user/desired_form";
    }

    @PostMapping("/desired/{id}")
    public String desired2(@PathVariable("id") Integer id, UserDesiredForm userDesiredForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/desired_form";
        }
        SiteUser user = this.userService.getUser(id);
        userService.add_desired(user, userDesiredForm.getDesired_age(), userDesiredForm.getDesired_living(),
                userDesiredForm.getDesired_tall(), userDesiredForm.getDesired_body_type(),
                userDesiredForm.getDesired_smoking(), userDesiredForm.getDesired_drinking(),
                userDesiredForm.getDesired_styleList(), userDesiredForm.getDesired_religion(),
                userDesiredForm.getDesired_mbti(), userDesiredForm.getDesired_school(),
                userDesiredForm.getDesired_job());
        return "redirect:/";
    }

    //로그인
    @GetMapping("/login")
    public String login(Model model) {
        return "user/login_form";
    }

    //마이 페이지
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myPage")
    public String userMyPage(Model model, Principal principal) {
        String username = principal.getName();
        SiteUser user = userService.getUserbyName(username);

        int userCyberMoney = user.getCyberMoney();
        int receivedCyberMoney = user.getReceivedCyberMoney(); // 다른 사용자로부터 받은 사이버머니

        model.addAttribute("userCyberMoney", userCyberMoney);
        model.addAttribute("receivedCyberMoney", receivedCyberMoney); // 다른 사용자로부터 받은 사이버머니
        model.addAttribute("user", user);

        return "myPage";
    }

    //비밀번호 변경
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/passwordChange")
    public String passwordChange(Model model) {
        return "user/passwordChange";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/passwordChange")
    public String passwordChange(@RequestParam("password") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Model model, Principal principal) {
        String username = principal.getName();
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "새로운 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
            return "user/passwordChange";
        }

        if (!userService.isCorrectPassword(username, currentPassword)) {
            model.addAttribute("error", "기존 비밀번호가 올바르지 않습니다.");
            return "user/passwordChange";
        }
        userService.updatePassword(username, newPassword);
        return "redirect:/user/myPage";
    }

    //탈퇴 페이지
    @PostMapping("/checkLoginPw")
    public ResponseEntity<String> checkLoginPw(Principal principal, @RequestParam("userPassword") String userPassword) {
        SiteUser siteUser = this.userService.getUserbyName(principal.getName());

        if (BCrypt.checkpw(userPassword, siteUser.getPassword())) {
            return ResponseEntity.ok("");
        } else {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete")
    public String mypage_exit(Principal principal, Model model) {
        SiteUser siteUser = this.userService.getUserbyName(principal.getName());
        model.addAttribute("siteUser", siteUser);
        return "user/delete";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete")
    public String userDelete(HttpServletRequest request, @RequestParam("password") String password,
                             HttpServletResponse response, Principal principal, RedirectAttributes attributes) {
        SiteUser siteUser = this.userService.getUserbyName(principal.getName());

        if (BCrypt.checkpw(password, siteUser.getPassword())) {
            this.userService.delete(siteUser);

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }

            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            return "redirect:/"; // 메인 페이지로 리다이렉트
        } else {
            // 비밀번호가 일치하지 않을 때 오류 메시지를 전달하고 회원 탈퇴 페이지로 리다이렉트합니다.
            attributes.addFlashAttribute("error", "비밀번호가 일치하지 않습니다. 다시 시도해주세요.");
            return "redirect:/user/delete";
        }
    }

    //프로필 수정
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile_modify")
    public String profile_modify1(UserAddForm userAddForm, Principal principal, Model model) {
        SiteUser siteUser = this.userService.getUserbyName(principal.getName());
        userAddForm.setAge(siteUser.getAge());
        userAddForm.setLiving(siteUser.getLiving());
        userAddForm.setHobbyList(siteUser.getHobbyList());
        userAddForm.setTall(siteUser.getTall());
        userAddForm.setBody_type(siteUser.getBody_type());
        userAddForm.setSmoking(siteUser.getSmoking());
        userAddForm.setDrinking(siteUser.getDrinking());
        userAddForm.setStyleList(siteUser.getStyleList());
        userAddForm.setReligion(siteUser.getReligion());
        userAddForm.setMbti(siteUser.getMbti());
        userAddForm.setSchool(siteUser.getSchool());
        userAddForm.setJob(siteUser.getJob());
        model.addAttribute("siteUser", siteUser);
        return "user/profile_modify";
    }

    @PostMapping("/profile_modify")
    public String profile_modify(UserAddForm userAddForm, Principal principal) {
        SiteUser user = this.userService.getUserbyName(principal.getName());
        userService.add_profile(user, userAddForm.getAge(), userAddForm.getLiving(), userAddForm.getHobbyList(),
                userAddForm.getTall(), userAddForm.getBody_type(), userAddForm.getSmoking(),
                userAddForm.getDrinking(), userAddForm.getStyleList(), userAddForm.getReligion(),
                userAddForm.getMbti(), userAddForm.getSchool(), userAddForm.getJob(), userAddForm.getAbout_Me());
        return "redirect:/user/myPage";
    }

    //이상형 수정
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/desired_modify")
    public String desired_modify1(UserDesiredForm userDesiredForm, Principal principal, Model model) {
        SiteUser siteUser = this.userService.getUserbyName(principal.getName());
        userDesiredForm.setDesired_age(siteUser.getDesired_age());
        userDesiredForm.setDesired_living(siteUser.getDesired_living());
        userDesiredForm.setDesired_tall(siteUser.getDesired_tall());
        userDesiredForm.setDesired_body_type(siteUser.getDesired_body_type());
        userDesiredForm.setDesired_smoking(siteUser.getDesired_smoking());
        userDesiredForm.setDesired_drinking(siteUser.getDesired_drinking());
        userDesiredForm.setDesired_styleList(siteUser.getDesired_styleList());
        userDesiredForm.setDesired_religion(siteUser.getDesired_religion());
        userDesiredForm.setDesired_mbti(siteUser.getDesired_mbti());
        userDesiredForm.setDesired_school(siteUser.getDesired_school());
        userDesiredForm.setDesired_job(siteUser.getDesired_job());
        model.addAttribute("siteUser", siteUser);
        return "user/desired_modify";
    }

    @PostMapping("/desired_modify")
    public String desired_modify(UserDesiredForm userDesiredForm, Principal principal) {
        SiteUser user = this.userService.getUserbyName(principal.getName());
        userService.add_desired(user, userDesiredForm.getDesired_age(), userDesiredForm.getDesired_living(),
                userDesiredForm.getDesired_tall(),
                userDesiredForm.getDesired_body_type(), userDesiredForm.getDesired_smoking(),
                userDesiredForm.getDesired_drinking(), userDesiredForm.getDesired_styleList(),
                userDesiredForm.getDesired_religion(), userDesiredForm.getDesired_mbti(),
                userDesiredForm.getDesired_school(), userDesiredForm.getDesired_job());
        return "redirect:/user/myPage";
    }

    //조회하기
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/detail/{id}")
    public String paymentPage(Principal principal, Model model, @PathVariable("id") Integer id) {
        SiteUser siteUser = this.userService.getUserbyName(principal.getName());
        SiteUser receivedSiteUser = this.userService.getUser(id);
        model.addAttribute("siteUser", siteUser);
        model.addAttribute("receivedSiteUser", receivedSiteUser);
        String interest_user = principal.getName();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SiteUser user = userService.getUserbyName(username);


        int userCyberMoney = user.getCyberMoney();
        int receivedCyberMoney = user.getReceivedCyberMoney(); // 다른 사용자로부터 받은 사이버머니
        boolean isInterested = interestService.isInterested(siteUser,receivedSiteUser);
        model.addAttribute("isInterested", isInterested);
        SiteUser loginUser = userService.getUserbyName(principal.getName());

        //로그인한 사용자와 현재 프로필 선택한 유저 사이의 채팅방과 사이버 머니 전송 기록을 찾는 코드
        ChatRoom chatRoom = chatRoomService.findRoomByUserIdAndUserId2( loginUser.getId(),siteUser.getId());
        CyberMoneyTransaction log =cyberMoneyTransactionRepository.findByUserIdAndUserId2(loginUser.getId(),siteUser.getId());
        //현재 그유저와 채팅방이 있으면 채팅방 만들기 버튼은 아예 안만들 생각
        model.addAttribute("chatRoom", chatRoom);
        model.addAttribute("log", log);
        model.addAttribute("userCyberMoney", userCyberMoney);
        model.addAttribute("receivedCyberMoney", receivedCyberMoney); // 다른 사용자로부터 받은 사이버머니


        return "user/profile";
    }

    //신고하기
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/report/{id}")
    public String report(ReportForm reportForm, @PathVariable("id") Integer id, Model model) {
        model.addAttribute("userId", id);
        return "user/report";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/report/{id}")
    public String reportCreate(@PathVariable("id") Integer id, ReportForm reportForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/report";
        }
        SiteUser siteUser = userService.getUser(id);
        this.reportService.create(siteUser, reportForm.getSubject(), reportForm.getContent());
        return "redirect:/";
    }

    //admin 계정 부여하기
    @GetMapping("/grantAuthorityToAdmin")
    public String grantAuthorityForm(Principal principal) {

        return "admin/grantAuthorityForm";
    }

    @PostMapping("/grantAuthorityToAdmin")
    public String grantAdminAuthority(@RequestParam String adminCode, Principal principal) {
        if ("admin".equals(adminCode)) {
            String username = principal.getName();
            userService.grantAdminAuthority(username);
        }
        return "redirect:/";
    }

    //경고 확인하기
    @GetMapping("/okay")
    public String ok(Principal principal) {
        String username = principal.getName();
        SiteUser siteUser = this.userService.getUserbyName(username);
        userService.resetUserWarning(siteUser);
        return "redirect:/";
    }

    //사진 수정하기
    @GetMapping("/photoModify/{id}")
    public String photoModify(UserCreateForm userCreateForm, MultipartFile file, Model model, Principal principal, @PathVariable Integer id) {
        SiteUser siteUser = userService.getUser(id);
        model.addAttribute("siteUser", siteUser);

        return "user/userPhoto_modify";

    }

    @PostMapping("/photoModify/{id}")
    public String photoModify2(@Valid BoardForm boardForm, MultipartFile file, Model model, Principal principal,
                               @PathVariable Integer id) throws Exception {
        SiteUser user = userService.getUserbyName(principal.getName());
//        userService.photoModify(user, boardForm.getFile());
        return "redirect:/user/myPage";
    }

    @GetMapping("/transactions")
    public String getTransactionHistory(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SiteUser user = userService.getUserbyName(username);

        int userCyberMoney = user.getCyberMoney();
        int receivedCyberMoney = user.getReceivedCyberMoney(); // 다른 사용자로부터 받은 사이버머니

        List<CyberMoneyTransaction> receivedTransactions = cyberMoneyTransactionRepository.findByRecipientUser(user);
        List<CyberMoneyTransaction> sentTransactions = cyberMoneyTransactionRepository.findBySenderUser(user);

        // 완료된 거래 목록을 생성
        List<CyberMoneyTransaction> completedTransactions = new ArrayList<>();
        for (CyberMoneyTransaction transaction : receivedTransactions) {
            if (transaction.isAccepted() || transaction.isRejected()) {
                completedTransactions.add(transaction);
            }
        }

        // receivedTransactions에서 완료된 거래를 제거하고 completedTransactions에 추가
        receivedTransactions.removeIf(transaction -> transaction.isAccepted() || transaction.isRejected());
        completedTransactions.addAll(receivedTransactions);

        // 여기에서 sentTransactions에서도 완료된 거래를 필터링하고 추가하는 로직을 추가할 수 있습니다.
        for (CyberMoneyTransaction transaction : sentTransactions) {
            if (transaction.isAccepted() || transaction.isRejected()) {
                completedTransactions.add(transaction);
            }
        }

        int minHeart = user.getMinHeart();
        model.addAttribute("minHeart", minHeart);

        // 모델에 데이터를 추가하여 뷰로 전달
        model.addAttribute("receivedTransactions", receivedTransactions); // 받은 거래 정보
        model.addAttribute("sentTransactions", sentTransactions); // 보낸 거래 정보
        model.addAttribute("userCyberMoney", userCyberMoney);
        model.addAttribute("receivedCyberMoney", receivedCyberMoney); // 다른 사용자로부터 받은 사이버머니
        model.addAttribute("completedTransactions", completedTransactions); // 완료된 거래 정보

        return "transactions"; // 템플릿 이름 (예: transaction-history.html)
    }

    @PostMapping("/updateMinHeart")
    public String updateMinHeart(@RequestParam("minHeart") Integer minHeart, Principal principal) {
        try {
            String username = principal.getName();
            userService.updateMinHeart(username, minHeart);
            return "redirect:/user/transactions"; // 최신 정보를 반영하도록 리다이렉트
        } catch (Exception e) {
            // 예외 처리 로직 추가
            return "error"; // 예외 발생 시 에러 페이지로 이동하거나 다른 적절한 처리를 수행할 수 있습니다.
        }
    }


    @PostMapping("/processTransaction")
    public String processTransaction(
            @RequestParam("transactionId") Long transactionId,
            @RequestParam("action") String action

    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<SiteUser> recipientUserOptional = userRepository.findByUsername(username);
        if (!recipientUserOptional.isPresent()) {
            return "redirect:/error?message=받는 사용자를 찾을 수 없습니다.";
        }
        SiteUser recipientUser = recipientUserOptional.get();

        Optional<CyberMoneyTransaction> transactionOptional = cyberMoneyTransactionRepository.findById(transactionId);
        if (!transactionOptional.isPresent()) {
            return "redirect:/error?message=거래를 찾을 수 없습니다.";
        }
        CyberMoneyTransaction transaction = transactionOptional.get();

        if ("accept".equals(action) && !transaction.isAccepted() && !transaction.isRejected()) {
            // 거래가 아직 수락되지 않았을 경우에만 처리
            transaction.setAccepted(true);
            cyberMoneyTransactionRepository.save(transaction);

            recipientUser.setReceivedCyberMoney(recipientUser.getReceivedCyberMoney() + transaction.getAmount());
            userRepository.save(recipientUser);
        } else if ("reject".equals(action) && !transaction.isAccepted() && !transaction.isRejected()) {
            // 거래가 아직 수락되지 않았고 거부되지 않았을 경우에만 처리
            transaction.setRejected(true); // 거부 플래그 설정

            // 보낸 사람에게 사이버머니를 반환하는 로직 구현
            SiteUser senderUser = transaction.getSenderUser();
            senderUser.setCyberMoney(senderUser.getCyberMoney() + transaction.getAmount());
            userRepository.save(senderUser);
            return "redirect:/user/transactions";
        } else {
            return "redirect:/error?message=이미 수락 또는 거부된 거래입니다.";
        }

        // 아래의 코드로 거래를 receivedTransactions에서 삭제하고 completedTransactions로 이동
        recipientUser.getReceivedTransactions().remove(transaction);
        recipientUser.getCompletedTransactions().add(transaction);
        userRepository.save(recipientUser);

        //채팅방 만드는건 여기서부터 구현

        ChatRoom chatRoom = chatRoomService.findLastRoom();
        //만들어진 채팅 방이 없을시 기본값 채팅방 번호 1 부여
        int roomId = 1;

        if (chatRoom != null) {
            roomId = chatRoom.getId() + 1;
        }

        //사이버 머니 받은 사용자의 id 즉 채팅방 초대받는 유저 인덱스번호
        Integer toUserId= transaction.getSenderUser().getId();



        return "redirect:/chat/" + roomId + "/room/" + toUserId; // 거래 내역 페이지로 리다이렉트
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/interest")
    public String interest_all(Principal principal ,Model model) {
        SiteUser siteUser = this.userService.getUserbyName(principal.getName());
        model.addAttribute("siteUser", siteUser);
        return "user/interest_all";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/heart")
    public String heart(Principal principal ,Model model) {
        SiteUser siteUser = this.userService.getUserbyName(principal.getName());
        model.addAttribute("siteUser", siteUser);
        return "user/interest_all";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/setting")
    public String setting(Principal principal ,Model model) {
        SiteUser siteUser = this.userService.getUserbyName(principal.getName());
        model.addAttribute("siteUser", siteUser);
        return "user/setting";
    }
}
