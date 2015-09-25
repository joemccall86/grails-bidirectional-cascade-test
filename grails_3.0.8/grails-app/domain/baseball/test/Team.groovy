package baseball.test

class Team {

  String name
  String city

  static hasMany = [players: Player]

    static constraints = {
    }
}
