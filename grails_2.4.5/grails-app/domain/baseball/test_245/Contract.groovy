package baseball.test_245

class Contract {

  Date expiration
  BigDecimal salary

  static belongsTo = [player: Player]

    static constraints = {
    }
}
