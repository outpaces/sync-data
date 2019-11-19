package com.lzj.sync.data.recover;

import com.lzj.sync.data.AbstractEntity;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 保存发送的数据
 *
 * @author outpaces
 * @date 2019/10/26 16:09
 * @version 1.0
 */
@Getter
public class DataSaveHandle<T extends AbstractEntity> implements Iterable<T> {
    private final BlockingQueue<T> dataSet = new ArrayBlockingQueue<>(300000);

    public synchronized void addAllData(Collection<T> data) {
        if (!CollectionUtils.isEmpty(data)) {
            this.dataSet.addAll(data);
        }
    }

    public synchronized void addData(T data) {
        dataSet.add(data);
    }

    public synchronized void removeData(T data) {
        dataSet.remove(data);
    }

    public int getDataSize() {
        return dataSet.size();
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<T> iterator() {
        return dataSet.iterator();
    }

    public int size(){
        return this.dataSet.size();
    }
}
