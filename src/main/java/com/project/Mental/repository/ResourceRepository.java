package com.project.Mental.repository;
import com.project.Mental.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ResourceRepository extends JpaRepository<Resource, String> {
    List<Resource> findByToolkitId(int toolkitId);
}