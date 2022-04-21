import kotlin.random.Random
import kotlin.random.nextUInt

const val KOPECKS_IN_RUBLE = 100u

const val MAX_AMOUNT_FOR_MASTERCARD_MAESTRO = 7_500_000u
const val MIN_AMOUNT_FOR_VISA_AND_MIR = 3_600u
const val MIN_TAX_FOR_VISA_AND_MIR = 3_500u
const val TAX_FOR_VISA_AND_MIR = 0.0075

const val LIMIT_PER_CARD_PER_MONTH = 60_000_000u
const val LIMIT_VK_PAY_ONETIME = 1_500_000u
const val LIMIT_VK_PAY_PER_MONTH = 4_000_000u

fun main() {
    print("Введите сумму перевода в рублях: ")
    val paymentAmount: UInt = readln().toUInt() * KOPECKS_IN_RUBLE
    val cardType = CardTypes.values().random().name
    val monthlyPayments = Random.nextUInt(70_000_000u)

    println("\nПеревод в сумме: " + "%.2f".format(paymentAmount.toDouble() / KOPECKS_IN_RUBLE.toDouble()) + " руб.")
    println("Карта: $cardType")
    println("Сумма платежей за месяц: " + "%.2f".format(monthlyPayments.toDouble() / KOPECKS_IN_RUBLE.toDouble()) + " руб.")

    val commission = calculateCommission(cardType, monthlyPayments, paymentAmount) / KOPECKS_IN_RUBLE.toDouble()
    println("Ваша комиссия за перевод составила: " + "%.2f".format(commission) + " руб.")
}

fun calculateCommission(
    cardType: String,
    previousPayments: UInt = 0u,
    paymentAmount: UInt,
    isWithinAMonth: Boolean = true
): Double {

    println("Больше месяца? $isWithinAMonth")

    val amountForTaxEquals35 = MIN_TAX_FOR_VISA_AND_MIR.toDouble() / TAX_FOR_VISA_AND_MIR // 466 666.6666666667 копеек

    if (cardType != CardTypes.VKPAY.name && isWithinAMonth && (previousPayments + paymentAmount) > LIMIT_PER_CARD_PER_MONTH) {
        println("Вы превысили лимит переводов в календарном месяце!")
        return -1.0
    } else if (cardType == CardTypes.VKPAY.name && paymentAmount > LIMIT_VK_PAY_ONETIME) {
        println("Сумма перевода со счета VK Pay не должна превышать 15 000,00 руб.")
        return -1.0
    } else if (cardType == CardTypes.VKPAY.name && isWithinAMonth && (previousPayments + paymentAmount) > LIMIT_VK_PAY_PER_MONTH) {
        println("Вы превысили лимит переводов со счета VK Pay за календарный месяц!")
        return -1.0
    } else return when (cardType) {
        CardTypes.MASTERCARD.name, CardTypes.MAESTRO.name -> {
            if (isWithinAMonth && (previousPayments + paymentAmount) <= MAX_AMOUNT_FOR_MASTERCARD_MAESTRO)
                0.0
            else (paymentAmount.toDouble() * 0.006 + 2_000.0)
        }
        CardTypes.VISA.name, CardTypes.MIR.name -> {
            if (paymentAmount < MIN_AMOUNT_FOR_VISA_AND_MIR) {
                println("Минимальная сумма перевода 35 руб.")
                return -1.0
            } else {
                if (paymentAmount.toDouble() > amountForTaxEquals35) {
                    paymentAmount.toDouble() * TAX_FOR_VISA_AND_MIR
                } else {
                    MIN_TAX_FOR_VISA_AND_MIR.toDouble()
                }
            }
        }
        else -> 0.0
    }
}

enum class CardTypes {
    MASTERCARD,
    MAESTRO,
    VISA,
    MIR,
    VKPAY
}