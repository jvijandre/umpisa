package com.app.justin.reservation.umpisa;

import com.app.justin.reservation.umpisa.repository.ReservationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UmpisaApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ReservationRepository reservationRepository;

	@Test
	@Order(1)
	void shouldCreateReservation() throws Exception {
		String reservationRequestString = generateCreateRequestJsonString();
		mockMvc.perform(MockMvcRequestBuilders.post("/api/reservation/create")
						.contentType(MediaType.APPLICATION_JSON)
						.content(reservationRequestString))
				.andExpect(status().isCreated());
	}

	@Test
	@Order(2)
	void shouldUpdateReservation() throws Exception {
		String reservationRequestString = generateUpdateRequestJsonString();
		mockMvc.perform(MockMvcRequestBuilders.put("/api/reservation/update/{id}", 1)
						.contentType(MediaType.APPLICATION_JSON)
						.content(reservationRequestString))
				.andExpect(status().isAccepted());
	}

	@Test
	@Order(3)
	void shouldCancelReservation() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/api/reservation/cancel/{id}", 1))
				.andExpect(status().isAccepted());
	}

	@Test
	@Order(4)
	void shouldGetAllReservations() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/reservation"))
				.andExpect(status().isOk())
				.andReturn();
		Assert.assertEquals("[{\"id\":1,\"name\":\"Marc G\",\"contactNumber\":\"006667\",\"email\":\"jusvijlocal@mailinator.com\",\"reservationDateTime\":\"2026-11-11T12:12:30.000+00:00\",\"numberOfGuests\":15,\"notifyViaSMS\":true,\"notifyViaEmail\":true,\"active\":false}]",
				mvcResult.getResponse().getContentAsString());
	}

	private String generateCreateRequestJsonString() {
		return """
				{
				    "name": "Marc G",
				    "contactNumber" : "006667",
				    "email": "jusvijlocal@mailinator.com",
				    "reservationDateTime" : "2023-01-01T12:12:30",
				    "numberOfGuests" : 2
				}
				""";
	}

	private String generateUpdateRequestJsonString() {
		return """
				{
				    "reservationDateTime" : "2026-11-11T12:12:30",
				    "numberOfGuests" : 15
				}
				""";
	}
}
