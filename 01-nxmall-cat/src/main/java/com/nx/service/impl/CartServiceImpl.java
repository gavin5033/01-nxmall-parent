package com.nx.service.impl;
import com.nx.api.service.ItemService;
import com.nx.pojo.NxmallResult;
import com.nx.pojo.SearchEntity;
import com.nx.pojo.TbItem;
import com.nx.pojo.TbUser;
import com.nx.service.CartService;
import com.nx.utils.CookieUtil;
import com.nx.utils.JsonUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Value("${custom.redis.cart}")
    private String key;
    @Value("${custom.redis.carttemp}")
    private String tempCartKey;

    @DubboReference
    private ItemService itemService;

    //添加购物车数据
    @Override
    public void addCart(HttpServletRequest request, HttpServletResponse response, Long id, int num) {

        //获取请求中Cookie值
        String cookieValue = CookieUtil.getCookieValue(request, "NX_TOKEN");
//        if(Strings.isNotBlank(cookieValue)){
            if(cookieValue!=null&&redisTemplate.hasKey(cookieValue)){ // 已登录
                //用户已登录系统
                //从redis中获取用户数据
                redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<TbUser>(TbUser.class));
                final Object o = redisTemplate.opsForValue().get(cookieValue);
                TbUser user = (TbUser) o;


                String cartKey = key+ user.getId();

                //判断redis中是否有购物车商品数据
                List<SearchEntity> list =null;
                if(redisTemplate.hasKey(cartKey)){
                    //如果有
                    redisTemplate.setValueSerializer(new StringRedisSerializer());
                    String redisJson = (String) redisTemplate.opsForValue().get(cartKey);
                    list = JsonUtil.jsonToList(redisJson,SearchEntity.class);
                }else{
                    //如果没有
                    list = new ArrayList<SearchEntity>();
                }
                //判断redis购物车中是否有新增的商品数据
                if(list != null && list.size()>0){
                    int index = -1;
                    for (int i = 0; i < list.size(); i++) {
                        //如果有
                        if(list.get(i).getId().equals(id)){
                            index = i;
                        }
                    }
                    if(index == -1){
                        //如果没有
                        TbItem tbItem = itemService.selectItemById(id);
                        SearchEntity se = new SearchEntity();
                        BeanUtils.copyProperties(tbItem,se);
                        se.setNum(num);
                        String image = tbItem.getImage();
                        se.setImages(Strings.isNotBlank(image)?image.split(","):new String[1]);
                        list.add(se);
                    }else{
                        list.get(index).setNum(list.get(index).getNum()+num);
                    }
                }else{
                    TbItem tbItem = itemService.selectItemById(id);
                    SearchEntity se = new SearchEntity();
                    BeanUtils.copyProperties(tbItem,se);
                    se.setNum(num);
                    String image = tbItem.getImage();
                    se.setImages(Strings.isNotBlank(image)?image.split(","):new String[1]);
                    list.add(se);
                }

                //将购物车商品集合放到redis中
                redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<SearchEntity>(SearchEntity.class));
                redisTemplate.opsForValue().set(cartKey,list);

                //合并购物车
                mergeCart(request,response);

                return;
            }

            //用户未登录
            String tempCookieValue = CookieUtil.getCookieValue(request, tempCartKey,true);
            List<SearchEntity> list = null;
            if(Strings.isNotBlank(tempCookieValue)){
                //之前向redis中存入购物车数据

                list = JsonUtil.jsonToList(tempCookieValue,SearchEntity.class);
            }else{
                list = new ArrayList<SearchEntity>();
            }
            //判断redis购物车中是否有新增的商品数据
            //for循环中不能进行增删改操作，否则并发时出现ConcurrentModificationException
            if(list != null && list.size()>0){
                int index = -1;
                for (int i = 0; i < list.size(); i++) {
                    //如果有
                    if(list.get(i).getId().equals(id)){
                        index = i;
                    }
                }
                if(index == -1){
                    //如果没有
                    TbItem tbItem = itemService.selectItemById(id);
                    SearchEntity se = new SearchEntity();
                    BeanUtils.copyProperties(tbItem,se);
                    se.setNum(num);
                    String image = tbItem.getImage();
                    se.setImages(Strings.isNotBlank(image)?image.split(","):new String[1]);
                    list.add(se);
                }else{
                    list.get(index).setNum(list.get(index).getNum()+num);
                }
            }else{
                TbItem tbItem = itemService.selectItemById(id);
                SearchEntity se = new SearchEntity();
                BeanUtils.copyProperties(tbItem,se);
                se.setNum(num);
                String image = tbItem.getImage();
                se.setImages(Strings.isNotBlank(image)?image.split(","):new String[1]);
                list.add(se);
            }



            //将购物车商品集合放到Cookie中
            System.out.println("临时购物车商品数据为："+JsonUtil.objectToJson(list));
            CookieUtil.setCookie(request,response,tempCartKey,JsonUtil.objectToJson(list),604800,true);
