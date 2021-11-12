/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.commons.context;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.bravo.logger.commons.model.TableMetadata;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hejianbing
 * @version @Id: RequestMethodContext.java, v 0.1 2021年10月27日 00:51 hejianbing Exp $
 */
@Setter
@Getter
@EqualsAndHashCode(of = { "traceId" })
public class RequestScopeContext {

    private String                          traceId;

    private String                          spanId;

    private String                          parentId;

    private String                          ip;

    private Map<String, TableMetadata> tableMeta = new ConcurrentHashMap<>();

    public String getPrimaryKey(String tableName) {
        TableMetadata tableMetadata = tableMeta.get(tableName);
        if (null ==tableMetadata) {
            return null;
        }
        return tableMetadata.getPrimaryKey();
    }

    public String getSchema() {
        if (CollectionUtils.isNotEmpty(tableMeta.values())) {
            String key = tableMeta.keySet().stream().findFirst().get();
            return tableMeta.get(key).getSchema();
        }
        return null;
    }
}