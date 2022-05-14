package com.leonduri.d7back.api.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leonduri.d7back.api.challenge.Challenge;
import com.leonduri.d7back.api.likes.Likes;
import com.leonduri.d7back.api.video.Video;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToMany(mappedBy = "user")
    private List<Likes> likesList = new ArrayList<>();

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false, length = 100)
    private String password;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(nullable = false)
    private boolean isAdmin;

    @Column
    private String profileFilePath;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime lastLogin;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "user"
    )
    List<Challenge> challenges;
    // reference
    // https://perfectacle.github.io/2019/05/01/hibernate-multiple-bag-fetch-exception/

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "postedBy"
    )
    List<Video> videos;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return this.email;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getPassword() {
        return this.password;
    }

    // not needed, but should be implemented, which returns always true
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() { return true; }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }

    public User(long id) {
        this.id = id;
    }
}