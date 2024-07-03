package common.niuniu.controller.user

import common.niuniu.annotation.Slf4j
import common.niuniu.annotation.Slf4j.Companion.log
import common.niuniu.context.BaseContext.currentId
import common.niuniu.po.AddressBook
import common.niuniu.result.Result
import common.niuniu.service.AddressBookService

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user/address")
@Slf4j
class AddressBookController {
    @Autowired
    private val addressBookService: AddressBookService? = null

    /**
     * 新增地址
     * @return
     */
    @PostMapping
    fun addAddress(@RequestBody addressBook: AddressBook?): Result<*> {
        log.info("新增地址，用户传过来的地址信息：{}", addressBook)
        addressBookService!!.addAddress(addressBook)
        return Result.success()
    }

    /**
     * 根据当前用户查询地址列表
     * @return
     */
    @GetMapping("/list")
    fun list(): Result<List<AddressBook>> {
        log.info("查询当前用户的地址列表")
        val addressBook = AddressBook()
        addressBook.userId = currentId
        val addressBookList:List<AddressBook> = addressBookService!!.list(addressBook)
        return Result.success(addressBookList)
    }

    /**
     * 查询默认地址
     * @return
     */
    @GetMapping("/default")
    fun defaultAddress(): Result<AddressBook> {
        log.info("查询默认地址")
        val addressBook = AddressBook()
        addressBook.userId = currentId
        addressBook.isDefault = 1
        val defaultAddress = addressBookService?.list(addressBook)
        if (defaultAddress != null && defaultAddress.size == 1) {
            return Result.success(defaultAddress[0])
        }
        return Result.error("没有查询到默认地址")
    }

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Int?): Result<AddressBook> {
        log.info("根据id查询地址：{}", id)
        val addressBook = addressBookService!!.getById(id)!!
        return Result.success(addressBook)
    }

    /**
     * 根据id修改地址
     * @param addressBook
     * @return
     */
    @PutMapping
    fun updateAddress(@RequestBody addressBook: AddressBook?): Result<*> {
        log.info("根据id查询地址，新地址信息为：{}", addressBook)
        addressBookService!!.updateAddress(addressBook)
        return Result.success()
    }

    /**
     * 设置默认地址
     * @return
     */
    @PutMapping("/default")
    fun setDefaultAddress(@RequestBody addressBook: AddressBook?): Result<*> {
        log.info("设置默认地址：{}", addressBook)
        addressBookService!!.setDefault(addressBook)
        return Result.success()
    }

    /**
     * 根据id删除地址
     * @return
     */
    @DeleteMapping("/{id}")
    fun deleteAddress(@PathVariable id: Int?): Result<*> {
        log.info("要删除的地址id:{}", id)
        addressBookService!!.deleteById(id)
        return Result.success()
    }
}
