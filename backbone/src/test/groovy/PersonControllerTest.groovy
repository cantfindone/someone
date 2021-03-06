import cn.hutool.json.JSONObject
import com.alibaba.fastjson.JSON
import com.kvn.mockj.reflection.MockR
import com.kvn.mockj.reflection.TypeReference
import com.superjoy.someone.BackboneApp
import com.superjoy.someone.model.LoginRes
import com.superjoy.someone.model.PersonInfoReq
import com.superjoy.someone.model.PhoneNoLoginReq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.http.HttpHeaders
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
    @Shared
    def id;

    @Shared
    def token

    @Autowired
    MongoTemplate db;

    void setup() {
        String res = mvc.perform(MockMvcRequestBuilders
                .post("/auth/phone")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(PhoneNoLoginReq.builder().mobile("188888888").smsCode("1234").build()))
        ).andReturn().response.getContentAsString()
        token = JSON.parseObject(res, LoginRes.class).getJwt()
        println(res)
        //db.remove(org.springframework.data.mongodb.core.query.Query.query(Criteria.where("phone").is("188888888")), PersonInfo.class)
        expect:
        res != null


    }

    def "Save"() {
        def random = MockR.random(new TypeReference<PersonInfoReq>() {
        })
        random.setId(null)
        random.setPhone("188888888")
        String jsonString = com.alibaba.fastjson.JSON.toJSON(random)
        String res = mvc.perform(MockMvcRequestBuilders.post("/person/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)).andReturn().response.getContentAsString()
        println(res)
        id = JSON.parseObject(res, PersonInfoReq.class).getId()
        expect:
        id != null
    }

    def "Get"() {
        String res = mvc.perform(MockMvcRequestBuilders.get("/person/".concat(id))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().response.getContentAsString()
        def json = new JSONObject(res)
        println(json)
        id = json.getStr("id")
        expect:
        id != null
    }
    def "Viewers"() {
        String res = mvc.perform(MockMvcRequestBuilders.get("/person/".concat(id).concat("/viewers"))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().response.getContentAsString()
        def json = new JSONObject(res)
        println(json)
        id = json.getStr("ownerId")
        expect:
        id != null
    }

    def "List"() {
        String res = mvc.perform(MockMvcRequestBuilders.get("/person/list")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().response.getContentAsString()
        def json = new JSONObject(res)
        println(json)
        expect:
        true
    }
}
