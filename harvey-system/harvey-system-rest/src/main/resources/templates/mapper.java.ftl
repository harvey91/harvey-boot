package ${packageName}.${moduleName}.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ${packageName}.${moduleName}.model.entity.${entityName};
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * ${tableComment} Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Mapper
public interface ${entityName}Mapper extends BaseMapper<${entityName}> {

}
