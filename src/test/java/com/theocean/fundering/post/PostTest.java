package com.theocean.fundering.post;


import com.theocean.fundering.domain.post.controller.PostController;
import com.theocean.fundering.domain.post.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@WebMvcTest(PostController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class PostTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @WithMockUser(roles = "USER")
    @Test
    public void writePost_test() throws Exception {

    }
}
