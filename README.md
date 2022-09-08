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
![](https://i.postimg.cc/44V4fYSw/getforbiden-jwt.png)

![](https://i.postimg.cc/PrMtDBhw/getforbiden-jwt-2.png)

![](https://i.postimg.cc/pdmxgrjf/getforbiden-jwt-3.png)

![](https://i.postimg.cc/7LjDD41p/getforbiden-jwt-4.png)

![](https://i.postimg.cc/B6D3nzMt/getforbiden-jwt-5.png)


#### Part 2: Implementing the Authentication Controller

    1) Create a new package called auth inside the controller package.

    2) Update your UserDto to have a new String password property ( don't forget getter )

    3) Create a new class called LoginDto that you will use to map the JSON send to authenticate a user:

        public class LoginDto
         {
             String email;
         
             String password;
         
             public LoginDto( String email, String password )
             {
                 this.email = email;
                 this.password = password;
             }
         
             public String getEmail()
             {
                 return email;
             }
         
             public String getPassword()
             {
                 return password;
             }
         }

    4) Create a new class called TokenDto that you will use to return the token and expiration date when the authentication is successful.

       public class TokenDto
       {
       
           private String token;
       
           private Date expirationDate;
       
           public TokenDto( String token, Date expirationDate )
           {
               this.token = token;
               this.expirationDate = expirationDate;
           }
       
           public String getToken()
           {
               return token;
           }
       
           public Date getExpirationDate()
           {
               return expirationDate;
           }
       }

    5) Create a RoleEnum enum which will have as a possible option the roles ADMIN and USER

    6) Update your User class to have a passwordhash and roles new properties

    private String passwordHash;
    private List<RoleEnum> roles;

    7) Create the corresponding getter functions for the new props

    8) Generate a hashed password based on the UserDto password ( On your User constructor which should take as a parameter UserDto ) using the BCrypt algorithm:

    this.passwordHash = BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt());

    9) Also update your update function which previously should have been implemented to update a User based on a UserDto:

    public void toEntity(UserDto user) {
    	//Previous labs implementation
    	if (user.getPassword() != null) {
    		this.passwordHash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
    	}
    }

    10) Create ErrorCodeEnum to throw the corresponding error message in case there is one:

    public enum ErrorCodeEnum {
    	USER_NOT_FOUND,
    	USER_WITH_EMAIL_ALREADY_EXISTS,
    	EXPIRED_TOKEN,
    	INVALID_USER_CREDENTIALS
    }

    11) Create ServerErrorResponseDto to structure server error responses:

    public  class  ServerErrorResponseDto {

    	String  message;
    	ErrorCodeEnum  errorCode;
    	int  httpStatus;

    	public  ServerErrorResponseDto(String  message, ErrorCodeEnum  errorCode, HttpStatus  httpStatus) {
    		this.message = message;
    		this.errorCode = errorCode;
    		this.httpStatus = httpStatus.value();
    	}
    	public  String  getMessage() {
    		return  message;
    	}
    	
    	public  ErrorCodeEnum  getErrorCode() {
    		return  errorCode;
    	}

    	public  int  getHttpStatus() {
    		return  httpStatus;
    	}

    }

    12) Create a new exception class inside the exception package called InvalidCredentialsException:

   public class InvalidCredentialsException extends InternalServerErrorException
   {
      public InvalidCredentialsException() {

 		super(new ServerErrorResponseDto("User not found", ErrorCodeEnum.USER_NOT_FOUND, HttpStatus.NOT_FOUND),
 				HttpStatus.NOT_FOUND);

      }
   }

    13) Create a new Constants Class and add the following 2 constants:

    public class Constants {
    	public static final String CLAIMS_ROLES_KEY = "claims";
    	// Token expiration defined time
    	public static final int TOKEN_DURATION_MINUTES = 60;
    }

    14) Add a new environment variable called SECRET with a password you'd like to give ( which later will work as the private key ) and then update your application.properties file adding the new variable app.secret

    15) Create a new Rest Controller class inside the controller.auth package called AuthController

       import io.jsonwebtoken.Jwts;
       import io.jsonwebtoken.SignatureAlgorithm;
       import org.ada.school.exception.InvalidCredentialsException;
       import org.ada.school.repository.document.User;
       import org.ada.school.service.UserService;
       import org.springframework.beans.factory.annotation.Autowired;
       import org.springframework.beans.factory.annotation.Value;
       import org.springframework.security.crypto.bcrypt.BCrypt;
       import org.springframework.web.bind.annotation.PostMapping;
       import org.springframework.web.bind.annotation.RequestBody;
       import org.springframework.web.bind.annotation.RequestMapping;
       import org.springframework.web.bind.annotation.RestController;
       
       import java.util.Calendar;
       import java.util.Date;
       
       import static co.edu.escuelaing.users.utils.Constants.CLAIMS_ROLES_KEY;
       import static co.edu.escuelaing.users.utils.Constants.TOKEN_DURATION_MINUTES;
       
       @RestController
       @RequestMapping( "v1/auth" )
       public class AuthController
       {
       
           @Value( "${app.secret}" )
           String secret;
       
           private final UserService userService;
       
           public AuthController( @Autowired UserService userService )
           {
               this.userService = userService;
           }
       
           @PostMapping
           public TokenDto login( @RequestBody LoginDto loginDto )
           {
               // TODO: Implement findByEmail method
               User user = userService.findByEmail( loginDto.email );
               if ( BCrypt.checkpw( loginDto.password, user.getPasswordHash() ) )
               {
                   return generateTokenDto( user );
               }
               else
               {
                   throw new InvalidCredentialsException();
               }
       
           }
       
           private String generateToken( User user, Date expirationDate )
           {
               return Jwts.builder()
                   .setSubject( user.getId() )
                   .claim( CLAIMS_ROLES_KEY, user.getRoles() )
                   .setIssuedAt(new Date() )
                   .setExpiration( expirationDate )
                   .signWith( SignatureAlgorithm.HS256, secret )
                   .compact();
           }
       
           private TokenDto generateTokenDto( User user )
           {
               Calendar expirationDate = Calendar.getInstance();
               expirationDate.add( Calendar.MINUTE, TOKEN_DURATION_MINUTES );
               String token = generateToken( user, expirationDate.getTime() );
               return new TokenDto( token, expirationDate.getTime() );
           }
       }

    16) Add the /v1/user/ endpoint temporary to the SecurityConfiguration so you can access the endpoint to create a test user.

 .antMatchers( HttpMethod.POST,"/v1/user" ).permitAll()

    17) Verify the authentication endpoint by sending a user's credentials ( Remember that we have to add a password to the body now )


