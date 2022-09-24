package com.roughsea.repositories;

import com.roughsea.models.Message;
import com.roughsea.models.User;
import com.roughsea.models.dto.MessageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select new com.roughsea.models.dto.MessageDto(" +
            "m, " +
            "count(ml), " +
            "   sum(case when ml = :user then 1 else 0 end) > 0) " +
            "from Message m left join m.likes ml " +
            "group by m")
    Page<MessageDto> findAll(Pageable pageable, @Param("user") User user);

    @Query("select new com.roughsea.models.dto.MessageDto(" +
            "m, " +
            "count(ml), " +
            "sum(case when ml = :user then 1 else 0 end) > 0) " +
            "from Message m left join m.likes ml " +
            "where m.tag = :tag " +
            "group by m")
    Page<MessageDto> findByTag(@Param("tag") String tag, Pageable pageable, @Param("user") User user);

    @Query("select new com.roughsea.models.dto.MessageDto(" +
            "m, " +
            "count(ml), " +
            "sum(case when ml = :user then 1 else 0 end) > 0) " +
            "from Message m left join m.likes ml " +
            "where m.author = :author " +
            "group by m")
    Page<MessageDto> findByUser(Pageable pageable, @Param("author") User author, @Param("user") User user);

}