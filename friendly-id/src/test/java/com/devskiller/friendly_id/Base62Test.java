package com.devskiller.friendly_id;

import org.junit.Test;

import static com.devskiller.friendly_id.IdUtil.areEqualIgnoringLeadingZeros;
import static io.vavr.test.Property.def;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.util.Objects.areEqual;

public class Base62Test {

	@Test
	public void decodingValuePrefixedWithZeros() {
		assertThat(Base62.encode(Base62.decode("00001"))).isEqualTo("1");
		assertThat(Base62.encode(Base62.decode("01001"))).isEqualTo("1001");
		assertThat(Base62.encode(Base62.decode("00abcd"))).isEqualTo("abcd");
	}

	@Test
	public void shouldCheck128BitLimits() {
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
				Base62.decode("1Vkp6axDWu5pI3q1xQO3oO0"));
	}

	@Test
	public void decodingIdShouldBeReversible() {
		def("areEqualIgnoringLeadingZeros(Base62.toFriendlyId(Base62.toUuid(id)), id)")
				.forAll(DataProvider.FRIENDLY_IDS)
				.suchThat(id -> areEqualIgnoringLeadingZeros(Base62.encode(Base62.decode(id)), id))
				.check(24, 100_000)
				.assertIsSatisfied();
	}

	@Test
	public void encodingNumberShouldBeReversible() {
		def("areEqualIgnoringLeadingZeros(Base62.toFriendlyId(Base62.toUuid(id)), id)")
				.forAll(DataProvider.POSITIVE_BIG_INTEGERS)
				.suchThat(bigInteger -> areEqual(Base62.decode(Base62.encode(bigInteger)), bigInteger)
				)
				.check(-1, 100_000)
				.assertIsSatisfied();
	}

}
