package com.project.Mental.repository;

import com.project.Mental.model.MoodEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MoodRepository extends JpaRepository<MoodEntry, Long> {
    List<MoodEntry> findByUsername(String username);
}