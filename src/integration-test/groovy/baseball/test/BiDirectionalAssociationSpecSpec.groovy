package baseball.test


import grails.test.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*

@Integration
@Rollback
class BiDirectionalAssociationSpecSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    def 'a valid team is bootstrapped into the test'() {
      given: 'test data is setup'
      setupData()

      expect: 'the team exists'
      Team.count() == 1
    }

    def setupData() {
      Team padres = new Team(
          name: "Padres",
          city: "San Diego"
      )

      padres.addToPlayers(new Player(
          firstName: "John",
          lastName: "Doe",
          position: "Pitcher",
          contract: new Contract(
              expiration: new Date(),
              salary: 40_000_000
          )
      ))

      // Desired behavior: Team cascades saves down to Player, which
      // cascades its saves down to Contract
      padres.save(flush: true)
    }
}
