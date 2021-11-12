/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.store;
import org.bravo.logger.core.context.LoggerInfo;

/**
 * 存储定位器
 * @author hejianbing
 * @version @Id: StorageLocator.java, v 0.1 2021年10月24日 23:41 hejianbing Exp $
 */
public interface StorageLocator {

    void locate(LoggerInfo logInfo);
}