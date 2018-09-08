package com.springconfig.shiro.dao;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangmin on 2018/3/24.
 */
public class CustomSessionDao extends AbstractSessionDAO {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String KEY_PREFIX = "shiro_redis_session:";
    private static final String KEY_USER_PREFIX = "shiro_redis_user:";

    private long expireTime = 1800000L;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        this.saveSession(session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if(sessionId == null){
            logger.error("sessionId为空");
            return null;
        }

        Session s = (Session) redisTemplate.opsForValue().get(KEY_PREFIX + sessionId);
        return s;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        this.saveSession(session);
    }

    @Override
    public void delete(Session session) {
        if (session == null || session.getId() == null) {
            logger.error("session或者sessionId已经为空");
            return;
        }
        redisTemplate.opsForValue().getOperations().delete(KEY_PREFIX + session.getId());
    }

    @Override
    public Collection<Session> getActiveSessions() {
        return redisTemplate.keys(KEY_PREFIX + "*");
    }

    private void saveSession(Session session) throws UnknownSessionException{
        if (session == null || session.getId() == null) {
            logger.error("session或者sessionId已经为空");
            return;
        }
        String userCode = (String) session.getAttribute("userCode");
        if(userCode != null && !"".equals(userCode)) {
            String shiroSessionId = stringRedisTemplate.opsForValue().get(KEY_USER_PREFIX+userCode);
            String nowSessionId = session.getId().toString();
            if(shiroSessionId!=null && !"".equals(shiroSessionId)) {
                if(!shiroSessionId.equals(nowSessionId)) {
                    //如果不一致，踢出之前登录的用户
                    stringRedisTemplate.opsForValue().getOperations().delete(KEY_USER_PREFIX+shiroSessionId);
                    redisTemplate.opsForValue().getOperations().delete(KEY_PREFIX+shiroSessionId);
                }
            }
            //如果成功登录后刷新session信息，redis上关于user的数据重新设置过期时间
//            stringRedisTemplate.opsForValue().set(KEY_USER_PREFIX+nowSessionId, userCode, 60*30, TimeUnit.SECONDS);
            stringRedisTemplate.opsForValue().set(KEY_USER_PREFIX+userCode, nowSessionId, 60*30, TimeUnit.SECONDS);
        }
        //设置过期时间
        redisTemplate.opsForValue().set(KEY_PREFIX + session.getId(), session, expireTime, TimeUnit.MILLISECONDS);
    }

    public long getExpireTime() {
        return expireTime;
    }
}
