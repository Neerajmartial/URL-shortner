package com.url.shortner.Repository;

import com.url.shortner.models.URLMapping;
import com.url.shortner.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UrlMappingRepository extends JpaRepository<URLMapping,Long> {
        URLMapping findByShortUrl(String shortUrl);
        List<URLMapping> findByUser(User user);
}
