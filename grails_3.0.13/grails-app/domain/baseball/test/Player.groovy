package baseball.test

class Player {

  String firstName
  String lastName
  String position

  static belongsTo = [team: Team]
  static hasOne = [contract: Contract]
}
