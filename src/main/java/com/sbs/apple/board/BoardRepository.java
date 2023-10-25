package com.sbs.apple.board;


import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board , Integer> {
}
