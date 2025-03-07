package com.example.mobilestore.entity;

import jakarta.persistence.*;

import java.nio.MappedByteBuffer;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "cart_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

//    @ManyToMany
//    @JoinTable(name= "cart_item",
//            joinColumns = @JoinColumn(name = "cart_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name="product_id", referencedColumnName = "id"))
//    private Set<Product> productSet = new HashSet<>();

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems = new HashSet<>();

    private Date cartOrder;

    private float total;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCartOrder() {
        return cartOrder;
    }

    public void setCartOrder(Date cartOrder) {
        this.cartOrder = cartOrder;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public Set<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Set<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
}
