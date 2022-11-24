package org.optaplanner.core.api.score.buildin.hardmediumsoftbigdecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.optaplanner.core.api.score.buildin.AbstractScoreTest;
import org.optaplanner.core.impl.testdata.util.PlannerAssert;

class HardMediumSoftBigDecimalScoreTest extends AbstractScoreTest {

    @Test
    void of() {
        assertThat(HardMediumSoftBigDecimalScore.ofHard(new BigDecimal("-147.2"))).isEqualTo(
                HardMediumSoftBigDecimalScore.of(new BigDecimal("-147.2"), new BigDecimal("0.0"), new BigDecimal("0.0")));
        assertThat(HardMediumSoftBigDecimalScore.ofMedium(new BigDecimal("-3.2"))).isEqualTo(
                HardMediumSoftBigDecimalScore.of(new BigDecimal("-0.0"), new BigDecimal("-3.2"), new BigDecimal("0.0")));
        assertThat(HardMediumSoftBigDecimalScore.ofSoft(new BigDecimal("-258.3"))).isEqualTo(
                HardMediumSoftBigDecimalScore.of(new BigDecimal("0.0"), new BigDecimal("0.0"), new BigDecimal("-258.3")));
    }

    @Test
    void parseScore() {
        assertThat(HardMediumSoftBigDecimalScore.parseScore("-147.2hard/-3.2medium/-258.3soft")).isEqualTo(
                HardMediumSoftBigDecimalScore.of(new BigDecimal("-147.2"), new BigDecimal("-3.2"), new BigDecimal("-258.3")));
        assertThat(HardMediumSoftBigDecimalScore.parseScore("-7init/-147.2hard/-3.2medium/-258.3soft"))
                .isEqualTo(HardMediumSoftBigDecimalScore.ofUninitialized(-7, new BigDecimal("-147.2"), new BigDecimal("-3.2"),
                        new BigDecimal("-258.3")));
    }

    @Test
    void toShortString() {
        assertThat(HardMediumSoftBigDecimalScore.of(new BigDecimal("0.0"), new BigDecimal("0.0"), new BigDecimal("0.0"))
                .toShortString()).isEqualTo("0");
        assertThat(HardMediumSoftBigDecimalScore.of(new BigDecimal("0.0"), new BigDecimal("0.0"), new BigDecimal("-258.3"))
                .toShortString()).isEqualTo("-258.3soft");
        assertThat(HardMediumSoftBigDecimalScore.of(new BigDecimal("0.0"), new BigDecimal("-3.20"), new BigDecimal("0.0"))
                .toShortString()).isEqualTo("-3.20medium");
        assertThat(HardMediumSoftBigDecimalScore.of(new BigDecimal("0.0"), new BigDecimal("-3.20"), new BigDecimal("-258.3"))
                .toShortString()).isEqualTo("-3.20medium/-258.3soft");
        assertThat(HardMediumSoftBigDecimalScore.of(new BigDecimal("-147.2"), new BigDecimal("-3.20"), new BigDecimal("-258.3"))
                .toShortString()).isEqualTo("-147.2hard/-3.20medium/-258.3soft");
        assertThat(HardMediumSoftBigDecimalScore
                .ofUninitialized(-7, new BigDecimal("0.0"), new BigDecimal("0.0"), new BigDecimal("0.0"))
                .toShortString()).isEqualTo("-7init");
        assertThat(HardMediumSoftBigDecimalScore
                .ofUninitialized(-7, new BigDecimal("-147.2"), new BigDecimal("-3.20"), new BigDecimal("-258.3"))
                .toShortString()).isEqualTo("-7init/-147.2hard/-3.20medium/-258.3soft");
    }

    @Test
    void testToString() {
        assertThat(HardMediumSoftBigDecimalScore.of(new BigDecimal("0.0"), new BigDecimal("-3.20"), new BigDecimal("-258.3")))
                .hasToString("0.0hard/-3.20medium/-258.3soft");
        assertThat(HardMediumSoftBigDecimalScore
                .of(new BigDecimal("-147.2"), new BigDecimal("-3.20"), new BigDecimal("-258.3")))
                        .hasToString("-147.2hard/-3.20medium/-258.3soft");
        assertThat(HardMediumSoftBigDecimalScore
                .ofUninitialized(-7, new BigDecimal("-147.2"), new BigDecimal("-3.20"), new BigDecimal("-258.3")))
                        .hasToString("-7init/-147.2hard/-3.20medium/-258.3soft");
    }