##### Pruebas Endpoint:

![](https://i.postimg.cc/CKdywXBp/insomnia-endpoint-craete-1-0.png)

![](https://i.postimg.cc/jj1WMV08/insomnia-endpoint-craete-2.png)

![](https://i.postimg.cc/Sszn5Zmm/insomnia-endpoint-craete-3.png)

![](https://i.postimg.cc/T1MKXrPg/insomnia-endpoint-craete-4.png)

![](https://i.postimg.cc/v8f1tSPz/insomnia-endpoint-craete-5.png)

    18) If everything goes well you would have your token object response with it's corresponding expiration date.

##### generacion Tokens:

![](https://i.postimg.cc/sDzM69qR/comprobacion-token-1.png)

![](https://i.postimg.cc/sDJvtGCG/comprobacion-token-2.png)



#### Part 3: Implement JWT Request Filter

This filter will help you verify the authorization token send on the request authorization header or using a Cookie.

    1) Implement an AbstractAuthenticationToken that will facilitate the process of handling endpoints access based on user roles. Create a new class called TokenAuthentication inside the config package:

    import org.springframework.security.authentication.AbstractAuthenticationToken;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;

    import java.util.Collection;
    import java.util.List;
    import java.util.stream.Collectors;

    public class TokenAuthentication
    extends AbstractAuthenticationToken
    {
        String token;

        String subject;

        List<String> roles;

        public TokenAuthentication( String token, String subject, List<String> roles )
        {
            super( null );
            this.token = token;
            this.subject = subject;
            this.roles = roles;
        }

        @Override
        public boolean isAuthenticated()
        {
            return !token.isEmpty() && !subject.isEmpty() && !roles.isEmpty();
        }

        @Override
        public Object getCredentials()
        {
            return token;
        }

        @Override
        public Object getPrincipal()
        {
            return subject;
        }

        @Override
        public Collection<GrantedAuthority> getAuthorities()
        {
            return roles.stream().map( role -> new SimpleGrantedAuthority( "ROLE_" + role ) ).collect(
                Collectors.toList() );
        }


    }

    2) Create a new class inside the config package called JwtRequestFilter:

    import io.jsonwebtoken.Claims;
    import io.jsonwebtoken.ExpiredJwtException;
    import io.jsonwebtoken.Jws;
    import io.jsonwebtoken.Jwts;
    import io.jsonwebtoken.MalformedJwtException;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.http.HttpHeaders;
    import org.springframework.http.HttpMethod;
    import org.springframework.http.HttpStatus;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.stereotype.Component;
    import org.springframework.web.filter.OncePerRequestFilter;

    import javax.servlet.FilterChain;
    import javax.servlet.ServletException;
    import javax.servlet.http.Cookie;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.List;
    import java.util.Objects;
    import java.util.Optional;

    import static org.ada.school.utils.Constants.CLAIMS_ROLES_KEY;
    import static org.ada.school.utils.Constants.COOKIE_NAME;

    @Component
    public class JwtRequestFilter
    extends OncePerRequestFilter
    {
    @Value( "${app.secret}" )
    String secret;

        public JwtRequestFilter()
        {
        }

        @Override
        protected void doFilterInternal( HttpServletRequest request, HttpServletResponse response, FilterChain filterChain )
            throws ServletException, IOException
        {
            String authHeader = request.getHeader( HttpHeaders.AUTHORIZATION );

            if ( HttpMethod.OPTIONS.name().equals( request.getMethod() ) )
            {
                response.setStatus( HttpServletResponse.SC_OK );
                filterChain.doFilter( request, response );
            }
            else
            {
                try
                {
                    Optional<Cookie> optionalCookie =
                        request.getCookies() != null ? Arrays.stream( request.getCookies() ).filter(
                            cookie -> Objects.equals( cookie.getName(), COOKIE_NAME ) ).findFirst() : Optional.empty();

                    String headerJwt = null;
                    if ( authHeader != null && authHeader.startsWith( "Bearer " ) )
                    {
                        headerJwt = authHeader.substring( 7 );
                    }
                    String token = optionalCookie.isPresent() ? optionalCookie.get().getValue() : headerJwt;

                    if ( token != null )
                    {
                        Jws<Claims> claims = Jwts.parser().setSigningKey( secret ).parseClaimsJws( token );
                        Claims claimsBody = claims.getBody();
                        String subject = claimsBody.getSubject();
                        List<String> roles  = claims.getBody().get( CLAIMS_ROLES_KEY , ArrayList.class);

                        if (roles == null) {
                            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid token roles");
                        } else {
                            SecurityContextHolder.getContext().setAuthentication( new TokenAuthentication( token, subject, roles));
                        }

                        request.setAttribute( "claims", claimsBody );
                        request.setAttribute( "jwtUserId", subject );
                        request.setAttribute("jwtUserRoles", roles);

                    }
                    filterChain.doFilter( request, response );
                }
                catch ( MalformedJwtException e )
                {
                    response.sendError( HttpStatus.BAD_REQUEST.value(), "Missing or wrong token" );
                }
                catch ( ExpiredJwtException e )
                {
                    response.sendError( HttpStatus.UNAUTHORIZED.value(), "Token expired or malformed" );
                }
            }
        }

    }

3) Modify the SecurityConfiguration class to include the JwtRequestFilter:

@EnableWebSecurity
@EnableGlobalMethodSecurity( securedEnabled = true, jsr250Enabled = true, prePostEnabled = true )
public class SecurityConfiguration
     extends WebSecurityConfigurerAdapter
{

JwtRequestFilter jwtRequestFilter;

public SecurityConfiguration( @Autowired JwtRequestFilter jwtRequestFilter )
{
   this.jwtRequestFilter = jwtRequestFilter;
}

@Override
protected void configure( HttpSecurity http )
        throws Exception
{
   http.addFilterBefore( jwtRequestFilter,
                         BasicAuthenticationFilter.class )
                         .cors()
                         .and()
                         .csrf()
                         .disable()
                         .authorizeRequests()
                         .antMatchers( HttpMethod.POST, "/v1/auth" )
                         .permitAll()
                         .anyRequest()
                         .authenticated()
                         .and()
                         .sessionManagement()
                         .sessionCreationPolicy(SessionCreationPolicy.STATELESS );
}
}  

    4) Add the following annotation to the DELETE user endpoint below the @PostMapping annotation. This will help you restrict which users can perform this critical operation:

    @RolesAllowed("ADMIN")

    5) Run the project and verify that it works as expected following these steps:
        - Start the server.
        - Send a POST request to the auth endpoint using the credentials of your test user.
        - Copy the token from the response.
        - Make a new GET request to the user endpoint adding the Autorization header with the word Bearer as this example:

           Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI2MTMwZmMzMWYwNTk2YzE0YzRiOWY5NTMiLCJhZGFfcm9sZXMiOlsiVVNFUiJdLCJpYXQiOjE2MzA2MDAzMjAsImV4cCI6MTYzMDY4NjcyMH0.s29NZMHYDCsCXqj9W9ZajNnlwyzW4qJG832Z3PXhwhk

### Pruebas autorizaicon tokens:

![](https://i.postimg.cc/5twBtvtq/creacion-token-parte-3.png)

![](https://i.postimg.cc/5ykvrSP1/creacion-token-parte-3-2.png)

![](https://i.postimg.cc/ZKC6dXRq/creacion-token-parte-3-3.png)

![](https://i.postimg.cc/W1Zr9r0L/creacion-token-parte-3-4.png)

![](https://i.postimg.cc/CKH80gvc/creacion-token-parte-3-5.png)

![](https://i.postimg.cc/HxYyshRd/creacion-token-parte-3-6.png)

![](https://i.postimg.cc/T1h5nWTs/creacion-token-parte-3-7.png)

![](https://i.postimg.cc/SR52dNsx/creacion-token-parte-3-8.png)


### Estructure: 

![](https://i.postimg.cc/pTSLbct0/assesment-estructure.png)

















