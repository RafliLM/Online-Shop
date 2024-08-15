package com.readiness.online_shop.service;

import com.readiness.online_shop.dto.request.AddCustomerRequestDTO;
import com.readiness.online_shop.dto.request.EditCustomerRequestDTO;
import com.readiness.online_shop.model.Customer;
import com.readiness.online_shop.repository.CustomerRepository;
import io.minio.*;
import io.minio.http.Method;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    MinioService minioService;

    public List<Customer> getCustomer() throws Exception {
        List<Customer> customers = customerRepository.findAll();
        for (int i = 0; i < customers.size(); i++){
            Customer customer = customers.get(i);
            customer.setPic(minioService.getImageUrl(customer.getPic()));
            customers.set(i, customer);
        }
        return customers;
    }

    public String addCustomer(AddCustomerRequestDTO addCustomerRequestDTO) throws Exception {
        MultipartFile file = addCustomerRequestDTO.getPic();
        if (file.isEmpty()){
            throw new BadRequestException("Column image_filename can't be empty");
        }
        List<String> imageContentType = new ArrayList<String>(
                Arrays.asList("image/jpg", "image/jpeg", "image/png")
        );
        if((!imageContentType.contains(file.getContentType())) || (file.getSize() > 1024 * 1024)){
            throw new BadRequestException("File should be an image");
        }
        if (file.getSize() > 1024 * 1024){
            throw new BadRequestException("File size can't be more than 1MB");
        }
        String imageFileName = addCustomerRequestDTO.getCustomerName() + "_" + (System.currentTimeMillis()/1000) + "." + FileNameUtils.getExtension(file.getOriginalFilename());
        Customer customer = Customer
                .builder()
                .customerName(addCustomerRequestDTO.getCustomerName())
                .customerAddress(addCustomerRequestDTO.getCustomerAddress())
                .customerPhone(addCustomerRequestDTO.getCustomerPhone())
                .pic(imageFileName)
                .isActive(true)
                .build();
        minioService.uploadFile(file, imageFileName);
        customerRepository.save(customer);
        return "Successfully added customer %s".formatted(customer.getCustomerName());
    }

    @Transactional
    public String editCustomer(Long customerId, EditCustomerRequestDTO editCustomerRequestDTO) throws Exception{
        Optional<Customer> findCustomer = customerRepository.findById(customerId);
        if(findCustomer.isEmpty())
            throw new EntityNotFoundException("customerId not found");
        MultipartFile file = editCustomerRequestDTO.getPic();
        Customer customer = findCustomer.get();
        String oldFileName = null;
        String imageFileName = null;
        if ((file != null) && (!file.isEmpty())){
            List<String> imageContentType = new ArrayList<String>(
                    Arrays.asList("image/jpg", "image/jpeg", "image/png")
            );
            if((!imageContentType.contains(file.getContentType())) || (file.getSize() > 1024 * 1024)){
                throw new BadRequestException("File should be an image");
            }
            if (file.getSize() > 1024 * 1024){
                throw new BadRequestException("File size can't be more than 1MB");
            }
            oldFileName = customer.getPic();
            imageFileName = editCustomerRequestDTO.getCustomerName() + "_" + (System.currentTimeMillis()/1000) + "." + FileNameUtils.getExtension(file.getOriginalFilename());
            customer.setPic(imageFileName);
        }
        customer.setCustomerName(editCustomerRequestDTO.getCustomerName());
        customer.setCustomerAddress(editCustomerRequestDTO.getCustomerAddress());
        customer.setCustomerPhone(editCustomerRequestDTO.getCustomerPhone());
        customer.setIsActive(editCustomerRequestDTO.getIsActive());
        if(oldFileName != null)
            minioService.deleteFile(oldFileName);
        if(imageFileName != null)
            minioService.uploadFile(file, imageFileName);
        customerRepository.save(customer);
        return "Successfully updated customer %s".formatted(customer.getCustomerName());
    }

    public String deleteCustomer(Long customerId) throws Exception{
        Optional<Customer> findCustomer = customerRepository.findById(customerId);
        if(findCustomer.isEmpty())
            throw new EntityNotFoundException("customerId not found");
        Customer customer = findCustomer.get();
        customerRepository.delete(customer);
        minioService.deleteFile(customer.getPic());
        return "Successfully deleted customer %s".formatted(customer.getCustomerName());
    }
}
