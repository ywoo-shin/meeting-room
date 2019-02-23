package com.pay.room.common.util;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.modelmapper.convention.MatchingStrategies.STRICT;

public class ModelMapperUtil {
	private ModelMapperUtil() {
		throw new AssertionError();
	}

	private static final Javers javers = JaversBuilder.javers().build();
	private static final ModelMapper MODEL_MAPPER;
	private static final ModelMapper SKIP_NULL_MODEL_MAPPER;

	static {
		MODEL_MAPPER = new ModelMapper();
		MODEL_MAPPER.getConfiguration()
					.setMatchingStrategy(STRICT);

		SKIP_NULL_MODEL_MAPPER = new ModelMapper();
		SKIP_NULL_MODEL_MAPPER.getConfiguration()
							  .setMatchingStrategy(STRICT)
							  .setPropertyCondition(Conditions.isNotNull());
	}

	public static ModelMapper getModelMapper() {
		return MODEL_MAPPER;
	}

	public static <D> D convertValue(Object source, Class<D> destinationType) {
		if (source != null) {
			return getModelMapper().map(source, destinationType);
		}
		return null;
	}

	public static <D> List<D> convertValue(List<?> source, Class<D> destinationType) {
		return Optional.ofNullable(source)
					   .map(List::stream)
					   .orElse(Stream.empty())
					   .map(value -> convertValue(value, destinationType))
					   .collect(Collectors.toList());
	}

	public static void mapSkipNull(Object source, Object destination) {
		if (source != null && destination != null) {
			SKIP_NULL_MODEL_MAPPER.map(source, destination);
		}
	}

	public static List<String> getDiffItems(Object object1, Object object2) {
		Diff diff = javers.compare(object1, object2);

		return Optional.ofNullable(diff.getChangesByType(ValueChange.class))
					   .map(List::stream)
					   .orElse(Stream.empty())
					   .map(change -> change.getPropertyName())
					   .collect(Collectors.toList());

	}
}
