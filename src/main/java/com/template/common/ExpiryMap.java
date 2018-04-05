package com.template.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.misc.NotNull;
/**
 * @Description 带过期效果的Map
 * @author cloud.d
 *
 * @param <K> 键
 * @param <V> 值
 */
public class ExpiryMap<K, V> extends HashMap<K, V> {

    private static final long serialVersionUID = 1L;

    /**
     * default expiry time 5 minutes
     */
    private long EXPIRY = 1000 * 60 * 5;

    private HashMap<K, Long> expiryMap = new HashMap<K, Long>();

    public ExpiryMap() {
        super();
    }

    public ExpiryMap(long defaultExpiryTime) {
        this(1 << 4, defaultExpiryTime);
    }

    public ExpiryMap(int initialCapacity, long defaultExpiryTime) {
        super(initialCapacity);
        this.EXPIRY = defaultExpiryTime;
    }

    public boolean containsKey(Object key) {
        return !checkExpiry(key, true) && super.containsKey(key);
    }

    public V put(K key, V value) {
        expiryMap.put(key, System.currentTimeMillis() + EXPIRY);
        return super.put(key, value);
    }

    /**
     * @param key
     * @param value
     * @param expiryTime
     *            键值对有效期 毫秒
     * @return
     */
    public V put(K key, V value, long expiryTime) {
        expiryMap.put(key, System.currentTimeMillis() + expiryTime);
        return super.put(key, value);
    }

    public int size() {
        return entrySet().size();
    }

    public boolean isEmpty() {
        return entrySet().size() == 0;
    }

    public boolean containsValue(Object value) {
        if (value == null) return Boolean.FALSE;
        Set<java.util.Map.Entry<K, V>> set = super.entrySet();
        Iterator<java.util.Map.Entry<K, V>> iterator = set.iterator();
        while (iterator.hasNext()) {
            java.util.Map.Entry<K, V> entry = iterator.next();
            if (value.equals(entry.getValue())) {
                if (checkExpiry(entry.getKey(), false)) {
                    iterator.remove();
                    return Boolean.FALSE;
                } else
                    return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public Collection<V> values() {

        Collection<V> values = super.values();

        if (values == null || values.size() < 1) return values;

        Iterator<V> iterator = values.iterator();

        while (iterator.hasNext()) {
            V next = iterator.next();
            if (!containsValue(next))
                iterator.remove();
        }
        return values;
    }

    public V get(Object key) {
        if (key == null)
            return null;
        if (checkExpiry(key, true))
            return null;
        return super.get(key);
    }

    /**
     * 
     * @Description: 是否过期
     * @param key
     * @return true - 已过期，false - 未过期
     */
    public boolean isExpired(K key) {
        if (key == null) return true;
        if (!expiryMap.containsKey(key)) return true;

        long expiryTime = expiryMap.get(key);
        boolean flag = System.currentTimeMillis() > expiryTime;

        return flag;
    }

    /**
     * 是否过期
     * @param key
     * @param isRemove 过期是否删除
     * @return
     */
    private boolean checkExpiry(@NotNull Object key, boolean isRemove) {
        if (!expiryMap.containsKey(key)) return Boolean.FALSE;

        long expiryTime = expiryMap.get(key);

        boolean flag = System.currentTimeMillis() > expiryTime;

        if (flag) {
            if (isRemove) super.remove(key);
            expiryMap.remove(key);
        }
        return flag;
    }
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> e : m.entrySet())
            expiryMap.put(e.getKey(), System.currentTimeMillis() + EXPIRY);
        super.putAll(m);
    }

    public Set<Map.Entry<K, V>> entrySet() {
        Set<java.util.Map.Entry<K, V>> set = super.entrySet();
        Iterator<java.util.Map.Entry<K, V>> iterator = set.iterator();
        while (iterator.hasNext()) {
            java.util.Map.Entry<K, V> entry = iterator.next();
            if (checkExpiry(entry.getKey(), false))
                iterator.remove();
        }
        return set;
    }

    public static void main(String[] args) {
        System.out.println(1 << 4);
    }
}
