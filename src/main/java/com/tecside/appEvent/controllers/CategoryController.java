package com.tecside.appEvent.controllers;

import com.tecside.appEvent.errors.ErrorMessages;
import com.tecside.appEvent.models.Category;
import com.tecside.appEvent.models.User;
import com.tecside.appEvent.services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {


    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/")
    public ResponseEntity<?> createCategory(@RequestBody Category newCategory) {

        try {

            Category category = categoryService.createCategory(newCategory);
            return new ResponseEntity<>(category, HttpStatus.CREATED);

        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(ErrorMessages.errorJSON(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println(e.toString());
            return new ResponseEntity<>(ErrorMessages.errorJSON(ErrorMessages.GENERAL_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("{categoryId}/image")
    public ResponseEntity<?> uploadImage(@PathVariable("categoryId") String id, @RequestParam("file") MultipartFile file) {

        try {

            Category category = categoryService.uploadImage(id, file);
            return new ResponseEntity<>(category, HttpStatus.CREATED);

        } catch (EntityNotFoundException | IOException e) {
            return new ResponseEntity<>(ErrorMessages.errorJSON(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println(e.toString());
            return new ResponseEntity<>(ErrorMessages.errorJSON(ErrorMessages.GENERAL_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategoryById(@PathVariable("categoryId") String categoryId) {

        try {

            Optional<Category> category = categoryService.getCategoryById(categoryId);

            if (category.isEmpty()) {
                return new ResponseEntity<>(ErrorMessages.errorJSON(ErrorMessages.CATEGORY_NOT_FOUND), HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(category, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.toString());
            return new ResponseEntity<>(ErrorMessages.errorJSON(ErrorMessages.GENERAL_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @GetMapping("/")
    public ResponseEntity<?> getAllCategories() {

        try {

            List<Category> categories = categoryService.getAllCategories();

            return new ResponseEntity<>(categories, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.toString());
            return new ResponseEntity<>(ErrorMessages.errorJSON(ErrorMessages.GENERAL_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable("categoryId") String categoryId, @RequestBody Category category) {


        try {

            Category updatedCategory = categoryService.updateCategory(categoryId, category);

            return new ResponseEntity<>(updatedCategory, HttpStatus.OK);

        } catch (DataIntegrityViolationException e) {

            HttpStatus httpStatus = (e.getMessage().equals(ErrorMessages.CATEGORY_NOT_FOUND)) ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;

            return new ResponseEntity<>(ErrorMessages.errorJSON(e.getMessage()), httpStatus);
        } catch (Exception e) {
            System.out.println(e.toString());
            return new ResponseEntity<>(ErrorMessages.errorJSON(ErrorMessages.GENERAL_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable("categoryId") String categoryId) {

        try {

           categoryService.deleteCategory(categoryId);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (EmptyResultDataAccessException e) {

            return new ResponseEntity<>(ErrorMessages.errorJSON(ErrorMessages.CATEGORY_NOT_FOUND), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            System.out.println(e.toString());
            return new ResponseEntity<>(ErrorMessages.errorJSON(ErrorMessages.GENERAL_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
