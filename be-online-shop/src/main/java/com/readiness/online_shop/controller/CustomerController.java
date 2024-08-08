package com.readiness.online_shop.controller;

import com.readiness.online_shop.dto.request.AddCustomerRequestDTO;
import com.readiness.online_shop.dto.request.EditCustomerRequestDTO;
import com.readiness.online_shop.model.Customer;
import com.readiness.online_shop.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<Customer>> getCustomer() throws Exception{
        return ResponseEntity.status(HttpStatus.OK).body(customerService.getCustomer());
    }

    @PostMapping
    public ResponseEntity<String> addCustomer (@Valid @ModelAttribute AddCustomerRequestDTO customerRequestDTO) throws Exception{
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.addCustomer(customerRequestDTO));
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<String> editCustomer (@PathVariable Long customerId, @Valid @ModelAttribute EditCustomerRequestDTO editCustomerRequestDTO) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.editCustomer(customerId, editCustomerRequestDTO));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<String> deleteCustomer (@PathVariable Long customerId) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.deleteCustomer(customerId));
    }
}
