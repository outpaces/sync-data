package com.lzj.sync.loadbalance;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 抽象消息发送的负载均衡类
 *
 * @author outpaces
 * @version 1.0
 * @date 2019/10/28 15:01
 */
public class AbstractLoadBalance<T> implements ILoadBalance<T>, ApplicationContextAware {
    private final Object lock = new Object();
    private Class<T> dataT;
    private ApplicationContext applicationContext;
    private int pos;
    private List<T> robinDataList;

    public AbstractLoadBalance() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        dataT = (Class) params[0];
    }

    /**
     * 负轮限载均衡算法
     *
     * @return T
     * @author outpaces
     * @date 2019/10/28 14:34
     */
    @Override
    public T roundRobin() {
        return getRoundRobin(null);
    }

    protected T getRoundRobin(String beanPrefix) {
        if (CollectionUtils.isEmpty(robinDataList)) {
            synchronized (lock) {
                if (CollectionUtils.isEmpty(robinDataList)) {
                    robinDataList = getRoundRobinData(beanPrefix);
                }
            }
        }

        if (!CollectionUtils.isEmpty(robinDataList)) {
            if (pos > robinDataList.size()) {
                pos = 0;
            }
            return robinDataList.get(pos);
        }
        return null;
    }

    /**
     * Set the ApplicationContext that this object runs in.
     * Normally this call will be used to initialize the object.
     * <p>Invoked after population of normal bean properties but before an init callback such
     * as {@link InitializingBean#afterPropertiesSet()}
     * or a custom init-method. Invoked after {@link ResourceLoaderAware#setResourceLoader},
     * {@link ApplicationEventPublisherAware#setApplicationEventPublisher} and
     * {@link MessageSourceAware}, if applicable.
     *
     * @param applicationContext the ApplicationContext object to be used by this object
     * @throws ApplicationContextException in case of context initialization errors
     * @throws BeansException              if thrown by application context methods
     * @see BeanInitializationException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 轮询负载均衡算法
     *
     * @param
     * @return java.util.List<T>
     * @author outpaces
     * @date 2019/10/28 15:31
     */
    private List<T> getRoundRobinData(String beanPrefix) {

        if (applicationContext != null) {
            Map<String, T> beansOfType = applicationContext.getBeansOfType(dataT);
            if (!CollectionUtils.isEmpty(beansOfType)) {
                List<T> dataList = new ArrayList<>();
                for (Map.Entry<String, T> entry : beansOfType.entrySet()) {
                    if (entry.getKey() != null && entry.getKey().startsWith(beanPrefix)) {
                        dataList.add(entry.getValue());
                    }
                }
                return dataList;
            }
        }

        return null;
    }
}
