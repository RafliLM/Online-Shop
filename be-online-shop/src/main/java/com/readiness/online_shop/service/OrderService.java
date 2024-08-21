package com.readiness.online_shop.service;

import com.readiness.online_shop.dto.request.AddOrderRequestDTO;
import com.readiness.online_shop.dto.request.EditOrderRequestDTO;
import com.readiness.online_shop.model.Customer;
import com.readiness.online_shop.model.Item;
import com.readiness.online_shop.model.Order;
import com.readiness.online_shop.repository.CustomerRepository;
import com.readiness.online_shop.repository.ItemRepository;
import com.readiness.online_shop.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.*;

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

    public Page<Order> getOrder(int pageNumber, int pageSize, String name) throws Exception{
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Order> orderPage = orderRepository.findAll(new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (name != null) {
                    predicates.add(criteriaBuilder.like(root.get("customer").get("customerName"), "%"+name+"%"));
                    predicates.add(criteriaBuilder.like(root.get("item").get("itemName"), "%"+name+"%"));
                    return criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]));
                }
                return null;
            }
        }, pageable);
        return orderPage.map(order -> {
            Customer customer = order.getCustomer();
            try {
                customer.setPic(minioService.getImageUrl(customer.getPic()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            order.setCustomer(customer);
            return order;
        });
    }

    @Transactional
    public String addOrder(AddOrderRequestDTO orderRequestDTO) throws Exception{
        Optional<Customer> findCustomer = customerRepository.findById(orderRequestDTO.getCustomerId());
        if(findCustomer.isEmpty())
            throw new EntityNotFoundException("customerId not found");
        Optional<Item> findItem = itemRepository.findById(orderRequestDTO.getItemId());
        if(findItem.isEmpty())
            throw new EntityNotFoundException("itemId not found");
        Customer customer = findCustomer.get();
        Item item = findItem.get();
        if(!item.getIsAvailable())
            throw new IllegalStateException("Can't order unavailable item");
        if(!customer.getIsActive())
            throw new IllegalStateException("Inactive customer can't make an order");
        if(orderRequestDTO.getQuantity() > item.getStock())
            throw new BadRequestException("Can't order more than available stock");
        Long totalPrice = orderRequestDTO.getQuantity() * item.getPrice();
        customer.setLastOrderDate(new Date());
        customerRepository.save(customer);
        item.setStock(item.getStock() - orderRequestDTO.getQuantity());
        itemRepository.save(item);
        Order order = Order
                .builder()
                .customer(customer)
                .item(item)
                .quantity(orderRequestDTO.getQuantity())
                .totalPrice(totalPrice)
                .orderDate(new Date())
                .build();
        orderRepository.save(order);
        return "Successfully added an order for item %s for customer %s".formatted(item.getItemName(), customer.getCustomerName());
    }

    @Transactional
    public String editOrder(Long orderId, EditOrderRequestDTO editOrderRequestDTO) throws Exception{
        Optional<Order> findOrder = orderRepository.findById(orderId);
        if(findOrder.isEmpty())
            throw new EntityNotFoundException("orderId not found");
        Optional<Item> findItem = itemRepository.findById(editOrderRequestDTO.getItemId());
        if(findItem.isEmpty())
            throw new EntityNotFoundException("itemId not found");
        Order order = findOrder.get();
        Item oldItem = order.getItem();
        Customer customer = order.getCustomer();
        Item item = findItem.get();
        if(oldItem.equals(item)){
            if(editOrderRequestDTO.getQuantity() > item.getStock() + order.getQuantity())
                throw new BadRequestException("Can't order more than available stock");
            item.setStock(item.getStock() + order.getQuantity() - editOrderRequestDTO.getQuantity());
            order.setQuantity(editOrderRequestDTO.getQuantity());
            order.setTotalPrice(editOrderRequestDTO.getQuantity() * item.getPrice());
            itemRepository.save(item);
            orderRepository.save(order);
            return "Successfully updated order %s".formatted(order.getOrderCode());
        }
        oldItem.setStock(oldItem.getStock() + order.getQuantity());
        itemRepository.save(oldItem);
        if(!item.getIsAvailable())
            throw new IllegalStateException("Can't order unavailable item");
        if(!customer.getIsActive())
            throw new IllegalStateException("Inactive customer can't make an order");
        if(editOrderRequestDTO.getQuantity() > item.getStock())
            throw new BadRequestException("Can't order more than available stock");
        customer.setLastOrderDate(new Date());
        customerRepository.save(customer);
        item.setStock(item.getStock() - editOrderRequestDTO.getQuantity());
        itemRepository.save(item);
        order.setCustomer(customer);
        order.setItem(item);
        order.setQuantity(editOrderRequestDTO.getQuantity());
        order.setTotalPrice(editOrderRequestDTO.getQuantity() * item.getPrice());
        orderRepository.save(order);
        return "Successfully updated order %s".formatted(order.getOrderCode());
    }

    @Transactional
    public String deleteOrder(Long orderId){
        Optional<Order> findOrder = orderRepository.findById(orderId);
        if(findOrder.isEmpty())
            throw new EntityNotFoundException("orderId not found");
        Order order = findOrder.get();
        Item item = order.getItem();
        item.setStock(item.getStock() + order.getQuantity());
        itemRepository.save(item);
        orderRepository.delete(order);
        return "Successfully deleted order %s".formatted(order.getOrderCode());
    }

    public byte[] getOrderReport() throws Exception{
        File file = ResourceUtils.getFile("src/main/resources/report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        List<Order> orders = orderRepository.findAll();
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(orders);
        Map<String, Object> parameters = new HashMap<>();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
