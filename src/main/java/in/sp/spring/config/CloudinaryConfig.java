package in.sp.spring.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dfwql0vto",
                "api_key", "213537551984389",
                "api_secret", "y4FVE_xQcogeqBIEAfaUyZQLGQQ"
        ));
    }
}