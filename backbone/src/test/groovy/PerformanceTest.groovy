import com.fuyun.chain.api.model.Transaction
import com.fuyun.chain.auth.UserContext
import com.fuyun.chain.auth.model.JwtUserInfo
import com.superjoy.someone.BackboneApp
import com.superjoy.someone.controller.PersonInfoController
import org.databene.contiperf.PerfTest
import org.databene.contiperf.junit.ContiPerfRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = BackboneApp.class)
class PerformanceTest {
    @Autowired
    PersonInfoController chain;
    @Rule
    public ContiPerfRule contiPerfRule = new ContiPerfRule();


//
//    def "Auth Token"() {
//        String res = mvc.perform(MockMvcRequestBuilders
//                .post("/oauth/token?client_id=ChainCodeTest&client_secret=ChainCodeTestSecret&scope=*&grant_type=client_credentials")
////                .header(HttpHeaders.AUTHORIZATION, "Bearer "+token)
//                .contentType(MediaType.APPLICATION_JSON)
//        ).andReturn().response.getContentAsString()
//        def json = new JSONObject(res)
//
//        token = json.getString("access_token")
//
//        expect:
//        token != null
//
//    }
    @Before
     void setup(){
        UserContext.setUser(new JwtUserInfo("perfTest","perfTest"))
    }
    @Test
    @PerfTest(invocations = 1000,threads = 10)
     void "Save"() {
       def res = chain.save(Transaction.builder()
               .hash("0x0a5d17d3b19f82f8340d3977609aa9e86b4ad8b9bd71bd9eced9271f1d5b2e4a")
               .user("0xca35b7d915458ef540ade6068dfe2f44e8fa733c")
               .resources("a2d602f3-5812-4879-9ed8-47b39200f61e")
               .data("\"{\\\"ext\\\":\\\"extention\\\"}\",\n")
               .localData("\"{\\\"local\\\":\\\"data\\\"}\",\n")
               .preEid("0x934afc51a516d8ea4bd03af6719b52c25f6e9f9755139e8c82e620b0a0114b7c")
               .build()
       )
    }
    @After
     void "sleep 40s waiting checking job finish"(){
        sleep(40000)
    }

}