//            }
        }


    //显示购物车详情页
    @Override
    public List<SearchEntity> showCartDetail(HttpServletRequest request) {
        //获取请求中Cookie值
        String cookieValue = CookieUtil.getCookieValue(request, "NX_TOKEN");
        if(Strings.isNotBlank(cookieValue)) {
            if (redisTemplate.hasKey(cookieValue)) {
                //用户已登录系统
                //从redis中获取用户数据
                redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<TbUser>(TbUser.class));
                TbUser user = (TbUser) redisTemplate.opsForValue().get(cookieValue);

                String cartKey = key + user.getId();

                //判断redis中是否有购物车商品数据
                List<SearchEntity> list = null;
                if (redisTemplate.hasKey(cartKey)) {
                    //如果有
                    redisTemplate.setValueSerializer(new StringRedisSerializer());
                    String json = (String) redisTemplate.opsForValue().get(cartKey);
                    list = JsonUtil.jsonToList(json, SearchEntity.class);
                } else {
                    //如果没有
                    list = new ArrayList<SearchEntity>();
                }
                return list;
            }
        }


            //用户未登录
            String tempCookieValue = CookieUtil.getCookieValue(request, tempCartKey, true);
            System.out.println("cookie值为：" + tempCookieValue);
            List<SearchEntity> list = null;
            if (Strings.isNotBlank(tempCookieValue)) {
                //之前向redis中存入购物车数据
                list = JsonUtil.jsonToList(tempCookieValue, SearchEntity.class);
            } else {
                list = new ArrayList<SearchEntity>();
            }
            return list;
        }


 //合并购物车
    @Override
    public void mergeCart(HttpServletRequest request, HttpServletResponse response) {
        List<SearchEntity> totalList = new ArrayList<>();
        //从临时购物车中获取购物车商品数据
        List<SearchEntity> tempList = new ArrayList<>();
        String cookieValue = CookieUtil.getCookieValue(request, tempCartKey,true);
        if(Strings.isNotBlank(cookieValue)){
            tempList = JsonUtil.jsonToList(cookieValue,SearchEntity.class);
        }
        for (SearchEntity tempSe : tempList) {
            int index  = -1;
            for (int i = 0; i < totalList.size(); i++) {
                if(tempSe.getId().equals(totalList.get(i).getId())){
                    index = i;
                    break;
                }
            }
            if(index == -1){
                totalList.add(tempSe);
            }else{
                totalList.get(index).setNum(totalList.get(index).getNum()+tempSe.getNum());
            }
        }




        //从用户购物车获取购物车商品数据
        String cartKey = "";
        List<SearchEntity> redisList = new ArrayList<>();
        //获取请求中Cookie值
        String token  = CookieUtil.getCookieValue(request, "NX_TOKEN");
        if(Strings.isNotBlank(token)) {
            if (redisTemplate.hasKey(token)) {
                //用户已登录系统
                //从redis中获取用户数据
                redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<TbUser>(TbUser.class));
                TbUser user = (TbUser) redisTemplate.opsForValue().get(token);

                cartKey = key + user.getId();

                //判断redis中是否有购物车商品数据
                if (redisTemplate.hasKey(cartKey)) {
                    //如果有
                    redisTemplate.setValueSerializer(new StringRedisSerializer());
                    String json = (String) redisTemplate.opsForValue().get(cartKey);
                    redisList = JsonUtil.jsonToList(json, SearchEntity.class);
                }
            }
        }

        for (SearchEntity redisSe : redisList) {
            int index  = -1;
            for (int i = 0; i < totalList.size(); i++) {
                if(redisSe.getId().equals(totalList.get(i).getId())){
                    index = i;
                    break;
                }
            }
            if(index == -1){
                totalList.add(redisSe);
            }else{
                totalList.get(index).setNum(totalList.get(index).getNum()+redisSe.getNum());
            }
        }


        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<SearchEntity>(SearchEntity.class));
        redisTemplate.opsForValue().set(cartKey,totalList);

        //清空临时购物车
        CookieUtil.deleteCookie(request,response,tempCartKey);
    }


    //删除购物车对应商品
    @Override
    public NxmallResult removeCartById(HttpServletRequest request, Long id) {
        NxmallResult er = new NxmallResult();
        //用户登录
        //获取请求中Cookie值
        String cookieValue = CookieUtil.getCookieValue(request, "NX_TOKEN");
        if(Strings.isNotBlank(cookieValue)) {
            if (redisTemplate.hasKey(cookieValue)) {
                //用户已登录系统
                //从redis中获取用户数据
                redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<TbUser>(TbUser.class));
                TbUser user = (TbUser) redisTemplate.opsForValue().get(cookieValue);

                String cartKey = key + user.getId();

                //判断redis中是否有购物车商品数据
                List<SearchEntity> list = new ArrayList<>();
                if (redisTemplate.hasKey(cartKey)) {
                    //如果有
                    redisTemplate.setValueSerializer(new StringRedisSerializer());
                    String json = (String) redisTemplate.opsForValue().get(cartKey);
                    list = JsonUtil.jsonToList(json, SearchEntity.class);
                }
                int index = -1;
                for (int i = 0; i < list.size(); i++) {
                    if(list.get(i).getId().equals(id)){
                        index = i;
                    }
                }
                if(index != -1){
                    list.remove(list.get(index));
                }
                redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<SearchEntity>(SearchEntity.class));
                redisTemplate.opsForValue().set(cartKey,list);
            }
        }
        er.setStatus(200);
        return er;
    }


    //显示订单购物车详情数据
    @Override
    public List<SearchEntity> showOrderCartDetail(HttpServletRequest request,List<Long> ids) {
        //获取请求中Cookie值
        String cookieValue = CookieUtil.getCookieValue(request, "NX_TOKEN");
        if(Strings.isNotBlank(cookieValue)) {
            if (redisTemplate.hasKey(cookieValue)) {
                //用户已登录系统
                //从redis中获取用户数据
                redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<TbUser>(TbUser.class));
                TbUser user = (TbUser) redisTemplate.opsForValue().get(cookieValue);

                String cartKey = key + user.getId();

                //判断redis中是否有购物车商品数据
                List<SearchEntity> list = null;
                if (redisTemplate.hasKey(cartKey)) {
                    //如果有
                    redisTemplate.setValueSerializer(new StringRedisSerializer());
                    String json = (String) redisTemplate.opsForValue().get(cartKey);
                    list = JsonUtil.jsonToList(json, SearchEntity.class);
                    List<SearchEntity> entities = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        if(ids.contains(list.get(i).getId())){
                            TbItem tbItem = itemService.selectItemById(list.get(i).getId());
                            if(tbItem.getNum() >= list.get(i).getNum()){
                                list.get(i).setEnough(true);
                            }else{
                                list.get(i).setEnough(false);
                            }
                            entities.add(list.get(i));
                        }
                    }
                    return entities;

                }
            }
        }
        return new ArrayList<SearchEntity>();
    }


    //修改购物车商品数量
    @Override
    public NxmallResult updateCartNum(Long id, int num, HttpServletRequest request) {
        NxmallResult er  = new NxmallResult();
        //本案例只考虑登录情况
        //获取请求中Cookie值
        String cookieValue = CookieUtil.getCookieValue(request, "NX_TOKEN");
        if(Strings.isNotBlank(cookieValue)) {
            if (redisTemplate.hasKey(cookieValue)) {
                //用户已登录系统
                //从redis中获取用户数据
                redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<TbUser>(TbUser.class));
                TbUser user = (TbUser) redisTemplate.opsForValue().get(cookieValue);

                String cartKey = key + user.getId();

                //判断redis中是否有购物车商品数据
                List<SearchEntity> list = new ArrayList<SearchEntity>();
                if (redisTemplate.hasKey(cartKey)) {
                    //如果有
                    redisTemplate.setValueSerializer(new StringRedisSerializer());
                    String json = (String) redisTemplate.opsForValue().get(cartKey);
                    list = JsonUtil.jsonToList(json, SearchEntity.class);
                    int index = -1;
                    for (int i = 0; i < list.size(); i++) {
                        if(id.equals(list.get(i).getId())){
                            index = i;
                        }
                    }
                    if(index != -1){
                        list.get(index).setNum(num);
                    }
                }
                redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<SearchEntity>(SearchEntity.class));
                redisTemplate.opsForValue().set(cartKey,list);

            }
        }
        er.setStatus(200);
        return er;
    }


    //删除redis中下订单的所有商品
    @Override
    public NxmallResult removeOrderCartByIds(List<Long> ids, String token) {
        NxmallResult er = new NxmallResult();
        String finalCartKey="";
        if(Strings.isNotBlank(token)) {
            if (redisTemplate.hasKey(token)) {
                //用户已登录系统
                //从redis中获取用户数据
                redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<TbUser>(TbUser.class));
                TbUser user = (TbUser) redisTemplate.opsForValue().get(token);
                finalCartKey =  key+ user.getId();
            }
        }

        List<SearchEntity> list =new ArrayList<>();
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        String cartJson = (String) redisTemplate.opsForValue().get(finalCartKey);
        list = JsonUtil.jsonToList(cartJson, SearchEntity.class);

        List<SearchEntity> removeList = new ArrayList<>();
        for (Long id : ids) {
            for (SearchEntity se : list) {
                if(id.equals(se.getId())){
                    removeList.add(se);
                }
            }
        }
        list.removeAll(removeList);



        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<SearchEntity>(SearchEntity.class));
        redisTemplate.opsForValue().set(finalCartKey,list);
        er.setStatus(200);
        return er;
    }

}
