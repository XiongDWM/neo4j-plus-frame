package com.xiongdwm.framework.support.orm.helper.provider;

import com.xiongdwm.framework.graph.entities.P;
import com.xiongdwm.framework.graph.entities.relationship.R;
import com.xiongdwm.framework.support.io.JacksonUtil;
import com.xiongdwm.framework.support.orm.entity.iInterfaces.DynamicPrinciple;
import com.xiongdwm.framework.support.orm.helper.AbstractCypherHelper;
import com.xiongdwm.framework.support.orm.helper.exception.Neo4jCypherBuildException;
import com.xiongdwm.framework.support.orm.helper.exception.Neo4jCypherPKException;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.schema.Id;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class CypherHelper<T> extends AbstractCypherHelper<T> {

    //获取T的class对象
    @SuppressWarnings("unchecked")
    private Class<T> getTClass(){
        return (Class<T>) ((java.lang.reflect.ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
    public CypherHelper(Neo4jClient neo4jClient) {
        super(neo4jClient);
    }

    @Override
    public boolean createRelationship(Object start, Object end, T relationship,RelationshipDirection direction) {
        String relationshipType=relationship.getClass().getSimpleName().toUpperCase();
        String nodeLabel=start.getClass().getSimpleName();
        String fieldName=getIdFields(start);
        if(fieldName==null)throw new Neo4jCypherPKException("Entity "+start.getClass().getName()+" has no @Id annotation");
        Object startId=getPkValue(start,fieldName).isPresent()?getPkValue(start,fieldName).get():null;
        if(startId==null)throw new Neo4jCypherPKException("Start Node "+start.getClass().getName()+" has no primary key value");

        Object endId=getPkValue(end,fieldName).isPresent()?getPkValue(end,fieldName).get():null;
        if(endId==null)throw new Neo4jCypherPKException("End Node "+end.getClass().getName()+"has no primary key value");

        //如果startId和endId是字符串的时候要加上单引号
        if(startId instanceof String)startId= ("'"+startId+"'");
        if(endId instanceof String)endId= ("'"+endId+"'");

        String properties;
        try {
            properties=resolveRelationshipEntity(relationship);
        } catch (IllegalAccessException e) {
            throw new Neo4jCypherBuildException("Entity "+relationship.getClass().getName()+" has no properties");
        }
        if(null==properties||properties.equals(""))throw new Neo4jCypherBuildException("Entity "+relationship.getClass().getName()+" has no properties");
//        String cypher="MATCH(start:"+nodeLabel+"{"+fieldName+":"+startId+"})," +
//                "(end:"+nodeLabel+"{"+fieldName+":"+endId+"}) " +
//                "CREATE(start)-[r:"+relationshipType+"{"+properties+"}]->(end)";
        String cypher=null;
        if (direction==RelationshipDirection.DIRECTED) {
            cypher = cypherBuilder(nodeLabel,fieldName,startId,endId,relationshipType,properties, OperationType.CREATE, RelationshipDirection.DIRECTED);
        } else if(direction==RelationshipDirection.UNDIRECTED){
            cypher = cypherBuilder(nodeLabel,fieldName,startId,endId,relationshipType,properties, OperationType.CREATE, RelationshipDirection.UNDIRECTED);
        }
        try {
            assert cypher!=null;
            super.query(cypher);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int updateRelationship(Object start, Object end, T relationship) {
        String relationshipType=relationship.getClass().getSimpleName().toUpperCase();
        String nodeLabel=start.getClass().getSimpleName();
        String fieldName=getIdFields(start);
        if(fieldName==null)throw new RuntimeException("实体类"+start.getClass().getName()+"没有@Id注解");
        Object startId=getPkValue(start,fieldName).isPresent()?getPkValue(start,fieldName).get():null;
        if(startId==null)throw new RuntimeException("实体类"+start.getClass().getName()+"的主键值为空");
        Object endId=getPkValue(end,fieldName).isPresent()?getPkValue(end,fieldName).get():null;
        if(endId==null)throw new RuntimeException("实体类"+end.getClass().getName()+"的主键值为空");
        String properties;
        try {
            properties=resolveRelationshipEntity(relationship);
        } catch (IllegalAccessException e) {
           throw new RuntimeException("实体类"+relationship.getClass().getName()+"转换为Map时出错");
        }
        if(null==properties||properties.equals(""))throw new RuntimeException("实体类"+relationship.getClass().getName()+"为空");

//        String queryMerge="MATCH(start:"+nodeLabel+"{"+fieldName+":"+startId+"})," +
//                "(end:"+nodeLabel+"{"+fieldName+":"+endId+"}) " +
//                "MERGE(start)-[r:"+relationshipType+"{"+properties+"}]->(end)";
        String cypher=cypherBuilder(nodeLabel,fieldName,startId,endId,relationshipType,properties,OperationType.UPDATE,null);

        try {
            assert cypher!=null;
            super.query(cypher);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<T> retrieveRelationshipByPrinciple(DynamicPrinciple principle) {
        principle.getPrincipalArray();
        
        return null;
    }


//==================================private methods=================================================================

    private Optional<Object> getPkValue(Object entity,String pkName){
        String methodName="get"+pkName.substring(0,1).toUpperCase()+pkName.substring(1);
        Class<?>clazz=entity.getClass();
        try{
            Object pkValue= clazz.getMethod(methodName).invoke(entity);
            return pkValue==null?Optional.empty():Optional.of(pkValue);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private String getIdFields(Object entity){
        Field[] fields=entity.getClass().getDeclaredFields();
        for(Field field:fields){
            if(field.isAnnotationPresent(Id.class)){
                return field.getName();
            }
        }
        return null;
    }

    private String resolveRelationshipEntity(T relationship) throws IllegalAccessException {
        Map<String,Object> map=JacksonUtil.convertEntityToMap(relationship);
        Set<Map.Entry<String,Object>> entries=map.entrySet();
        return entries.stream().map(entry->{
            if(entry.getValue() instanceof String){
                return entry.getKey()+":'"+entry.getValue()+"'"; //如果是字符串的话要加上单引号
            }
            return entry.getKey()+":"+entry.getValue();
        }).collect(Collectors.joining(","));
    }



    private String cypherBuilder(String nodeLabel, String fieldName, Object startId, Object endId,String relationship, String properties, OperationType operationType, RelationshipDirection direction){
        switch (operationType){
            case CREATE:
                return createCypherBuilder(nodeLabel,fieldName,startId,endId,relationship,properties,direction);
            case UPDATE:
                return updateCypherBuilder(nodeLabel,fieldName,startId,endId,relationship,properties);
            case DELETE:
                return deleteCypherBuilder(nodeLabel,fieldName,startId,endId,relationship,properties);
            case RETRIEVE:
                return retreiveCypherBuilder(nodeLabel,fieldName,startId,endId,relationship,direction);
            default:
                throw new Neo4jCypherBuildException("OperationType is not supported");
        }

    }

    private String retreiveCypherBuilder(String nodeLabel, String fieldName, Object startId,Object endId ,String relationship,RelationshipDirection direction) {
        switch (direction){
            case DIRECTED:
                return "MATCH(start:"+nodeLabel+"{"+fieldName+":"+startId+"})-[r:"+relationship+"]->(end:"+nodeLabel+"{"+fieldName+":"+endId+"}) RETURN r";
            case UNDIRECTED:
                return "MATCH(start:"+nodeLabel+"{"+fieldName+":"+startId+"})-[r:"+relationship+"]-(end:"+nodeLabel+"{"+fieldName+":"+startId+"}) RETURN r";
            default:
                throw new Neo4jCypherBuildException("RelationshipDirection is not supported");
        }
    }

    private String deleteCypherBuilder(String nodeLabel, String fieldName, Object startId, Object endId,String relationship, String properties) {

                return "MATCH()-[r:"+relationship+"{"+fieldName+":"+startId+"}]->() DELETE r";
    }

    private String updateCypherBuilder(String nodeLabel, String fieldName, Object startId, Object endId,String relationship, String properties) {
        return "MATCH(start:"+nodeLabel+"{"+fieldName+":"+startId+"})-[r:"+relationship+"]->(end:"+nodeLabel+"{"+fieldName+":"+endId+"}) SET r="+properties;
    }

    private String createCypherBuilder(String nodeLabel, String fieldName, Object startId,Object endId, String relationship, String properties, RelationshipDirection direction) {
        switch (direction){
            case DIRECTED:
                return "MATCH(start:"+nodeLabel+"{"+fieldName+":"+startId+"})," +
                        "(end:"+nodeLabel+"{"+fieldName+":"+endId+"}) " +
                        "CREATE(start)-[r:"+relationship+"{"+properties+"}]->(end) RETURN r";
            case UNDIRECTED:
                return "MATCH(start:"+nodeLabel+"{"+fieldName+":"+startId+"})," +
                        "(end:"+nodeLabel+"{"+fieldName+":"+endId+"}) " +
                        "CREATE(start)-[:"+relationship+"{"+properties+"}]-(end)";
            default:
                throw new Neo4jCypherBuildException("RelationshipDirection is not supported");
        }
    }

    public static void main(String[] args) throws IllegalAccessException {
        Map<String,Object>stringObjectMap=new HashMap<>();
        stringObjectMap.put("name","张三");
        stringObjectMap.put("age",18);

        Set<Map.Entry<String,Object>> entries=stringObjectMap.entrySet();
        String s=entries.stream().map(entry->entry.getKey()+":"+entry.getValue()).collect(Collectors.joining(","));
        System.out.println(s);


        P p =new P();
        p.setName("A");
        R r =new R();
        r.setEnd(new P());
        r.setLength(0.0);
        r.setName("测试");
        r.setUid(1L);
        System.out.println();
        Map<String,Object>map=JacksonUtil.convertEntityToMap(r);
        System.out.println(map);
    }
}
