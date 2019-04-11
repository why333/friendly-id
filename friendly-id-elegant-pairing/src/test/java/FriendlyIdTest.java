import io.vavr.test.Arbitrary;
import org.junit.Test;

import com.devskiller.friendly_id.DataProvider;
import com.devskiller.friendly_id.FriendlyId;

import static com.devskiller.friendly_id.FriendlyId.toFriendlyId;
import static com.devskiller.friendly_id.FriendlyId.toUuid;
import static com.devskiller.friendly_id.IdUtil.areEqualIgnoringLeadingZeros;
import static io.vavr.test.Property.def;
import static org.assertj.core.util.Objects.areEqual;

public class FriendlyIdTest {

	private static final int NUMBER_OF_ATTEMPS = 10_000;

	@Test
	public void name() {
		FriendlyId.createFriendlyId();
	}

	@Test
	public void shouldCreateValidIdsThatConformToUuidType4() {
		def("areEqual(FriendlyId.toUuid(FriendlyId.toFriendlyId(uuid))), uuid)")
				.forAll(Arbitrary.integer())
				.suchThat(ignored -> toUuid(FriendlyId.createFriendlyId()).version() == 4)
				.check(-1, NUMBER_OF_ATTEMPS)
				.assertIsSatisfied();
	}

	@Test
	public void encodingUuidShouldBeReversible() {
		def("areEqual(FriendlyId.toUuid(FriendlyId.toFriendlyId(uuid))), uuid)")
				.forAll(DataProvider.UUIDS)
				.suchThat(uuid -> areEqual(toUuid(toFriendlyId(uuid)), uuid))
				.check(-1, 100_000)
				.assertIsSatisfied();
	}

	@Test
	public void decodingIdShouldBeReversible() {
		def("areEqualIgnoringLeadingZeros(Url62.toFriendlyId(Url62.toUuid(id)), id)")
				.forAll(DataProvider.FRIENDLY_IDS)
				.suchThat(id -> areEqualIgnoringLeadingZeros(toFriendlyId(toUuid(id)), id))
				.check(100, 100_000)
				.assertIsSatisfied();
	}

}