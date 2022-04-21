import org.junit.Test

import org.junit.Assert.*

class MainKtTest {

    @Test
    fun mastercardMaestro_commissionIsZeroWhenMonthlyAmountIsUnder75kWithinAMonth() {
        // arrange
        val monthlyAmount = 4_500_000u // в копейках
        val paymentAmount = 2_500_000u // в копейках
        val isWithinAMonth = true
        val cardType = CardTypes.MAESTRO.name
        val expectedResult = 0.0

        // act
        val result = calculateCommission(
            cardType = cardType,
            previousPayments = monthlyAmount,
            paymentAmount = paymentAmount,
            isWithinAMonth = isWithinAMonth
        )

        // assert
        assertEquals(expectedResult, result, 1e-6)
    }

    @Test
    fun mastercardMaestro_commissionIsNotZeroWhenMonthlyAmountIsOver75kWithinAMonth() {
        // arrange
        val monthlyAmount = 5_500_000u // в копейках
        val paymentAmount = 2_500_000u // в копейках
        val isWithinAMonth = true
        val cardType = CardTypes.MAESTRO.name
        val expectedResult = 0.0

        // act
        val result = calculateCommission(
            cardType = cardType,
            previousPayments = monthlyAmount,
            paymentAmount = paymentAmount,
            isWithinAMonth = isWithinAMonth
        )

        // assert
        assertNotEquals(expectedResult, result, 1e-6)
    }

    @Test
    fun mastercardMaestro_commissionIsNotZeroWhenMonthlyAmountIsUnder75kNotWithinAMonth() {
        // arrange
        val monthlyAmount = 4_500_000u // в копейках
        val paymentAmount = 2_500_000u // в копейках
        val isWithinAMonth = false
        val cardType = CardTypes.MAESTRO.name
        val expectedResult = 0.0

        // act
        val result = calculateCommission(
            cardType = cardType,
            previousPayments = monthlyAmount,
            paymentAmount = paymentAmount,
            isWithinAMonth = isWithinAMonth
        )

        // assert
        assertNotEquals(expectedResult, result, 1e-6)
    }

    @Test
    fun mastercardMaestro_commissionIsNotZeroWhenMonthlyAmountIsOver75kNotWithinAMonth() {
        // arrange
        val monthlyAmount = 5_500_000u // в копейках
        val paymentAmount = 2_500_000u // в копейках
        val isWithinAMonth = false
        val cardType = CardTypes.MAESTRO.name
        val expectedResult = 0.0

        // act
        val result = calculateCommission(
            cardType = cardType,
            previousPayments = monthlyAmount,
            paymentAmount = paymentAmount,
            isWithinAMonth = isWithinAMonth
        )

        // assert
        assertNotEquals(expectedResult, result, 1e-6)
    }

    @Test
    fun visaMir_commissionIs35WhenPaymentAmountIsLessThan4666() {
        // arrange
        val paymentAmount = 400_000u // в копейках
        val cardType = CardTypes.VISA.name
        val expectedResult = 3_500.0 // в копейках

        // act
        val result = calculateCommission(
            cardType = cardType,
            paymentAmount = paymentAmount
        )

        // assert
        assertEquals(expectedResult, result, 1e-6)
    }

    @Test
    fun visaMir_commissionIsMore35WhenPaymentAmountIsMoreThan4666() {
        // arrange
        val paymentAmount = 500_000u // в копейках
        val cardType = CardTypes.VISA.name
        val expectedResult = 3_750.0 // в копейках

        // act
        val result = calculateCommission(
            cardType = cardType,
            paymentAmount = paymentAmount
        )

        // assert
        assertEquals(expectedResult, result, 1e-6)
    }

    @Test
    fun vkPay_commissionIsZeroWhenPaymentAmountIsNotMoreThan15kAndNotMoreThan40kPerMonth() {
        // arrange
        val cardType = CardTypes.VKPAY.name
        val paymentAmount = 1_000_000u // в копейках
        val monthlyAmount = 2_000_000u // в копейках
        val expectedResult = 0.0 // в копейках

        // act
        val result = calculateCommission(
            cardType = cardType,
            paymentAmount = paymentAmount,
            previousPayments = monthlyAmount
        )

        // assert
        assertEquals(expectedResult, result, 1e-6)
    }

    @Test
    fun vkPay_commissionIsMinus1WhenPaymentAmountIsNotMoreThan15kButMoreThan40kPerMonth() {
        // arrange
        val cardType = CardTypes.VKPAY.name
        val paymentAmount = 1_500_000u // в копейках
        val monthlyAmount = 3_800_000u // в копейках
        val expectedResult = -1.0

        // act
        val result = calculateCommission(
            cardType = cardType,
            paymentAmount = paymentAmount,
            previousPayments = monthlyAmount
        )

        // assert
        assertEquals(expectedResult, result, 1e-6)
    }

    @Test
    fun vkPay_commissionIsMinus1WhenPaymentAmountIsMoreThan15kButNotMoreThan40kPerMonth() {
        // arrange
        val cardType = CardTypes.VKPAY.name
        val paymentAmount = 1_500_001u // в копейках
        val monthlyAmount = 2_500_000u // в копейках
        val expectedResult = -1.0

        // act
        val result = calculateCommission(
            cardType = cardType,
            paymentAmount = paymentAmount,
            previousPayments = monthlyAmount
        )

        // assert
        assertEquals(expectedResult, result, 1e-6)
    }

    @Test
    fun maestroMastercardVisaMir_commissionIsMinus1WhenPaymentAmountIsMoreThan600kPerMonth() {
        // arrange
        val cardType = CardTypes.VISA.name
        val paymentAmount = 2_000_001u // в копейках
        val monthlyAmount = 58_000_000u // в копейках
        val expectedResult = -1.0

        // act
        val result = calculateCommission(
            cardType = cardType,
            paymentAmount = paymentAmount,
            previousPayments = monthlyAmount
        )

        // assert
        assertEquals(expectedResult, result, 1e-6)
    }

    @Test
    fun visaMir_commissionIsMinus1WhenPaymentAmountIsLessThan35() {
        // arrange
        val cardType = CardTypes.VISA.name
        val paymentAmount = 3_599u // в копейках
        val expectedResult = -1.0

        // act
        val result = calculateCommission(
            cardType = cardType,
            paymentAmount = paymentAmount
        )

        // assert
        assertEquals(expectedResult, result, 1e-6)
    }
}