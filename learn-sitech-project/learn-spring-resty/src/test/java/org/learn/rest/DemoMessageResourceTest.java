package org.learn.rest;

import java.io.IOException;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * 测试类
 *
 * @author:dufyun
 * @version:1.0.0
 * @date 2018/7/13
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */


//@RunWith(SpringJUnit4ClassRunner.class) //使用junit4进行测试
//@ContextConfiguration(locations={"classpath:spring-config/application.xml"}) //加载配置文件
public class DemoMessageResourceTest {


    public static void main(String[] args) {
        testMsg();

        testAccept();
    }

    public static void testAccept(){
        String url = "http://localhost:7000/service/accept";
        String str = "{\"name\":\"dufy\"}";
        String result = HttpPostWithJson(url, str,false);
        System.out.println(result);
    }

    public static void testMsg(){
        String url = "http://localhost:7000/service/msg";
        String str = "{\"name\":\"dufy\"}";
        String result = HttpPostWithJson(url, str,true);
        System.out.println(result);
    }

    public static String HttpPostWithJson(String url, String json,boolean cookieFlag) {
        String returnValue = "这是默认返回值，接口调用失败";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try{
            //第一步：创建HttpClient对象
            httpClient = HttpClients.createDefault();

            //第二步：创建httpPost对象
            HttpPost httpPost = new HttpPost(url);

            //第三步：给httpPost设置JSON格式的参数
            StringEntity requestEntity = new StringEntity(json,"utf-8");
            requestEntity.setContentEncoding("UTF-8");
            httpPost.setHeader("Content-type", "application/json; charset=UTF-8");
            if(cookieFlag){
                httpPost.setHeader("Cookie","query-order-list=0049a8ee4c16e39b449ac59945129a05");
            }
            httpPost.setEntity(requestEntity);


            //第四步：发送HttpPost请求，获取返回值
            //调接口获取返回值时，必须用此方法
            returnValue = httpClient.execute(httpPost,responseHandler);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        //第五步：处理返回值
        return returnValue;
    }
}
