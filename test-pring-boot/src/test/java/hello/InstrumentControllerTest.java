package hello;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

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
		instrument.setQrCode("qr_code".getBytes(StandardCharsets.UTF_8));
		dao.save(instrument);

		mvc.perform(MockMvcRequestBuilders.get("/instrument/list").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(equalTo("[{\"name\":\"Balance\",\"qrCode\":\"cXJfY29kZQ==\"}]")));
	}

//	@Test
	public void should_register_instrument() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/instrument/register").param("name", "Balance").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andExpect(content().string(equalTo("cXJDb2Rl")));
	}

}
