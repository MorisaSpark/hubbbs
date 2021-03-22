package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: HB
 * Date: 2018/11/28
 *
 * @author HB
 * To change this template use File | Settings | File Templates.
 */
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("serial")
@Data
public class PageResult<T> {
    private Long total;
    private List<T> rows;
}
