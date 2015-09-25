package baseball.test

class Contract {

  Date expiration
  BigDecimal salary

  static belongsTo = [player: Player]

    static constraints = {
    }
}
