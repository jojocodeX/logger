package org.bravo.logger.mybatis.model;

import lombok.Getter;
import lombok.Setter;
/**
 * @author hejianbing
 * @Id: ColumnChangeData.java, v 0.1 2021年10月29日 14:08 hejianbing Exp $
 */
@Setter
@Getter
public class ColumnChangeData {
	
	public String name;
	public Object value;

	public ColumnChangeData(){}

	public ColumnChangeData(String name, Object value){
		this.name = name;
		this.value =value;
	}

}
