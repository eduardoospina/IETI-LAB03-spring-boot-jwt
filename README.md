# IETI-LAB03-JWT

## AUTOR.

> Eduardo Ospina Mejia



### Desplegando localmente.

En esta seccion se daran intrucciones de como descargar y correr localmente la API REST de users:

##### Requisitos:
1)   [Java 11](https://www.java.com/download/ie_manual.jsp) 
2)   [Git](https://git-scm.com/downloads) 
3)   IDE de java. (intellij o visual studio code)

##### Despliegue local API

para esto se siguen los siguientes pasos:

1) clonar el repositorio, ya se a traves de cmd o de GIT: 

	git clone https://github.com/eduardoospina/IETI-LAB03-spring-boot-jwt.git

2) ingresamos al proyecto clonado y desde cmd hacer uso de gradle. 

```maven
	gradle build
      gradlew bootRun
```


3) Ejecutamos el proyecto utilizando los comandos en el cmd o corremos directamente desde la ide.


### Desarrollo:


#### Part 1: Adding Security Configuration:

    1) Add the following dependencies to your build.gradle:

      implementation 'javax.ws.rs:javax.ws.rs-api:2.0'
      implementation 'org.springframework.boot:spring-boot-starter-security'
      implementation 'org.springframework.security:spring-security-crypto'
      implementation 'io.jsonwebtoken:jjwt:0.9.1'

    2) Create a new class inside the config package called SecurityConfiguration where you will define the secure and open endpoints and the session management policy:

    Java:

       import org.springframework.http.HttpMethod;
       import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
       import org.springframework.security.config.annotation.web.builders.HttpSecurity;
       import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
       import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
       import org.springframework.security.config.http.SessionCreationPolicy;
       
       @EnableWebSecurity
       @EnableGlobalMethodSecurity( securedEnabled = true, jsr250Enabled = true, prePostEnabled = true )
       public class SecurityConfiguration
           extends WebSecurityConfigurerAdapter
       {
       
       
           @Override
           protected void configure( HttpSecurity http )
               throws Exception
           {
               http.cors().and().csrf().disable()
                   .authorizeRequests()
                   .antMatchers( HttpMethod.GET, "/v1/user" ).permitAll()
                   .antMatchers( HttpMethod.POST,"/v1/user" ).permitAll()
                   .anyRequest().authenticated()
                   .and()
                   .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS );
           }
       }


    3) Start your server and verify that the configuration works as expected:
        - Open Endpoint: User's List
        - Secured Endpoint: User by Id

##### Start:
![](https://i.postimg.cc/FK3Lxd8C/secure-start.png)

##### tests
![](https://i.postimg.cc/6qRgWhmL/getforbiden-jwt.png)

![](https://i.postimg.cc/02RhtLzt/getforbiden-jwt-2.png)

![](https://i.postimg.cc/DyrHRBs7/postforbiden-jwt.png)




### Estructure: 

![]()

















