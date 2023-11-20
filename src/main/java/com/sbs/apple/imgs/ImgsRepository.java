package com.sbs.apple.imgs;

import com.sbs.apple.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.Map;

public interface ImgsRepository extends JpaRepository<Imgs, Integer> {
  List<Imgs> findByBoardId(Integer id);

  List<Imgs> findByBoardIdOrderByIndexAAsc(Integer boardId);

  @Query("SELECT i FROM Imgs i WHERE i.board = :board AND i.indexA = :index")
  Imgs findByBoardIdAndIndexA(@Param("board") Board board, @Param("index") Integer index);

  @Transactional
  @Modifying
  @Query("DELETE FROM Imgs i WHERE i.board = :board AND i.indexA IN :deleteIndex")
  void deleteByBoardAndIndexA(@Param("board")Board board,@Param("deleteIndex") List<Integer> deleteIndex);
}
