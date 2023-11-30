package com.sbs.apple.chat;

import com.sbs.apple.notification.Notification;
import com.sbs.apple.notification.NotificationService;
import com.sbs.apple.user.SiteUser;
import com.sbs.apple.user.UserRole;
import com.sbs.apple.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final SseEmitters sseEmitters;
    private final UserService userService;
    private final ChatMessages chatMessages;
    private final ChatRoomService chatRoomService;
    private final NotificationService notificationService;
    private  final ChatRoomRepository chatRoomRepository;



    public record WriteMessageRequest(String authorName, String content) {
    }

    public record WriteMessageResponse(long id) {
    }


    //채팅방 만들기
    @PostMapping("/{roomId}/room/{userId}")
    public String makeRoom(  @PathVariable("userId") Integer userId2 , @PathVariable Long roomId, Model model, Principal principal) {
        System.out.println("채팅방 만들기 실행됨");

        SiteUser user =userService.getUserbyName(principal.getName());
        SiteUser user2= userService.getUser(userId2);



           ChatRoom room= chatRoomService.create(user, user2);


        String groupKey = "userId_" + userId2;
        System.out.println(groupKey);
        SseEmitter emitter = new SseEmitter();
        sseEmitters.add(groupKey, emitter);
        sseEmitters.noti(groupKey, "invite_chatRoom");

        // 채팅방 만들었다는 알림 기록 남기기
        notificationService.create(user2,user,"chatRoom");





        model.addAttribute("roomId", room.getId());
        model.addAttribute("user",user);
        return "redirect:/chat/"+room.getId()+"/room3?userId2="+userId2;
    }
    
    
    // 사이버 머니 받고 수락시 넘어올 떄를 위해 만든거
    @GetMapping("/{roomId}/room/{userId}")
    public String makeRoom2(  @PathVariable("userId") Integer userId2 , @PathVariable Long roomId, Model model, Principal principal) {
        System.out.println("채팅방 만들기 실행됨");

        SiteUser user =userService.getUserbyName(principal.getName());
        SiteUser user2= userService.getUser(userId2);



        ChatRoom room= chatRoomService.create(user, user2);




        String groupKey = "userId_" + userId2;
        System.out.println(groupKey);
        SseEmitter emitter = new SseEmitter();
        sseEmitters.add(groupKey, emitter);
        sseEmitters.noti(groupKey, "invite_chatRoom");

        // 채팅방 만들었다는 알림 기록 남기기
        notificationService.create(user2,user,"chatRoom");





        model.addAttribute("roomId", room.getId());
        model.addAttribute("user",user);
        return "redirect:/chat/"+room.getId()+"/room3?userId2="+userId2;
    }


    //   내가 초대했던 원래 있던 채팅방 들어가기
    @GetMapping("/{roomId}/room3")
    public String showRoom2(  @RequestParam("userId2") Integer userId2 ,Model model, Principal principal) {
        SiteUser user =userService.getUserbyName(principal.getName());
        ChatRoom room = chatRoomService.findRoomByUserIdAndUserId2(user.getId(),userId2);


        //채팅방 사용자가 아닌 사람이 주소창으로 억지로 채팅방에 들어올려고 할 경우 막기

        if(room ==null){

            return "redirect:/";
        }

        //채팅방 사용자가 아닌 사람이 주소창으로 억지로 채팅방에 들어올려고 할 경우 막기
        if(user.getId()!=room.getSiteUser().getId() && !user.getAuthorities().equals("[ADMIN]")){
            return "redirect:/";

        }

        System.out.println("원래 있던 채팅방에 접속");


        //혹시 중간에 상대방이 채팅방을 나가서 채팅방 사라지거나 그러면 채팅방 목록으로 가기
        if(room ==null){
            return "redirect:/chat/allRoom";
            
        }
        
        System.out.println("채팅방에 들어갈 방번호 "+room.getId());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SiteUser users = userService.getUserbyName(username);
        int userCyberMoney = users.getCyberMoney();
        model.addAttribute("userCyberMoney", userCyberMoney);




        model.addAttribute("roomId",room.getId() );
        model.addAttribute("user",user);

        //나랑 채팅하고 있는 사람
        SiteUser toUser=room.getSiteUser2();
        model.addAttribute(("toUser"),toUser);


        return "chat/room";
    }

