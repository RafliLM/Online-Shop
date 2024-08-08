package com.readiness.online_shop.service;

import com.readiness.online_shop.dto.request.OrderRequestDTO;
import com.readiness.online_shop.model.Customer;
import com.readiness.online_shop.model.Item;
import com.readiness.online_shop.model.Order;
import com.readiness.online_shop.repository.CustomerRepository;
import com.readiness.online_shop.repository.ItemRepository;
import com.readiness.online_shop.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MinioService minioService;

    public List<Order> getOrder(){
        return orderRepository.findAll();
    }

    public String addOrder(OrderRequestDTO orderRequestDTO){
        Optional<Customer> findCustomer = customerRepository.findById(orderRequestDTO.getCustomerId());
        if(findCustomer.isEmpty())
            throw new EntityNotFoundException("customerId tidak terdaftar");
        Optional<Item> findItem = itemRepository.findById(orderRequestDTO.getItemId());
        if(findItem.isEmpty())
            throw new EntityNotFoundException("itemId tidak terdaftar");
        Customer customer = findCustomer.get();
        Item item = findItem.get();
        Long totalPrice = orderRequestDTO.getQuantity() * item.getPrice();
        Order order = Order
                .builder()
                .customer(customer)
                .item(item)
                .quantity(orderRequestDTO.getQuantity())
                .totalPrice(totalPrice)
                .build();
        orderRepository.save(order);
        return "Berhasil menambahkan order untuk item %s pada customer %s".formatted(item.getItemName(), customer.getCustomerName());
    }

    public String editOrder(Long orderId, OrderRequestDTO orderRequestDTO){
        Optional<Order> findOrder = orderRepository.findById(orderId);
        if(findOrder.isEmpty())
            throw new EntityNotFoundException("orderId tidak terdaftar");
        Order order = findOrder.get();
        return "Berhasil update data order %s".formatted(order.getOrderCode());
    }

    public String deleteOrder(Long orderId){
        Optional<Order> findOrder = orderRepository.findById(orderId);
        if(findOrder.isEmpty())
            throw new EntityNotFoundException("orderId tidak terdaftar");
        Order order = findOrder.get();
        orderRepository.delete(order);
        return "Berhasil menghapus order %s".formatted();
    }
}
