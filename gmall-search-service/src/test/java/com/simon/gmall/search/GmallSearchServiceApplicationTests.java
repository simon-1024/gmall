package com.simon.gmall.search;

import com.alibaba.dubbo.config.annotation.Reference;
import com.simon.gmall.bean.PmsSearchSkuInfo;
import com.simon.gmall.bean.PmsSkuInfo;
import com.simon.gmall.service.SkuService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallSearchServiceApplicationTests {

    @Reference
    private SkuService skuService;

    @Autowired
    private JestClient jestClient;


    @Test
    public void get(){




    }


    /**
     * 将数据从mysql中转移到elasticsearch中
    */
    @Test
    public void put() {

        List<PmsSkuInfo> pmsSkuInfos = new ArrayList<>();
        //查询到所有的sku
        pmsSkuInfos = skuService.getAllSku();

        //将skuz 转换成 PmsSearchSkuInfo
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList<>();

        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
            PmsSearchSkuInfo pmsSearchSkuInfo = new PmsSearchSkuInfo();
            BeanUtils.copyProperties(pmsSkuInfo,pmsSearchSkuInfo);
            pmsSearchSkuInfo.setId(Long.parseLong(pmsSkuInfo.getId()));
            pmsSearchSkuInfos.add(pmsSearchSkuInfo);
        }

        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfos) {

            Index build = new Index.Builder(pmsSearchSkuInfo).index("gmall").type("PmsSkuInfo").id(String.valueOf(pmsSearchSkuInfo.getId())).build();
            try {
                jestClient.execute(build);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
