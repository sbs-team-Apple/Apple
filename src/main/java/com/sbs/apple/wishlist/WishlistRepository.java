package com.sbs.apple.wishlist;

import com.sbs.apple.user.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<SiteUser, String> {
    // 필요한 경우 사용자 정의 메서드 추가 가능
    List<SiteUser> findByOwner(String ownerId);
}