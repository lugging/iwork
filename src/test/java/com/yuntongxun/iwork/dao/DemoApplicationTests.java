package com.yuntongxun.iwork.dao;

import com.yuntongxun.iwork.dao.api.DaoSupport;
import com.yuntongxun.iwork.ds.entity.YtxUserInfoEntity;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private DaoSupport daoSupport;

	@Test
	public void contextLoads() {
		YtxUserInfoEntity entity = new YtxUserInfoEntity();
		entity.setAppId("ABCEFGEAHIDFJKSLLEWWQ");
		entity = daoSupport.selectOne(entity);
		System.out.println(entity);
	}

}
