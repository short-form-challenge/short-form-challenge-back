package com.leonduri.d7back.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    private static final String[] PERMIT_URL_ARRAY = {
            /* swagger v2 */
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/swagger-resource",
            "/swagger-resource/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            /* swagger v3 */
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/javainuse-openapi/**",
            "/swagger-resources/configuration/ui",
            "/swagger-resources/configuration/security"
    };

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .httpBasic().disable() // for skipping spring security login page
//                .csrf().disable()   // no need for csrf in rest api
//                .sessionManagement().sessionCreationPolicy(
//                        SessionCreationPolicy.STATELESS) // no need for session, only jwt needed
//                .and()
//                .authorizeRequests()
//                    .antMatchers("/*/signin", "/*/signup").permitAll()
//                    .antMatchers("/", "/swagger-resources/**", "/swagger**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
//                    .antMatchers("/*/admin/**").hasRole("ADMIN")
//                    .anyRequest().permitAll()
//                .and()
//                .addFilterBefore(
//                        new JwtAuthenticationFilter(jwtTokenProvider),
//                        UsernamePasswordAuthenticationFilter.class
//                );

        httpSecurity.authorizeRequests()
                .antMatchers(PERMIT_URL_ARRAY).permitAll()
                .antMatchers("/users").permitAll()
                .anyRequest().permitAll()
                .and()
                .csrf().disable();
        /*
         hasIpAddress(ip) - 접근자의 IP 주소가 매칭 하는지 확인한다.
         hasRole(role) - 역할이 부여된 권한(Granted Authority)과 일치하는지 확인한다.
         hasAnyRole(role) - 부여된 역할 중 일치하는 항목이 있는지 확인한다.
            ex) access = "hasAnyRole('ROLE_USER', 'ROLE_ADMIN')"
         permitAll - 모든 접근자를 항상 승인한다.
         denyAll - 모든 사용자의 접근을 거부한다.
         anonymous - 사용자가 익명 사용자인지 확인한다.
         authenticated - 인증된 사용자인지 확인한다.
         rememberMe - 사용자가 remember me를 사용해 인증했는지 확인한다.
         fullyAuthenticated - 사용자가 모든 크리덴셜을 갖춘 상태에서 인증했는지 확인한다.
         */
    }

    private static final String[] DOC_URLS = {
            "/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html","/swagger-ui/**"
    };

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .and().ignoring().antMatchers(DOC_URLS)
                .and().httpFirewall(allowUrlEncodedSlashHttpFirewall());
    }

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        return firewall;
    }
}