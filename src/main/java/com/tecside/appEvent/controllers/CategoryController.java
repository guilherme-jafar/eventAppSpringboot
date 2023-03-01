package com.tecside.appEvent.controllers;

import com.tecside.appEvent.errors.ErrorMessages;
import com.tecside.appEvent.models.Category;
import com.tecside.appEvent.models.User;
import com.tecside.appEvent.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {


    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/")
    public ResponseEntity<?> createCategory(@RequestBody Category newCategory){

        try {

           Optional<Category> category= categoryService.createCategory(newCategory);
            return  new ResponseEntity<>(category.get(), HttpStatus.CREATED);

        }catch (DataIntegrityViolationException e){
            return new ResponseEntity<>(ErrorMessages.errorJSON(e.getMessage()), HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            System.out.println(e.toString());
            return new ResponseEntity<>(ErrorMessages.errorJSON(ErrorMessages.GENERAL_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

//    @PostMapping("/image/upload")
//    public ResponseEntity<?> uploadImage(@@RequestParam("file")MultipartFile file){
//
//        try {
//
//            Optional<Category> category= categoryService.createCategory(newCategory);
//            return  new ResponseEntity<>(category.get(), HttpStatus.CREATED);
//
//        }catch (DataIntegrityViolationException e){
//            return new ResponseEntity<>(ErrorMessages.errorJSON(e.getMessage()), HttpStatus.BAD_REQUEST);
//        }catch (Exception e){
//            System.out.println(e.toString());
//            return new ResponseEntity<>(ErrorMessages.errorJSON(ErrorMessages.GENERAL_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
//
//        }

//    }

//    @PutMapping("/categoryId}")
//    public ResponseEntity<?> updateUser(@PathVariable("categoryId") Long categoryId, @RequestBody Category category) {
//
//
//        try {
//
//            userService.updateUser(userId, user);
//
//            return new ResponseEntity<>(user, HttpStatus.OK);
//
//        } catch (DataIntegrityViolationException e) {
//
//            HttpStatus httpStatus = (e.getMessage().equals(ErrorMessages.USER_NOT_FOUND))? HttpStatus.NOT_FOUND: HttpStatus.BAD_REQUEST;
//
//            return new ResponseEntity<>(ErrorMessages.errorJSON(e.getMessage()),httpStatus);
//        } catch (Exception e) {
//            System.out.println(e.toString());
//            return new ResponseEntity<>(ErrorMessages.errorJSON(ErrorMessages.GENERAL_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }
}
