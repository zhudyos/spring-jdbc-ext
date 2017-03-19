package io.zhudy.spring.jdbc.parser;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kevin Zou <kevinz@weghst.com>
 */
public class CountSqlParser {

    private static final boolean CACHE_ENABLED = Boolean.valueOf(
            System.getProperty("io.zhudy.spring.jdbc.countSql.cacheEnabled", "true"));

    private static final List<SelectItem> COUNT_SELECT_ITEMS = new ArrayList<SelectItem>(1) {
        {
            add(new SelectExpressionItem(new Column("count(1)")));
        }
    };


    private static final Map<Integer, String> CACHE_COUNT_SQL = new HashMap<>();

    /**
     * @param sql
     * @return
     * @throws JSQLParserException
     */
    public String parse(String sql) throws JSQLParserException {
        int k = sql.hashCode();
        String csql;
        if (CACHE_ENABLED && (csql = CACHE_COUNT_SQL.get(k)) != null) {
            return csql;
        }

        Statement stmt = CCJSqlParserUtil.parse(sql);
        SelectBody body = ((Select) stmt).getSelectBody();
        cleanSelect(body);

        if (body instanceof PlainSelect) {
            PlainSelect ps = (PlainSelect) body;
            ps.setSelectItems(COUNT_SELECT_ITEMS);

            csql = ps.toString();
        } else {
            PlainSelect ps = new PlainSelect();

            SubSelect ss = new SubSelect();
            ss.setSelectBody(body);

            ps.setFromItem(ss);
            ps.setSelectItems(COUNT_SELECT_ITEMS);

            csql = ps.toString();
        }

        CACHE_COUNT_SQL.put(k, csql);
        return csql;
    }

    private void cleanSelect(SelectBody sb) {
        if (sb instanceof PlainSelect) {
            ((PlainSelect) sb).setOrderByElements(null);
        } else if (sb instanceof WithItem) {
            WithItem wi = (WithItem) sb;
            if (wi.getSelectBody() != null) {
                cleanSelect(wi.getSelectBody());
            }
        } else {
            SetOperationList sol = (SetOperationList) sb;
            if (sol.getSelects() != null) {
                sol.getSelects().forEach(this::cleanSelect);
            }
            sol.setOrderByElements(null);
        }
    }


    public static void main(String[] args) throws JSQLParserException {
//        Statement stmt = CCJSqlParserUtil.parse("SELECT * FROM tab1 ORDER BY b");
//        Statement stmt = CCJSqlParserUtil.parse("select a,b from mytab1 union select c,b from mytab2");
//        Select select = (Select) stmt;
//
//        SelectBody selectBody = select.getSelectBody();
//        PlainSelect plainSelect = (PlainSelect) selectBody;
//
//        plainSelect.setSelectItems(COUNT_SELECT_ITEMS);
//        plainSelect.setOrderByElements(null);
//
//        System.out.println(plainSelect);
//
//        System.out.println(plainSelect.getOrderByElements());

        CountSqlParser csp = new CountSqlParser();
        System.out.println(csp.parse("SELECT * FROM tab1 ORDER BY b"));
        System.out.println(csp.parse("select a,b from mytab1 union select c,b from mytab2"));
    }

}
