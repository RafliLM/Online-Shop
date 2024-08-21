package com.readiness.online_shop.service;

import com.readiness.online_shop.dto.request.ItemRequestDTO;
import com.readiness.online_shop.model.Item;
import com.readiness.online_shop.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;

    public Page<Item> getItem(Integer pageNumber, Integer pageSize, String name) {
        Pageable pageable;
        if (pageNumber != null && pageSize != null) {
            pageable = PageRequest.of(pageNumber, pageSize);
        }
        else
            pageable = Pageable.unpaged();
        Page<Item> itemPage;
        if(name == null){
            itemPage = itemRepository.findAll(pageable);
        }
        else{
            itemPage = itemRepository.findByItemNameContaining(name, pageable);
        }
        return itemPage;
    }

    public String addItem(ItemRequestDTO itemRequestDTO) {
        Item item = Item
                .builder()
                .itemName(itemRequestDTO.getItemName())
                .price(itemRequestDTO.getPrice())
                .stock(itemRequestDTO.getStock())
                .isAvailable(itemRequestDTO.getIsAvailable())
                .build();
        itemRepository.save(item);
        return "Successfully added item %s".formatted(item.getItemName());
    }

    public String editItem(Long itemId, ItemRequestDTO itemRequestDTO) {
        Optional<Item> findItem = itemRepository.findById(itemId);
        if(findItem.isEmpty())
            throw new EntityNotFoundException("itemId not found");
        Item item = findItem.get();
        item.setItemName(itemRequestDTO.getItemName());
        item.setPrice(itemRequestDTO.getPrice());
        if(item.getStock() < itemRequestDTO.getStock()){
            item.setLastReStock(new Date());
        }
        item.setStock(itemRequestDTO.getStock());
        item.setIsAvailable(itemRequestDTO.getIsAvailable());
        itemRepository.save(item);
        return "Successfully updated item %s".formatted(item.getItemName());
    }

    public String deleteItem(Long itemId) {
        Optional<Item> findItem = itemRepository.findById(itemId);
        if(findItem.isEmpty())
            throw new EntityNotFoundException("itemId not found");
        Item item = findItem.get();
        itemRepository.delete(item);
        return "Successfully deleted item %s".formatted(item.getItemName());
    }
}
