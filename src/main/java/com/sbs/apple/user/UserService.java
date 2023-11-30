package com.sbs.apple.user;

import com.sbs.apple.DataNotFoundException;
import com.sbs.apple.RsData;
import com.sbs.apple.chat.ChatRoom;
import com.sbs.apple.chat.ChatRoomService;
import com.sbs.apple.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ChatRoomService chatRoomService;
    private String uploadDir;
    private final EmailService emailService;

    Random random = new Random();


    @Value("${file.upload-dir}")
    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public SiteUser getUser(Integer id) {
        Optional<SiteUser> user = this.userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new DataNotFoundException("user not found");
        }
    }

    public SiteUser getUserbyName(String name) {
        Optional<SiteUser> user = this.userRepository.findByUsername(name);
        if (user.isPresent()) {
            return user.get();
        } else {

            return null;
        }
    }

    public SiteUser createUser(String username, String gender, List<String> hobbyList, List<String> styleList, List<String> desiredStyleList) {
        SiteUser user = new SiteUser();
        user.setUserWarning(false);
        user.setUserStop(false);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(username));
        user.setNickname(username);
        user.setGender(gender);
        user.setAge(random.nextInt(30));
        user.setLiving("서울");
        user.setHobbyList(hobbyList);
        user.setTall(random.nextInt(170));
        user.setBody_type("평범한");
        user.setSmoking("비흡연");
        user.setDrinking("가끔");
        user.setStyleList(styleList);
        user.setReligion("무교");
        user.setMbti("INFP");
        user.setSchool("4년제 졸업");
        user.setJob("무직");
        user.setAbout_Me("반갑소");
        user.setDesired_living("서울");
        user.setDesired_body_type("평범한");
        user.setDesired_smoking("비흡연");
        user.setDesired_drinking("가끔");
        user.setDesired_styleList(desiredStyleList);
        user.setDesired_religion("무교");
        user.setDesired_mbti("INFP");
        user.setDesired_school("4년제 졸업");
        user.setDesired_job("무직");

        return userRepository.save(user);
    }

    //회원가입
    public RsData<SiteUser>  create(boolean userStop, boolean userWarning, MultipartFile file, String username, String password, String email, String domain, String nickname, String gender)
            throws Exception {
        if (findByUsername(username).isPresent())
            return RsData.of("F-1", "%s(은)는 사용중인 아이디입니다.".formatted(username));
        if (findByEmail(email).isPresent())
            return RsData.of("F-2", "%s(은)는 사용중인 이메일입니다.".formatted(email));
        if (findByNickname(nickname).isPresent())
            return RsData.of("F-3", "%s(은)는 사용중인 닉네임입니다.".formatted(nickname));

        SiteUser user = new SiteUser();
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs(); // 디렉토리가 없으면 생성
        }
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(directory, fileName);
        file.transferTo(saveFile);
        user.setFilename(fileName);
        user.setFilepath("/gen/" + fileName);
        user.setUserStop(userStop);
        user.setUserWarning(userWarning);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setDomain(domain);
        user.setNickname(nickname);
        user.setGender(gender);

        this.userRepository.save(user);
        user = userRepository.save(user);
        return RsData.of("S-1", "회원가입이 완료되었습니다.", user);
    }
    //닉네임 있는지 찾기
    private Optional<SiteUser> findByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }
    //아이디 있는지 찾기
    public Optional<SiteUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    private Optional<SiteUser> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    //회원가입
    public SiteUser create(String username, String password, String gender) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setGender(gender);
        this.userRepository.save(user);
        return user;
    }

    //프로필 설정
    public SiteUser add_profile(SiteUser user, int age, String living, List<String> hobbyList, int tall, String bodyType,
                                String smoking, String drinking, List<String> styleList, String religion,
                                String mbti, String school, String job, String About_Me) {
        user.setAge(age);
        user.setLiving(living);
        user.setHobbyList(hobbyList);
        user.setTall(tall);
        user.setBody_type(bodyType);
        user.setSmoking(smoking);
        user.setDrinking(drinking);
        user.setStyleList(styleList);
        user.setReligion(religion);
        user.setMbti(mbti);
        user.setSchool(school);
        user.setJob(job);
        user.setAbout_Me(About_Me);
        this.userRepository.save(user);
        return user;
    }

    //이상형 설정 회원가입 할때
    public RsData<SiteUser> add_desired(SiteUser user, int desiredAge1,int desiredAge2, String desiredLiving,
                                        int desiredTall1,int desiredTall2, String desiredBodyType, String desiredSmoking,
                                        String desiredDrinking, List<String> desiredStyleList, String desiredReligion,
                                        String desiredMbti, String desiredSchool, String desiredJob) {

        user.setDesired_age1(desiredAge1);
        user.setDesired_age2(desiredAge2);
        user.setDesired_living(desiredLiving);
        user.setDesired_tall1(desiredTall1);
        user.setDesired_tall2(desiredTall2);
        user.setDesired_body_type(desiredBodyType);
        user.setDesired_smoking(desiredSmoking);
        user.setDesired_drinking(desiredDrinking);
        user.setDesired_styleList(desiredStyleList);
        user.setDesired_religion(desiredReligion);
        user.setDesired_mbti(desiredMbti);
        user.setDesired_school(desiredSchool);
        user.setDesired_job(desiredJob);
        this.userRepository.save(user);
        user = userRepository.save(user);
        return RsData.of("S-1", "회원가입이 완료되었습니다.", user);
    }
    private void sendJoinCompleteMail(SiteUser siteUser) {
        String full_email = siteUser.getEmail() + "@" + siteUser.getDomain();
        emailService.send(full_email, "회원가입이 완료되었습니다.", "회원가입이 완료되었습니다.");
    }
    //수정할 때
    public RsData<SiteUser> add_desired2(SiteUser user,int desiredAge1,int desiredAge2, String desiredLiving,
                                         int desiredTall1,int desiredTall2, String desiredBodyType, String desiredSmoking,
                                        String desiredDrinking, List<String> desiredStyleList, String desiredReligion,
                                        String desiredMbti, String desiredSchool, String desiredJob) {

        user.setDesired_age1(desiredAge1);
        user.setDesired_age2(desiredAge2);
        user.setDesired_living(desiredLiving);
        user.setDesired_tall1(desiredTall1);
        user.setDesired_tall2(desiredTall2);
        user.setDesired_body_type(desiredBodyType);
        user.setDesired_smoking(desiredSmoking);
        user.setDesired_drinking(desiredDrinking);
        user.setDesired_styleList(desiredStyleList);
        user.setDesired_religion(desiredReligion);
        user.setDesired_mbti(desiredMbti);
        user.setDesired_school(desiredSchool);
        user.setDesired_job(desiredJob);
        this.userRepository.save(user);
        user = userRepository.save(user);
        return RsData.of("S-1", "회원가입이 완료되었습니다.", user);
    }

    public boolean isCorrectPassword(String username, String password) {
        SiteUser user = getUserbyName(username);
        String actualPassword = user.getPassword();
        return passwordEncoder.matches(password, actualPassword);
    }

    public void updatePassword(String username, String newPassword) {
        SiteUser user = getUserbyName(username);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    //회원 탈퇴
    public void delete(SiteUser siteUser) {
        // 해당 사용자와 연결된 question 레코드 삭제
//        List<Question> userQuestions = questionRepository.findByAuthor(siteUser);
//        questionRepository.deleteAll(userQuestions);
        // 사용자 삭제
        this.userRepository.delete(siteUser);
    }

    @Transactional
    public SiteUser whenSocialLogin(String providerTypeCode, String username) {
        Optional <SiteUser> siteUser = findByUsername(username);

        if (siteUser.isPresent()) return siteUser.get();

        // 소셜 로그인를 통한 가입시 비번은 없다.
        return create(username, "","남"); // 최초 로그인 시 딱 한번 실행
    }



    public List<SiteUser> getAllUser() {
        List<SiteUser> siteUsers = userRepository.findAll();
        return siteUsers;
    }

    //메인에 지역이 같은 이성 랜덤하게 4명 불러오기
    public List<SiteUser> getFourUsers(String gender, String living) {
        List<SiteUser> randomUsers;

        if ("남".equals(gender)) {
            randomUsers = this.userRepository.findRandomUsersByGenderAndLiving("여", living, 4);
        } else if ("여".equals(gender)) {
            randomUsers = this.userRepository.findRandomUsersByGenderAndLiving("남", living, 4);
        } else {
            // Handle invalid gender or other cases
            randomUsers = Collections.emptyList();
        }

        return randomUsers;
    }

    //관리자 권한 부여
    public void grantAdminAuthority(String username) {
        SiteUser user = userRepository.findByUsername(username).orElse(null);

        if (user != null) {
            // 기존 권한 수정
            user.getAuthorities().clear(); // 모든 권한 제거
            user.getAuthorities().add(UserRole.ADMIN); // "ADMIN" 권한 추가
            userRepository.save(user);
        }
    }

    //관리자 권한 삭제
    public void deleteAdminAuthority(String username) {
        SiteUser user = userRepository.findByUsername(username).orElse(null);

        if (user != null) {
//             기존 권한 수정
            user.getAuthorities().clear(); // 모든 권한 제거
            userRepository.save(user);
        }
    }

    //로그인 한 유저 제외한 유저 목록들 불러오기
    public List<SiteUser> getAllUser2(SiteUser loginUser) {
        List<SiteUser> siteUsers = userRepository.findAll();

        for (int i = 0; i < siteUsers.size(); i++) {
            if (siteUsers.get(i).getId() == loginUser.getId()) {
                siteUsers.remove(i);
            }
        }
        return siteUsers;
    }


    //로그인한 사용자와 채팅방이 없는 유저만 불러오는 함수
    public List<SiteUser> getUsersNotRoom(SiteUser loginUser, List<SiteUser> siteUsers) {
        List<ChatRoom> AllRooms = chatRoomService.getAll();


        //이미 채팅방이 있는 유저들 id 저장소
        List<Integer> chatUsers = new ArrayList<>();

        for (int i = 0; i < AllRooms.size(); i++) {
            if (AllRooms.get(i).getSiteUser().getId() == loginUser.getId()) {
                chatUsers.add(AllRooms.get(i).getSiteUser2().getId()); //내가 초대한 유저의 아이디 저장
            }

            if (AllRooms.get(i).getSiteUser2().getId() == loginUser.getId()) {
                chatUsers.add(AllRooms.get(i).getSiteUser().getId()); //나를 초대한 유저의 아이디 저장
            }
        }

        //채팅방이 이미 있어서 삭제해야할  유저들이 있어야되는 chatUsers 에서의 인덱스값 들
        List<Integer> deleteUserIds = new ArrayList<>();
        System.out.println(chatUsers.toString());

        //그 인덱스 값들에 있는 user 들 삭제해서 중복된 채팅방 못만들게 하기
        for (int i = 0; i < siteUsers.size(); i++) {
            for (int j = 0; j < chatUsers.size(); j++) {
                if (siteUsers.get(i).getId() == chatUsers.get(j)) {
                    deleteUserIds.add(i);

                }
            }


        }


        for (int i = deleteUserIds.size() - 1; i >= 0; i--) {
            siteUsers.remove((int) deleteUserIds.get(i));
        }
        return siteUsers;

    }

    //사용자 정지
    public void changeUserStop(SiteUser siteUser) {
        siteUser.setUserStop(true);
        userRepository.save(siteUser);
    }

    //사용자 정지 해제
    public void resetUserStop(SiteUser siteUser) {
        siteUser.setUserStop(false);
        userRepository.save(siteUser);
    }

    //사용자 경고
    public void changeUserWarning(SiteUser siteUser) {
        siteUser.setUserWarning(true);
        userRepository.save(siteUser);
    }

    //사용자 경고 해제
    public void resetUserWarning(SiteUser siteUser) {
        siteUser.setUserWarning(false);
        userRepository.save(siteUser);
    }

    public List<SiteUser> getDesiredUsers(SiteUser user) {
        return userRepository.findByDesired(user.getGender(), user.getDesired_living(), user.getDesired_religion());
    }

    //사진 수정
    public void photoModify(SiteUser user, MultipartFile file) throws Exception {
        File directory = new File(uploadDir);
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(directory, fileName);
        file.transferTo(saveFile);
        user.setFilename(fileName);
        user.setFilepath("/gen/" + fileName);
        this.userRepository.save(user);
    }


    public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByUsername(username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }


    public void updateMinHeart(String username, Integer minHeart) {
        SiteUser user = getUserbyName(username);
        user.setMinHeart(minHeart);
        updateUser(user);
    }

    public void updateUser(SiteUser user) {
        userRepository.save(user);
    }

    public boolean isUsernameAlreadyExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public RsData<String> checkUsernameDup(String username) {
        if (findByUsername(username).isPresent()) return RsData.of("F-1", "%s(은)는 사용중인 아이디입니다.".formatted(username));
        return RsData.of("S-1", "%s(은)는 사용 가능한 아이디입니다.".formatted(username),username);
    }
    public RsData<String> checkNicknameDup(String nickname) {
        if (findByNickname(nickname).isPresent()) return RsData.of("F-1", "%s(은)는 사용중인 닉네임입니다.".formatted(nickname));
        return RsData.of("S-1", "%s(은)는 사용 가능한 닉네임입니다.".formatted(nickname),nickname);
    }

    public RsData<String> checkEmailDup(String email) {
        if (findByEmail(email).isPresent()) return RsData.of("F-1", "%s(은)는 사용중인 이메일입니다.".formatted(email));
        return RsData.of("S-1", "%s(은)는 사용 가능한 이메일입니다.".formatted(email),email);
    }

}