package common.niuniu.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishFlavor implements Serializable {

    private Integer id;
    private String name;
    private String list;
    private Integer dishId;

}
