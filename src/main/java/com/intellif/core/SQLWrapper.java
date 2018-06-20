package com.intellif.core;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.Table;
import java.util.LinkedList;
import java.util.List;

public  class SQLWrapper<T> implements Wrapper<T> {

    String tableName = null;
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    List<Object> params = new LinkedList<>();

    public SQLWrapper(Class<T> clazz){
       Table table =  clazz.getAnnotation(Table.class);
       String name = "";
       if(!StringUtils.isEmpty(table.schema())){
           name=table.schema()+".";
       }
       name+=table.name();
       tableName = name;
    }

    public Wrapper showSelect(String... names){
        if(names==null||names.length==0){
            sb.append("select * from ").append(tableName);
        }else{
            int len = names.length;
            sb.append("select ");
            for(int i=0;i<len;i++){
                sb.append(names[i]);
                if(i!=len-1){
                    sb.append(",");
                }
            }
            sb.append(" from ").append(tableName);
        }
        return this;
    }


    @Override
    public Wrapper andEq(String name, Object value) {
        if(first){
            first = false;
            sb.append(" where ").append(name).append("=").append("?");
        }else{
            sb.append(" and ").append(name).append("=").append("?");
        }
        params.add(value);
        return this;
    }

    @Override
    public Wrapper orEq(String name, Object value) {
        if(first){
            first = false;
            sb.append(" where ").append(name).append("=").append("?");
        }else{
            sb.append(" or ").append(name).append("=").append("?");
        }
        params.add(value);
        return this;
    }

    @Override
    public Wrapper andNe(String name, Object value) {
        if(first){
            first = false;
            sb.append(" where ").append(name).append("!=").append("?");
        }else{
            sb.append(" and ").append(name).append("!=").append("?");
        }
        params.add(value);
        return this;
    }

    @Override
    public Wrapper orNe(String name, Object value) {
        if(first){
            first = false;
            sb.append(" where ").append(name).append("!=").append("?");
        }else{
            sb.append(" or ").append(name).append("!=").append("?");
        }
        params.add(value);
        return this;
    }

    @Override
    public Wrapper andGt(String name, Object value) {
        if(first){
            first = false;
            sb.append(" where ").append(name).append(">").append("?");
        }else{
            sb.append(" and ").append(name).append(">").append("?");
        }
        params.add(value);
        return this;
    }

    @Override
    public Wrapper orGt(String name, Object value) {
        if(first){
            first = false;
            sb.append(" where ").append(name).append(">").append("?");
        }else{
            sb.append(" or ").append(name).append(">").append("?");
        }
        params.add(value);
        return this;
    }

    @Override
    public Wrapper andGe(String name, Object value) {
        if(first){
            first = false;
            sb.append(" where ").append(name).append(">=").append("?");
        }else{
            sb.append(" and ").append(name).append(">=").append("?");
        }
        params.add(value);
        return this;
    }

    @Override
    public Wrapper orGe(String name, Object value) {
        if(first){
            first = false;
            sb.append(" where ").append(name).append(">=").append("?");
        }else{
            sb.append(" or ").append(name).append(">=").append("?");
        }
        params.add(value);
        return this;
    }

    @Override
    public Wrapper andLt(String name, Object value) {
        if(first){
            first = false;
            sb.append(" where ").append(name).append("<").append("?");
        }else{
            sb.append(" and ").append(name).append("<").append("?");
        }
        params.add(value);
        return this;
    }

    @Override
    public Wrapper orLt(String name, Object value) {
        if(first){
            first = false;
            sb.append(" where ").append(name).append("<").append("?");
        }else{
            sb.append(" or ").append(name).append("<").append("?");
        }
        params.add(value);
        return this;
    }

    @Override
    public Wrapper andLe(String name, Object value) {
        if(first){
            first = false;
            sb.append(" where ").append(name).append("<=").append("?");
        }else{
            sb.append(" and ").append(name).append("<=").append("?");
        }
        params.add(value);
        return this;
    }

    @Override
    public Wrapper orLe(String name, Object value) {
        if(first){
            first = false;
            sb.append(" where ").append(name).append("<=").append("?");
        }else{
            sb.append(" or ").append(name).append("<=").append("?");
        }
        params.add(value);
        return this;
    }

    @Override
    public Wrapper groupBy(String... name) {
        int len = name.length;
        sb.append(" group by ");
        for(int i=0;i<len;i++){
            sb.append(name[i]);
            if(i!=len-1){
                sb.append(",");
            }
        }
        return this;
    }

    @Override
    public Wrapper orderBy(String name, boolean asc) {
        if(asc){
            sb.append(" order by ").append(name).append(" asc");
        }else{
            sb.append(" order by ").append(name).append(" desc");
        }
        return this;
    }

    @Override
    public Wrapper andLike(String name, String value) {
        if(first){
            first = false;
            sb.append(" where ").append(name).append(" like").append("?");
        }else{
            sb.append(" and ").append(name).append(" like").append("?");
        }
        params.add(value);
        return this;
    }

    @Override
    public Wrapper orLike(String name, String value) {
        if(first){
            first = false;
            sb.append(" where ").append(name).append(" like").append("?");
        }else{
            sb.append(" or ").append(name).append(" like").append("?");
        }
        params.add(value);
        return this;
    }

