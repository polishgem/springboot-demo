package com.wm.springboot.demo;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.cas.CasFilter;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.cas.CasSubjectFactory;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.TextConfigurationRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@SpringBootApplication
@MapperScan("com.wm.springboot.demo.mapper")
@EnableApolloConfig
public class DemoApplication extends SpringBootServletInitializer {

	private static final String casFilterUrlPattern = "/hello";

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(DemoApplication.class);
	}

	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
		filterRegistration.addInitParameter("targetFilterLifecycle", "true");
		filterRegistration.setEnabled(true);
		filterRegistration.addUrlPatterns("/*");
		return filterRegistration;
	}

	@Bean(name = "lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean(name = "securityManager")
	public DefaultWebSecurityManager getDefaultWebSecurityManager(@Value("${shiro.cas}") String casServerUrlPrefix,
																  @Value("${shiro.server}") String shiroServerUrlPrefix) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		CasRealm casRealm = new CasRealm();
		casRealm.setDefaultRoles("ROLE_USER");
		casRealm.setCasServerUrlPrefix(casServerUrlPrefix);
		casRealm.setCasService(shiroServerUrlPrefix + casFilterUrlPattern);
		securityManager.setRealm(casRealm);
		securityManager.setCacheManager(new MemoryConstrainedCacheManager());
		securityManager.setSubjectFactory(new CasSubjectFactory());
		return securityManager;
	}

	private void loadShiroFilterChain(ShiroFilterFactoryBean shiroFilterFactoryBean) {
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

		filterChainDefinitionMap.put(casFilterUrlPattern, "casFilter");
		filterChainDefinitionMap.put("/logout","logout");
		filterChainDefinitionMap.put("/**", "authc");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
	}

	/**
	 * CAS Filter
	 */
	@Bean(name = "casFilter")
	public CasFilter getCasFilter(@Value("${shiro.cas}") String casServerUrlPrefix,
								  @Value("${shiro.server}") String shiroServerUrlPrefix) {
		CasFilter casFilter = new CasFilter();
		casFilter.setName("casFilter");
		casFilter.setEnabled(true);
		String loginUrl = casServerUrlPrefix + "/login?service=" + shiroServerUrlPrefix + casFilterUrlPattern;
		casFilter.setFailureUrl(loginUrl);
		return casFilter;
	}

	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager,
															CasFilter casFilter,
															@Value("${shiro.cas}") String casServerUrlPrefix,
															@Value("${shiro.server}") String shiroServerUrlPrefix) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		String loginUrl = casServerUrlPrefix + "/login?service=" + shiroServerUrlPrefix + casFilterUrlPattern;
		shiroFilterFactoryBean.setLoginUrl(loginUrl);
		shiroFilterFactoryBean.setSuccessUrl("/");
		Map<String, Filter> filters = new HashMap<>();
		filters.put("casFilter", casFilter);
		LogoutFilter logoutFilter = new LogoutFilter();
		logoutFilter.setRedirectUrl(casServerUrlPrefix + "/logout?service=" + shiroServerUrlPrefix);
		filters.put("logout",logoutFilter);
		shiroFilterFactoryBean.setFilters(filters);

		loadShiroFilterChain(shiroFilterFactoryBean);
		return shiroFilterFactoryBean;
	}

	@Bean
	public ShiroFilterChainDefinition shiroFilterChainDefinition() {
		DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
		chainDefinition.addPathDefinition("/**", "anon"); // all paths are managed via annotations

		// or allow basic authentication, but NOT require it.
		// chainDefinition.addPathDefinition("/**", "authcBasic[permissive]");
		return chainDefinition;
	}

	@Bean
	public Realm realm() {
		TextConfigurationRealm realm = new TextConfigurationRealm();
		realm.setUserDefinitions("joe.coder=password,user\n" +
				"jill.coder=password,admin");

		realm.setRoleDefinitions("admin=read,write\n" +
				"user=read");
		realm.setCachingEnabled(true);
		return realm;
	}
}
