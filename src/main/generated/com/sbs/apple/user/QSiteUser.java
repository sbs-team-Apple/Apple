package com.sbs.apple.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSiteUser is a Querydsl query type for SiteUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSiteUser extends EntityPathBase<SiteUser> {

    private static final long serialVersionUID = -597906262L;

    public static final QSiteUser siteUser = new QSiteUser("siteUser");

    public final StringPath About_Me = createString("About_Me");

    public final NumberPath<Integer> age = createNumber("age", Integer.class);

    public final SetPath<UserRole, EnumPath<UserRole>> authorities = this.<UserRole, EnumPath<UserRole>>createSet("authorities", UserRole.class, EnumPath.class, PathInits.DIRECT2);

    public final ListPath<com.sbs.apple.board.Board, com.sbs.apple.board.QBoard> boardList = this.<com.sbs.apple.board.Board, com.sbs.apple.board.QBoard>createList("boardList", com.sbs.apple.board.Board.class, com.sbs.apple.board.QBoard.class, PathInits.DIRECT2);

    public final StringPath body_type = createString("body_type");

    public final ListPath<com.sbs.apple.chat.ChatRoom, com.sbs.apple.chat.QChatRoom> chatRoomList = this.<com.sbs.apple.chat.ChatRoom, com.sbs.apple.chat.QChatRoom>createList("chatRoomList", com.sbs.apple.chat.ChatRoom.class, com.sbs.apple.chat.QChatRoom.class, PathInits.DIRECT2);

    public final NumberPath<Integer> cyberMoney = createNumber("cyberMoney", Integer.class);

    public final StringPath desired_age = createString("desired_age");

    public final StringPath desired_body_type = createString("desired_body_type");

    public final StringPath desired_drinking = createString("desired_drinking");

    public final StringPath desired_hobby = createString("desired_hobby");

    public final StringPath desired_job = createString("desired_job");

    public final StringPath desired_living = createString("desired_living");

    public final StringPath desired_mbti = createString("desired_mbti");

    public final StringPath desired_religion = createString("desired_religion");

    public final StringPath desired_school = createString("desired_school");

    public final StringPath desired_smoking = createString("desired_smoking");

    public final ListPath<String, StringPath> desired_styleList = this.<String, StringPath>createList("desired_styleList", String.class, StringPath.class, PathInits.DIRECT2);

    public final StringPath desired_tall = createString("desired_tall");

    public final StringPath drinking = createString("drinking");

    public final StringPath filename = createString("filename");

    public final StringPath filepath = createString("filepath");

    public final StringPath gender = createString("gender");

    public final ListPath<String, StringPath> hobbyList = this.<String, StringPath>createList("hobbyList", String.class, StringPath.class, PathInits.DIRECT2);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final ListPath<com.sbs.apple.interest.Interest, com.sbs.apple.interest.QInterest> interestList = this.<com.sbs.apple.interest.Interest, com.sbs.apple.interest.QInterest>createList("interestList", com.sbs.apple.interest.Interest.class, com.sbs.apple.interest.QInterest.class, PathInits.DIRECT2);

    public final StringPath job = createString("job");

    public final StringPath living = createString("living");

    public final StringPath mbti = createString("mbti");

    public final StringPath nickname = createString("nickname");

    public final ListPath<com.sbs.apple.notification.Notification, com.sbs.apple.notification.QNotification> notificationList = this.<com.sbs.apple.notification.Notification, com.sbs.apple.notification.QNotification>createList("notificationList", com.sbs.apple.notification.Notification.class, com.sbs.apple.notification.QNotification.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    public final NumberPath<Integer> receivedCyberMoney = createNumber("receivedCyberMoney", Integer.class);

    public final StringPath religion = createString("religion");

    public final ListPath<com.sbs.apple.report.Report, com.sbs.apple.report.QReport> reportList = this.<com.sbs.apple.report.Report, com.sbs.apple.report.QReport>createList("reportList", com.sbs.apple.report.Report.class, com.sbs.apple.report.QReport.class, PathInits.DIRECT2);

    public final StringPath school = createString("school");

    public final StringPath smoking = createString("smoking");

    public final ListPath<String, StringPath> styleList = this.<String, StringPath>createList("styleList", String.class, StringPath.class, PathInits.DIRECT2);

    public final NumberPath<Integer> tall = createNumber("tall", Integer.class);

    public final StringPath username = createString("username");

    public final BooleanPath userStop = createBoolean("userStop");

    public final BooleanPath userWarning = createBoolean("userWarning");

    public QSiteUser(String variable) {
        super(SiteUser.class, forVariable(variable));
    }

    public QSiteUser(Path<? extends SiteUser> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSiteUser(PathMetadata metadata) {
        super(SiteUser.class, metadata);
    }

}

