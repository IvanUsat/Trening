package com.jm.online_store.service.impl;

import com.jm.online_store.exception.SubBasketNotFoundException;
import com.jm.online_store.model.Product;
import com.jm.online_store.model.SubBasket;
import com.jm.online_store.model.User;
import com.jm.online_store.repository.BasketRepository;
import com.jm.online_store.service.interf.BasketService;
import com.jm.online_store.service.interf.ProductService;
import com.jm.online_store.service.interf.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BasketServiceImplTest {

    @MockBean
    private BasketRepository basketRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private ProductService productService;

    @Autowired
    private BasketService basketService;

    List<SubBasket> subBasketList;
    User user;
    SubBasket subBasket_1;
    SubBasket subBasket_2;
    SubBasket subBasket_3;
    Product product_1;
    Product product_2;
    Product product_3;

    @BeforeEach
    public void init() {
        user = new User();
        user.setId(1L);
        user.setFirstName("Ivan");
        user.setLastName("Petrov");
        user.setEmail("ivan@mail.ru");
        product_1 = new Product("apple", 100000D, 4, 0.1);
        product_2 = new Product("samsung", 80000D, 12, 0.9);
        product_3 = new Product("xiaomi", 30000D, 0, 0.5);

        subBasket_1 = new SubBasket();
        subBasket_1.setId(1L);
        subBasket_1.setProduct(product_1);
        subBasket_1.setCount(5);

        subBasket_2 = new SubBasket();
        subBasket_2.setId(2L);
        subBasket_2.setProduct(product_2);
        subBasket_2.setCount(2);

        subBasket_3 = new SubBasket();
        subBasket_3.setId(3L);
        subBasket_3.setProduct(product_3);
        subBasket_3.setCount(1);

        subBasketList = new ArrayList<>();
        subBasketList.add(subBasket_1);
        subBasketList.add(subBasket_2);
        subBasketList.add(subBasket_3);
        user.setUserBasket(subBasketList);
    }

    @Test
    void findBasketById() {
        when(basketRepository.findById(1L)).thenReturn(Optional.ofNullable(subBasket_1));
        SubBasket subBasket = basketService.findBasketById(1L);
        assertNotNull(subBasket);
        assertEquals(1L, subBasket.getId());
        verify(basketRepository, times(1)).findById(1L);
    }

    @Test
    void findBasketByIdThrowException() {
        when(basketRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(SubBasketNotFoundException.class, () -> basketService.findBasketById(1L));
        verify(basketRepository, times(1)).findById(1L);
    }

    @Test
    void getBasket() {
        when(userService.getCurrentLoggedInUser()).thenReturn(user);
        when(productService.findProductById(subBasket_1.getProduct().getId()))
                .thenReturn(Optional.ofNullable(product_1));
        List<SubBasket> basketList = basketService.getBasket();
        assertEquals(3, basketList.size());
        assertEquals(basketList.get(0), subBasket_1);
        assertEquals(basketList.get(1), subBasket_2);
        assertEquals(basketList.get(2), subBasket_3);
        assertEquals(subBasket_1.getCount(), 4);
    }

    @Test
    void updateBasket() {
        when(basketRepository.saveAndFlush(subBasket_1)).thenReturn(subBasket_1);
        SubBasket subBasket = basketService.updateBasket(subBasket_1, 1);
        assertEquals(subBasket.getCount(), 4);
        verify(basketRepository, times(1)).saveAndFlush(subBasket_1);
    }

    @Test
    void addBasket() {
        when(basketRepository.save(subBasket_3)).thenReturn(subBasket_3);
        SubBasket subBasketAdd = basketService.addBasket(subBasket_3);
        assertNotNull(subBasketAdd);
        assertNotNull(subBasketAdd.getProduct());
        assertEquals(1, subBasketAdd.getCount());
        verify(basketRepository, times(1)).save(subBasket_3);
    }

    @Test
    void deleteBasket() {
        when(userService.getCurrentLoggedInUser()).thenReturn(user);
        basketService.deleteBasket(subBasket_2);
        verify(basketRepository, times(1)).delete(subBasket_2);
    }

    @Test
    void addProductToBasket() {
        when(userService.getCurrentLoggedInUser()).thenReturn(user);
        when(productService.findProductById(2L)).thenReturn(Optional.ofNullable(product_2));
        when(basketRepository.save(subBasket_2)).thenReturn(subBasket_2);
        basketService.addProductToBasket(2L);
        verify(basketRepository, times(1)).save(any());
    }

    @AfterEach
    void after() {
        subBasketList.clear();
        product_1 = null;
        product_2 = null;
        subBasket_1 = null;
        subBasket_2 = null;
        user = null;
    }
}
