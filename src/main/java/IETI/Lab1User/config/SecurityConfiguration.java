package IETI.Lab1User.config;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
@EnableGlobalMethodSecurity( securedEnabled = true, jsr250Enabled = true, prePostEnabled = true )
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{


    @Override
    protected void configure( HttpSecurity http )
            throws Exception
    {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers( HttpMethod.GET, "/api/user" ).permitAll()
                .antMatchers( HttpMethod.POST,"/api/user" ).permitAll()
                .antMatchers( HttpMethod.POST,"/api/auth" ).permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS );
    }
}
