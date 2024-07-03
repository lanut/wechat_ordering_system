package common.niuniu.service

import common.niuniu.po.AddressBook

interface AddressBookService {
    fun addAddress(addressBook: AddressBook?)

    fun list(addressBook: AddressBook): List<AddressBook>

    fun updateAddress(addressBook: AddressBook?)

    fun getById(id: Int?): AddressBook?

    fun setDefault(addressBook: AddressBook?)

    fun deleteById(id: Int?)
}
