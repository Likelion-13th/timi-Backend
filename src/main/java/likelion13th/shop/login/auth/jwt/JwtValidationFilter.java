package likelion13th.shop.login.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import likelion13th.shop.global.api.ApiResponse;
import likelion13th.shop.global.api.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor

public class JwtValidationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;

    @Override
    // reissue는 필터 거치지 않고 넘어가게
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return "/reissue".equals(request.getServletPath());
    }

    // 인증하기
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // 수정함
        String uri = request.getRequestURI();
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("[JWT] uri={}, authHeader={}", uri, authHeader);

        Authentication existing = SecurityContextHolder.getContext().getAuthentication();
        if(existing != null && existing.isAuthenticated() && !(existing instanceof AnonymousAuthenticationToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        // String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = tokenProvider.parseClaims(token);
            // 수정함
            log.info("[JWT] parsed claims subject={}", claims.getSubject());

            String providerId = claims.getSubject();
            if (providerId == null || providerId.isEmpty()) {
                sendErrorResponse(response, ErrorCode.TOKEN_INVALID);
                return;
            }

            var authorities = tokenProvider.getAuthFromClaims(claims);

            CustomUserDetails userDetails = new CustomUserDetails(
                    providerId,
                    "",
                    authorities
            );
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authToken);

            // 수정함.
            log.info("[JWT] set authentication for providerId={}", providerId);

            filterChain.doFilter(request, response);
        } catch(io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            // 잘못된 서명
            sendErrorResponse(response, ErrorCode.TOKEN_INVALID);

            // 수정함.
            log.warn("[JWT] invalid token", e);
        } catch(ExpiredJwtException e) {
            // 토큰 만료
            sendErrorResponse(response, ErrorCode.TOKEN_EXPIRED);

            // 수정함.
            log.warn("[JWT] token expired", e);
        } catch(UnsupportedJwtException e) {
            // 지원하지 않는 형식의 토큰
            sendErrorResponse(response, ErrorCode.TOKEN_INVALID);
        } catch (IllegalArgumentException e) {
            // 널/공백 등 잘못된 입력
            sendErrorResponse(response, ErrorCode.TOKEN_INVALID);
        } catch(Exception e) {
            // 예기치 못한 오류
            sendErrorResponse(response, ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(ApiResponse.onFailure(errorCode, null))
        );
    }
}
