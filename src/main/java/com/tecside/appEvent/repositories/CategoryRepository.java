package com.tecside.appEvent.repositories;

import com.tecside.appEvent.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {


    List<Category> findByNameLike(String name);

    Page<Category> findAll(Pageable pageable);
}
