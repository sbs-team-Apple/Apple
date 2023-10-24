package com.sbs.apple.whislist;

import com.sbs.apple.user.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<SiteUser, String> {
    // 필요한 경우 사용자 정의 메서드 추가 가능
}
