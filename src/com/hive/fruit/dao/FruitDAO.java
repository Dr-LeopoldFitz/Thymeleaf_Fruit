package com.hive.fruit.dao;

import com.hive.fruit.pojo.Fruit;

import java.util.List;

public interface FruitDAO {
    //获取所有的库存列表信息
    //获取所有的库存列表信息

    List<Fruit> getFruitList();

    //获取指定页码上的库存列表信息 , 每页显示5条
    List<Fruit> getFruitList(String keyword , Integer pageNo);

    //增加分页功能后：获取指定页码上的库存列表信息，每页显示5条
    List<Fruit> getFruitList(Integer pageNo);

    //根据fid获取特定的水果库存信息
    Fruit getFruitByFid(Integer fid);

    //修改指定的库存记录
    void updateFruit(Fruit fruit);

    //根据fid删除指定的库存记录
    void delFruit(Integer fid);

    //添加新库存记录
    void addFruit(Fruit fruit);

    //查询总记录条数(用于计算总页数来实现尾页跳转)
    int getFruitCount();

    int getFruitCount(String keyword);
}
