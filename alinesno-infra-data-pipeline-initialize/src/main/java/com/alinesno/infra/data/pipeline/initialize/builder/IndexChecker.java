//package com.alinesno.infra.data.pipeline.initialize.builder;
//
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//
//import javax.sql.DataSource;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class IndexChecker {
//
//    private final NamedParameterJdbcTemplate jdbcTemplate;
//    private final String tableSchema;
//
//    public IndexChecker(DataSource dataSource , String tableSchema) {
//        this.jdbcTemplate = new NamedParameterJdbcTemplate(new JdbcTemplate(dataSource));
//        this.tableSchema = tableSchema;
//    }
//
//    public List<String> getNonExistentIndexes(String tableName, List<String> indexNames) {
//        List<String> nonExistentIndexes = new ArrayList<>();
//
//        for (String indexName : indexNames) {
//            String sql = "SELECT COUNT(*) AS count " +
//                    "FROM information_schema.statistics " +
//                    "WHERE table_name = :tableName " +
//                    "AND table_schema = :table_schema " +
//                    "AND index_name = :indexName";
//
//            MapSqlParameterSource params = new MapSqlParameterSource()
//                    .addValue("tableName", tableName)
//                    .addValue("table_schema", tableSchema)
//                    .addValue("indexName", indexName);
//
//            Map<String, Object> result = jdbcTemplate.queryForMap(sql, params);
//
//            if ((Long) result.get("count") == 0L) {
//                nonExistentIndexes.add(indexName);
//            }
//        }
//
//        return nonExistentIndexes;
//    }
//
//    public void createIndexesIfNotExists(String tableName, HashMap<String, String> indexColumnPairs) {
//        List<String> nonExistentIndexes = getNonExistentIndexes(tableName, new ArrayList<>(indexColumnPairs.keySet()));
//
//        for (Map.Entry<String, String> entry : indexColumnPairs.entrySet()) {
//            String indexName = entry.getKey();
//            String columnName = entry.getValue();
//
//            if (nonExistentIndexes.contains(indexName)) {
//                String sql = "CREATE INDEX `" + indexName + "` ON `" + tableName + "` (" + columnName + ")";
//
//                jdbcTemplate.execute(sql, ps -> {
//                            ps.execute();
//                            ps.getResultSet();
//                            System.out.println("创建索引成功");
//                            return null ;
//                        }
//                );
//            }
//        }
//    }
//}