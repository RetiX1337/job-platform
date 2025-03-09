package com.butenov.jobplatform.commons;

public interface Converter<E, D>
{
	E convertToEntity(final D dto);
	D convertToDto(final E entity);

}
