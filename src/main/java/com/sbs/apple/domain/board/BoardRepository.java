package com.sbs.apple.domain.board;


import com.sbs.apple.domain.user.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board , Integer> {


    @Query("SELECT cr FROM Board cr WHERE cr.siteUser = :user" )
    List<Board> findByUserId(@Param("user") SiteUser user);
}
