import cn.hutool.json.JSONArray
import cn.hutool.json.JSONObject
import com.alibaba.fastjson.JSON
import com.kvn.mockj.reflection.MockR
import com.kvn.mockj.reflection.TypeReference
import com.superjoy.someone.BackboneApp
import com.superjoy.someone.model.InstantPostReq
import com.superjoy.someone.model.LoginRes
import com.superjoy.someone.model.PhoneNoLoginReq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Shared
import spock.lang.Specification

@SpringBootTest(classes = BackboneApp.class)
@AutoConfigureMockMvc
class InstantPostControllerTest extends Specification {
    @Autowired
    MockMvc mvc
    @Shared
    def id;

    @Shared
    def token

    void setup() {
        String res = mvc.perform(MockMvcRequestBuilders
                .post("/auth/phone")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(PhoneNoLoginReq.builder().mobile("188888888").smsCode("1234").build()))
        ).andReturn().response.getContentAsString()
        token = JSON.parseObject(res, LoginRes.class).getJwt()
        println(res)
        expect:
        res != null

    }

    def "Save"() {
        def random = MockR.random(new TypeReference<InstantPostReq>() {
        })
        random.setId(null)
        String jsonString = com.alibaba.fastjson.JSON.toJSON(random)
        String res = mvc.perform(MockMvcRequestBuilders.post("/instantPost/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)).andReturn().response.getContentAsString()
        println(res)
        id = JSON.parseObject(res, InstantPostReq.class).getId()
        expect:
        id != null
    }

    def "View"() {
        String res = mvc.perform(MockMvcRequestBuilders.get("/instantPost/".concat(id).concat("/view"))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().response.getContentAsString()
        def json = new JSONObject(res)
        println(json)
        id = json.getStr("id")
        expect:
        id != null
    }

    def "Like"() {
        String res = mvc.perform(MockMvcRequestBuilders.post("/instantPost/".concat(id).concat("/like"))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().response.getContentAsString()
        def json = new JSONObject(res)
        println(json)
        id = json.getStr("id")
        expect:
        id != null
    }

    def "Comment"() {
        def random = MockR.random(new TypeReference<InstantPostReq.Comment>() {
        })
        String jsonString = com.alibaba.fastjson.JSON.toJSON(random)
        String res = mvc.perform(MockMvcRequestBuilders.post("/instantPost/".concat(id).concat("/comment"))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)).andReturn().response.getContentAsString()
        def json = new JSONObject(res)
        println(json)
        id = json.getStr("id")
        expect:
        id != null
    }

    def "List"() {
        String res = mvc.perform(MockMvcRequestBuilders.get("/instantPost/list")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().response.getContentAsString()
        def json = new JSONObject(res)
        println(json)
        expect:
        true
    }
}
