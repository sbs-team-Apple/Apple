package com.sbs.apple.notification;

import com.sbs.apple.user.SiteUser;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class NotificationDTO {
    private Integer id;
    private String kind;
    private SiteUserDTO userTo;
    private SiteUserDTO userFrom;


    public NotificationDTO(Notification notification) {

        this.id= notification.getId();
        this.kind= notification.getKind();
        this.userTo=new SiteUserDTO(notification.getSiteUser());
        this.userFrom=new SiteUserDTO(notification.getSiteUserFrom());




    }



        @Getter
        public static class SiteUserDTO {

            private Integer id;
            private String nickname;


            public SiteUserDTO(SiteUser siteUser){

                this.id= siteUser.getId();
                this.nickname= siteUser.getNickname();

            }

        }



}
