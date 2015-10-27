package hello;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.agilent.shipit.Application;
import com.agilent.shipit.pancakemobile.controller.InstrumentController;
import com.agilent.shipit.pancakemobile.dao.InstrumentDAO;
import com.agilent.shipit.pancakemobile.entity.Instrument;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class InstrumentControllerTest {

	private MockMvc mvc;

	@Autowired
	private InstrumentController controller;

	@Autowired
	private InstrumentDAO dao;

	@Before
	public void setUp() throws Exception {
		mvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void should_return_all_instrument() throws Exception {
		Instrument instrument = new Instrument();
		instrument.setName("Balance");
		instrument.setQrCode(Base64.encodeBase64String("qr_code".getBytes(StandardCharsets.UTF_8)));
		dao.save(instrument);

		mvc.perform(MockMvcRequestBuilders.get("/instrument/list").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(equalTo("[{\"name\":\"Balance\",\"qrCode\":\"cXJfY29kZQ==\"}]")));
	}

	@Test
	public void should_register_instrument() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/instrument/register").param("name", "Balance").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().string(equalTo("{\"qrCode\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAZAAAAGQAQAAAACoxAthAAABbElEQVR42u3aQa6DIBCA4TEuuuwRPEqP9no0j/KO4LILIw8HSqfGppgIvMU/CyPot5sAA4g7HAKBQCAQCAQCgUAgp5FFXnFzbpKf2b+5h+mGQOqTa3x7yG31ngzaiN2/EEgTcpeQvIHoX7EhPQTSmsQBdnAQyP8hcbQ9lvwQSBFi5n3fmFNa5y0VIJACxFRJmtZ9yOTMwgoCKUDeYtn+9X0bCgIpQGy9Lp1fjvqpXkdbH7oiGCCQ6kRjDh9GCcW7Sz7se0IgLYh22HlfLmvXtF8lQSDlyRST95nJcxpTJ9uAQGqSWKKv0aXkjfP+bvEOgVQib1VSWAR0/nHvMwsrCORMstlTklAl+Xl/FOMhkMrE7MDrClTjkr582bSHQAqR/WP0vEtHEEhpspgqaZR0eAmBtCSxktfkDfN+3j0lCOR8Ym53PJejWsl/PK+EQEqT7e2OeIw+dp+2OiGQ0uRAQCAQCAQCgUAgEEgT8gfLBHpleB+XkQAAAABJRU5ErkJggg==\"}")));
	}

}
