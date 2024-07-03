package common.niuniu.mapper

import common.niuniu.po.AddressBook
import org.apache.ibatis.annotations.*

@Mapper
interface AddressBookMapper {
    @Insert(
        "insert into address_book" +
                "        (user_id, consignee, phone, gender, province_code, province_name, city_code, city_name, district_code," +
                "         district_name, detail, label, is_default)" +
                "        values (#{userId}, #{consignee}, #{phone}, #{gender}, #{provinceCode}, #{provinceName}, #{cityCode}, #{cityName}," +
                "                #{districtCode}, #{districtName}, #{detail}, #{label}, #{isDefault})"
    )
    fun insert(addressBook: AddressBook?)

    fun getUserAddress(addressBook: AddressBook?): List<AddressBook>

    fun update(addressBook: AddressBook?)

    @Update("update address_book set is_default = #{isDefault} where user_id = #{userId}")
    fun updateIsDefaultByUserId(addressBook: AddressBook?)

    @Select("select * from address_book where id = #{id}")
    fun getById(id: Int?): AddressBook?

    @Delete("delete from address_book where id = #{id}")
    fun delete(id: Int?)
}
