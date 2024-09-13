package com.test.msiglife.controller.v1;

import com.test.msiglife.model.Product;
import com.test.msiglife.repo.ProductRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class ControllerV1 {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    ProductRepo productRepo;

    @GetMapping(value = "/api/v1/products")
    public ResponseEntity<List<Product>> getAllFinalProducts() {
        log.info("==================== Get All Final Product ===================");
        try {
            List<Product> products = productRepo.findAllByStatus("Final");
            log.info("==================== Get All Final Product Success ===================");
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            log.error("==================== Get All Final Product Error Below ===================");
            log.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/api/v1/products")
    public ResponseEntity<String > requestNewProduct(@RequestBody Product product) {
        log.info("==================== Request New Product: {} ===================", product);
        try {
            product.setId(null);
            product.setProductId(null);
            product.setAction("Add");
            product.setStatus("Pending");
            productRepo.save(product);
            log.info("==================== Request New Product Success {} ===================", product);
            return new ResponseEntity<>("Success", HttpStatus.OK);
        } catch (Exception e) {
            log.error("==================== Request New Product Error {} ===================", product);
            log.error(e.getMessage());
            return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/api/v1/products/{productId}")
    public ResponseEntity<String > requestEditProduct(@PathVariable Integer productId, @RequestBody Product product) {
        log.info("==================== Request Edit Product: {} ===================", product);
        try {
            product.setId(null);
            product.setProductId(productId);
            product.setAction("Edit");
            product.setStatus("Pending");
            productRepo.save(product);
            log.info("==================== Request Edit Product Success {} ===================", product);
            return new ResponseEntity<>("Success", HttpStatus.OK);
        } catch (Exception e) {
            log.error("==================== Request Edit Product Error {} ===================", product);
            log.error(e.getMessage());
            return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/api/v1/products/{productId}")
    public ResponseEntity<String > requestDeleteProduct(@PathVariable Integer productId) {
        log.info("==================== Request Delete Product with Id: {} ===================", productId);
        try {
            Product product = productRepo.findById(productId).get();

            Product requestProduct = new Product();
            requestProduct.setId(null);
            requestProduct.setName(product.getName());
            requestProduct.setPrice(product.getPrice());
            requestProduct.setDescription(product.getDescription());
            requestProduct.setProductId(productId);
            requestProduct.setAction("Delete");
            requestProduct.setStatus("Pending");
            productRepo.save(requestProduct);
            log.info("==================== Request Delete Product with Id: {} Success ===================", productId);
            return new ResponseEntity<>("Success", HttpStatus.OK);
        } catch (Exception e) {
            log.error("==================== Request Delete Product with Id: {} Error ===================", productId);
            log.error(e.getMessage());
            return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/api/v1/products/pending")
    public ResponseEntity<List<Product>> getAllPendingProducts() {
        log.info("==================== Get All Pending Product ===================");
        try {
            List<Product> products = productRepo.findAllByStatus("Pending");
            log.info("==================== Get All Pending Product Success ===================");
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            log.error("==================== Get All Pending Product Error Below ===================");
            log.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/api/v1/products/{requestProductId}/approve")
    public ResponseEntity<String > approveRequestProduct(@PathVariable Integer requestProductId) {
        log.info("==================== Approve Request Product with Id: {} ===================", requestProductId);
        try {
            Product requestProduct = productRepo.findById(requestProductId).get();

            if (requestProduct.getAction().equals("Add")) {
                requestProduct.setStatus("Final");
                requestProduct.setAction(null);
                productRepo.save(requestProduct);
            } else if (requestProduct.getAction().equals("Edit")) {
                requestProduct.setStatus("Approved");

                Product product = productRepo.findById(requestProduct.getProductId()).get();
                product.setName(requestProduct.getName());
                product.setPrice(requestProduct.getPrice());
                product.setDescription(requestProduct.getDescription());

                productRepo.save(requestProduct);
                productRepo.save(product);

            } else if (requestProduct.getAction().equals("Delete")) {
                requestProduct.setStatus("Approved");

                Product product = productRepo.findById(requestProduct.getProductId()).get();
                product.setStatus("Deleted");

                productRepo.save(requestProduct);
                productRepo.save(product);

            }

            log.info("==================== Approve Request Product with Id: {} Success ===================", requestProductId);
            return new ResponseEntity<>("Success", HttpStatus.OK);
        } catch (Exception e) {
            log.error("==================== Approve Request Product with Id: {} Error ===================", requestProductId);
            log.error(e.getMessage());
            return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/api/v1/products/{requestProductId}/reject")
    public ResponseEntity<String > rejectRequestProduct(@PathVariable Integer requestProductId) {
        log.info("==================== Reject Request Product with Id: {} ===================", requestProductId);
        try {
            Product product = productRepo.findById(requestProductId).get();
            product.setStatus("Rejected");
            productRepo.save(product);
            log.info("==================== Reject Request Product with Id: {} Success ===================", requestProductId);
            return new ResponseEntity<>("Success", HttpStatus.OK);
        } catch (Exception e) {
            log.error("==================== Reject Request Product with Id: {} Error ===================", requestProductId);
            log.error(e.getMessage());
            return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
