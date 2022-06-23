package ua.yarynych.lab.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ua.yarynych.lab.service.UserService;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().disable()
                .csrf().disable().authorizeRequests().
                antMatchers( "/menu/add", "/bar/add", "/personal/add",
                        "/menu/**/edit", "/bar/**/edit", "/personal/**/edit",
                        "/menu/**/remove", "/bar/**/remove", "/personal/**/remove").hasRole("ADMIN").
                antMatchers("/makeMenuOrder/{id}", "/makeBarOrder/{id}",
                        "/orderDone", "/order", "/menuOrder").hasAnyRole("ADMIN", "USER").
                antMatchers("/", "/menu", "/bar", "/personal",
                            "/menu/{*}", "/bar/{*}", "/login", "/registration", "/search/bar",
                        "/search_detail/bar", "/search", "/search_detail", "/filter/menu",
                        "/filter/popularity/menu", "/filter/popularity/bar", "/bar/filter", "/about").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout().permitAll()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll().
                and()
                .exceptionHandling().accessDeniedPage("/accessDenied");
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                // статика
                "/css/**",
                "/js/**",
                "/fonts/**",
                "/img/**");
    }

}