    @Override
    public Wrapper andNotLike(String name, String value) {
        if(first){
            first = false;
            sb.append(" where ").append(name).append(" not like").append("?");
        }else{
            sb.append(" and ").append(name).append(" not like").append("?");
        }
        params.add(value);
        return this;
    }

    @Override
    public Wrapper orNotLike(String name, String value) {
        if(first){
            first = false;
            sb.append(" where ").append(name).append(" not like").append("?");
        }else{
            sb.append(" or ").append(name).append(" not like").append("?");
        }
        params.add(value);
        return this;
    }

    @Override
    public Wrapper andIsNull(String name) {
        if(first){
            first = false;
           sb.append(" where ").append(name).append(" is null");
        }else{
            sb.append(" and ").append(name).append(" is null");
        }
        return this;
    }

    @Override
    public Wrapper orIsNull(String name) {
        if(first){
            first = false;
            sb.append(" where ").append(name).append(" is null");
        }else{
            sb.append(" or ").append(name).append(" is null");
        }
        return this;
    }

    @Override
    public Wrapper andNotNull(String name) {
        if(first){
            first = false;
            sb.append(" where ").append(name).append(" is not null");
        }else{
            sb.append(" and ").append(name).append(" is not null");
        }
        return this;
    }

    @Override
    public Wrapper orNotNull(String name) {
        if(first){
            first = false;
            sb.append(" where ").append(name).append(" is not null");
        }else{
            sb.append(" or ").append(name).append(" is not null");
        }
        return this;
    }

    @Override
    public Wrapper andIn(String name, List<?> values) {
        int len = values.size();
        if(first){
            first = false;
            sb.append(" where ").append(name).append(" in(");
            for(int i=0;i<len;i++){
                sb.append("?");
                if(i!=len-1)
                    sb.append(",");
            }
            sb.append(")");
        }else{
            sb.append(" and ").append(name).append(" in(");
            for(int i=0;i<len;i++){
                sb.append("?");
                if(i!=len-1)
                    sb.append(",");
            }
            sb.append(")");
        }
        for(Object temp :values){
            params.add(temp);
        }
        return this;
    }

    @Override
    public Wrapper orIn(String name, List<?> values) {
        int len = values.size();
        if(first){
            first = false;
            sb.append(" where ").append(name).append(" in(");
            for(int i=0;i<len;i++){
                sb.append("?");
                if(i!=len-1)
                    sb.append(",");
            }
            sb.append(")");
        }else{
            sb.append(" or ").append(name).append(" in(");
            for(int i=0;i<len;i++){
                sb.append("?");
                if(i!=len-1)
                    sb.append(",");
            }
            sb.append(")");
        }
        for(Object temp :values){
            params.add(temp);
        }
        return this;
    }

    @Override
    public Wrapper andNotIn(String name, List<?> values) {
        int len = values.size();
        if(first){
            first = false;
            sb.append(" where ").append(name).append(" not in(");
            for(int i=0;i<len;i++){
                sb.append("?");
                if(i!=len-1)
                    sb.append(",");
            }
            sb.append(")");
        }else{
            sb.append(" and ").append(name).append(" not in(");
            for(int i=0;i<len;i++){
                sb.append("?");
                if(i!=len-1)
                    sb.append(",");
            }
            sb.append(")");
        }
        for(Object temp :values){
            params.add(temp);
        }
        return this;
    }

    @Override
    public Wrapper orNotIn(String name, List<?> values) {
        int len = values.size();
        if(first){
            first = false;
            sb.append(" where ").append(name).append(" not in(");
            for(int i=0;i<len;i++){
                sb.append("?");
                if(i!=len-1)
                    sb.append(",");
            }
            sb.append(")");
        }else{
            sb.append(" or ").append(name).append(" not in(");
            for(int i=0;i<len;i++){
                sb.append("?");
                if(i!=len-1)
                    sb.append(",");
            }
            sb.append(")");
        }
        for(Object temp :values){
            params.add(temp);
        }
        return this;
    }

    @Override
    public Wrapper andBetween(String name, Object start, Object end) {
        if(first){
            first = false;
            sb.append(" where ").append(name).append(" between ? and ? ");
        }else{
            sb.append(" and ").append(name).append(" between ? and ? ");
        }
        params.add(start);
        params.add(end);
        return this;
    }

    @Override
    public Wrapper orBetween(String name, Object start, Object end) {
        if(first){
            first = false;
            sb.append(" where ").append(name).append(" between ? and ? ");
        }else{
            sb.append(" or ").append(name).append(" between ? and ? ");
        }
        params.add(start);
        params.add(end);
        return this;
    }

    @Override
    public Wrapper andNotBetween(String name, Object start, Object end) {
        if(first){
            first = false;
            sb.append(" where ").append(name).append(" not between ? and ? ");
        }else{
            sb.append(" and ").append(name).append(" not between ? and ? ");
        }
        params.add(start);
        params.add(end);
        return this;
    }

    @Override
    public Wrapper orNotBetween(String name, Object start, Object end) {
        if(first){
            first = false;
            sb.append(" where ").append(name).append(" not between ? and ? ");
        }else{
            sb.append(" or ").append(name).append(" not between ? and ? ");
        }
        params.add(start);
        params.add(end);
        return this;
    }

    @Override
    public List<Object> getParams() {
        return params;
    }

    @Override
    public String getQuery() {
        return sb.toString();
    }
}
