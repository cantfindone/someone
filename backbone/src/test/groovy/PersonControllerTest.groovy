import cn.hutool.json.JSONObject
import com.kvn.mockj.reflection.MockR
import com.kvn.mockj.reflection.TypeReference
import com.superjoy.someone.BackboneApp
import com.superjoy.someone.model.PersonBasicInfoReq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Shared
import spock.lang.Specification

@SpringBootTest(classes = BackboneApp.class)
@AutoConfigureMockMvc
class PersonControllerTest extends Specification {
    @Autowired
    MockMvc mvc
    @Shared def id;
    def "Save"() {
        def random = MockR.random(new TypeReference<PersonBasicInfoReq>() {
        })
        random.setId(null)
        String jsonString =com.alibaba.fastjson.JSON.toJSON(random)
        String res = mvc.perform(MockMvcRequestBuilders.post("/person/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)).andReturn().response.getContentAsString()
        JSONObject json = new JSONObject(res)
        println(json)
        id = json.getStr("id")
        expect:
        id != null

    }

    def "Get"() {
        String res = mvc.perform(MockMvcRequestBuilders.get("/person/".concat(id))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().response.getContentAsString()
        def json = new JSONObject(res)
        println(json)
        id = json.getStr("id")
        expect:
        id != null
    }

    def "List"() {
        String res = mvc.perform(MockMvcRequestBuilders.get("/person/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().response.getContentAsString()
        def json = new JSONObject(res)
        println(json)
        expect:
        true
    }
}
