package common.niuniu.service

import common.niuniu.dto.CartDTO
import common.niuniu.po.Cart

interface CartService {
    fun add(cartDTO: CartDTO?)

    val list: List<Cart>?

    fun clean()

    fun sub(cartDTO: CartDTO?)
}
