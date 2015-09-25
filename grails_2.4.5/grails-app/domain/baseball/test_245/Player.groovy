package baseball.test_245

class Player {

  String firstName
  String lastName
  String position

  static belongsTo = [team: Team]
  static hasOne = [contract: Contract]
}
