package com.sbs.apple.imgs;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import java.util.Map;

public interface ImgsRepository extends JpaRepository<Imgs, Integer> {
  List<Imgs> findByBoardId(Integer id);
}
