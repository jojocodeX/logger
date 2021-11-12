package org.bravo.logger.mybatis.mapped.sqlparser;

import lombok.Getter;
import lombok.Setter;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.WithItem;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.update.UpdateSet;
import org.bravo.logger.commons.enums.SqlCmd;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hejianbing
 * @Id: SqlMeta.java, v 0.1 2021年10月29日 14:08 hejianbing Exp $
 */
@Setter
@Getter
public class SqlMeta {

    private SqlCmd           sqlCmd;
    private String           tableName;
    private String           schemaName;
    private Table            table;
    private Expression       whereExpression;
    private int              whereSize;
    //只有update才需要
    private List<Column>     columns;
    private List<Expression> expressions;

    public SqlMeta(String sql, SqlCmd sqlCmd) throws JSQLParserException {
        this.sqlCmd = sqlCmd;
        if (sql == null || sql.length() <= 0) {
            return;
        }
        Statement statement = CCJSqlParserUtil.parse(sql);
        if (sqlCmd == SqlCmd.UPDATE) {
            Update updateStatement = (Update) statement;
            this.table = updateStatement.getTable();
            this.whereExpression = updateStatement.getWhere();

            List<Column> updateColumns = new ArrayList<>();
            for (UpdateSet updateSet : updateStatement.getUpdateSets()) {
                updateColumns.addAll(updateSet.getColumns());
            }
            this.columns = updateColumns;
            this.expressions = updateStatement.getExpressions();
        } else if (sqlCmd == SqlCmd.INSERT) {
            Insert insertStatement = (Insert) statement;
            Table insertTable = insertStatement.getTable();
            if (insertTable == null) {
                return;
            }
            this.table = insertTable;
            this.whereExpression = null;
        } else if (sqlCmd == SqlCmd.DELETE) {
            Delete deleteStatement = (Delete) statement;
            Table deleteTables = deleteStatement.getTable();
            if (deleteTables == null) {
                return;
            }
            this.table = deleteTables;
            this.whereExpression = deleteStatement.getWhere();
        }

        if (this.whereExpression != null) {
            this.whereSize = (int) this.whereExpression.toString()
                    .chars().filter(ch -> ch == '?')
                    .count();
        }
		this.tableName = table.getName();
		this.schemaName = table.getSchemaName();
    }
}
