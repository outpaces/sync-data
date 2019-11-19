package com.lzj.sync.data.recover;

import com.lzj.sync.data.AbstractEntity;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 缓存需要发送的数据
 *
 * @author outpaces
 * @date 2019/10/28 13:30
 * @version 1.0
 * @param <E> 缓存的类
 */
public class DataCacheHandle<E> implements Iterable<E> {
    private final BlockingQueue<E> blockingQueue = new ArrayBlockingQueue<>(2000);

    public boolean add(E e) {
        return this.blockingQueue.add(e);
    }

    public boolean addAll(Collection<E> c) {
        return blockingQueue.addAll(c);
    }

    public void clear() {
        blockingQueue.clear();
    }

    public E peek(){
        return this.blockingQueue.peek();
    }

    public E poll(){
        return this.blockingQueue.poll();
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<E> iterator() {
        return blockingQueue.iterator();
    }

    public boolean remove(E element) {
        return this.blockingQueue.remove(element);
    }

    public int size() {
        return this.blockingQueue.size();
    }
}
