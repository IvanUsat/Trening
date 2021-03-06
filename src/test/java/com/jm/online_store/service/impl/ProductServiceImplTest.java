package com.jm.online_store.service.impl;

import com.jm.online_store.exception.EmailAlreadyExistsException;
import com.jm.online_store.exception.ProductNotFoundException;
import com.jm.online_store.model.Product;
import com.jm.online_store.repository.ProductRepository;
import com.jm.online_store.service.interf.CommonSettingsService;
import com.jm.online_store.service.interf.EvaluationService;
import com.jm.online_store.service.interf.MailSenderService;
import com.jm.online_store.service.interf.ProductService;
import com.jm.online_store.service.interf.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
class ProductServiceImplTest {
    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final EvaluationService evaluationService = mock(EvaluationService.class);
    private final UserService userService = mock(UserService.class);
    private final MailSenderService mailSenderService = mock(MailSenderService.class);
    private final CommonSettingsService commonSettingsService = mock(CommonSettingsService.class);
    private final ProductService productService = new ProductServiceImpl(productRepository, evaluationService, userService, commonSettingsService, mailSenderService);
    private Product product;
    private Set<String> subscribers;
    private Map<LocalDateTime, Double> prices;

    @BeforeEach
    void init() {
        product = new Product();
        subscribers = new HashSet<>();
        prices = new HashMap<>();
        subscribers.add("user@mail.ru");
        prices.put(LocalDateTime.now(),150D);
        product.setId(1L);
        product.setProduct("Ledger");
        product.setPrice(150D);
        product.setChangePriceHistory(prices);
        product.setPriceChangeSubscribers(subscribers);
        log.info("startup");
    }

    @Test
    void findProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(product));
        Optional<Product> optionalProduct = productService.findProductById(1L);
        assertNotNull(optionalProduct);
        verify(productRepository, times(1)).findById(product.getId());
    }

    @Test
    void findProductByName() {
        when(productRepository.findByProduct(product.getProduct())).thenReturn(Optional.ofNullable(product));
        Optional<Product> optionalProduct = productService.findProductByName(product.getProduct());
        assertNotNull(optionalProduct);
        verify(productRepository, times(1)).findByProduct(product.getProduct());
    }

    @Test
    void saveProduct() {
        when(productRepository.save(product)).thenReturn(product);
        Long id = productService.saveProduct(product);
        assertNotNull(id);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void deleteProduct() {
        when(productRepository.getOne(1L)).thenReturn(product);
        productService.deleteProduct(product.getId());
        assertEquals(true, product.getDeleteStatus());
        verify(productRepository, times(1)).save(product);
    }

    /**
     * ?????????? ???????????????????????? ???????????????????? ?????????? ?????????????????????? ???? ?????????????????? ????????
     */
    @Test
    void addNewSubscriberTest() {
        assertFalse(productService.addNewSubscriber(any(),"wrongMail"));

        when(productService.findProductById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenReturn(product);
        assertTrue(productService.addNewSubscriber(1L,"correct@mail.ru"));
    }

    /**
     * ????????, ???????? ?????? ?????????? ???????????????????? ?????????? ?????????????????????? ???????????? ????????????????????
     */
    @Test
    void addNewSubscriberThrowsExceptionTest() {
        when(productService.findProductById(any())).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.addNewSubscriber(any(),"correct@mail.ru"));

        when(productService.findProductById(any())).thenReturn(Optional.of(product));
        assertThrows(EmailAlreadyExistsException.class, () -> productService.addNewSubscriber(any(),"user@mail.ru"));
    }

    /**
     * ???????? ????????, ?????? ?????????? ?????????????????? ???????????????? ?????????????? ????????????????????
     */
    @Test
    void editProductThrowsExceptionTest() {
        when(productService.findProductById(1L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.editProduct(product));
    }

    /**
     * ???????? ???????????? ?????????????????? ????????????
     */
    @Test
    void editProductTest() {
        Product updateProduct = new Product();
        updateProduct.setId(1L);
        updateProduct.setPrice(130D);
        prices.put(LocalDateTime.now(),130D);
        when(productService.findProductById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenReturn(1L);
        assertEquals(product.getPriceChangeSubscribers(),subscribers);
        assertEquals(product.getChangePriceHistory(),prices);
    }

    @AfterEach
    void after() {
        product = null;
        log.info("finalize");
    }
}