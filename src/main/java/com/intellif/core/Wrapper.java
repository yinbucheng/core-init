package com.intellif.core;

import java.util.Collection;
import java.util.List;

public interface  Wrapper<T> {
    Wrapper showSelect(String... names);
    Wrapper andEq(String name,Object value);
    Wrapper orEq(String name,Object value);
    Wrapper andNe(String name,Object value);
    Wrapper orNe(String name,Object value);
    Wrapper andGt(String name,Object value);
    Wrapper orGt(String name,Object value);
    Wrapper andGe(String name,Object value);
    Wrapper orGe(String name,Object value);
    Wrapper andLt(String name,Object value);
    Wrapper orLt(String name,Object value);
    Wrapper andLe(String name,Object value);
    Wrapper orLe(String name,Object value);
    Wrapper groupBy(String... name);
    Wrapper orderBy(String name,boolean asc);
    Wrapper andLike(String name,String value);
    Wrapper orLike(String name,String value);
    Wrapper andNotLike(String name,String value);
    Wrapper orNotLike(String name,String value);
    Wrapper andIsNull(String name);
    Wrapper orIsNull(String name);
    Wrapper andNotNull(String name);
    Wrapper orNotNull(String name);
    Wrapper andIn(String name, List<?> values);
    Wrapper orIn(String name,List<?> values);
    Wrapper andNotIn(String name,List<?> values);
    Wrapper orNotIn(String name,List<?> values);
    Wrapper andBetween(String name,Object start,Object end);
    Wrapper orBetween(String name,Object start,Object end);
    Wrapper andNotBetween(String name,Object start,Object end);
    Wrapper orNotBetween(String name,Object start,Object end);
    List<Object> getParams();
    String       getQuery();

}
