package com.readiness.online_shop.controller;

import com.readiness.online_shop.dto.request.ItemRequestDTO;
import com.readiness.online_shop.model.Item;
import com.readiness.online_shop.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    ItemService itemService;
    @GetMapping
    public ResponseEntity<Page<Item>> getItem(
            @RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String name
    ) throws Exception{
        return ResponseEntity.status(HttpStatus.OK).body(itemService.getItem(pageNumber, pageSize, name));
    }

    @PostMapping
    public ResponseEntity<String> addItem (@Valid @RequestBody ItemRequestDTO itemRequestDTO) throws Exception{
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.addItem(itemRequestDTO));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<String> editItem (@PathVariable Long itemId, @Valid @RequestBody ItemRequestDTO itemRequestDTO) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(itemService.editItem(itemId, itemRequestDTO));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<String> deleteItem (@PathVariable Long itemId) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(itemService.deleteItem(itemId));
    }
}
