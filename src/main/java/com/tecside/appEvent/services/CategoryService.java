package com.tecside.appEvent.services;

import com.tecside.appEvent.models.Category;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public interface CategoryService {

    Optional<Category> createCategory(Category category) throws DataIntegrityViolationException;

    Optional<Category> getCategoryById(String id);

    List<Category> getAllCategories();

    List<Category> getCategoriesByName(String name);

    Category updateCategory(String categoryId, Category updatedCategory) throws DataIntegrityViolationException;

    void deleteCategory(String categoryId) throws EmptyResultDataAccessException;

    public Category uploadImage(String id, MultipartFile file)throws EntityNotFoundException, Exception;

}
