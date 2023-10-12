package com.sbs.apple.chat;

import com.sbs.apple.user.SiteUser;
import com.sbs.apple.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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

    @GetMapping("/{roomId}/room")
    public String showRoom(@PathVariable Long roomId, Model model, Principal principal) {
        SiteUser user =userService.getUserbyName(principal.getName());
        System.out.println(user.getId());

        chatRoomService.create(user.getId());


        model.addAttribute("roomId", roomId);
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
}
