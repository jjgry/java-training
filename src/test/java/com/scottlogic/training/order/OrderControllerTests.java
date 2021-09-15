/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.scottlogic.training.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void initialGetRequestShouldReturnNoOrders() throws Exception {
        this.mockMvc
                .perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders.length()").value(0));
    }

    @Test
    public void initialPostRequestShouldReturnOneOrder() throws Exception {
        this.mockMvc
                .perform(post("/orders").content("{\n" +
                        "    \"username\": \"jjgray\",\n" +
                        "    \"price\": 50,\n" +
                        "    \"quantity\": 10,\n" +
                        "    \"direction\": \"SELL\"\n" +
                        "}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders.length()").value(1));
    }

    @Test
    public void badPostRequestShouldReturnOneOrder() throws Exception {
        this.mockMvc
                .perform(post("/orders").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}