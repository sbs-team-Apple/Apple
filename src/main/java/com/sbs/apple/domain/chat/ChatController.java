package com.sbs.apple.domain.chat;

import com.sbs.apple.domain.notification.Notification;
import com.sbs.apple.domain.notification.NotificationService;
import com.sbs.apple.domain.user.SiteUser;
import com.sbs.apple.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        System.out.println("원래 있던 채팅방에 접속");
        SiteUser user =userService.getUserbyName(principal.getName());
        ChatRoom room = chatRoomService.findRoomByUserIdAndUserId2(user.getId(),userId2);
        System.out.println("채팅방에 들어갈 방번호 "+room.getId());

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
        System.out.println("원래 있던 채팅방에 접속");
        SiteUser user =userService.getUserbyName(principal.getName());
        ChatRoom room = chatRoomService.findRoomByUserIdAndUserId2(userId,user.getId());
        System.out.println("채팅방에 들어갈 방번호 "+room.getId());

        model.addAttribute("roomId",room.getId() );
        model.addAttribute("user",user);

        //나랑 채팅하고 있는 사람
        SiteUser toUser=room.getSiteUser();
        model.addAttribute(("toUser"),toUser);


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
            if(chatRooms.get(i).getSiteUser().getId() == siteUser.getId()){
                chatRooms2.add(chatRooms.get(i));
            }else {
                chatRooms3.add(chatRooms.get(i));
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
    public String deleteRoom ( @PathVariable Integer roomId) {
        ChatRoom room=chatRoomService.findById(roomId);

        chatRoomService.delete(room);


        //그 채팅룸의 유저 정보로 알람 기록을 찾아서 나갈때 그 알람 기록도 삭제해주기
        Notification notification=notificationService.findByUsers(room.getSiteUser(),room.getSiteUser2());

        if(notification !=null){
                notificationService.delete(notification);
        }





        return "redirect:/chat/allRoom";


    }
}
