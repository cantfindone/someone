import com.alibaba.fastjson.JSON
import com.superjoy.someone.BackboneApp
import com.superjoy.someone.model.PhoneNoLoginReq
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Shared
import spock.lang.Specification


@SpringBootTest(classes = BackboneApp.class)
@AutoConfigureMockMvc
class AuthControllerTest extends Specification {
    @Autowired
    MockMvc mvc
    @Shared
    def token
    def "Sms"() {
        String res = mvc.perform(MockMvcRequestBuilders
                .get("/auth/sms/188888888")
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response.getContentAsString()
        res
        println(res)
        expect:
        res != null

    }
    def "Auth Token"() {
        String res = mvc.perform(MockMvcRequestBuilders
                .post("/auth/phone")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(PhoneNoLoginReq.builder().mobile("188888888").smsCode("1234").build()))
        ).andReturn().response.getContentAsString()
        res
        println(res)
        expect:
        res != null

    }

    def "Refresh"() {
    }
}
