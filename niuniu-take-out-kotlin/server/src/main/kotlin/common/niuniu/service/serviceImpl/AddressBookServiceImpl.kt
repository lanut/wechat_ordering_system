package common.niuniu.service.serviceImpl

import common.niuniu.context.BaseContext.currentId
import common.niuniu.mapper.AddressBookMapper
import common.niuniu.po.AddressBook
import common.niuniu.service.AddressBookService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AddressBookServiceImpl : AddressBookService {
    @Autowired
    private lateinit var addressBookMapper: AddressBookMapper

    /**
     * 新增地址
     * @param addressBook
     */
    override fun addAddress(addressBook: AddressBook?) {
        // 要先知道是哪个用户要新增地址，并且刚开始无法设置默认地址，需要在其他前端页面设置
        addressBook!!.userId = currentId
        addressBook.isDefault = 0
        addressBookMapper.insert(addressBook)
    }

    /**
     * 条件查询用户地址
     * @param addressBook
     * @return
     */
    override fun list(addressBook: AddressBook): List<AddressBook> {
        return addressBookMapper.getUserAddress(addressBook)
    }

    /**
     * 修改地址
     * @param addressBook
     */
    override fun updateAddress(addressBook: AddressBook?) {
        addressBookMapper.update(addressBook)
    }

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    override fun getById(id: Int?): AddressBook? {
        return addressBookMapper.getById(id)
    }

    /**
     * 设置默认地址
     * @param addressBook
     */
    override fun setDefault(addressBook: AddressBook?) {
        // 1、先把当前用户所有地址都设置成非默认地址
        addressBook!!.isDefault = 0
        addressBook.userId = currentId
        addressBookMapper.updateIsDefaultByUserId(addressBook)
        // 2、再把当前地址设置成默认地址
        addressBook.isDefault = 1
        addressBookMapper.update(addressBook)
    }

    /**
     * 根据id删除地址
     * @param id
     */
    override fun deleteById(id: Int?) {
        addressBookMapper.delete(id)
    }
}
