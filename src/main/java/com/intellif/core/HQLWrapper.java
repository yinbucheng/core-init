package com.intellif.core;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class HQLWrapper<T> implements Wrapper<T> {
    String className = null;
    //拼接hql语句
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    //封装接收的参数
    List<Object> params = new LinkedList<>();

    private List<String> args = new LinkedList<>();


    public List<String> getArgs(){
        return args;
    }

    public HQLWrapper showSelect(String... names){
       if(names==null||names.length==0) {
           sb.append("select p from ").append(className).append(" p ");
       }else{
          int length = names.length;
          sb.append("select ");
          for(int i=0;i<length;i++){
              args.add(names[i]);
              sb.append(" p.").append(names[i]);
              if(i!=length-1){
                  sb.append(",");
              }
          }
          sb.append(" from ").append(className).append(" p ");
       }
       return this;
    }

    public HQLWrapper(Class<T> clazz){
        className = clazz.getSimpleName();
    }


    @Override
    public Wrapper andEq(String name, Object value) {
        if(first){
            first=false;
            sb.append(" where p.").append(name).append("=?");
        }else{
            sb.append(" and p.").append(name).append("=?");
        }
        params.add(value);
        return this;
    }

    @Override
    public Wrapper orEq(String name, Object value) {
        if(first){
            first = false;
            sb.append(" where p.").append(name).append("=?");
        }else{
            sb.append(" or p.").append(name).append("=?");
        }
        params.add(value);
        return this;
    }

    @Override
    public Wrapper andNe(String name, Object value) {
        if(first){
            first = false;
            sb.append(" where p.").append(name).append("!=?");
        }else{
            sb.append(" and p.").append(name).append("!=?");
        }
        params.add(value);
        return this;
    }

    @Override
    public Wrapper orNe(String name, Object value) {
        if(first){
            first = false;
            sb.append(" where p.").append(name).append("!=?");
        }else{
            sb.append(" or p.").append(name).append("!=?");
        }
        params.add(value);
        return this;
    }

    @Override
    public Wrapper andGt(String name, Object value) {
        if(first){
            first = false;
            sb.append(" where p.").append(name).append(">?");
        }else{
            sb.append(" and p.").append(name).append(">?");
        }
        params.add(value);
        return this;
    }

    @Override
    public Wrapper orGt(String name, Object value) {
        if(first){
            first = false;
            sb.append(" where p.").append(name).append(">?");
        }else{
            sb.append(" or p.").append(name).append(">?");
        }
        params.add(value);
        return this;
    }

    @Override
    public Wrapper andGe(String name, Object value) {
        if(first){
            first = false;
            sb.append(" where p.").append(name).append(">=?");
        }else{
            sb.append(" and p.").append(name).append(">=?");
        }
        params.add(value);
        return this;
    }

    @Override
    public Wrapper orGe(String name, Object value) {
        if(first){
            first = false;
            sb.append(" where p.").append(name).append(">=?");
        }else{
            sb.append(" or p.").append(name).append(">=?");
        }
        params.add(value);
        return this;
    }

    @Override
    public Wrapper andLt(String name, Object value) {
        if(first){
            first = false;
            sb.append(" where p.").append(name).append("<?");
        }else{
            sb.append(" and p.").append(name).append("<?");
        }
        params.add(value);
        return this;
    }

    @Override
    public Wrapper orLt(String name, Object value) {
        if(first){
            first = false;
            sb.append(" where p.").append(name).append("<?");
        }else{
            sb.append(" or p.").append(name).append("<?");
        }
        params.add(value);
        return this;
    }

    @Override
    public Wrapper andLe(String name, Object value) {
        if(first){
            first = false;
            sb.append(" where p.").append(name).append("<=?");
        }else{
            sb.append(" and p.").append(name).append("<=?");
        }
        params.add(value);
        return this;
    }

    @Override
    public Wrapper orLe(String name, Object value) {
        if(first){
            first = false;
            sb.append(" where p.").append(name).append("<=?");
        }else{
            sb.append(" or p.").append(name).append("<=?");
        }
        params.add(value);
        return this;
    }


    @Override
    public Wrapper groupBy(String... names) {
        int len = names.length;
        sb.append("group by ");
        for(int i=0;i<len;i++){
            sb.append(names[i]);
            if(i!=len-1)
                sb.append(",");
        }
        return this;
    }

    @Override
    public Wrapper orderBy(String name, boolean asc) {
         if(asc){
             sb.append(" order by ").append(name).append(" asc ");
         }else{
             sb.append(" order by ").append(name).append(" desc ");
         }
         return this;
    }

    @Override
    public Wrapper andLike(String name, String value) {
        if(first){
            first = false;
            sb.append(" where p.").append(name).append(" like ?");
        }else{
            sb.append(" and p.").append(name).append(" like ?");
        }
        if(value==null){
            value = "";
        }
        params.add(value+"%");
        return this;
    }

    @Override
    public Wrapper orLike(String name, String value) {
        if(first){
            first = false;
            sb.append(" where p.").append(name).append(" like ?");
        }else{
            sb.append(" or p.").append(name).append(" like ?");
        }
        if(value==null){
            value="";
        }
        params.add(value+"%");
        return this;
    }

    @Override
    public Wrapper andNotLike(String name, String value) {
        if(first){
            first = false;
            sb.append(" where p.").append(name).append(" not like ?");
        }else{
            sb.append(" and p.").append(name).append(" not like ?");
        }
        if(value==null){
            value="";
        }
        params.add(value+"%");
        return this;
    }

    @Override
    public Wrapper orNotLike(String name, String value) {
        if(first){
            first = false;
            sb.append(" where p.").append(name).append(" not like ?");
        }else{
            sb.append(" or p.").append(name).append(" not like ?");
        }
        if(value==null){
            value="";
        }
        params.add(value+"%");
        return this;
    }

    @Override
    public Wrapper andIsNull(String name) {
        if(first){
            first = false;
            sb.append(" where p.").append(name).append(" is null");
        }else{
            sb.append(" and p.").append(name).append(" is null");
        }
        return this;
    }

    @Override
    public Wrapper orIsNull(String name) {
        if(first){
            first = false;
            sb.append(" where p.").append(name).append(" is null");
        }else{
            sb.append(" or p.").append(name).append(" is null");
        }
        return this;
    }

    @Override
    public Wrapper andNotNull(String name) {
        if(first){
            first = false;
            sb.append(" where p.").append(name).append(" is not null");
        }else{
            sb.append(" and p.").append(name).append(" is not null");
        }
        return this;
    }

    @Override
    public Wrapper orNotNull(String name) {
        if(first){
            first = false;
            sb.append(" where p.").append(name).append(" is not null");
        }else{
            sb.append(" or p.").append(name).append(" is not null");
        }
        return this;
    }


    @Override
    public Wrapper andIn(String name, List<?> values) {
        if(first){
            first = false;
            sb.append(" where p.").append(name).append(" in ? ");
        }else{
            sb.append(" and p.").append(name).append(" in ? ");
        }
        params.add(values);
        return this;
    }

    @Override
    public Wrapper orIn(String name, List<?> values) {
        if(first){
            first = false;
            sb.append(" where p.").append(name).append(" in(?) ");
        }else{
            sb.append(" or p.").append(name).append(" in(?) ");
        }
        params.add(values);
        return this;
    }

    @Override
    public Wrapper andNotIn(String name, List<?> values) {
        if(first){
            first = false;
            sb.append(" where p.").append(name).append(" not in ? ");
        }else{
            sb.append(" and p.").append(name).append(" not in ? ");
        }
        params.add(values);
        return this;
    }

    @Override
    public Wrapper orNotIn(String name, List<?> values) {
        if(first){
            first = false;
            sb.append(" where p.").append(name).append(" not in ? ");
        }else{
            sb.append(" or p.").append(name).append(" not in ? ");
        }
        params.add(values);
        return this;
    }

    @Override
    public Wrapper andBetween(String name, Object start, Object end) {
        if(first){
            first = false;
            sb.append(" where p.").append(name).append(" between ? and ?");
        }else{
            sb.append(" and p.").append(name).append(" between ? and ?");
        }
        params.add(start);
        params.add(end);
        return this;
    }

    @Override
    public Wrapper orBetween(String name, Object start, Object end) {
        if(first){
            first = false;
            sb.append(" where p.").append(name).append(" between ? and ?");
        }else{
            sb.append(" or p.").append(name).append(" between ? and ?");
        }
        params.add(start);
        params.add(end);
        return this;
    }

    @Override
    public Wrapper andNotBetween(String name, Object start, Object end) {
        if(first){
            first = false;
            sb.append(" where p.").append(name).append(" not between ? and ?");
        }else{
            sb.append(" and p.").append(name).append(" not between ? and ?");
        }
        params.add(start);
        params.add(end);
        return this;
    }

    @Override
    public Wrapper orNotBetween(String name, Object start, Object end) {
        if(first){
            first = false;
            sb.append(" where p.").append(name).append(" not between ? and ?");
        }else{
            sb.append(" or p.").append(name).append(" not between ? and ?");
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
