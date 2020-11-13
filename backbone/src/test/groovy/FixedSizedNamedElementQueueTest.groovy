package groovy

import com.fuyun.chain.api.model.Daily
import com.fuyun.chain.api.util.FixedSizedNamedElementQueue
import spock.lang.Specification

class FixedSizedNamedElementQueueTest extends Specification {
    def "Push"() {
        given:
        def queue = new FixedSizedNamedElementQueue(3)
        queue.push(new Daily("1",1,1))
        queue.push(new Daily("2",2,2))
        queue.push(new Daily("3",3,3))
        queue.push(new Daily("4",4,4))
        queue.push(new Daily("5",5,5))

        expect:
        queue.getList().get(2).getName() == '3'
    }

}
