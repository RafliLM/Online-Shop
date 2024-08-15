package com.readiness.online_shop.controller;

import com.readiness.online_shop.dto.request.AddOrderRequestDTO;
import com.readiness.online_shop.dto.request.EditOrderRequestDTO;
import com.readiness.online_shop.model.Order;
import com.readiness.online_shop.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    OrderService orderService;
    
    @GetMapping
    public ResponseEntity<List<Order>> getOrder() throws Exception{
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrder());
    }

    @PostMapping
    public ResponseEntity<String> addOrder (@Valid @RequestBody AddOrderRequestDTO orderRequestDTO) throws Exception{
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.addOrder(orderRequestDTO));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<String> editOrder (@PathVariable Long orderId, @Valid @RequestBody EditOrderRequestDTO editOrderRequestDTO) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.editOrder(orderId, editOrderRequestDTO));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder (@PathVariable Long orderId) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.deleteOrder(orderId));
    }

    @GetMapping("/generate-report")
    public ResponseEntity<Resource> downloadReport() throws Exception{
        byte[] reportContent = orderService.getOrderReport();
        ByteArrayResource resource = new ByteArrayResource(reportContent);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename("order-report" + new Date() + ".pdf").build().toString())
                .body(resource);
    }
}
