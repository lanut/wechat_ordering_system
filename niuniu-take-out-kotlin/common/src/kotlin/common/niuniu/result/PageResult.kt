package common.niuniu.result

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.io.Serializable


data class PageResult (
    val total: Long, // 总记录数
    val records: List<*> // 当前页数据集合
) : Serializable
