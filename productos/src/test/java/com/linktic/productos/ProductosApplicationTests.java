package com.linktic.productos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linktic.productos.dto.product.ProductManageDTO;
import com.linktic.productos.utilities.AppConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class ProductServiceImplTest {

	@Autowired
	private MockMvc mockMvc;

	private final ObjectMapper mapper = new ObjectMapper();

	@Test
	@DisplayName("CUANDO se guarda un producto existente ENTONCES el estado es 200 create")
	public void CUANDO_se_guarda_producto_ENTONCES_el_estado_es_201() throws Exception {

        String name = "MacBook PRO M10";
		BigDecimal price = new BigDecimal("124.45");

        var request = new ProductManageDTO(name, price);

		mockMvc.perform(MockMvcRequestBuilders
						.post("/product/save")
						.header("X-API-KEY", AppConstants.PRIVATE_KEY)
						.content(mapper.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
   }

	@Test
	@DisplayName("CUANDO se actualiza un producto existente ENTONCES el estado es 200 OK")
	void CUANDO_se_actualiza_producto_existente_ENTONCES_el_estado_es_200() throws Exception {
		Long productId = 12L;
		String name = "MacBook PRO M3";
		BigDecimal price = new BigDecimal("2000.00");

		ProductManageDTO requestDTO = new ProductManageDTO();
		requestDTO.setName(name);
		requestDTO.setPrice(price);

		mockMvc.perform(MockMvcRequestBuilders
						.put("/product/update/{id}", productId)
						.header("X-API-KEY", AppConstants.PRIVATE_KEY)
						.content(mapper.writeValueAsString(requestDTO))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

}
