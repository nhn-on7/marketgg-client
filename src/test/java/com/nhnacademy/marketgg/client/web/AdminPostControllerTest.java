package com.nhnacademy.marketgg.client.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.marketgg.client.dto.request.PostRequest;
import com.nhnacademy.marketgg.client.dto.request.PostStatusUpdateRequest;
import com.nhnacademy.marketgg.client.dto.request.SearchRequest;
import com.nhnacademy.marketgg.client.dto.response.PostResponse;
import com.nhnacademy.marketgg.client.dto.response.PostResponseForDetail;
import com.nhnacademy.marketgg.client.dto.response.SearchBoardResponse;
import com.nhnacademy.marketgg.client.exception.NotFoundException;
import com.nhnacademy.marketgg.client.service.PostService;

import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AdminPostController.class)
class AdminPostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;

    @Autowired
    private ObjectMapper mapper;

    private static final String DEFAULT_ADMIN_POST = "/admin/customer-services";

    private PostResponseForDetail responseForDetail;
    private PostResponse response;
    private SearchBoardResponse boardResponse;
    private PostRequest request;

    @BeforeEach
    void setUp() {
        responseForDetail = new PostResponseForDetail();
        response = new PostResponse();
        boardResponse = new SearchBoardResponse();
        request = new PostRequest("701", "hi", "hello", "??????");
    }

    @Test
    @DisplayName("????????? ?????? (1:1 ??????)")
    void testIndex() throws Exception {
        given(postService.retrievesPostList(anyInt(), anyString(), anyString())).willReturn(List.of(response));

        MvcResult mvcResult = this.mockMvc.perform(get(DEFAULT_ADMIN_POST + "/{type}", "oto-inquiries")
                                                           .param("page", "0"))
                                          .andExpect(status().isOk())
                                          .andExpect(view().name("board/oto-inquiries/index"))
                                          .andReturn();

        assertThat(Objects.requireNonNull(mvcResult.getModelAndView()).getModel().get("responses")).isNotNull();
    }

    @Test
    @DisplayName("????????? ?????? (1:1 ??????, ????????? ??? X)")
    void testIndexIsPageEnd() throws Exception {
        given(postService.retrievesPostList(anyInt(), anyString(), anyString())).willReturn(
                List.of(response, response, response, response, response, response, response, response, response,
                        response, response, response));

        MvcResult mvcResult = this.mockMvc.perform(get(DEFAULT_ADMIN_POST + "/{type}", "oto-inquiries")
                                                           .param("page", "0"))
                                          .andExpect(status().isOk())
                                          .andExpect(view().name("board/oto-inquiries/index"))
                                          .andReturn();

        assertThat(Objects.requireNonNull(mvcResult.getModelAndView()).getModel().get("responses")).isNotNull();
    }

    @Test
    @DisplayName("????????? ?????? (faq)")
    void testIndexFaqs() throws Exception {
        given(postService.retrievesPostListForMe(anyInt(), anyString())).willReturn(List.of(response));

        MvcResult mvcResult = this.mockMvc.perform(get(DEFAULT_ADMIN_POST + "/{type}", "faqs")
                                                           .param("page", "0"))
                                          .andExpect(status().isOk())
                                          .andExpect(view().name("board/faqs/index"))
                                          .andReturn();

        assertThat(Objects.requireNonNull(mvcResult.getModelAndView()).getModel().get("responses")).isNotNull();
    }

    @Test
    @DisplayName("????????? ?????? (????????????")
    void testIndexNotices() throws Exception {
        given(postService.retrievesPostListForMe(anyInt(), anyString())).willReturn(List.of(response));

        MvcResult mvcResult = this.mockMvc.perform(get(DEFAULT_ADMIN_POST + "/{type}", "notices")
                                                           .param("page", "0"))
                                          .andExpect(status().isOk())
                                          .andExpect(view().name("board/notices/index"))
                                          .andReturn();

        assertThat(Objects.requireNonNull(mvcResult.getModelAndView()).getModel().get("responses")).isNotNull();
    }

    @Test
    @DisplayName("????????? ???????????? ??????")
    void testDoCreatePost() throws Exception {
        given(postService.retrieveOtoReason()).willReturn(List.of("hi"));

        MvcResult mvcResult = this.mockMvc.perform(get(DEFAULT_ADMIN_POST + "/{type}/create", "oto-inquiries"))
                                          .andExpect(status().isOk())
                                          .andExpect(view().name("board/oto-inquiries/create-form")).andReturn();

        assertThat(Objects.requireNonNull(mvcResult.getModelAndView()).getModel().get("reasons")).isNotNull();
    }

    @Test
    @DisplayName("????????? ????????????")
    void testCreatePost() throws Exception {
        willDoNothing().given(postService).createPost(any(PostRequest.class), anyString());

        this.mockMvc.perform(post(DEFAULT_ADMIN_POST + "/oto-inquiries/create")
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .content(mapper.writeValueAsString(request)))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(view().name("redirect:" + DEFAULT_ADMIN_POST + "/oto-inquiries?page=0"));
    }

    @Test
    @DisplayName("???????????? ??? ??????")
    void testSearchForCategory() throws Exception {
        given(postService.searchForCategory(anyString(), any(SearchRequest.class), anyString())).willReturn(
                List.of(boardResponse));

        MvcResult mvcResult = this.mockMvc.perform(post(DEFAULT_ADMIN_POST + "/search/categories/{categoryCode}", "701")
                                                           .param("keyword", "hi")
                                                           .param("page", "0")
                                                           .param("size", "1"))
                                          .andExpect(status().isOk())
                                          .andExpect(view().name("board/notices/index"))
                                          .andReturn();

        assertThat(Objects.requireNonNull(mvcResult.getModelAndView()).getModel().get("responses")).isNotNull();
    }

    @Test
    @DisplayName("???????????? ??? ?????? (1:1 ??????)")
    void testSearchForCategoryForOto() throws Exception {
        given(postService.searchForCategory(anyString(), any(SearchRequest.class), anyString())).willReturn(
            List.of(boardResponse));

        MvcResult mvcResult = this.mockMvc.perform(post(DEFAULT_ADMIN_POST + "/search/categories/{categoryCode}", "702")
                                      .param("keyword", "hi")
                                      .param("page", "0")
                                      .param("size", "1"))
                                          .andExpect(status().isOk())
                                          .andExpect(view().name("board/oto-inquiries/index"))
                                          .andReturn();

        assertThat(Objects.requireNonNull(mvcResult.getModelAndView()).getModel().get("responses")).isNotNull();
    }

    @Test
    @DisplayName("???????????? ??? ?????? (???????????? X)")
    void testSearchForCategoryCheckNotFoundType() throws Exception {
        given(postService.searchForCategory(anyString(), any(SearchRequest.class), anyString())).willReturn(
                List.of(boardResponse));

        this.mockMvc.perform(post(DEFAULT_ADMIN_POST + "/search/categories/{categoryCode}", "710")
                                     .param("keyword", "hi")
                                     .param("page", "0")
                                     .param("size", "1"))
                    .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException())
                                                           .getClass().isAssignableFrom(NotFoundException.class)))
                    .andReturn();
    }

    @Test
    @DisplayName("???????????? ??? ?????? (FAQ)")
    void testSearchForCategoryForFaq() throws Exception {
        given(postService.searchForCategory(anyString(), any(SearchRequest.class), anyString())).willReturn(
                List.of(boardResponse));

        MvcResult mvcResult = this.mockMvc.perform(post(DEFAULT_ADMIN_POST + "/search/categories/{categoryCode}", "703")
                                                           .param("keyword", "hi")
                                                           .param("page", "0")
                                                           .param("size", "1"))
                                          .andExpect(status().isOk())
                                          .andExpect(view().name("board/faqs/index"))
                                          .andReturn();

        assertThat(Objects.requireNonNull(mvcResult.getModelAndView()).getModel().get("responses")).isNotNull();
    }

    @Test
    @DisplayName("???????????? ??? ????????? ??????")
    void testSearchForReason() throws Exception {
        given(postService.searchForReason(anyString(), any(SearchRequest.class), anyString())).willReturn(
                List.of(boardResponse));

        MvcResult mvcResult =
                this.mockMvc.perform(post(DEFAULT_ADMIN_POST + "/search/categories/{categoryCode}/reason", "702")
                                             .param("keyword", "hi")
                                             .param("page", "0")
                                             .param("size", "1")
                                             .param("reason", "??????"))
                            .andExpect(status().isOk())
                            .andExpect(view().name("board/oto-inquiries/index"))
                            .andReturn();

        assertThat(Objects.requireNonNull(mvcResult.getModelAndView()).getModel().get("responses")).isNotNull();
    }

    @Test
    @DisplayName("???????????? ??? ????????? ??????")
    void testSearchForStatus() throws Exception {
        given(postService.searchForStatus(anyString(), any(SearchRequest.class), anyString())).willReturn(
                List.of(boardResponse));

        MvcResult mvcResult =
                this.mockMvc.perform(post(DEFAULT_ADMIN_POST + "/search/categories/{categoryCode}/status", "702")
                                             .param("keyword", "hi")
                                             .param("page", "0")
                                             .param("size", "1")
                                             .param("status", "??????"))
                            .andExpect(status().isOk())
                            .andExpect(view().name("board/oto-inquiries/index"))
                            .andReturn();

        assertThat(Objects.requireNonNull(mvcResult.getModelAndView()).getModel().get("responses")).isNotNull();
    }

    @Test
    @DisplayName("????????? ?????? ??????")
    void testDoUpdatePost() throws Exception {
        given(postService.retrievePost(anyLong(), anyString(), anyString())).willReturn(responseForDetail);
        given(postService.retrieveOtoReason()).willReturn(List.of("hi"));

        MvcResult mvcResult = this.mockMvc.perform(get(DEFAULT_ADMIN_POST + "/oto-inquiries/{boardNo}/update", 1L))
                                          .andExpect(status().isOk())
                                          .andExpect(view().name("board/oto-inquiries/update-form"))
                                          .andReturn();

        assertThat(Objects.requireNonNull(mvcResult.getModelAndView()).getModel().get("reasons")).isNotNull();
    }

    @Test
    @DisplayName("????????? ??????")
    void testUpdatePost() throws Exception {
        willDoNothing().given(postService).updatePost(anyLong(), any(PostRequest.class), anyString(), anyString());

        this.mockMvc.perform(put(DEFAULT_ADMIN_POST + "/oto-inquiries/{boardNo}/update", 1L)
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .content(mapper.writeValueAsString(request)))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(view().name("redirect:" + DEFAULT_ADMIN_POST + "/oto-inquiries?page=0"));

        then(postService).should(times(1)).updatePost(anyLong(), any(PostRequest.class), anyString(), anyString());
    }

    @Test
    @DisplayName("????????? ??????")
    void testDeletePost() throws Exception {
        willDoNothing().given(postService).deletePost(anyLong(), anyString(), anyString());

        this.mockMvc.perform(delete(DEFAULT_ADMIN_POST + "/oto-inquiries/{boardNo}/delete", 1L))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(view().name("redirect:" + DEFAULT_ADMIN_POST + "/oto-inquiries?page=0"));

        then(postService).should(times(1)).deletePost(anyLong(), anyString(), anyString());
    }

    @Test
    @DisplayName("????????? ?????? ??????")
    void testChangeStatus() throws Exception {
        willDoNothing().given(postService).changeStatus(anyLong(), any(PostStatusUpdateRequest.class));

        this.mockMvc.perform(patch(DEFAULT_ADMIN_POST + "/oto-inquiries/{boardNo}/status/change", 1L))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(view().name("redirect:" + DEFAULT_ADMIN_POST + "/oto-inquiries?page=0"));

        then(postService).should(times(1)).changeStatus(anyLong(), any(PostStatusUpdateRequest.class));
    }

}
