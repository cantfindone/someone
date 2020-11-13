package groovy

import cn.hutool.json.JSON
import com.fuyun.chain.api.common.Period
import com.fuyun.chain.api.model.PageableQuery
import com.fuyun.chain.api.model.Transaction
import com.superjoy.someone.BackboneApp
import org.json.JSONArray
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

@Stepwise
@SpringBootTest(classes = BackboneApp.class)
@AutoConfigureMockMvc
class ChainApiTest extends Specification {
    @Autowired
    MockMvc mvc

    @Shared
    def eid
    @Shared
    def resources
    @Shared
    def txHash
    @Shared
    def token

    def "Auth Token"() {
        String res = mvc.perform(MockMvcRequestBuilders
                .post("/oauth/token?client_id=ChainCodeTest&client_secret=ChainCodeTestSecret&scope=*&grant_type=client_credentials")
//                .header(HttpHeaders.AUTHORIZATION, "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response.getContentAsString()
        def json = new JSONObject(res)

        token = json.getString("access_token")

        expect:
        token != null

    }


    def "Save"() {
        String jsonString = "{\n" +
                "\"hash\":\"0x0a5d17d3b19f82f8340d3977609aa9e86b4ad8b9bd71bd9eced9271f1d5b2e4a\",\n" +
                "\"user\":\"0xca35b7d915458ef540ade6068dfe2f44e8fa733c\",\n" +
                "\"resources\":\"a2d602f3-5812-4879-9ed8-47b39200f61e\",\n" +
                "\"data\":\"{\\\"ext\\\":\\\"extention\\\"}\",\n" +
                "\"localData\":\"{\\\"local\\\":\\\"data\\\"}\",\n" +
                "\"preEid\":\"0x934afc51a516d8ea4bd03af6719b52c25f6e9f9755139e8c82e620b0a0114b7c\"\n" +
                "}"
        String res = mvc.perform(MockMvcRequestBuilders.post("/api/v1/save")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)).andReturn().response.getContentAsString()
        def json = new JSONObject(res)
        eid = json.getString("eid")
        resources = json.getString("resources")
        txHash = json.getString("txHash")
        expect:
        eid != null
        resources != null
        txHash != null
    }

    def "GetByEid"() {
        String jsonString = eid;
        String res = mvc.perform(MockMvcRequestBuilders.post("/api/v1/getByEid")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)).andReturn().response.getContentAsString()
        def json = new JSONObject(res)
        def eid = json.getString("eid")
        def resources = json.getString("resources")
        def txHash = json.getString("txHash")
        expect:
        eid != null
        resources != null
        txHash != null
    }

    def "GetByTxHash"() {
        String jsonString = txHash;
        String res = mvc.perform(MockMvcRequestBuilders.post("/api/v1/getByTxHash")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)).andReturn().response.getContentAsString()
        def json = new JSONObject(res)
        def eid = json.getString("eid")
        def resources = json.getString("resources")
        def txHash = json.getString("txHash")
        expect:
        eid != null
        resources != null
        txHash != null
    }

    def "GetByresources"() {
        String jsonString = resources;
        String res = mvc.perform(MockMvcRequestBuilders.post("/api/v1/getByresources")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)).andReturn().response.getContentAsString()
        def json = new JSONArray(res)
        def eid = json.get(0).getString("eid")
        def resources = json.get(0).getString("resources")
        def txHash = json.get(0).getString("txHash")
        expect:
        eid != null
        resources != null
        txHash != null
    }


    def "Statistic"() {
        String jsonString = ""
        String res = mvc.perform(MockMvcRequestBuilders.post("/api/v1/statistic")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)).andReturn().response.getContentAsString()
        def json = new JSONObject(res)
        def today = json.getString(Period.today.name())
        def daily = json.getString(Period.daily.name())
        def monthly = json.getString(Period.monthly.name())
        def total = json.getString(Period.total.name())

        expect:
        today != null
        daily != null
        monthly != null
        total != null
    }

    def "List with query params resources: #resources"() {
        PageableQuery pq = new PageableQuery();
        pq.setExample(Transaction.builder().resources(resources).build())
        String jsonString = com.alibaba.fastjson.JSON.toJSON(pq)
                String res = mvc.perform(MockMvcRequestBuilders.post("/api/v1/list")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)).andReturn().response.getContentAsString()
        println(res);
        def json = new JSONObject(res).getJSONArray("data")
        def eid = json.get(0).getString("eid")
        def resources = json.get(0).getString("resources")
        def txHash = json.get(0).getString("txHash")
        expect:
        eid != null
        resources != null
        txHash != null
    }

    def "sleep 40s waiting checking job finish"(){
        sleep(40000)
        expect:"sleep 40s"
        true
    }
    def "List with no param"() {
        String jsonString = "{}"
        String res = mvc.perform(MockMvcRequestBuilders.post("/api/v1/list")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)).andReturn().response.getContentAsString()
        println(res);
        def json = new JSONObject(res).getJSONArray("data")
        def eid = json.get(0).getString("eid")
        def resources = json.get(0).getString("resources")
        def txHash = json.get(0).getString("txHash")
        expect:
        eid != null
        resources != null
        txHash != null
    }

    def "BlockInfo"() {
        sleep(10000)
        String jsonString = txHash;
        String res = mvc.perform(MockMvcRequestBuilders.post("/api/v1/blockInfo")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)).andReturn().response.getContentAsString()
        def json = new JSONObject(res)
        def blockHash = json.getString("blockHash")
        def blockNumber = json.getString("blockHash")
        expect:
        blockHash != null
        blockNumber != null

    }

    def "GetBlock"() {
        String jsonString = eid;
        String res = mvc.perform(MockMvcRequestBuilders.post("/api/v1/getBlock")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)).andReturn().response.getContentAsString()
        def json = new JSONObject(res)
        def eid = json.getString("eid")
        def resources = json.getString("resources")
        expect:
        eid != null
        resources != null
    }
}
