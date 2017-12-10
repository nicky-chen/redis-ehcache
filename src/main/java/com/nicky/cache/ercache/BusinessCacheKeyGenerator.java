package com.nicky.cache.ercache;

import com.nicky.utils.Md5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.DefaultKeyGenerator;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * 缓存键自定义 可以用于修改默认缓存键规则
 *
 */
public class BusinessCacheKeyGenerator extends DefaultKeyGenerator {
	 

	private static final Logger LOG = LoggerFactory.getLogger(BusinessCacheKeyGenerator.class);

	@Override
	public Object generate(Object target, Method method, Object... params) {
		if (Void.class == method.getReturnType()) {
			LOG.error("无返回值的方法不可缓存 {}", method.getName());
			return null;
		}
		Object result;
		StringBuilder sb = new StringBuilder();
		sb.append(method.getDeclaringClass().getSimpleName());
		sb.append("/").append(method.getName()).append("/");
		if (params.length > 0) {
			for (Object param : params) {
				if (param == null) {
					sb.append(NULL_PARAM_KEY).append("/");
					continue;
				}
				if (String.class.isAssignableFrom(param.getClass())
						|| Number.class.isAssignableFrom(param.getClass())) {
					sb.append(param).append("/");
					continue;
				}
				if (Date.class.isAssignableFrom(param.getClass())) {
					Date date = (Date) param;
					sb.append(date.getTime()).append("/");
					continue;
				}
				//list.toString是可行不妥的，不同机器，相同集合的地址不一样，降低缓存命中
//				if (List.class.isAssignableFrom(param.getClass())) {
//					sb.append(param.toString()).append("/");
//					continue;
//				}
				//TODO List参数类型处理
				if(List.class.isAssignableFrom(param.getClass())){
					List<Object> objs = (List<Object>) param;
					 for (Object object : objs) {
					 	//对象是否是ListKeyParam类型或者实现了ListKeyParam接口
						if (object instanceof ListKeyParam) {
							sb.append(((ListKeyParam)object).getKey());
						}
					}
					continue;
				}
				LOG.warn("缓存数据时存在不可识别的参数类型 {},method={}", param.getClass(), method);
				sb.append(param).append("/");
			}
		}
		//TODO md5-->number 可将key缩短节省内存空间
		result = super.generate(target, method, Md5Util.genMd5Hex(sb.toString()));
		LOG.debug("Cache key = {},method={}", result, method);
		return result;
	}
}
