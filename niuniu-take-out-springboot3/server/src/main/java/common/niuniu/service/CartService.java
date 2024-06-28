package common.niuniu.service;

import common.niuniu.dto.CartDTO;
import common.niuniu.entity.Cart;

import java.util.List;

public interface CartService {
    void add(CartDTO cartDTO);

    List<Cart> getList();

    void clean();

    void sub(CartDTO cartDTO);
}
