package Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel(value = "返回结果对象",discriminator = "结果的返回对象，包含状态码，信息提示，返回数据类型")
public class ResultVo {
    @ApiModelProperty(value = "状态码",required = true)
    private int code;

    @ApiModelProperty(value = "信息提示",required = true)
    private String msg;

    @ApiModelProperty(value = "返回数据",required = true)
    private Object data;
}
