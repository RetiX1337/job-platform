package com.butenov.jobplatform.commons;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

import lombok.extern.java.Log;

@Log
public class EntityFieldComparator
{

	/**
	 * Compares the fields of two objects dynamically based on the provided list of field names.
	 *
	 * @param existing      The existing object.
	 * @param updated       The updated object.
	 * @param fieldsToTrack List of field names to track for changes.
	 * @return true if any tracked field has changed; false otherwise.
	 */
	public static boolean hasSignificantChanges(final Object existing, final Object updated, final List<String> fieldsToTrack)
	{
		boolean hasChanges = false;

		for (final String fieldName : fieldsToTrack)
		{
			try
			{
				final Field field = existing.getClass().getDeclaredField(fieldName);
				field.setAccessible(true);

				final Object existingValue = field.get(existing);
				final Object updatedValue = field.get(updated);

				if (!Objects.equals(existingValue, updatedValue))
				{
					hasChanges = true;
				}
			}
			catch (NoSuchFieldException | IllegalAccessException e)
			{
				// Log an error or handle the case where the field does not exist
				log.warning("Error comparing field " + fieldName + ": " + e.getMessage());
			}
		}

		return hasChanges;
	}
}
