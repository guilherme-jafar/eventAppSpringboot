package com.tecside.appEvent.services;

import com.tecside.appEvent.models.Category;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CategoryService {

    void createCategory(Category category) throws DataIntegrityViolationException;

    Optional<Category> getCategoryById(Long id);

    List<Category> getAllCategories();

    List<Category> getCategoriesByName(String name);

    Category updateCategory(Long categoryId, Category updatedCategory) throws DataIntegrityViolationException;

    void deleteCategory(Long categoryId) throws EmptyResultDataAccessException;

}