// 내가 초대 받은 채팅방 들어가기
    @GetMapping("/{roomId}/room2")
    public String showRoom3(  @RequestParam("userId") Integer userId ,Model model, Principal principal) {

        SiteUser user =userService.getUserbyName(principal.getName());
        ChatRoom room = chatRoomService.findRoomByUserIdAndUserId2(userId,user.getId());

        //채팅방 사용자가 아닌 사람이 주소창으로 억지로 채팅방에 들어올려고 할 경우 막기
        if(room ==null){

            return "redirect:/";
        }

        //채팅방 사용자가 아닌 사람이 주소창으로 억지로 채팅방에 들어올려고 할 경우 막기
        if(user.getId() != room.getSiteUser2().getId() && !user.getAuthorities().equals("[ADMIN]")){
            return "redirect:/";

        }

        System.out.println("원래 있던 채팅방에 접속");


        if(room ==null){
            return "redirect:/chat/allRoom";

        }



        System.out.println("채팅방에 들어갈 방번호 "+room.getId());

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            SiteUser users = userService.getUserbyName(username);
            int userCyberMoney = users.getCyberMoney();
            model.addAttribute("userCyberMoney", userCyberMoney);


        model.addAttribute("roomId",room.getId() );
        model.addAttribute("user",user);

        //나랑 채팅하고 있는 사람, 나한테 채팅초대한 사람
        SiteUser toUser=room.getSiteUser();
        model.addAttribute(("toUser"),toUser);
//        Notification notification =notificationService.findByUsers(toUser, user);
//        if(notification !=null) {
//            notificationService.delete(notification);
//        }


        return "chat/room";
    }

    @PostMapping("/{roomId}/writeMessage")
    @ResponseBody
    public RsData<WriteMessageResponse> writeMessage(@PathVariable Long roomId, @RequestBody WriteMessageRequest req) {

        ChatMessage message = new ChatMessage(req.authorName(), req.content());

        chatMessages.add(roomId, message);

        String groupKey = "chatRoom__" + roomId;
        sseEmitters.noti(groupKey, "chat__messageAdded");

        return new RsData<>(
                "S-1",
                "메세지가 작성되었습니다.",
                new WriteMessageResponse(message.getId())
        );
    }

    public record MessagesRequest(Long fromId) {
    }

    public record MessagesResponse(List<ChatMessage> messages, long count) {
    }

    @GetMapping("/{roomId}/messages")
    @ResponseBody
    public RsData<MessagesResponse> messages(@PathVariable Long roomId, MessagesRequest req) {
        List<ChatMessage> messages = chatMessages.from(roomId, req.fromId);

        return new RsData<>(
                "S-1",
                "성공",
                new MessagesResponse(messages, messages.size())
        );
    }

    @GetMapping("/{roomId}/lastMessages")
    @ResponseBody
    public  RsData<ChatMessage> lastMessages(@PathVariable Long roomId, MessagesRequest req) {
        List<ChatMessage> messages = chatMessages.from(roomId, req.fromId);

        ChatMessage lastMessage = null;
        if (!messages.isEmpty()) {
            lastMessage = messages.get(messages.size() - 1);
        }


        return new RsData<>(
                "S-1",
                "성공",
                lastMessage
        );
    }



    @GetMapping("/allRoom")
    public String allRoom(Model model,Principal principal){
        SiteUser siteUser = userService.getUserbyName(principal.getName());
        List<ChatRoom> chatRooms=chatRoomService.findByUser(siteUser);
        List<ChatRoom> chatRooms2 =new ArrayList<>();
        List<ChatRoom> chatRooms3=new ArrayList<>() ;
        for(int i=0; i<chatRooms.size(); i++){


                if(chatRooms.get(i).getSiteUser().getId() == siteUser.getId() ){
                    chatRooms2.add(chatRooms.get(i));
                }else {
                    chatRooms3.add(chatRooms.get(i));
                }


                 //채팅방 나가기를 누른 유저들의 입장권 유무 검사
                if(chatRooms.get(i).getFromUserPass()==false ) {

                    chatRooms2.remove(chatRooms.get(i));

                }else if (chatRooms.get(i).getToUserPass()==false ){

                    chatRooms3.remove(chatRooms.get(i));

                }


        }



        if( chatRooms2 !=null) {
            model.addAttribute("chatRoom2",chatRooms2); //내가 초대 한  채팅방
        }
        if( chatRooms3 !=null) {
            model.addAttribute("chatRoom3",chatRooms3);  //내가 초대 받은 채팅방
        }




        return "chat/allRoom";
    }


    @GetMapping("/allRoomForAdmin/{userId}")
    public String allRoomForAdmin(Model model,Principal principal,@PathVariable Integer userId){
        SiteUser siteUser = userService.getUserbyName(principal.getName());
        List<ChatRoom> chatRooms=chatRoomService.findByUser(siteUser);
        List<ChatRoom> chatRooms2 =new ArrayList<>();
        List<ChatRoom> chatRooms3=new ArrayList<>() ;
        for(int i=0; i<chatRooms.size(); i++){


            if(chatRooms.get(i).getSiteUser().getId() == siteUser.getId() ){
                chatRooms2.add(chatRooms.get(i));
            }else {
                chatRooms3.add(chatRooms.get(i));
            }


            //채팅방 나가기를 누른 유저들의 입장권 유무 검사
            if(chatRooms.get(i).getFromUserPass()==false ) {

                chatRooms2.remove(chatRooms.get(i));

            }else if (chatRooms.get(i).getToUserPass()==false ){

                chatRooms3.remove(chatRooms.get(i));

            }


        }



        if( chatRooms2 !=null) {
            model.addAttribute("chatRoom2",chatRooms2); //내가 초대 한  채팅방
        }
        if( chatRooms3 !=null) {
            model.addAttribute("chatRoom3",chatRooms3);  //내가 초대 받은 채팅방
        }




        return "chat/allRoom";
    }



    @GetMapping("/{roomId}/delete")
    public String deleteRoom ( @PathVariable Integer roomId,@RequestParam("userId") Integer userId ) {
        ChatRoom room=chatRoomService.findById(roomId);

        // 채팅방 나가기를 누른 유저
        SiteUser user = userService.getUser(userId);
        System.out.println(user.getId());

        if(room.getSiteUser()==user){
            System.out.println("보낸 사람이 나가기 누른사람");
            room.setFromUserPass(false);
            chatRoomRepository.save(room);
        }else if(room.getSiteUser2()==user){
            System.out.println("초대받은  사람이 나가기 누른사람");
            room.setToUserPass(false);
            chatRoomRepository.save(room);
        }




//        chatRoomService.delete(room);


//        그 채팅룸의 유저 정보로 알람 기록을 찾아서 나갈때 그 알람 기록도 삭제해주기
//        Notification notification=notificationService.findByUsers(room.getSiteUser(),room.getSiteUser2());
//
//        if(notification !=null){
//                notificationService.delete(notification);
//        }

        return "redirect:/chat/allRoom";


    }


    @GetMapping("/admin/allChatRooms")
    public String adminAllChatRooms(Model model , Principal principal){
        SiteUser currentUser =userService.getUserbyName(principal.getName());


        //관리자가 아닌 사람이 접속 하려할때 메인페이지로 보내기
        if(!currentUser.getAuthorities().contains(UserRole.ADMIN)){



            return "redirect:/";

        }

        List<ChatRoom> rooms = chatRoomService.findAll();

        model.addAttribute("rooms" ,rooms);


        return "chat/admin_allChatRoom";

    }

    @GetMapping("/{roomId}/adminChatRoom/{toUserId}/and/{fromUserId}")
    public String adminChatRoom(@PathVariable Integer roomId , @PathVariable Integer toUserId,
                                @PathVariable Integer fromUserId,Model model,Principal principal){
        SiteUser currentUser =userService.getUserbyName(principal.getName());

        //관리자가 아닌 사람이 접속 하려할때 메인페이지로 보내기
        if(!currentUser.getAuthorities().contains(UserRole.ADMIN)){

            return "redirect:/";

        }

        ChatRoom room = chatRoomService.findById(roomId);
        SiteUser fromUser = userService.getUser(fromUserId);
        SiteUser toUser = userService.getUser(toUserId);

        model.addAttribute("roomId",room.getId() );
        model.addAttribute("user",fromUser);

        //나랑 채팅하고 있는 사람
        model.addAttribute("toUser",toUser);


        return "chat/admin_chatRoom";
    }

}
