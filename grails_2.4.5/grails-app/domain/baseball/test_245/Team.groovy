package baseball.test_245

class Team {

  String name
  String city

  static hasMany = [players: Player]

    static constraints = {
    }
}
