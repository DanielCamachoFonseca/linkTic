package com.linktic.inventarios;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linktic.inventarios.client.ProductClient;
import com.linktic.inventarios.dto.product.ProductDTO;
import com.linktic.inventarios.dto.purchase.PurchaseRequestDTO;
import com.linktic.inventarios.service.InventoryService;
import com.linktic.inventarios.utilities.AppConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class InventariosApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	private final ObjectMapper mapper = new ObjectMapper();

	@MockBean
	private ProductClient productClient;

	@Autowired
	private InventoryService inventoryService;

	@Test
	@DisplayName("CUANDO hay inventario suficiente ENTONCES se procesa la compra exitosamente")
	void compra_exitosa() throws Exception {
		PurchaseRequestDTO request = new PurchaseRequestDTO(2L, 2L);

		mockMvc.perform(MockMvcRequestBuilders
						.post("/inventory/purchase")
						.header("X-API-KEY", AppConstants.PRIVATE_KEY)
						.content(mapper.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}


	@Test
	@DisplayName("CUANDO no hay inventario suficiente ENTONCES se retorna error")
	void compra_fallida_inventario_insuficiente() throws Exception {
		PurchaseRequestDTO request = new PurchaseRequestDTO(1L, 999L);

		mockMvc.perform(MockMvcRequestBuilders
						.post("/inventory/purchase")
						.header("X-API-KEY", AppConstants.PRIVATE_KEY)
						.content(mapper.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("Inventario insuficiente"));
	}

	@Test
	@DisplayName("CUANDO se consulta un producto de otro microservicio ENTONCES se procesa la compra usando esa info")
	void comunicacion_entre_microservicios_exitosa() throws Exception {
		// Simular respuesta del microservicio de productos
		when(productClient.getProductById(1L, AppConstants.PRIVATE_KEY))
				.thenReturn(new ProductDTO(1L, "Laptop de 19 pulgadas", new BigDecimal("14400.50")));

		PurchaseRequestDTO request = new PurchaseRequestDTO(1L, 2L);

		mockMvc.perform(MockMvcRequestBuilders
						.post("/inventory/purchase")
						.header("X-API-KEY", AppConstants.PRIVATE_KEY)
						.content(new ObjectMapper().writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Compra realizada exitosamente"));
	}

	@Test
	@DisplayName("CUANDO el producto no existe ENTONCES se devuelve 400")
	void producto_no_encontrado() throws Exception {
		// Simular que el otro microservicio devuelve un error
		when(productClient.getProductById(99L, AppConstants.PRIVATE_KEY))
				.thenThrow(new RuntimeException("Producto no encontrado"));

		PurchaseRequestDTO request = new PurchaseRequestDTO(99L, 1L);

		mockMvc.perform(MockMvcRequestBuilders
						.post("/inventory/purchase")
						.header("X-API-KEY", AppConstants.PRIVATE_KEY)
						.content(new ObjectMapper().writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("Producto no encontrado"));
	}

	@Test
	@DisplayName("CUANDO no hay inventario suficiente ENTONCES se devuelve 400")
	void inventario_insuficiente() throws Exception {
		when(productClient.getProductById(1L, AppConstants.PRIVATE_KEY))
				.thenReturn(new ProductDTO(1L, "Laptop de 19 pulgadas", new BigDecimal("14400.50")));

		PurchaseRequestDTO request = new PurchaseRequestDTO(1L, 1000L);

		mockMvc.perform(MockMvcRequestBuilders
						.post("/inventory/purchase")
						.header("X-API-KEY", AppConstants.PRIVATE_KEY)
						.content(new ObjectMapper().writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(AppConstants.INSUFFICIENT_INVENTARY));
	}

}
