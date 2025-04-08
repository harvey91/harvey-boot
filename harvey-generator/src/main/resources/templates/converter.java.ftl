package ${package.Parent}.mapstruct;

import com.harvey.core.mapstruct.IConverter;
import ${package.Parent}.model.dto.${entity}Dto;
import ${package.Parent}.model.entity.${entity};
import ${package.Parent}.model.vo.${entity}VO;
import org.mapstruct.Mapper;

/**
* ${table.comment!} 转换类
*
* @author ${author}
* @since ${date}
*/
@Mapper(componentModel = "spring")
public interface ${entity}Converter extends IConverter<${entity}, ${entity}Dto, ${entity}VO> {

}
