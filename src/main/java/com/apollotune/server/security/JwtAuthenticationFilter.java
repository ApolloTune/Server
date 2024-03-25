package com.apollotune.server.security;

import com.apollotune.server.exceptions.GlobalExceptionHandler;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final GlobalExceptionHandler globalExceptionHandler;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        // 1. İstek başlığından "Authorization" başlığını al
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 2. Authorization başlığının varlığını ve "Bearer " önekini kontrol et
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // 3. Eğer Authorization başlığı yoksa veya "Bearer " önekini içermiyorsa diğer filtreleri devam ettir
            filterChain.doFilter(request, response);
            return;
        }
        try{

            // 4. JWT'yi "Bearer " önekinden ayır
            jwt = authHeader.substring(7);
            // 5. JWT'den kullanıcı adını çıkart
            userEmail = jwtService.extractUsername(jwt);

            // 6. Kullanıcı adı varsa ve güvenlik bağlamında kimlik doğrulaması yapılmamışsa
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 7. Kullanıcı detaylarını kullanıcı adına göre yükle
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                // 8. Token geçerliyse kimlik doğrulama yap
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    // 9. Kimlik doğrulama token'ını oluştur
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    // 10. Kimlik doğrulama token'ına ilgili detayları ekle
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // 11. Güvenlik bağlamına kimlik doğrulama token'ını ekle
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            // 12. Diğer filtreleri devam ettir
            filterChain.doFilter(request, response);
        }catch (ExpiredJwtException expiredJwtException){
            handlerExceptionResolver.resolveException(request, response, null, expiredJwtException);
        }
    }
}
