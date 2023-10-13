package com.sbs.apple.chat;

import com.sbs.apple.user.SiteUser;
import com.sbs.apple.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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


    public record WriteMessageRequest(String authorName, String content) {
    }

    public record WriteMessageResponse(long id) {
    }


    //채팅방 만들기
    @GetMapping("/{roomId}/room")
    public String showRoom(  @RequestParam("userId") Integer userId2 , Model model, Principal principal) {
        SiteUser user =userService.getUserbyName(principal.getName());
        SiteUser user2= userService.getUser(userId2);



            chatRoomService.create(user, user2);


        return "chat/room";
    }

//    원래 있던 채팅방 들어가기
//    @GetMapping("/{roomId}/room")
//    public String showRoom2(  @RequestParam("userId2") Integer userId2 ,Model model, Principal principal) {
//        SiteUser user =userService.getUserbyName(principal.getName());
//
//
//        return "chat/room";
//    }

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
        model.addAttribute("chatRoom",chatRooms);

        if( chatRooms2 !=null) {
            model.addAttribute("chatRoom2",chatRooms2); //내가 초대 한  채팅방
        }
        if( chatRooms3 !=null) {
            model.addAttribute("chatRoom3",chatRooms3);  //내가 초대 받은 채팅방
        }

        return "chat/allRoom";
    }
}