    @Test
    void parseScoreIllegalArgument() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> HardMediumSoftBigDecimalScore.parseScore("-147.2"));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> HardMediumSoftBigDecimalScore.parseScore("-147.2hard/-258.3soft"));
    }

    @Test
    void withInitScore() {
        assertThat(HardMediumSoftBigDecimalScore.of(new BigDecimal("-147.2"), new BigDecimal("-3.20"), new BigDecimal("-258.3"))
                .withInitScore(-7)).isEqualTo(
                        HardMediumSoftBigDecimalScore.ofUninitialized(-7, new BigDecimal("-147.2"), new BigDecimal("-3.20"),
                                new BigDecimal("-258.3")));
    }

    @Test
    void feasible() {
        assertScoreNotFeasible(
                HardMediumSoftBigDecimalScore.of(new BigDecimal("-5"), new BigDecimal("-3.20"), new BigDecimal("-300")),
                HardMediumSoftBigDecimalScore.of(new BigDecimal("-5"), new BigDecimal("3.20"), new BigDecimal("4000")),
                HardMediumSoftBigDecimalScore.of(new BigDecimal("-0.007"), new BigDecimal("-3.20"), new BigDecimal("4000")),
                HardMediumSoftBigDecimalScore.ofUninitialized(-7, new BigDecimal("-5"), new BigDecimal("-32"),
                        new BigDecimal("-300")),
                HardMediumSoftBigDecimalScore.ofUninitialized(-7, new BigDecimal("0"), new BigDecimal("32"),
                        new BigDecimal("-300")));
        assertScoreFeasible(
                HardMediumSoftBigDecimalScore.of(new BigDecimal("0"), new BigDecimal("-32"), new BigDecimal("-300.007")),
                HardMediumSoftBigDecimalScore.of(new BigDecimal("0"), new BigDecimal("-32.3"), new BigDecimal("-300")),
                HardMediumSoftBigDecimalScore.of(new BigDecimal("2"), new BigDecimal("-7.3"), new BigDecimal("-300")),
                HardMediumSoftBigDecimalScore.ofUninitialized(0, new BigDecimal("0"), new BigDecimal("-321"),
                        new BigDecimal("-300")));
    }

    @Test
    void add() {
        assertThat(HardMediumSoftBigDecimalScore.of(new BigDecimal("20"), new BigDecimal("-32"), new BigDecimal("-20")).add(
                HardMediumSoftBigDecimalScore.of(new BigDecimal("-1"), new BigDecimal("27"), new BigDecimal("-300"))))
                        .isEqualTo(HardMediumSoftBigDecimalScore.of(new BigDecimal("19"), new BigDecimal("-5"),
                                new BigDecimal("-320")));
        assertThat(HardMediumSoftBigDecimalScore
                .ofUninitialized(-70, new BigDecimal("20"), new BigDecimal("-20"), new BigDecimal("-20")).add(
                        HardMediumSoftBigDecimalScore.ofUninitialized(-7, new BigDecimal("-1"), new BigDecimal("-12"),
                                new BigDecimal("-300"))))
                                        .isEqualTo(HardMediumSoftBigDecimalScore.ofUninitialized(-77, new BigDecimal("19"),
                                                new BigDecimal("-32"),
                                                new BigDecimal("-320")));
    }

    @Test
    void subtract() {
        assertThat(
                HardMediumSoftBigDecimalScore.of(new BigDecimal("20"), new BigDecimal("-30"), new BigDecimal("-20")).subtract(
                        HardMediumSoftBigDecimalScore.of(new BigDecimal("-1"), new BigDecimal("2"), new BigDecimal("-300"))))
                                .isEqualTo(HardMediumSoftBigDecimalScore.of(new BigDecimal("21"), new BigDecimal("-32"),
                                        new BigDecimal("280")));
        assertThat(HardMediumSoftBigDecimalScore
                .ofUninitialized(-70, new BigDecimal("20"), new BigDecimal("-32"), new BigDecimal("-20")).subtract(
                        HardMediumSoftBigDecimalScore.ofUninitialized(-7, new BigDecimal("-1"), new BigDecimal("-10"),
                                new BigDecimal("-300"))))
                                        .isEqualTo(HardMediumSoftBigDecimalScore.ofUninitialized(-63, new BigDecimal("21"),
                                                new BigDecimal("-22"),
                                                new BigDecimal("280")));
    }

    @Test
    void multiply() {
        assertThat(HardMediumSoftBigDecimalScore.of(new BigDecimal("5.0"), new BigDecimal("-4.0"), new BigDecimal("-5.0"))
                .multiply(1.2))
                        .isEqualTo(HardMediumSoftBigDecimalScore.of(new BigDecimal("6.0"), new BigDecimal("-4.8"),
                                new BigDecimal("-6.0")));
        assertThat(HardMediumSoftBigDecimalScore.of(new BigDecimal("1.0"), new BigDecimal("-2.0"), new BigDecimal("-1.0"))
                .multiply(1.2))
                        .isEqualTo(HardMediumSoftBigDecimalScore.of(new BigDecimal("1.2"), new BigDecimal("-2.4"),
                                new BigDecimal("-1.2")));
        assertThat(HardMediumSoftBigDecimalScore.of(new BigDecimal("4.0"), new BigDecimal("-8.0"), new BigDecimal("-4.0"))
                .multiply(1.2))
                        .isEqualTo(HardMediumSoftBigDecimalScore.of(new BigDecimal("4.8"), new BigDecimal("-9.6"),
                                new BigDecimal("-4.8")));
        assertThat(HardMediumSoftBigDecimalScore
                .ofUninitialized(-7, new BigDecimal("4.3"), new BigDecimal("2.0"), new BigDecimal("-5.2"))
                .multiply(2.0)).isEqualTo(
                        HardMediumSoftBigDecimalScore.ofUninitialized(-14, new BigDecimal("8.6"), new BigDecimal("4.0"),
                                new BigDecimal("-10.4")));
    }

    @Test
    void divide() {
        assertThat(HardMediumSoftBigDecimalScore.of(new BigDecimal("25.0"), new BigDecimal("-50.0"), new BigDecimal("-25.0"))
                .divide(5.0))
                        .isEqualTo(HardMediumSoftBigDecimalScore.of(new BigDecimal("5.0"), new BigDecimal("-10.0"),
                                new BigDecimal("-5.0")));
        assertThat(HardMediumSoftBigDecimalScore.of(new BigDecimal("21.0"), new BigDecimal("-10.5"), new BigDecimal("-21.0"))
                .divide(5.0))
                        .isEqualTo(HardMediumSoftBigDecimalScore.of(new BigDecimal("4.2"), new BigDecimal("-2.1"),
                                new BigDecimal("-4.2")));
        assertThat(HardMediumSoftBigDecimalScore.of(new BigDecimal("24.0"), new BigDecimal("12.0"), new BigDecimal("-24.0"))
                .divide(5.0)).isEqualTo(
                        HardMediumSoftBigDecimalScore.of(new BigDecimal("4.8"), new BigDecimal("2.4"), new BigDecimal("-4.8")));
        assertThat(HardMediumSoftBigDecimalScore
                .ofUninitialized(-14, new BigDecimal("8.6"), new BigDecimal("-50.6"), new BigDecimal("-10.4"))
                .divide(2.0)).isEqualTo(
                        HardMediumSoftBigDecimalScore.ofUninitialized(-7, new BigDecimal("4.3"), new BigDecimal("-25.3"),
                                new BigDecimal("-5.2")));
    }

    @Test
    void power() {
        assertThat(HardMediumSoftBigDecimalScore.of(new BigDecimal("-4.0"), new BigDecimal("-8.0"), new BigDecimal("5.0"))
                .power(2.0))
                        .isEqualTo(HardMediumSoftBigDecimalScore.of(new BigDecimal("16.0"), new BigDecimal("64.0"),
                                new BigDecimal("25.0")));
        assertThat(HardMediumSoftBigDecimalScore
                .ofUninitialized(-7, new BigDecimal("-4.0"), new BigDecimal("-3.0"), new BigDecimal("5.0")).power(3.0))
                        .isEqualTo(HardMediumSoftBigDecimalScore.ofUninitialized(-343, new BigDecimal("-64.0"),
                                new BigDecimal("-27.0"),
                                new BigDecimal("125.0")));
    }

    @Test
    void negate() {
        assertThat(HardMediumSoftBigDecimalScore.of(new BigDecimal("4.0"), new BigDecimal("25.0"), new BigDecimal("-5.0"))
                .negate())
                        .isEqualTo(HardMediumSoftBigDecimalScore.of(new BigDecimal("-4.0"), new BigDecimal("-25.0"),
                                new BigDecimal("5.0")));
        assertThat(HardMediumSoftBigDecimalScore.of(new BigDecimal("-4.0"), new BigDecimal("3.0"), new BigDecimal("5.0"))
                .negate())
                        .isEqualTo(HardMediumSoftBigDecimalScore.of(new BigDecimal("4.0"), new BigDecimal("-3.0"),
                                new BigDecimal("-5.0")));
    }

    @Test
    void abs() {
        assertThat(HardMediumSoftBigDecimalScore.of(new BigDecimal("4.0"), new BigDecimal("25.0"), new BigDecimal("5.0"))
                .abs())
                        .isEqualTo(HardMediumSoftBigDecimalScore.of(new BigDecimal("4.0"), new BigDecimal("25.0"),
                                new BigDecimal("5.0")));
        assertThat(HardMediumSoftBigDecimalScore.of(new BigDecimal("4.0"), new BigDecimal("25.0"), new BigDecimal("-5.0"))
                .abs())
                        .isEqualTo(HardMediumSoftBigDecimalScore.of(new BigDecimal("4.0"), new BigDecimal("25.0"),
                                new BigDecimal("5.0")));
        assertThat(HardMediumSoftBigDecimalScore.of(new BigDecimal("-4.0"), new BigDecimal("3.0"), new BigDecimal("5.0"))
                .abs())
                        .isEqualTo(HardMediumSoftBigDecimalScore.of(new BigDecimal("4.0"), new BigDecimal("3.0"),
                                new BigDecimal("5.0")));
        assertThat(HardMediumSoftBigDecimalScore.of(new BigDecimal("-4.0"), new BigDecimal("-3.0"), new BigDecimal("-5.0"))
                .abs())
                        .isEqualTo(HardMediumSoftBigDecimalScore.of(new BigDecimal("4.0"), new BigDecimal("3.0"),
                                new BigDecimal("5.0")));
    }

    @Test
    void zero() {
        HardMediumSoftBigDecimalScore manualZero =
                HardMediumSoftBigDecimalScore.of(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(manualZero.zero()).isEqualTo(manualZero);
            softly.assertThat(manualZero.isZero()).isTrue();
            HardMediumSoftBigDecimalScore manualOne =
                    HardMediumSoftBigDecimalScore.of(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ONE);
            softly.assertThat(manualOne.isZero()).isFalse();
        });
    }

    @Test
    void equalsAndHashCode() {
        PlannerAssert.assertObjectsAreEqual(
                HardMediumSoftBigDecimalScore.of(new BigDecimal("-10.0"), new BigDecimal("3.0"), new BigDecimal("-200.0")),
                HardMediumSoftBigDecimalScore.of(new BigDecimal("-10.0"), new BigDecimal("3.0"), new BigDecimal("-200.0")),
                HardMediumSoftBigDecimalScore.of(new BigDecimal("-10.000"), new BigDecimal("3.000"),
                        new BigDecimal("-200.000")),
                HardMediumSoftBigDecimalScore.ofUninitialized(0, new BigDecimal("-10.0"), new BigDecimal("3.0"),
                        new BigDecimal("-200.0")));
        PlannerAssert.assertObjectsAreEqual(
                HardMediumSoftBigDecimalScore.ofUninitialized(-7, new BigDecimal("-10.0"), new BigDecimal("3.0"),
                        new BigDecimal("-200.0")),
                HardMediumSoftBigDecimalScore.ofUninitialized(-7, new BigDecimal("-10.0"), new BigDecimal("3.0"),
                        new BigDecimal("-200.0")));
        PlannerAssert.assertObjectsAreNotEqual(
                HardMediumSoftBigDecimalScore.of(new BigDecimal("-10.0"), new BigDecimal("-30.0"), new BigDecimal("-200.0")),
                HardMediumSoftBigDecimalScore.of(new BigDecimal("-30.0"), new BigDecimal("-30.0"), new BigDecimal("-200.0")),
                HardMediumSoftBigDecimalScore.of(new BigDecimal("-10.0"), new BigDecimal("-10.0"), new BigDecimal("-200.0")),
                HardMediumSoftBigDecimalScore.of(new BigDecimal("-10.0"), new BigDecimal("-30.0"), new BigDecimal("-400.0")),
                HardMediumSoftBigDecimalScore.of(new BigDecimal("-10.0"), new BigDecimal("-400.0"), new BigDecimal("-30.0")),
                HardMediumSoftBigDecimalScore.ofUninitialized(-7, new BigDecimal("-10.0"), new BigDecimal("-30.0"),
                        new BigDecimal("-200.0")));
    }

    @Test
    void compareTo() {
        PlannerAssert.assertCompareToOrder(
                HardMediumSoftBigDecimalScore.ofUninitialized(-8, new BigDecimal("0"), new BigDecimal("0"),
                        new BigDecimal("0")),
                HardMediumSoftBigDecimalScore.ofUninitialized(-7, new BigDecimal("-20"), new BigDecimal("0"),
                        new BigDecimal("-20")),
                HardMediumSoftBigDecimalScore.ofUninitialized(-7, new BigDecimal("-1"), new BigDecimal("-30"),
                        new BigDecimal("-300")),
                HardMediumSoftBigDecimalScore.ofUninitialized(-7, new BigDecimal("-1"), new BigDecimal("-20.0"),
                        new BigDecimal("-300")),
                HardMediumSoftBigDecimalScore.ofUninitialized(-7, new BigDecimal("0"), new BigDecimal("-10.0"),
                        new BigDecimal("0")),
                HardMediumSoftBigDecimalScore.ofUninitialized(-7, new BigDecimal("0"), new BigDecimal("-2.0"),
                        new BigDecimal("0")),
                HardMediumSoftBigDecimalScore.ofUninitialized(-7, new BigDecimal("0"), new BigDecimal("0"),
                        new BigDecimal("0")),
                HardMediumSoftBigDecimalScore.ofUninitialized(-7, new BigDecimal("0"), new BigDecimal("0"),
                        new BigDecimal("1")),
                HardMediumSoftBigDecimalScore.of(new BigDecimal("-20.06"), new BigDecimal("-2.3"), new BigDecimal("-20")),
                HardMediumSoftBigDecimalScore.of(new BigDecimal("-20.06"), new BigDecimal("0"), new BigDecimal("-20")),
                HardMediumSoftBigDecimalScore.of(new BigDecimal("-20.007"), new BigDecimal("-2.3"), new BigDecimal("-20")),
                HardMediumSoftBigDecimalScore.of(new BigDecimal("-20.007"), new BigDecimal("-2.03"), new BigDecimal("-20")),
                HardMediumSoftBigDecimalScore.of(new BigDecimal("-20.007"), new BigDecimal("0"), new BigDecimal("-20")),
                HardMediumSoftBigDecimalScore.of(new BigDecimal("-20.007"), new BigDecimal("2.3"), new BigDecimal("-20")),
                HardMediumSoftBigDecimalScore.of(new BigDecimal("-20"), new BigDecimal("0"), new BigDecimal("-20.06")),
                HardMediumSoftBigDecimalScore.of(new BigDecimal("-20"), new BigDecimal("0"), new BigDecimal("-20.007")),
                HardMediumSoftBigDecimalScore.of(new BigDecimal("-20"), new BigDecimal("0"), new BigDecimal("-20")),
                HardMediumSoftBigDecimalScore.of(new BigDecimal("-1"), new BigDecimal("-30"), new BigDecimal("-300")),
                HardMediumSoftBigDecimalScore.of(new BigDecimal("-1"), new BigDecimal("-20"), new BigDecimal("-300")),
                HardMediumSoftBigDecimalScore.of(new BigDecimal("-1"), new BigDecimal("0"), new BigDecimal("-300")),
                HardMediumSoftBigDecimalScore.of(new BigDecimal("-1"), new BigDecimal("1"), new BigDecimal("-300")),
                HardMediumSoftBigDecimalScore.of(new BigDecimal("-1"), new BigDecimal("1"), new BigDecimal("4000")),
                HardMediumSoftBigDecimalScore.of(new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("-1")),
                HardMediumSoftBigDecimalScore.of(new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0")),
                HardMediumSoftBigDecimalScore.of(new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("1")));
    }
}
