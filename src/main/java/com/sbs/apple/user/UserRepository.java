package com.sbs.apple.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<SiteUser, Integer> {
    Optional<SiteUser> findByUsername(String username);

    boolean existsByUsername(String username);


    @Query(value = "SELECT * FROM site_user WHERE gender = :gender AND living = :living ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<SiteUser> findRandomUsersByGenderAndLiving(@Param("gender") String gender, @Param("living") String living, @Param("count") int count);

}
