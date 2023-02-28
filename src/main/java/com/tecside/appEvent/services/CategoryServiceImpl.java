package com.tecside.appEvent.services;

import com.tecside.appEvent.errors.ErrorMessages;
import com.tecside.appEvent.models.Category;
import com.tecside.appEvent.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;

    }

    public void createCategory(Category category) throws DataIntegrityViolationException {

        String name = category.getName();
        String image = category.getImage();

        if (name == null) {
            throw new DataIntegrityViolationException(ErrorMessages.MISSING_NAME);
        }


        if (image == null) {
            throw new DataIntegrityViolationException(ErrorMessages.MISSING_IMAGE);
        }

        Date createdAt = new Date();
        Category newCategory = new Category();
        newCategory.setId(UUID.randomUUID());
        newCategory.setCreatedAt(createdAt);
        newCategory.setName(category.getName());
        newCategory.setImage(category.getImage());

        categoryRepository.saveAndFlush(newCategory);


    }


    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }


    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getCategoriesByName(String name) {
        return categoryRepository.findByNameLike(name);
    }

    public Category updateCategory(Long categoryId, Category updatedCategory) throws DataIntegrityViolationException {

        Optional<Category> optional = categoryRepository.findById(categoryId);
        if (optional.isEmpty()) {
            throw new DataIntegrityViolationException(ErrorMessages.CATEGORY_NOT_FOUND);


        }

        Category category = optional.get();

        if (updatedCategory.getImage() != null) {
            category.setImage(updatedCategory.getImage());
        }

        if (updatedCategory.getName() != null) {
            category.setName(updatedCategory.getName());
        }

        return categoryRepository.saveAndFlush(category);

    }

    public void deleteCategory(Long categoryId) throws EmptyResultDataAccessException {

        categoryRepository.deleteById(categoryId);

    }

}
