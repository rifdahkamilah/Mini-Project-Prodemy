package com.prodemy.services;

import com.prodemy.entity.Product;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.prodemy.entity.UserEntity;
import com.prodemy.model.RequestEditUser;
import com.prodemy.model.UserDto;

import java.util.List;

public interface UserService extends UserDetailsService {
    public UserEntity save(UserDto registrationDto);
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
//    public User getUserById(long id);
//
//    public void saveUser(User user);
    public void editUser(UserEntity user, RequestEditUser userDto);
    List<Product> getAllProducts();

    Product getProductById(Long id);

    void addToCart(Long id);
}
