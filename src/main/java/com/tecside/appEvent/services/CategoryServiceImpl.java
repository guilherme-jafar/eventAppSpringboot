package com.tecside.appEvent.services;

import com.tecside.appEvent.errors.ErrorMessages;
import com.tecside.appEvent.models.Category;
import com.tecside.appEvent.repositories.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.azure.storage.blob.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final ImageService imageService;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ImageService imageService) {
        this.categoryRepository = categoryRepository;
        this.imageService = imageService;

    }

    @Override
    public Optional<Category> createCategory(Category category) throws DataIntegrityViolationException {

        String name = category.getName();


        if (name == null) {
            throw new DataIntegrityViolationException(ErrorMessages.MISSING_NAME);
        }



        Date createdAt = new Date();
        Category newCategory = new Category();
        newCategory.setCreatedAt(createdAt);
        newCategory.setName(category.getName());

        categoryRepository.saveAndFlush(newCategory);
        return categoryRepository.findById(newCategory.getId());

    }

    @Override
    public Optional<Category> getCategoryById(String id) {
        return categoryRepository.findById(id);
    }
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    @Override
    public List<Category> getCategoriesByName(String name) {
        return categoryRepository.findByNameLike(name);
    }
    @Override
    public Category updateCategory(String categoryId, Category updatedCategory) throws DataIntegrityViolationException {

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
    @Override
    public void deleteCategory(String categoryId) throws EmptyResultDataAccessException {

        categoryRepository.deleteById(categoryId);

    }

    public Category uploadImage(String id, MultipartFile file)throws EntityNotFoundException, Exception{

        Optional<Category> optional = categoryRepository.findById(id);

        if (optional.isEmpty())
            throw new EntityNotFoundException(ErrorMessages.CATEGORY_NOT_FOUND);


        String imageName = imageService.blobUpload(file);

        Category category = optional.get();

        category.setImage(imageName);




        return categoryRepository.saveAndFlush(category);


    }

}
