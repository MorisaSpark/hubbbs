package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Created by IntelliJ IDEA.
 * User: HB
 * Date: 2018/11/27
 *
 * @author HB
 * To change this template use File | Settings | File Templates.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuppressWarnings("serial")
@Accessors(chain = true)
public class Result {
    private boolean flag;//是否成功
    private Integer code;// 返回码
    private String message;//返回信息
    private Object data;// 返回数据
    
    public Result(boolean flag, Integer code, String message) { super();
        this.flag = flag;
        this.code = code;
        this.message = message;
    }

    public boolean isFlag() {
        return flag;
    }
}
