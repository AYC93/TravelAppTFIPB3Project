package travel.app.unused;
// package travel.app.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.Customizer;
// // import org.springframework.security.config.Customizer;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration
// @EnableWebSecurity
// public class AuthConfig {

//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//         return http.authorizeHttpRequests(auth -> {
//             auth.requestMatchers("/").permitAll();
//             auth.anyRequest().authenticated(); // any other request needs to be authenticated
//         })
//                 .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
//                 .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                 // to modify again, oauth2Login -> oauth2Login.loginPage("/login")
//                 // https://www.youtube.com/watch?v=-b_dRDixTN0&t=308s
//                 // .oauth2Login(Customizer.withDefaults())
//                 // formLogin has to be present to hav secrutiy
//                 .build();
//     }
// }
