package com.nicky.cache.redis;

import com.alibaba.fastjson.JSON;
import com.nicky.utils.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;


@Component("jsonStringSerializer")
public class JSONStringSerializer {
	
	public static final String SPLIT = ":";

	/**
	 * String序列化
	 * @param obj
	 * @return
	 */
	public synchronized <T>  String stringSerialize(T obj){
		if(obj == null){
			return null;
		}
		
		String className = obj.getClass().getName();
		String jsonString = JSON.toJSONString(obj);
		String str = className+SPLIT+jsonString;
		return str;
	}
	
	
	/**
	 * String反序列化(不指定class)
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public synchronized <T> T stringUnserialize(String str){
		return this.stringUnserialize(str, null);
	}
	
	/**
	 * String反序列化
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public synchronized <T> T stringUnserialize(String str,Class<T> objClass){
		if(StringUtils.isEmpty(str)){
			return null;
		}
		
		int splitIndex = str.indexOf(SPLIT);
		String className = str.substring(0, splitIndex);
		String jsonString = str.substring(splitIndex+1, str.length());
		if(StringUtils.isEmpty(jsonString)){
			return null;
		}
		
		Class jsonClass = objClass;
		try{
			if(jsonClass == null){ //没有指定objClass
				jsonClass = Class.forName(className);
			}
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}

        return JSON.parseObject(jsonString, (Class<T>)jsonClass);
	}
	
	/**
     * 将对象Map<String,T>转换为Map<String,String>以便存储
     * @return
     */
    public <T> Map<String,String>  objMap2StrMap(Map<String, T> hash){
        Map<String,String> strMap = new LinkedHashMap<String,String>(hash.size());
        Iterator<Map.Entry<String, T>> it = hash.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, T> entry = it.next();
            String key = entry.getKey();
            T value = entry.getValue();
            strMap.put(key,this.stringSerialize(value));
        }
        
        return strMap;
    }
    
    /**
     * 将Map<String,String>转换为对象Map<String,T>
     * @param strMap
     * @param objClass
     * @return
     */
    public <T> Map<String, T> strMap2ObjMap(final Map<String,String> strMap,Class<T> objClass){
        Map<String, T> hash = new LinkedHashMap<String, T>();
        Iterator<Map.Entry<String,String>> it = strMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String,String> entry = it.next();
            String key = entry.getKey();
            String jsonString = entry.getValue();
            hash.put(key, this.stringUnserialize(jsonString, objClass));
        }
        
        return hash;
    }
    
    
    /**
     * 将jsonString的List转换为对象List
     * @param jsonStrList
     * @return
     */
    public <T> List<T> jsonStrList2ObjList(final Collection<String> jsonStrList,Class<T> objClass){
        int size = jsonStrList.size();
        List<T> objList = new ArrayList<T>(size);
        Iterator<String> it = jsonStrList.iterator();
        while(it.hasNext()){
            String jsonString = it.next();
            objList.add(this.stringUnserialize(jsonString, objClass));
        }
        return objList;
    }
    
    /**
     * 将对象类型List<T>转换为List<String>
     * @param objList
     * @return
     */
    public <T>  List<String> objList2JsonStrList(Collection<T> objList){
    	int size = objList.size();
        List<String> jsonStrList = new ArrayList<String>(size);
        Iterator<T> it = objList.iterator();
        while(it.hasNext()){
            T obj = it.next();
            jsonStrList.add(this.stringSerialize(obj));
        }
        return jsonStrList;
    }
    
    /**
     * 将Set<String>转换为对象Set<T>
     * @param jsonStrSet
     * @return
     */
    public <T> Set<T> jsonStrSet2ObjSet(final Collection<String> jsonStrSet,Class<T> objClass){
        int size = jsonStrSet.size();
        Set<T> objSet = new LinkedHashSet<T>(size);
        Iterator<String> it = jsonStrSet.iterator();
        while(it.hasNext()){
            String jsonString = it.next();
            objSet.add(this.stringUnserialize(jsonString, objClass));
        }
        return objSet;
    }
    
    /**
     * 将Set<T>转换为对象Set<String>
     * @param objSet
     * @return
     */
    public <T> Set<String> objSet2JsonStrSet(final Collection<T> objSet){
        int size = objSet.size();
        Set<String> jsonStrSet = new LinkedHashSet<String>(size);
        Iterator<T> it = objSet.iterator();
        while(it.hasNext()){
            T obj = it.next();
            jsonStrSet.add(this.stringSerialize(obj));
        }
        return jsonStrSet;
    }
    
    
    /**
     * 将jsonString的List转换为对象List
     * @param jsonStrList
     * @return
     */
    public <T> String[] objArr2StrArr(T[] objArr){
    	int size = objArr.length;
    	String [] strArr = new String[size];
        for(int i = 0;i < size; i++){
        	T obj = objArr[i];
        	String jsonString = this.stringSerialize(obj);
        	strArr[i] = jsonString;
        }
        return strArr;
    }
    
    
    public <T> Map<String, Double> doubleObjMap2BytesMap(Map<T, Double> hash) {
        Map<String, Double> jsonStrMap = new LinkedHashMap<String, Double>(hash.size());
        Iterator<Map.Entry<T, Double>> it = hash.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<T, Double> entry = it.next();
            T key = entry.getKey();
            Double value = entry.getValue();
            jsonStrMap.put(this.stringSerialize(key), value);
        }

        return jsonStrMap;
    }
}
