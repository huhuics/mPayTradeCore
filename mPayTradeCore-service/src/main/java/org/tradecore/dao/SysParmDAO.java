package org.tradecore.dao;


public interface SysParmDAO {
    
	/**
	 * 
	 * 方法描述 :根据code获取参数表中的value  
	 * @param parmCode
	 * @return String
	 */
    String getValue(String parmCode);
    
}