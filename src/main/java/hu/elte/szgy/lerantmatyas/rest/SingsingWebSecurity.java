package hu.elte.szgy.lerantmatyas.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SingsingWebSecurity extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
    	http
            .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/","/extjs/**").permitAll()
                // admin
                .antMatchers(HttpMethod.GET,"/admin/self").authenticated()
                .antMatchers(HttpMethod.GET,"/admin/*").authenticated()
                .antMatchers(HttpMethod.POST,"/admin/new").authenticated()
                .antMatchers(HttpMethod.POST,"/admin/delete/*").authenticated()
                .antMatchers(HttpMethod.POST,"/admin/name").authenticated()
                .antMatchers(HttpMethod.POST,"/admin/password").authenticated()
                // rab
                .antMatchers(HttpMethod.GET,"/prisoner/*").authenticated()
                .antMatchers(HttpMethod.GET,"/prisoner/releaseinfo/date").authenticated()
                .antMatchers(HttpMethod.GET,"/prisoner/releaseinfo/interval").authenticated()
                .antMatchers(HttpMethod.POST,"/prisoner/new").authenticated()
                .antMatchers(HttpMethod.POST,"/prisoner/delete/*").authenticated()
                .antMatchers(HttpMethod.POST,"/prisoner/update/sentence/*").authenticated()
                .antMatchers(HttpMethod.POST,"/prisoner/update/cell/*").authenticated()
                // cella
                .antMatchers(HttpMethod.GET,"/cell/*").authenticated()
                .antMatchers(HttpMethod.GET,"/cell/fullnessinfo/today").authenticated()
                .antMatchers(HttpMethod.GET,"/cell/fullnessinfo/date").authenticated()
                .antMatchers(HttpMethod.POST,"/cell/new").authenticated()
                .antMatchers(HttpMethod.POST,"/cell/delete/*").authenticated()
                .antMatchers(HttpMethod.POST,"/cell/update/places/*").authenticated()
                .antMatchers(HttpMethod.POST,"/cell/reorder").authenticated()
                .and()
            .csrf().disable()
            .formLogin()
                .loginPage("/login")
                .successForwardUrl( "/admin/dispatch" )
                .permitAll()
                .and()
            .logout()
                .permitAll();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {

//  SIMPLE USERSERVICE TO BE USED FOR TESTING ONLY     
/*     UserDetails user =
             User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);*/ 

		return new SingsingAdminService();
    }
}
