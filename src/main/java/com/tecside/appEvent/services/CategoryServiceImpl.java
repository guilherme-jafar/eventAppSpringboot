package com.tecside.appEvent.services;

import com.tecside.appEvent.errors.ErrorMessages;
import com.tecside.appEvent.models.Category;
import com.tecside.appEvent.repositories.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@EnableTransactionManagement
@Service
public class CategoryServiceImpl implements CategoryService {

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

        if (updatedCategory.getName() != null) {
            category.setName(updatedCategory.getName());
        }

        return categoryRepository.saveAndFlush(category);

    }

    @Override
    public void deleteCategory(String categoryId) throws EmptyResultDataAccessException {

        categoryRepository.deleteById(categoryId);

    }

    @Transactional
    public Category uploadImage(String id, MultipartFile file) throws EntityNotFoundException, IOException {

        Optional<Category> optional = categoryRepository.findById(id);

        if (optional.isEmpty())
            throw new EntityNotFoundException(ErrorMessages.CATEGORY_NOT_FOUND);


        if (file.isEmpty())
            throw new FileUploadException(ErrorMessages.MISSING_IMAGE);

        Category category = optional.get();

        if (category.getImage() != null) {
            System.out.println(category.getImage());
            imageService.blobDelete(category.getImage());

        }
        String categoryName = category.getName().toLowerCase().trim();

        String imageName = imageService.blobUpload(file, categoryName);

        category.setImage(imageName);


        return categoryRepository.saveAndFlush(category);


    }

}
