package com.hive.fruit.dao.impl;

import com.hive.fruit.dao.FruitDAO;
import com.hive.fruit.pojo.Fruit;
import com.hive.myssm.basedao.BaseDAO;

import java.util.List;

public class FruitDAOImpl extends BaseDAO<Fruit> implements FruitDAO {
    @Override
    public List<Fruit> getFruitList() {
        return super.executeQuery("select * from t_fruit");
    }

    @Override
    public List<Fruit> getFruitList(String keyword, Integer pageNo) {
        // %表示通配符(只要包含keyword即可 模糊查询)
        return super.executeQuery("select * from t_fruit where fname like ? or remark like ? limit ? , 5" ,
                "%"+keyword+"%","%"+keyword+"%", (pageNo-1)*5);
    }

    @Override
    public List<Fruit> getFruitList(Integer pageNo) {
        return super.executeQuery("select * from t_fruit limit ?,5",5*(pageNo-1));
    }

    @Override
    public Fruit getFruitByFid(Integer fid) {
        return super.load("select * from t_fruit where fid = ? " , fid);
    }

    @Override
    public void updateFruit(Fruit fruit) {
        String sql = "update t_fruit set fname = ? , price = ? , fcount = ? , remark = ? where fid = ? " ;
        super.executeUpdate(sql,fruit.getFname(),fruit.getPrice(),fruit.getFcount(),fruit.getRemark(),fruit.getFid());
    }

    @Override
    public void delFruit(Integer fid) {
        super.executeUpdate("delete from t_fruit where fid = ? " , fid) ;
    }

    @Override
    public void addFruit(Fruit fruit) {
        String sql = "insert into t_fruit values(0,?,?,?,?)";
        super.executeUpdate(sql,fruit.getFname(),fruit.getPrice(),fruit.getFcount(),fruit.getRemark());
    }
    @Override
    public int getFruitCount() {
        //executeComplexQuery的是object数组，取出第一个元素，强制类型转换为int/long
        return ((Long)super.executeComplexQuery("select count(*) from t_fruit")[0]).intValue();
    }

    @Override
    public int getFruitCount(String keyword) {
        return ((Long)super.executeComplexQuery("select count(*) " + "from t_fruit where fname like ? or remark like ?" ,
                "%"+keyword+"%","%"+keyword+"%")[0]).intValue();
    }
}
