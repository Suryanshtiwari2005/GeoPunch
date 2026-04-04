package com.GeoPunch.backend;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;


@Component
public class FirebaseAuthFilter extends OncePerRequestFilter {

    //---------------------------------------------------------------------------
    private final FirebaseAuth firebaseAuth;

    public FirebaseAuthFilter(FirebaseAuth firebaseAuth){
        this.firebaseAuth = firebaseAuth;
    }

    //---------------------------------------------------------------------------

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("Entered doFilterInterval");

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);

            try {
                System.out.println("Trying for decoding FireBase Token");
                FirebaseToken decodedToken =
                        FirebaseAuth.getInstance().verifyIdToken(token);

                String uid = decodedToken.getUid();

                System.out.println("Decoded Token");

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(uid, null, List.of());

                System.out.println("Verified auth using credentials");

                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
